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

import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.biome.SimpleDistributor;
import com.azortis.orbis.generator.biome.complex.ComplexDistributor;
import com.azortis.orbis.generator.terrain.Terrain;
import com.azortis.orbis.generator.terrain.defaults.ConfigTerrain;
import com.azortis.orbis.generator.terrain.defaults.PlainsTerrain;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class Registry<T> {

    public static final Registry<Distributor> DISTRIBUTOR = new Registry<>(Map.of(
            Key.key("orbis:simple"), SimpleDistributor.class, Key.key("orbis:complex"), ComplexDistributor.class
    ));
    public static final Registry<Terrain> TERRAIN = new Registry<>(Map.of(
            Key.key("orbis:config"), ConfigTerrain.class, Key.key("orbis:plains"), PlainsTerrain.class
    ));

    private final Map<Key, Class<? extends T>> typeClasses = new HashMap<>();

    private Registry(Map<Key, Class<? extends T>> defaultTypes) {
        typeClasses.putAll(defaultTypes);
    }

    public Class<? extends T> getType(@NotNull Key key) {
        return typeClasses.get(key);
    }

    @SuppressWarnings("PatternValidation")
    public Class<? extends T> getType(@NotNull String key) {
        return typeClasses.get(Key.key(key));
    }

    public void registerType(Key key, Class<? extends T> type) {
        if (!typeClasses.containsKey(key)) {
            typeClasses.put(key, type);
        } else {
            throw new IllegalArgumentException("A type by " + key + " has already been registered in " + this);
        }
    }

}
