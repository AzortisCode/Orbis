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

package com.azortis.orbis.paper.world;

import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.paper.util.ConversionUtils;
import com.azortis.orbis.world.ChunkAccess;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used for chunks in any state, including those not yet fully generated, which the
 * Bukkit API cannot represent, and so we use vanilla's ChunkAccess class as the handle.
 */
public final class PaperProtoChunkAccess implements ChunkAccess {

    private final net.minecraft.world.level.chunk.ChunkAccess handle;

    public PaperProtoChunkAccess(final net.minecraft.world.level.chunk.ChunkAccess handle) {
        this.handle = handle;
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public void unload() {
        // TODO: Should we do something here for this type of chunk access?
    }

    @Override
    public int chunkX() {
        return handle.getPos().x;
    }

    @Override
    public int chunkZ() {
        return handle.getPos().z;
    }

    @Override
    public boolean checkBounds(int x, int y, int z) {
        return handle.isOutsideBuildHeight(y) ||
                x < handle.getPos().getMinBlockX() || x > handle.getPos().getMaxBlockX() ||
                z < handle.getPos().getMinBlockZ() || z > handle.getPos().getMaxBlockZ();
    }

    @Override
    public @NotNull BlockState getState(int x, int y, int z) {
        return ConversionUtils.fromNative(handle.getBlockState(x, y, z));
    }

    @Override
    public void setState(int x, int y, int z, @NotNull BlockState state) {
        handle.setBlockState(new BlockPos(x, y, z), ConversionUtils.toNative(state), false);
    }
}
