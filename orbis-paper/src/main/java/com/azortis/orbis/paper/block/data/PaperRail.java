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

package com.azortis.orbis.paper.block.data;

import com.azortis.orbis.block.data.Rail;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class PaperRail extends PaperWaterlogged implements Rail {

    public PaperRail(BlockData handle) {
        super(handle);
    }

    @Override
    public @NotNull Shape getShape() {
        return fromPaper(((org.bukkit.block.data.Rail) getHandle()).getShape());
    }

    @Override
    public void setShape(@NotNull Shape shape) {
        ((org.bukkit.block.data.Rail) getHandle()).setShape(toPaper(shape));
    }

    @Override
    public @NotNull Set<Shape> getShapes() {
        return ((org.bukkit.block.data.Rail) getHandle()).getShapes().stream().map(PaperRail::fromPaper).collect(Collectors.toSet());
    }
    
    public static org.bukkit.block.data.Rail.Shape toPaper(Rail.Shape shape){
        return switch (shape){
            case NORTH_SOUTH -> org.bukkit.block.data.Rail.Shape.NORTH_SOUTH;
            case EAST_WEST -> org.bukkit.block.data.Rail.Shape.EAST_WEST;
            case ASCENDING_EAST -> org.bukkit.block.data.Rail.Shape.ASCENDING_EAST;
            case ASCENDING_WEST -> org.bukkit.block.data.Rail.Shape.ASCENDING_WEST;
            case ASCENDING_NORTH -> org.bukkit.block.data.Rail.Shape.ASCENDING_NORTH;
            case ASCENDING_SOUTH -> org.bukkit.block.data.Rail.Shape.ASCENDING_SOUTH;
            case SOUTH_EAST -> org.bukkit.block.data.Rail.Shape.SOUTH_EAST;
            case SOUTH_WEST -> org.bukkit.block.data.Rail.Shape.SOUTH_WEST;
            case NORTH_WEST -> org.bukkit.block.data.Rail.Shape.NORTH_WEST;
            case NORTH_EAST -> org.bukkit.block.data.Rail.Shape.NORTH_EAST;
        };
    }

    public static Rail.Shape fromPaper(org.bukkit.block.data.Rail.Shape shape){
        return switch (shape){
            case NORTH_SOUTH -> Shape.NORTH_SOUTH;
            case EAST_WEST -> Shape.EAST_WEST;
            case ASCENDING_EAST -> Shape.ASCENDING_EAST;
            case ASCENDING_WEST -> Shape.ASCENDING_WEST;
            case ASCENDING_NORTH -> Shape.ASCENDING_NORTH;
            case ASCENDING_SOUTH -> Shape.ASCENDING_SOUTH;
            case SOUTH_EAST -> Shape.SOUTH_EAST;
            case SOUTH_WEST -> Shape.SOUTH_WEST;
            case NORTH_WEST -> Shape.NORTH_WEST;
            case NORTH_EAST -> Shape.NORTH_EAST;
        };
    }
    
}
