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

import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.azortis.orbis.generator.noise.PerlinNoise;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public abstract class Engine implements ChunkGenerator {

    private final NoiseGenerator noise = new PerlinNoise(174871285);
    private final Dimension dimension;
    private final int seaLevel = 130;
    private final int stoneHeight = 120;

    public Engine(Dimension dimension){
        this.dimension = dimension;
    }

    @Override
    public void generateChunkData(@NotNull ChunkBatch chunkBatch, int chunkX, int chunkZ) {
        for (int cx = 0; cx < Chunk.CHUNK_SIZE_X; cx++){
            for (int cz = 0; cz < Chunk.CHUNK_SIZE_Z; cz++) {
                final int x = cx + (chunkX << 4);
                final int z = cz + (chunkZ << 4);
                final int height = 100 + (int) Math.round(dimension.getTerrain().getTerrainHeight(x, z, 0, 0, noise));

                if(height >= seaLevel) {
                    for (int y = 0; y <= height; y++) {
                        if (y == 0) {
                            chunkBatch.setBlock(cx, y, cz, Block.BEDROCK);
                        }else if (y <= stoneHeight){
                            chunkBatch.setBlock(cx, y , cz, Block.STONE);
                        } else if (y < height){
                            chunkBatch.setBlock(cx, y, cz, Block.DIRT);
                        } else {
                            chunkBatch.setBlock(cx, y, cz, Block.GRASS_BLOCK);
                        }
                    }
                }else {
                    for (int y = 0; y <= seaLevel; y++){
                        if(y == 0){
                            chunkBatch.setBlock(cx, y, cz, Block.BEDROCK);
                        } else if(y <= height){
                            if(y <= stoneHeight){
                                chunkBatch.setBlock(cx, y, cz, Block.STONE);
                            }else{
                                chunkBatch.setBlock(cx, y, cz, Block.DIRT);
                            }
                        } else {
                            chunkBatch.setBlock(cx, y, cz, Block.WATER);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void fillBiomes(@NotNull Biome[] biomes, int x, int z) {
        Arrays.fill(biomes, Biome.PLAINS);
    }

    @Nullable
    @Override
    public List<ChunkPopulator> getPopulators() {
        return null;
    }

}
