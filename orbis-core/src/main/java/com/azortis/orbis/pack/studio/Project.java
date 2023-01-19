/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2023 Azortis
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.azortis.orbis.pack.studio;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.Registry;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.pack.data.Component;
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.pack.studio.schema.SchemaManager;
import com.azortis.orbis.util.Scheduler;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.kyori.adventure.key.Key;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.Semaphore;

/**
 * A representation of a studio project environment, manages all things from Schema generation and
 * mapping the schema to the right files, and forces entries schema's to regenerate when a new config data file
 * has been added, to make sure it can be referenced in other files.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
public final class Project {

    private final String name;
    private final File directory;
    private boolean closed = false;

    private final StudioDataAccess dataAccess;
    private final SchemaManager schemaManager;
    private final WorkspaceConfig workspaceConfig;
    private final File settingsDir;
    private final ProjectWatcher watcher;
    private final StudioWorld studioWorld;

    private volatile Scheduler.Task hotReloadTask = null;

    Project(@NotNull String name, @NotNull File directory, boolean withWorld) throws IOException {
        this.name = name;
        this.directory = directory.getAbsoluteFile();

        // Create the settings folder before creating the schema manager
        this.settingsDir = new File(this.directory + "/.orbis/");
        if (!settingsDir.exists() && !settingsDir.mkdirs())
            Orbis.getLogger().error("Failed to create /.orbis/ settings directory for {}", name);

        this.dataAccess = new StudioDataAccess(directory);
        this.schemaManager = new SchemaManager(this);

        // Initialize code-workspace config for VSCode completions
        File workspaceFile = new File(this.directory + "/" + name + ".code-workspace");
        defaultWorkspace:
        {
            if (workspaceFile.exists()) {
                JsonObject existingWorkspace = Orbis.getGson().fromJson(new FileReader(workspaceFile), JsonObject.class);
                if (existingWorkspace != null) {
                    this.workspaceConfig = new WorkspaceConfig(existingWorkspace.getAsJsonObject("settings"),
                            this, workspaceFile);
                    break defaultWorkspace;
                }
            }
            this.workspaceConfig = new WorkspaceConfig(WorkspaceConfig.DEFAULT_SETTINGS, this, workspaceFile);
        }

        // Create a studio world, so we have a data access point.
        if (withWorld) {
            this.studioWorld = Orbis.getPlatform().createStudioWorld(this);
            this.studioWorld.initialize();
        } else {
            studioWorld = null;
        }

        this.watcher = new ProjectWatcher(this);

        // Open VSCode environment if the user has it enabled.
        if (Orbis.getSettings().studio().openVSCode() && SystemUtils.IS_OS_WINDOWS) {
            Orbis.getLogger().info("Opening VSCode. You may see output from VSCode, just ignore it.");
            Desktop.getDesktop().open(workspaceFile);
        }
    }

    public String name() {
        return name;
    }

    public File directory() {
        return directory;
    }

    public boolean isClosed() {
        return closed;
    }

    public SchemaManager schemaManager() {
        return schemaManager;
    }

    public StudioDataAccess dataAccess() {
        return dataAccess;
    }

    public WorkspaceConfig workspaceConfig() {
        return workspaceConfig;
    }

    public File settingsDir() {
        return settingsDir;
    }

    public boolean hasWorld() {
        return studioWorld != null;
    }

    public @NotNull StudioWorld studioWorld() throws IllegalStateException {
        Preconditions.checkState(hasWorld(), "This project doesn't use a studio world!");
        return studioWorld;
    }

    boolean close() {
        if (!closed) {
            watcher.terminate();
            Orbis.getLogger().info("Closing project {}, clearing viewers...", name);
            studioWorld.clearViewers();
            Orbis.getLogger().info("Unloading studio world...");
            if (studioWorld.unload()) {
                Orbis.getLogger().info("Successfully closed project!");
                closed = true;
                return true;
            }
        }
        return false;
    }

    //
    // File change events
    //

    // To make sure only one call happens at a time.
    private final Semaphore lock = new Semaphore(1);

    /**
     * Called when a new directory has been created. It will re-index the studio data access
     * so the directory gets a type assigned to it. If the new directory is a component root directory then it will
     * call the {@link SchemaManager} to generate the schema's for that component instance and {@link WorkspaceConfig}
     * to assign them.
     *
     * @param directory The path of the directory.
     */
    @Blocking
    void onDirectoryCreate(@NotNull Path directory) {
        try {
            lock.acquire();
            // We have to re-index the entirety of the DataAccess, but not WorkspaceConfig
            // This is because created directories do not influence existing matching rules.
            // If said created directory is a new component root, we manually register it without re-indexing the rest
            dataAccess.reset();

            if (dataAccess.isComponentRoot(directory)) {
                Class<?> generatorType = dataAccess.getType(directory.getParent());
                Class<?> componentType = dataAccess.getComponentType(directory);
                String componentName = directory.getFileName().toString().replaceAll("/", "").trim();
                schemaManager.createComponentSchemas(componentType, generatorType, componentName);
                workspaceConfig.registerComponent(generatorType, directory, componentName);
            } else if (dataAccess.hasType(directory)) {
                Class<?> type = dataAccess.getType(directory);

                if (dataAccess.isComponentType(type)) {
                    String componentName = dataAccess.getComponentName(directory);
                    workspaceConfig.addDirectory(type, componentName, directory);
                } else {
                    workspaceConfig.addDirectory(type, directory);
                }
                workspaceConfig.save();
            }
            doHotReload();
            lock.release();
        } catch (InterruptedException ex) {
            Orbis.getLogger().error("Failed to process directory create event for {}", directory);
            lock.release();
        }
    }

    @Blocking
    void onFileCreate(@NotNull Path file) {
        try {
            // Only requires re-indexing of a Registry definition if the file is not applicable for component status
            // otherwise it'd need to re-index DataAccess, component schemas and the WorkspaceConfig.
            lock.acquire();
            if (DataAccess.GENERATOR_TYPES.containsKey(dataAccess.getType(file.getParent()))) {
                dataAccess.reset();
                if (dataAccess.isComponentFile(file)) {
                    schemaManager.reset();
                    workspaceConfig.resetComponents();
                    workspaceConfig.save();
                }
            }

            // Reset the entries for the file type
            if (dataAccess.hasType(file.getParent())) {
                Class<?> type = dataAccess.getType(file.getParent());
                if (type == Dimension.class) {
                    workspaceConfig.reset();
                    workspaceConfig.save();
                } else if (dataAccess.isComponentType(type)) {
                    schemaManager.resetEntries(type, dataAccess.getComponentName(file.getParent()));
                } else {
                    schemaManager.resetEntries(type);
                }
            }
            doHotReload();
            lock.release();
        } catch (InterruptedException ex) {
            Orbis.getLogger().error("Failed to process file create event for {}", directory);
            lock.release();
        }
    }

    @Blocking
    @SuppressWarnings("PatternValidation")
    void onFileModify(@NotNull Path file) {
        try {
            lock.acquire();

            if (dataAccess().hasType(file.getParent())) {
                Class<?> dataType = dataAccess.getType(file.getParent());

                // Changes made to the type of data classes that support components have to be processed properly
                // in order for the schema's to properly match against the changed type
                if (DataAccess.GENERATOR_TYPES.containsKey(dataType)) {
                    try {
                        JsonObject generatorObject = Orbis.getGson().fromJson(new FileReader(file.toFile()), JsonObject.class);

                        // Make sure the file actually has a type specified.
                        if (generatorObject.has("type")) {
                            Key key = Key.key(generatorObject.get("type").getAsString());

                            // Check if the file is already registered as a component file
                            if (dataAccess.isComponentFile(file)) {
                                // If the stored implementation type doesn't coincide anymore with the current type
                                // reset the system so that it can be figured out properly.
                                if (!key.equals(dataAccess.getComponentKey(file))) {
                                    dataAccess.reset();
                                    schemaManager.reset();
                                    workspaceConfig.resetComponents();
                                    workspaceConfig.save();
                                }
                            } else {
                                Registry<?> registry = Registry.getRegistry(dataType);

                                // If the file is not registered as a component file, but has a type with component attached
                                // to it, we need to reset the system so that it can figure out if it matches with a
                                // component root directory and registers it.
                                if (registry.hasType(key) && registry.getType(key).isAnnotationPresent(Component.class)) {
                                    dataAccess.reset();
                                    schemaManager.reset();
                                    workspaceConfig.resetComponents();
                                    workspaceConfig.save();
                                }
                            }
                        } else {
                            // If no type is specified anymore, but it was registered as a component file, remove the
                            // component from the system
                            if (dataAccess.isComponentFile(file)) {
                                String componentName = file.getFileName().toString().replace(".json", "").trim();
                                dataAccess.reset();
                                schemaManager.deleteComponentSchemas(dataType, componentName);
                                workspaceConfig.resetComponents();
                                workspaceConfig.save();
                            }
                            Orbis.getLogger().error("Generator file {} of type {} doesn't have a type specified!", file,
                                    dataType.getSimpleName().toLowerCase(Locale.ENGLISH));
                        }

                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                doHotReload();
            }
            lock.release();
        } catch (InterruptedException ex) {
            Orbis.getLogger().error("Failed to process file modify event for {}", directory);
            lock.release();
        }
    }

    @Blocking
    void onDirectoryDelete() {
        try {
            lock.acquire();
            // We have to re-index the entirety of DataAccess, SchemaManager components and WorkspaceConfig,
            // because we do not know how many directories or files were deleted.
            dataAccess.reset();
            schemaManager.reset();
            workspaceConfig.reset();
            workspaceConfig.save();
            doHotReload();
            lock.release();
        } catch (InterruptedException ex) {
            Orbis.getLogger().error("Failed to process directory delete event for {}", directory);
            lock.release();
        }
    }

    @Blocking
    void onFileDelete(@NotNull Path file) {
        try {
            lock.acquire(); // Only requires re-indexing of a Registry definition unless the file is a component file.
            if (dataAccess.isComponentFile(file)) {
                Class<?> generatorType = dataAccess.getType(file.getParent());
                String componentName = file.getFileName().toString().replace(".json", "").trim();
                dataAccess.reset();
                schemaManager.deleteComponentSchemas(generatorType, componentName);
                workspaceConfig.resetComponents();
                workspaceConfig.save();
            }

            // Reset the entries for the file type
            Class<?> type = dataAccess.getType(file.getParent());
            if (dataAccess.isComponentType(type)) {
                schemaManager.resetEntries(type, dataAccess.getComponentName(file.getParent()));
            } else {
                schemaManager.resetEntries(type);
            }
            doHotReload();
            lock.release();
        } catch (InterruptedException ex) {
            Orbis.getLogger().error("Failed to process file delete event for {}", directory);
            lock.release();
        }
    }

    private void doHotReload() {
        if (hasWorld() && lock.getQueueLength() == 0 && (hotReloadTask == null || !hotReloadTask.isQueued())) {
            hotReloadTask = Orbis.getPlatform().scheduler().runDelayedTaskAsync(studioWorld::hotReload,
                    Orbis.getSettings().studio().hotReloadDelay());
        }
    }

}
