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

package com.azortis.orbis.paper.block.data.type;

import com.azortis.orbis.block.data.type.Jigsaw;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

public class PaperJigsaw extends PaperBlockData implements Jigsaw {

    public PaperJigsaw(BlockData handle) {
        super(handle);
    }

    @Override
    public @NotNull Orientation getOrientation() {
        return fromPaper(((org.bukkit.block.data.type.Jigsaw) getHandle()).getOrientation());
    }

    @Override
    public void setOrientation(@NotNull Orientation orientation) {
        ((org.bukkit.block.data.type.Jigsaw) getHandle()).setOrientation(toPaper(orientation));
    }

    public static org.bukkit.block.data.type.Jigsaw.Orientation toPaper(Orientation orientation){
        return switch (orientation){
            case DOWN_EAST -> org.bukkit.block.data.type.Jigsaw.Orientation.DOWN_EAST;
            case DOWN_NORTH -> org.bukkit.block.data.type.Jigsaw.Orientation.DOWN_NORTH;
            case DOWN_SOUTH -> org.bukkit.block.data.type.Jigsaw.Orientation.DOWN_SOUTH;
            case DOWN_WEST -> org.bukkit.block.data.type.Jigsaw.Orientation.DOWN_WEST;
            case UP_EAST -> org.bukkit.block.data.type.Jigsaw.Orientation.UP_EAST;
            case UP_NORTH -> org.bukkit.block.data.type.Jigsaw.Orientation.UP_NORTH;
            case UP_SOUTH -> org.bukkit.block.data.type.Jigsaw.Orientation.UP_SOUTH;
            case UP_WEST -> org.bukkit.block.data.type.Jigsaw.Orientation.UP_WEST;
            case WEST_UP -> org.bukkit.block.data.type.Jigsaw.Orientation.WEST_UP;
            case EAST_UP -> org.bukkit.block.data.type.Jigsaw.Orientation.EAST_UP;
            case NORTH_UP -> org.bukkit.block.data.type.Jigsaw.Orientation.NORTH_UP;
            case SOUTH_UP -> org.bukkit.block.data.type.Jigsaw.Orientation.SOUTH_UP;
        };
    }

    public static Orientation fromPaper(org.bukkit.block.data.type.Jigsaw.Orientation orientation){
        return switch (orientation){
            case DOWN_EAST -> Orientation.DOWN_EAST;
            case DOWN_NORTH -> Orientation.DOWN_NORTH;
            case DOWN_SOUTH -> Orientation.DOWN_SOUTH;
            case DOWN_WEST -> Orientation.DOWN_WEST;
            case UP_EAST -> Orientation.UP_EAST;
            case UP_NORTH -> Orientation.UP_NORTH;
            case UP_SOUTH -> Orientation.UP_SOUTH;
            case UP_WEST -> Orientation.UP_WEST;
            case WEST_UP -> Orientation.WEST_UP;
            case EAST_UP -> Orientation.EAST_UP;
            case NORTH_UP -> Orientation.NORTH_UP;
            case SOUTH_UP -> Orientation.SOUTH_UP;
        };
    }

}
