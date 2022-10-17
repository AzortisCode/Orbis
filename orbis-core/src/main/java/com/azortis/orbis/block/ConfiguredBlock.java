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

package com.azortis.orbis.block;

import net.kyori.adventure.key.Keyed;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a captured block(state) configured in a dimension object json datafile.
 * This is (de)serialized by {@link com.azortis.orbis.pack.adapter.BlockAdapter}
 *
 * @since 0.3-Alpha
 * @author Jake Nijssen
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public sealed interface ConfiguredBlock extends Keyed permits Block, BlockState {

    /**
     * <p>Gets the internal id of the platform for the block.</p>
     * <p><b>NOTE</b> this id should never be used for persistence purposes, use {@link ConfiguredBlock#key()} instead!</p>
     *
     * @return The platform internal blockId for this block.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    int id();

    /**
     * Get the captured {@link Block} for this configured block.
     *
     * @return The captured block.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull Block block();

    /**
     * <p>Gets the internal stateId of the platform for the blockState.</p>
     * <p><b>NOTE</b> this id should never be used for persistence purposes, use {@link BlockState#values()} instead!</p>
     *
     * @return The platform internal blockId for this blockState.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    int stateId();

    /**
     * Gets the captured {@link BlockState} for this configured block.
     * This will be {@link Block#defaultState()} when the instance backing this interface is a {@link Block}.
     *
     * @return The captured blockState.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull BlockState state();

}
