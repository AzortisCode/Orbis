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

import com.azortis.orbis.block.BlockFace;
import com.azortis.orbis.block.data.MultipleFacing;
import com.azortis.orbis.paper.block.BlockAdapter;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class PaperMultipleFacing extends PaperBlockData implements MultipleFacing {

    public PaperMultipleFacing(BlockData handle) {
        super(handle);
    }

    @Override
    public boolean hasFace(@NotNull BlockFace face) {
        return ((org.bukkit.block.data.MultipleFacing) getHandle()).hasFace(BlockAdapter.toPaper(face));
    }

    @Override
    public void setFace(@NotNull BlockFace face, boolean has) {
        ((org.bukkit.block.data.MultipleFacing) getHandle()).setFace(BlockAdapter.toPaper(face), has);
    }

    @Override
    public @NotNull Set<BlockFace> getFaces() {
        return ((org.bukkit.block.data.MultipleFacing) getHandle()).getFaces().stream()
                .map(BlockAdapter::fromPaper).collect(Collectors.toSet());
    }

    @Override
    public @NotNull Set<BlockFace> getAllowedFaces() {
        return ((org.bukkit.block.data.MultipleFacing) getHandle()).getAllowedFaces().stream()
                .map(BlockAdapter::fromPaper).collect(Collectors.toSet());
    }
}
