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

package com.azortis.orbis.generator.biome;

import org.apiguardian.api.API;

/**
 * An enum that defines a biome layout of the dimension.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.EXPERIMENTAL, since = "0.3-Alpha")
public enum BiomeLayout {

    /**
     * A biome layout, where there only exists a surface, meaning that y-coordinates are not considered.
     * Just like the pre-1.18 overworld.
     */
    SURFACE,

    /**
     * A biome layout, where there is a primary surface, but biomes below or above the surface
     * may be different. For example cave or sky biomes. Just like the vanilla overworld.
     */
    HYBRID,

    /**
     * A biome layout, which doesn't have a surface and the y-coordinate is crucial for generation.
     * Just like the vanilla nether dimension.
     */
    FULL;

    /**
     * If the layout has a 2d biome map.
     *
     * @return If the layout is {@link BiomeLayout#SURFACE} or {@link BiomeLayout#HYBRID}.
     * @since 0.3-Alpha
     */
    public boolean hasBiomeMap() {
        return this == SURFACE || this == HYBRID;
    }

    /**
     * If the layout has a 3d biome sections.
     *
     * @return If the layout is {@link BiomeLayout#HYBRID} or {@link BiomeLayout#FULL}.
     * @since 0.3-Alpha
     */
    public boolean hasFullBiomes() {
        return this == HYBRID || this == FULL;
    }

}
