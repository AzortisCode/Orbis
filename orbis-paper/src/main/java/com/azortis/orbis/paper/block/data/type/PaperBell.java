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
import com.azortis.orbis.block.data.type.Bell;
import com.azortis.orbis.paper.block.BlockAdapter;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class PaperBell extends PaperBlockData implements Bell {

    public PaperBell(BlockData handle) {
        super(handle);
    }

    @Override
    public @NotNull BlockFace getFacing() {
        return BlockAdapter.fromPaper(((org.bukkit.block.data.type.Bell) getHandle()).getFacing());
    }

    @Override
    public void setFacing(@NotNull BlockFace facing) {
        ((org.bukkit.block.data.type.Bell) getHandle()).setFacing(BlockAdapter.toPaper(facing));
    }

    @Override
    public @NotNull Set<BlockFace> getFaces() {
        return ((org.bukkit.block.data.type.Bell) getHandle()).getFaces().stream().map(BlockAdapter::fromPaper)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isPowered() {
        return ((org.bukkit.block.data.type.Bell) getHandle()).isPowered();
    }

    @Override
    public void setPowered(boolean powered) {
        ((org.bukkit.block.data.type.Bell) getHandle()).setPowered(powered);
    }

    @Override
    public @NotNull Attachment getAttachment() {
        return fromPaper(((org.bukkit.block.data.type.Bell) getHandle()).getAttachment());
    }

    @Override
    public void setAttachment(@NotNull Attachment attachment) {
        ((org.bukkit.block.data.type.Bell) getHandle()).setAttachment(toPaper(attachment));
    }

    public static org.bukkit.block.data.type.Bell.Attachment toPaper(Attachment attachment){
        return switch (attachment){
            case FLOOR -> org.bukkit.block.data.type.Bell.Attachment.FLOOR;
            case CEILING -> org.bukkit.block.data.type.Bell.Attachment.CEILING;
            case SINGLE_WALL -> org.bukkit.block.data.type.Bell.Attachment.SINGLE_WALL;
            case DOUBLE_WALL -> org.bukkit.block.data.type.Bell.Attachment.DOUBLE_WALL;
        };
    }

    public static Attachment fromPaper(org.bukkit.block.data.type.Bell.Attachment attachment){
        return switch (attachment){
            case FLOOR -> Attachment.FLOOR;
            case CEILING -> Attachment.CEILING;
            case SINGLE_WALL -> Attachment.SINGLE_WALL;
            case DOUBLE_WALL -> Attachment.DOUBLE_WALL;
        };
    }

}
