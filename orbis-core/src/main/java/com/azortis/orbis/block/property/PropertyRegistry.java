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

package com.azortis.orbis.block.property;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * A registry that keeps track of all {@link Property} in {@link Properties} and
 * the original Mojang name associated with it, if changed.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public final class PropertyRegistry {

    /**
     * A collection mapping each Mojang property name to ours if we'd like the name
     * to be written to something else.
     */
    public static final ImmutableMap<String, String> NAME_REWRITES = ImmutableMap.of(
            "ROTATION_16", "ROTATION",
            "MODE_COMPARATOR", "COMPARATOR_MODE",
            "STRUCTUREBLOCK_MODE", "STRUCTURE_BLOCK_MODE",
            "NOTEBLOCK_INSTRUMENT", "NOTE_BLOCK_INSTRUMENT");

    /**
     * A collection mapping each {@link Property} its field name in {@link Properties}
     * to its instance.
     */
    public static final ImmutableMap<String, Property<?>> PROPERTIES;

    static {
        // Lazily map all the property names from Properties when this class is called.
        ImmutableMap.Builder<String, Property<?>> builder = ImmutableMap.builder();
        for (final Field field : Properties.class.getDeclaredFields()) {
            try {
                builder.put(field.getName(), (Property<?>) field.get(Property.class));
            } catch (final IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }
        PROPERTIES = builder.build();
    }

    /**
     * Gets a {@link Property} by its name defined in {@link Properties}.
     *
     * @param name The name of the property.
     * @return The corresponding property.
     * @throws IllegalArgumentException If property name is invalid.
     * @since 0.3-Alpha
     */
    @SuppressWarnings("ConstantConditions")
    @API(status = API.Status.STABLE, since = "0.3-Alpha")
    public static @NotNull Property<?> getByName(@NotNull String name) throws IllegalArgumentException {
        Preconditions.checkArgument(PROPERTIES.containsKey(name), "Invalid property name!");
        return PROPERTIES.get(name);
    }

    /**
     * Gets a {@link Property} by its Mojang name.
     *
     * @param mojangName The mojang name of the property.
     * @return The corresponding property.
     * @throws IllegalArgumentException If property name is invalid.
     * @since 0.3-Alpha
     */
    @SuppressWarnings("ConstantConditions")
    @API(status = API.Status.STABLE, since = "0.3-Alpha")
    public static @NotNull Property<?> getByMojangName(@NotNull String mojangName) throws IllegalArgumentException {
        Preconditions.checkArgument(PROPERTIES.containsKey(NAME_REWRITES.getOrDefault(mojangName, mojangName)),
                "Invalid mojang property name!");
        return PROPERTIES.get(NAME_REWRITES.getOrDefault(mojangName, mojangName));
    }

}
