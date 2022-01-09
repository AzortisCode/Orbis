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
import com.azortis.orbis.pack.Pack;
import com.azortis.orbis.paper.generator.PaperChunkGenerator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OrbisPlugin extends JavaPlugin {

    private final Map<String, PaperWorld> worldMap = new HashMap<>();
    private Metrics metrics;

    @Override
    public void onLoad() {
        this.metrics = new Metrics(this, 10874);
        Orbis.initialize(new PaperPlatform(this));
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String packName) {
        if (packName != null) {
            Pack pack = Orbis.getPackManager().getPack(packName);
            if (pack != null) {
                return new PaperChunkGenerator(this, worldName, pack);
            } else {
                Orbis.getLogger().error("No pack by the fieldName {} exists!", packName);
                return super.getDefaultWorldGenerator(worldName, packName);
            }
        }
        Orbis.getLogger().error("No pack fieldName specified for world {}", worldName);
        return super.getDefaultWorldGenerator(worldName, null);
    }

    @NotNull
    public PaperWorld loadWorld(@NotNull WorldInfo worldInfo, @NotNull Pack pack) {
        PaperWorld paperWorld = new PaperWorld(worldInfo);
        paperWorld.installPack(pack, false);
        if (!paperWorld.isLoaded()) paperWorld.load(worldInfo.getSeed());
        worldMap.put(worldInfo.getName(), paperWorld);
        return paperWorld;
    }

    @Nullable
    public PaperWorld getWorld(WorldInfo worldInfo) {
        return worldMap.get(worldInfo.getName());
    }

    @Nullable
    public PaperWorld getWorld(String worldName) {
        return worldMap.get(worldName);
    }

    public Collection<PaperWorld> getWorlds() {
        return worldMap.values();
    }

}
