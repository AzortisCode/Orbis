/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2022 Azortis
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

package com.azortis.orbis.pack.data;

import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.azortis.orbis.generator.terrain.Terrain;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Datasource for fetching {@link com.azortis.orbis.generator.Dimension} datafiles.
 * It uses a {@link String} based system for data paths, which specify where a resource of said {@link Class} type
 * is located. Refer to {@link ComponentAccess} on how to register your own data paths.
 *
 * @since 0.3-Alpha
 * @author Jake Nijssen
 */
public abstract class DataAccess {

    /**
     * The data path for {@link Biome}.
     */
    public static final String BIOMES_PATH = "/biomes/**";

    /**
     * The root data path for all generators.
     */
    public static final String GENERATORS_PATH = "/generators/";

    /**
     * The data path for {@link NoiseGenerator}.
     */
    public static final String NOISE_GENERATORS_PATH = GENERATORS_PATH + "noise/";

    /**
     * The data path for {@link Distributor}.
     */
    public static final String DISTRIBUTOR_PATH = GENERATORS_PATH + "distributor/";

    /**
     * The data path for {@link Terrain}
     */
    public static final String TERRAIN_PATH = GENERATORS_PATH + "terrain/";

    /**
     * Types that support {@link Component}'s, and can have multiple implementations.
     * Root types never have a subdirectory wildcard(**) because those directories should be reserved for components.
     */
    public static final ImmutableMap<Class<?>, String> GENERATOR_TYPES = ImmutableMap.of(
            NoiseGenerator.class, NOISE_GENERATORS_PATH, Distributor.class, DISTRIBUTOR_PATH,
            Terrain.class, TERRAIN_PATH);

    /**
     * Types that do not support {@link Component} and have one implementation.
     */
    public static final ImmutableMap<Class<?>, String> DATA_TYPES = ImmutableMap.of(Biome.class, BIOMES_PATH);

    private final Map<ImmutableSet<Class<?>>, Class<? extends ComponentAccess>> componentTypes = new HashMap<>();
    private final Table<Class<? extends ComponentAccess>, String, ComponentAccess> componentAccessTable = HashBasedTable.create();

    public @NotNull String getDataPath(@NotNull Class<?> type) throws IllegalArgumentException {
        if (type == Biome.class) {
            return BIOMES_PATH;
        } else if (type == NoiseGenerator.class) {
            return NOISE_GENERATORS_PATH;
        } else if (type == Distributor.class) {
            return DISTRIBUTOR_PATH;
        } else if (type == Terrain.class) {
            return TERRAIN_PATH;
        }
        throw new IllegalArgumentException("Unsupported datatype " + type);
    }

    public String getComponentDataPath(@NotNull Class<?> type, @NotNull String name) {
        return getComponent(getComponentType(type), name).getDataPath(type);
    }

    public void registerComponent(@NotNull Class<? extends ComponentAccess> type, @NotNull String name)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ComponentAccess componentAccess = type.getConstructor(String.class, DataAccess.class).newInstance(name, this);
        if (GENERATOR_TYPES.containsKey(componentAccess.parentType)) {
            if (!componentTypes.containsValue(type)) {
                componentTypes.put(ImmutableSet.copyOf(componentAccess.dataTypes()), type);
            }
            if (!componentAccessTable.contains(type, name)) {
                componentAccessTable.put(type, name, componentAccess);
                return;
            }
            throw new IllegalArgumentException("Component of type " + type + " already has an instance by name " + name);
        }
        throw new IllegalArgumentException(componentAccess.parentType + " isn't a supported parent type.");
    }

    public ComponentAccess getComponent(@NotNull Class<? extends ComponentAccess> type, String name) throws IllegalArgumentException {
        ComponentAccess componentAccess = componentAccessTable.get(type, name);
        if (componentAccess != null) return componentAccess;
        throw new IllegalArgumentException("Component of type " + type + " doesn't have an instance by name " + name);
    }

    public Class<? extends ComponentAccess> getComponentType(@NotNull Class<?> type) throws IllegalArgumentException {
        for (ImmutableSet<Class<?>> set : componentTypes.keySet()) {
            if (set.contains(type)) return componentTypes.get(set);
        }
        throw new IllegalArgumentException(type + " isn't a component type");
    }

    public boolean isComponentType(@NotNull Class<?> type) {
        for (ImmutableSet<Class<?>> set : componentTypes.keySet()) {
            if (set.contains(type)) return true;
        }
        return false;
    }

    @NotNull
    public File getDataFile(@NotNull Class<?> type, @NotNull String entry)
            throws FileNotFoundException, IllegalArgumentException {
        return getFile(getDataPath(type), entry);
    }

    @NotNull
    public File getComponentDataFile(@NotNull Class<?> type, @NotNull String name, @NotNull String entry)
            throws FileNotFoundException, IllegalArgumentException {
        if (isComponentType(type)) {
            return getFile(getComponentDataPath(type, name), entry);
        } else {
            throw new IllegalArgumentException("Type " + type + " isn't a valid component type");
        }
    }

    @NotNull
    public Set<File> getDataFiles(@NotNull Class<?> type) throws IllegalArgumentException {
        return getFiles(getDataPath(type));
    }

    @NotNull
    public Set<File> getComponentDataFiles(@NotNull Class<?> type, @NotNull String name) throws IllegalArgumentException {
        if (isComponentType(type)) {
            return getFiles(getComponentDataPath(type, name));
        } else {
            throw new IllegalArgumentException("Type " + type + " isn't a valid component type");
        }
    }

    @NotNull
    public Set<String> getDataEntries(@NotNull Class<?> type) throws IllegalArgumentException {
        return getEntries(getDataPath(type));
    }

    @NotNull
    public Set<String> getComponentDataEntries(@NotNull Class<?> type, @NotNull String name) throws IllegalArgumentException {
        if (isComponentType(type)) {
            return getEntries(getComponentDataPath(type, name));
        } else {
            throw new IllegalArgumentException("Type " + type + " isn't a valid component type");
        }
    }

    public void reset() {
        componentTypes.clear();
        componentAccessTable.clear();
    }

    protected abstract @NotNull File getFile(@NotNull String dataPath, @NotNull String entryName) throws FileNotFoundException;

    protected abstract @NotNull Set<File> getFiles(@NotNull String dataPath);

    protected abstract @NotNull Set<String> getEntries(@NotNull String dataPath);

}
