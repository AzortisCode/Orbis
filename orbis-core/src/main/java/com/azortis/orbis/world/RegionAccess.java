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

package com.azortis.orbis.world;

import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.exception.CoordsOutOfBoundsException;
import com.azortis.orbis.util.BlockPos;
import com.azortis.orbis.util.BoundingBox;
import com.azortis.orbis.util.annotations.AbsoluteCoords;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Set;

/**
 * Represents a loaded region of a world. Which can be safely accessed without delays.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.EXPERIMENTAL, since = "0.3-Alpha")
public interface RegionAccess {

    @Contract(pure = true)
    @NotNull WorldAccess world();

    boolean isLoaded();

    void unload();

    /**
     * Get all the Chunks that are loaded for this region access.
     *
     * @return An immutable set of {@link ChunkAccess} in this region.
     */
    @Contract(pure = true)
    @NotNull @UnmodifiableView Set<ChunkAccess> chunks();

    @Contract(pure = true)
    @NotNull BoundingBox bounds();

    /**
     * Get the minimum position of this region.
     *
     * @return The minimum position of this region.
     */
    @Contract(pure = true)
    default @NotNull BlockPos min() {
        return bounds().min();
    }

    @Contract(pure = true)
    default int minX() {
        return min().x();
    }

    @Contract(pure = true)
    default int minY() {
        return min().y();
    }

    @Contract(pure = true)
    default int minZ() {
        return min().z();
    }

    /**
     * Get the maximum position of this region.
     *
     * @return The maximum position of this region.
     */
    @Contract(pure = true)
    default @NotNull BlockPos max() {
        return bounds().max();
    }

    @Contract(pure = true)
    default int maxX() {
        return max().x();
    }

    @Contract(pure = true)
    default int maxY() {
        return max().y();
    }

    @Contract(pure = true)
    default int maxZ() {
        return max().z();
    }

    @AbsoluteCoords
    @Contract(pure = true)
    default boolean checkBounds(int x, int y, int z) {
        return bounds().checkBounds(x, y, z);
    }

    /**
     * Check if the given position is within bounds.
     *
     * @param pos
     * @return
     */
    @Contract(pure = true)
    default boolean checkBounds(@NotNull BlockPos pos) {
        return bounds().checkBounds(pos);
    }

    /**
     * Get the {@link BlockState} at specified world coordinates.
     *
     * @param x The x-coord of the block.
     * @param y The y-coord of the block.
     * @param z The z-coord of the block.
     * @return The blockState at specified coordinates.
     * @throws CoordsOutOfBoundsException If the coordinates are not within the bounds of this region.
     */
    @Contract(pure = true)
    @AbsoluteCoords
    @NotNull BlockState getState(int x, int y, int z) throws CoordsOutOfBoundsException;

    /**
     * Get the {@link BlockState} at specified world position.
     *
     * @param pos The position of the block.
     * @return The blockState at the specified position.
     * @throws CoordsOutOfBoundsException If the position is not within the bounds of this region.
     */
    @Contract(pure = true)
    @NotNull BlockState getState(@NotNull BlockPos pos) throws CoordsOutOfBoundsException;

    /**
     * Set the {@link BlockState} at specified world coordinates.
     *
     * @param x     The x-coord of the block.
     * @param y     The y-coord of the block.
     * @param z     The z-coord of the block.
     * @param state The block state to set at specified coordinates.
     * @throws CoordsOutOfBoundsException If the coordinates are not within the bounds of this region.
     */
    @AbsoluteCoords
    void setState(int x, int y, int z, @Nullable BlockState state) throws CoordsOutOfBoundsException;

    @Contract(pure = true)
    void setState(@NotNull BlockPos pos, @Nullable BlockState state) throws CoordsOutOfBoundsException;

}
