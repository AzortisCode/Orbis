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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public final class ReferenceBuilder extends SchemaBuilder {

    private final JsonObject schema = new JsonObject();
    private final Class<?> type;
    private final String name;

    public ReferenceBuilder(@NotNull Project project, @NotNull DataAccess data,
                            @NotNull File schemaFile, @NotNull Class<?> type, @Nullable String name) {
        super(project, data, schemaFile);
        this.type = type;
        this.name = name;

        // Create initial schema
        schema.addProperty("$schema", "http://json-schema.org/draft-07/schema");
        schema.addProperty("type", "string");
    }

    @Override
    protected @NotNull JsonObject generateSchema() {
        if (schema.has("enum")) schema.remove("enum");
        JsonArray entries = new JsonArray();
        if (name == null) {
            data.getDataEntries(type).forEach(entries::add);
        } else {
            data.getComponentDataEntries(type, name).forEach(entries::add);
        }
        schema.add("enum", entries);
        return schema;
    }

    @Override
    protected boolean shouldRegenerate() {
        return true;
    }
}
