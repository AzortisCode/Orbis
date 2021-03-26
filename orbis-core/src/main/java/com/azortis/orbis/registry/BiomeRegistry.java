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

package com.azortis.orbis.registry;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.container.Container;
import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.terrain.Terrain;
import com.azortis.orbis.generator.terrain.TerrainLayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BiomeRegistry implements Registry<Biome>{

    private static final String BIOME_DIRECTORY = "/biomes/";

    public BiomeRegistry() {
    }

    @Override
    public Biome loadType(Container container, String name, Object... context) {
        File biomeFile = new File(container.getSettingsFolder(), BIOME_DIRECTORY + name + ".json");
        try {
            Biome biome = Orbis.getGson().fromJson(new FileReader(biomeFile), Biome.class);
            Registry<Terrain> terrainRegistry = Orbis.getRegistry(Terrain.class);
            assert terrainRegistry != null;
            for (TerrainLayer layer : biome.getTerrainLayers()){
                layer.setTerrain(terrainRegistry.loadType(container, layer.getTerrainName(), biome));
            }
            return biome;
        } catch (FileNotFoundException e) {
            Orbis.getLogger().error("Biome file {} not found", name);
        }
        return null;
    }

    @Override
    public List<String> getEntries(Container container) {
        List<String> entryList = new ArrayList<>();
        Arrays.stream(getFolder(container).listFiles()).forEach(file -> entryList.add(file.getName()));
        return entryList;
    }

    @Override
    public void createFolders(Container container) {
        if(!getFolder(container).mkdirs()){
            Orbis.getLogger().error("Failed to create biome directory for {}", container.getName());
        }
    }

    @Override
    public File getFolder(Container container) {
        return new File(container.getSettingsFolder(), BIOME_DIRECTORY);
    }

}
