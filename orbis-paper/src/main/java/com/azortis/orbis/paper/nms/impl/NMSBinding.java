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

import com.azortis.orbis.block.property.*;
import com.azortis.orbis.paper.nms.INMSBinding;
import com.azortis.orbis.util.Nameable;
import com.azortis.orbis.util.NamespaceId;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;

import java.util.*;

public class NMSBinding implements INMSBinding {

    private static Block getBlockFromId(NamespaceId material){
        return Registry.BLOCK.get(ResourceLocation.tryParse(material.getId()));
    }

    @Override
    public Map<Property<?>, Optional<?>> getPropertyMap(BlockData blockData) {
        BlockState state = ((CraftBlockData)blockData).getState();
        Map<Property<?>, Optional<?>> propertyMap = new HashMap<>();
        state.getProperties().forEach(nativeProperty -> {
            Property<?> property = ConversionUtils.fromNative(nativeProperty);
            if(nativeProperty instanceof net.minecraft.world.level.block.state.properties.EnumProperty){
                StringRepresentable stringRepresentable = (StringRepresentable) state.getValue(nativeProperty);
                propertyMap.put(property, Optional.ofNullable(property.getValueFor(stringRepresentable.getSerializedName())));
            } else {
                propertyMap.put(property, state.getOptionalValue(nativeProperty));
            }
        });
        return propertyMap;
    }

    @Override
    public Map<String, Property<?>> getProperties(NamespaceId material) {
        BlockState state = getBlockFromId(material).defaultBlockState();
        Map<String, Property<?>> propertyMap = new HashMap<>();
        state.getProperties().forEach(nativeProperty -> {
            Property<?> property = ConversionUtils.fromNative(nativeProperty);
            propertyMap.put(property.getName(), property);
        });
        return propertyMap;
    }

    @Override
    public boolean hasProperty(NamespaceId material, Property<?> property) {
        net.minecraft.world.level.block.state.properties.Property<?> nativeProperty = ConversionUtils.toNative(property);
        return getBlockFromId(material).defaultBlockState().hasProperty(nativeProperty);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue(BlockData blockData, Property<T> property) {
        net.minecraft.world.level.block.state.properties.Property<?> nativeProperty = ConversionUtils.toNative(property);
        BlockState state = ((CraftBlockData) blockData).getState();

        if(state.hasProperty(nativeProperty)) {
            if (nativeProperty instanceof net.minecraft.world.level.block.state.properties.EnumProperty enumProperty) {
                StringRepresentable stringRepresentable = (StringRepresentable) state.getValue(enumProperty);
                return property.getValueFor(stringRepresentable.getSerializedName());
            } else {
                return (T) state.getValue(nativeProperty);
            }
        } else {
            throw new IllegalArgumentException("Cannot get property " + property.getName() +
                    " as it does not exist in" + blockData.getMaterial().name());
        }
    }

    @Override
    public <T> void setValue(BlockData blockData, Property<T> property, T value) {
        BlockState state = ((CraftBlockData) blockData).getState();

        if(property instanceof BooleanProperty){
            net.minecraft.world.level.block.state.properties.BooleanProperty nativeProperty =
                    (net.minecraft.world.level.block.state.properties.BooleanProperty) ConversionUtils.toNative(property);
            state.setValue(nativeProperty, (Boolean) value);
        } else if(property instanceof IntegerProperty){
            net.minecraft.world.level.block.state.properties.IntegerProperty nativeProperty =
                    (net.minecraft.world.level.block.state.properties.IntegerProperty) ConversionUtils.toNative(property);
            state.setValue(nativeProperty, (Integer) value);
        } else if(property instanceof EnumProperty enumProperty){
            String name = ((Nameable)value).getSerializedName();
            setEnumValue(state, enumProperty, name);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T> & StringRepresentable> void setEnumValue(BlockState state, EnumProperty<?> property,
                                                                        String name){
        net.minecraft.world.level.block.state.properties.EnumProperty<T> enumProperty =
                (net.minecraft.world.level.block.state.properties.EnumProperty<T>) ConversionUtils.toNative(property);
        Optional<T> value = enumProperty.getValue(name);
        value.ifPresentOrElse(t -> state.setValue(enumProperty, t), () -> {
            throw new IllegalStateException("Unexpected value: " + name);
        });
    }

}
