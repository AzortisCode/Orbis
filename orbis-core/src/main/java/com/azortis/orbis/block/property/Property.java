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

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public sealed abstract class Property<T extends Comparable<T>> permits BooleanProperty,
        IntegerProperty, EnumProperty {

    private final String key;
    private final Class<T> type;
    private final ImmutableSet<T> values;

    public Property(String key, Class<T> type, Set<T> values) {
        this.key = key;
        this.type = type;
        this.values = ImmutableSet.copyOf(values);
    }

    public String key() {
        return key;
    }

    public Class<T> type() {
        return type;
    }

    public ImmutableSet<T> values() {
        return values;
    }

    public abstract @Nullable T getValueFor(final @NotNull String value);

}
