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

import com.azortis.orbis.block.Block;
import com.azortis.orbis.block.BlockRegistry;
import com.azortis.orbis.block.property.BooleanProperty;
import com.azortis.orbis.block.property.EnumProperty;
import com.azortis.orbis.block.property.IntegerProperty;
import com.azortis.orbis.block.property.Property;
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.pack.studio.Project;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class BlockBuilder extends SchemaBuilder {

    BlockBuilder(@NotNull Project project, @NotNull DataAccess data, @NotNull File schemaFile) {
        super(project, data, schemaFile);
    }

    @Override
    protected @NotNull JsonObject generateSchema() {
        JsonObject schema = new JsonObject();
        schema.addProperty("$schema", "https://json-schema.org/draft/2020-12/schema");

        // The Configured Block type is either just a string of the legacyBlock type, or one that supports a blocks properties
        JsonArray oneOf = new JsonArray();

        // The type object that is just purely a string, and an enum with all the possible legacyBlock keys
        JsonObject blockKeyOnly = new JsonObject();
        blockKeyOnly.addProperty("type", "string");
        JsonArray blockKeys = new JsonArray();
        BlockRegistry.blockKeys().forEach(key -> blockKeys.add(key.asString()));
        blockKeyOnly.add("enum", blockKeys);
        oneOf.add(blockKeyOnly);

        // Add an object that maps each legacyBlock key with its properties if it has one.
        JsonArray requiredObject = new JsonArray();
        requiredObject.add("type");
        requiredObject.add("properties");
        for (Block block : BlockRegistry.blocks()) {
            if (!block.properties().isEmpty()) {
                JsonObject blockObject = new JsonObject();
                blockObject.addProperty("type", "object");
                JsonObject propertiesObject = new JsonObject();

                // Loop through all properties
                for (Property<?> property : block.properties()) {
                    JsonObject propertyObject = new JsonObject();
                    if (property instanceof IntegerProperty integerProperty) {
                        propertyObject.addProperty("type", "integer");
                        propertyObject.addProperty("minimum", integerProperty.min());
                        propertyObject.addProperty("maximum", integerProperty.max());
                    } else if (property instanceof BooleanProperty) {
                        propertiesObject.addProperty("type", "boolean");
                    } else if (property instanceof EnumProperty<?> enumProperty) {
                        propertyObject.addProperty("type", "string");
                        JsonArray enumNameArray = new JsonArray();
                        enumProperty.names().forEach(enumNameArray::add);
                        propertyObject.add("enum", enumNameArray);
                    }
                    propertiesObject.add(property.key(), propertyObject);
                }
                blockObject.add("required", requiredObject);
                oneOf.add(blockObject);
            }
        }
        schema.add("oneOf", oneOf);
        return schema;
    }

}
