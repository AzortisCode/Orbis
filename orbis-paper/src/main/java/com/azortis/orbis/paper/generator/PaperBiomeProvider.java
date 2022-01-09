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

package com.azortis.orbis.paper.generator;

import net.kyori.adventure.key.Key;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PaperBiomeProvider extends BiomeProvider {

    private final PaperChunkGenerator chunkGenerator;

    public PaperBiomeProvider(PaperChunkGenerator chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
    }

    @Override
    public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
        if (chunkGenerator.requiresLoading()) chunkGenerator.load(worldInfo);
        return Biome.valueOf(chunkGenerator.getWorld().getDimension()
                .distributor().getNativeBiome(x, y, z).value().toUpperCase(Locale.ROOT));
    }

    @Override
    public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
        if (chunkGenerator.requiresLoading()) chunkGenerator.load(worldInfo);
        return chunkGenerator.getWorld().getDimension().distributor().getNativePossibleBiomes()
                .stream().map(Key::value).map(String::toUpperCase).map(Biome::valueOf).collect(Collectors.toList());
    }
}
