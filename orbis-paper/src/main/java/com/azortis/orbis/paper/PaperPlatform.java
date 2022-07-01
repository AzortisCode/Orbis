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
import com.azortis.orbis.command.CommandSender;
import com.azortis.orbis.command.ConsoleSender;
import com.azortis.orbis.item.ItemFactory;
import com.azortis.orbis.paper.item.PaperItemFactory;
import com.azortis.orbis.util.maven.Dependency;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
    private final PaperItemFactory itemFactory;
    private final PaperConsoleSender consoleSender = new PaperConsoleSender();
    private final Map<UUID, com.azortis.orbis.Player> players = new HashMap<>();

    public PaperPlatform(OrbisPlugin plugin) throws Exception {
        this.plugin = plugin;
        this.itemFactory = new PaperItemFactory();
    }

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

    @Override
    public @NotNull ItemFactory itemFactory() {
        return itemFactory;
    }

    @Nullable
    @Override
    public World getWorld(String name) {
        return OrbisPlugin.getWorld(name);
    }

    @Override
    public @NotNull Collection<World> worlds() {
        return new ArrayList<>(OrbisPlugin.getWorlds());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerJoinEvent joinEvent) {
        players.put(joinEvent.getPlayer().getUniqueId(), new PaperPlayer(joinEvent.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent quitEvent) {
        players.remove(quitEvent.getPlayer().getUniqueId());
    }

    @Override
    public com.azortis.orbis.@Nullable Player getPlayer(UUID uuid) {
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
