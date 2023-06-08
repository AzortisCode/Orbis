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
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.framework.ChunkSnapshot;
import com.azortis.orbis.generator.framework.Engine;
import com.azortis.orbis.paper.util.ConversionUtils;
import com.azortis.orbis.paper.world.PaperNativeHeightMap;
import com.azortis.orbis.world.Heightmap;
import com.azortis.orbis.world.World;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.craftbukkit.v1_19_R3.generator.CraftChunkData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public final class PaperChunkSnapshot extends ChunkSnapshot {

    private final CraftChunkData handle;
    private final int chunkX;
    private final int chunkZ;
    private boolean finished = false;

    public PaperChunkSnapshot(@NotNull World world, @NotNull Dimension dimension,
                              @NotNull Engine engine, @NotNull CraftChunkData handle, int chunkX, int chunkZ) {
        super(world, dimension, engine);
        this.handle = handle;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;

        // We make use of vanilla their default heightmaps for surface and ocean floor to prevent duplication
        addHeightMap(Heightmap.WG_SURFACE, new PaperNativeHeightMap(Heightmap.WG_SURFACE,
                handle.getHandle().getOrCreateHeightmapUnprimed
                        (net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG), chunkX, chunkZ));
        addHeightMap(Heightmap.WG_OCEAN_FLOOR, new PaperNativeHeightMap(Heightmap.WG_OCEAN_FLOOR,
                handle.getHandle().getOrCreateHeightmapUnprimed
                        (net.minecraft.world.level.levelgen.Heightmap.Types.OCEAN_FLOOR_WG), chunkX, chunkZ));
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    public void finish() {
        finished = true;
    }

    @Override
    public int chunkX() {
        return chunkX;
    }

    @Override
    public int chunkZ() {
        return chunkZ;
    }

    @Override
    public @NotNull BlockState getState(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z)
            throws IllegalArgumentException, IllegalStateException {
        if (isFinished()) throw new IllegalStateException(String.format("ChunkSnapshot [%s,%s] is finished",
                chunkX, chunkZ));
        return ConversionUtils.fromNative(handle.getHandle().getBlockState(x, y, z));
    }

    @Override
    public void setState(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z,
                         @Nullable BlockState state) throws IllegalArgumentException, IllegalStateException {
        if (isFinished()) throw new IllegalStateException(String.format("ChunkSnapshot [%s,%s] is finished",
                chunkX, chunkZ));
        if (state != null) {
            setBlock(x, y, z, state.stateId());
            return;
        }
        setBlock(x, y, z, Blocks.AIR.stateId());
    }

    private void setBlock(int x, int y, int z, int stateId) throws IllegalArgumentException {
        if (x != (x & 0xf) || y < world.minHeight() || y >= world.maxHeight() || z != (z & 0xf)) {
            throw new IllegalArgumentException(String.format("Illegal relative block coordinates for chunk [%s,%s,%s]",
                    x, y, z));
        }

        net.minecraft.world.level.block.state.BlockState blockState = Block.stateById(stateId);
        net.minecraft.world.level.chunk.ChunkAccess access = handle.getHandle();
        BlockPos blockPos = new BlockPos(access.getPos().getMinBlockX() + x, y, access.getPos().getMinBlockZ() + z);
        net.minecraft.world.level.block.state.BlockState oldBlockState = access.setBlockState(blockPos, blockState, false);

        if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = ((EntityBlock) blockState.getBlock()).newBlockEntity(blockPos, blockState);

            // newBlockEntity can return null, currently only the case with material MOVING_PISTON
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
