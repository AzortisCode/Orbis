/*
 * MIT License
 *
 * Copyright (c) 2021 Azortis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.azortis.orbis.generator;

import com.azortis.orbis.block.Block;
import gnu.trove.map.hash.TIntObjectHashMap;

public abstract class ChunkData {
    private final Dimension dimension;
    private final TIntObjectHashMap<Block> blockMap = new TIntObjectHashMap<>();
    private final int[] blocks;

    public ChunkData(Dimension dimension) {
        this.dimension = dimension;

        int ySize = Math.abs(dimension.getMinHeight()) + dimension.getMaxHeight();
        blocks = new int[16 * ySize * 16];
    }

    /**
     * Stores a block in the chunk. The specified coordinates are chunk positions.
     *
     * @param x The x-coordinate in the chunk. Ranges from 0-15
     * @param y The y-coordinate in the chunk Ranges from heightMin and heightMax
     * @param z The z-coordinate in the chunk. Ranges from 0-15
     * @param block The block to put at the specified position.
     */
    public void setBlock(int x, int y, int z, Block block){
        int blockHash = block.hashCode();
        if(!blockMap.containsKey(blockHash))blockMap.put(blockHash, block);
        int yIndex = y + Math.abs(dimension.getMinHeight());
        blocks[x * yIndex * z] = block.hashCode();
        setBlock(x, y, z, block.getBlock().getId());
    }

    public Block getBlock(int x, int y, int z){
        int yIndex = y + Math.abs(dimension.getMinHeight());
        return blockMap.get(blocks[x * yIndex * z]);
    }

    protected abstract void setBlock(int x, int y, int z, String blockId);

}
