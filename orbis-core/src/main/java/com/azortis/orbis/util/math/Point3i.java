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

package com.azortis.orbis.util.math;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Point3i(int x, int y, int z) {

    public static final Point3i ZERO = new Point3i(0, 0, 0);

    @Contract("_ -> new")
    public @NotNull Point3i setX(final int x) {
        return new Point3i(x, y, z);
    }

    @Contract("_ -> new")
    public @NotNull Point3i setY(final int y) {
        return new Point3i(x, y, z);
    }

    @Contract("_ -> new")
    public @NotNull Point3i setZ(final int z) {
        return new Point3i(x, y, z);
    }

}
