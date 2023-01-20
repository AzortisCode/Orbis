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

package com.azortis.orbis.world;

import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Interface to interact with the platforms world.
 */
public interface WorldAccess {

    @NotNull
    String name();

    boolean isWorldLoaded();

    int minHeight();

    int maxHeight();

    @Unmodifiable @NotNull Set<Player> getPlayers();

    boolean isChunkGenerated(int chunkX, int chunkZ);

    boolean isChunkLoaded(int chunkX, int chunkZ);

    @NotNull @Unmodifiable Set<ChunkAccess> getLoadedChunks();

    @Nullable ChunkAccess getChunk(int chunkX, int chunkZ);

    default @NotNull CompletableFuture<ChunkAccess> getChunkAsync(int chunkX, int chunkZ) {
        return CompletableFuture.supplyAsync(() -> getChunk(chunkX, chunkZ));
    }

    @NotNull BlockState getBlockState(int x, int y, int z);

    void setBlockState(int x, int y, int z, @NotNull BlockState blockState);
}
