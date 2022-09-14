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

package com.azortis.orbis.paper;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.azortis.orbis.Orbis;
import com.azortis.orbis.Platform;
import com.azortis.orbis.World;
import com.azortis.orbis.WorldAccess;
import com.azortis.orbis.command.CommandSender;
import com.azortis.orbis.command.ConsoleSender;
import com.azortis.orbis.item.ItemFactory;
import com.azortis.orbis.pack.Pack;
import com.azortis.orbis.pack.studio.Project;
import com.azortis.orbis.pack.studio.StudioWorld;
import com.azortis.orbis.paper.item.PaperItemFactory;
import com.azortis.orbis.paper.studio.PaperStudioWorld;
import com.azortis.orbis.util.maven.Dependency;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.*;

@Dependency(group = "io.github.jglrxavpok.hephaistos", artifact = "common", version = "2.2.0")
@Dependency(group = "org.jetbrains.kotlin", artifact = "kotlin-stdlib", version = "1.6.10")
@Dependency(group = "cloud.commandframework", artifact = "cloud-paper", version = "1.7.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-bukkit", version = "1.7.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-paper", version = "1.7.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-tasks", version = "1.7.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-brigadier", version = "1.7.0")
public class PaperPlatform implements Platform, Listener {

    private final OrbisPlugin plugin;
    private final Map<String, PaperWorld> worldMap = new HashMap<>();
    private final Map<String, WorldAccess> worldAccessMap = new HashMap<>();
    private final PaperItemFactory itemFactory;
    private final PaperConsoleSender consoleSender = new PaperConsoleSender();
    private final Map<UUID, com.azortis.orbis.Player> players = new HashMap<>();

    public PaperPlatform(OrbisPlugin plugin) throws Exception {
        this.plugin = plugin;
        this.itemFactory = new PaperItemFactory();
    }

    //
    // Basic
    //
    @Override
    public @NotNull String adaptation() {
        return "Bukkit";
    }

    @Override
    public @NotNull Logger logger() {
        return plugin.getSLF4JLogger();
    }

    @Override
    public @NotNull File directory() {
        return plugin.getDataFolder();
    }

    // Factories

    @Override
    public @NotNull ItemFactory itemFactory() {
        return itemFactory;
    }

    //
    // World
    //

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(@NotNull WorldLoadEvent event) {
        org.bukkit.World world = event.getWorld();
        PaperWorldAccess worldAccess = new PaperWorldAccess(world);
        if (worldMap.containsKey(world.getName())) {
            worldMap.get(world.getName()).setWorldAccess(worldAccess);
            worldAccessMap.put(world.getName(), worldMap.get(world.getName()));
        } else worldAccessMap.put(world.getName(), worldAccess);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldUnload(@NotNull WorldUnloadEvent event) {
        org.bukkit.World world = event.getWorld();
        worldMap.remove(world.getName());
        worldAccessMap.remove(world.getName());
    }

    public PaperWorld loadWorld(@NotNull WorldInfo worldInfo, @NotNull Pack pack) {
        PaperWorld paperWorld = new PaperWorld(worldInfo);
        paperWorld.installPack(pack, false);
        if (!paperWorld.isLoaded()) paperWorld.load(worldInfo.getSeed());
        worldMap.put(worldInfo.getName(), paperWorld);

        // If somehow the WorldLoad event got fired before this
        if (worldAccessMap.containsKey(worldInfo.getName())) {
            paperWorld.setWorldAccess(worldAccessMap.get(worldInfo.getName()));
            worldAccessMap.remove(worldInfo.getName());
            worldAccessMap.put(worldInfo.getName(), paperWorld);
        }
        return paperWorld;
    }

    public @Nullable PaperWorld getWorld(@NotNull WorldInfo worldInfo) {
        return worldMap.get(worldInfo.getName());
    }

    @Override
    public @Nullable World getWorld(@NotNull String name) {
        return worldMap.get(name);
    }

    @Override
    public @NotNull Collection<World> worlds() {
        return new ArrayList<>(worldMap.values());
    }

    public @Nullable WorldAccess getWorldAccess(@NotNull WorldInfo worldInfo) {
        return worldAccessMap.get(worldInfo.getName());
    }

    @Override
    public @Nullable WorldAccess getWorldAccess(@NotNull String name) {
        return worldAccessMap.get(name);
    }

    @Override
    public @NotNull Collection<WorldAccess> worldAccesses() {
        return new ArrayList<>(worldAccessMap.values());
    }

    @Override
    public @NotNull StudioWorld createStudioWorld(@NotNull Project project) {
        return new PaperStudioWorld(project);
    }

    //
    // Player
    //

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(@NotNull PlayerJoinEvent joinEvent) {
        players.put(joinEvent.getPlayer().getUniqueId(), new PaperPlayer(joinEvent.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(@NotNull PlayerQuitEvent quitEvent) {
        players.remove(quitEvent.getPlayer().getUniqueId());
    }

    public @NotNull com.azortis.orbis.Player getPlayer(@NotNull Player player) {
        return players.get(player.getUniqueId());
    }

    @Override
    public @Nullable com.azortis.orbis.Player getPlayer(@NotNull UUID uuid) {
        return players.get(uuid);
    }

    @Override
    public @NotNull Collection<com.azortis.orbis.Player> getPlayers() {
        return players.values();
    }

    @Override
    public @Nullable Class<?> mainClass() {
        return plugin.getClass();
    }

    void loadCommands() {
        try {
            Orbis.getLogger().info("Loading commands...");
            PaperCommandManager<CommandSender> commandManager = new PaperCommandManager<>(plugin,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    (commandSender -> {
                        if (commandSender instanceof Player player) {
                            return getPlayer(player.getUniqueId());
                        } else if (commandSender instanceof ConsoleCommandSender) {
                            return consoleSender;
                        }
                        return null;
                    }), (commandSender -> {
                if (commandSender instanceof PaperPlayer player) {
                    return player.handle();
                } else if (commandSender instanceof ConsoleSender) {
                    return Bukkit.getConsoleSender();
                }
                return null;
            }));
            commandManager.registerBrigadier();
            commandManager.registerAsynchronousCompletions();
            Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
            Orbis.loadCommands(commandManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
