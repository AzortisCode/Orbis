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
import com.azortis.orbis.block.data.type.BigDripleaf;
import com.azortis.orbis.paper.block.BlockAdapter;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class PaperBigDripleaf extends PaperBlockData implements BigDripleaf {

    public PaperBigDripleaf(BlockData handle) {
        super(handle);
    }

    @Override
    public @NotNull BlockFace getFacing() {
        return BlockAdapter.fromPaper(((org.bukkit.block.data.type.BigDripleaf) getHandle()).getFacing());
    }

    @Override
    public void setFacing(@NotNull BlockFace facing) {
        ((org.bukkit.block.data.type.BigDripleaf) getHandle()).setFacing(BlockAdapter.toPaper(facing));
    }

    @Override
    public @NotNull Set<BlockFace> getFaces() {
        return ((org.bukkit.block.data.type.BigDripleaf) getHandle()).getFaces().stream().map(BlockAdapter::fromPaper)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isWaterLogged() {
        return ((org.bukkit.block.data.type.BigDripleaf) getHandle()).isWaterlogged();
    }

    @Override
    public void setWaterLogged(boolean waterLogged) {
        ((org.bukkit.block.data.type.BigDripleaf) getHandle()).setWaterlogged(waterLogged);
    }

    @Override
    public @NotNull Tilt getTilt() {
        return fromPaper(((org.bukkit.block.data.type.BigDripleaf) getHandle()).getTilt());
    }

    @Override
    public void setTilt(@NotNull Tilt tilt) {
        ((org.bukkit.block.data.type.BigDripleaf) getHandle()).setTilt(toPaper(tilt));
    }

    public static org.bukkit.block.data.type.BigDripleaf.Tilt toPaper(Tilt tilt){
        return switch (tilt){
            case NONE -> org.bukkit.block.data.type.BigDripleaf.Tilt.NONE;
            case UNSTABLE -> org.bukkit.block.data.type.BigDripleaf.Tilt.UNSTABLE;
            case PARTIAL -> org.bukkit.block.data.type.BigDripleaf.Tilt.PARTIAL;
            case FULL -> org.bukkit.block.data.type.BigDripleaf.Tilt.FULL;
        };
    }

    public static Tilt fromPaper(org.bukkit.block.data.type.BigDripleaf.Tilt tilt){
        return switch (tilt){
            case NONE -> Tilt.NONE;
            case UNSTABLE -> Tilt.UNSTABLE;
            case PARTIAL -> Tilt.PARTIAL;
            case FULL -> Tilt.FULL;
        };
    }

}
