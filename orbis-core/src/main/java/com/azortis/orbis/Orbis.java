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

package com.azortis.orbis;

import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.terrain.Terrain;
import com.azortis.orbis.pack.PackManager;
import com.azortis.orbis.registry.adapter.DistributorAdapter;
import com.azortis.orbis.registry.*;
import com.azortis.orbis.registry.adapter.NamespaceIdAdapter;
import com.azortis.orbis.registry.adapter.TerrainAdapter;
import com.azortis.orbis.util.NamespaceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public final class Orbis {

    public static final String SETTINGS_VERSION = "1";

    @Getter
    private static Platform platform = null;
    @Getter
    private static Logger logger;
    @Getter
    private static Gson gson;
    private static Map<Class<?>, Registry<?>> registries;
    private static Map<Class<?>, GeneratorRegistry<?>> generatorRegistries;

    // Managers
    @Getter
    private static PackManager packManager;

    private Orbis() {
    }

    public static void initialize(Platform platform) {
        // Only initialize once.
        if (Orbis.platform == null) {
            Orbis.platform = platform;
            logger = platform.getLogger();
            logger.info("Initializing {} adaptation of Orbis", platform.getAdaptation());

            // Load registries for loading certain objects of packs dynamically
            registries = new HashMap<>();
            registries.put(Dimension.class, new DimensionRegistry());
            registries.put(Biome.class, new BiomeRegistry());
            generatorRegistries = new HashMap<>();
            generatorRegistries.put(Terrain.class, new TerrainRegistry());
            generatorRegistries.put(Distributor.class, new DistributorRegistry());

            // Register the type adapters to use in the serialization/deserialization of settings in packs.
            gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting()
                    .registerTypeAdapter(NamespaceId.class, new NamespaceIdAdapter())
                    .registerTypeAdapter(Terrain.class, new TerrainAdapter())
                    .registerTypeAdapter(Distributor.class, new DistributorAdapter()).create();

            // Load managers
            packManager = new PackManager(platform.getDirectory());

        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Registry<T> getRegistry(Class<? extends T> typeClass) {
        return (Registry<T>) registries.get(typeClass);
    }

    @SuppressWarnings("unchecked")
    public static <T> GeneratorRegistry<T> getGeneratorRegistry(Class<T> typeClass) {
        return (GeneratorRegistry<T>) generatorRegistries.get(typeClass);
    }
}
