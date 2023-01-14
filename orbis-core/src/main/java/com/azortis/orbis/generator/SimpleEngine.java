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

package com.azortis.orbis.generator;

import com.azortis.orbis.block.Blocks;
import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.world.ChunkAccess;
import com.azortis.orbis.world.World;
import org.jetbrains.annotations.NotNull;

public final class SimpleEngine extends Engine {

    public SimpleEngine(World world) {
        super(world);
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ, @NotNull ChunkAccess chunkAccess) {
        for (int cx = 0; cx <= 16; cx++) {
            for (int cz = 0; cz <= 16; cz++) {
                final int x = cx + (chunkX << 4);
                final int z = cz + (chunkZ << 4);

                Biome biome = dimension().distributor().getBiome(x, 0, z);
                int height = (int) Math.round(biome.surface().getTerrainHeight(x, z, 1.0d));

                for (int y = dimension().minHeight(); y < dimension().maxHeight(); y++) {
                    if (y == dimension().minHeight()) {
                        chunkAccess.setBlock(cx, y, cz, Blocks.BEDROCK);
                    } else {
                        if (y < height) {
                            chunkAccess.setBlock(cx, y, cz, biome.belowSurfaceBlock());
                        } else if (y == height) {
                            chunkAccess.setBlock(cx, y, cz, biome.surfaceBlock());
                        }
                    }
                }
            }
        }
    }
}
