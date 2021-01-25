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
import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.terrain.ConfigTerrain;
import com.azortis.orbis.generator.terrain.JavaScriptTerrain;
import com.azortis.orbis.generator.terrain.Terrain;
import com.azortis.orbis.generator.terrain.defaults.PlainsTerrain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class TerrainRegistry {

    public static final String PROVIDER_ID_REGEX = "^([a-z]+):([a-z]+)$";
    private final File terrainFolder;
    private final Map<String, Class<? extends Terrain>> providerIdTerrainMap = new HashMap<>();

    public TerrainRegistry() {
        this.terrainFolder = new File(Orbis.getDirectory(), "/settings/generator/terrain/");
        if (!this.terrainFolder.exists()) {
            if (!this.terrainFolder.mkdirs()) {
                Orbis.getLogger().error("Couldn't create terrain folder.");
            }
        }
        // Register default terrain classes.
        providerIdTerrainMap.put("orbis:config", ConfigTerrain.class);
        providerIdTerrainMap.put("orbis:javascript", JavaScriptTerrain.class);
        providerIdTerrainMap.put("orbis:plains", PlainsTerrain.class);
    }

    /**
     * Gets a {@link Terrain} instance with the right implementation & loaded with the necessary context,
     * Can be used for generation once loaded.
     *
     * @param terrainName The name of the terrain and terrain file.
     * @param context     The biome instance for which the terrain is loaded.
     * @return A terrain instance with the necessary context for generation.
     */
    public Terrain loadTerrain(String terrainName, Biome context) {
        final File terrainFile = new File(terrainFolder, terrainName + ".json");
        try {
            final Terrain terrain = Orbis.getGson().fromJson(new FileReader(terrainFile), Terrain.class);
            terrain.setBiome(context);
            return terrain;
        } catch (FileNotFoundException ex) {
            Orbis.getLogger().error("No terrain file by the name: " + terrainName + " exists!");
        }
        return null;
    }

    public boolean createTerrainFile(String providerId, String terrainName) {
        final File terrainFile = new File(terrainFolder, terrainName + ".json");
        if (!providerIdTerrainMap.containsKey(providerId)) {
            Orbis.getLogger().error("Terrain providerId: " + providerId + " doesn't exist, file creation aborted!");
        } else if (terrainFile.exists()) {
            Orbis.getLogger().error("Terrain by name: " + terrainName + " already exists!");
        } else {
            try {
                if (terrainFile.createNewFile()) {
                    final Terrain terrain = providerIdTerrainMap.get(providerId).getConstructor(String.class, String.class).newInstance(terrainName, providerId);
                    final String json = Orbis.getGson().toJson(terrain, providerIdTerrainMap.get(providerId));
                    Files.write(terrainFile.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                    return true;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                Orbis.getLogger().error("Unable to create terrain file: " + terrainName + ".json!");
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
                Orbis.getLogger().error("Something went wrong with Terrain type registry!");
            }
        }
        return false;
    }

    public boolean registerTerrainClass(String providerId, Class<? extends Terrain> terrainClass) {
        if (!providerIdTerrainMap.containsKey(providerId)) {
            if (providerId.matches(PROVIDER_ID_REGEX)) {
                if (!providerId.split(":")[0].equals("orbis")) { // Prevent the default namespace being used by external code.
                    providerIdTerrainMap.put(providerId, terrainClass);
                } else {
                    Orbis.getLogger().error("Namespace \"orbis\" cannot be used in external code.");
                }
            } else {
                Orbis.getLogger().error("ProviderId: " + providerId + " doesn't comply to the naming scheme.");
            }
        }
        return false;
    }

    public boolean hasProviderId(String providerId) {
        return providerIdTerrainMap.containsKey(providerId);
    }

    public Class<? extends Terrain> getTerrainClass(String providerId) {
        return providerIdTerrainMap.get(providerId);
    }

    public Collection<String> getRegisteredProviderIds() {
        return providerIdTerrainMap.keySet();
    }

    public Collection<Class<? extends Terrain>> getRegisteredTerrainClasses() {
        return providerIdTerrainMap.values();
    }

}
