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
import com.azortis.orbis.util.Location;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PaperPlayer implements Player {
    private final org.bukkit.entity.Player handle;

    PaperPlayer(org.bukkit.entity.Player handle) {
        this.handle = handle;
    }

    @Override
    public @NotNull CompletableFuture<Boolean> teleport(@NotNull Location location) {
        return null;
    }

    @Override
    public @NotNull Location getLocation() {
        return null;
    }

    @Override
    public @NotNull WorldAccess getWorld() {
        return null;
    }

    @Override
    public @NotNull Audience audience() {
        return handle;
    }

    public org.bukkit.entity.Player handle() {
        return handle;
    }

}
