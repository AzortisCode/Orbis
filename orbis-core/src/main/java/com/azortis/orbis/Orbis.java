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

import com.azortis.orbis.container.Container;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.biome.Region;
import com.azortis.orbis.generator.terrain.Terrain;
import com.azortis.orbis.pack.PackManager;
import com.azortis.orbis.registry.*;
import com.azortis.orbis.registry.adapter.NamespaceIdAdapter;
import com.azortis.orbis.registry.adapter.TerrainAdapter;
import com.azortis.orbis.util.NamespaceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Orbis {

    public static final String SETTINGS_VERSION = "1";

    private static Adapter adapter = null;
    private static Logger logger;
    private static Gson gson;
    private static Map<Class<?>, Registry<?>> registries;
    private static Map<Class<?>, GeneratorRegistry<?>> generatorRegistries;
    private static Map<String, Container> containerMap;

    // Managers
    private static PackManager packManager;

    private Orbis(){
    }

    public static void initialize(Adapter adaptation){
        if(adapter == null){
            adapter = adaptation;
            logger = adaptation.getLogger();
            logger.info("Initializing {} adaptation of Orbis", adaptation.getAdaptation());

            // Load registries
            registries = new HashMap<>();
            registries.put(Dimension.class, new DimensionRegistry());
            registries.put(Region.class, new RegionRegistry());
            registries.put(Biome.class, new BiomeRegistry());
            generatorRegistries = new HashMap<>();
            generatorRegistries.put(Terrain.class, new TerrainRegistry());

            GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting();
            gsonBuilder.registerTypeAdapter(NamespaceId.class, new NamespaceIdAdapter());
            gsonBuilder.registerTypeAdapter(Terrain.class, new TerrainAdapter());
            gson = gsonBuilder.create();

            // Load managers
            packManager = new PackManager(adaptation.getDirectory());

            containerMap = new HashMap<>();
        }
    }

    public static Adapter getAdapter() {
        return adapter;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static Gson getGson() {
        return gson;
    }

    @SuppressWarnings("unchecked")
    public static <T> Registry<T> getRegistry(Class<? extends T> typeClass){
        Registry<?> registry = registries.get(typeClass);
        if(registry != null)return (Registry<T>) registry;
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> GeneratorRegistry<T> getGeneratorRegistry(Class<T> typeClass){
        GeneratorRegistry<?> registry = generatorRegistries.get(typeClass);
        if(registry != null)return (GeneratorRegistry<T>) registry;
        return null;
    }

    //
    // Container
    //

    /**
     * FOR INTERNAL USE ONLY!
     *
     * @param container The container to be registered.
     */
    public static void registerContainer(@NotNull Container container){
        containerMap.putIfAbsent(container.getName(), container);
    }

    @Nullable
    public static Container getContainer(String name){
        return containerMap.get(name);
    }

    @NotNull
    public static Collection<Container> getContainers(){
        return containerMap.values();
    }

    //
    // Managers
    //

    public static PackManager getPackManager() {
        return packManager;
    }
}
