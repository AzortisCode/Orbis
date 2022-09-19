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

package com.azortis.orbis.paper.studio;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.Player;
import com.azortis.orbis.WorldAccess;
import com.azortis.orbis.pack.studio.Project;
import com.azortis.orbis.pack.studio.StudioWorld;
import com.azortis.orbis.paper.ConversionUtils;
import com.azortis.orbis.paper.OrbisPlugin;
import com.azortis.orbis.paper.PaperPlatform;
import com.azortis.orbis.paper.PaperWorldAccess;
import com.azortis.orbis.util.Location;
import com.google.common.base.Preconditions;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public final class PaperStudioWorld extends StudioWorld implements Listener {

    private static final PaperPlatform platform = OrbisPlugin.getPlatform();
    private World nativeWorld;
    private WorldAccess worldAccess;

    public PaperStudioWorld(Project project) {
        super("orbis_studio", new File(Bukkit.getWorldContainer() + "/orbis_studio/"), project);
        Bukkit.getServer().getPluginManager().registerEvents(this, OrbisPlugin.getPlugin());
    }

    public @NotNull World nativeWorld() {
        Preconditions.checkNotNull(nativeWorld, "Native world hasn't been set yet!");
        return nativeWorld;
    }

    @Override
    public void hotReload() {

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
            Orbis.getLogger().error("Failed to unload studio world! Shutting down server for safety...");
            Bukkit.getServer().shutdown();
            return false;
        }
        return true;
    }

    //
    // WorldAccess
    //

    @Override
    public boolean isWorldLoaded() {
        return worldAccess.isWorldLoaded();
    }

    @Override
    public @NotNull Set<com.azortis.orbis.Player> getPlayers() {
        return worldAccess.getPlayers();
    }

    //
    // Events
    //

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = platform.getPlayer(event.getPlayer());
        if (player.getWorld() == this) {
            Location fallBackLocation = Orbis.getSettings().studio().fallBackLocation();
            if (player.hasPermission("orbis.admin")) {
                viewers.put(player, fallBackLocation);
                player.setGameMode(Player.GameMode.CREATIVE);
                player.setAllowFlying(true);
                player.sendMessage(Orbis.getMiniMessage().deserialize("<prefix> <gray>You're currently viewing the studio world."));
            } else {
                if (fallBackLocation.isWorldLoaded()) {
                    player.teleportAsync(fallBackLocation);
                } else {
                    player.kick(Orbis.getMiniMessage().deserialize(
                            "<red>Fallback location invalid, you cannot join in a studio world without the right permissions!"));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = platform.getPlayer(event.getPlayer());
        if (getViewers().contains(player)) {
            viewers.remove(player); // Prevent memory leaks by this player instance not being removed from the list
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = platform.getPlayer(event.getPlayer());
        if (getViewers().contains(player) && event.getTo().getWorld() != nativeWorld) {
            removeViewer(player);
        } else if (!getViewers().contains(player) && event.getTo().getWorld() == nativeWorld) {
            if (player.hasPermission("orbis.admin")) {
                // The teleporting logic has already been done, so just force add the player to the viewers list
                viewers.put(player, ConversionUtils.fromNative(event.getFrom()));
                player.setGameMode(Player.GameMode.CREATIVE);
                player.setAllowFlying(true);
                player.sendMessage(Orbis.getMiniMessage().deserialize("<prefix> <gray>You're now viewing the studio world."));
            } else {
                player.sendMessage(Orbis.getMiniMessage().deserialize("<prefix> <red>You don't have the permission to enter the studio world!"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = platform.getPlayer(event.getPlayer());
        if (viewers.containsKey(player)) {
            event.setRespawnLocation(ConversionUtils.toNative(Orbis.getSettings().studio().fallBackLocation()));
        }
    }

}
