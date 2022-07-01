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

package com.azortis.orbis.pack.adapter;

import com.azortis.orbis.block.Block;
import com.azortis.orbis.block.ConfiguredBlock;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class BlockAdapter implements JsonSerializer<ConfiguredBlock>, JsonDeserializer<ConfiguredBlock> {

    @Override
    public ConfiguredBlock deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) { // If no BlockState properties have been configured
            return Block.fromKey(json.getAsString());
        }
        // It is a configured BlockState
        final JsonObject blockObject = json.getAsJsonObject();
        final Block block = Block.fromKey(blockObject.getAsJsonPrimitive("type").getAsString());
        final Map<String, String> properties = context.deserialize(json.getAsJsonObject().getAsJsonObject("properties"), Map.class);
        return block.withProperties(properties);
    }

    @Override
    public JsonElement serialize(ConfiguredBlock configuredBlock, Type typeOfSrc, JsonSerializationContext context) {
        if (configuredBlock.blockState() == configuredBlock.block().defaultState()) {
            return context.serialize(configuredBlock.key());
        }
        JsonObject blockObject = new JsonObject();
        blockObject.add("type", context.serialize(configuredBlock.key()));
        return blockObject;
    }
}
