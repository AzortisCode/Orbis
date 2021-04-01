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
import com.azortis.orbis.generator.terrain.defaults.*;
import com.azortis.orbis.util.NamespaceId;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerrainRegistry implements GeneratorRegistry<Terrain> {

    private static final String TERRAIN_DIRECTORY = "/generators/terrain/";
    private final Map<NamespaceId, Class<? extends Terrain>> terrainClasses = new HashMap<>();

    public TerrainRegistry(){
        // Initialize Orbis default terrains
        terrainClasses.put(new NamespaceId("orbis:config"), ConfigTerrain.class);
        terrainClasses.put(new NamespaceId("orbis:plains"), PlainsTerrain.class);
    }

    @Override
    public Terrain loadType(Container container, String name, Object... context) {
        if(context[0] instanceof Biome){
            Biome biome = (Biome) context[0];
            File terrainFile = new File(container.getSettingsFolder(), TERRAIN_DIRECTORY + name + ".json");
            try {
                Terrain terrain = Orbis.getGson().fromJson(new FileReader(terrainFile), Terrain.class);
                terrain.setContainer(container);
                terrain.setBiome(biome);
                return terrain;
            } catch (FileNotFoundException ex){
                Orbis.getLogger().error("Terrain file {} not found!", name);
            }
        }
        return null;
    }

    @Override
    public List<String> getEntries(Container container) {
        File terrainFolder = new File(container.getSettingsFolder(), TERRAIN_DIRECTORY);
        List<String> entries = new ArrayList<>();
        for (File terrainFile : terrainFolder.listFiles()){
            if(terrainFile.getName().endsWith(".json")){
                entries.add(terrainFile.getName().replace(".json", ""));
            }
        }
        return entries;
    }

    @Override
    public void createFolders(Container container) {
        File folders = getFolder(container);
        if(folders.isDirectory()){
            if(!folders.mkdirs()){
                Orbis.getLogger().error("Failed to create terrain folder for {}", container.getName());
            }
        }
    }

    @Override
    public File getFolder(Container container) {
        return new File(container.getSettingsFolder(), TERRAIN_DIRECTORY);
    }

    @Override
    public void registerTypeClass(NamespaceId namespaceId, Class<? extends Terrain> typeClass){
        if(!namespaceId.getNamespace().equals("orbis")){
            if(!terrainClasses.containsKey(namespaceId)){
                terrainClasses.put(namespaceId, typeClass);
            } else {
                Orbis.getLogger().error("Terrain type namespaceId {} already exists!", namespaceId.getNamespaceId());
            }
        } else {
            Orbis.getLogger().error("Cannot register outside terrain type with the orbis namespace!");
        }
    }

    @Override
    public Class<? extends Terrain> getTypeClass(NamespaceId namespaceId){
        return terrainClasses.get(namespaceId);
    }

    @Override
    public List<NamespaceId> getTypeNamespaceIds() {
        return new ArrayList<>(terrainClasses.keySet());
    }
}
