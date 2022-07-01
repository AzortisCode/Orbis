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

import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.pack.studio.Project;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public sealed abstract class SchemaBuilder permits ClassBuilder, BlockBuilder, RegistryBuilder {

    protected final Project project;
    protected final DataAccess data;
    private final File schemaFile;

    private JsonObject schema;

    public SchemaBuilder(@NotNull Project project, @NotNull DataAccess data, @NotNull File schemaFile) {
        this.project = project;
        this.data = data;
        this.schemaFile = schemaFile;
    }

    protected abstract @NotNull JsonObject generateSchema();

    protected abstract boolean shouldRegenerate();

}
