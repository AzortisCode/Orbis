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

import com.azortis.orbis.block.property.Property;
import com.azortis.orbis.item.Item;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class Block implements ConfiguredBlock {

    private final Key key;
    private final int id;
    private final ImmutableSet<Property<?>> properties;
    private final ImmutableMap<String, Property<?>> propertyMap;
    private final Key itemKey;

    // Populated in BlockRegistry
    private BlockState defaultState;
    private ImmutableSet<BlockState> states;

    Block(@NotNull Key key, int id, @NotNull ImmutableSet<Property<?>> properties, @Nullable Key itemKey) {
        this.key = key;
        this.id = id;
        this.properties = properties;

        // Build the property map
        ImmutableMap.Builder<String, Property<?>> builder = ImmutableMap.builder();
        for (Property<?> property : properties) {
            builder.put(property.key(), property);
        }
        this.propertyMap = builder.build();
        this.itemKey = itemKey;
    }

    public static Block fromKey(@NotNull String key) {
        return BlockRegistry.fromKey(key);
    }

    public static Block fromKey(@NotNull Key key) {
        return BlockRegistry.fromKey(key);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public int id() {
        return id;
    }

    public @NotNull ImmutableSet<Property<?>> properties() {
        return properties;
    }

    public @NotNull ImmutableMap<String, Property<?>> propertyMap() {
        return propertyMap;
    }

    public boolean hasProperty(Property<?> property) {
        return properties.contains(property);
    }

    void setDefaultState(BlockState state) {
        if (defaultState == null) defaultState = state;
    }

    public @NotNull BlockState defaultState() {
        return defaultState;
    }

    void setStates(ImmutableSet<BlockState> states) {
        if (this.states == null) this.states = states;
    }

    public ImmutableSet<BlockState> states() {
        return states;
    }

    public @NotNull BlockState withProperties(@NotNull Map<String, String> properties) {
        BlockState state = this.defaultState;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            state = state.setValue(propertyMap.get(entry.getKey()), entry.getValue());
        }
        return state;
    }

    @Override
    public int stateId() {
        return defaultState.stateId();
    }

    @Override
    public Block block() {
        return this;
    }

    @Override
    public BlockState blockState() {
        return defaultState;
    }

    public boolean hasItem() {
        return itemKey != null;
    }

    public @Nullable Key itemKey() {
        return itemKey;
    }

    public @Nullable Item item() {
        return itemKey == null ? null : Item.fromKey(itemKey);
    }

}
