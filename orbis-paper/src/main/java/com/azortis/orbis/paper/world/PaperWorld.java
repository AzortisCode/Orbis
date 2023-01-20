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
import com.azortis.orbis.entity.Player;
import com.azortis.orbis.world.ChunkAccess;
import com.azortis.orbis.world.World;
import com.azortis.orbis.world.WorldAccess;
import com.google.common.base.Preconditions;
import org.apiguardian.api.API;
import org.bukkit.Bukkit;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.Set;

public final class PaperWorld extends World {

    private final WorldInfo worldInfo;
    private org.bukkit.World nativeWorld;
    private WorldAccess worldAccess;

    public PaperWorld(@NotNull WorldInfo worldInfo) {
        super(worldInfo.getName(), new File(Bukkit.getWorldContainer() + "/" + worldInfo.getName() + "/"));
        this.worldInfo = worldInfo;
    }

    public WorldInfo getNativeWorldInfo() {
        return worldInfo;
    }

    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.paper")
    public void setWorldAccess(@NotNull WorldAccess worldAccess) throws IllegalStateException {
        if (this.worldAccess == null) {
            this.worldAccess = worldAccess;
            this.nativeWorld = ((PaperWorldAccess) worldAccess).handle();
        } else throw new IllegalStateException("WorldAccess for " + worldInfo.getName() + " has already been set!");
    }

    public @NotNull org.bukkit.World nativeWorld() {
        Preconditions.checkNotNull(nativeWorld, "Native world hasn't been set yet!");
        return nativeWorld;
    }

    //
    // WorldAccess
    //

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWorldLoaded() {
        return worldAccess.isWorldLoaded();
    }

    @Override
    public int minHeight() {
        return worldAccess.minHeight();
    }

    @Override
    public int maxHeight() {
        return worldAccess.maxHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Set<Player> getPlayers() {
        return worldAccess.getPlayers();
    }

    @Override
    public boolean isChunkGenerated(int chunkX, int chunkZ) {
        return worldAccess.isChunkGenerated(chunkX, chunkZ);
    }

    @Override
    public boolean isChunkLoaded(int chunkX, int chunkZ) {
        return worldAccess.isChunkLoaded(chunkX, chunkZ);
    }

    @Override
    public @NotNull @Unmodifiable Set<ChunkAccess> getLoadedChunks() {
        return worldAccess.getLoadedChunks();
    }

    @Override
    public @Nullable ChunkAccess getChunk(int chunkX, int chunkZ) {
        return worldAccess.getChunk(chunkX, chunkZ);
    }

    @Override
    public @NotNull BlockState getBlockState(int x, int y, int z) {
        return worldAccess.getBlockState(x, y, z);
    }

    @Override
    public void setBlockState(int x, int y, int z, @NotNull BlockState blockState) {
        worldAccess.setBlockState(x, y, z, blockState);
    }
}
