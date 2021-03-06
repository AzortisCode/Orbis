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

package com.azortis.orbis.registry;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.container.Container;
import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.terrain.Terrain;

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
            GeneratorRegistry<Terrain> terrainRegistry = Orbis.getGeneratorRegistry(Terrain.class);
            assert terrainRegistry != null;
            biome.setTerrain(terrainRegistry.loadType(container, biome.getTerrainName(), biome));
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
