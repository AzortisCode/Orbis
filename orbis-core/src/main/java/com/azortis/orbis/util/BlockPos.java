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

package com.azortis.orbis.util;

import com.google.common.base.Preconditions;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A 3 dimensional integer Vector that resembles an absolute position of a block.
 *
 * @param x The x-coordinate of the position.
 * @param y The y-coordinate of the position.
 * @param z The z-coordinate of the position.
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public record BlockPos(int x, int y, int z) {

    /**
     * Absolute zero BlockPos.
     */
    public static final BlockPos ZERO = new BlockPos(0, 0, 0);

    //
    // X-axis mutations
    //

    /**
     * Get a new BlockPos with specified x-coord.
     *
     * @param x The new x-coord.
     * @return A new BlockPos with given x-coord.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos setX(final int x) {
        return new BlockPos(x, y, z);
    }

    /**
     * Get the BlockPos relative with specified distance on the x-axis to this one with.
     *
     * @param distance The amount of blocks in the x-axis.
     * @return A BlockPos relative to this one on the x-axis with specified distance.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos relativeX(final int distance) {
        return setX(x + distance);
    }

    /**
     * Get the relative BlockPos east of this one.
     *
     * @return The relative BlockPos east of this one.
     * @since 0.3-Alpha
     */
    @Contract("-> new")
    public @NotNull BlockPos east() {
        return east(1);
    }

    /**
     * Get the relative BlockPos east of this one by specified distance.
     *
     * @param distance The amount of blocks to go east.
     * @return The relative BlockPos east of this one with specified distance.
     * @throws IllegalArgumentException If distance is smaller than 1.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos east(final int distance) throws IllegalArgumentException {
        Preconditions.checkArgument(distance > 0, "Distance cannot be smaller than 1.");
        return setX(x + distance);
    }

    /**
     * Get the relative BlockPos west of this one.
     *
     * @return The relative BlockPos west of this one.
     * @since 0.3-Alpha
     */
    @Contract("-> new")
    public @NotNull BlockPos west() {
        return west(1);
    }

    /**
     * Get the relative BlockPos west of this one by specified distance.
     *
     * @param distance The amount of blocks to go west.
     * @return The relative BlockPos west of this one with specified distance.
     * @throws IllegalArgumentException If distance is smaller than 1.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos west(final int distance) throws IllegalArgumentException {
        Preconditions.checkArgument(distance > 0, "Distance cannot be smaller than 1.");
        return setX(x - distance);
    }

    //
    // Y-axis manipulations
    //

    /**
     * Get a new BlockPos with specified y-coord.
     *
     * @param y The new y-coord.
     * @return A new BlockPos with given y-coord.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos setY(final int y) {
        return new BlockPos(x, y, z);
    }

    /**
     * Get the BlockPos relative with specified distance on the y-axis to this one with.
     *
     * @param distance The amount of blocks in the y-axis.
     * @return A BlockPos relative to this one on the y-axis with specified distance.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos relativeY(final int distance) {
        return setY(y + distance);
    }

    /**
     * Get the relative BlockPos up of this one.
     *
     * @return The relative BlockPos up of this one.
     * @since 0.3-Alpha
     */
    @Contract("-> new")
    public @NotNull BlockPos up() {
        return up(1);
    }

    /**
     * Get the relative BlockPos up of this one by specified distance.
     *
     * @param distance The amount of blocks to go up.
     * @return The relative BlockPos up of this one with specified distance.
     * @throws IllegalArgumentException If distance is smaller than 1.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos up(final int distance) throws IllegalArgumentException {
        Preconditions.checkArgument(distance > 0, "Distance cannot be smaller than 1.");
        return setY(y + distance);
    }

    /**
     * Get the relative BlockPos down of this one.
     *
     * @return The relative BlockPos down of this one.
     * @since 0.3-Alpha
     */
    @Contract("-> new")
    public @NotNull BlockPos down() {
        return down(1);
    }

    /**
     * Get the relative BlockPos down of this one by specified distance.
     *
     * @param distance The amount of blocks to go down.
     * @return The relative BlockPos down of this one with specified distance.
     * @throws IllegalArgumentException If distance is smaller than 1.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos down(final int distance) throws IllegalArgumentException {
        Preconditions.checkArgument(distance > 0, "Distance cannot be smaller than 1.");
        return setY(y - distance);
    }

    //
    // Z-axis manipulations
    //

    /**
     * Get a new BlockPos with specified z-coord.
     *
     * @param z The new z-coord.
     * @return A new BlockPos with given z-coord.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos setZ(final int z) {
        return new BlockPos(x, y, z);
    }

    /**
     * Get the BlockPos relative with specified distance on the z-axis to this one with.
     *
     * @param distance The amount of blocks in the z-axis.
     * @return A BlockPos relative to this one on the z-axis with specified distance.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos relativeZ(final int distance) {
        return setZ(z + distance);
    }

    /**
     * Get the relative BlockPos north of this one.
     *
     * @return The relative BlockPos north of this one.
     * @since 0.3-Alpha
     */
    @Contract("-> new")
    public @NotNull BlockPos north() {
        return north(1);
    }

    /**
     * Get the relative BlockPos north of this one by specified distance.
     *
     * @param distance The amount of blocks to go north.
     * @return The relative BlockPos north of this one with specified distance.
     * @throws IllegalArgumentException If distance is smaller than 1.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos north(final int distance) throws IllegalArgumentException {
        Preconditions.checkArgument(distance > 0, "Distance cannot be smaller than 1.");
        return setZ(z - distance);
    }

    /**
     * Get the relative BlockPos south of this one.
     *
     * @return The relative BlockPos south of this one.
     * @since 0.3-Alpha
     */
    @Contract("-> new")
    public @NotNull BlockPos south() {
        return south(1);
    }

    /**
     * Get the relative BlockPos south of this one by specified distance.
     *
     * @param distance The amount of blocks to go south.
     * @return The relative BlockPos south of this one with specified distance.
     * @throws IllegalArgumentException If distance is smaller than 1.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos south(final int distance) throws IllegalArgumentException {
        Preconditions.checkArgument(distance > 0, "Distance cannot be smaller than 1.");
        return setZ(z + distance);
    }

    //
    // Global manipulations
    //

    /**
     * Get the relative block with the specified {@link Direction} of this one.
     *
     * @param direction The direction.
     * @return The relative block with specified direction.
     * @since 0.3-Alpha
     */
    @Contract("_ -> new")
    public @NotNull BlockPos relative(@NotNull Direction direction) {
        return relative(direction, 1);
    }

    /**
     * Get the relative block with the specified distance in the specified {@link Direction} of this one.
     *
     * @param direction The direction to go in to.
     * @param distance  The amount of blocks to go in given direction.
     * @return The relative block with specified direction and distance.
     * @throws IllegalArgumentException If the distance is smaller than 1.
     */
    @Contract("_,_ -> new")
    public @NotNull BlockPos relative(@NotNull Direction direction, final int distance) throws IllegalArgumentException {
        Preconditions.checkArgument(distance > 0, "Distance cannot be smaller than 1.");
        return switch (direction) {
            case DOWN -> new BlockPos(x, y - distance, z);
            case UP -> new BlockPos(x, y + distance, z);
            case NORTH -> new BlockPos(x, y, z - distance);
            case SOUTH -> new BlockPos(x, y, z + distance);
            case WEST -> new BlockPos(x - distance, y, z);
            case EAST -> new BlockPos(x + distance, y, z);
        };
    }

}
