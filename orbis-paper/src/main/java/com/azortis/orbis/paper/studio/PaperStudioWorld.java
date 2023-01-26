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

package com.azortis.orbis.paper.studio;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.block.Blocks;
import com.azortis.orbis.entity.Player;
import com.azortis.orbis.pack.studio.Project;
import com.azortis.orbis.pack.studio.StudioWorld;
import com.azortis.orbis.paper.OrbisPlugin;
import com.azortis.orbis.paper.PaperPlatform;
import com.azortis.orbis.paper.util.ConversionUtils;
import com.azortis.orbis.paper.world.PaperWorldAccess;
import com.azortis.orbis.util.Location;
import com.azortis.orbis.world.WorldAccess;
import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.generator.CraftChunkData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class PaperStudioWorld extends StudioWorld implements Listener {

    private static final PaperPlatform platform = OrbisPlugin.getPlatform();
    private World nativeWorld;
    private WorldAccess worldAccess;

    public PaperStudioWorld(@NotNull Project project) {
        super("orbis_studio", new File(Bukkit.getWorldContainer() + "/orbis_studio/"), project);
        Bukkit.getServer().getPluginManager().registerEvents(this, OrbisPlugin.getPlugin());
    }

    public @NotNull World nativeWorld() {
        Preconditions.checkNotNull(nativeWorld, "Native world hasn't been set yet!");
        return nativeWorld;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void hotReload() {
        // TODO use reflection remapper to fix this
        /*ServerLevel level = ((CraftWorld) nativeWorld).getHandle();

        // Update possible biomes.
        try {
            BiomeSource biomeSource = level.getChunkSource().getGenerator().getBiomeSource();
            biomeSource.possibleBiomes();
            Field biomesField = biomeSource.getClass().getField("possibleBiomes");
            biomesField.setAccessible(true);
            Set<Holder<Biome>> possibleBiomes = (Set<Holder<Biome>>) biomesField.get(biomesField);
            possibleBiomes.clear();
            for (org.bukkit.block.Biome paperBiome : Objects.requireNonNull(nativeWorld.getBiomeProvider()).getBiomes(nativeWorld)) {
                possibleBiomes.add(CraftBlock.biomeToBiomeBase(level.getServer().registries().compositeAccess().registryOrThrow(Registries.BIOME), paperBiome));
            }
            biomesField.set(biomeSource, possibleBiomes);
            biomesField.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            Orbis.getLogger().error("Failed to reset the possible biomes for studio world!", ex);
        }
        Arrays.stream(nativeWorld.getLoadedChunks()).parallel().forEach(this::regenChunk);*/
    }

    @Override
    public void initialize() {
        WorldCreator worldCreator = new WorldCreator(name())
                .generator(new PaperStudioChunkGenerator(this.project))
                .generateStructures(false)
                .environment(World.Environment.NORMAL);
        nativeWorld = Bukkit.createWorld(worldCreator);
        worldAccess = new PaperWorldAccess(nativeWorld);
    }

    @Override
    protected boolean unload() {
        // Unregister all events
        HandlerList.unregisterAll(this);

        // Unload the world
        if (Bukkit.getServer().unloadWorld(nativeWorld, false)) {
            this.nativeWorld = null;

            // Clear world files
            File studioWorldDir = new File(Bukkit.getWorldContainer() + "/orbis_studio/");
            try {
                FileUtils.deleteDirectory(studioWorldDir);
            } catch (IOException ex) {
                Orbis.getLogger().error("Failed to delete studio world directory! Shutting down server for safety...");
                Bukkit.getServer().shutdown();
                return false;
            }
        } else {
            Orbis.getLogger().error("Failed to finish studio world! Shutting down server for safety...");
            Bukkit.getServer().shutdown();
            return false;
        }
        return true;
    }

    private void regenChunk(@NotNull final Chunk chunk) {
        CompletableFuture.runAsync(() -> {
            ChunkAccess access = ((CraftWorld) nativeWorld).getHandle().getChunk(chunk.getX(), chunk.getZ());

            // Set all blocks in chunk to air and reset the biome
            for (int cx = 0; cx <= 15; cx++) {
                for (int cz = 0; cz <= 15; cz++) {
                    for (int y = nativeWorld.getMinHeight(); y < nativeWorld.getMaxHeight(); y++) {
                        BlockPos blockPos = new BlockPos(access.getPos().getMinBlockX() + cx, y,
                                access.getPos().getMinBlockZ() + cz);
                        int x = access.getPos().getMinBlockX() + cx;
                        int z = access.getPos().getMinBlockZ() + cz;
                        access.setBiome(x >> 2, y >> 2, z >> 2, access.getNoiseBiome(x, y, z));
                        access.setBlockState(blockPos, Block.stateById(Blocks.AIR.stateId()), false);
                    }
                }
            }
            // Regenerate the chunk by passing a ChunkAccess into the generator like normal, we know which method needs to be called
            Objects.requireNonNull(nativeWorld.getGenerator())
                    .generateNoise(nativeWorld, new Random(), chunk.getX(), chunk.getZ(),
                            new CraftChunkData(nativeWorld, access));
        });
    }

    //
    // WorldAccess
    //

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
    public @NotNull @Unmodifiable Set<com.azortis.orbis.world.ChunkAccess> getLoadedChunks() {
        return worldAccess.getLoadedChunks();
    }

    @Override
    public com.azortis.orbis.world.@NotNull ChunkAccess getChunk(int chunkX, int chunkZ) {
        return worldAccess.getChunk(chunkX, chunkZ);
    }

    //
    // Events
    //

    @EventHandler(priority = EventPriority.LOWEST)
    private void onChunkLoad(ChunkLoadEvent event) {
        if (event.getWorld() == nativeWorld && !event.isNewChunk()) {
            // Regenerate each chunk that gets loaded, in case something changed
            regenChunk(event.getChunk());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onChunkUnload(ChunkUnloadEvent event) {
        if (event.getWorld() == nativeWorld) {
            // Don't bother saving chunks, since they won't stay anyway.
            event.setSaveChunk(false);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = platform.getPlayer(event.getPlayer());
        if (player.getWorld() == this) {
            Location fallBackLocation = Orbis.getSettings().studio().fallBackLocation();
            if (player.hasPermission("orbis.admin")) {
                viewers.put(player, fallBackLocation);
                player.setGameMode(Player.GameMode.CREATIVE);
                player.setAllowFlying(true);
                player.sendMessage(Orbis.getMiniMessage()
                        .deserialize("<prefix> <gray>You're currently viewing the studio world."));
            } else {
                if (fallBackLocation.isWorldLoaded()) {
                    player.teleportAsync(fallBackLocation);
                } else {
                    player.kick(Orbis.getMiniMessage().deserialize(
                            "<red>Fallback location invalid, you cannot join in a " +
                                    "studio world without the right permissions!"));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = platform.getPlayer(event.getPlayer());
        if (getViewers().contains(player)) {
            viewers.remove(player); // Prevent memory leaks by this player instance not being removed from the list
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = platform.getPlayer(event.getPlayer());
        if (getViewers().contains(player) && event.getTo().getWorld() != nativeWorld) {
            removeViewer(player);
        } else if (!getViewers().contains(player) && event.getTo().getWorld() == nativeWorld) {
            if (player.hasPermission("orbis.admin")) {
                // The teleporting logic has already been done, so just force add the player to the viewers list
                viewers.put(player, ConversionUtils.fromPaper(event.getFrom()));
                player.setGameMode(Player.GameMode.CREATIVE);
                player.setAllowFlying(true);
                player.sendMessage(Orbis.getMiniMessage()
                        .deserialize("<prefix> <gray>You're now viewing the studio world."));
            } else {
                player.sendMessage(Orbis.getMiniMessage()
                        .deserialize("<prefix> <red>You don't have the permission to enter the studio world!"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = platform.getPlayer(event.getPlayer());
        if (viewers.containsKey(player)) {
            event.setRespawnLocation(ConversionUtils.toPaper(Orbis.getSettings().studio().fallBackLocation()));
        }
    }

}
