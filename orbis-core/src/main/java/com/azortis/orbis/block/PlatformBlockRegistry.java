/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2023 Azortis
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

package com.azortis.orbis.block;

import com.google.common.collect.ImmutableSet;
import net.kyori.adventure.key.Key;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = {"com.azortis.orbis.block",
        "com.azortis.orbis.paper.block"})
public interface PlatformBlockRegistry {

    /**
     * Returns an immutable set of all the possible block {@link Key}'s.
     *
     * @return Immutable set of block keys.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull
    ImmutableSet<Key> blockKeys();

    /**
     * Returns an immutable set view of all the {@link Block} on the platform.
     *
     * @return Immutable set view of all the blocks.
     * @since 0.3-Alpha
     */
    @Contract(" -> new")
    @NotNull
    ImmutableSet<Block> blocks();

    /**
     * Returns an immutable set view of all the {@link BlockState} on the platform.
     *
     * @return Immutable set view of all the blockStates.
     * @since 0.3-Alpha
     */
    @Contract(" -> new")
    @NotNull
    ImmutableSet<BlockState> states();

    @Contract(pure = true)
    boolean containsKey(@NotNull Key key);

    @Contract(pure = true)
    @NotNull
    Block fromKey(@NotNull Key key);

    @Contract(pure = true)
    @NotNull
    Block fromId(final int id);

    @Contract(pure = true)
    @NotNull
    BlockState fromStateId(final int stateId);

}
