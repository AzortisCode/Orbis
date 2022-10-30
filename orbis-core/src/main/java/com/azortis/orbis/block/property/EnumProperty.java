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

import com.azortis.orbis.util.Nameable;
import com.google.common.collect.ImmutableSet;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A {@link Property} that can be either one of the given enums.
 *
 * @param <T> The type of enum for this property.
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public final class EnumProperty<T extends Enum<T> & Nameable> extends Property<T> {

    private final Map<String, T> names = new HashMap<>();

    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.block.property")
    private EnumProperty(final @NotNull String key, final Class<T> type, final @NotNull Set<T> values) {
        super(key, type, values);
        for (final T value : values) {
            final String name = value.serializedName();
            if (names.containsKey(name)) {
                throw new IllegalArgumentException("Multiple values have the same fieldName! Name: " + name);
            }
            names.put(value.serializedName().toLowerCase(Locale.ENGLISH), value);
        }
    }

    @Contract("_,_ -> new")
    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.block.property")
    static <T extends Enum<T> & Nameable> EnumProperty<T> create(final @NotNull String key,
                                                                 final @NotNull Class<T> type) {
        return create(key, type, t -> true);
    }

    @Contract("_,_,_ -> new")
    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.block.property")
    static <T extends Enum<T> & Nameable> EnumProperty<T> create(final @NotNull String key,
                                                                 final @NotNull Class<T> type,
                                                                 final @NotNull Predicate<T> filter) {
        return create(key, type, Arrays.stream(type.getEnumConstants()).filter(filter).collect(Collectors.toSet()));
    }

    @SafeVarargs
    @Contract("_,_,_ -> new")
    @SuppressWarnings("SameParameterValue")
    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.block.property")
    static <T extends Enum<T> & Nameable> EnumProperty<T> create(final @NotNull String key,
                                                                 final Class<T> type,
                                                                 final @NotNull T... values) {
        return create(key, type, Set.of(values));
    }

    @Contract("_,_,_ -> new")
    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.block.property")
    static <T extends Enum<T> & Nameable> EnumProperty<T> create(final @NotNull String key,
                                                                 final @NotNull Class<T> type,
                                                                 final @NotNull Set<T> values) {
        return new EnumProperty<>(key, type, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull T getValue(@NotNull String value) {
        value = value.toLowerCase(Locale.ENGLISH);
        if (!names.containsKey(value)) {
            throw new IllegalArgumentException("Invalid value: " + value + ". Must be in " + names.keySet());
        }
        return names.get(value);
    }

    @Contract(" -> new")
    public @NotNull ImmutableSet<String> names() {
        return ImmutableSet.copyOf(names.keySet());
    }

}
