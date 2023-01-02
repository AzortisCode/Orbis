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

package com.azortis.orbis.world;

import com.azortis.orbis.block.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public interface ChunkAccess {

    /**
     * Checks if the underlying Chunk instance is loaded, if not any reference
     * to this instance should be discarded.
     *
     * @return If the underlying Chunk is loaded.
     */
    boolean isLoaded();

    /**
     * Unloads this Chunk if it was force loaded, won't do anything if the Chunk got loaded naturally.
     */
    void unload();

    @NotNull BlockState getState(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z);

    void setState(@Range(from = 0, to = 15) int x, int y, @Range(from = 0, to = 15) int z, @Nullable BlockState state);

}
