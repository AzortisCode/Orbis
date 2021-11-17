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
import com.azortis.orbis.block.property.PropertyHolder;
import com.azortis.orbis.utils.NamespaceId;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class BlockLoader {

    private static final Map<String, Block> KEY_MAP = new HashMap<>();
    private static final Map<String, PropertyEntry> PROPERTY_MAP = new HashMap<>();
    private static final Int2ObjectMap<Block> STATES = new Int2ObjectOpenHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockLoader.class);
    private static final String FILE_NAME = "1_17_1_blocks.json";
    private static volatile boolean isLoaded = false;

    public static void init() {
        if (isLoaded) {
            LOGGER.warn("");
            return;
        }

        final InputStream inputStream = ClassLoader.getSystemResourceAsStream(FILE_NAME);
        if (inputStream == null) throw new IllegalStateException("Could not find " + FILE_NAME + " bundled in JAR!");
        final JsonObject data = Orbis.getGson().fromJson(new InputStreamReader(inputStream), JsonObject.class);
        load(data);
        isLoaded = true;
    }

    public static @Nullable Block fromStateId(final int stateId) {
        return STATES.get(stateId);
    }

    public static @Nullable Block fromKey(final String key) {
        final String id = key.indexOf(':') == -1 ? "minecraft:" + key : key;
        return KEY_MAP.get(id);
    }

    public static @Nullable Block fromKey(final NamespaceId key) {
        return fromKey(key.toString());
    }

    public static @Nullable Block getWithProperties(final String key, final Map<String, String> properties) {
        final PropertyEntry entry = PROPERTY_MAP.get(key);
        return entry == null ? null : entry.properties.get(properties);
    }

    private static void load(final JsonObject data) {
        data.entrySet().forEach(entry -> {
            final String key = entry.getKey();
            final JsonObject value = (JsonObject) entry.getValue();

            // Map properties
            final PropertyEntry propertyEntry = new PropertyEntry();
            final ImmutableSet.Builder<Property<?>> availablePropertySet = ImmutableSet.builder();
            value.get("properties").getAsJsonArray().forEach(element -> {
                final String string = element.getAsString();
                availablePropertySet.add(PropertyHolder.getByName(string));
            });
            final ImmutableSet<Property<?>> availableProperties = availablePropertySet.build();

            // Iterate states
            value.remove("states").getAsJsonArray().forEach(element -> {
                final StateEntry state = getState(element.getAsJsonObject(), key, availableProperties, value);
                propertyEntry.properties.put(state.properties(), state.block());
            });

            // Get default state and add to map
            final int defaultState = value.get("defaultStateId").getAsInt();
            final Block defaultBlock = STATES.get(defaultState);
            KEY_MAP.put(key, defaultBlock);
            PROPERTY_MAP.put(key, propertyEntry);
        });
    }

    private static StateEntry getState(final JsonObject data, final String key,
                                       final Set<Property<?>> availableProperties, final JsonObject blockObject) {
        final int stateId = data.get("stateId").getAsInt();
        final ImmutableMap.Builder<String, String> propertyMap = new ImmutableMap.Builder<>();
        data.get("properties").getAsJsonObject().entrySet().forEach(entry -> {
            propertyMap.put(entry.getKey(), entry.getValue().getAsString().toLowerCase(Locale.ROOT));
        });
        final ImmutableMap<String, String> properties = propertyMap.build();
        final Block block = new BlockImpl(
                new NamespaceId(key),
                blockObject.get("id").getAsInt(),
                data.get("stateId").getAsInt(),
                data.get("air").getAsBoolean(),
                data.get("solid").getAsBoolean(),
                data.get("liquid").getAsBoolean(),
                availableProperties,
                properties
        );
        STATES.put(stateId, block);
        return new StateEntry(properties, block);
    }

    private static final class PropertyEntry {

        public final Map<Map<String, String>, Block> properties = new ConcurrentHashMap<>();
    }

    private record StateEntry(Map<String, String> properties, Block block) {
    }
}
