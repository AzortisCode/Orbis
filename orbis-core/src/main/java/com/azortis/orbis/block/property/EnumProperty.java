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

import com.azortis.orbis.util.Nameable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EnumProperty<T extends Enum<T> & Nameable> extends AbstractProperty<T> {

    private final Map<String, T> names = new HashMap<>();

    protected EnumProperty(final @NotNull String name, final Class<T> type, final @NotNull Set<T> values) {
        super(name, type, values);
        for (final T value : values) {
            final String key = value.getSerializedName();
            if (names.containsKey(key)) {
                throw new IllegalArgumentException("Multiple values have the same name! Name: " + key);
            }
            names.put(value.getSerializedName(), value);
        }
    }

    @Override
    public @Nullable T getValueFor(@NotNull String value) {
        final T val = names.get(value);
        if (!getValues().contains(val)) {
            throw new IllegalArgumentException("Invalid value: " + value + ". Must be in " + getValues());
        }
        return val;
    }

    public Set<String> getNames() {
        return Collections.unmodifiableSet(names.keySet());
    }

    protected static <T extends Enum<T> & Nameable> EnumProperty<T> create(final @NotNull String name,
                                                                           final @NotNull Class<T> type) {
        return create(name, type, t -> true);
    }

    protected static <T extends Enum<T> & Nameable> EnumProperty<T> create(final @NotNull String name,
                                                                           final @NotNull Class<T> type,
                                                                           final @NotNull Predicate<T> filter) {
        return create(name, type, Arrays.stream(type.getEnumConstants()).filter(filter).collect(Collectors.toSet()));
    }

    @SafeVarargs
    protected static <T extends Enum<T> & Nameable> EnumProperty<T> create(final @NotNull String name,
                                                                           final Class<T> type,
                                                                           final @NotNull T... values) {
        return create(name, type, Set.of(values));
    }

    protected static <T extends Enum<T> & Nameable> EnumProperty<T> create(final @NotNull String name,
                                                                           final @NotNull Class<T> type,
                                                                           final @NotNull Set<T> values) {
        return new EnumProperty<>(name, type, values);
    }

}
