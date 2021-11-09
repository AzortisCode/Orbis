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

import com.azortis.orbis.block.Axis;
import com.azortis.orbis.block.property.*;
import com.azortis.orbis.paper.nms.INMSBinding;
import com.azortis.orbis.util.NamespaceId;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;

import java.util.*;
import java.util.stream.Collectors;

public class NMSBinding implements INMSBinding {

    private static Block getBlockFromId(NamespaceId material){
        return Registry.BLOCK.get(ResourceLocation.tryParse(material.getId()));
    }

    private static net.minecraft.world.level.block.state.properties.Property<?> translateProperty(Property<?> property){
        final String name = property.getName();
        net.minecraft.world.level.block.state.properties.Property<?> nativeProp = null;

        if(property instanceof BooleanProperty){
            nativeProp = net.minecraft.world.level.block.state.properties.BooleanProperty.create(name);
        } else if (property instanceof IntegerProperty intProperty){
            nativeProp = net.minecraft.world.level.block.state.properties.IntegerProperty.create(name,
                    intProperty.getMin(), intProperty.getMax());
        } else if (property instanceof EnumProperty enumProperty){
            Class<?> type = enumProperty.getType();

            // Check against all the different enums... kill me
            if(type == Axis.class) {
                nativeProp = net.minecraft.world.level.block.state.properties.EnumProperty.create(name,
                        net.minecraft.core.Direction.Axis.class,
                        getNativeValues(net.minecraft.core.Direction.Axis.class, enumProperty));
            } else if (type == BedPart.class){
                nativeProp = net.minecraft.world.level.block.state.properties.EnumProperty.create(name,
                        net.minecraft.world.level.block.state.properties.BedPart.class,
                        getNativeValues(net.minecraft.world.level.block.state.properties.BedPart.class, enumProperty));
            }else if(type == Direction.class){
                nativeProp = net.minecraft.world.level.block.state.properties.EnumProperty.create(name,
                        net.minecraft.core.Direction.class,
                        getNativeValues(net.minecraft.core.Direction.class, enumProperty));
            }
        }
        return nativeProp;
    }

    private static <T extends StringRepresentable> Collection<T> getNativeValues(Class<T> type, EnumProperty<?> enumProperty){
        return Arrays.stream(type.getEnumConstants()).filter(t -> enumProperty.getNames().contains(t.getSerializedName()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Map<Property<?>, ?> getPropertyMap(BlockData blockData) {
        BlockState state = ((CraftBlockData)blockData).getState();
        return null;
    }

    @Override
    public Property<?> getProperty(NamespaceId material, String name) {
        return null;
    }

    @Override
    public Map<String, Property<?>> getProperties(NamespaceId material) {
        return null;
    }

    @Override
    public boolean hasProperty(NamespaceId material, Property<?> property) {
        net.minecraft.world.level.block.state.properties.Property<?> property1 = translateProperty(property);
        if(property1 == null) return false;
        return getBlockFromId(material).defaultBlockState().hasProperty(property1);
    }

    @Override
    public <T> T getValue(BlockData blockData, Property<T> property) {
        return null;
    }

    @Override
    public <T> void setValue(BlockData blockData, Property<T> property, T value) {

    }
}
