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

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class AbstractProperty<T> implements Property<T> {

    private final String name;
    private final Collection<T> values;

    public AbstractProperty(final @NotNull String name, final @NotNull Collection<T> values) {
        this.name = name;
        this.values = values;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull Collection<T> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Property<?> property)) return false;
        return name.equals(property.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{name=" + name + "}";
    }
}
