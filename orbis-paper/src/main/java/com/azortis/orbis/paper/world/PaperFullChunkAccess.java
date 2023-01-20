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
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used for full chunks, which the Bukkit API can represent, and so we use Bukkit's Chunk
 * interface as the handle.
 */
public final class PaperFullChunkAccess implements ChunkAccess {

    private final Chunk handle;

    public PaperFullChunkAccess(final Chunk handle) {
        this.handle = handle;
    }

    @Override
    public boolean isLoaded() {
        return handle.isLoaded();
    }

    @Override
    public void unload() {
        handle.unload();
    }

    @Override
    public int chunkX() {
        return handle.getX();
    }

    @Override
    public int chunkZ() {
        return handle.getZ();
    }

    @Override
    public boolean checkBounds(int x, int y, int z) {
        return y < handle.getWorld().getMinHeight() || y >= handle.getWorld().getMaxHeight() ||
                x < getBlockX(0) || x > getBlockX(15) ||
                z < getBlockZ(0) || z > getBlockZ(15);
    }

    private int getBlockX(int offset) {
        return (handle.getX() << 4) + offset;
    }

    private int getBlockZ(int offset) {
        return (handle.getZ() << 4) + offset;
    }

    @Override
    public @NotNull BlockState getState(int x, int y, int z) {
        return ConversionUtils.fromPaper(handle.getBlock(x, y, z).getBlockData());
    }

    @Override
    public void setState(int x, int y, int z, @NotNull BlockState state) {
        handle.getBlock(x, y, z).setBlockData(ConversionUtils.toPaper(state));
    }
}
