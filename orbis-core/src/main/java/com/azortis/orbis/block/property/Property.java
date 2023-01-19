/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2023 Azortis
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

import com.google.common.collect.ImmutableSet;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Represents a property that exists on Blocks which alter the looks of that block.
 *
 * @param <T> The type of property.
 * @author Jake Nijssen
 * @see <a href="https://minecraft.fandom.com/wiki/Block_states#:~:text=Block%20states%20(also%20known%20as,Metadata)%20to%20define%20a%20block.">Properties</a> for more information.
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public sealed abstract class Property<T extends Comparable<T>> permits BooleanProperty,
        IntegerProperty, EnumProperty {

    private final String key;
    private final Class<T> type;
    private final ImmutableSet<T> values;

    /**
     * Constructs a property with specified key, type and values.
     *
     * @param key    The key of the property.
     * @param type   The type of the property.
     * @param values The possible values of the property.
     * @since 0.3-Alpha
     */
    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.block.property")
    protected Property(@NotNull String key, @NotNull Class<T> type, @NotNull Set<T> values) {
        this.key = key;
        this.type = type;
        this.values = ImmutableSet.copyOf(values);
    }

    /**
     * Gets the key of the property.
     *
     * @return The property key.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    public @NotNull String key() {
        return key;
    }

    /**
     * Gets the type of the property.
     *
     * @return The property type.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    public @NotNull Class<T> type() {
        return type;
    }

    /**
     * Gets an immutable set of the possible values of the property.
     *
     * @return Immutable set of possible values.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    public @NotNull ImmutableSet<T> values() {
        return values;
    }

    /**
     * Gets the typed value for the given string.
     *
     * @param value The string of the value.
     * @return The typed value.
     * @throws IllegalArgumentException If no value is found for given string.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    public abstract @NotNull T getValue(final @NotNull String value) throws IllegalArgumentException;

}
