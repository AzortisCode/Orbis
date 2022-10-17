/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2022 Azortis
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

package com.azortis.orbis.paper.studio;

import com.azortis.orbis.pack.studio.Project;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class PaperStudioBiomeProvider extends BiomeProvider {

    private final PaperStudioChunkGenerator chunkGenerator;
    private final Project project;

    public PaperStudioBiomeProvider(PaperStudioChunkGenerator chunkGenerator, Project project) {
        this.chunkGenerator = chunkGenerator;
        this.project = project;
    }

    @Override
    public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
        if (chunkGenerator.requiresLoading()) chunkGenerator.load(worldInfo);
        if (!project.studioWorld().shouldRender()) return Biome.PLAINS;
        return Biome.valueOf(project.studioWorld().getDimension().distributor()
                .getBiome(x, y, z).derivative().value().toUpperCase(Locale.ROOT));
    }

    @Override
    public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
        if (chunkGenerator.requiresLoading()) chunkGenerator.load(worldInfo);
        if (!project.studioWorld().shouldRender()) return Collections.singletonList(Biome.PLAINS);
        return project.studioWorld().getDimension().distributor().biomes()
                .stream().map(com.azortis.orbis.generator.biome.Biome::derivative).map(Key::value)
                .map(String::toUpperCase).map(Biome::valueOf).collect(Collectors.toList());
    }

}
