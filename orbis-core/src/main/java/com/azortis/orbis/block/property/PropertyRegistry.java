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

package com.azortis.orbis.block.property;

import com.google.common.collect.ImmutableMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class PropertyRegistry {

    private static final Map<String, Property<?>> PROPERTIES = new HashMap<>();
    public static final ImmutableMap<String, String> NAME_REWRITES = ImmutableMap.of(
            "ROTATION_16", "ROTATION",
            "MODE_COMPARATOR", "COMPARATOR_MODE",
            "STRUCTUREBLOCK_MODE", "STRUCTURE_BLOCK_MODE",
            "NOTEBLOCK_INSTRUMENT", "NOTE_BLOCK_INSTRUMENT");
    private static volatile boolean loaded = false;

    public static void init() {
        if (!loaded) {
            for (final Field field : Properties.class.getDeclaredFields()) {
                try {
                    PROPERTIES.put(field.getName(), (Property<?>) field.get(Property.class));
                } catch (final IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
            loaded = true;
        }
    }

    public static Property<?> getByName(String name) {
        return PROPERTIES.get(name);
    }

    public static Property<?> getByMojangName(String mojangName) {
        return PROPERTIES.get(NAME_REWRITES.getOrDefault(mojangName, mojangName));
    }

}
