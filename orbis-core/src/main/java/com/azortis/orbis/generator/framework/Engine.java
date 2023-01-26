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

package com.azortis.orbis.generator.framework;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.biome.BiomeLayout;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

@SuppressWarnings("ClassCanBeRecord")
public final class Engine {

    private final World world;

    private final Dimension dimension;

    public Engine(@NotNull World world, @NotNull Dimension dimension) {
        this.world = world;
        this.dimension = dimension;
    }

    public void generateChunk(@NotNull ChunkSnapshot chunkSnapshot, @NotNull WorldSnapshot worldSnapshot) {
        final int chunkX = chunkSnapshot.chunkX();
        final int chunkZ = chunkSnapshot.chunkZ();

        // Populate the ChunkSnapshot with all the biomes sections.
        // This stage is *always* executed as the platform will already have read this data from
        // the distributor, and thus is already finalized.
        int sectionOriginX = chunkX << 2;
        int sectionOriginZ = chunkZ << 2;
        for (int csx = 0; csx <= 4; csx++) {
            int sx = csx + sectionOriginX;
            for (int csz = 0; csz <= 4; csz++) {
                int sz = csz + sectionOriginZ;
                chunkSnapshot.setSection(sx, sz, distributor().getSection(sx << 2, sz << 2));
                for (int sy = dimension.minHeight() >> 2; sy <= (dimension.maxHeight() >> 2); sy++) {
                    chunkSnapshot.setSection(sx, sy, sz, distributor().getSection(sx << 2, sy << 2, sz << 2));
                }
            }
        }

        // Create a random generator for chunk
        RandomGenerator random = RandomGeneratorFactory.of("Xoshiro256PlusPlus").create(world.getWorldInfo().seed());

        // Apply chunk stages sequentially
        dimension.chunkStages().forEach((chunkStage) -> chunkStage.apply(chunkSnapshot, random));

        // Schedule an async task for world stages.
        Orbis.getPlatform().scheduler().runTaskAsync(() -> {
            while (true) {
                if (chunkSnapshot.isFinished()) {
                    break;
                }
            }
            dimension.worldStages().forEach((worldStage -> worldStage.apply(chunkSnapshot, worldSnapshot, random)));
        });
    }

    public @NotNull World world() {
        return world;
    }

    public @NotNull Dimension dimension() {
        return dimension;
    }

    public @NotNull Distributor distributor() {
        return world.getDimension().distributor();
    }

    public @NotNull BiomeLayout biomeLayout() {
        return distributor().layout(); // TODO determine this from the configured stages in dimension
    }

}
