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
import com.azortis.orbis.registry.Registry;
import lombok.Getter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * An representation of a world/instance for core package
 */
public abstract class Container {
    //name, folder, installed, settings folder, dimension, engine

    @Getter
    private final String name;
    @Getter
    private final File folder;
    @Getter
    private final File settingsFolder;

    private final File containerInfoFile;
    private ContainerInfo containerInfo;

    @Getter
    private boolean loaded = false;
    @Getter
    private boolean installed = false;
    @Getter
    private Dimension dimension;
    @Getter
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
            } else {
                reloadContainerInfo();
            }
        } catch (IOException ex) {
            Orbis.getLogger().error("Failed to create container-info.json file for container {}", name);
        }
    }

    public void load() {
        if (!loaded) {
            Registry<Dimension> dimensionRegistry = Orbis.getRegistry(Dimension.class);
            assert dimensionRegistry != null;
            this.dimension = dimensionRegistry.loadType(this, containerInfo.getDimensionFile());
            this.engine = new SimpleEngine(this);
            this.loaded = true;
        }
    }

    public void saveContainerInfo(){
        Orbis.getLogger().info("Saving container-info for {}", name);
        try {
            if(containerInfoFile.delete()) {
                final String containerJson = Orbis.getGson().toJson(containerInfo);
                Files.write(containerInfoFile.toPath(), containerJson.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }
        } catch (IOException ex) {
            Orbis.getLogger().error("Failed to save container-info.json file for container {}", name);
        }
    }

    public void reloadContainerInfo(){
        Orbis.getLogger().info("Loading container-info for {}", name);
        try {
            this.containerInfo = Orbis.getGson().fromJson(new FileReader(containerInfoFile), ContainerInfo.class);
        }catch (FileNotFoundException ex){
            Orbis.getLogger().error("container-info file not found for container: {}", name);
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
            Orbis.getLogger().info("Installing pack {} version {} for {}", pack.getName(), pack.getPackVersion(), name);
            Orbis.getPackManager().extractPack(settingsFolder, pack);
            if (containerInfo.getPackName() == null) {
                containerInfo.setPackName(pack.getName());
                containerInfo.setDimensionFile(pack.getDimensionFile());
                saveContainerInfo();
            }
            installed = true;
        }
    }
}
