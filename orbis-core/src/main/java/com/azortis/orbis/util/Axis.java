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
 * An enum that represents an object that is aligned to a certain axis.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public enum Axis implements Nameable {
    /**
     * Represents an alignment on the x-axis.
     */
    X("x"),

    /**
     * Represents an alignment on the y-axis.
     */
    Y("y"),

    /**
     * Represents an alignment on the z-axis.
     */
    Z("z");

    /**
     * The serialized name of the axis.
     */
    private final String name;

    Axis(String name) {
        this.name = name;
    }

    /**
     * Get the String representation of this axis.
     *
     * @return The string representation of this axis.
     * @since 0.3-Alpha
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
     * Get the axis when the current one is rotated by specified rotation.
     *
     * @param rotation The rotation to apply.
     * @return The rotated axis, same if the current Axis is Y or when the specified rotation is NONE or FLIP.
     * @since 0.3-Alpha
     */
    public @NotNull Axis rotate(@NotNull Rotation rotation) {
        if (this == Y || rotation == Rotation.NONE || rotation == Rotation.FLIP) return this;
        return switch (this) {
            case X -> Z;
            case Z -> X;
            default -> throw new IllegalStateException("This shouldn't happen.");
        };
    }

}
