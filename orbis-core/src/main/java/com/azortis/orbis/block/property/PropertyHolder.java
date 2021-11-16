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

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PropertyHolder {

    private static final Map<String, Property<?>> PROPERTIES = new ConcurrentHashMap<>();
    private static final Map<String, String> NAME_REWRITES = Map.of();

    static {
        for (final Field field : Properties.class.getDeclaredFields()) {
            try {
                PROPERTIES.put(NAME_REWRITES.getOrDefault(field.getName(), field.getName()), (Property<?>) field.get(null));
            } catch (final IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static Property<?> getByName(final String name) {
        return PROPERTIES.get(name);
    }
}
