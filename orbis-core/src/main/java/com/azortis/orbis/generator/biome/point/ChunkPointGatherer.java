package com.azortis.orbis.generator.biome.point;

import java.util.List;

/**
 * Credits to k.jpg from FastNoise for making this class.
 *
 * @param <TTag> The type tag of the point gatherer.
 */
public class ChunkPointGatherer<TTag> {

    private static final double CHUNK_RADIUS_RATIO = Math.sqrt(1.0 / 2.0);
    private static final int CHUNK_WIDTH = 16;
    private static final int HALF_CHUNK_WIDTH = CHUNK_WIDTH / 2;

    private final double maxPointContributionRadius;
    private final double maxPointContributionRadiusSq;
    private final PointGatherer<TTag> unfilteredPointGatherer;

    public ChunkPointGatherer(double frequency, double maxPointContributionRadius) {
        this.maxPointContributionRadius = maxPointContributionRadius;
        this.maxPointContributionRadiusSq = maxPointContributionRadius * maxPointContributionRadius;
        unfilteredPointGatherer = new PointGatherer<>(frequency,
                maxPointContributionRadius + CHUNK_WIDTH * CHUNK_RADIUS_RATIO);
    }

    public List<GatheredPoint<TTag>> getPointsFromChunkBase(long seed, int chunkBaseWorldX, int chunkBaseWorldZ) {
        return getPointsFromChunkCenter(seed, chunkBaseWorldX + HALF_CHUNK_WIDTH, chunkBaseWorldZ + HALF_CHUNK_WIDTH);
    }

    public List<GatheredPoint<TTag>> getPointsFromChunkCenter(long seed, int chunkCenterWorldX, int chunkCenterWorldZ) {
        List<GatheredPoint<TTag>> worldPoints =
                unfilteredPointGatherer.getPoints(seed, chunkCenterWorldX, chunkCenterWorldZ);
        for (int i = 0; i < worldPoints.size(); i++) {
            GatheredPoint<TTag> point = worldPoints.get(i);
            // Check if point contribution radius lies outside any coordinate in the chunk
            double axisCheckValueX = Math.abs(point.getX() - chunkCenterWorldX) - HALF_CHUNK_WIDTH;
            double axisCheckValueZ = Math.abs(point.getZ() - chunkCenterWorldZ) - HALF_CHUNK_WIDTH;
            if (axisCheckValueX >= maxPointContributionRadius || axisCheckValueZ >= maxPointContributionRadius
                    || (axisCheckValueX > 0 && axisCheckValueZ > 0
                    && axisCheckValueX * axisCheckValueX + axisCheckValueZ * axisCheckValueZ >= maxPointContributionRadiusSq)) {

                // If so, remove it.
                // Copy the last value to this value, and remove the last,
                // to avoid shifting because order doesn't matter.
                int lastIndex = worldPoints.size() - 1;
                worldPoints.set(i, worldPoints.get(lastIndex));
                worldPoints.remove(lastIndex);
                i--;
            }
        }
        return worldPoints;
    }

}
