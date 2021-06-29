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

package com.azortis.orbis.paper.block;

import com.azortis.orbis.block.Axis;
import com.azortis.orbis.block.BlockFace;

// Utility class for converting common paper block enums to orbis enums
public class BlockAdapter {

    public static org.bukkit.block.BlockFace toPaper(BlockFace blockFace){
        return switch (blockFace) {
            case NORTH -> org.bukkit.block.BlockFace.NORTH;
            case EAST -> org.bukkit.block.BlockFace.EAST;
            case SOUTH -> org.bukkit.block.BlockFace.SOUTH;
            case WEST -> org.bukkit.block.BlockFace.WEST;
            case UP -> org.bukkit.block.BlockFace.UP;
            case DOWN -> org.bukkit.block.BlockFace.DOWN;
            case NORTH_EAST -> org.bukkit.block.BlockFace.NORTH_EAST;
            case NORTH_WEST -> org.bukkit.block.BlockFace.NORTH_WEST;
            case SOUTH_EAST -> org.bukkit.block.BlockFace.SOUTH_EAST;
            case SOUTH_WEST -> org.bukkit.block.BlockFace.SOUTH_WEST;
            case WEST_NORTH_WEST -> org.bukkit.block.BlockFace.WEST_NORTH_WEST;
            case NORTH_NORTH_WEST -> org.bukkit.block.BlockFace.NORTH_NORTH_WEST;
            case NORTH_NORTH_EAST -> org.bukkit.block.BlockFace.NORTH_NORTH_EAST;
            case EAST_NORTH_EAST -> org.bukkit.block.BlockFace.EAST_NORTH_EAST;
            case EAST_SOUTH_EAST -> org.bukkit.block.BlockFace.EAST_SOUTH_EAST;
            case SOUTH_SOUTH_EAST -> org.bukkit.block.BlockFace.SOUTH_SOUTH_EAST;
            case SOUTH_SOUTH_WEST -> org.bukkit.block.BlockFace.SOUTH_SOUTH_WEST;
            case WEST_SOUTH_WEST -> org.bukkit.block.BlockFace.WEST_SOUTH_WEST;
            case SELF -> org.bukkit.block.BlockFace.SELF;
        };
    }

    public static BlockFace fromPaper(org.bukkit.block.BlockFace blockFace){
        return switch (blockFace){
            case NORTH -> BlockFace.NORTH;
            case EAST -> BlockFace.EAST;
            case SOUTH -> BlockFace.SOUTH;
            case WEST -> BlockFace.WEST;
            case UP -> BlockFace.UP;
            case DOWN -> BlockFace.DOWN;
            case NORTH_EAST -> BlockFace.NORTH_EAST;
            case NORTH_WEST -> BlockFace.NORTH_WEST;
            case SOUTH_EAST -> BlockFace.SOUTH_EAST;
            case SOUTH_WEST -> BlockFace.SOUTH_WEST;
            case WEST_NORTH_WEST -> BlockFace.WEST_NORTH_WEST;
            case NORTH_NORTH_WEST -> BlockFace.NORTH_NORTH_WEST;
            case NORTH_NORTH_EAST -> BlockFace.NORTH_NORTH_EAST;
            case EAST_NORTH_EAST -> BlockFace.EAST_NORTH_EAST;
            case EAST_SOUTH_EAST -> BlockFace.EAST_SOUTH_EAST;
            case SOUTH_SOUTH_EAST -> BlockFace.SOUTH_SOUTH_EAST;
            case SOUTH_SOUTH_WEST -> BlockFace.SOUTH_SOUTH_WEST;
            case WEST_SOUTH_WEST -> BlockFace.WEST_SOUTH_WEST;
            case SELF -> BlockFace.SELF;
        };
    }

    public static org.bukkit.Axis toPaper(Axis axis){
        return switch (axis){
            case X -> org.bukkit.Axis.X;
            case Y -> org.bukkit.Axis.Y;
            case Z -> org.bukkit.Axis.Z;
        };
    }

    public static Axis fromPaper(org.bukkit.Axis axis){
        return switch (axis){
            case X -> Axis.X;
            case Y -> Axis.Y;
            case Z -> Axis.Z;
        };
    }

}
