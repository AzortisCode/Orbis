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

package com.azortis.orbis.paper.studio;

import com.azortis.orbis.pack.studio.Project;
import com.azortis.orbis.paper.generator.PaperGeneratorChunkAccess;
import org.bukkit.craftbukkit.v1_19_R2.generator.CraftChunkData;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public final class PaperStudioChunkGenerator extends ChunkGenerator {

    private final Project project;

    public PaperStudioChunkGenerator(@NotNull Project project) {
        this.project = project;
    }

    synchronized void load(WorldInfo worldInfo) {
        if (requiresLoading()) {
            project.studioWorld().load(worldInfo.getSeed());
        }
    }

    boolean requiresLoading() {
        return !project.studioWorld().isLoaded();
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random ignored, int chunkX, int chunkZ,
                              @NotNull ChunkData chunkData) {
        if (requiresLoading()) load(worldInfo);
        if (project.studioWorld().shouldRender() && project.studioWorld().getDimension() != null
                && project.studioWorld().getEngine() != null) {
            PaperGeneratorChunkAccess chunkAccess = new PaperGeneratorChunkAccess(project.studioWorld(),
                    project.studioWorld().getDimension(), project.studioWorld().getEngine(), (CraftChunkData) chunkData);
            project.studioWorld().getEngine().generateChunk(chunkX, chunkZ, chunkAccess);
            chunkAccess.unload();
        }
    }

    @Override
    public @NotNull BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new PaperStudioBiomeProvider(this, project);
    }
}
