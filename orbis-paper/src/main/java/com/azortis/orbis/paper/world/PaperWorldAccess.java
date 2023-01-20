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
import com.azortis.orbis.block.Blocks;
import com.azortis.orbis.entity.Player;
import com.azortis.orbis.paper.OrbisPlugin;
import com.azortis.orbis.paper.PaperPlatform;
import com.azortis.orbis.world.ChunkAccess;
import com.azortis.orbis.world.WorldAccess;
import io.papermc.paper.chunk.system.ChunkSystem;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("ClassCanBeRecord")
public final class PaperWorldAccess implements WorldAccess, Listener {

    private static final PaperPlatform platform = OrbisPlugin.getPlatform();
    private final World handle;
    private final ServerLevel nmsHandle;

    public PaperWorldAccess(World handle) {
        this.handle = handle;
        this.nmsHandle = ((CraftWorld) handle).getHandle();
    }

    public World handle() {
        return handle;
    }

    @Override
    public @NotNull String name() {
        return handle.getName();
    }

    @Override
    public boolean isWorldLoaded() {
        return Bukkit.getWorlds().contains(handle);
    }

    @Override
    public int minHeight() {
        return handle.getMinHeight();
    }

    @Override
    public int maxHeight() {
        return handle.getMaxHeight();
    }

    @Override
    public @NotNull Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();
        for (org.bukkit.entity.Player bukkitPlayer : handle.getPlayers()) {
            players.add(platform.getPlayer(bukkitPlayer.getUniqueId()));
        }
        return players;
    }

    @Override
    public boolean isChunkGenerated(int chunkX, int chunkZ) {
        return handle.isChunkGenerated(chunkX, chunkZ);
    }

    @Override
    public boolean isChunkLoaded(int chunkX, int chunkZ) {
        return handle.isChunkLoaded(chunkX, chunkZ);
    }

    @Override
    public @NotNull @Unmodifiable Set<ChunkAccess> getLoadedChunks() {
        // Based off Paper's implementation of getLoadedChunks from Bukkit.
        return ChunkSystem.getVisibleChunkHolders(nmsHandle).stream()
                .map(ChunkHolder::getAvailableChunkNow)
                .filter(Objects::nonNull)
                .map(PaperProtoChunkAccess::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @Nullable ChunkAccess getChunk(int chunkX, int chunkZ) {
        if (handle.isChunkLoaded(chunkX, chunkZ)) {
            // If the chunk is loaded, we can just get it from the Bukkit API and base our wrapper on that, since
            // Bukkit can represent loaded chunks.
            return new PaperFullChunkAccess(handle.getChunkAt(chunkX, chunkZ));
        }

        // We use getChunkAtImmediately because it will not do world generation, it will not load the chunk, we can just
        // get the chunk if it exists.
        final net.minecraft.world.level.chunk.ChunkAccess chunk = nmsHandle.getChunkSource().getChunkAtImmediately(chunkX, chunkZ);
        if (chunk == null) return null;
        return new PaperProtoChunkAccess(chunk);
    }

    @Override
    public @NotNull BlockState getBlockState(int x, int y, int z) {
        final ChunkAccess chunk = getChunk(x >> 4, z >> 4);
        if (chunk == null) return Blocks.AIR.defaultState();
        return chunk.getState(x, y, z);
    }

    @Override
    public void setBlockState(int x, int y, int z, @NotNull BlockState blockState) {
        final ChunkAccess chunk = getChunk(x >> 4, z >> 4);
        if (chunk != null) chunk.setBlock(x, y, z, blockState);
    }
}
