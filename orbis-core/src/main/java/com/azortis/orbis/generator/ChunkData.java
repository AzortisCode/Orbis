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

package com.azortis.orbis.generator;

import com.azortis.orbis.block.BlockState;

public abstract class ChunkData {
    private final Dimension dimension;

    public ChunkData(Dimension dimension) {
        this.dimension = dimension;
    }

    public abstract BlockState getBlock(final int x, final int y, final int z);

    /**
     * Stores a block in the chunk. The specified coordinates are chunk positions.
     *
     * @param x          The x-coordinate in the chunk. Ranges from 0-15
     * @param y          The y-coordinate in the chunk Ranges from heightMin and heightMax
     * @param z          The z-coordinate in the chunk. Ranges from 0-15
     * @param blockState The block to put at the specified position.
     */
    public final void setBlock(final int x, final int y, final int z, final BlockState blockState) {
        setBlock(x, y, z, blockState.stateId());
    }

    protected abstract void setBlock(final int x, final int y, final int z, final int stateId);
}
