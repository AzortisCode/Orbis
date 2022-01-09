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

public class SimpleDistributor {

    /*private final List<BiomeLayer> biomeLayers;
    //private transient Map<Biome, OldNoiseGenerator> noiseGenMap;


    private SimpleDistributor(String name, Key providerKey, List<BiomeLayer> biomeLayers) {
        super(name, providerKey);
        this.biomeLayers = biomeLayers;
    }

    public void load() {
        //noiseGenMap = new HashMap<>();
        *//*OldRegistry<Biome> biomeOldRegistry = Orbis.getRegistryOld(Biome.class);
        assert biomeOldRegistry != null;
        for (BiomeLayer layer : biomeLayers) {
            layer.setBiome(biomeOldRegistry.loadType(world(), layer.getBiomeName()));
            noiseGenMap.put(layer.getBiome(), new OpenSimplex2S(layer.getSeed()));
        }*//*
    }

    @Override
    public Biome getBiome(int x, int z) {
        return getBiome(x, z);
    }

    @Override
    public Biome getBiome(double x, double z) {
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
        return null;
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
    }*/

}
