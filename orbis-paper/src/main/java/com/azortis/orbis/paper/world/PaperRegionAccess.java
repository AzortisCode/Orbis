/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2022 Azortis
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

package com.azortis.orbis.paper.world;

import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.block.Blocks;
import com.azortis.orbis.exception.CoordsOutOfBoundsException;
import com.azortis.orbis.paper.util.ConversionUtils;
import com.azortis.orbis.util.BoundingBox;
import com.azortis.orbis.world.ChunkAccess;
import com.azortis.orbis.world.RegionAccess;
import com.azortis.orbis.world.WorldAccess;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Set;

public final class PaperRegionAccess implements RegionAccess {

    private final LimitedRegion handle;

    public PaperRegionAccess(final LimitedRegion handle) {
        this.handle = handle;
    }

    @Override
    public @NotNull WorldAccess world() {
        return new PaperWorldAccess(handle.getWorld());
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public void unload() {
    }

    @Override
    public @NotNull @UnmodifiableView Set<ChunkAccess> chunks() {
        return Set.of();
    }

    @Override
    public @NotNull BoundingBox bounds() {
        // TODO: There isn't really a good way to get the bounds of a region.
        return null;
    }

    @Override
    public @NotNull BlockState getState(int x, int y, int z) throws CoordsOutOfBoundsException {
        return ConversionUtils.fromPaper(handle.getBlockData(x, y, z));
    }

    @Override
    public void setState(int x, int y, int z, @Nullable BlockState state) throws CoordsOutOfBoundsException {
        final BlockData data = state == null ? ConversionUtils.toPaper(Blocks.AIR.defaultState()) : ConversionUtils.toPaper(state);
        handle.setBlockData(x, y, z, data);
    }
}
