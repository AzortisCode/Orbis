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

package com.azortis.orbis.world;

import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.generator.framework.ChunkSnapshot;
import com.azortis.orbis.generator.framework.WorldStage;
import com.azortis.orbis.util.annotations.RelativeCoords;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * A map that provides context for the height of a certain property during generation.
 * Implementations of this can vary, platforms should always provide {@link Heightmap#WG_SURFACE}
 * and {@link Heightmap#WG_OCEAN_FLOOR}. To add a heightmap refer to {@link ChunkSnapshot#addHeightMap(Key, Heightmap)}.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
public interface Heightmap {

    /**
     * The default provided heightmap for the surface of a world.
     */
    Key WG_SURFACE = Key.key("orbis:wg_surface");

    /**
     * The default provided heightmap for the ocean floor of a world.
     */
    Key WG_OCEAN_FLOOR = Key.key("orbis:wg_ocean_floor");

    @NotNull
    Key type();

    /**
     * If the platform should persist this heightmap after chunk generation, so it can be used in {@link WorldStage}'s.
     *
     * @return If this heightmap is saved to the disk.
     */
    boolean isPersistent();

    int chunkX();

    int chunkZ();

    /**
     * Get the first height coordinate for this map where a {@link BlockState#isAir()} block is present.
     *
     * @param x The relative x-coordinate.
     * @param z The relative z-coordinate.
     * @return The first {@link BlockState#isAir()} available.
     */
    @RelativeCoords
    int getFirstAvailable(@Range(from = 0, to = 15) int x, @Range(from = 0, to = 15) int z);

    @RelativeCoords
    int getHighestTaken(@Range(from = 0, to = 15) int x, @Range(from = 0, to = 15) int z);

}
