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
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.pack.Pack;
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.pack.studio.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class SchemaManager {

    private final Project project;
    private final File schemasDir;
    private final File blocksSchema;
    private final File packSchema;
    private final File dimensionSchema;
    private final File definitionsDir;
    private final Map<Class<?>, File> globalDefinitions = new HashMap<>();

    public SchemaManager(@NotNull Project project) {
        this.project = project;
        this.schemasDir = new File(project.settingsDir() + "/.orbis/schemas/");

        if (!schemasDir.exists() && !schemasDir.mkdirs()) Orbis.getLogger().error("Failed to create schemas folder!");

        // Definitions.
        // Global definitions
        definitionsDir = new File(schemasDir + "/definitions/");


        // Generate special schema's and Minecraft data definitions.
        this.blocksSchema = new File(definitionsDir, "blocks.json");
        SchemaBuilder.generateBlocks(project, blocksSchema);
        this.packSchema = new File(schemasDir, "pack-schema.json");
        SchemaBuilder.generateClassSchema(project, packSchema, Pack.class);
        this.dimensionSchema = new File(schemasDir, "dimension-schema.json");
        SchemaBuilder.generateClassSchema(project, dimensionSchema, Dimension.class);

        // Generate all data schema's
        for (Class<?> dataType : DataAccess.DATA_TYPES.keySet()) {
            File schemaFile = new File(schemasDir, dataType.getSimpleName().toLowerCase(Locale.ENGLISH) + "-schema.json");
            SchemaBuilder.generateClassSchema(project, schemaFile, dataType);
        }

        for (Class<?> generatorType : DataAccess.GENERATOR_TYPES.keySet()) {
            File generatorDir = new File(schemasDir + "/" + generatorType.getSimpleName().toLowerCase(Locale.ENGLISH) + "/");
            if (!generatorDir.exists() && !generatorDir.mkdirs()) {
                Orbis.getLogger().error("Failed to create schema directory for {}", generatorType.getSimpleName().toLowerCase(Locale.ENGLISH));
                return;
            }
            File schemaFile = new File(generatorDir, generatorType.getSimpleName().toLowerCase(Locale.ENGLISH) + "-schema.json");
            SchemaBuilder.generateClassSchema(project, schemaFile, generatorType);
        }

    }

    public void resetComponents() {

    }

    public void createComponent(@NotNull Class<?> type, @NotNull String name) {

    }

    public @NotNull File blocksSchema() {
        return blocksSchema;
    }  // TODO remove this reference.

    public @NotNull File packSchema() {
        return packSchema;
    }

    public @NotNull File dimensionSchema() {
        return dimensionSchema;
    }

    public @NotNull File getSchema(@NotNull Class<?> type) {
        return null;
    }

}
