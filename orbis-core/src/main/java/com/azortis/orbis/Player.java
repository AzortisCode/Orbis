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

package com.azortis.orbis;

import com.azortis.orbis.command.CommandSender;
import com.azortis.orbis.util.Location;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface Player extends CommandSender {

    /**
     * Teleports a player asynchronously to provided location.
     *
     * @param location The location to teleport the player to.
     * @return A CompletableFuture boolean that marks success.
     */
    @NotNull CompletableFuture<Boolean> teleport(@NotNull Location location);

    /**
     * Get the current {@link Location} of the player.
     *
     * @return Current player location
     */
    @NotNull Location getLocation();

    /**
     * Get the {@link WorldAccess} instance of the world the player currently resides in.
     *
     * @return The current world the player is in.
     */
    @NotNull WorldAccess getWorld();

}
