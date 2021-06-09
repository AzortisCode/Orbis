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

package com.azortis.orbis.bukkit.generator;

import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.biome.BiomeGrid;
import org.bukkit.generator.ChunkGenerator;

import java.util.Locale;

public class BukkitBiomeGrid implements BiomeGrid {

    private final ChunkGenerator.BiomeGrid handle;

    public BukkitBiomeGrid(ChunkGenerator.BiomeGrid handle) {
        this.handle = handle;
    }

    @Override
    public void setBiome(int x, int y, int z, Biome biome) {
        handle.setBiome(x, y, z, org.bukkit.block.Biome.valueOf(biome.getDerivative().getId().toUpperCase(Locale.ROOT)));
    }
}
