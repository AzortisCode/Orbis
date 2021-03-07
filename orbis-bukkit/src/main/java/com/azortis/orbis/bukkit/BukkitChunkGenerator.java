/*
 * MIT License
 *
 * Copyright (c) 2021 Azortis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.azortis.orbis.bukkit;

import com.azortis.orbis.pack.Pack;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BukkitChunkGenerator extends ChunkGenerator {

    private final OrbisPlugin plugin;
    private final String worldName;
    private final Pack pack;

    private boolean loaded = false;
    private OrbisWorld orbisWorld;

    public BukkitChunkGenerator(OrbisPlugin plugin, String worldName, Pack pack) {
        this.plugin = plugin;
        this.worldName = worldName;
        this.pack = pack;
    }

    // If the world got initialized through the normal Bukkit configs then the ChunkGenerator gets made before the world
    // can be registered with orbis, so this is our bypass
    private void load(World world){
        orbisWorld = plugin.getWorld(worldName);
        if(orbisWorld != null){
            if(orbisWorld.isLoaded())orbisWorld.load();
        } else {
            orbisWorld = plugin.loadWorld(world, pack);
        }
        loaded = true;
    }

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
        if(!loaded) load(world);

        return super.generateChunkData(world, random, chunkX, chunkZ, biome);
    }

}
