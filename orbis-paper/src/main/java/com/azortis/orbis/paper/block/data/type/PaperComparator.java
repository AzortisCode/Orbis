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
import com.azortis.orbis.block.data.type.Comparator;
import com.azortis.orbis.paper.block.BlockAdapter;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class PaperComparator extends PaperBlockData implements Comparator {

    public PaperComparator(BlockData handle) {
        super(handle);
    }

    @Override
    public @NotNull BlockFace getFacing() {
        return BlockAdapter.fromPaper(((org.bukkit.block.data.type.Comparator) getHandle()).getFacing());
    }

    @Override
    public void setFacing(@NotNull BlockFace facing) {
        ((org.bukkit.block.data.type.Comparator) getHandle()).setFacing(BlockAdapter.toPaper(facing));
    }

    @Override
    public @NotNull Set<BlockFace> getFaces() {
        return ((org.bukkit.block.data.type.Comparator) getHandle()).getFaces().stream().map(BlockAdapter::fromPaper)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isPowered() {
        return ((org.bukkit.block.data.type.Comparator) getHandle()).isPowered();
    }

    @Override
    public void setPowered(boolean powered) {
        ((org.bukkit.block.data.type.Comparator) getHandle()).setPowered(powered);
    }

    @Override
    public @NotNull Mode getMode() {
        return fromPaper(((org.bukkit.block.data.type.Comparator) getHandle()).getMode());
    }

    @Override
    public void setMode(@NotNull Mode mode) {
        ((org.bukkit.block.data.type.Comparator) getHandle()).setMode(toPaper(mode));
    }

    public static org.bukkit.block.data.type.Comparator.Mode toPaper(Mode mode){
        return switch (mode){
            case COMPARE -> org.bukkit.block.data.type.Comparator.Mode.COMPARE;
            case SUBTRACT -> org.bukkit.block.data.type.Comparator.Mode.SUBTRACT;
        };
    }

    public static Mode fromPaper(org.bukkit.block.data.type.Comparator.Mode mode){
        return switch (mode){
            case COMPARE -> Mode.COMPARE;
            case SUBTRACT -> Mode.SUBTRACT;
        };
    }

}
