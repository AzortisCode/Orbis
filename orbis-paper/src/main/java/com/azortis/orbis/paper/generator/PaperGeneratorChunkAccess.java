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

package com.azortis.orbis.paper.generator;

import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.block.Blocks;
import com.azortis.orbis.paper.util.ConversionUtils;
import com.azortis.orbis.world.ChunkAccess;
import com.azortis.orbis.world.WorldAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.craftbukkit.v1_19_R2.generator.CraftChunkData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public final class PaperGeneratorChunkAccess implements ChunkAccess {

    private final WorldAccess world;
    private final CraftChunkData handle;
    private boolean loaded = true;

    public PaperGeneratorChunkAccess(@NotNull WorldAccess world, @NotNull CraftChunkData handle) {
        this.world = world;
        this.handle = handle;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }


    @Override
    public void unload() {
        loaded = false;
    }

    @Override
    public @NotNull BlockState getState(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z) {
        return ConversionUtils.fromNative(handle.getHandle().getBlockState(x, y, z));
    }

    @Override
    public void setState(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z,
                         @Nullable BlockState state) {
        if (state != null) {
            setBlock(x, y, z, state.stateId());
            return;
        }
        setBlock(x, y, z, Blocks.AIR.stateId());
    }

    private void setBlock(int x, int y, int z, int stateId) {
        if (x != (x & 0xf) || y < world.minHeight() || y >= world.maxHeight() || z != (z & 0xf)) {
            return;
        }

        net.minecraft.world.level.block.state.BlockState blockState = Block.stateById(stateId);
        net.minecraft.world.level.chunk.ChunkAccess access = handle.getHandle();
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
