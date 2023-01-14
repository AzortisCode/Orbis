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

package com.azortis.orbis.paper;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.paper.PaperCommandManager;
import com.azortis.orbis.Orbis;
import com.azortis.orbis.Platform;
import com.azortis.orbis.Settings;
import com.azortis.orbis.block.BlockRegistry;
import com.azortis.orbis.command.CommandSender;
import com.azortis.orbis.command.ConsoleSender;
import com.azortis.orbis.pack.Pack;
import com.azortis.orbis.pack.studio.Project;
import com.azortis.orbis.pack.studio.StudioWorld;
import com.azortis.orbis.paper.block.PaperBlockRegistry;
import com.azortis.orbis.paper.entity.PaperPlayer;
import com.azortis.orbis.paper.studio.PaperStudioWorld;
import com.azortis.orbis.paper.util.PaperScheduler;
import com.azortis.orbis.paper.world.PaperWorld;
import com.azortis.orbis.paper.world.PaperWorldAccess;
import com.azortis.orbis.util.Scheduler;
import com.azortis.orbis.util.maven.Dependency;
import com.azortis.orbis.world.World;
import com.azortis.orbis.world.WorldAccess;
import com.google.common.collect.ImmutableSet;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Dependency(group = "cloud.commandframework", artifact = "cloud-paper", version = "1.8.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-bukkit", version = "1.8.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-paper", version = "1.8.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-tasks", version = "1.8.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-brigadier", version = "1.8.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-minecraft-extras", version = "1.8.0")
@Dependency(group = "net.kyori", artifact = "adventure-nbt", version = "4.12.0")
public class PaperPlatform implements Platform, Listener {

    private final OrbisPlugin plugin;
    private final PaperScheduler scheduler = new PaperScheduler();
    private final Map<String, PaperWorld> worldMap = new HashMap<>();
    private final Map<String, WorldAccess> worldAccessMap = new HashMap<>();
    private final Map<UUID, com.azortis.orbis.entity.Player> players = new HashMap<>();

    PaperPlatform(OrbisPlugin plugin) {
        this.plugin = plugin;

        BlockRegistry.init(new PaperBlockRegistry());
    }

    //
    // Basic
    //

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String adaptation() {
        return "Paper";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Logger logger() {
        return plugin.getSLF4JLogger();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull File directory() {
        return plugin.getDataFolder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Scheduler scheduler() {
        return scheduler;
    }

    //
    // World
    //

    @EventHandler(priority = EventPriority.MONITOR)
    private void onWorldLoad(@NotNull WorldLoadEvent event) {
        org.bukkit.World world = event.getWorld();
        PaperWorldAccess worldAccess = new PaperWorldAccess(world);
        if (worldMap.containsKey(world.getName())) {
            worldMap.get(world.getName()).setWorldAccess(worldAccess);
            worldAccessMap.put(world.getName(), worldMap.get(world.getName()));
        } else worldAccessMap.put(world.getName(), worldAccess);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onWorldUnload(@NotNull WorldUnloadEvent event) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable World getWorld(@NotNull String name) {
        return worldMap.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImmutableSet<World> worlds() {
        return ImmutableSet.copyOf(worldMap.values());
    }

    /**
     * {@inheritDoc}
     */
    public @Nullable WorldAccess getWorldAccess(@NotNull WorldInfo worldInfo) {
        return worldAccessMap.get(worldInfo.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable WorldAccess getWorldAccess(@NotNull String name) {
        return worldAccessMap.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImmutableSet<WorldAccess> worldAccesses() {
        return ImmutableSet.copyOf(worldAccessMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull StudioWorld createStudioWorld(@NotNull Project project) {
        return new PaperStudioWorld(project);
    }

    //
    // Player
    //

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerLogin(@NotNull PlayerLoginEvent loginEvent) {
        if (loginEvent.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            players.put(loginEvent.getPlayer().getUniqueId(), new PaperPlayer(loginEvent.getPlayer()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerQuit(@NotNull PlayerQuitEvent quitEvent) {
        players.remove(quitEvent.getPlayer().getUniqueId());
    }

    public @NotNull com.azortis.orbis.entity.Player getPlayer(@NotNull Player player) {
        return players.get(player.getUniqueId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable com.azortis.orbis.entity.Player getPlayer(@NotNull UUID uuid) {
        return players.get(uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImmutableSet<com.azortis.orbis.entity.Player> getPlayers() {
        return ImmutableSet.copyOf(players.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable Class<?> mainClass() {
        return plugin.getClass();
    }

    //
    // Settings
    //

    public @NotNull PaperSettings settings() {
        return (PaperSettings) Orbis.getSettings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Class<? extends Settings> settingsClass() {
        return PaperSettings.class; // Default for now
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Settings defaultSettings() {
        return PaperSettings.defaultSettings();
    }

    /**
     * Creates and initializes a command manager for the {@link Orbis} to register the commands in the core.
     */
    void loadCommands() {
        try {
            Orbis.getLogger().info("Loading commands...");
            PaperCommandManager<CommandSender> commandManager = new PaperCommandManager<>(plugin,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    (commandSender -> {
                        if (commandSender instanceof Player player) {
                            return getPlayer(player.getUniqueId());
                        } else if (commandSender instanceof ConsoleCommandSender) {
                            return (ConsoleSender) Bukkit::getConsoleSender;
                        }
                        return null;
                    }),
                    (commandSender -> {
                        if (commandSender instanceof PaperPlayer player) {
                            return player.handle();
                        } else if (commandSender instanceof ConsoleSender) {
                            return Bukkit.getConsoleSender();
                        }
                        return null;
                    }));
            commandManager.registerBrigadier();
            commandManager.registerAsynchronousCompletions();
            new MinecraftExceptionHandler<CommandSender>()
                    .withArgumentParsingHandler()
                    .withInvalidSenderHandler()
                    .withInvalidSyntaxHandler()
                    .withNoPermissionHandler()
                    .withCommandExecutionHandler()
                    .withDecorator(message -> Orbis.getPrefixComponent().append(Component.space()).append(message))
                    .apply(commandManager, sender -> {
                        if (sender instanceof PaperPlayer player) {
                            return player.handle();
                        } else if (sender instanceof ConsoleSender) {
                            return Bukkit.getConsoleSender();
                        } else {
                            return Audience.empty();
                        }
                    });
            Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
            Orbis.loadCommands(commandManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
