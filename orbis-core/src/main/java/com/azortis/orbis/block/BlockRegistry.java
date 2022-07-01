/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2021  Azortis
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

package com.azortis.orbis.block;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.block.property.Property;
import com.azortis.orbis.block.property.PropertyRegistry;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.kyori.adventure.key.Key;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class BlockRegistry {

    private static final Map<String, Block> KEY_BLOCK_MAP = new HashMap<>();
    private static final Int2ObjectMap<BlockState> ID_STATE_MAP = new Int2ObjectOpenHashMap<>();
    private static final String BLOCKS_DATA = "blocks.json";
    private static volatile boolean loaded = false;

    public static void init() {
        try {
            if (!loaded) {
                File blocksDataFile = Orbis.getDataFile(BLOCKS_DATA);
                assert blocksDataFile != null;
                final JsonObject data = Orbis.getGson().fromJson(new FileReader(blocksDataFile), JsonObject.class);
                load(data);
                loaded = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Set<String> blockKeys() {
        return Set.copyOf(KEY_BLOCK_MAP.keySet());
    }

    public static Set<Block> blocks() {
        return Set.copyOf(KEY_BLOCK_MAP.values());
    }

    public static boolean containsKey(final String key) {
        final String id = key.indexOf(':') == -1 ? "minecraft:" + key : key;
        return KEY_BLOCK_MAP.containsKey(id);
    }

    public static Block fromKey(final String key) {
        final String id = key.indexOf(':') == -1 ? "minecraft:" + key : key;
        return KEY_BLOCK_MAP.get(id);
    }

    public static boolean containsKey(final Key key) {
        return KEY_BLOCK_MAP.containsKey(key.asString());
    }

    public static Block fromKey(final Key key) {
        return KEY_BLOCK_MAP.get(key.asString());
    }

    public static BlockState fromStateId(final int stateId) {
        return ID_STATE_MAP.get(stateId);
    }

    public static boolean isLoaded() {
        return loaded;
    }

    @SuppressWarnings("PatternValidation")
    private static void load(final JsonObject data) {
        data.entrySet().forEach(entry -> {
            final Key key = Key.key(entry.getKey());
            final JsonObject blockData = entry.getValue().getAsJsonObject();
            final int id = blockData.get("id").getAsInt();
            final int defaultStateId = blockData.get("defaultStateId").getAsInt();


            ImmutableBiMap.Builder<String, Property<?>> propertiesBuilder = ImmutableBiMap.builder();
            for (JsonElement propertyElement : blockData.getAsJsonArray("properties")) {
                String mojangName = propertyElement.getAsString();
                Property<?> property = PropertyRegistry.getByMojangName(mojangName);
                propertiesBuilder.put(property.key(), property);
            }
            ImmutableBiMap<String, Property<?>> availableProperties = propertiesBuilder.build();

            Key itemKey = null;
            if (blockData.has("correspondingItem")) {
                itemKey = Key.key(blockData.get("correspondingItem").getAsString());
            }

            Block block = new Block(key, id, availableProperties.values(), itemKey);
            Map<Map<Property<?>, Comparable<?>>, BlockState> blockStates = new HashMap<>();
            JsonArray states = blockData.getAsJsonArray("states");
            for (JsonElement stateElement : states) {
                final JsonObject state = stateElement.getAsJsonObject();
                final int stateId = state.get("stateId").getAsInt();
                final ImmutableMap<Property<?>, Comparable<?>> values = getValues(availableProperties, state);

                BlockState blockState = new BlockState(block,
                        stateId,
                        state.get("air").getAsBoolean(),
                        state.get("solid").getAsBoolean(),
                        state.get("liquid").getAsBoolean(),
                        values);
                if (stateId == defaultStateId) block.setDefaultState(blockState);
                blockStates.put(values, blockState);
            }
            block.setStates(ImmutableSet.copyOf(blockStates.values()));
            blockStates.values().forEach(blockState -> {
                blockState.populateNeighbours(blockStates);
                ID_STATE_MAP.put(blockState.stateId(), blockState);
            });
            KEY_BLOCK_MAP.put(key.asString(), block);
        });
    }

    private static ImmutableMap<Property<?>, Comparable<?>> getValues(Map<String, Property<?>> availableProperties,
                                                                      final JsonObject state) {
        JsonObject properties = state.getAsJsonObject("properties");
        ImmutableMap.Builder<Property<?>, Comparable<?>> builder = ImmutableMap.builder();
        for (Map.Entry<String, Property<?>> entry : availableProperties.entrySet()) {
            String stringValue = properties.get(entry.getKey()).getAsJsonPrimitive().getAsString();
            Comparable<?> value = entry.getValue().getValueFor(stringValue);
            if (value != null) {
                builder.put(entry.getValue(), value);
            }
        }
        return builder.build();
    }

}
