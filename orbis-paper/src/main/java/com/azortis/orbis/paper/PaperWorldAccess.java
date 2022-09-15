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
import com.azortis.orbis.WorldAccess;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class PaperWorldAccess implements WorldAccess {

    private static final PaperPlatform platform = OrbisPlugin.getPlatform();
    private final World handle;

    public PaperWorldAccess(World handle) {
        this.handle = handle;
    }

    public World handle() {
        return handle;
    }

    @Override
    public @NotNull String name() {
        return handle.getName();
    }

    @Override
    public boolean isWorldLoaded() {
        return Bukkit.getWorlds().contains(handle);
    }

    @Override
    public @NotNull Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();
        for (org.bukkit.entity.Player bukkitPlayer : handle.getPlayers()) {
            players.add(platform.getPlayer(bukkitPlayer.getUniqueId()));
        }
        return players;
    }
}
