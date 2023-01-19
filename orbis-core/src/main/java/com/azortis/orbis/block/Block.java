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

import com.azortis.orbis.block.property.Property;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.kyori.adventure.key.Key;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A representation of a Block that contains a {@link BlockState} for each property/value combination.
 * In order to get an instance of a block refer to {@link Blocks} or {@link BlockRegistry}
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public non-sealed interface Block extends ConfiguredBlock {

    /**
     * Gets a block from specified {@link Key}.
     *
     * @param key The block resource key.
     * @return The corresponding Block.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    static @NotNull Block fromKey(@NotNull Key key) {
        return BlockRegistry.fromKey(key);
    }

    /**
     * Gets a block from specified key string.
     *
     * @param key The block resource key string.
     * @return The corresponding Block.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    static @NotNull Block fromKey(@NotNull String key) {
        return BlockRegistry.fromKey(key);
    }

    /**
     * Get the properties this block has.
     *
     * @return Immutable set view of the block properties.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull
    ImmutableSet<Property<?>> properties();

    /**
     * Checks if the {@link Block} has said property that can be modified.
     *
     * @param property The {@link Property} to check.
     * @return If the block contains this property.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean hasProperty(@NotNull Property<?> property);

    /**
     * Get an immutable view of property names and their {@link Property}.
     *
     * @return An immutable propertyName/Property view.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull
    ImmutableMap<String, Property<?>> propertyMap();

    /**
     * Get the default {@link BlockState} of this block.
     *
     * @return The default state of this block.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull
    BlockState defaultState();

    /**
     * Get an immutable view of the possible {@link BlockState}'s.
     * This set always contains at least one state.
     *
     * @return Immutable view of the possible states.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull
    ImmutableSet<BlockState> states();

    /**
     * Get the {@link BlockState} of this block for the specified properties.
     *
     * @param properties The properties of the state.
     * @return The BlockState of given properties.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull
    BlockState with(@NotNull Map<String, String> properties);

    /**
     * Check if this block also contains a {@link com.azortis.orbis.block.entity.BlockEntity}
     *
     * @return If the block has an entity.
     * @since 0.3-Alpha
     */
    @API(status = API.Status.EXPERIMENTAL, since = "0.3-Alpha")
    @Contract(pure = true)
    boolean hasEntity();

}
