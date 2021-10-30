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
import com.azortis.orbis.block.data.type.Bed;
import com.azortis.orbis.paper.block.BlockAdapter;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class PaperBed extends PaperBlockData implements Bed {

    public PaperBed(BlockData handle) {
        super(handle);
    }

    @Override
    public @NotNull BlockFace getFacing() {
        return BlockAdapter.fromPaper(((org.bukkit.block.data.type.Bed) getHandle()).getFacing());
    }

    @Override
    public void setFacing(@NotNull BlockFace facing) {
        ((org.bukkit.block.data.type.Bed) getHandle()).setFacing(BlockAdapter.toPaper(facing));
    }

    @Override
    public @NotNull Set<BlockFace> getFaces() {
        return ((org.bukkit.block.data.type.Bed) getHandle()).getFaces().stream().map(BlockAdapter::fromPaper)
                .collect(Collectors.toSet());
    }

    @Override
    public @NotNull Part getPart() {
        return fromPaper(((org.bukkit.block.data.type.Bed) getHandle()).getPart());
    }

    @Override
    public void setPart(@NotNull Part part) {
        ((org.bukkit.block.data.type.Bed) getHandle()).setPart(toPaper(part));
    }

    @Override
    public boolean isOccupied() {
        return ((org.bukkit.block.data.type.Bed) getHandle()).isOccupied();
    }

    public static org.bukkit.block.data.type.Bed.Part toPaper(Part part){
        return switch (part){
            case HEAD -> org.bukkit.block.data.type.Bed.Part.HEAD;
            case FOOT -> org.bukkit.block.data.type.Bed.Part.FOOT;
        };
    }

    public static Part fromPaper(org.bukkit.block.data.type.Bed.Part part){
        return switch (part){
            case HEAD -> Part.HEAD;
            case FOOT -> Part.FOOT;
        };
    }

}
