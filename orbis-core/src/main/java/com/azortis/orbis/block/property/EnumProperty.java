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

import com.azortis.orbis.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class EnumProperty<T extends Enum<T> & StringRepresentable> extends AbstractProperty<T> {

    private final Map<String, T> names = new HashMap<>();

    public EnumProperty(final @NotNull String name, final @NotNull Set<T> values) {
        super(name, values);
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
}
