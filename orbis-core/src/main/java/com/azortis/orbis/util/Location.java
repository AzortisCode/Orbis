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

import com.azortis.orbis.WorldAccess;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public record Location(double x, double y, double z, float yaw, float pitch,
                       @NotNull WeakReference<WorldAccess> world) {

    public boolean isWorldLoaded() {
        WorldAccess world = world().get();
        return world != null && world.isWorldLoaded();
    }

    public WorldAccess getWorld() {
        WorldAccess world = this.world.get();
        if (world == null) throw new IllegalStateException("World unloaded");
        return world;
    }

    public Location setX(double x) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    public Location setY(double y) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    public Location setZ(double z) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    public Location setCoords(double x, double y, double z) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    public Location setYaw(float yaw) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    public Location setPitch(float pitch) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    public Location setOrientation(float yaw, float pitch) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    public Location setWorld(@NotNull WorldAccess world) {
        return new Location(x, y, z, yaw, pitch, new WeakReference<>(world));
    }

    public Location copy() {
        return new Location(x, y, z, yaw, pitch, world);
    }

}
