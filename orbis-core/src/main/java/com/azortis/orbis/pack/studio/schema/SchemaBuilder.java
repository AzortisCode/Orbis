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
import com.azortis.orbis.pack.studio.Project;
import com.azortis.orbis.pack.studio.StudioDataAccess;
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
 * @author Jake Nijssen
 * @see <a href="https://json-schema.org/">Json-Schema website.</a>
 * @since 0.3-Alpha
 */
public sealed abstract class SchemaBuilder permits ClassBuilder, BlockBuilder, EntriesBuilder {

    protected final Project project;
    protected final StudioDataAccess data;
    protected final SchemaManager schemaManager;
    protected final File schemaFile;

    protected SchemaBuilder(@NotNull Project project, @NotNull StudioDataAccess data,
                            @NotNull SchemaManager schemaManager, @NotNull File schemaFile) {
        this.project = project;
        this.data = data;
        this.schemaManager = schemaManager;
        this.schemaFile = schemaFile;

        // Create empty dummy file, so it can be found when initializing
        try {
            if (!schemaFile.exists() && !schemaFile.createNewFile()) {
                Orbis.getLogger().info("Failed to create schema dummy file {}", schemaFile.getName());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    static BlockBuilder blocks(@NotNull Project project, @NotNull SchemaManager schemaManager, @NotNull File blocksFile) {
        return new BlockBuilder(project, project.dataAccess(), schemaManager, blocksFile);
    }

    static EntriesBuilder entries(@NotNull Project project, @NotNull SchemaManager schemaManager,
                                  @NotNull File entriesFile, @NotNull Class<?> type) {
        return entries(project, schemaManager, entriesFile, type, null);
    }

    static EntriesBuilder entries(@NotNull Project project, @NotNull SchemaManager schemaManager,
                                  @NotNull File entriesFile, @NotNull Class<?> type, @Nullable String name) {
        return new EntriesBuilder(project, project.dataAccess(), schemaManager, entriesFile, type, name);
    }

    static ClassBuilder clazz(@NotNull Project project, @NotNull SchemaManager schemaManager,
                              @NotNull File schemaFile, @NotNull Class<?> type) {
        return clazz(project, schemaManager, schemaFile, type, null);
    }

    static ClassBuilder clazz(@NotNull Project project, @NotNull SchemaManager schemaManager, @NotNull File schemaFile,
                              @NotNull Class<?> type, @Nullable String name) {
        return new ClassBuilder(project, project.dataAccess(), schemaManager, schemaFile, type, name);
    }

    public File file() {
        return schemaFile;
    }

    public void save() {
        try {
            if (!schemaFile.exists() && !schemaFile.createNewFile()) {
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
        Preconditions.checkArgument(referencedSchemaFile.exists(),
                "Referenced schema file doesn't exist!");
        Preconditions.checkArgument(referencedSchemaFile.isFile(),
                "Referenced schema file must be a file not a directory!");
        return schemaFile.getParentFile().toPath().relativize(referencedSchemaFile.toPath())
                .toString().replaceAll("\\\\", "/");
    }
}
