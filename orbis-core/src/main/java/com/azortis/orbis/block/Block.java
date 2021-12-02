/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2021  Azortis
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
import com.azortis.orbis.utils.NamespaceId;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class Block {

    private final NamespaceId key;
    private final int id;
    private final ImmutableSet<Property<?>> properties;
    private final ImmutableMap<String, Property<?>> propertyMap;

    // Populated in BlockRegistry
    private BlockState defaultState;
    private ImmutableSet<BlockState> states;

    Block(NamespaceId key, int id, @NotNull ImmutableSet<Property<?>> properties) {
        this.key = key;
        this.id = id;
        this.properties = properties;

        // Build the property map
        ImmutableMap.Builder<String, Property<?>> builder = ImmutableMap.builder();
        for (Property<?> property : properties){
            builder.put(property.getKey(), property);
        }
        this.propertyMap = builder.build();
    }

    public @NotNull NamespaceId getKey() {
        return key;
    }

    public int getId() {
        return id;
    }

    public @NotNull ImmutableSet<Property<?>> getProperties() {
        return properties;
    }

    public @NotNull ImmutableMap<String, Property<?>> getPropertyMap() {
        return propertyMap;
    }

    public boolean hasProperty(Property<?> property){
        return properties.contains(property);
    }

    void setDefaultState(BlockState state){
        if(defaultState == null)defaultState = state;
    }

    public @NotNull BlockState getDefaultState() {
        return defaultState;
    }

    void setStates(ImmutableSet<BlockState> states){
        if(this.states == null)this.states = states;
    }

    public ImmutableSet<BlockState> getStates() {
        return states;
    }

    public @NotNull BlockState withProperties(Map<String, String> properties){
        BlockState state = this.defaultState;
        for (Map.Entry<String, String> entry : properties.entrySet()){
            state = state.setValue(propertyMap.get(entry.getKey()), entry.getValue());
        }
        return state;
    }

}
