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

import com.azortis.orbis.block.property.Properties;
import com.azortis.orbis.block.property.Property;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ConversionUtils {
    private static final BiMap<Property<?>, net.minecraft.world.level.block.state.properties.Property<?>> propertyBiMap;

    static {
        // Map all Orbis properties to NMS properties for easy lookup
        ImmutableBiMap.Builder<Property<?>, net.minecraft.world.level.block.state.properties.Property<?>> propertyBuilder
                = ImmutableBiMap.builder();
        propertyBuilder.put(Properties.ATTACHED, BlockStateProperties.ATTACHED);
        propertyBuilder.put(Properties.BOTTOM, BlockStateProperties.BOTTOM);
        propertyBuilder.put(Properties.CONDITIONAL, BlockStateProperties.CONDITIONAL);
        propertyBuilder.put(Properties.DISARMED, BlockStateProperties.DISARMED);
        propertyBuilder.put(Properties.DRAG, BlockStateProperties.DRAG);
        propertyBuilder.put(Properties.ENABLED, BlockStateProperties.ENABLED);
        propertyBuilder.put(Properties.EXTENDED, BlockStateProperties.EXTENDED);
        propertyBuilder.put(Properties.EYE, BlockStateProperties.EYE);
        propertyBuilder.put(Properties.FALLING, BlockStateProperties.FALLING);
        propertyBuilder.put(Properties.HANGING, BlockStateProperties.HANGING);
        propertyBuilder.put(Properties.HAS_BOTTLE_0, BlockStateProperties.HAS_BOTTLE_0);
        propertyBuilder.put(Properties.HAS_BOTTLE_1, BlockStateProperties.HAS_BOTTLE_1);
        propertyBuilder.put(Properties.HAS_BOTTLE_2, BlockStateProperties.HAS_BOTTLE_2);
        propertyBuilder.put(Properties.HAS_RECORD, BlockStateProperties.HAS_RECORD);
        propertyBuilder.put(Properties.HAS_BOOK, BlockStateProperties.HAS_BOOK);
        propertyBuilder.put(Properties.INVERTED, BlockStateProperties.INVERTED);
        propertyBuilder.put(Properties.IN_WALL, BlockStateProperties.IN_WALL);
        propertyBuilder.put(Properties.LIT, BlockStateProperties.LIT);
        propertyBuilder.put(Properties.LOCKED, BlockStateProperties.LOCKED);
        propertyBuilder.put(Properties.OCCUPIED, BlockStateProperties.OCCUPIED);
        propertyBuilder.put(Properties.OPEN, BlockStateProperties.OPEN);
        propertyBuilder.put(Properties.PERSISTENT, BlockStateProperties.PERSISTENT);
        propertyBuilder.put(Properties.POWERED, BlockStateProperties.POWERED);
        propertyBuilder.put(Properties.SHORT, BlockStateProperties.SHORT);
        propertyBuilder.put(Properties.SIGNAL_FIRE, BlockStateProperties.SIGNAL_FIRE);
        propertyBuilder.put(Properties.SNOWY, BlockStateProperties.SNOWY);
        propertyBuilder.put(Properties.TRIGGERED, BlockStateProperties.TRIGGERED);
        propertyBuilder.put(Properties.UNSTABLE, BlockStateProperties.UNSTABLE);
        propertyBuilder.put(Properties.WATERLOGGED, BlockStateProperties.WATERLOGGED);
        propertyBuilder.put(Properties.VINE_END, BlockStateProperties.VINE_END);
        propertyBuilder.put(Properties.BERRIES, BlockStateProperties.BERRIES);
        propertyBuilder.put(Properties.AXIS, BlockStateProperties.AXIS);
        propertyBuilder.put(Properties.HORIZONTAL_AXIS, BlockStateProperties.HORIZONTAL_AXIS);
        propertyBuilder.put(Properties.UP, BlockStateProperties.UP);
        propertyBuilder.put(Properties.DOWN, BlockStateProperties.DOWN);
        propertyBuilder.put(Properties.NORTH, BlockStateProperties.NORTH);
        propertyBuilder.put(Properties.EAST, BlockStateProperties.EAST);
        propertyBuilder.put(Properties.SOUTH, BlockStateProperties.SOUTH);
        propertyBuilder.put(Properties.WEST, BlockStateProperties.WEST);
        propertyBuilder.put(Properties.FACING, BlockStateProperties.FACING);
        propertyBuilder.put(Properties.FACING_HOPPER, BlockStateProperties.FACING_HOPPER);
        propertyBuilder.put(Properties.HORIZONTAL_FACING, BlockStateProperties.HORIZONTAL_FACING);
        // TODO add jigsaw orientation
        propertyBuilder.put(Properties.NORTH_WALL, BlockStateProperties.NORTH_WALL);
        propertyBuilder.put(Properties.EAST_WALL, BlockStateProperties.EAST_WALL);
        propertyBuilder.put(Properties.SOUTH_WALL, BlockStateProperties.SOUTH_WALL);
        propertyBuilder.put(Properties.WEST_WALL, BlockStateProperties.WEST_WALL);
        propertyBuilder.put(Properties.NORTH_REDSTONE, BlockStateProperties.NORTH_REDSTONE);
        propertyBuilder.put(Properties.EAST_REDSTONE, BlockStateProperties.EAST_REDSTONE);
        propertyBuilder.put(Properties.SOUTH_REDSTONE, BlockStateProperties.SOUTH_REDSTONE);
        propertyBuilder.put(Properties.WEST_REDSTONE, BlockStateProperties.EAST_REDSTONE);
        propertyBuilder.put(Properties.DOUBLE_BLOCK_HALF, BlockStateProperties.DOUBLE_BLOCK_HALF);
        propertyBuilder.put(Properties.HALF, BlockStateProperties.HALF);
        propertyBuilder.put(Properties.RAIL_SHAPE, BlockStateProperties.RAIL_SHAPE);
        propertyBuilder.put(Properties.RAIL_SHAPE_STRAIGHT, BlockStateProperties.RAIL_SHAPE_STRAIGHT);
        propertyBuilder.put(Properties.AGE_1, BlockStateProperties.AGE_1);
        propertyBuilder.put(Properties.AGE_2, BlockStateProperties.AGE_2);
        propertyBuilder.put(Properties.AGE_3, BlockStateProperties.AGE_3);
        propertyBuilder.put(Properties.AGE_5, BlockStateProperties.AGE_5);
        propertyBuilder.put(Properties.AGE_7, BlockStateProperties.AGE_7);
        propertyBuilder.put(Properties.AGE_15, BlockStateProperties.AGE_15);
        propertyBuilder.put(Properties.AGE_25, BlockStateProperties.AGE_25);
        propertyBuilder.put(Properties.BITES, BlockStateProperties.BITES);
        propertyBuilder.put(Properties.CANDLES, BlockStateProperties.CANDLES);
        propertyBuilder.put(Properties.DELAY, BlockStateProperties.DELAY);
        propertyBuilder.put(Properties.DISTANCE, BlockStateProperties.DISTANCE);
        propertyBuilder.put(Properties.SCAFFOLDING_DISTANCE, BlockStateProperties.STABILITY_DISTANCE);
        propertyBuilder.put(Properties.EGGS, BlockStateProperties.EGGS);
        propertyBuilder.put(Properties.HATCH, BlockStateProperties.HATCH);
        propertyBuilder.put(Properties.LAYERS, BlockStateProperties.LAYERS);
        propertyBuilder.put(Properties.LEVEL_CAULDRON, BlockStateProperties.LEVEL_CAULDRON);
        propertyBuilder.put(Properties.LEVEL_COMPOSTER, BlockStateProperties.LEVEL_COMPOSTER);
        propertyBuilder.put(Properties.LEVEL_FLOWING, BlockStateProperties.LEVEL_FLOWING);
        propertyBuilder.put(Properties.LEVEL_HONEY, BlockStateProperties.LEVEL_HONEY);
        propertyBuilder.put(Properties.LEVEL, BlockStateProperties.LEVEL);
        propertyBuilder.put(Properties.MOISTURE, BlockStateProperties.MOISTURE);
        propertyBuilder.put(Properties.NOTE, BlockStateProperties.NOTE);
        propertyBuilder.put(Properties.PICKLES, BlockStateProperties.PICKLES);
        propertyBuilder.put(Properties.POWER, BlockStateProperties.POWER);
        propertyBuilder.put(Properties.STAGE, BlockStateProperties.STAGE);
        propertyBuilder.put(Properties.RESPAWN_ANCHOR_CHARGES, BlockStateProperties.RESPAWN_ANCHOR_CHARGES);
        propertyBuilder.put(Properties.ROTATION, BlockStateProperties.ROTATION_16);
        propertyBuilder.put(Properties.BED_PART, BlockStateProperties.BED_PART);
        propertyBuilder.put(Properties.CHEST_TYPE, BlockStateProperties.CHEST_TYPE);
        propertyBuilder.put(Properties.COMPARATOR_MODE, BlockStateProperties.MODE_COMPARATOR);
        propertyBuilder.put(Properties.DOOR_HINGE, BlockStateProperties.DOOR_HINGE);
        propertyBuilder.put(Properties.NOTE_BLOCK_INSTRUMENT, BlockStateProperties.NOTEBLOCK_INSTRUMENT);
        propertyBuilder.put(Properties.PISTON_TYPE, BlockStateProperties.PISTON_TYPE);
        propertyBuilder.put(Properties.SLAB_TYPE, BlockStateProperties.SLAB_TYPE);
        propertyBuilder.put(Properties.STAIRS_SHAPE, BlockStateProperties.STAIRS_SHAPE);
        propertyBuilder.put(Properties.STRUCTURE_BLOCK_MODE, BlockStateProperties.STRUCTUREBLOCK_MODE);
        propertyBuilder.put(Properties.BAMBOO_LEAVES, BlockStateProperties.BAMBOO_LEAVES);
        propertyBuilder.put(Properties.TILT, BlockStateProperties.TILT);
        propertyBuilder.put(Properties.VERTICAL_DIRECTION, BlockStateProperties.VERTICAL_DIRECTION);
        propertyBuilder.put(Properties.DRIPSTONE_THICKNESS, BlockStateProperties.DRIPSTONE_THICKNESS);
        propertyBuilder.put(Properties.SCULK_SENSOR_PHASE, BlockStateProperties.SCULK_SENSOR_PHASE);
        propertyBiMap = propertyBuilder.build();
    }

    protected static Property<?> fromNative(net.minecraft.world.level.block.state.properties.Property<?> nativeProperty) {
        return propertyBiMap.inverse().get(nativeProperty);
    }

    protected static net.minecraft.world.level.block.state.properties.Property<?> toNative(Property<?> property) {
        return propertyBiMap.get(property);
    }

}
