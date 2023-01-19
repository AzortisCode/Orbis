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

package com.azortis.orbis;

import com.azortis.orbis.generator.Engine;
import com.azortis.orbis.generator.OverworldEngine;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.biome.SingleDistributor;
import com.azortis.orbis.generator.biome.complex.ComplexDistributor;
import com.azortis.orbis.generator.noise.Noise;
import com.azortis.orbis.generator.noise.OpenSimplex2;
import com.azortis.orbis.generator.noise.OpenSimplex2S;
import com.azortis.orbis.generator.surface.Surface;
import com.azortis.orbis.generator.surface.defaults.ConfigSurface;
import com.azortis.orbis.generator.surface.defaults.PlainsSurface;
import com.azortis.orbis.pack.data.DataAccess;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Registry<T> {

    public static final Registry<Engine> ENGINE = new Registry<>(Engine.class, Map.of(
            Key.key("orbis:overworld"), OverworldEngine.class
    ));
    public static final Registry<Noise> NOISE = new Registry<>(Noise.class, Map.of(
            Key.key("fastnoise:opensimplex2"), OpenSimplex2.class, Key.key("fastnoise:opensimplex2s"), OpenSimplex2S.class
    ));
    public static final Registry<Distributor> DISTRIBUTOR = new Registry<>(Distributor.class, Map.of(
            Key.key("orbis:single"), SingleDistributor.class, Key.key("orbis:complex"), ComplexDistributor.class
    ));
    public static final Registry<Surface> SURFACE = new Registry<>(Surface.class, Map.of(
            Key.key("orbis:config"), ConfigSurface.class, Key.key("orbis:plains"), PlainsSurface.class
    ));
    private static final Map<Class<?>, Registry<?>> registries = new HashMap<>();

    static {
        addRegistry(Engine.class, ENGINE);
        addRegistry(Noise.class, NOISE);
        addRegistry(Distributor.class, DISTRIBUTOR);
        addRegistry(Surface.class, SURFACE);
    }

    private final Class<T> type;
    private final Map<Key, Class<? extends T>> typeClasses = new HashMap<>();
    private final Map<Class<?>, ImmutableSet<Class<?>>> dataTypeClasses = new HashMap<>();

    public Registry(Class<T> type, Map<Key, Class<? extends T>> defaultTypes) {
        this.type = type;
        typeClasses.putAll(defaultTypes);
    }

    //
    // Type
    //

    @SuppressWarnings("unchecked")
    public static <K> @NotNull Registry<K> getRegistry(@NotNull Class<K> type) throws IllegalArgumentException {
        Preconditions.checkArgument(registries.containsKey(type),
                "Type " + type.getSimpleName() + "does not have a registry registered!");
        return (Registry<K>) registries.get(type);
    }

    public static <K> void addRegistry(@NotNull Class<K> type, @NotNull Registry<K> registry) {
        registries.put(type, registry);
    }

    public static ImmutableMap<Class<?>, Registry<?>> getRegistries() {
        return ImmutableMap.copyOf(registries);
    }

    public Class<T> type() {
        return type;
    }

    public boolean hasType(@NotNull Key key) {
        return typeClasses.containsKey(key);
    }

    @SuppressWarnings("PatternValidation")
    public boolean hasType(@NotNull String key) {
        return hasType(Key.key(key));
    }

    public @NotNull Class<? extends T> getType(@NotNull Key key) {
        Preconditions.checkArgument(typeClasses.containsKey(key), "Registry doesn't contain " + key.asString());
        return typeClasses.get(key);
    }

    @SuppressWarnings("PatternValidation")
    public @NotNull Class<? extends T> getType(@NotNull String key) {
        return typeClasses.get(Key.key(key));
    }

    public @NotNull ImmutableMap<Key, Class<? extends T>> getTypeMap() {
        return ImmutableMap.copyOf(typeClasses);
    }

    public @NotNull ImmutableSet<Class<? extends T>> getTypes() {
        return ImmutableSet.copyOf(typeClasses.values());
    }

    public void registerType(Key key, Class<? extends T> type) {
        if (!typeClasses.containsKey(key)) {
            typeClasses.put(key, type);
        } else {
            throw new IllegalArgumentException("A type by " + key + " has already been registered in " + this);
        }
    }

    //
    // Data type
    //

    public boolean supportsDataTypes() {
        return DataAccess.GENERATOR_TYPES.containsKey(type);
    }

    public boolean hasDataTypes(Class<?> implementation) {
        return dataTypeClasses.containsKey(implementation) && !dataTypeClasses.get(implementation).isEmpty();
    }

    public @NotNull ImmutableSet<Class<?>> getDataTypes(Class<?> implementation) {
        if (!hasDataTypes(implementation)) return ImmutableSet.of();
        return dataTypeClasses.get(implementation);
    }

    public void registerDataTypes(Class<?> implementation, Set<Class<?>> dataTypes) {
        if (typeClasses.containsValue(implementation)) {
            dataTypeClasses.put(implementation, ImmutableSet.copyOf(dataTypes));
        }
    }

    public void clearDataTypes() {
        dataTypeClasses.clear();
    }

}
