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

package com.azortis.orbis.block;

import com.azortis.orbis.block.property.*;
import com.azortis.orbis.util.Axis;
import com.azortis.orbis.util.Direction;
import com.azortis.orbis.util.Rotation;
import com.google.common.collect.ImmutableMap;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * <p>A representation of a {@link Block} in a state based on the values of the block
 * its properties. This will only affect visuals, for more advanced data see {@link com.azortis.orbis.block.entity.BlockEntity}</p>
 *
 * @author Jake Nijssen
 * @see <a href="https://minecraft.fandom.com/wiki/Block_states#:~:text=Block%20states%20(also%20known%20as,Metadata)%20to%20define%20a%20block.">BlockStates Wiki</a>
 * for more information..
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public non-sealed interface BlockState extends ConfiguredBlock {

    /**
     * Check if this state is air.
     *
     * @return If the state is air.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean isAir();

    /**
     * Check if this state is flammable.
     *
     * @return If the state is flammable.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean isFlammable();

    /**
     * Check if this state is liquid.
     *
     * @return If the state is liquid.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean isLiquid();

    /**
     * Check if this state is replaceable.
     *
     * @return If the state is replaceable.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean isReplaceable();

    /**
     * Check if the state is solid.
     *
     * @return If the state is solid.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean isSolid();

    /**
     * Check if the state is colliding.
     *
     * @return If the state is colliding.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean isColliding();

    /**
     * Get the values of this state for the {@link Block} its {@link Property}'s
     *
     * @return An immutable view of the properties and its values for this state.
     * @since 0.3-Alpha
     */
    @Contract(" -> new")
    @NotNull
    ImmutableMap<Property<?>, Comparable<?>> values();

    /**
     * Get the value of this {@link BlockState} of the given property.
     * <p><b>NOTE</b> the specified property must be existent on this {@link BlockState#block()} otherwise
     * this method will throw an {@link IllegalArgumentException}</p>
     *
     * @param property The property to get the value for.
     * @param <T>      The type of the property.
     * @return The value of the given property for this state.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    <T extends Comparable<T>> @NotNull T get(@NotNull Property<T> property);

    /**
     * Get the value of this {@link BlockState} of the given property wrapped in a {@link Optional},
     * which will be empty if the Block doesn't have this property.
     *
     * @param property The property to get the value for.
     * @param <T>      The type of the property.
     * @return The value of the given property for this state.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    <T extends Comparable<T>> @NotNull Optional<T> getOptional(@NotNull Property<T> property);

    /**
     * Get the {@link BlockState} if specified {@link Property} is changed to the specified value.
     * <p><b>NOTE</b> the specified property must be existent on this {@link BlockState#block()} & the property must contain
     * specified value otherwise this method will throw an {@link IllegalArgumentException}</p>
     *
     * @param property The property to change.
     * @param value    The value of the property.
     * @param <T>      The type of the property.
     * @param <V>      The type of the value.
     * @return The BlockState for the block after you set this value.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull <T extends Comparable<T>, V extends T> BlockState set(@NotNull Property<T> property, @NotNull V value);

    /**
     * Get the {@link BlockState} if specified {@link Property} is changed to the specified value.
     * <p><b>NOTE</b> the specified property must be existent on this {@link BlockState#block()} & the property must contain
     * specified value otherwise this method will throw an {@link IllegalArgumentException}</p>
     *
     * @param property The property to change.
     * @param value    A string representation of the property value.
     * @return The BlockState for the block after you set this value.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull
    BlockState set(@NotNull Property<?> property, @NotNull String value);

    /**
     * Gets the BlockState if the current state is rotated by specified rotation.
     *
     * @param rotation The rotation to apply.
     * @return The rotated BlockState instance, if this block has rotatable properties.
     * @since 0.3-Alpha
     */
    @API(status = API.Status.EXPERIMENTAL)
    @Contract(pure = true)
    @NotNull
    default BlockState rotate(@NotNull Rotation rotation) {
        if ((rotation == Rotation.NONE || block().properties().size() == 0)) {
            BlockState rotatedState = this;

            // If a block is aligned to the x or z axis, and the rotation is 90 degrees, we flip it.
            if (block().hasProperty(Properties.AXIS)) {
                Axis axis = rotatedState.get(Properties.AXIS);
                if (axis != Axis.Y && (rotation == Rotation.CLOCKWISE || rotation == Rotation.COUNTER_CLOCKWISE)) {
                    axis = axis == Axis.X ? Axis.Z : Axis.X;
                    rotatedState = rotatedState.set(Properties.AXIS, axis);
                }
            }
            if (block().hasProperty(Properties.HORIZONTAL_AXIS)) {
                Axis axis = rotatedState.get(Properties.HORIZONTAL_AXIS);
                if (axis != Axis.Y && (rotation == Rotation.CLOCKWISE || rotation == Rotation.COUNTER_CLOCKWISE)) {
                    axis = axis == Axis.X ? Axis.Z : Axis.X;
                    rotatedState = rotatedState.set(Properties.HORIZONTAL_AXIS, axis);
                }
            }

            // North, East, South, West booleans, we can assume a block has all if they have one.
            if (block().hasProperty(Properties.NORTH)) {
                boolean north = rotatedState.get(Properties.NORTH);
                boolean east = rotatedState.get(Properties.EAST);
                boolean south = rotatedState.get(Properties.SOUTH);
                boolean west = rotatedState.get(Properties.WEST);

                // No need to rotate if all are enabled
                if (!(north && east && south && west)) {
                    boolean rotatedNorth = false, rotatedEast = false, rotatedSouth = false, rotatedWest = false;

                    switch (rotation) {
                        case CLOCKWISE -> {
                            if (north) rotatedEast = true;
                            if (east) rotatedSouth = true;
                            if (south) rotatedWest = true;
                            if (west) rotatedNorth = true;
                        }
                        case COUNTER_CLOCKWISE -> {
                            if (north) rotatedWest = true;
                            if (east) rotatedNorth = true;
                            if (south) rotatedEast = true;
                            if (west) rotatedSouth = true;
                        }
                        case FLIP -> {
                            if (north) rotatedSouth = true;
                            if (east) rotatedWest = true;
                            if (south) rotatedNorth = true;
                            if (west) rotatedEast = true;
                        }
                        default -> throw new IllegalStateException("A none rotation shouldn't reach this statement!");
                    }
                    rotatedState = rotatedState.set(Properties.NORTH, rotatedNorth);
                    rotatedState = rotatedState.set(Properties.EAST, rotatedEast);
                    rotatedState = rotatedState.set(Properties.SOUTH, rotatedSouth);
                    rotatedState = rotatedState.set(Properties.WEST, rotatedWest);
                }
            }

            // North, East, South, West redstone enums, we can assume it has one, it will have all.
            if (block().hasProperty(Properties.NORTH_REDSTONE)) {
                RedstoneSide north = rotatedState.get(Properties.NORTH_REDSTONE);
                RedstoneSide east = rotatedState.get(Properties.EAST_REDSTONE);
                RedstoneSide south = rotatedState.get(Properties.SOUTH_REDSTONE);
                RedstoneSide west = rotatedState.get(Properties.WEST_REDSTONE);

                // If north is the same as all other sides, we can don't have to do any rotation.
                if (!(north == east && north == south && north == west)) {
                    RedstoneSide rotatedNorth, rotatedEast, rotatedSouth, rotatedWest;

                    switch (rotation) {
                        case CLOCKWISE -> {
                            rotatedEast = north;
                            rotatedSouth = east;
                            rotatedWest = south;
                            rotatedNorth = west;
                        }
                        case COUNTER_CLOCKWISE -> {
                            rotatedEast = south;
                            rotatedSouth = west;
                            rotatedWest = north;
                            rotatedNorth = east;
                        }
                        case FLIP -> {
                            rotatedEast = west;
                            rotatedSouth = north;
                            rotatedWest = east;
                            rotatedNorth = south;
                        }
                        default -> throw new IllegalStateException("A none rotation shouldn't reach this statement!");
                    }

                    rotatedState = rotatedState.set(Properties.NORTH_REDSTONE, rotatedNorth);
                    rotatedState = rotatedState.set(Properties.EAST_REDSTONE, rotatedEast);
                    rotatedState = rotatedState.set(Properties.SOUTH_REDSTONE, rotatedSouth);
                    rotatedState = rotatedState.set(Properties.WEST_REDSTONE, rotatedWest);
                }
            }

            // North, East, South, West wall enums, we can assume it has one, it will have all.
            if (block().hasProperty(Properties.NORTH_WALL)) {
                WallSide north = rotatedState.get(Properties.NORTH_WALL);
                WallSide east = rotatedState.get(Properties.EAST_WALL);
                WallSide south = rotatedState.get(Properties.SOUTH_WALL);
                WallSide west = rotatedState.get(Properties.WEST_WALL);

                // If north is the same as all other sides, we can don't have to do any rotation.
                if (!(north == east && north == south && north == west)) {
                    WallSide rotatedNorth, rotatedEast, rotatedSouth, rotatedWest;

                    switch (rotation) {
                        case CLOCKWISE -> {
                            rotatedEast = north;
                            rotatedSouth = east;
                            rotatedWest = south;
                            rotatedNorth = west;
                        }
                        case COUNTER_CLOCKWISE -> {
                            rotatedEast = south;
                            rotatedSouth = west;
                            rotatedWest = north;
                            rotatedNorth = east;
                        }
                        case FLIP -> {
                            rotatedEast = west;
                            rotatedSouth = north;
                            rotatedWest = east;
                            rotatedNorth = south;
                        }
                        default -> throw new IllegalStateException("A none rotation shouldn't reach this statement!");
                    }

                    rotatedState = rotatedState.set(Properties.NORTH_WALL, rotatedNorth);
                    rotatedState = rotatedState.set(Properties.EAST_WALL, rotatedEast);
                    rotatedState = rotatedState.set(Properties.SOUTH_WALL, rotatedSouth);
                    rotatedState = rotatedState.set(Properties.WEST_WALL, rotatedWest);
                }
            }

            // The facing properties.
            if (block().hasProperty(Properties.FACING)) {
                rotatedState = rotatedState.set(Properties.FACING, rotatedState.get(Properties.FACING).rotate(rotation));
            }
            if (block().hasProperty(Properties.HORIZONTAL_FACING)) {
                rotatedState = rotatedState.set(Properties.HORIZONTAL_FACING, rotatedState.get(Properties.HORIZONTAL_FACING).rotate(rotation));
            }
            if (block().hasProperty(Properties.FACING_HOPPER)) {
                Direction facing = rotatedState.get(Properties.FACING_HOPPER);
                if (facing != Direction.DOWN) {
                    rotatedState = rotatedState.set(Properties.FACING_HOPPER, facing.rotate(rotation));
                }
            }

            // TODO actually do this, but this enum is cursed af, and probably never used.
            if (block().hasProperty(Properties.ORIENTATION)) {
                throw new UnsupportedOperationException("Jigsaw block rotation is not yet supported.");
            }

            // Rail shape properties. For normal rails
            if (block().hasProperty(Properties.RAIL_SHAPE)) {
                RailShape shape = rotatedState.get(Properties.RAIL_SHAPE);

                switch (rotation) {
                    case CLOCKWISE -> {
                        switch (shape) {
                            case NORTH_SOUTH -> shape = RailShape.EAST_WEST;
                            case EAST_WEST -> shape = RailShape.NORTH_SOUTH;
                            case ASCENDING_NORTH -> shape = RailShape.ASCENDING_EAST;
                            case ASCENDING_EAST -> shape = RailShape.ASCENDING_SOUTH;
                            case ASCENDING_SOUTH -> shape = RailShape.ASCENDING_WEST;
                            case ASCENDING_WEST -> shape = RailShape.ASCENDING_NORTH;
                            case NORTH_EAST -> shape = RailShape.SOUTH_EAST;
                            case NORTH_WEST -> shape = RailShape.NORTH_EAST;
                            case SOUTH_EAST -> shape = RailShape.SOUTH_WEST;
                            case SOUTH_WEST -> shape = RailShape.NORTH_WEST;
                        }
                    }
                    case COUNTER_CLOCKWISE -> {
                        switch (shape) {
                            case NORTH_SOUTH -> shape = RailShape.EAST_WEST;
                            case EAST_WEST -> shape = RailShape.NORTH_SOUTH;
                            case ASCENDING_NORTH -> shape = RailShape.ASCENDING_WEST;
                            case ASCENDING_EAST -> shape = RailShape.ASCENDING_NORTH;
                            case ASCENDING_SOUTH -> shape = RailShape.ASCENDING_EAST;
                            case ASCENDING_WEST -> shape = RailShape.ASCENDING_SOUTH;
                            case NORTH_EAST -> shape = RailShape.NORTH_WEST;
                            case NORTH_WEST -> shape = RailShape.SOUTH_WEST;
                            case SOUTH_EAST -> shape = RailShape.NORTH_EAST;
                            case SOUTH_WEST -> shape = RailShape.SOUTH_EAST;
                        }
                    }
                    case FLIP -> {
                        switch (shape) {
                            case ASCENDING_NORTH -> shape = RailShape.ASCENDING_SOUTH;
                            case ASCENDING_EAST -> shape = RailShape.ASCENDING_WEST;
                            case ASCENDING_SOUTH -> shape = RailShape.ASCENDING_NORTH;
                            case ASCENDING_WEST -> shape = RailShape.ASCENDING_EAST;
                            case NORTH_EAST -> shape = RailShape.SOUTH_WEST;
                            case NORTH_WEST -> shape = RailShape.SOUTH_EAST;
                            case SOUTH_EAST -> shape = RailShape.NORTH_WEST;
                            case SOUTH_WEST -> shape = RailShape.NORTH_EAST;
                        }
                    }
                    default -> throw new IllegalStateException("A none rotation shouldn't reach this statement!");
                }
                rotatedState = rotatedState.set(Properties.RAIL_SHAPE, shape);
            }

            // Rail shape properties, For special rails which can only be straight.
            if (block().hasProperty(Properties.RAIL_SHAPE_STRAIGHT)) {
                RailShape shape = rotatedState.get(Properties.RAIL_SHAPE_STRAIGHT);

                switch (rotation) {
                    case CLOCKWISE -> {
                        switch (shape) {
                            case NORTH_SOUTH -> shape = RailShape.EAST_WEST;
                            case EAST_WEST -> shape = RailShape.NORTH_SOUTH;
                            case ASCENDING_NORTH -> shape = RailShape.ASCENDING_EAST;
                            case ASCENDING_EAST -> shape = RailShape.ASCENDING_SOUTH;
                            case ASCENDING_SOUTH -> shape = RailShape.ASCENDING_WEST;
                            case ASCENDING_WEST -> shape = RailShape.ASCENDING_NORTH;
                        }
                    }
                    case COUNTER_CLOCKWISE -> {
                        switch (shape) {
                            case NORTH_SOUTH -> shape = RailShape.EAST_WEST;
                            case EAST_WEST -> shape = RailShape.NORTH_SOUTH;
                            case ASCENDING_NORTH -> shape = RailShape.ASCENDING_WEST;
                            case ASCENDING_EAST -> shape = RailShape.ASCENDING_NORTH;
                            case ASCENDING_SOUTH -> shape = RailShape.ASCENDING_EAST;
                            case ASCENDING_WEST -> shape = RailShape.ASCENDING_SOUTH;
                        }
                    }
                    case FLIP -> {
                        switch (shape) {
                            case ASCENDING_NORTH -> shape = RailShape.ASCENDING_SOUTH;
                            case ASCENDING_EAST -> shape = RailShape.ASCENDING_WEST;
                            case ASCENDING_SOUTH -> shape = RailShape.ASCENDING_NORTH;
                            case ASCENDING_WEST -> shape = RailShape.ASCENDING_EAST;
                        }
                    }
                    default -> throw new IllegalStateException("A none rotation shouldn't reach this statement!");
                }
                rotatedState = rotatedState.set(Properties.RAIL_SHAPE_STRAIGHT, shape);
            }

            // Rotation for the rotation int property
            if (block().hasProperty(Properties.ROTATION)) {
                int i = rotatedState.get(Properties.ROTATION);

                // A rotation consists of 16 possible states, with 4 per 90-degree turn.
                switch (rotation) {
                    case CLOCKWISE -> i = i + 4;
                    case COUNTER_CLOCKWISE -> i = i - 4;
                    case FLIP -> i = i + 8;
                    default -> throw new IllegalStateException("A none rotation shouldn't reach this statement!");
                }
                if (i > 15) i = i - 16;
                else if (i < 0) i = i + 16;
                rotatedState = rotatedState.set(Properties.ROTATION, i);
            }

            return rotatedState;
        }
        return this;
    }

}
