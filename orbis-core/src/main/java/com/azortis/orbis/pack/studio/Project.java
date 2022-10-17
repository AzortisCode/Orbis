/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2022 Azortis
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
import com.azortis.orbis.pack.studio.schema.SchemaManager;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Semaphore;

/**
 * A representation of a studio project environment, manages all things from Schema generation and
 * mapping the schema to the right files, and forces entries schema's to regenerate when a new config data file
 * has been added, to make sure it can be referenced in other files.
 *
 * @since 0.3-Alpha
 * @author Jake Nijssen
 */
public final class Project {

    private final String name;
    private final File directory;
    private boolean closed = false;

    private final SchemaManager schemaManager;
    private final StudioDataAccess dataAccess;
    private final WorkspaceConfig workspaceConfig;
    private final File settingsDir;
    private final ProjectWatcher watcher;
    private final StudioWorld studioWorld;

    Project(String name, File directory) throws IOException {
        this.name = name;
        this.directory = directory;
        this.schemaManager = new SchemaManager(this);
        this.dataAccess = new StudioDataAccess(directory);

        // Initialize code-workspace config for VSCode completions
        File workspaceFile = new File(directory + "/" + name + ".code-workspace");
        if (workspaceFile.exists()) {
            JsonObject existingWorkspace = Orbis.getGson().fromJson(new FileReader(workspaceFile), JsonObject.class);
            this.workspaceConfig = new WorkspaceConfig(existingWorkspace.getAsJsonObject("settings"), this, workspaceFile);
        } else {
            this.workspaceConfig = new WorkspaceConfig(WorkspaceConfig.DEFAULT_SETTINGS, this, workspaceFile);
        }

        this.settingsDir = new File(directory + "/.orbis/");
        if (!settingsDir.exists() && !settingsDir.mkdirs())
            Orbis.getLogger().error("Failed to create /.orbis/ settings directory for {}", name);

        // Create a studio world, so we have a data access point.
        this.studioWorld = Orbis.getPlatform().createStudioWorld(this);

        // Read pack.json in order to get the Dimension file name, if none is specified the project is deemed invalid,
        // and so we cannot load a Dimension tree yet, and thus not spin up a visualization.
        this.studioWorld.initialize();

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

    public SchemaManager schemaManager(){
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

    public StudioWorld studioWorld() {
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
     * Called when a new directory has been created.
     *
     * @param path The path of the directory.
     */
    void onDirectoryCreate(@NotNull Path path) throws InterruptedException {
        lock.acquire();
        // We have to re-index the entirety of the DataAccess, but not WorkspaceConfig
        // This is because created directories do not influence existing matching rules.
        dataAccess.reset();

        doHotReload();
        lock.release();
    }

    void onFileCreate(@NotNull Path path) throws InterruptedException {
        lock.acquire(); // Requires only re-indexing of a Registry definition

        doHotReload();
        lock.release();
    }

    void onFileModify(@NotNull Path path) throws InterruptedException {
        lock.acquire();
        doHotReload(); // Just queue a hot reload.
        lock.release();
    }

    void onDirectoryDelete(@NotNull Path path) throws InterruptedException {
        lock.acquire();
        // We have to re-index the entirety of DataAccess and WorkspaceConfig,
        // because we do not know how many directories or files were deleted.
        dataAccess.reset();

        doHotReload();
        lock.release();
    }

    void onFileDelete(@NotNull Path path) throws InterruptedException {
        lock.acquire(); // Requires only re-indexing of a Registry definition


        doHotReload();
        lock.release();
    }

    private void doHotReload(){
        if(lock.getQueueLength() == 0) {
            studioWorld.hotReload();
        }
    }

}
