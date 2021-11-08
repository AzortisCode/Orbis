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

import com.azortis.orbis.paper.nms.INMSBinding;
import com.azortis.orbis.util.NamespaceId;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftMagicNumbers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NMSBinding implements INMSBinding {

    @Override
    public Map<String, String> getPropertyMap(BlockData blockData) {
        BlockState state = ((CraftBlockData)blockData).getState();
        Map<String, String> propertyMap = new HashMap<>();
        for (Property<?> property : state.getProperties()){
            String value = property.value(state).toString().split("=")[1];
            propertyMap.put(property.getName(), value);
        }
        return propertyMap;
    }

    public BlockData createBlockData(NamespaceId id, Map<String, String> properties){
        Block block = CraftMagicNumbers.getBlock(Material.valueOf(id.getId().toLowerCase(Locale.ENGLISH)));
        BlockState state = block.defaultBlockState();

        for(Property<?> property : state.getProperties()){

        }
        return null;
    }

}
