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

package com.azortis.orbis.generator.biome.complex;

import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.biome.complex.modifier.Modifier;
import com.azortis.orbis.generator.biome.complex.requirement.Requirement;
import com.azortis.orbis.pack.adapter.TypeAdapter;
import com.azortis.orbis.pack.data.ComponentAccess;
import com.azortis.orbis.pack.data.DataAccess;
import com.google.gson.JsonDeserializer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public final class ComplexAccess extends ComponentAccess {

    public static final String REGION_FOLDER = "/**";

    public ComplexAccess(String name, DataAccess dataAccess) {
        super(name, Distributor.class, dataAccess);
    }

    @Override
    protected @NotNull String getTypeDataPath(@NotNull Class<?> type) throws IllegalArgumentException {
        if (type == Region.class) return REGION_FOLDER;
        throw new IllegalArgumentException("Unsupported data type " + type);
    }

    @Override
    public @NotNull Set<Class<?>> dataTypes() {
        return Set.of(Region.class, Requirement.class, Modifier.class);
    }

    @Override
    public @NotNull Map<Class<?>, JsonDeserializer<?>> adapters() {
        return Map.of(
                Requirement.class, new TypeAdapter<>(Requirement.class),
                Modifier.class, new TypeAdapter<>(Modifier.class)
        );
    }
}
