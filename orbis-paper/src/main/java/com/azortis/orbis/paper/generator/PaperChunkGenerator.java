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

import com.azortis.orbis.pack.Pack;
import com.azortis.orbis.paper.OrbisPlugin;
import com.azortis.orbis.paper.world.PaperWorld;
import org.bukkit.craftbukkit.v1_19_R2.generator.CraftChunkData;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public final class PaperChunkGenerator extends ChunkGenerator {

    private final Pack pack;

    private boolean loaded = false;
    private PaperWorld paperWorld;

    public PaperChunkGenerator(Pack pack) {
        this.pack = pack;
    }

    // If the world got initialized through the normal Bukkit configs then the ChunkGenerator gets made before the world
    // can be registered with orbis, so this is our bypass
    synchronized void load(@NotNull WorldInfo worldInfo) {
        if (!loaded) {
            paperWorld = OrbisPlugin.getPlatform().getWorld(worldInfo);
            if (paperWorld != null) {
                if (paperWorld.isLoaded()) {
                    paperWorld.load(worldInfo.getSeed());
                }
            } else {
                paperWorld = OrbisPlugin.getPlatform().loadWorld(worldInfo, pack);
            }
            loaded = true;
        }
    }

    boolean requiresLoading() {
        return !loaded;
    }

    public PaperWorld getWorld() {
        return paperWorld;
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random ignored, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (requiresLoading()) load(worldInfo);
        PaperGeneratorChunkAccess chunkAccess = new PaperGeneratorChunkAccess(paperWorld, paperWorld.getDimension(),
                paperWorld.getEngine(), (CraftChunkData) chunkData);
        paperWorld.getEngine().generateChunk(chunkX, chunkZ, chunkAccess);
        chunkAccess.unload(); // Make sure to let the core API know this ChunkAccess instance shouldn't be used.
    }

    @Override
    public @NotNull BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new PaperBiomeProvider(this);
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

}
