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

import com.azortis.orbis.block.Block;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public abstract class ChunkData {
    private final Dimension dimension;
    private final Int2ObjectMap<Block> blockMap = new Int2ObjectOpenHashMap<>();
    private final int[] blocks;

    public ChunkData(Dimension dimension) {
        this.dimension = dimension;

        int ySize = Math.abs(dimension.getMinHeight()) + dimension.getMaxHeight();
        blocks = new int[16 * ySize * 16];
    }

    /**
     * Stores a block in the chunk. The specified coordinates are chunk positions.
     *
     * @param x     The x-coordinate in the chunk. Ranges from 0-15
     * @param y     The y-coordinate in the chunk Ranges from heightMin and heightMax
     * @param z     The z-coordinate in the chunk. Ranges from 0-15
     * @param block The block to put at the specified position.
     */
    public void setBlock(int x, int y, int z, Block block) {
        int blockHash = block.hashCode();
        if (!blockMap.containsKey(blockHash)) blockMap.put(blockHash, block);
        int yIndex = y + Math.abs(dimension.getMinHeight());
        blocks[x + z * 16 + yIndex * 256] = block.hashCode();
        setBlock(x, y, z, block.getBlock().getId());
    }

    public Block getBlock(int x, int y, int z) {
        int yIndex = y + Math.abs(dimension.getMinHeight());
        return blockMap.get(blocks[x + z * 16 + yIndex * 256]);
    }

    protected abstract void setBlock(int x, int y, int z, String blockId);

}
