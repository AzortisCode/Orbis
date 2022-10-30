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
import com.azortis.orbis.pack.data.ComponentAccess;
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
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
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
        DEFAULT_SETTINGS.addProperty("files.autoSave", "onFocusChange");

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

    private final JsonArray folders = FOLDERS_ARRAY;
    private transient final File workspaceFile;
    private transient final Path directoryPath;
    private transient final ImmutableMap<Class<?>, SchemaMatcher> schemaMatchers;
    private transient final ImmutableMap<Class<?>, Map<String, SchemaMatcher>> componentSchemaMatchers;
    private JsonObject settings;

    public WorkspaceConfig(@NotNull JsonObject settings, @NotNull Project project, @NotNull File workspaceFile) throws IOException {
        this.settings = settings.deepCopy();
        this.workspaceFile = workspaceFile;

        // Add the special Pack and Dimension matching rules.
        this.directoryPath = project.directory().toPath();
        SchemaManager schemaManager = project.schemaManager();

        // The pack schema should only be pointed at pack.json
        ImmutableMap.Builder<Class<?>, SchemaMatcher> builder = ImmutableMap.builder();

        SchemaMatcher packMatcher = new SchemaMatcher(directoryPath.relativize(schemaManager.packSchema().toPath()).toString());
        Path packPath = Path.of("pack.json");
        packMatcher.matchingRules.put(packPath, "pack.json");
        builder.put(Pack.class, packMatcher);

        // The dimension schema should be pointed at all json files except for the pack.json
        SchemaMatcher dimensionMatcher = new SchemaMatcher(directoryPath.relativize(schemaManager.dimensionSchema().toPath()).toString());
        try (Stream<Path> files = Files.list(directoryPath)) {
            files.filter(path -> Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)).forEach(path -> {
                Path filePath = directoryPath.relativize(path);
                if (!filePath.endsWith("pack.json")) {
                    dimensionMatcher.matchingRules.put(filePath, filePath.toString());
                }
            });
        }
        builder.put(Dimension.class, packMatcher);

        // Create schema matchers for each type possible.
        for (Class<?> dataType : DataAccess.DATA_TYPES.keySet()) {
            builder.put(dataType, new SchemaMatcher(directoryPath.relativize(schemaManager.getSchema(dataType).toPath()).toString()));
        }

        ImmutableMap.Builder<Class<?>, Map<String, SchemaMatcher>> componentBuilder = ImmutableMap.builder();
        for (Class<?> rootType : DataAccess.GENERATOR_TYPES.keySet()) {
            builder.put(rootType, new SchemaMatcher(directoryPath.relativize(schemaManager.getSchema(rootType).toPath()).toString()));

            for (Class<?> type : Registry.getRegistry(rootType).getTypes()) {
                if (type.isAnnotationPresent(Component.class)) {
                    try {
                        Class<? extends ComponentAccess> componentAccessType = type.getAnnotation(Component.class).value();
                        ComponentAccess access = componentAccessType.getConstructor(String.class, DataAccess.class).newInstance(null, null);
                        schemaManager.generateComponentSchemas(type, access);

                        for (Class<?> dataType : access.dataTypes()) {
                            //builder.put(dataType, new SchemaMatcher(directoryPath.relativize(schemaManager.getSchema(dataType).toPath()).toString()));
                        }

                    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        schemaMatchers = builder.build();
        componentSchemaMatchers = componentBuilder.build();
        resetMatchers();
        save();
    }

    void resetMatchers() {

    }

    private void save() throws IOException {
        if (!workspaceFile.exists() && !workspaceFile.createNewFile()) {
            Orbis.getLogger().error("Failed to create new *.code-workspace file!");
            return;
        }
        // Load any changed settings
        JsonObject existingWorkspace = Orbis.getGson().fromJson(new FileReader(workspaceFile), JsonObject.class);
        settings = existingWorkspace.getAsJsonObject("settings");
        settings.remove("json.schemas");

        // Write to json schema's
        JsonArray jsonSchemas = new JsonArray();
        for (SchemaMatcher schemaMatcher : schemaMatchers.values()) {
            JsonObject matcherObject = new JsonObject();
            JsonArray fileMatch = new JsonArray();

            for (String matchingRule : schemaMatcher.matchingRules.values()) {
                fileMatch.add(matchingRule);
            }

            matcherObject.add("fileMatch", fileMatch);
            matcherObject.addProperty("url", schemaMatcher.schema);
        }
        settings.add("json.schemas", jsonSchemas);

        final String json = Orbis.getGson().toJson(this);
        Files.writeString(workspaceFile.toPath(), json, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private final static class SchemaMatcher {

        private final Map<Path, String> matchingRules = new HashMap<>();
        private final String schema;

        private SchemaMatcher(@NotNull String schema) {
            this.schema = schema;
        }

        private void addDirectory(@NotNull Path directory) throws IllegalArgumentException {
            Preconditions.checkArgument(Files.isDirectory(directory, LinkOption.NOFOLLOW_LINKS));

            if (matchingRules.putIfAbsent(directory, directory + "/*.json") != null) {
                Orbis.getLogger().error("Directory matching rule {} for {} already exists!", matchingRules.get(directory), directory);
            }
        }

        private void removeDirectory(@NotNull Path directory) throws IllegalArgumentException {
            Preconditions.checkArgument(Files.isDirectory(directory, LinkOption.NOFOLLOW_LINKS));

            if (matchingRules.containsKey(directory)) {
                matchingRules.remove(directory);
                return;
            }
            Orbis.getLogger().error("Directory matching rule for {} never existed!", directory);
        }

    }

}
