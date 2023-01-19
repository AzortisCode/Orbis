/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2023 Azortis
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
import com.azortis.orbis.generator.noise.Noise;
import com.azortis.orbis.generator.surface.Surface;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

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
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public abstract class DataAccess {

    //
    // Core Orbis data types.
    //

    /**
     * The data path for {@link Biome}.
     */
    public static final String BIOMES_PATH = "/biomes/**";

    /**
     * The root data path for all generators.
     */
    public static final String GENERATORS_PATH = "/generators/";

    /**
     * The data path for {@link Noise}.
     */
    public static final String NOISE_PATH = GENERATORS_PATH + "noise/";

    /**
     * The data path for {@link Distributor}.
     */
    public static final String DISTRIBUTOR_PATH = GENERATORS_PATH + "distributor/";

    /**
     * The data path for {@link Surface}
     */
    public static final String SURFACE_PATH = GENERATORS_PATH + "surface/";

    /**
     * Types that support {@link Component}'s, and can have multiple implementations.
     * Root types never have a subdirectory wildcard(**) because those directories should be reserved for components.
     */
    public static final ImmutableMap<Class<?>, String> GENERATOR_TYPES = ImmutableMap.of(
            Noise.class, NOISE_PATH, Distributor.class, DISTRIBUTOR_PATH,
            Surface.class, SURFACE_PATH);

    /**
     * Types that do not support {@link Component} and have one implementation.
     */
    public static final ImmutableMap<Class<?>, String> DATA_TYPES = ImmutableMap.of(Biome.class, BIOMES_PATH);

    //
    // Component data types.
    //

    /**
     * Maps all component data type classes to the {@link ComponentAccess} type.
     */
    private final Map<ImmutableSet<Class<?>>, Class<? extends ComponentAccess>> componentTypes = new HashMap<>();

    /**
     * Maps each {@link ComponentAccess} type to a component name and access instance.
     */
    private final Table<Class<? extends ComponentAccess>, String, ComponentAccess> componentAccessTable = HashBasedTable.create();

    /**
     * Gets the data path for a core Orbis type.
     *
     * @param type The data type.
     * @return The corresponding data path.
     * @throws IllegalArgumentException If the given type is not a core data type.
     */
    public @NotNull String getDataPath(@NotNull Class<?> type) throws IllegalArgumentException {
        if (type == Biome.class) {
            return BIOMES_PATH;
        } else if (type == Noise.class) {
            return NOISE_PATH;
        } else if (type == Distributor.class) {
            return DISTRIBUTOR_PATH;
        } else if (type == Surface.class) {
            return SURFACE_PATH;
        }
        throw new IllegalArgumentException("Unsupported datatype " + type);
    }

    /**
     * Gets the data path for a component type registered with this access.
     *
     * @param type The data type.
     * @param name The name of the component instance.
     * @return The corresponding data path.
     * @throws IllegalArgumentException If given type is not a registered component type.
     *                                  See {@link DataAccess#isComponentType(Class)}
     */
    public @NotNull String getDataPath(@NotNull Class<?> type, @NotNull String name) throws IllegalArgumentException {
        Preconditions.checkArgument(isComponentType(type), "Given type is not registered as component type!");
        return getComponentAccess(getAccessType(type), name).getDataPath(type);
    }

    /**
     * Registers a component with the data access.
     *
     * @param type The access type.
     * @param name The name of the component instance.
     * @since 0.3-Alpha
     */
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

    /**
     * Gets the {@link ComponentAccess} instance for the given component type and name.
     *
     * @param type The access type.
     * @param name The name of the component instance.
     * @return The access instance for given component.
     * @throws IllegalArgumentException If no component is registered for given type and name.
     */
    public ComponentAccess getComponentAccess(@NotNull Class<? extends ComponentAccess> type, String name)
            throws IllegalArgumentException {
        ComponentAccess componentAccess = componentAccessTable.get(type, name);
        if (componentAccess != null) return componentAccess;
        throw new IllegalArgumentException("Component of type " + type + " doesn't have an instance by name " + name);
    }

    /**
     * Gets the {@link ComponentAccess} type for the given data type.
     *
     * @param type The data type that the component manages.
     * @return The corresponding component type.
     * @throws IllegalArgumentException If the data type isn't part of a component access.
     */
    public @NotNull Class<? extends ComponentAccess> getAccessType(@NotNull Class<?> type)
            throws IllegalArgumentException {
        for (ImmutableSet<Class<?>> set : componentTypes.keySet()) {
            if (set.contains(type)) return componentTypes.get(set);
        }
        throw new IllegalArgumentException(type + " isn't a component data type");
    }

    public @Unmodifiable @NotNull Set<String> getComponentNames(@NotNull Class<?> type)
            throws IllegalArgumentException {
        Preconditions.checkArgument(type.isAnnotationPresent(Component.class),
                "Given type is not a component");
        Class<? extends ComponentAccess> accessType = type.getAnnotation(Component.class).value();
        if (componentAccessTable.containsRow(accessType)) {
            return Set.copyOf(componentAccessTable.row(accessType).keySet());
        }
        return Set.of();
    }

    /**
     * Checks if given datatype is part of a component.
     * For reference see {@link ComponentAccess#dataTypes()}
     *
     * @param type The data type.
     * @return If it is part of a component.
     */
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
    public File getDataFile(@NotNull Class<?> type, @NotNull String name, @NotNull String entry)
            throws FileNotFoundException, IllegalArgumentException {
        if (isComponentType(type)) {
            return getFile(getDataPath(type, name), entry);
        } else {
            throw new IllegalArgumentException("Type " + type + " isn't a valid component type");
        }
    }

    @NotNull
    public Set<File> getDataFiles(@NotNull Class<?> type) throws IllegalArgumentException {
        return getFiles(getDataPath(type));
    }

    @NotNull
    public Set<File> getDataFiles(@NotNull Class<?> type, @NotNull String name) throws IllegalArgumentException {
        if (isComponentType(type)) {
            return getFiles(getDataPath(type, name));
        } else {
            throw new IllegalArgumentException("Type " + type + " isn't a valid component type");
        }
    }

    @NotNull
    public Set<String> getDataEntries(@NotNull Class<?> type) throws IllegalArgumentException {
        return getEntries(getDataPath(type));
    }

    @NotNull
    public Set<String> getDataEntries(@NotNull Class<?> type, @NotNull String name) throws IllegalArgumentException {
        if (isComponentType(type)) {
            return getEntries(getDataPath(type, name));
        } else {
            throw new IllegalArgumentException("Type " + type + " isn't a valid component type");
        }
    }

    public void reset() {
        componentTypes.clear();
        componentAccessTable.clear();
    }

    protected abstract @NotNull File getFile(@NotNull String dataPath, @NotNull String entryName) throws FileNotFoundException;

    protected abstract @NotNull @Unmodifiable Set<File> getFiles(@NotNull String dataPath);

    protected abstract @NotNull @Unmodifiable Set<String> getEntries(@NotNull String dataPath);

}
