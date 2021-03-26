/*
 * MIT License
 *
 * Copyright (c) 2021 Azortis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.azortis.orbis.container;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.Engine;
import com.azortis.orbis.generator.SimpleEngine;
import com.azortis.orbis.pack.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * An representation of a world/instance for core package
 */
public abstract class Container {

    private final String name;
    private final File folder;
    private final File settingsFolder;

    private final File containerInfoFile;
    private ContainerInfo containerInfo;

    private boolean loaded = false;
    private boolean installed = false;
    private Dimension dimension;
    private Engine engine;

    public Container(String name, File folder) {
        this.name = name;
        this.folder = folder;
        this.settingsFolder = new File(folder, "/settings/");
        if (settingsFolder.exists() && settingsFolder.listFiles().length > 0) installed = true;

        this.containerInfoFile = new File(folder, "container-info.json");
        try {
            if (!containerInfoFile.exists() && containerInfoFile.createNewFile()) {
                containerInfo = new ContainerInfo(Orbis.SETTINGS_VERSION);
                String containerJson = Orbis.getGson().toJson(containerInfo);
                Files.write(containerInfoFile.toPath(), containerJson.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }
        } catch (IOException ex) {
            Orbis.getLogger().error("Failed to create container-info.json file for container {}", name);
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void load() {
        if (!loaded) {
            this.dimension = Objects.requireNonNull(Orbis.getRegistry(Dimension.class)).loadType(this, "overworld");
            this.engine = new SimpleEngine(this);
            this.loaded = true;
        }
    }

    /**
     * Installs a pack into the world.
     *
     * @param pack     The pack-info.
     * @param override If it should install pack even when one is already present.
     */
    public void installPack(Pack pack, boolean override) {
        if (!installed || override) {
            if (override) {
                if (!settingsFolder.delete()) {
                    Orbis.getLogger().error("Couldn't reinstall pack: {}, for container: {}", pack.getName(), this.name);
                    return;
                }
            }
            Orbis.getPackManager().extractPack(settingsFolder, pack);
            if (!containerInfo.getPackName().equals(pack.getName())) {
                containerInfo.setPackName(pack.getName());
                try {
                    if (containerInfoFile.delete()) {
                        final String json = Orbis.getGson().toJson(containerInfo);
                        Files.write(containerInfoFile.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                    }
                } catch (IOException ioException) {
                    Orbis.getLogger().error("Failed to write new pack name to container {}", this.name);
                }
            }
            installed = true;
        }
    }

    public String getName() {
        return name;
    }

    public File getFolder() {
        return folder;
    }

    public boolean isInstalled() {
        return installed;
    }

    public File getSettingsFolder() {
        return settingsFolder;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Engine getEngine() {
        return engine;
    }


}
