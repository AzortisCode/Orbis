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

package com.azortis.orbis.paper.nms;

import com.azortis.orbis.block.BlockRegistry;
import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.item.Item;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

public final class ConversionUtils {

    public static BlockState getBlock(@NotNull BlockData blockData) {
        return BlockRegistry.fromStateId(net.minecraft.world.level.block.Block.getId(((CraftBlockData) blockData).getState()));
    }

    /**
     * Convert Orbis its {@link BlockState} to Paper its {@link BlockData}
     *
     * @param blockState The {@link BlockState} to convert
     * @return The {@link BlockData} state associated with the {@link BlockState}
     */
    public static BlockData getBlock(@NotNull BlockState blockState) {
        return getBlock(blockState.stateId());
    }

    /**
     * Convert Orbis its {@link BlockState} to Paper its {@link BlockData}
     *
     * @param stateId The inherent stateId {@link BlockState#stateId()}
     * @return The {@link BlockData} state associated with the {@link BlockState}
     */
    public static BlockData getBlock(int stateId) {
        return net.minecraft.world.level.block.Block.stateById(stateId).createCraftBlockData();
    }

    public static Material getMaterial(@NotNull Item item) {
        return CraftMagicNumbers.getMaterial(Registry.ITEM.get(ResourceLocation.tryParse(item.key().asString())));
    }

    public static Item getItem(@NotNull Material material) {
        return Item.fromKey(Registry.ITEM.getKey(CraftMagicNumbers.getItem(material)).toString());
    }

}
