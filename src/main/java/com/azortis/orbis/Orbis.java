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
import com.azortis.orbis.registry.*;
import com.azortis.orbis.registry.adapter.TerrainAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class Orbis {

    // Generic fields
    private static boolean initialised = false;
    private static final Logger logger = LoggerFactory.getLogger(Orbis.class);
    private static Gson gson;
    private static File directory;

    // Registries
    private static DimensionRegistry dimensionRegistry;
    private static TerrainRegistry terrainRegistry;

    private Orbis() {
    }

    public static void initialise(OrbisSettings settings) {
        if (!initialised) {
            initialised = true;

            // Load registries
            dimensionRegistry = new DimensionRegistry();
            terrainRegistry = new TerrainRegistry();

            // GsonBuilder
            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
            gsonBuilder.registerTypeAdapter(Terrain.class, new TerrainAdapter(terrainRegistry));

            directory = settings.getOrbisFolder();
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

    // Registries

    public static DimensionRegistry getDimensionRegistry() {
        return dimensionRegistry;
    }

    public static TerrainRegistry getTerrainRegistry() {
        return terrainRegistry;
    }
}
