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

import com.azortis.orbis.block.BlockFace;
import com.azortis.orbis.block.data.type.Door;
import com.azortis.orbis.paper.block.BlockAdapter;
import com.azortis.orbis.paper.block.data.PaperBisected;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class PaperDoor extends PaperBlockData implements Door {

    public PaperDoor(BlockData handle) {
        super(handle);
    }

    @Override
    public @NotNull Half getHalf() {
        return PaperBisected.fromPaper(((org.bukkit.block.data.type.Door) getHandle()).getHalf());
    }

    @Override
    public void setHalf(@NotNull Half half) {
        ((org.bukkit.block.data.type.Door) getHandle()).setHalf(PaperBisected.toPaper(half));
    }

    @Override
    public @NotNull BlockFace getFacing() {
        return BlockAdapter.fromPaper(((org.bukkit.block.data.type.Door) getHandle()).getFacing());
    }

    @Override
    public void setFacing(@NotNull BlockFace facing) {
        ((org.bukkit.block.data.type.Door) getHandle()).setFacing(BlockAdapter.toPaper(facing));
    }

    @Override
    public @NotNull Set<BlockFace> getFaces() {
        return ((org.bukkit.block.data.type.Door) getHandle()).getFaces().stream().map(BlockAdapter::fromPaper)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isOpen() {
        return ((org.bukkit.block.data.type.Door) getHandle()).isOpen();
    }

    @Override
    public void setOpen(boolean open) {
        ((org.bukkit.block.data.type.Door) getHandle()).setOpen(open);
    }

    @Override
    public boolean isPowered() {
        return ((org.bukkit.block.data.type.Door) getHandle()).isPowered();
    }

    @Override
    public void setPowered(boolean powered) {
        ((org.bukkit.block.data.type.Door) getHandle()).setPowered(powered);
    }

    @Override
    public @NotNull Hinge getHinge() {
        return fromPaper(((org.bukkit.block.data.type.Door) getHandle()).getHinge());
    }

    @Override
    public void setHinge(@NotNull Hinge hinge) {
        ((org.bukkit.block.data.type.Door) getHandle()).setHinge(toPaper(hinge));
    }

    public static org.bukkit.block.data.type.Door.Hinge toPaper(Hinge hinge){
        return switch (hinge){
            case LEFT -> org.bukkit.block.data.type.Door.Hinge.LEFT;
            case RIGHT -> org.bukkit.block.data.type.Door.Hinge.RIGHT;
        };
    }

    public static Hinge fromPaper(org.bukkit.block.data.type.Door.Hinge hinge){
        return switch (hinge){
            case LEFT -> Hinge.LEFT;
            case RIGHT -> Hinge.RIGHT;
        };
    }

}
