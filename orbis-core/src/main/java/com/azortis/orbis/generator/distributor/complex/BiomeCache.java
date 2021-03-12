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

package com.azortis.orbis.generator.distributor.complex;

import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.biome.Biome;
import gnu.trove.map.hash.TIntObjectHashMap;

public class BiomeCache {
    private final Dimension dimension;
    private final TIntObjectHashMap<Biome> biomeMap = new TIntObjectHashMap<>();
    private final int[] biomes;
    private final double[] weights;

    public BiomeCache(Dimension dimension) {
        this.dimension = dimension;

        int ySize = Math.abs(dimension.getMinHeight()) + dimension.getMaxHeight();
        biomes = new int[16 * ySize * 16];
        weights = new double[16 * ySize * 16];
    }

    protected void setBiome(int x, int y, int z, Biome biome, double weight){
        int biomeHash = biome.hashCode();
        if(!biomeMap.containsKey(biomeHash))biomeMap.put(biomeHash, biome);
        int yIndex = y + Math.abs(dimension.getMinHeight());
        biomes[x * yIndex * z] = biomeHash;
        weights[x * yIndex * z] = weight;
    }

    public Point getPoint(int x, int y, int z){
        int yIndex = y + Math.abs(dimension.getMinHeight());
        return new Point(biomeMap.get(biomes[x * yIndex * z]), weights[x * yIndex * z]);
    }

    public static class Point {
        private final Biome biome;
        private final double weight;

        private Point(Biome biome, double weight) {
            this.biome = biome;
            this.weight = weight;
        }

        public Biome getBiome() {
            return biome;
        }

        public double getWeight() {
            return weight;
        }
    }

}
