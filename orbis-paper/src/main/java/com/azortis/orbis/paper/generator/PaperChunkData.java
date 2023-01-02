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

package com.azortis.orbis.paper.generator;

import com.azortis.orbis.block.BlockRegistry;
import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.generator.ChunkData;
import com.azortis.orbis.generator.Dimension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.bukkit.craftbukkit.v1_19_R2.generator.CraftChunkData;
import org.bukkit.generator.ChunkGenerator;

/**
 * More efficient ChunkData since we're skipping the whole {@link org.bukkit.block.data.BlockData} conversion,
 * and directly write using NMS its {@link net.minecraft.world.level.block.state.BlockState}
 */
public final class PaperChunkData extends ChunkData {

    private final CraftChunkData handle;

    public PaperChunkData(Dimension dimension, ChunkGenerator.ChunkData handle) {
        super(dimension);
        this.handle = (CraftChunkData) handle;
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        return BlockRegistry.fromStateId(net.minecraft.world.level.block.Block.getId(handle.getTypeId(x, y, z)));
    }

    @Override
    protected void setBlock(int x, int y, int z, int stateId) {
        if (x != (x & 0xf) || y < dimension.minHeight() || y >= dimension.maxHeight() || z != (z & 0xf)) {
            return;
        }

        net.minecraft.world.level.block.state.BlockState blockState = Block.stateById(stateId);
        ChunkAccess access = handle.getHandle();
        BlockPos blockPos = new BlockPos(access.getPos().getMinBlockX() + x, y, access.getPos().getMinBlockZ() + z);
        net.minecraft.world.level.block.state.BlockState oldBlockState = access.setBlockState(blockPos, blockState, false);

        if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = ((EntityBlock) blockState.getBlock()).newBlockEntity(blockPos, blockState);

            // createTile can return null, currently only the case with material MOVING_PISTON
            if (blockEntity == null) {
                access.removeBlockEntity(blockPos);
            } else {
                access.setBlockEntity(blockEntity);
            }
        } else if (oldBlockState != null && oldBlockState.hasBlockEntity()) {
            access.removeBlockEntity(blockPos);
        }
    }

}
