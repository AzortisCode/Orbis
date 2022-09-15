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
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface Player extends CommandSender {

    /**
     * Check if player has the given permission.
     *
     * @param permission The permission to check.
     * @return If the player has that permission.
     */
    boolean hasPermission(@NotNull String permission);

    /**
     * Kick the player of the server.
     */
    void kick();

    /**
     * Kick the player of the server with a reason.
     *
     * @param component Reasoning message.
     */
    void kick(@NotNull Component component);

    /**
     * Teleports a player asynchronously to provided location.
     *
     * @param location The location to teleport the player to.
     * @return A CompletableFuture boolean that marks success.
     */
    @NotNull CompletableFuture<Boolean> teleport(@NotNull Location location);

    /**
     * Get the current {@link GameMode} pf the player.
     *
     * @return Current player game mode.
     */
    GameMode getGameMode();

    /**
     * Sets the player their game mode.
     *
     * @param gameMode The game mode to set the player in.
     */
    void setGameMode(@NotNull GameMode gameMode);

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

    /**
     * Represents the 4 game mode states the player can be in.
     */
    enum GameMode {
        CREATIVE,
        SURVIVAL,
        ADVENTURE,
        SPECTATOR;
    }

}
