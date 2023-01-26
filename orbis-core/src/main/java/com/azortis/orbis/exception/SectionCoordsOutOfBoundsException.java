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

import com.azortis.orbis.generator.framework.ChunkSnapshot;
import com.azortis.orbis.util.annotations.SectionCoords;
import org.jetbrains.annotations.NotNull;

public class SectionCoordsOutOfBoundsException extends RuntimeException {

    public SectionCoordsOutOfBoundsException() {
        super();
    }

    public SectionCoordsOutOfBoundsException(String message) {
        super(message);
    }

    @SectionCoords
    public SectionCoordsOutOfBoundsException(int x, int z, @NotNull ChunkSnapshot snapshot) {
        super(String.format("Section Coordinate [%s,%s] is not within the bounds of chunk [%s,%s]",
                x, z, snapshot.chunkX(), snapshot.chunkZ()));
    }

    @SectionCoords
    public SectionCoordsOutOfBoundsException(int x, int y, int z, @NotNull ChunkSnapshot snapshot) {
        super(String.format("Section Coordinate [%s,%s,%s] is not within the bounds of chunk [%s,%s]",
                x, y, z, snapshot.chunkX(), snapshot.chunkZ()));
    }

}
