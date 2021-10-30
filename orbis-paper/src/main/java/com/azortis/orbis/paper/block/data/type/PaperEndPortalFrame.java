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
import com.azortis.orbis.block.data.type.EndPortalFrame;
import com.azortis.orbis.paper.block.BlockAdapter;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class PaperEndPortalFrame extends PaperBlockData implements EndPortalFrame {

    public PaperEndPortalFrame(BlockData handle) {
        super(handle);
    }

    @Override
    public @NotNull BlockFace getFacing() {
        return BlockAdapter.fromPaper(((org.bukkit.block.data.type.EndPortalFrame) getHandle()).getFacing());
    }

    @Override
    public void setFacing(@NotNull BlockFace facing) {
        ((org.bukkit.block.data.type.EndPortalFrame) getHandle()).setFacing(BlockAdapter.toPaper(facing));
    }

    @Override
    public @NotNull Set<BlockFace> getFaces() {
        return ((org.bukkit.block.data.type.EndPortalFrame) getHandle()).getFaces().stream().map(BlockAdapter::fromPaper)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean hasEye() {
        return ((org.bukkit.block.data.type.EndPortalFrame) getHandle()).hasEye();
    }

    @Override
    public void setEye(boolean has) {
        ((org.bukkit.block.data.type.EndPortalFrame) getHandle()).setEye(has);
    }

}
