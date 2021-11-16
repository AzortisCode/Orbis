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

package com.azortis.orbis.paper.generator;

import com.azortis.orbis.block.Block;
import com.azortis.orbis.block.BlockLoader;
import com.azortis.orbis.generator.ChunkData;
import com.azortis.orbis.generator.Dimension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.bukkit.craftbukkit.v1_17_R1.generator.CraftChunkData;
import org.bukkit.generator.ChunkGenerator;

public class PaperChunkData extends ChunkData {

    private final CraftChunkData handle;

    public PaperChunkData(Dimension dimension, ChunkGenerator.ChunkData handle) {
        super(dimension);
        this.handle = (CraftChunkData) handle;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return BlockLoader.fromStateId(net.minecraft.world.level.block.Block.getId(handle.getTypeId(x, y, z)));
    }

    @Override
    protected void setBlock(int x, int y, int z, int stateId) {
        setBlock(handle, x, y, z, net.minecraft.world.level.block.Block.stateById(stateId));
    }

    public ChunkGenerator.ChunkData getHandle() {
        return handle;
    }

    // TODO: Desperately try and come up with a better solution than just copying the private method out so we can
    //  use it
    private static void setBlock(CraftChunkData data, int x, int y, int z, BlockState type) {
        if (x != (x & 0xf) || y < data.getMinHeight() || y >= data.getMaxHeight() || z != (z & 0xf)) {
            return;
        }

        ChunkAccess access = data.getHandle();
        BlockPos blockPosition = new BlockPos(access.getPos().getMinBlockX() + x, y, access.getPos().getMinBlockZ() + z);
        BlockState oldBlockData = access.setBlockState(blockPosition, type, false);

        if (type.hasBlockEntity()) {
            BlockEntity tileEntity = ((EntityBlock) type.getBlock()).newBlockEntity(blockPosition, type);

            // createTile can return null, currently only the case with material MOVING_PISTON
            if (tileEntity == null) {
                access.removeBlockEntity(blockPosition);
            } else {
                access.setBlockEntity(tileEntity);
            }
        } else if (oldBlockData != null && oldBlockData.hasBlockEntity()) {
            access.removeBlockEntity(blockPosition);
        }
    }
}
