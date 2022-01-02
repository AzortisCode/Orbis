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

import com.azortis.orbis.Orbis;
import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.azortis.orbis.generator.noise.OpenSimplex2S;
import com.azortis.orbis.registry.Registry;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleDistributor extends Distributor {

    private List<BiomeLayer> biomeLayers;
    private transient Map<Biome, NoiseGenerator> noiseGenMap;

    private SimpleDistributor() {
    }

    public SimpleDistributor(String name, Key distributor) {
        super(name, distributor);
    }

    @Override
    public void load() {
        noiseGenMap = new HashMap<>();
        Registry<Biome> biomeRegistry = Orbis.getRegistry(Biome.class);
        assert biomeRegistry != null;
        for (BiomeLayer layer : biomeLayers) {
            layer.setBiome(biomeRegistry.loadType(getContainer(), layer.getBiomeName()));
            noiseGenMap.put(layer.getBiome(), new OpenSimplex2S(layer.getSeed()));
        }
    }

    @Override
    public Biome getBiomeAt(int x, int z) {
        return getBiome(x, z);
    }

    @Override
    public Biome getBiomeAt(double x, double z) {
        return getBiome(x, z);
    }

    @NotNull
    private Biome getBiome(double x, double z) {
        double maxValue = Double.NEGATIVE_INFINITY;
        Biome biome = null;
        for (BiomeLayer layer : biomeLayers) {
            double noiseValue = noiseGenMap.get(layer.getBiome()).noise(x / layer.getZoom(), z / layer.getZoom());
            if (noiseValue > maxValue) {
                biome = layer.getBiome();
                maxValue = noiseValue;
            }
        }
        assert biome != null;
        return biome;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Getter
    public static class BiomeLayer {
        @SerializedName("biome")
        private String biomeName;
        private long seed;
        private int zoom;
        @Setter
        private transient Biome biome;
    }

}
