/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2021  Azortis
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

package com.azortis.orbis;

import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.Engine;
import com.azortis.orbis.pack.Pack;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * A representation of a world/instance for core package
 */
public abstract class World {
    //name, folder, installed, settings folder, dimension, engine

    @Getter
    private final String name;
    @Getter
    private final File folder;
    @Getter
    private final File settingsFolder;

    private final File worldInfoFile;
    private WorldInfo worldInfo;

    @Getter
    private final boolean loaded = false;
    @Getter
    private boolean installed = false;
    @Getter
    private Dimension dimension;
    @Getter
    private Engine engine;

    public World(String name, File folder) {
        this.name = name;
        this.folder = folder;
        this.settingsFolder = new File(folder, "/settings/");
        if (settingsFolder.exists() && Objects.requireNonNull(settingsFolder.listFiles()).length > 0) installed = true;

        this.worldInfoFile = new File(folder, "world-info.json");
        try {
            if (!worldInfoFile.exists() && worldInfoFile.createNewFile()) {
                //worldInfo = new WorldInfo(Orbis.SETTINGS_VERSION);
                String containerJson = Orbis.getGson().toJson(worldInfo);
                Files.write(worldInfoFile.toPath(), containerJson.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } else {
                reloadWorldInfo();
            }
        } catch (IOException ex) {
            Orbis.getLogger().error("Failed to create container-info.json file for container {}", name);
        }
    }

    public void load() {
        if (!loaded) {
            /*OldRegistry<Dimension> dimensionOldRegistry = Orbis.getRegistryOld(Dimension.class);
            assert dimensionOldRegistry != null;
            this.dimension = dimensionOldRegistry.loadType(this, worldInfo.getDimensionFile());
            this.engine = new SimpleEngine(this);
            this.loaded = true;*/
        }
    }

    public void saveWorldInfo() {
        Orbis.getLogger().info("Saving container-info for {}", name);
        try {
            if (worldInfoFile.delete()) {
                final String containerJson = Orbis.getGson().toJson(worldInfo);
                Files.write(worldInfoFile.toPath(), containerJson.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }
        } catch (IOException ex) {
            Orbis.getLogger().error("Failed to save container-info.json file for container {}", name);
        }
    }

    public void reloadWorldInfo() {
        Orbis.getLogger().info("Loading world-info for {}", name);
        try {
            this.worldInfo = Orbis.getGson().fromJson(new FileReader(worldInfoFile), WorldInfo.class);
        } catch (FileNotFoundException ex) {
            Orbis.getLogger().error("world-info file not found for container: {}", name);
        }
    }

    /**
     * Installs a pack into the world.
     *
     * @param pack     The pack-info.
     * @param override If it should install pack even when one is already present.
     */
    public void installPack(@NotNull Pack pack, boolean override) {
        if (!installed || override) {
            /*if (override) {
                if (!settingsFolder.delete()) {
                    Orbis.getLogger().error("Couldn't reinstall pack: {}, for container: {}", pack.getName(), this.name);
                    return;
                }
            }
            Orbis.getLogger().info("Installing pack {} version {} for {}", pack.getName(), pack.getPackVersion(), name);
            Orbis.getPackManager().extractPack(settingsFolder, pack);
            if (worldInfo.getPackName() == null) {
                worldInfo.setPackName(pack.getName());
                worldInfo.setDimensionFile(pack.getDimensionFile());
                saveWorldInfo();
            }
            installed = true;*/
        }
    }
}
