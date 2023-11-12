/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2023 Azortis
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

import com.azortis.orbis.world.Heightmap;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public record PaperNativeHeightMap(@NotNull Key type, @NotNull net.minecraft.world.level.levelgen.Heightmap handle,
                                   int chunkX, int chunkZ) implements Heightmap {

    @Override
    public boolean isPersistent() {
        return false; // These are persisted any way.
    }

    @Override
    public int getFirstAvailable(int x, int z) {
        return handle.getFirstAvailable(x, z);
    }

    @Override
    public int getHighestTaken(int x, int z) {
        return handle.getHighestTaken(x, z);
    }
}