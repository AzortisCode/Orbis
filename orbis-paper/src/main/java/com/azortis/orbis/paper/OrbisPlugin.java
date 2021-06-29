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

package com.azortis.orbis.paper;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.paper.generator.PaperChunkGenerator;
import com.azortis.orbis.pack.Pack;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OrbisPlugin extends JavaPlugin {

    private final Map<String, OrbisWorld> worldMap = new HashMap<>();
    private Metrics metrics;

    @Override
    public void onLoad() {
        this.metrics = new Metrics(this, 10874);
        Orbis.initialize(new PaperPlatform(this));
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String packName) {
        if(packName != null){
            Pack pack = Orbis.getPackManager().getPack(packName);
            if(pack != null){
                return new PaperChunkGenerator(this, worldName, pack);
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
        if(!orbisWorld.isLoaded()) orbisWorld.load();
        worldMap.put(world.getName(), orbisWorld);
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

    public Collection<OrbisWorld> getWorlds(){
        return worldMap.values();
    }

}
