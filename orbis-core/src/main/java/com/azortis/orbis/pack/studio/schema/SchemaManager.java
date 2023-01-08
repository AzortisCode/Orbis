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

package com.azortis.orbis.pack.studio.schema;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.Registry;
import com.azortis.orbis.block.ConfiguredBlock;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.pack.Pack;
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.pack.studio.Project;
import com.azortis.orbis.pack.studio.StudioDataAccess;
import com.azortis.orbis.pack.studio.annotations.GlobalDefinition;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class SchemaManager {

    private final Project project;
    private final StudioDataAccess dataAccess;
    private final File schemasDir;
    private final File definitionsDir;
    private final File packSchemaFile;
    private final File dimensionSchemaFile;

    private final Map<Class<?>, SchemaBuilder> typeSchemaMap = new HashMap<>();
    private final Map<Class<?>, SchemaBuilder> typeEntriesMap = new HashMap<>();
    private final Table<Class<?>, String, SchemaBuilder> componentTypeSchemaTable = HashBasedTable.create();
    private final Table<Class<?>, String, SchemaBuilder> componentTypeEntriesTable = HashBasedTable.create();

    public SchemaManager(@NotNull Project project) {
        this.project = project;
        this.dataAccess = project.dataAccess();
        this.schemasDir = new File(project.settingsDir() + "/.orbis/schemas/");

        if (!schemasDir.exists() && !schemasDir.mkdirs()) Orbis.getLogger().error("Failed to create schemas folder!");

        // Definitions.
        // Global definitions
        this.definitionsDir = new File(schemasDir + "/definitions/");

        // Generate all the data definitions
        File blocksSchemaFile = new File(definitionsDir, "blocks.schema.json");
        SchemaBuilder blocksSchema = SchemaBuilder.blocks(project, blocksSchemaFile);
        typeSchemaMap.put(ConfiguredBlock.class, blocksSchema);
        blocksSchema.save();

        // Generate the pack & dimension schema
        this.packSchemaFile = new File(schemasDir, "pack.schema.json");
        SchemaBuilder.clazz(project, packSchemaFile, Pack.class);
        this.dimensionSchemaFile = new File(schemasDir, "dimension.schema.json");
        SchemaBuilder.clazz(project, dimensionSchemaFile, Dimension.class);

        // Create the entries directory
        File entriesDir = new File(schemasDir + "/entries/");
        if (!entriesDir.exists() && !entriesDir.mkdirs()) Orbis.getLogger().error("Failed to create entry schemas folder!");

        for (Class<?> dataType : DataAccess.DATA_TYPES.keySet()) {
            File schemaFile = new File(schemasDir, dataType.getSimpleName()
                    .toLowerCase(Locale.ENGLISH) + ".schema.json");
            typeSchemaMap.put(dataType, SchemaBuilder.clazz(project, schemaFile, dataType));

            File dataEntriesFile = new File(entriesDir, dataType.getSimpleName()
                    .toLowerCase(Locale.ENGLISH) + "-entries.schema.json");
            SchemaBuilder dataEntriesBuilder = SchemaBuilder.entries(project, dataEntriesFile, dataType);
            typeEntriesMap.put(dataType, dataEntriesBuilder);
        }

        for (Class<?> generatorType : DataAccess.GENERATOR_TYPES.keySet()) {
            File generatorDir = new File(schemasDir + "/" + generatorType.getSimpleName()
                    .toLowerCase(Locale.ENGLISH) + "/");
            if (!generatorDir.exists() && !generatorDir.mkdirs()) {
                Orbis.getLogger().error("Failed to create schema directory for {}", generatorType.getSimpleName()
                        .toLowerCase(Locale.ENGLISH));
                return;
            }

            File schemaFile = new File(generatorDir, generatorType.getSimpleName()
                    .toLowerCase(Locale.ENGLISH) + ".schema.json");
            SchemaBuilder schemaBuilder = SchemaBuilder.clazz(project, schemaFile, generatorType);
            typeSchemaMap.put(generatorType, schemaBuilder);

            File generatorEntriesFile = new File(entriesDir, generatorType.getSimpleName()
                    .toLowerCase(Locale.ENGLISH) + "-entries.schema.json");
            SchemaBuilder generatorEntriesBuilder = SchemaBuilder.entries(project, generatorEntriesFile, generatorType);
            typeEntriesMap.put(generatorType, generatorEntriesBuilder);
        }
        typeSchemaMap.entrySet().stream().filter(entry -> DataAccess.DATA_TYPES.containsKey(entry.getKey()))
                        .forEach(entry -> entry.getValue().save());
        reset();
    }

    public void reset() {
        // Delete all the component schema's
        componentTypeSchemaTable.clear();
        componentTypeEntriesTable.clear();

        for (Class<?> generatorType : DataAccess.GENERATOR_TYPES.keySet()) {
            File generatorDir = new File(schemasDir + "/" + generatorType.getSimpleName()
                    .toLowerCase(Locale.ENGLISH) + "/");
            for (File componentDir : Objects.requireNonNull(generatorDir.listFiles(File::isDirectory))) {
                if (!componentDir.delete()) Orbis.getLogger().error("Failed to delete component schema directory!");
            }
        }

        // Regenerate all entries schema's
        typeEntriesMap.values().forEach(SchemaBuilder::save);

        // Regenerate all the generator schema's
        typeSchemaMap.entrySet().stream().filter(entry -> DataAccess.GENERATOR_TYPES.containsKey(entry.getKey()))
                .forEach(entry -> entry.getValue().save());

        // Create all the component schema's
        for (Path componentRoot : dataAccess.getComponentRoots()) {
            Class<?> generatorType = dataAccess.getType(componentRoot.getParent());
            Class<?> componentType = dataAccess.getComponentType(componentRoot);
            String name = dataAccess.getComponentName(componentRoot);
            createComponentSchemas(componentType, generatorType, name);
        }
    }

    public void createComponentSchemas(@NotNull Class<?> generatorType, @NotNull Class<?> componentType,
                                       @NotNull String name) {
        File componentDir = new File(schemasDir + "/" + generatorType.getSimpleName()
                .toLowerCase(Locale.ENGLISH) + "/" + name + "/");
        if(!componentDir.exists() && componentDir.mkdirs())
            Orbis.getLogger().error("Failed to create components directory for {}", name);

        File entriesDir = new File(componentDir + "/entries/");
        if (!entriesDir.exists() && !entriesDir.mkdirs())
            Orbis.getLogger().error("Failed to create entry schemas directory for {}!", name);

        // Assign the schemas to the types
        for (Class<?> dataType : Registry.getRegistry(generatorType).getDataTypes(componentType)) {
            File schemaFile = new File(componentDir, dataType.getSimpleName()
                    .toLowerCase(Locale.ENGLISH) + ".schema.json");
            componentTypeSchemaTable.put(dataType, name, SchemaBuilder.clazz(project, schemaFile, dataType, name));

            File dataEntriesFile = new File(entriesDir, dataType.getSimpleName()
                    .toLowerCase(Locale.ENGLISH) + "-entries.schema.json");
            componentTypeEntriesTable.put(dataType, name, SchemaBuilder.entries(project, dataEntriesFile, dataType, name));
        }

        // Generate the schema's
        componentTypeEntriesTable.columnMap().get(name).values().forEach(SchemaBuilder::save);
        componentTypeSchemaTable.columnMap().get(name).values().forEach(SchemaBuilder::save);

        // Regenerate the generator schema
        typeSchemaMap.get(generatorType).save();
    }

    public void deleteComponentSchemas(@NotNull Class<?> generatorType, @NotNull String name) {
        File componentDir = new File(schemasDir + "/" + generatorType.getSimpleName()
                .toLowerCase(Locale.ENGLISH) + "/" + name + "/");
        if(componentDir.exists() && !componentDir.delete())
            Orbis.getLogger().error("Failed to delete components directory for {}", name);
    }

    public void resetEntries(@NotNull Class<?> type) {
        if(typeEntriesMap.containsKey(type)) {
            typeEntriesMap.get(type).save();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void resetEntries(@NotNull Class<?> type, @NotNull String name) {
        if(componentTypeEntriesTable.contains(type, name)) {
            componentTypeEntriesTable.get(type, name).save();
        }
    }

    public @NotNull File packSchema() {
        return packSchemaFile;
    }

    public @NotNull File dimensionSchema() {
        return dimensionSchemaFile;
    }

    public @NotNull File getSchema(@NotNull Class<?> type) throws IllegalArgumentException {
        if(!typeSchemaMap.containsKey(type) && type.isAnnotationPresent(GlobalDefinition.class)) {
            // TODO add enum support
            String definitionName = type.getAnnotation(GlobalDefinition.class).value();
            File schemaFile = new File(definitionsDir, definitionName + ".schema.json");
            SchemaBuilder schemaBuilder = SchemaBuilder.clazz(project, schemaFile, type);
            typeSchemaMap.put(type, schemaBuilder);
            schemaBuilder.save();
        }
        Preconditions.checkArgument(typeSchemaMap.containsKey(type), "Given type doesn't have a schema");
        return typeSchemaMap.get(type).file();
    }

    public @NotNull File getEntriesSchema(@NotNull Class<?> type) throws IllegalArgumentException {
        Preconditions.checkArgument(typeEntriesMap.containsKey(type),
                "Given type doesn't have an entries schema!");
        return typeEntriesMap.get(type).file();
    }

    @SuppressWarnings("DataFlowIssue")
    public @NotNull File getSchema(@NotNull Class<?> type, @NotNull String name) throws IllegalArgumentException {
        Preconditions.checkArgument(componentTypeSchemaTable.contains(type, name),
                "No schema exists for given type and component name!");
        return componentTypeSchemaTable.get(type, name).file();
    }

    @SuppressWarnings("DataFlowIssue")
    public @NotNull File getEntriesSchema(@NotNull Class<?> type, @NotNull String name) throws IllegalArgumentException {
        Preconditions.checkArgument(componentTypeEntriesTable.contains(type, name),
                "No entries schema exists for given type and component name!");
        return componentTypeEntriesTable.get(type, name).file();
    }

}
