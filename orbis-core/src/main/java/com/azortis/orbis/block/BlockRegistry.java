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
import com.azortis.orbis.utils.NamespaceId;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public final class BlockRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockRegistry.class);
    private static final Map<String, Block> KEY_MAP = new HashMap<>();
    private static final Int2ObjectMap<Block> STATE_MAP = new Int2ObjectOpenHashMap<>();
    private static final String BLOCKS_DATA = "blocks.json";
    private static volatile boolean loaded = false;

    public static void init(){
        try {
            if(loaded){
                File blocksDataFile = Orbis.getDataFile(BLOCKS_DATA);
                assert blocksDataFile != null;
                final JsonObject data = Orbis.getGson().fromJson(new FileReader(blocksDataFile), JsonObject.class);
                load(data);
                loaded = true;
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static boolean containsKey(final String key){
        final String id = key.indexOf(':') == -1 ? "minecraft:" + key : key;
        return KEY_MAP.containsKey(key);
    }

    public static Block fromKey(final String key){
        final String id = key.indexOf(':') == -1 ? "minecraft:" + key : key;
        return KEY_MAP.get(id);
    }

    public static boolean containsKey(final NamespaceId key){
        return KEY_MAP.containsKey(key.getNamespaceId());
    }

    public static Block fromKey(final NamespaceId key){
        return KEY_MAP.get(key.getNamespaceId());
    }

    public static Block fromStateId(final int stateId){
        return STATE_MAP.get(stateId);
    }

    private static void load(final JsonObject data){
        data.entrySet().forEach(entry -> {
            final String key = entry.getKey();
            final NamespaceId namespacedKey = new NamespaceId(key);
            final JsonObject block = entry.getValue().getAsJsonObject();
            final int id = block.get("id").getAsInt();
            final int defaultStateId = block.get("defaultStateId").getAsInt();

            Map<String, Property<?>> availableProperties = new HashMap<>();
            for (JsonElement propertyElement : block.getAsJsonArray("properties")){
                String mojangName = propertyElement.getAsString();
                Property<?> property = PropertyRegistry.getByMojangName(mojangName);
                availableProperties.put(property.getKey(), property);
            }

            // Find default block state first, then remove that entry from the states.
            Map<Map<Property<?>, Comparable<?>>, BlockImpl> blockStates = new HashMap<>();
            BlockImpl defaultBlock = null;
            JsonArray states = block.getAsJsonArray("states");
            JsonElement elementToRemove = null;
            for (JsonElement stateElement : states){
                final JsonObject state = stateElement.getAsJsonObject();
                final int stateId = state.get("stateId").getAsInt();
                if(stateId == defaultStateId){
                    ImmutableMap<Property<?>, Comparable<?>> propertyMap = getPropertyMap(availableProperties, state);
                    defaultBlock = new BlockImpl(
                            namespacedKey,
                            id,
                            stateId,
                            state.get("air").getAsBoolean(),
                            state.get("solid").getAsBoolean(),
                            state.get("liquid").getAsBoolean(),
                            propertyMap,
                            null
                    );
                    blockStates.put(propertyMap, defaultBlock);
                    elementToRemove = stateElement;
                    break;
                }
            }
            states.remove(elementToRemove);
            for (JsonElement stateElement : states){
                final JsonObject state = stateElement.getAsJsonObject();
                final int stateId = state.get("stateId").getAsInt();
                ImmutableMap<Property<?>, Comparable<?>> propertyMap = getPropertyMap(availableProperties, state);
                BlockImpl block1 = new BlockImpl(
                        namespacedKey,
                        id,
                        stateId,
                        state.get("air").getAsBoolean(),
                        state.get("solid").getAsBoolean(),
                        state.get("liquid").getAsBoolean(),
                        propertyMap,
                        defaultBlock
                );
                blockStates.put(propertyMap, block1);
            }
            blockStates.values().forEach(block1 -> {
                block1.populateNeighbours(blockStates);
                STATE_MAP.put(block1.getStateId(), block1);
            });
            KEY_MAP.put(key, defaultBlock);
        });
    }

    private static ImmutableMap<Property<?>, Comparable<?>> getPropertyMap(Map<String, Property<?>> availableProperties,
                                                                           final JsonObject state){
        JsonObject properties = state.getAsJsonObject("properties");
        ImmutableMap.Builder<Property<?>, Comparable<?>> builder = ImmutableMap.builder();
        for (Map.Entry<String, Property<?>> entry : availableProperties.entrySet()){
            String stringValue = properties.get(entry.getKey()).getAsString();
            Comparable<?> value = entry.getValue().getValueFor(stringValue);
            if(value != null){
                builder.put(entry.getValue(), value);
            }
        }
        return builder.build();
    }

    public static boolean isLoaded() {
        return loaded;
    }
}
