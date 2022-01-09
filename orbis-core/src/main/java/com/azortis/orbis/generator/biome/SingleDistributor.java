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

package com.azortis.orbis.generator.biome;

import com.azortis.orbis.util.Inject;
import com.google.gson.annotations.SerializedName;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class SingleDistributor extends Distributor {

    @SerializedName("biome")
    private final String biomeName;

    @Inject(fieldName = "biomeName")
    private transient Biome biome;

    private SingleDistributor(String name, Key providerKey, String biomeName) {
        super(name, providerKey);
        this.biomeName = biomeName;
    }

    @Override
    public @NotNull Set<Biome> getBiomes() {
        return Set.of(biome);
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        return biome;
    }

    @Override
    public Biome getBiome(double x, double y, double z) {
        return biome;
    }

    @Override
    public @NotNull Set<Key> getNativeBiomes() {
        return Set.of(biome.derivative());
    }

    @Override
    public Key getNativeBiome(int x, int y, int z) {
        return biome.derivative();
    }
}
