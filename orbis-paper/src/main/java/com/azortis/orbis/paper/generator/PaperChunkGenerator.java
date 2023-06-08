/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2023 Azortis
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

package com.azortis.orbis.paper.generator;

import com.azortis.orbis.paper.world.PaperWorld;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class PaperChunkGenerator extends ChunkGenerator {

    private final PaperWorld world;
    private final ChunkGenerator delegate;

    public PaperChunkGenerator(@NotNull BiomeSource biomeSource, @NotNull PaperWorld world,
                               @NotNull ChunkGenerator delegate) {
        super(biomeSource);
        this.world = world;
        this.delegate = delegate;
    }

    @Override
    public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(@NotNull Executor executor, @NotNull Blender blender,
                                                                 @NotNull RandomState noiseConfig,
                                                                 @NotNull StructureManager structureAccessor,
                                                                 @NotNull ChunkAccess chunk) {
        return null;
    }

    @Override
    public void applyBiomeDecoration(@NotNull WorldGenLevel world, @NotNull ChunkAccess chunk,
                                     @NotNull StructureManager structureAccessor) {

    }

    @Override
    public void spawnOriginalMobs(@NotNull WorldGenRegion region) {
        this.delegate.spawnOriginalMobs(region);
    }

    @Override
    public void applyCarvers(@NotNull WorldGenRegion chunkRegion, long seed, @NotNull RandomState noiseConfig,
                             @NotNull BiomeManager biomeAccess, @NotNull StructureManager structureAccessor,
                             @NotNull ChunkAccess chunk, GenerationStep.@NotNull Carving carverStep) {
        // no-op
    }

    @Override
    public void buildSurface(@NotNull WorldGenRegion region, @NotNull StructureManager structures,
                             @NotNull RandomState noiseConfig, @NotNull ChunkAccess chunk) {
        // no-op
    }

    @Override
    public int getGenDepth() {
        return delegate.getGenDepth();
    }

    @Override
    public int getSeaLevel() {
        return delegate.getSeaLevel();
    }

    @Override
    public int getMinY() {
        return world.minHeight();
    }

    @Override
    public int getBaseHeight(int x, int z, @NotNull Heightmap.Types heightmap, @NotNull LevelHeightAccessor world,
                             @NotNull RandomState noiseConfig) {
        return this.delegate.getBaseHeight(x, z, heightmap, world, noiseConfig);
    }

    @Override
    public @NotNull NoiseColumn getBaseColumn(int x, int z, @NotNull LevelHeightAccessor world,
                                              @NotNull RandomState noiseConfig) {
        return delegate.getBaseColumn(x, z, world, noiseConfig);
    }

    @Override
    public void addDebugScreenInfo(@NotNull List<String> text, @NotNull RandomState noiseConfig, @NotNull BlockPos pos) {
        this.delegate.addDebugScreenInfo(text, noiseConfig, pos);
    }

    @Override
    protected @NotNull Codec<? extends ChunkGenerator> codec() {
        return Codec.unit(null);
    }

}
