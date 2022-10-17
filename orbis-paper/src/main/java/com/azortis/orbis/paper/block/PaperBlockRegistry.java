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

package com.azortis.orbis.paper.block;

import com.azortis.orbis.block.Block;
import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.block.IBlockRegistry;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Registry;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The PaperMC platform implementation of {@link IBlockRegistry}
 *
 * @since 0.3-Alpha
 * @author Jake Nijssen
 */
@API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.paper")
public final class PaperBlockRegistry implements IBlockRegistry {

    private final ImmutableMap<Key, Block> keyBlockMap;
    private final Int2ObjectMap<Block> idBlockMap = new Int2ObjectArrayMap<>();
    private final Int2ObjectMap<BlockState> idStateMap = new Int2ObjectArrayMap<>();

    /**
     * Initializes the Paper internal block registry.
     */
    @SuppressWarnings("PatternValidation")
    public PaperBlockRegistry(){
        ImmutableMap.Builder<Key, Block> builder = ImmutableMap.builder();
        for (net.minecraft.world.level.block.Block nativeBlock : Registry.BLOCK) {
            Block block = new PaperBlock(nativeBlock);
            builder.put(Key.key(Registry.BLOCK.getKey(nativeBlock).toString()), block);
            idBlockMap.put(block.id(), block);

            for (BlockState state : block.states()) {
                idStateMap.put(state.stateId(), state);
            }
        }
        keyBlockMap = builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImmutableSet<Key> blockKeys() {
        return keyBlockMap.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImmutableSet<Block> blocks() {
        return ImmutableSet.copyOf(keyBlockMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImmutableSet<BlockState> states() {
        return ImmutableSet.copyOf(idStateMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(@NotNull Key key) {
        return keyBlockMap.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Block fromKey(@NotNull Key key) {
        Preconditions.checkArgument(keyBlockMap.containsKey(key), "Invalid block key!");
        return Objects.requireNonNull(keyBlockMap.get(key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Block fromId(int id) {
        Preconditions.checkArgument(idBlockMap.containsKey(id), "Invalid block id!");
        return idBlockMap.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull BlockState fromStateId(int stateId) {
        Preconditions.checkArgument(idStateMap.containsKey(stateId), "Invalid blockState id!");
        return idStateMap.get(stateId);
    }
}
