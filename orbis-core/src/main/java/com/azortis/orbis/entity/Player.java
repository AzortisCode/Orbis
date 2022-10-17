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

package com.azortis.orbis.entity;

import com.azortis.orbis.command.CommandSender;
import com.azortis.orbis.util.Location;
import com.azortis.orbis.world.WorldAccess;
import net.kyori.adventure.text.Component;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * A representation of a Player on the server.
 *
 * @since 0.3-Alpha
 * @author Jake Nijssen
 */
@API(status = API.Status.EXPERIMENTAL, since = "0.3-Alpha")
public interface Player extends CommandSender {

    /**
     * Check if player has the given permission.
     *
     * @param permission The permission to check.
     * @return If the player has that permission.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean hasPermission(@NotNull String permission);

    /**
     * Kick the player of the server.
     *
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    void kick();

    /**
     * Kick the player of the server with a reason.
     *
     * @param component Reasoning message.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    void kick(@NotNull Component component);

    /**
     * Teleports a player synchronously to provided location.
     *
     * @param location The location to teleport the player to.
     * @return If the teleport went successfully.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean teleport(@NotNull Location location);

    /**
     * Teleports a player asynchronously to provided location.
     *
     * @param location The location to teleport the player to.
     * @return A CompletableFuture boolean that marks success.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    @NotNull CompletableFuture<Boolean> teleportAsync(@NotNull Location location);

    /**
     * Get the current {@link GameMode} pf the player.
     *
     * @return Current player game mode.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull GameMode getGameMode();

    /**
     * Sets the player their game mode.
     *
     * @param gameMode The game mode to set the player in.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    void setGameMode(@NotNull GameMode gameMode);

    /**
     * Checks if the player can fly.
     *
     * @return If the player can fly.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean canFly();

    /**
     * Sets if the player is able to fly.
     *
     * @param allowFlying If the player should be able to fly.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    void setAllowFlying(boolean allowFlying);

    /**
     * Get the current {@link Location} of the player.
     *
     * @return Current player location
     * @since 0.3-Alpha
     */
    @Contract(" -> new")
    @NotNull Location getLocation();

    /**
     * Get the {@link WorldAccess} instance of the world the player currently resides in.
     *
     * @return The current world the player is in.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull WorldAccess getWorld();

    /**
     * Represents the 4 game mode states the player can be in.
     *
     * @since 0.3-Alpha
     * @author Jake Nijssen
     */
    enum GameMode {
        /**
         * GameMode where player cannot take any damage, break blocks instantly & has unlimited resources.
         */
        CREATIVE,

        /**
         * GameMode where the player can take damage and has to survive.
         */
        SURVIVAL,

        /**
         * Like {@link GameMode#SURVIVAL} but then with the limitation of not being able to break
         * certain blocks.
         */
        ADVENTURE,

        /**
         * GameMode where the player is invisible to the rest of the server, and can fly & clip around freely.
         */
        SPECTATOR;
    }

}
