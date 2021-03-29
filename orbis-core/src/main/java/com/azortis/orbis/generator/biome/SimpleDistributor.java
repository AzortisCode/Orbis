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

package com.azortis.orbis.generator.biome;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.azortis.orbis.generator.noise.OpenSimplex2S;
import com.azortis.orbis.registry.Registry;
import com.azortis.orbis.util.NamespaceId;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SimpleDistributor extends Distributor{

    private List<BiomeLayer> biomeLayers;
    private transient Map<Biome, NoiseGenerator> noiseGenMap;

    private SimpleDistributor() {
    }

    public SimpleDistributor(String name, NamespaceId distributor) {
        super(name, distributor);
    }

    @Override
    public void load() {
        noiseGenMap = new HashMap<>();
        Registry<Biome> biomeRegistry = Orbis.getRegistry(Biome.class);
        assert biomeRegistry != null;
        for (BiomeLayer layer : biomeLayers){
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
        for (BiomeLayer layer : biomeLayers){
            double noiseValue = noiseGenMap.get(layer.getBiome()).noise(x / layer.getZoom(), z / layer.getZoom());
            if(noiseValue > maxValue){
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
