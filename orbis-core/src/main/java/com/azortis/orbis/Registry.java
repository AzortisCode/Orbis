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

package com.azortis.orbis;

import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.biome.SingleDistributor;
import com.azortis.orbis.generator.biome.complex.ComplexDistributor;
import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.azortis.orbis.generator.noise.OpenSimplex2;
import com.azortis.orbis.generator.noise.OpenSimplex2S;
import com.azortis.orbis.generator.terrain.Terrain;
import com.azortis.orbis.generator.terrain.defaults.ConfigTerrain;
import com.azortis.orbis.generator.terrain.defaults.PlainsTerrain;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class Registry<T> {

    public static final Registry<NoiseGenerator> NOISE_GENERATOR = new Registry<>(Map.of(
            Key.key("fastnoise:opensimplex2"), OpenSimplex2.class, Key.key("fastnoise:opensimplex2s"), OpenSimplex2S.class
    ));
    public static final Registry<Distributor> DISTRIBUTOR = new Registry<>(Map.of(
            Key.key("orbis:single"), SingleDistributor.class, Key.key("orbis:complex"), ComplexDistributor.class
    ));
    public static final Registry<Terrain> TERRAIN = new Registry<>(Map.of(
            Key.key("orbis:config"), ConfigTerrain.class, Key.key("orbis:plains"), PlainsTerrain.class
    ));
    private static final Map<Class<?>, Registry<?>> registries = new HashMap<>();

    static {
        addRegistry(NoiseGenerator.class, NOISE_GENERATOR);
        addRegistry(Distributor.class, DISTRIBUTOR);
        addRegistry(Terrain.class, TERRAIN);
    }

    private final Map<Key, Class<? extends T>> typeClasses = new HashMap<>();

    public Registry(Map<Key, Class<? extends T>> defaultTypes) {
        typeClasses.putAll(defaultTypes);
    }

    @SuppressWarnings("unchecked")
    public static <K> Registry<K> getRegistry(@NotNull Class<K> type) {
        return (Registry<K>) registries.get(type);
    }

    public static <K> void addRegistry(@NotNull Class<K> type, @NotNull Registry<K> registry) {
        registries.put(type, registry);
    }

    public static ImmutableMap<Class<?>, Registry<?>> getRegistries() {
        return ImmutableMap.copyOf(registries);
    }

    public boolean hasType(@NotNull Key key) {
        return typeClasses.containsKey(key);
    }

    @SuppressWarnings("PatternValidation")
    public boolean hasType(@NotNull String key) {
        return hasType(Key.key(key));
    }

    public @NotNull Class<? extends T> getType(@NotNull Key key) {
        Preconditions.checkArgument(typeClasses.containsKey(key), "Registry doesnt contain " + key.asString());
        return typeClasses.get(key);
    }

    @SuppressWarnings("PatternValidation")
    public @NotNull Class<? extends T> getType(@NotNull String key) {
        return typeClasses.get(Key.key(key));
    }

    public @NotNull ImmutableSet<Class<?>> getTypes() {
        return ImmutableSet.copyOf(typeClasses.values());
    }

    public void registerType(Key key, Class<? extends T> type) {
        if (!typeClasses.containsKey(key)) {
            typeClasses.put(key, type);
        } else {
            throw new IllegalArgumentException("A type by " + key + " has already been registered in " + this);
        }
    }

}
