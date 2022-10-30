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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import net.kyori.adventure.key.Key;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A registry responsible for retrieving {@link Block} and {@link BlockState} from the platform,
 * internally using {@link IBlockRegistry}. This class is also used for creating {@link com.azortis.orbis.block.entity.BlockEntity}'s
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public final class BlockRegistry {

    private static IBlockRegistry REGISTRY = null;

    private BlockRegistry() {
    }

    /**
     * Sets the internal {@link IBlockRegistry} for this registry to delegate to.
     *
     * @param registry The platform implementation of the internal registry.
     * @since 0.3-Alpha
     */
    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = {"com.azortis.orbis.paper"})
    public static void init(@NotNull IBlockRegistry registry) {
        if (REGISTRY == null) {
            REGISTRY = registry;
        }
    }

    /**
     * Returns an immutable set of all the possible block {@link Key}'s.
     *
     * @return Immutable set of block keys.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    public static @NotNull ImmutableSet<Key> blockKeys() {
        return REGISTRY.blockKeys();
    }

    /**
     * Returns an immutable set view of all the {@link Block} on the platform.
     *
     * @return Immutable set view of all the blocks.
     * @since 0.3-Alpha
     */
    @Contract(" -> new")
    public static @NotNull ImmutableSet<Block> blocks() {
        return REGISTRY.blocks();
    }

    /**
     * Returns an immutable set view of all the {@link BlockState} on the platform.
     *
     * @return Immutable set view of all the blockStates.
     * @since 0.3-Alpha
     */
    @Contract(" -> new")
    public static @NotNull ImmutableSet<BlockState> states() {
        return REGISTRY.states();
    }

    @Contract(pure = true)
    public static boolean containsKey(@NotNull Key key) {
        return REGISTRY.containsKey(key);
    }

    @Contract(pure = true)
    public static @NotNull Block fromKey(@NotNull Key key) throws IllegalArgumentException {
        Preconditions.checkArgument(REGISTRY.containsKey(key), "Invalid block key!");
        return REGISTRY.fromKey(key);
    }

    @Contract(pure = true)
    @SuppressWarnings("PatternValidation")
    public static boolean containsKey(@NotNull String key) throws IllegalArgumentException {
        Preconditions.checkArgument(key.matches("([a-z0-9_\\-.]+:)?[a-z0-9_\\-./]+"), "Invalid key string!");
        return REGISTRY.containsKey(Key.key(key));
    }

    @Contract(pure = true)
    @SuppressWarnings("PatternValidation")
    public static @NotNull Block fromKey(@NotNull String key) throws IllegalArgumentException {
        Preconditions.checkArgument(key.matches("([a-z0-9_\\-.]+:)?[a-z0-9_\\-./]+"), "Invalid key string!");
        return fromKey(Key.key(key));
    }

    @Contract(pure = true)
    public static @NotNull Block fromId(final int id) throws IllegalArgumentException {
        // TODO add check if id exists.
        return REGISTRY.fromId(id);
    }

    @Contract(pure = true)
    public static @NotNull BlockState fromStateId(final int stateId) throws IllegalArgumentException {
        // TODO add check if state id exists.
        return REGISTRY.fromStateId(stateId);
    }

}
