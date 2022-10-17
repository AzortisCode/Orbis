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
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.pack.studio.Project;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Abstract utility class for building JsonSchema's that can be used for
 * config tab-completion/validation in VSCode.
 *
 * @since 0.3-Alpha
 * @see <a href="https://json-schema.org/">Json-Schema website.</a>
 * @author Jake Nijssen
 */
public sealed abstract class SchemaBuilder permits ClassBuilder, BlockBuilder, EntriesBuilder {

    protected final Project project;
    protected final DataAccess data;
    protected final File schemaFile;

    protected SchemaBuilder(@NotNull Project project, @NotNull DataAccess data, @NotNull File schemaFile) {
        this.project = project;
        this.data = data;
        this.schemaFile = schemaFile;
    }

    public void saveSchema() {
        try {
            if(!schemaFile.exists() && !schemaFile.createNewFile()) {
                Orbis.getLogger().error("Failed to create schema file!");
            }
            final String json = Orbis.getGson().toJson(generateSchema());
            Files.writeString(schemaFile.toPath(), json, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            Orbis.getLogger().error("Failed to save schema file!", ex);
        }
    }

    protected abstract @NotNull JsonObject generateSchema();

    /**
     * Get the correct string to use in $ref in the schema.
     * Say if the schemaFile is located at: /.orbis/schemas/static/biome/biome-schema.json
     * and you are trying to reference: /.orbis/schemas/block-schema.json
     * This method would produce this: "../../block-schema.json"
     *
     * @param referencedSchemaFile The schema file you're trying to reference.
     * @return A reference string that VSCode accepts.
     */
    protected String getSchemaReference(@NotNull File referencedSchemaFile) {
        Preconditions.checkArgument(referencedSchemaFile.isFile(), "Referenced schema file must be a file not a directory!");
        return schemaFile.toPath().relativize(referencedSchemaFile.toPath()).toString();
    }

    static void generateBlocks(@NotNull Project project, @NotNull File blocksFile) {
        new BlockBuilder(project, project.studioWorld().data(), blocksFile).saveSchema();
    }

    static void generateEntries(@NotNull Project project, @NotNull File entriesFile,
                                @NotNull Class<?> type, @Nullable String name){
        new EntriesBuilder(project, project.studioWorld().data(), entriesFile, type, name).saveSchema();
    }

    static void generateClassSchema(@NotNull Project project, @NotNull File schemaFile,
                                    @NotNull Class<?> type) {
        new ClassBuilder(project, project.studioWorld().data(), schemaFile, type).saveSchema();
    }
}
