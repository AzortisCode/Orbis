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

package com.azortis.orbis;

import com.azortis.orbis.generator.terrain.Terrain;
import com.azortis.orbis.instance.InstanceManager;
import com.azortis.orbis.registry.*;
import com.azortis.orbis.registry.adapter.TerrainAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;

@SuppressWarnings("unused")
public final class Orbis {

    // Generic fields
    private static boolean initialised = false;
    private static final Logger logger = LoggerFactory.getLogger(Orbis.class);
    private static Gson gson;
    private static File directory;
    private static File settingsFile;
    private static OrbisSettings settings;

    // Managers
    private static InstanceManager instanceManager;

    // Registries
    private static DimensionRegistry dimensionRegistry;
    private static BiomeRegistry biomeRegistry;
    private static TerrainRegistry terrainRegistry;

    private Orbis() {
    }

    public static void initialise(File directory) {
        if (!initialised) {
            initialised = true;
            Orbis.directory = directory;
            if(!directory.exists()){
                if(!directory.mkdirs())logger.error("Couldn't create orbis directory!");
            }

            // Load registries
            dimensionRegistry = new DimensionRegistry();
            biomeRegistry = new BiomeRegistry();
            terrainRegistry = new TerrainRegistry();

            // Gson
            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
            gsonBuilder.registerTypeAdapter(Terrain.class, new TerrainAdapter(terrainRegistry));
            gson = gsonBuilder.create();

            // Load settings file
            settingsFile = new File(directory, "settings.json");
            try{
                if(!settingsFile.exists())FileUtils.copyURLToFile(Objects.requireNonNull(Orbis.class.getClassLoader().getResource("settings.json")), settingsFile);
                settings = gson.fromJson(new FileReader(settingsFile), OrbisSettings.class);
            } catch (IOException ex){
                logger.error("Something went wrong with loading the settings file.");
            }

            // Load managers
            instanceManager = new InstanceManager(settings);

        }
    }

    public static boolean isInitialised() {
        return initialised;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static Gson getGson() {
        return gson;
    }

    public static File getDirectory() {
        return directory;
    }

    public static File getSettingsFile() {
        return settingsFile;
    }

    public static OrbisSettings getSettings() {
        return settings;
    }

    // Managers

    public static InstanceManager getInstanceManager() {
        return instanceManager;
    }


    // Registries

    public static DimensionRegistry getDimensionRegistry() {
        return dimensionRegistry;
    }

    public static RegionRegistry getRegionRegistry() {
        return null;
    }

    public static BiomeRegistry getBiomeRegistry() {
        return biomeRegistry;
    }

    public static TerrainRegistry getTerrainRegistry() {
        return terrainRegistry;
    }

    private static void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
