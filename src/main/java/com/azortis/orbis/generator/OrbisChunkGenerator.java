package com.azortis.orbis.generator;

import com.azortis.orbis.noise.NoiseGenerator;
import com.azortis.orbis.noise.OpenSimplexNoise;
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

public class OrbisChunkGenerator implements ChunkGenerator {

    private final NoiseGenerator noise = new OpenSimplexNoise(1188179420);
    private final int seaLevel = 130;
    private final int baseHeight = 100;
    private final int stoneHeight = 120;

    private float getHeight(int x, int z){
        float height = noise.noise2(x / 800f, z / 800f) * 80;
        height += noise.noise2(x / 100f, z / 100f) * 10;
        height += noise.noise2(x / 20f, z / 20f) * 0.5f;
        return baseHeight + height;
    }

    @Override
    public void generateChunkData(@NotNull ChunkBatch chunkBatch, int chunkX, int chunkZ) {
        for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++){
            for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                final int height = Math.round(getHeight(x + chunkX>>4, z + chunkZ>>4));

                if(height >= seaLevel) {
                    for (int y = 0; y <= height; y++) {
                        if (y == 0) {
                            chunkBatch.setBlock(x, y, z, Block.BEDROCK);
                        }else if (y <= stoneHeight){
                            chunkBatch.setBlock(x, y , z, Block.STONE);
                        } else if (y < height){
                            chunkBatch.setBlock(x, y, z, Block.DIRT);
                        } else {
                            chunkBatch.setBlock(x, y, z, Block.GRASS_BLOCK);
                        }
                    }
                }else {
                    for (int y = 0; y <= seaLevel; y++){
                        if(y == 0){
                            chunkBatch.setBlock(x, y, z, Block.BEDROCK);
                        } else if(y <= height){
                            if(y <= stoneHeight){
                                chunkBatch.setBlock(x, y, z, Block.STONE);
                            }else{
                                chunkBatch.setBlock(x, y, z, Block.DIRT);
                            }
                        } else {
                            chunkBatch.setBlock(x, y, z, Block.WATER);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void fillBiomes(@NotNull Biome[] biomes, int i, int i1) {
        Arrays.fill(biomes, Biome.PLAINS);
    }

    @Nullable
    @Override
    public List<ChunkPopulator> getPopulators() {
        return null;
    }
}
