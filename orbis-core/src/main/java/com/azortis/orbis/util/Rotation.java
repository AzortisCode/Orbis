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

import org.jetbrains.annotations.NotNull;

/**
 * An enum helper class to rotate certain objects by 90 degree increments.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
public enum Rotation {
    /**
     * Represents a 0 rotation.
     */
    NONE,

    /**
     * Represents a 90-degree rotation in a negative direction on a unit circle.
     */
    CLOCKWISE,

    /**
     * Represents a 90-degree rotation in a positive direction on a unit circle.
     */
    COUNTER_CLOCKWISE,

    /**
     * Represents a 180-degree flip on a unit circle.
     */
    FLIP;

    /**
     * Get the rotation when current rotation is rotated by the specified rotation.
     *
     * @param rotation The rotation to rotate this with.
     * @return The new rotation.
     * @since 0.3-Alpha
     */
    public @NotNull Rotation rotate(@NotNull final Rotation rotation) {
        switch (rotation) {
            case NONE -> {
                return this;
            }
            case CLOCKWISE -> {
                return switch (this) {
                    case NONE -> CLOCKWISE;
                    case CLOCKWISE -> FLIP;
                    case COUNTER_CLOCKWISE -> NONE;
                    case FLIP -> COUNTER_CLOCKWISE;
                };
            }
            case COUNTER_CLOCKWISE -> {
                return switch (this) {
                    case NONE -> COUNTER_CLOCKWISE;
                    case CLOCKWISE -> NONE;
                    case COUNTER_CLOCKWISE -> FLIP;
                    case FLIP -> CLOCKWISE;
                };
            }
            case FLIP -> {
                return switch (this) {
                    case NONE -> FLIP;
                    case CLOCKWISE -> COUNTER_CLOCKWISE;
                    case COUNTER_CLOCKWISE -> CLOCKWISE;
                    case FLIP -> NONE;
                };
            }
        }
        return this;
    }

}
