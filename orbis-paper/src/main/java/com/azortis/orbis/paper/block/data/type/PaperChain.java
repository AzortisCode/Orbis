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

import com.azortis.orbis.block.Axis;
import com.azortis.orbis.block.data.type.Chain;
import com.azortis.orbis.paper.block.BlockAdapter;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class PaperChain extends PaperBlockData implements Chain {

    public PaperChain(BlockData handle) {
        super(handle);
    }

    @Override
    public @NotNull Axis getAxis() {
        return BlockAdapter.fromPaper(((org.bukkit.block.data.type.Chain) getHandle()).getAxis());
    }

    @Override
    public void setAxis(@NotNull Axis axis) {
        ((org.bukkit.block.data.type.Chain) getHandle()).setAxis(BlockAdapter.toPaper(axis));
    }

    @Override
    public @NotNull Set<Axis> getAxes() {
        return ((org.bukkit.block.data.type.Chain) getHandle()).getAxes().stream().map(BlockAdapter::fromPaper)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isWaterLogged() {
        return ((org.bukkit.block.data.type.Chain) getHandle()).isWaterlogged();
    }

    @Override
    public void setWaterLogged(boolean waterLogged) {
        ((org.bukkit.block.data.type.Chain) getHandle()).setWaterlogged(waterLogged);
    }

}
