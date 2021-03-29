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

package com.azortis.orbis.generator;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.block.Block;
import com.azortis.orbis.container.Container;
import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.biome.BiomeGrid;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.interpolation.Interpolator;
import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.azortis.orbis.generator.noise.PerlinNoise;
import com.azortis.orbis.util.NamespaceId;

import java.util.Objects;

public class SimpleEngine extends Engine {

    private final NoiseGenerator noise;
    private final Distributor distributor;
    private final Interpolator interpolator = new Interpolator();

    final Block bedrock = new Block(new NamespaceId("minecraft:bedrock"));
    final Block stone = new Block(new NamespaceId("minecraft:stone"));
    final Block water = new Block(new NamespaceId("minecraft:water"));

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
                        chunkData.setBlock(cx, y, cz, bedrock);
                    } else if (y > height && y <= getDimension().getFluidHeight()) {
                        chunkData.setBlock(cx, y, cz, water);
                    } else if (y <= height) {
                        if (y <= (height - 6)) {
                            chunkData.setBlock(cx, y, cz, stone);
                        } else {
                            chunkData.setBlock(cx, y, cz, new Block(biome.getSurfaceBlock()));
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
