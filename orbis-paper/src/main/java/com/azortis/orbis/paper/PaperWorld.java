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

package com.azortis.orbis.paper;

import com.azortis.orbis.Player;
import com.azortis.orbis.World;
import com.azortis.orbis.WorldAccess;
import org.bukkit.Bukkit;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

public class PaperWorld extends World {

    private final WorldInfo worldInfo;
    private WorldAccess worldAccess;

    public PaperWorld(@NotNull WorldInfo worldInfo) {
        super(worldInfo.getName(), new File(Bukkit.getWorldContainer() + "/" + worldInfo.getName() + "/"));
        this.worldInfo = worldInfo;
    }

    public WorldInfo getNativeWorldInfo() {
        return worldInfo;
    }

    void setWorldAccess(@NotNull WorldAccess worldAccess) {
        if (this.worldAccess == null) {
            this.worldAccess = worldAccess;
        } else throw new IllegalStateException("WorldAccess for " + worldInfo.getName() + " has already been set!");
    }

    //
    // WorldAccess
    //


    @Override
    public boolean isWorldLoaded() {
        return worldAccess.isWorldLoaded();
    }

    @Override
    public @NotNull Set<Player> getPlayers() {
        return worldAccess.getPlayers();
    }
}
