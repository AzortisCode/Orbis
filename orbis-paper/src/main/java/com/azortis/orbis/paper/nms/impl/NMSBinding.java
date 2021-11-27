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

package com.azortis.orbis.paper.nms.impl;

import com.azortis.orbis.block.property.BooleanProperty;
import com.azortis.orbis.block.property.EnumProperty;
import com.azortis.orbis.block.property.IntegerProperty;
import com.azortis.orbis.block.property.Property;
import com.azortis.orbis.paper.nms.INMSBinding;
import com.azortis.orbis.utils.Nameable;
import com.azortis.orbis.utils.NamespaceId;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NMSBinding implements INMSBinding {

    private static Block getBlockFromId(NamespaceId material) {
        return Registry.BLOCK.get(ResourceLocation.tryParse(material.getId()));
    }

    @Override
    public @Nullable Property<?> getProperty(NamespaceId material, String name) {
        return ConversionUtils.fromNative(getBlockFromId(material).getStateDefinition().getProperty(name));
    }

    @Override
    public Map<String, Property<?>> getProperties(NamespaceId material) {
        StateDefinition<Block, BlockState> state = getBlockFromId(material).getStateDefinition();
        Map<String, Property<?>> propertyMap = new HashMap<>();
        state.getProperties().forEach(nativeProperty -> {
            Property<?> property = ConversionUtils.fromNative(nativeProperty);
            propertyMap.put(property.getKey(), property);
        });
        return propertyMap;
    }

    @Override
    public boolean hasProperty(NamespaceId material, Property<?> property) {
        net.minecraft.world.level.block.state.properties.Property<?> nativeProperty = ConversionUtils.toNative(property);
        return getBlockFromId(material).getStateDefinition().any().hasProperty(nativeProperty);
    }

    @Override
    public Map<Property<?>, Optional<?>> getValues(BlockData blockData) {
        BlockState state = ((CraftBlockData) blockData).getState();
        Map<Property<?>, Optional<?>> propertyMap = new HashMap<>();
        state.getProperties().forEach(nativeProperty -> {
            Property<?> property = ConversionUtils.fromNative(nativeProperty);
            if (nativeProperty instanceof net.minecraft.world.level.block.state.properties.EnumProperty) {
                StringRepresentable stringRepresentable = (StringRepresentable) state.getValue(nativeProperty);
                propertyMap.put(property, Optional.ofNullable(property.getValueFor(stringRepresentable.getSerializedName())));
            } else {
                propertyMap.put(property, state.getOptionalValue(nativeProperty));
            }
        });
        return propertyMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Comparable<T>> T getValue(BlockData blockData, Property<T> property) {
        net.minecraft.world.level.block.state.properties.Property<?> nativeProperty = ConversionUtils.toNative(property);
        BlockState state = ((CraftBlockData) blockData).getState();
        if (state.hasProperty(nativeProperty)) {
            if (nativeProperty instanceof net.minecraft.world.level.block.state.properties.EnumProperty enumProperty) {
                StringRepresentable stringRepresentable = (StringRepresentable) state.getValue(enumProperty);
                return property.getValueFor(stringRepresentable.getSerializedName());
            } else {
                return (T) state.getValue(nativeProperty);
            }
        } else {
            throw new IllegalArgumentException("Cannot get property " + property.getKey() +
                    " as it does not exist in" + blockData.getMaterial().name());
        }
    }

    @Override
    public <T extends Comparable<T>> BlockData setValue(BlockData blockData, Property<T> property, T value) {
        BlockState state = ((CraftBlockData) blockData).getState();

        if (property instanceof BooleanProperty) {
            net.minecraft.world.level.block.state.properties.BooleanProperty nativeProperty =
                    (net.minecraft.world.level.block.state.properties.BooleanProperty) ConversionUtils.toNative(property);
            state = state.setValue(nativeProperty, (Boolean) value);
        } else if (property instanceof IntegerProperty) {
            net.minecraft.world.level.block.state.properties.IntegerProperty nativeProperty =
                    (net.minecraft.world.level.block.state.properties.IntegerProperty) ConversionUtils.toNative(property);
            state = state.setValue(nativeProperty, (Integer) value);
        } else if (property instanceof EnumProperty enumProperty) {
            String name = ((Nameable) value).getSerializedName();
            state = setEnumValue(state, enumProperty, name);
        }
        return state.createCraftBlockData();
    }

    @Override
    public BlockData createBlockState(NamespaceId material) {
        return CraftBlockData.fromData(getBlockFromId(material).defaultBlockState());
    }

    @Override
    public BlockData createBlockState(NamespaceId material, Map<Property<?>, Optional<?>> values) {
        BlockState state = getBlockFromId(material).defaultBlockState();
        for (Map.Entry<Property<?>, Optional<?>> entry : values.entrySet()) {
            if (entry.getValue().isPresent()) {
                Property<?> property = entry.getKey();
                if (hasProperty(material, property)) {
                    if (property instanceof BooleanProperty) {
                        net.minecraft.world.level.block.state.properties.BooleanProperty nativeProperty =
                                (net.minecraft.world.level.block.state.properties.BooleanProperty) ConversionUtils.toNative(property);
                        state = state.setValue(nativeProperty, (Boolean) entry.getValue().get());
                    } else if (property instanceof IntegerProperty) {
                        net.minecraft.world.level.block.state.properties.IntegerProperty nativeProperty =
                                (net.minecraft.world.level.block.state.properties.IntegerProperty) ConversionUtils.toNative(property);
                        state = state.setValue(nativeProperty, (Integer) entry.getValue().get());
                    } else if (property instanceof EnumProperty enumProperty) {
                        String name = ((Nameable) entry.getValue().get()).getSerializedName();
                        state = setEnumValue(state, enumProperty, name);
                    }
                }
            }
        }
        return state.createCraftBlockData();
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T> & StringRepresentable> BlockState setEnumValue(BlockState state, EnumProperty<?> property,
                                                                        String name) {
        net.minecraft.world.level.block.state.properties.EnumProperty<T> enumProperty =
                (net.minecraft.world.level.block.state.properties.EnumProperty<T>) ConversionUtils.toNative(property);
        Optional<T> value = enumProperty.getValue(name);
        if(value.isPresent()) {
            return state.setValue(enumProperty, value.get());
        } else {
            throw new IllegalStateException("Unexpected value " + name);
        }
    }

}
