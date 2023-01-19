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

package com.azortis.orbis.exception;

import com.azortis.orbis.util.BlockPos;
import com.azortis.orbis.world.ChunkAccess;
import com.azortis.orbis.world.RegionAccess;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown to indicate that coordinates(2 dimensional or 3 dimensional) are not within
 * the bounds of given range.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
public class CoordsOutOfBoundsException extends RuntimeException {

    public CoordsOutOfBoundsException() {
        super();
    }

    public CoordsOutOfBoundsException(@NotNull String message) {
        super(message);
    }

    public CoordsOutOfBoundsException(int y, int minY, int maxY) {
        super(String.format("Y-coordinate %s is not withing the bounds of %s to %s", y, minY, maxY));
    }

    public CoordsOutOfBoundsException(int x, int z, @NotNull ChunkAccess chunk) {
        super(String.format("Coordinate [%s,%s] is not within the bounds of chunk [%s,%s]",
                x, z, chunk.chunkX(), chunk.chunkZ()));
    }

    public CoordsOutOfBoundsException(int x, int y, int z, @NotNull ChunkAccess chunk) {
        super(String.format("Coordinate [%s,%s,%s] is not within the bounds of chunk [%s,%s]",
                x, y, z, chunk.chunkX(), chunk.chunkZ()));
    }

    public CoordsOutOfBoundsException(@NotNull BlockPos pos, @NotNull ChunkAccess chunk) {
        this(pos.x(), pos.y(), pos.z(), chunk);
    }

    public CoordsOutOfBoundsException(int x, int y, int z, @NotNull RegionAccess region) {
        super(String.format("Coordinate [%s,%s,%s] is not within the bounds of region min=[%s,%s,%s], max=[%s,%s,%s]",
                x, y, z, region.min().x(), region.min().y(), region.min().z(),
                region.max().x(), region.max().y(), region.max().z()));
    }

    public CoordsOutOfBoundsException(@NotNull BlockPos pos, @NotNull RegionAccess region) {
        this(pos.x(), pos.y(), pos.z(), region);
    }

}
