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

package com.azortis.orbis.instance;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.Engine;
import com.azortis.orbis.generator.OrbisEngine;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.storage.StorageLocation;
import net.minestom.server.storage.StorageOptions;
import net.minestom.server.storage.systems.FileStorageSystem;
import net.minestom.server.world.DimensionType;

import java.io.File;

public class OrbisInstance {

    private final String name;
    private final Dimension dimension;
    private final File directory;
    private final Engine engine;
    private final InstanceContainer instanceContainer;

    protected OrbisInstance(String name, Dimension dimension, String storageLocation){
        this.name = name;
        this.dimension = dimension;
        directory = new File(System.getProperty("user.dir"), storageLocation);
        if(!directory.exists())directory.mkdirs();
        engine = new OrbisEngine(dimension);

        // This will get an overhaul in the future, its just temporary
        StorageLocation regionStorageLocation = MinecraftServer.getStorageManager().getLocation(storageLocation,
                new StorageOptions(), new FileStorageSystem());
        instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer(DimensionType.OVERWORLD, regionStorageLocation);
        instanceContainer.setChunkGenerator(engine);
        instanceContainer.enableAutoChunkLoad(true);
        for (int x = -32; x <= 32; x++){
            for (int z = -32; z <= 32; z++){
                instanceContainer.loadChunk(x, z);
            }
        }
    }

    public String getName() {
        return name;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public File getDirectory() {
        return directory;
    }

    public Engine getEngine() {
        return engine;
    }

    public InstanceContainer getInstanceContainer() {
        return instanceContainer;
    }

}
