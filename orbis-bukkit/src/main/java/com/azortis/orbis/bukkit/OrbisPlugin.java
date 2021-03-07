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

import com.azortis.orbis.Orbis;
import com.azortis.orbis.bukkit.adapter.BukkitAdapter;
import com.azortis.orbis.pack.Pack;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class OrbisPlugin extends JavaPlugin {
    private final Map<String, OrbisWorld> worldMap = new HashMap<>();

    @Override
    public void onLoad() {
        Orbis.initialize(new BukkitAdapter(this));
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String packName) {
        if(packName != null){
            Pack pack = Orbis.getPackManager().getPack(packName);
            if(pack != null){
                return new BukkitChunkGenerator(this, worldName, pack);
            } else {
                Orbis.getLogger().error("No pack by the name {} exists!", packName);
                return super.getDefaultWorldGenerator(worldName, packName);
            }
        }
        Orbis.getLogger().error("No pack name specified for world {}", worldName);
        return super.getDefaultWorldGenerator(worldName, null);
    }

    @NotNull
    public OrbisWorld loadWorld(@NotNull World world, @NotNull Pack pack){
        OrbisWorld orbisWorld = new OrbisWorld(world);
        orbisWorld.installPack(pack, false);
        if(!orbisWorld.isLoaded())orbisWorld.load();
        worldMap.put(world.getName(), orbisWorld);
        Orbis.registerContainer(orbisWorld);
        return orbisWorld;
    }

    @Nullable
    public OrbisWorld getWorld(World world){
        return worldMap.get(world.getName());
    }

    @Nullable
    public OrbisWorld getWorld(String worldName){
        return worldMap.get(worldName);
    }

}
