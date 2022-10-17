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

package com.azortis.orbis.util;

import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A 3 dimensional integer Vector that resembles the position of a legacyBlock.
 *
 * @since 0.3-Alpha
 * @author Jake Nijssen
 * @param x The x-coordinate of the position.
 * @param y The y-coordinate of the position.
 * @param z The z-coordinate of the position.
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public record BlockPos(int x, int y, int z) {

    @Contract("_ -> new")
    public @NotNull BlockPos setX(final int x) {
        return new BlockPos(x, y, z);
    }

    @Contract("_ -> new")
    public @NotNull BlockPos setY(final int y) {
        return new BlockPos(x, y, z);
    }

    @Contract("_ -> new")
    public @NotNull BlockPos setZ(final int z) {
        return new BlockPos(x, y, z);
    }

}
