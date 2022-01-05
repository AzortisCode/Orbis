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
import com.azortis.orbis.generator.SimpleEngine;
import com.azortis.orbis.pack.Pack;
import com.azortis.orbis.pack.PackLoader;
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.pack.data.WorldDataAccess;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.nbt.NBTReader;
import org.jglrxavpok.hephaistos.nbt.NBTWriter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * A representation of a world/instance for core package
 */
public abstract class World {
    // TODO restructure fields, make it more clean, but this outta work for now

    private final String name;
    private final File directory;
    private final File settingsFolder;

    // WorldInfo
    private final File worldInfoFile;
    private WorldInfo worldInfo;

    // Status booleans
    private boolean loaded = false;
    private boolean installed = false;

    private final DataAccess data;
    private Dimension dimension;
    private Engine engine;

    public World(String name, File directory) {
        this.name = name;
        this.directory = directory;
        this.settingsFolder = new File(directory, "/settings/");
        if (settingsFolder.exists() && Objects.requireNonNull(settingsFolder.listFiles()).length > 0) installed = true;
        this.data = new WorldDataAccess(this);
        this.worldInfoFile = new File(directory, "world-info.dat");
        if (worldInfoFile.exists()) {
            installed = true;
            reloadWorldInfo();
        }
    }

    public void load(long seed) {
        if (!loaded) {
            if (worldInfo.seed() == 0) {
                worldInfo = worldInfo.seed(seed);
                saveWorldInfo();
            }
            try {
                dimension = PackLoader.loadDimension(this);
                engine = new SimpleEngine(this); // TODO create actual engine impl
                loaded = true;
            } catch (IOException | NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace(); // TODO catch these errors earlier in PackLoader for more constructive feedback
            }
        }
    }

    public String getName() {
        return name;
    }

    public File getDirectory() {
        return directory;
    }

    public File getSettingsFolder() {
        return settingsFolder;
    }

    public WorldInfo getWorldInfo() {
        return worldInfo;
    }

    public DataAccess getData() {
        return data;
    }

    public void saveWorldInfo() {
        Orbis.getLogger().info("Saving container-info for {}", name);
        try {
            if (worldInfoFile.delete()) {
                NBTWriter writer = new NBTWriter(worldInfoFile);
                writer.writeRaw(worldInfo.serialize());
            }
        } catch (IOException ex) {
            Orbis.getLogger().error("Failed to save world-info.dat file for world {}", name);
        }
    }

    public void reloadWorldInfo() {
        Orbis.getLogger().info("Loading world-info for {}", name);
        try {
            NBTReader reader = new NBTReader(worldInfoFile);
            worldInfo = new WorldInfo((NBTCompound) reader.read());
        } catch (IOException | NBTException e) {
            Orbis.getLogger().error("Failed to load word-info for world {}", name);
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isInstalled() {
        return installed;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Engine getEngine() {
        return engine;
    }

    /**
     * Installs a pack into the world.
     *
     * @param pack     The pack-info.
     * @param override If it should install pack even when one is already present.
     */
    public void installPack(@NotNull Pack pack, boolean override) {
        if (!installed || override) {
            if (override) {
                if (!settingsFolder.delete()) {
                    Orbis.getLogger().error("Couldn't reinstall pack: {}, for world: {}", pack.getName(), name);
                    return;
                }
            }
            Orbis.getLogger().info("Installing pack {} version {} for {}", pack.getName(), pack.getPackVersion(), name);
            Orbis.getPackManager().extractPack(settingsFolder, pack);
            worldInfo = new WorldInfo(pack.getName(), pack.getDimensionFile(), 0L);
            installed = true;
        }
    }
}
