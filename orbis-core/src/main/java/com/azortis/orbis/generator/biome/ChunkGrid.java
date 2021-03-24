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

import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.HashMap;
import java.util.Map;

public class ChunkGrid {
    private final TIntObjectMap<Biome> biomeMap = new TIntObjectHashMap<>();
    private final Point[] points = new Point[256];

    public ChunkGrid() {
    }

    public void setBiome(int cx, int cz, Biome biome, double weight){
        int biomeHash = biome.hashCode();
        if(!biomeMap.containsKey(biomeHash))biomeMap.put(biomeHash, biome);
        //points[cx * cx] = new Point(this, biomeHash, weight);
    }

    public Point getPoint(int cx, int cz){
        return points[cx * cz];
    }

    public static class Point {
        private final ChunkGrid parent;
        private final int[] biomeArray;
        private final double[] weightArray;

        private Point(ChunkGrid parent, int[] biomeArray, double[] weightArray) {
            this.parent = parent;
            this.biomeArray = biomeArray;
            this.weightArray = weightArray;
        }

        public Biome getBiome(){
            return parent.biomeMap.get(biomeArray[0]);
        }

        public double getWeight(){
            return weightArray[0];
        }

        public Map<Biome, Double> getBiomes(){
            Map<Biome, Double> biomeMap = new HashMap<>();
            for (int i = 0; i < biomeArray.length; i++) {
                biomeMap.put(parent.biomeMap.get(biomeArray[i]), weightArray[i]);
            }
            return biomeMap;
        }

    }

}
