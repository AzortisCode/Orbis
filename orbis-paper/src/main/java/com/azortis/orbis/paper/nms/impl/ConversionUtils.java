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

import com.azortis.orbis.block.property.Property;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public class ConversionUtils {
    private static final BiMap<Property<?>, net.minecraft.world.level.block.state.properties.Property<?>> propertyBiMap;

    static {
        // Map all Orbis properties to NMS properties for easy lookup
        ImmutableBiMap.Builder<Property<?>, net.minecraft.world.level.block.state.properties.Property<?>> propertyBuilder
                = ImmutableBiMap.builder();
        /*propertyBuilder.put(SafeProperties.ATTACHED, BlockStateProperties.ATTACHED);
        propertyBuilder.put(SafeProperties.BOTTOM, BlockStateProperties.BOTTOM);
        propertyBuilder.put(SafeProperties.CONDITIONAL, BlockStateProperties.CONDITIONAL);
        propertyBuilder.put(SafeProperties.DISARMED, BlockStateProperties.DISARMED);
        propertyBuilder.put(SafeProperties.DRAG, BlockStateProperties.DRAG);
        propertyBuilder.put(SafeProperties.ENABLED, BlockStateProperties.ENABLED);
        propertyBuilder.put(SafeProperties.EXTENDED, BlockStateProperties.EXTENDED);
        propertyBuilder.put(SafeProperties.EYE, BlockStateProperties.EYE);
        propertyBuilder.put(SafeProperties.FALLING, BlockStateProperties.FALLING);
        propertyBuilder.put(SafeProperties.HANGING, BlockStateProperties.HANGING);
        propertyBuilder.put(SafeProperties.HAS_BOTTLE_0, BlockStateProperties.HAS_BOTTLE_0);
        propertyBuilder.put(SafeProperties.HAS_BOTTLE_1, BlockStateProperties.HAS_BOTTLE_1);
        propertyBuilder.put(SafeProperties.HAS_BOTTLE_2, BlockStateProperties.HAS_BOTTLE_2);
        propertyBuilder.put(SafeProperties.HAS_RECORD, BlockStateProperties.HAS_RECORD);
        propertyBuilder.put(SafeProperties.HAS_BOOK, BlockStateProperties.HAS_BOOK);
        propertyBuilder.put(SafeProperties.INVERTED, BlockStateProperties.INVERTED);
        propertyBuilder.put(SafeProperties.IN_WALL, BlockStateProperties.IN_WALL);
        propertyBuilder.put(SafeProperties.LIT, BlockStateProperties.LIT);
        propertyBuilder.put(SafeProperties.LOCKED, BlockStateProperties.LOCKED);
        propertyBuilder.put(SafeProperties.OCCUPIED, BlockStateProperties.OCCUPIED);
        propertyBuilder.put(SafeProperties.OPEN, BlockStateProperties.OPEN);
        propertyBuilder.put(SafeProperties.PERSISTENT, BlockStateProperties.PERSISTENT);
        propertyBuilder.put(SafeProperties.POWERED, BlockStateProperties.POWERED);
        propertyBuilder.put(SafeProperties.SHORT, BlockStateProperties.SHORT);
        propertyBuilder.put(SafeProperties.SIGNAL_FIRE, BlockStateProperties.SIGNAL_FIRE);
        propertyBuilder.put(SafeProperties.SNOWY, BlockStateProperties.SNOWY);
        propertyBuilder.put(SafeProperties.TRIGGERED, BlockStateProperties.TRIGGERED);
        propertyBuilder.put(SafeProperties.UNSTABLE, BlockStateProperties.UNSTABLE);
        propertyBuilder.put(SafeProperties.WATERLOGGED, BlockStateProperties.WATERLOGGED);
        propertyBuilder.put(SafeProperties.VINE_END, BlockStateProperties.VINE_END);
        propertyBuilder.put(SafeProperties.BERRIES, BlockStateProperties.BERRIES);
        propertyBuilder.put(SafeProperties.AXIS, BlockStateProperties.AXIS);
        propertyBuilder.put(SafeProperties.HORIZONTAL_AXIS, BlockStateProperties.HORIZONTAL_AXIS);
        propertyBuilder.put(SafeProperties.UP, BlockStateProperties.UP);
        propertyBuilder.put(SafeProperties.DOWN, BlockStateProperties.DOWN);
        propertyBuilder.put(SafeProperties.NORTH, BlockStateProperties.NORTH);
        propertyBuilder.put(SafeProperties.EAST, BlockStateProperties.EAST);
        propertyBuilder.put(SafeProperties.SOUTH, BlockStateProperties.SOUTH);
        propertyBuilder.put(SafeProperties.WEST, BlockStateProperties.WEST);
        propertyBuilder.put(SafeProperties.FACING, BlockStateProperties.FACING);
        propertyBuilder.put(SafeProperties.FACING_HOPPER, BlockStateProperties.FACING_HOPPER);
        propertyBuilder.put(SafeProperties.HORIZONTAL_FACING, BlockStateProperties.HORIZONTAL_FACING);
        // TODO add jigsaw orientation
        propertyBuilder.put(SafeProperties.NORTH_WALL, BlockStateProperties.NORTH_WALL);
        propertyBuilder.put(SafeProperties.EAST_WALL, BlockStateProperties.EAST_WALL);
        propertyBuilder.put(SafeProperties.SOUTH_WALL, BlockStateProperties.SOUTH_WALL);
        propertyBuilder.put(SafeProperties.WEST_WALL, BlockStateProperties.WEST_WALL);
        propertyBuilder.put(SafeProperties.NORTH_REDSTONE, BlockStateProperties.NORTH_REDSTONE);
        propertyBuilder.put(SafeProperties.EAST_REDSTONE, BlockStateProperties.EAST_REDSTONE);
        propertyBuilder.put(SafeProperties.SOUTH_REDSTONE, BlockStateProperties.SOUTH_REDSTONE);
        propertyBuilder.put(SafeProperties.WEST_REDSTONE, BlockStateProperties.EAST_REDSTONE);
        propertyBuilder.put(SafeProperties.DOUBLE_BLOCK_HALF, BlockStateProperties.DOUBLE_BLOCK_HALF);
        propertyBuilder.put(SafeProperties.HALF, BlockStateProperties.HALF);
        propertyBuilder.put(SafeProperties.RAIL_SHAPE, BlockStateProperties.RAIL_SHAPE);
        propertyBuilder.put(SafeProperties.RAIL_SHAPE_STRAIGHT, BlockStateProperties.RAIL_SHAPE_STRAIGHT);
        propertyBuilder.put(SafeProperties.AGE_1, BlockStateProperties.AGE_1);
        propertyBuilder.put(SafeProperties.AGE_2, BlockStateProperties.AGE_2);
        propertyBuilder.put(SafeProperties.AGE_3, BlockStateProperties.AGE_3);
        propertyBuilder.put(SafeProperties.AGE_5, BlockStateProperties.AGE_5);
        propertyBuilder.put(SafeProperties.AGE_7, BlockStateProperties.AGE_7);
        propertyBuilder.put(SafeProperties.AGE_15, BlockStateProperties.AGE_15);
        propertyBuilder.put(SafeProperties.AGE_25, BlockStateProperties.AGE_25);
        propertyBuilder.put(SafeProperties.BITES, BlockStateProperties.BITES);
        propertyBuilder.put(SafeProperties.CANDLES, BlockStateProperties.CANDLES);
        propertyBuilder.put(SafeProperties.DELAY, BlockStateProperties.DELAY);
        propertyBuilder.put(SafeProperties.DISTANCE, BlockStateProperties.DISTANCE);
        propertyBuilder.put(SafeProperties.SCAFFOLDING_DISTANCE, BlockStateProperties.STABILITY_DISTANCE);
        propertyBuilder.put(SafeProperties.EGGS, BlockStateProperties.EGGS);
        propertyBuilder.put(SafeProperties.HATCH, BlockStateProperties.HATCH);
        propertyBuilder.put(SafeProperties.LAYERS, BlockStateProperties.LAYERS);
        propertyBuilder.put(SafeProperties.LEVEL_CAULDRON, BlockStateProperties.LEVEL_CAULDRON);
        propertyBuilder.put(SafeProperties.LEVEL_COMPOSTER, BlockStateProperties.LEVEL_COMPOSTER);
        propertyBuilder.put(SafeProperties.LEVEL_FLOWING, BlockStateProperties.LEVEL_FLOWING);
        propertyBuilder.put(SafeProperties.LEVEL_HONEY, BlockStateProperties.LEVEL_HONEY);
        propertyBuilder.put(SafeProperties.LEVEL, BlockStateProperties.LEVEL);
        propertyBuilder.put(SafeProperties.MOISTURE, BlockStateProperties.MOISTURE);
        propertyBuilder.put(SafeProperties.NOTE, BlockStateProperties.NOTE);
        propertyBuilder.put(SafeProperties.PICKLES, BlockStateProperties.PICKLES);
        propertyBuilder.put(SafeProperties.POWER, BlockStateProperties.POWER);
        propertyBuilder.put(SafeProperties.STAGE, BlockStateProperties.STAGE);
        propertyBuilder.put(SafeProperties.RESPAWN_ANCHOR_CHARGES, BlockStateProperties.RESPAWN_ANCHOR_CHARGES);
        propertyBuilder.put(SafeProperties.ROTATION, BlockStateProperties.ROTATION_16);
        propertyBuilder.put(SafeProperties.BED_PART, BlockStateProperties.BED_PART);
        propertyBuilder.put(SafeProperties.CHEST_TYPE, BlockStateProperties.CHEST_TYPE);
        propertyBuilder.put(SafeProperties.COMPARATOR_MODE, BlockStateProperties.MODE_COMPARATOR);
        propertyBuilder.put(SafeProperties.DOOR_HINGE, BlockStateProperties.DOOR_HINGE);
        propertyBuilder.put(SafeProperties.NOTE_BLOCK_INSTRUMENT, BlockStateProperties.NOTEBLOCK_INSTRUMENT);
        propertyBuilder.put(SafeProperties.PISTON_TYPE, BlockStateProperties.PISTON_TYPE);
        propertyBuilder.put(SafeProperties.SLAB_TYPE, BlockStateProperties.SLAB_TYPE);
        propertyBuilder.put(SafeProperties.STAIRS_SHAPE, BlockStateProperties.STAIRS_SHAPE);
        propertyBuilder.put(SafeProperties.STRUCTURE_BLOCK_MODE, BlockStateProperties.STRUCTUREBLOCK_MODE);
        propertyBuilder.put(SafeProperties.BAMBOO_LEAVES, BlockStateProperties.BAMBOO_LEAVES);
        propertyBuilder.put(SafeProperties.TILT, BlockStateProperties.TILT);
        propertyBuilder.put(SafeProperties.VERTICAL_DIRECTION, BlockStateProperties.VERTICAL_DIRECTION);
        propertyBuilder.put(SafeProperties.DRIPSTONE_THICKNESS, BlockStateProperties.DRIPSTONE_THICKNESS);
        propertyBuilder.put(SafeProperties.SCULK_SENSOR_PHASE, BlockStateProperties.SCULK_SENSOR_PHASE);*/
        propertyBiMap = propertyBuilder.build();
    }

    protected static Property<?> fromNative(net.minecraft.world.level.block.state.properties.Property<?> nativeProperty) {
        return propertyBiMap.inverse().get(nativeProperty);
    }

    protected static net.minecraft.world.level.block.state.properties.Property<?> toNative(Property<?> property) {
        return propertyBiMap.get(property);
    }

}
