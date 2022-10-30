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

import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

/**
 * An enum that represents a direction in the minecraft 3d dimensions.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public enum Direction implements Nameable {
    /**
     * Represents a negative y-axis direction.
     */
    DOWN("down"),

    /**
     * Represents a positive y-axis direction.
     */
    UP("up"),

    /**
     * Represents a negative z-axis direction.
     */
    NORTH("north"),

    /**
     * Represents a positive z-axis direction.
     */
    SOUTH("south"),

    /**
     * Represents a negative x-axis direction.
     */
    WEST("west"),

    /**
     * Represents a positive x-axis direction.
     */
    EAST("east");

    /**
     * The serialized name of the direction.
     */
    private final String name;

    Direction(String name) {
        this.name = name;
    }

    /**
     * Get the String representation of this direction.
     *
     * @return The string representation of this direction.
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String serializedName() {
        return this.name;
    }

    /**
     * Gets the Direction enum from its serialized name.
     *
     * @param name The serialized name.
     * @return The corresponding enum.
     * @throws IllegalArgumentException If the provided name does not have an enum mapped to it.
     * @since 0.3-Alpha
     */
    public @NotNull Direction fromName(@NotNull String name) throws IllegalArgumentException {
        return switch (name) {
            case "down" -> DOWN;
            case "up" -> UP;
            case "north" -> NORTH;
            case "east" -> EAST;
            case "south" -> SOUTH;
            case "west" -> WEST;
            default -> throw new IllegalArgumentException(name + " is not a valid direction.");
        };
    }

    /**
     * Get the opposite of the current Direction.
     *
     * @return The opposite direction.
     * @since 0.3-Alpha
     */
    public @NotNull Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case EAST -> WEST;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case UP -> DOWN;
            case DOWN -> UP;
        };
    }

    /**
     * Get the Direction when current one is rotated by specified rotation.
     *
     * @param rotation The rotation to apply.
     * @return The rotated direction, the same if current direction is UP or DOWN.
     * @since 0.3-Alpha
     */
    public @NotNull Direction rotate(@NotNull Rotation rotation) {
        if (rotation != Rotation.NONE && !(this == UP || this == DOWN)) {
            switch (rotation) {
                case CLOCKWISE -> {
                    if (this == NORTH) return EAST;
                    else if (this == EAST) return SOUTH;
                    else if (this == SOUTH) return WEST;
                    else if (this == WEST) return NORTH;
                }
                case COUNTER_CLOCKWISE -> {
                    if (this == NORTH) return WEST;
                    else if (this == EAST) return NORTH;
                    else if (this == SOUTH) return EAST;
                    else if (this == WEST) return SOUTH;
                }
                case FLIP -> {
                    return opposite();
                }
            }
        }
        return this;
    }

}
