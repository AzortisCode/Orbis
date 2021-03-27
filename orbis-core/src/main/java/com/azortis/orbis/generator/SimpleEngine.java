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
import com.azortis.orbis.generator.biome.*;
import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.azortis.orbis.generator.noise.PerlinNoise;
import com.azortis.orbis.util.NamespaceId;

import java.util.Objects;

public class SimpleEngine extends Engine {

    private final Distributor distributor;
    private final ScatteredBiomeBlender biomeBlender;
    private final NoiseGenerator noiseGenerator;

    public SimpleEngine(Container container) {
        super(container);
        this.distributor = Objects.requireNonNull(Orbis.getGeneratorRegistry(Distributor.class))
                .loadType(super.getContainer(), container.getDimension().getDistributor());
        this.biomeBlender = new ScatteredBiomeBlender(0.04, 24);
        this.noiseGenerator = new PerlinNoise(container.getDimension().getSeed());
    }

    @Override
    public void generateChunkData(ChunkData chunkData, BiomeGrid biomeGrid, int chunkX, int chunkZ) {
        //Orbis.getLogger().info("Generating chunk at x={} z={}", chunkX, chunkZ);
        LinkedBiomeWeightMap biomeWeightMap = biomeBlender.getBlendForChunk(super.getContainer().getDimension().getSeed(),
                chunkX, chunkZ, distributor::getBiomeAt);
        Block bedrock = new Block(new NamespaceId("minecraft:bedrock"));
        Block stone = new Block(new NamespaceId("minecraft:stone"));
        Block water = new Block(new NamespaceId("minecraft:water"));
        int fluidHeight = getContainer().getDimension().getFluidHeight();
        for (int cx = 0; cx < 16; cx++) {
            for (int cz = 0; cz < 16; cz++) {
                final int x = cx + (chunkX << 4);
                final int z = cz + (chunkZ << 4);
                Biome pBiome = null;
                double pWeight = 0.0d;
                double height = 0;
                for (LinkedBiomeWeightMap entry = biomeWeightMap; entry.getNext() != null; entry = entry.getNext()){
                    double weight = entry.getWeights()[cz * 16 + cx];
                    Biome biome = distributor.getBiome(entry.getBiome());
                    if(pBiome == null){
                        pBiome = biome;
                        pWeight = weight;
                    } else if(weight > pWeight){
                        pBiome = biome;
                        pWeight = weight;
                    }
                    height += biome.getTerrain().getTerrainHeight(x, z, weight, noiseGenerator) * weight;
                }
                if(pBiome == null){
                    Orbis.getLogger().error("Biome is null fuuuuck {} {}", chunkX, chunkZ);
                }
                int finalHeight = (int)Math.round(height);
                assert pBiome != null;
                for (int y = 0; y <= getContainer().getDimension().getMaxHeight(); y++) {
                    biomeGrid.setBiome(cx, y, cz, pBiome);
                    if(y > finalHeight && y <= fluidHeight){
                        chunkData.setBlock(x, y, z, water);
                    } else if(y==0){
                        chunkData.setBlock(cx, y, cz, bedrock);
                    } else if(y <= (finalHeight - 7)){
                        chunkData.setBlock(cx, y, cz, stone);
                    } else if(y <= finalHeight) {
                        chunkData.setBlock(cx, y, cz, new Block(pBiome.getSurfaceBlock()));
                    }
                }
            }
        }
    }

}
