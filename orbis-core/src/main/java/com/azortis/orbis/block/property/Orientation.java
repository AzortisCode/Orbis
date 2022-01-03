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

public enum Orientation implements Nameable {
    UP_NORTH("up_north"),
    UP_EAST("up_east"),
    UP_SOUTH("up_south"),
    UP_WEST("up_west"),
    DOWN_NORTH("down_north"),
    DOWN_EAST("down_east"),
    DOWN_SOUTH("down_south"),
    DOWN_WEST("down_west"),
    NORTH_UP("north_up"),
    EAST_UP("east_up"),
    SOUTH_UP("south_up"),
    WEST_UP("west_up");

    private final String name;

    Orientation(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
