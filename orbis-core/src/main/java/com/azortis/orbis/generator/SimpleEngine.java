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

package com.azortis.orbis.generator;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.block.BlockRegistry;
import com.azortis.orbis.block.Blocks;
import com.azortis.orbis.container.Container;
import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.biome.BiomeGrid;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.interpolation.Interpolator;
import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.azortis.orbis.generator.noise.PerlinNoise;

import java.util.Objects;

public class SimpleEngine extends Engine {

    private final NoiseGenerator noise;
    private final Distributor distributor;
    private final Interpolator interpolator = new Interpolator();

    public SimpleEngine(Container container) {
        super(container);
        this.noise = new PerlinNoise(getDimension().getSeed());
        this.distributor = Objects.requireNonNull(Orbis.getGeneratorRegistry(Distributor.class))
                .loadType(container, getDimension().getDistributor());
    }

    @Override
    public void generateChunkData(ChunkData chunkData, BiomeGrid biomeGrid, int chunkX, int chunkZ) {
        // We start a for loop for each consecutive horizontal coordinate of the chunk.
        for (int cx = 0; cx < 16; cx++) {
            for (int cz = 0; cz < 16; cz++) {
                // Get the actual (x,z) coordinates in the world
                final int x = cx + (chunkX << 4);
                final int z = cz + (chunkZ << 4);

                Biome biome = distributor.getBiomeAt(x, z);
                int height = interpolator.getFinalHeight(x, z, getDimension().getInterpolationRadius(), this::getHeight);
                //int height = (int) getHeight(x, z);

                for (int y = 0; y < getDimension().getMaxHeight(); y++) {
                    biomeGrid.setBiome(x, y, z, biome);

                    if (y == 0) {
                        chunkData.setBlock(cx, y, cz, Blocks.BEDROCK.getDefaultState());
                    } else if (y > height && y <= getDimension().getFluidHeight()) {
                        chunkData.setBlock(cx, y, cz, Blocks.WATER.getDefaultState());
                    } else if (y <= height) {
                        if (y <= (height - 6)) {
                            chunkData.setBlock(cx, y, cz, Blocks.STONE.getDefaultState());
                        } else {
                            chunkData.setBlock(cx, y, cz, BlockRegistry.fromKey(biome.getSurfaceBlock()).getDefaultState());
                        }
                    }
                }
            }
        }
    }

    public double getHeight(double x, double z) {
        Biome biome = distributor.getBiomeAt(x, z);
        return biome.getTerrain().getTerrainHeight((int) x, (int) z, 1.0d, noise);
    }

}
