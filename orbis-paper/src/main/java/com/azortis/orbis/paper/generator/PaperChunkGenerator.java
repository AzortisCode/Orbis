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

import com.azortis.orbis.paper.OrbisPlugin;
import com.azortis.orbis.paper.OrbisWorld;
import com.azortis.orbis.pack.Pack;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class PaperChunkGenerator extends ChunkGenerator {

    private final OrbisPlugin plugin;
    private final String worldName;
    private final Pack pack;

    private volatile boolean loaded = false;
    private OrbisWorld orbisWorld;

    public PaperChunkGenerator(OrbisPlugin plugin, String worldName, Pack pack) {
        this.plugin = plugin;
        this.worldName = worldName;
        this.pack = pack;
    }

    // If the world got initialized through the normal Bukkit configs then the ChunkGenerator gets made before the world
    // can be registered with orbis, so this is our bypass
    private synchronized void load(World world){
        if(!loaded) {
            orbisWorld = plugin.getWorld(worldName);
            if (orbisWorld != null) {
                if (orbisWorld.isLoaded()) orbisWorld.load();
            } else {
                orbisWorld = plugin.loadWorld(world, pack);
            }
            loaded = true;
        }
    }

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biomeGrid) {
        if(!loaded) load(world);
        PaperChunkData chunkData = new PaperChunkData(orbisWorld.getDimension(), createChunkData(world));
        orbisWorld.getEngine().generateChunkData(chunkData, new PaperBiomeGrid(biomeGrid), chunkX, chunkZ);
        return chunkData.getHandle();
    }

    @Override
    public boolean isParallelCapable() {
        return true;
    }
}
