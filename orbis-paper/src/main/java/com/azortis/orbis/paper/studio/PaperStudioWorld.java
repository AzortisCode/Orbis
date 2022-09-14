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
    protected void unload() {
        // Get all viewers out this world.
        for (Player player : viewers.keySet()) {

        }

        // Unload the world
        Bukkit.getServer().unloadWorld(nativeWorld, false);
        this.nativeWorld = null;

        // Unregister the events
        HandlerList.unregisterAll(this);
    }

    //
    // WorldAccess
    //

    @Override
    public Set<com.azortis.orbis.Player> getPlayers() {
        return worldAccess.getPlayers();
    }

    //
    // Events
    //

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = platform.getPlayer(event.getPlayer());
        if (viewers.containsKey(player) && event.getTo().getWorld() != nativeWorld) {

        } else if (!viewers.containsKey(player) && event.getTo().getWorld() == nativeWorld) {

        }
    }
}
