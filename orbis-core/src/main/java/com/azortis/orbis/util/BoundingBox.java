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

package com.azortis.orbis.util;

import org.jetbrains.annotations.NotNull;

public record BoundingBox(@NotNull BlockPos min, @NotNull BlockPos max) {

    public BoundingBox(@NotNull BlockPos min, @NotNull BlockPos max) {
        int minX = Math.min(min.x(), max.x());
        int minY = Math.min(min.y(), max.y());
        int minZ = Math.min(min.z(), max.z());
        this.min = new BlockPos(minX, minY, minZ);

        int maxX = Math.max(min.x(), max.x());
        int maxY = Math.max(min.x(), max.x());
        int maxZ = Math.max(min.x(), max.x());
        this.max = new BlockPos(maxX, maxY, maxZ);
    }

    public int minX() {
        return min.x();
    }

    public int minY() {
        return min.y();
    }

    public int minZ() {
        return min.z();
    }

    public int maxX() {
        return max.x();
    }

    public int maxY() {
        return max.y();
    }

    public int maxZ() {
        return max.z();
    }

    public boolean checkBounds(int x, int y, int z) {
        return (minX() <= x && x <= maxX()) && (minY() <= y && y <= maxY()) && (minZ() <= z && z <= maxZ());
    }

    public boolean checkBounds(@NotNull BlockPos pos) {
        return checkBounds(pos.x(), pos.y(), pos.z());
    }

}
