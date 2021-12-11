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

import com.azortis.orbis.utils.Nameable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class EnumProperty<T extends Enum<T> & Nameable> extends Property<T> {

    private final Map<String, T> names = new HashMap<>();

    private EnumProperty(final @NotNull String key, final Class<T> type, final @NotNull Set<T> values) {
        super(key, type, values);
        for (final T value : values) {
            final String name = value.getSerializedName();
            if (names.containsKey(name)) {
                throw new IllegalArgumentException("Multiple values have the same name! Name: " + name);
            }
            names.put(value.getSerializedName(), value);
        }
    }

    @Override
    public @Nullable T getValueFor(@NotNull String value) {
        value = value.toLowerCase(Locale.ENGLISH);
        if (!names.containsKey(value)) {
            throw new IllegalArgumentException("Invalid value: " + value + ". Must be in " + names.keySet());
        }
        return names.get(value);
    }

    public Set<String> getNames() {
        return Collections.unmodifiableSet(names.keySet());
    }

    static <T extends Enum<T> & Nameable> EnumProperty<T> create(final @NotNull String key,
                                                                           final @NotNull Class<T> type) {
        return create(key, type, t -> true);
    }

    static <T extends Enum<T> & Nameable> EnumProperty<T> create(final @NotNull String key,
                                                                           final @NotNull Class<T> type,
                                                                           final @NotNull Predicate<T> filter) {
        return create(key, type, Arrays.stream(type.getEnumConstants()).filter(filter).collect(Collectors.toSet()));
    }

    @SuppressWarnings("SameParameterValue")
    @SafeVarargs
    static <T extends Enum<T> & Nameable> EnumProperty<T> create(final @NotNull String key,
                                                                           final Class<T> type,
                                                                           final @NotNull T... values) {
        return create(key, type, Set.of(values));
    }

    static <T extends Enum<T> & Nameable> EnumProperty<T> create(final @NotNull String key,
                                                                           final @NotNull Class<T> type,
                                                                           final @NotNull Set<T> values) {
        return new EnumProperty<>(key, type, values);
    }

}
