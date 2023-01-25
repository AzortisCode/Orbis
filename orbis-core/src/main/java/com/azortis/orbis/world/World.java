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

package com.azortis.orbis.world;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.framework.Engine;
import com.azortis.orbis.pack.Pack;
import com.azortis.orbis.pack.PackLoader;
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.pack.data.DirectoryDataAccess;
import com.azortis.orbis.pack.studio.Project;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * A representation of a world/instance for core package
 */
public abstract class World implements WorldAccess {
    // TODO restructure fields, make it more clean, but this outta work for now

    private final String name;
    private final File directory;
    private final File settingsDirectory;

    // WorldInfo
    private final File worldInfoFile;
    private final DataAccess data;
    protected WorldInfo worldInfo;
    // Status booleans
    protected boolean loaded = false;
    protected Dimension dimension;
    protected Engine engine;
    private boolean installed = false;

    public World(@NotNull String name, @NotNull File directory) {
        this.name = name;
        this.directory = directory;
        this.settingsDirectory = new File(directory, "/settings/");
        if (settingsDirectory.exists() && Objects.requireNonNull(settingsDirectory.listFiles()).length > 0)
            installed = true;
        this.data = new DirectoryDataAccess(settingsDirectory);
        this.worldInfoFile = new File(directory, "world-info.dat");
        if (installed) {
            if (!worldInfoFile.exists()) {
                Orbis.getLogger().error("Settings directory exists, but world-info.dat doesn't!");
            } else {
                reloadWorldInfo();
            }
        }
    }

    /**
     * Internal constructor for {@link com.azortis.orbis.pack.studio.StudioWorld}.
     *
     * @param name      The name of the world
     * @param directory The directory of the world
     * @param project   The project of the studio world.
     */
    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.pack.studio")
    protected World(@NotNull String name, @NotNull File directory, @NotNull Project project) {
        this.name = name;
        this.directory = directory;
        this.settingsDirectory = project.directory();
        this.installed = true;
        this.data = new DirectoryDataAccess(project.directory());
        this.worldInfoFile = new File(directory, "world-info.dat");
    }

    public void load(long seed) {
        if (!loaded && installed) {
            if (worldInfo.seed() == 0) {
                worldInfo = worldInfo.seed(seed);
                saveWorldInfo();
            }
            try {
                Orbis.getLogger().info("Loading pack {} for {}, this may take some time...", worldInfo.packName(), name);
                dimension = PackLoader.loadDimension(this);
                engine = new Engine(this, dimension);
                loaded = true;
            } catch (IOException | NoSuchFieldException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | InstantiationException e) {
                e.printStackTrace(); // TODO catch these errors earlier in PackLoader for more constructive feedback
            }
        }
    }

    public @NotNull String name() {
        return name;
    }

    public File directory() {
        return directory;
    }

    public File settingsDirectory() {
        return settingsDirectory;
    }

    public WorldInfo getWorldInfo() {
        return worldInfo;
    }

    public DataAccess data() {
        return data;
    }

    public void saveWorldInfo() {
        Orbis.getLogger().info("Saving world-info for {}", name);
        try {
            if (!worldInfoFile.exists() && !worldInfoFile.createNewFile()) {
                Orbis.getLogger().error("Failed to save world-info.dat file for world {}", name);
            }
            BinaryTagIO.writer().write(worldInfo.toNBT(), worldInfoFile.toPath(), BinaryTagIO.Compression.GZIP);
        } catch (IOException ex) {
            Orbis.getLogger().error("Failed to save world-info.dat file for world {}", name);
        }
    }

    public void reloadWorldInfo() {
        Orbis.getLogger().info("Loading world-info for {}", name);
        try {
            CompoundBinaryTag tag = BinaryTagIO.reader().read(worldInfoFile.toPath(), BinaryTagIO.Compression.GZIP);
            worldInfo = new WorldInfo(tag);
        } catch (IOException ex) {
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
                if (!settingsDirectory.delete()) {
                    Orbis.getLogger().error("Couldn't reinstall pack: {}, for world: {}", pack.name(), name);
                    return;
                }
            }
            Orbis.getLogger().info("Installing pack {} version {} for {}", pack.name(), pack.packVersion(), name);
            Orbis.getPackManager().extractPack(settingsDirectory, pack);
            worldInfo = new WorldInfo(pack.name(), pack.dimensionFile(), 0L);
            installed = true;
        }
    }
}
