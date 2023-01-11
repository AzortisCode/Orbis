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

import com.azortis.orbis.Orbis;
import com.azortis.orbis.pack.Pack;
import com.azortis.orbis.paper.generator.PaperChunkGenerator;
import com.azortis.orbis.util.maven.DependencyLoader;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public final class OrbisPlugin extends JavaPlugin {

    private boolean loaded = false;

    private static OrbisPlugin plugin;

    private static PaperPlatform platform;

    public static OrbisPlugin getPlugin() {
        return plugin;
    }

    public static PaperPlatform getPlatform() {
        return platform;
    }

    @Override
    public void onLoad() {
        if (!Bukkit.getMinecraftVersion().replaceAll("\\.", "_").equalsIgnoreCase(Orbis.MC_VERSION)) {
            this.getSLF4JLogger().error("Unsupported minecraft version! Only works on {}", Orbis.MC_VERSION);
            return;
        }
        plugin = this;

        File studioWorldDir = new File(Bukkit.getWorldContainer() + "/orbis_studio/");
        if (studioWorldDir.exists()) {
            this.getSLF4JLogger().info("Studio world directory still present! Deleting...");
            try {
                FileUtils.deleteDirectory(studioWorldDir);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to delete studio world directory!", ex);
            }
        }

        // Libraries need to be loaded before Orbis gets initialized, since the Orbis class & PaperPlatform use them.
        this.getSLF4JLogger().info("Loading libraries...");
        DependencyLoader.initialize(this.getClass(), new File(this.getDataFolder() + "/libs/"), this.getSLF4JLogger());
        DependencyLoader.loadAll(Orbis.class);
        DependencyLoader.loadAll(PaperPlatform.class);

        try {
            platform = new PaperPlatform(this);
        } catch (Exception e) {
            this.getSLF4JLogger().error("Failed to instantiate paper platform!");
            e.printStackTrace();
            return;
        }
        Orbis.initialize(platform);
        loaded = true;
    }

    @Override
    public void onEnable() {
        if (loaded) {
            platform.loadCommands();
        }
    }

    @Override
    public void onDisable() {
        if (loaded) {
            Bukkit.getScheduler().cancelTasks(this);
            Orbis.shutdown();
        }
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String packName) {
        if (!loaded) super.getDefaultWorldGenerator(worldName, packName);
        if (packName != null) {
            Pack pack = Orbis.getPackManager().getPack(packName);
            if (pack != null) {
                return new PaperChunkGenerator(worldName, pack);
            } else {
                Orbis.getLogger().error("No pack by the fieldName {} exists!", packName);
                return super.getDefaultWorldGenerator(worldName, packName);
            }
        }
        Orbis.getLogger().error("No pack fieldName specified for world {}", worldName);
        return super.getDefaultWorldGenerator(worldName, null);
    }
}
