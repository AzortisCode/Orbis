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

package com.azortis.orbis.pack.studio;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.Player;
import com.azortis.orbis.World;
import com.azortis.orbis.WorldInfo;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.pack.Pack;
import com.azortis.orbis.util.Location;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * A world interface that is used to visualize settings when working on a project.
 */
public abstract class StudioWorld extends World {

    protected final Project project;

    /**
     * If the world should render, or just create void chunks.
     */
    private boolean shouldRender = false;

    /**
     * Players viewing this world, with their last known location before entering it.
     */
    protected final Map<Player, Location> viewers = new HashMap<>();

    public StudioWorld(String name, File folder, Project project) {
        super(name, folder, project);
        this.project = project;
    }

    public Project project() {
        return project;
    }

    public boolean shouldRender() {
        return shouldRender;
    }

    public void addViewer(@NotNull Player viewer) {
        if (!viewers.containsKey(viewer) && viewer.hasPermission("orbis.admin") && this.isWorldLoaded()) {
            viewers.put(viewer, viewer.getLocation());
            viewer.teleportAsync(new Location(0, 255, 0, 0f, 0f, new WeakReference<>(this))).thenAccept(result -> {
                if (!result) {
                    viewers.remove(viewer);
                    viewer.sendMessage(Orbis.getMiniMessage().deserialize("<prefix> <red>Failed to enter studio world!"));
                } else {
                    viewer.setGameMode(Player.GameMode.CREATIVE);
                    viewer.setAllowFlying(true);
                    viewer.sendMessage(Orbis.getMiniMessage().deserialize("<prefix> <gray>You're now viewing the studio world."));
                }
            });
        } else {
            viewer.sendMessage(Orbis.getMiniMessage().deserialize("<prefix> <red>Failed to enter studio world!"));
        }
    }

    public void removeViewer(@NotNull Player viewer) {
        if (viewers.containsKey(viewer)) {
            Location location = viewers.get(viewer);

            if (location == null || !location.isWorldLoaded()) {
                location = Orbis.getSettings().studio().fallBackLocation();
            }

            if (location.isWorldLoaded()) {
                viewers.remove(viewer);
                try {
                    teleportOut(viewer, location);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                viewers.remove(viewer);
                viewer.kick(Orbis.getMiniMessage().deserialize("<red>Failed to teleportAsync out of studio world, so kicking as last resort..."));
            }
        }
    }

    public void clearViewers() {
        for (Player viewer : viewers.keySet()) {
            removeViewer(viewer);
        }
    }

    private void teleportOut(@NotNull Player player, final Location location) throws ExecutionException, InterruptedException {
        // We teleport the player sync, since this will also be called when unloading
        // a studio world and if the result is unknown it will fail to unload and thus cause a server shutdown
        // now we can intervene before that happens with kicking the player as a last resort.
        if (player.teleport(location)) {
            player.sendMessage(Orbis.getMiniMessage().deserialize("<prefix> <gray>You're no longer viewing the studio world."));
        } else {
            if (location != Orbis.getSettings().studio().fallBackLocation()) {
                if (player.teleport(Orbis.getSettings().studio().fallBackLocation())) {
                    player.sendMessage(Orbis.getMiniMessage().deserialize("<prefix> <gray>You're no longer viewing the studio world."));
                } else {
                    player.kick(Orbis.getMiniMessage().deserialize("<red>Failed to teleport out of studio world, so kicking as last resort..."));
                }
            } else {
                player.kick(Orbis.getMiniMessage().deserialize("<red>Failed to teleport out of studio world, so kicking as last resort..."));
            }
        }
    }

    public @NotNull Set<Player> getViewers() {
        return Set.copyOf(viewers.keySet());
    }

    @Override
    public void load(long seed) {
        if (!loaded) {
            this.worldInfo = new WorldInfo("null", "null", seed);
            saveWorldInfo();
            loaded = true;
        }
    }

    void setDimension(@NotNull Dimension dimension) {
        this.dimension = dimension;
    }

    @Override
    public void installPack(@NotNull Pack pack, boolean override) {
        throw new UnsupportedOperationException("Cannot install a pack for a studio world!");
    }

    /**
     * Called everytime the render mode has updated or the configs have updated.
     * Platform implementation should use this to clear the world, and regenerate chunks or update their
     * biome registries.
     */
    public abstract void hotReload();

    /**
     * Called when the platform should initialize the rendering of the world.
     */
    public abstract void initialize();

    /**
     * Called when the StudioWorld is being unloaded.
     * Platform implementations should use this to delete
     * any native world they have in memory or on disk.
     */
    protected abstract boolean unload();
}
