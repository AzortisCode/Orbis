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
import com.azortis.orbis.paper.OrbisPlugin;
import com.azortis.orbis.paper.PaperPlatform;
import com.azortis.orbis.paper.PaperWorldAccess;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

public final class PaperStudioWorld extends StudioWorld implements Listener {

    private static final PaperPlatform platform = OrbisPlugin.getPlatform();
    private World nativeWorld;
    private WorldAccess worldAccess;

    public PaperStudioWorld(Project project) {
        super("orbis_studio", new File(Bukkit.getWorldContainer() + "/orbis_studio/"), project);
        Bukkit.getServer().getPluginManager().registerEvents(this, OrbisPlugin.getPlugin());
    }

    public World nativeWorld() {
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
            if (!studioWorldDir.delete()) {
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = platform.getPlayer(event.getPlayer());
        if (getViewers().contains(player) && event.getTo().getWorld() != nativeWorld) {
            removeViewer(player);
        } else if (!getViewers().contains(player) && event.getTo().getWorld() == nativeWorld) {
            if (player.hasPermission("orbis.admin")) {
                player.sendMessage(Orbis.getMiniMessage().deserialize("<prefix> <gray>Rerouting your teleport..."));
                addViewer(player);
                // Let the teleporting be done by the studioWorld
                event.setCancelled(true);
            } else {
                player.sendMessage(Orbis.getMiniMessage().deserialize("<prefix> <red>You don't have the permission to enter the studio world!"));
                event.setCancelled(true);
            }
        }
    }
}
