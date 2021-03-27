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

import com.azortis.orbis.generator.biome.point.ChunkPointGatherer;
import com.azortis.orbis.generator.biome.point.GatheredPoint;
import com.azortis.orbis.generator.biome.point.PointGatherer;

import java.util.List;

/**
 * Credits to k.jpg from FastNoise for making this class.
 *
 * This class is a default option for every distributor if no weight algorithm is assigned.
 */
public class ScatteredBiomeBlender {

    private static final int CHUNK_WIDTH = 16;
    private static final int CHUNK_COLLUM_COUNT = CHUNK_WIDTH * CHUNK_WIDTH;
    private static final int CHUNK_WIDTH_MIN_ONE = CHUNK_WIDTH - 1;

    private final int blendRadiusBoundArrayCenter;
    private final double blendRadius, blendRadiusSq;
    private final double[] blendRadiusBound;
    private final ChunkPointGatherer<LinkedBiomeWeightMap> gatherer;

    public ScatteredBiomeBlender(double samplingFrequency, double blendRadiusPadding){
        this.blendRadius = blendRadiusPadding + PointGatherer.MAX_GRIDSCALE_DISTANCE_TO_CLOSEST_POINT / samplingFrequency;
        this.blendRadiusSq = blendRadius * blendRadius;
        this.gatherer = new ChunkPointGatherer<>(samplingFrequency, blendRadius);

        blendRadiusBoundArrayCenter = (int)Math.ceil(blendRadius) - 1;
        blendRadiusBound = new double[blendRadiusBoundArrayCenter * 2 + 1];
        for (int i = 0; i < blendRadiusBound.length; i++) {
            int dx = i - blendRadiusBoundArrayCenter;
            int maxDxBeforeTruncate = Math.abs(dx) + 1;
            blendRadiusBound[i] = Math.sqrt(blendRadiusSq - maxDxBeforeTruncate);
        }
    }

    public LinkedBiomeWeightMap getBlendForChunk(long seed, int chunkBaseWorldX, int chunkBaseWorldZ, BiomeEvaluationCallback callback) {
        chunkBaseWorldX = (chunkBaseWorldX << 4);
        chunkBaseWorldZ = (chunkBaseWorldZ << 4);

        // Get the list of data points in range.
        List<GatheredPoint<LinkedBiomeWeightMap>> points = gatherer.getPointsFromChunkBase(seed, chunkBaseWorldX, chunkBaseWorldZ);

        // Evaluate and aggregate all biomes to be blended in this chunk.
        LinkedBiomeWeightMap linkedBiomeMapStartEntry = null;
        for (GatheredPoint<LinkedBiomeWeightMap> point : points) {

            // Get the biome for this data point from the callback.
            int biome = callback.getBiomeAt(point.getX(), point.getZ()).hashCode();

            // Find or create the chunk biome blend weight layer entry for this biome.
            LinkedBiomeWeightMap entry = linkedBiomeMapStartEntry;
            while (entry != null) {
                if (entry.getBiome() == biome) break;
                entry = entry.getNext();
            }
            if (entry == null) {
                entry = linkedBiomeMapStartEntry =
                        new LinkedBiomeWeightMap(biome, linkedBiomeMapStartEntry);
            }

            point.setTag(entry);
        }

        // If there is only one biome in range here, we can skip the actual blending step.
        if (linkedBiomeMapStartEntry != null && linkedBiomeMapStartEntry.getNext() == null) {
            /*double[] weights = new double[chunkColumnCount];
            linkedBiomeMapStartEntry.setWeights(weights);
            for (int i = 0; i < chunkColumnCount; i++) {
                weights[i] = 1.0;
            }*/
            return linkedBiomeMapStartEntry;
        }

        for (LinkedBiomeWeightMap entry = linkedBiomeMapStartEntry; entry != null; entry = entry.getNext()) {
            entry.setWeights(new double[CHUNK_COLLUM_COUNT]);
        }

        double z = chunkBaseWorldZ, x = chunkBaseWorldX;
        double xStart = x;
        double xEnd = xStart + CHUNK_WIDTH_MIN_ONE;
        for (int i = 0; i < CHUNK_COLLUM_COUNT; i++) {

            // Consider each data point to see if it's inside the radius for this column.
            double columnTotalWeight = 0.0;
            for (GatheredPoint<LinkedBiomeWeightMap> point : points) {
                double dx = x - point.getX();
                double dz = z - point.getZ();

                double distSq = dx * dx + dz * dz;

                // If it's inside the radius...
                if (distSq < blendRadiusSq) {

                    // Relative weight = [r^2 - (x^2 + z^2)]^2
                    double weight = blendRadiusSq - distSq;
                    weight *= weight;

                    point.getTag().getWeights()[i] += weight;
                    columnTotalWeight += weight;
                }
            }

            // Normalize so all weights in a column add up to 1.
            double inverseTotalWeight = 1.0 / columnTotalWeight;
            for (LinkedBiomeWeightMap entry = linkedBiomeMapStartEntry; entry != null; entry = entry.getNext()) {
                entry.getWeights()[i] *= inverseTotalWeight;
            }

            // A double can fully represent an int, so no precision loss to worry about here.
            if (x == xEnd) {
                x = xStart;
                z++;
            } else x++;
        }

        return linkedBiomeMapStartEntry;
    }

    @FunctionalInterface
    public interface BiomeEvaluationCallback {
        Biome getBiomeAt(double x, double z);
    }


}
