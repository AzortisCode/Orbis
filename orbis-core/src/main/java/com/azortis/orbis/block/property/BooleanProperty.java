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

import java.util.Set;

public non-sealed class BooleanProperty extends Property<Boolean> {

    public static final Set<Boolean> VALUES = Set.of(true, false);

    private BooleanProperty(final @NotNull String key) {
        super(key, Boolean.class, VALUES);
    }

    @Override
    public @NotNull Boolean getValueFor(@NotNull String value) {
        final boolean val = Boolean.parseBoolean(value);
        if (!getValues().contains(val)) {
            throw new IllegalArgumentException("Invalid boolean value: " + value + ". Must be in " + getValues());
        }
        return val;
    }

    protected static BooleanProperty create(final @NotNull String key) {
        return new BooleanProperty(key);
    }

}
