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
import com.azortis.orbis.Registry;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.pack.Pack;
import com.azortis.orbis.pack.data.Component;
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.pack.studio.schema.SchemaManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

/**
 * Class representation of VSCode its *.code-workspace config file.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.pack.studio")
public final class WorkspaceConfig {

    public static final JsonObject FOLDER_PATH = new JsonObject();
    public static final JsonArray FOLDERS_ARRAY = new JsonArray();
    public static final JsonObject DEFAULT_SETTINGS = new JsonObject();

    static {
        FOLDER_PATH.addProperty("path", ".");
        FOLDERS_ARRAY.add(FOLDER_PATH);

        // Apply the default editor settings.
        DEFAULT_SETTINGS.addProperty("workbench.colorTheme", "Monokai");
        DEFAULT_SETTINGS.addProperty("workbench.preferredDarkColorTheme", "Solarized Dark");
        DEFAULT_SETTINGS.addProperty("workbench.tips.enabled", false);
        DEFAULT_SETTINGS.addProperty("workbench.tree.indent", 24);
        DEFAULT_SETTINGS.addProperty("files.autoSave", "afterDelay");
        DEFAULT_SETTINGS.addProperty("files.autoSaveDelay", 5000);

        // Json Language settings
        JsonObject jsonSettings = new JsonObject();
        jsonSettings.addProperty("editor.autoIndent", "brackets");
        jsonSettings.addProperty("editor.acceptSuggestionOnEnter", "smart");
        jsonSettings.addProperty("editor.cursorSmoothCaretAnimation", true);
        jsonSettings.addProperty("editor.dragAndDrop", false);
        jsonSettings.addProperty("files.trimTrailingWhitespace", true);
        jsonSettings.addProperty("diffEditor.ignoreTrimWhitespace", true);
        jsonSettings.addProperty("files.trimFinalNewlines", true);
        jsonSettings.addProperty("editor.suggest.showKeywords", false);
        jsonSettings.addProperty("editor.suggest.showSnippets", false);
        jsonSettings.addProperty("editor.suggest.showWords", false);
        JsonObject quickSuggestions = new JsonObject();
        quickSuggestions.addProperty("strings", true);
        jsonSettings.add("editor.quickSuggestions", quickSuggestions);
        jsonSettings.addProperty("editor.suggest.insertMode", "replace");
        DEFAULT_SETTINGS.add("[json]", jsonSettings);
        DEFAULT_SETTINGS.addProperty("json.maxItemsComputed", 30000);
    }

    @SuppressWarnings("unused")
    private final JsonArray folders = FOLDERS_ARRAY;
    private JsonObject settings;
    private transient final File workspaceFile;
    private transient final SchemaManager schemaManager;
    private transient final StudioDataAccess dataAccess;
    private transient final Path rootDirectory;

    /**
     * Store a reference for each data object to a schema matcher object that specifies which directories the
     * schema should be pointed at.
     */
    private transient final ImmutableMap<Class<?>, SchemaMatcher> schemaMatchers;

    /**
     * Component schema matchers(For data types used in components) are seperated from the normal schema matchers,
     * as they will have different rules due to them being isolated to themselves.
     */
    private transient final ImmutableMap<Class<?>, Map<String, SchemaMatcher>> componentSchemaMatchers;

    public WorkspaceConfig(@NotNull JsonObject settings, @NotNull Project project, @NotNull File workspaceFile) {
        this.settings = settings.deepCopy();
        this.workspaceFile = workspaceFile;
        this.schemaManager = project.schemaManager();
        this.dataAccess = project.dataAccess();
        this.rootDirectory = project.directory().toPath();

        ImmutableMap.Builder<Class<?>, SchemaMatcher> builder = ImmutableMap.builder();

        // The pack schema should only be pointed at pack.json
        SchemaMatcher packMatcher = new SchemaMatcher(rootDirectory
                .relativize(schemaManager.packSchema().toPath()).toString());
        builder.put(Pack.class, packMatcher);

        // The dimension schema should be pointed at all json files except for the pack.json
        SchemaMatcher dimensionMatcher = new SchemaMatcher(rootDirectory
                .relativize(schemaManager.dimensionSchema().toPath()).toString());
        builder.put(Dimension.class, dimensionMatcher);

        // Create schema matchers for each type possible.
        for (Class<?> dataType : DataAccess.DATA_TYPES.keySet()) {
            builder.put(dataType, new SchemaMatcher(rootDirectory.relativize(schemaManager
                    .getSchema(dataType).toPath()).toString()));
        }

        ImmutableMap.Builder<Class<?>, Map<String, SchemaMatcher>> componentBuilder = ImmutableMap.builder();
        for (Class<?> rootType : DataAccess.GENERATOR_TYPES.keySet()) {
            builder.put(rootType, new SchemaMatcher(rootDirectory.relativize(schemaManager
                    .getSchema(rootType).toPath()).toString()));

            Registry<?> rootRegistry = Registry.getRegistry(rootType);
            for (Class<?> type : rootRegistry.getTypes()) {
                if (type.isAnnotationPresent(Component.class) && rootRegistry.hasDataTypes(type)) {
                    for (Class<?> dataType : rootRegistry.getDataTypes(type)) {
                        // We don't know how many component implementations are(i.e. configs using a component type)
                        // since every "implementation" has its own schema's generated in order to isolate them from
                        // each other, we just set a mutable hashmap that gets updated during the session.
                        componentBuilder.put(dataType, new HashMap<>());
                    }
                }
            }
        }

        schemaMatchers = builder.build();
        componentSchemaMatchers = componentBuilder.build();
        reset();
        save();
    }

    void reset() {
        schemaMatchers.values().forEach(SchemaMatcher::clear);

        // Pack & Dimension needs a special treatment
        Objects.requireNonNull(schemaMatchers.get(Pack.class)).matchingRules.add("pack.json");
        SchemaMatcher dimensionMatcher = schemaMatchers.get(Dimension.class);
        assert dimensionMatcher != null;
        try (Stream<Path> files = Files.list(rootDirectory)) {
            files.filter(path -> Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)).forEach(path -> {
                String filePath = rootDirectory.relativize(path).toString().replaceAll("\\\\", "/");
                if (!filePath.equalsIgnoreCase("pack.json") && !filePath.endsWith(".code-workspace")) {
                    dimensionMatcher.matchingRules.add(filePath);
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // The rest is normal
        for (Map.Entry<Class<?>, SchemaMatcher> entry : schemaMatchers.entrySet()) {
            if (entry.getKey() != Pack.class && entry.getKey() != Dimension.class) {
                dataAccess.getDirectories(entry.getKey()).forEach((path) -> entry.getValue()
                        .addDirectory(rootDirectory, path));
            }
        }
        resetComponents();
    }

    void resetComponents() {
        componentSchemaMatchers.values().forEach(Map::clear);
        for (Path root : dataAccess.getComponentRoots()) {
            Class<?> generatorType = dataAccess.getType(root.getParent());
            String name = dataAccess.getComponentName(root);
            registerComponent(generatorType, root, name);
        }
    }

    void registerComponent(@NotNull Class<?> generatorType, @NotNull Path componentRoot,
                           @NotNull String name) throws IllegalArgumentException {
        Preconditions.checkArgument(componentRoot.isAbsolute(), "Component root must be absolute!");
        Preconditions.checkArgument(Files.exists(componentRoot, LinkOption.NOFOLLOW_LINKS), "Path doesn't exist!");
        Preconditions.checkArgument(Files.isDirectory(componentRoot, LinkOption.NOFOLLOW_LINKS),
                "Component root be a directory!");

        Class<?> componentType = dataAccess.getComponentType(componentRoot);
        Set<Class<?>> componentDataTypes = Registry.getRegistry(generatorType).getDataTypes(componentType);

        for (Class<?> type : componentDataTypes) {
            Preconditions.checkArgument(componentSchemaMatchers.containsKey(type),
                    "Type " + type.getCanonicalName() + " isn't a registered component data type");
            Map<String, SchemaMatcher> componentMatchers = componentSchemaMatchers.get(type);
            assert componentMatchers != null;

            if (!componentMatchers.containsKey(name)) {
                SchemaMatcher matcher = new SchemaMatcher(this.rootDirectory.relativize(
                        schemaManager.getSchema(type, name).toPath()).toString());
                componentMatchers.put(name, matcher);
                dataAccess.getDirectories(type, name).forEach(path -> matcher.addDirectory(rootDirectory, path));
            } else {
                Orbis.getLogger().error("Component with name {} already registered for implementation {}",
                        name, componentType.getCanonicalName());
            }
        }
    }

    void addDirectory(@NotNull Class<?> type, @NotNull Path directory) throws IllegalArgumentException {
        Preconditions.checkArgument(directory.isAbsolute(), "Directory must be absolute!");
        Preconditions.checkArgument(Files.exists(directory, LinkOption.NOFOLLOW_LINKS), "Path doesn't exist!");
        Preconditions.checkArgument(Files.isDirectory(directory, LinkOption.NOFOLLOW_LINKS), "Must be a directory!");
        Preconditions.checkArgument(schemaMatchers.containsKey(type), "Type isn't a registered data type");
        Objects.requireNonNull(schemaMatchers.get(type)).addDirectory(rootDirectory, directory);
    }

    void addDirectory(@NotNull Class<?> type, @NotNull String componentName,
                      @NotNull Path directory) throws IllegalArgumentException {
        Preconditions.checkArgument(directory.isAbsolute(), "Directory must be absolute!");
        Preconditions.checkArgument(Files.exists(directory, LinkOption.NOFOLLOW_LINKS), "Path doesn't exist!");
        Preconditions.checkArgument(Files.isDirectory(directory, LinkOption.NOFOLLOW_LINKS),
                "Must be a directory!");
        Preconditions.checkArgument(componentSchemaMatchers.containsKey(type),
                "Type isn't a registered component data type");
        Map<String, SchemaMatcher> componentMatchers = componentSchemaMatchers.get(type);
        assert componentMatchers != null;
        Preconditions.checkArgument(componentMatchers.containsKey(componentName), "Component " + componentName
                + " not registered for type " + type.getCanonicalName());

        componentMatchers.get(componentName).addDirectory(rootDirectory, directory);
    }

    void save() {
        try {
            if (!workspaceFile.exists() && !workspaceFile.createNewFile()) {
                Orbis.getLogger().error("Failed to create new *.code-workspace file!");
                return;
            }

            // Load any changed settings
            JsonObject existingWorkspace = Orbis.getGson().fromJson(new FileReader(workspaceFile), JsonObject.class);
            if (existingWorkspace != null) {
                settings = existingWorkspace.getAsJsonObject("settings");
                settings.remove("json.schemas");
            } else {
                settings = DEFAULT_SETTINGS.deepCopy();
            }

            // Write to json schema's
            JsonArray jsonSchemas = new JsonArray();
            Set<SchemaMatcher> schemaMatcherSet = new HashSet<>(schemaMatchers.values());
            for (Map<String, SchemaMatcher> componentMatcher : componentSchemaMatchers.values()) {
                schemaMatcherSet.addAll(componentMatcher.values());
            }
            for (SchemaMatcher schemaMatcher : schemaMatcherSet) {
                if (!schemaMatcher.matchingRules.isEmpty()) {
                    JsonObject matcherObject = new JsonObject();
                    JsonArray fileMatch = new JsonArray();

                    for (String matchingRule : schemaMatcher.matchingRules) {
                        fileMatch.add(matchingRule);
                    }

                    matcherObject.add("fileMatch", fileMatch);
                    matcherObject.addProperty("url", schemaMatcher.schema);
                    jsonSchemas.add(matcherObject);
                }
            }
            settings.add("json.schemas", jsonSchemas);

            final String json = Orbis.getGson().toJson(this);
            Files.writeString(workspaceFile.toPath(), json, StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            Orbis.getLogger().error("Failed to save workspace file!");
        }
    }

    private final static class SchemaMatcher {

        private final Set<String> matchingRules = new HashSet<>();
        private final String schema;

        private SchemaMatcher(@NotNull String schema) {
            this.schema = schema.replaceAll("\\\\", "/");
        }

        private void addDirectory(@NotNull Path rootDir, @NotNull Path directory) {
            matchingRules.add(rootDir.relativize(directory).toString().replaceAll("\\\\", "/") + "/*.json");
        }

        private void clear() {
            matchingRules.clear();
        }

    }

}
