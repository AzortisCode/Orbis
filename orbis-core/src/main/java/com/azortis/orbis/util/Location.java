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

import com.azortis.orbis.entity.Player;
import com.azortis.orbis.world.WorldAccess;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

/**
 * <p>Represents a location in a {@link WorldAccess} to which Entities & {@link Player}'s can be
 * teleported.</p>
 *
 * <p><b>Note</b> uses a {@link WeakReference} to a world, if the world is not present, this location will be
 * invalid for operations.</p>
 *
 * @param x     The x-coordinate.
 * @param y     The y-coordinate.
 * @param z     The z-coordinate.
 * @param yaw   The yaw rotation.
 * @param pitch The pitch rotation.
 * @param world The WorldAccess instance of the location.
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public record Location(double x, double y, double z, float yaw, float pitch,
                       @NotNull WeakReference<WorldAccess> world) {

    /**
     * Checks if the {@link WorldAccess} of this location is loaded.
     *
     * @return True if world is loaded.
     */
    @Contract(pure = true)
    public boolean isWorldLoaded() {
        WorldAccess world = world().get();
        return world != null && world.isWorldLoaded();
    }

    /**
     * <p>Returns the {@link WorldAccess} of this location.</p>
     * <p><b>NOTE</b> this method will fail if the world isn't loaded, please check {@link Location#isWorldLoaded()}
     * to make sure the world is loaded.</p>
     *
     * @return The WorldAccess of this location.
     * @throws IllegalStateException If the world isn't loaded.
     */
    @Contract(pure = true)
    public @NotNull WorldAccess getWorld() throws IllegalStateException {
        WorldAccess world = this.world.get();
        if (world == null) throw new IllegalStateException("World not loaded.");
        return world;
    }

    /**
     * Get a new {@link Location} from this instance with the specified {@link WorldAccess}.
     *
     * @param world The world to change.
     * @return A new Location object from this one with specified world.
     */
    @Contract("_ -> new")
    public @NotNull Location setWorld(@NotNull WorldAccess world) {
        return new Location(x, y, z, yaw, pitch, new WeakReference<>(world));
    }

    /**
     * Get a new {@link Location} from this instance with the specified x-coord.
     *
     * @param x The x-coord to change.
     * @return A new Location object from this one with specified x-coord.
     */
    @Contract("_ -> new")
    public @NotNull Location setX(double x) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    /**
     * Get a new {@link Location} from this instance with the specified y-coord.
     *
     * @param y The y-coord to change.
     * @return A new Location object from this one with specified y-coord.
     */
    @Contract("_ -> new")
    public @NotNull Location setY(double y) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    /**
     * Get a new {@link Location} from this instance with the specified z-coord.
     *
     * @param z The z-coord to change.
     * @return A new Location object from this one with specified x-coord.
     */
    @Contract("_ -> new")
    public @NotNull Location setZ(double z) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    /**
     * Get a new {@link Location} from this instance with the specified coords.
     *
     * @param x The x-coord to change.
     * @param y The y-coord to change.
     * @param z The z-coord to change.
     * @return A new Location object from this one with specified coords.
     */
    @Contract("_, _, _ -> new")
    public @NotNull Location setCoords(double x, double y, double z) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    /**
     * Get a new {@link Location} from this instance with the specified yaw rotation.
     *
     * @param yaw The yow rotation to change.
     * @return A new Location object from this one with specified yaw rotation.
     */
    @Contract("_ -> new")
    public @NotNull Location setYaw(float yaw) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    /**
     * Get a new {@link Location} from this instance with the specified pitch rotation.
     *
     * @param pitch The pitch rotation to change.
     * @return A new Location object from this one with specified pitch rotation.
     */
    @Contract("_ -> new")
    public @NotNull Location setPitch(float pitch) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    /**
     * Get a new {@link Location} from this instance with the specified rotations.
     *
     * @param yaw   The yow rotation to change.
     * @param pitch The pitch rotation to change.
     * @return A new Location object from this one with specified rotations.
     */
    @Contract("_, _ -> new")
    public @NotNull Location setRotation(float yaw, float pitch) {
        return new Location(x, y, z, yaw, pitch, world);
    }

    /**
     * Get the x-legacyBlock position of this {@link Location}.
     *
     * @return A floored x-coord.
     */
    @Contract(pure = true)
    public int blockX() {
        return (int) Math.floor(x);
    }

    /**
     * Get the y-legacyBlock position of this {@link Location}.
     *
     * @return A floored y-coord.
     */
    @Contract(pure = true)
    public int blockY() {
        return (int) Math.floor(y);
    }

    /**
     * Get the z-legacyBlock position of this {@link Location}.
     *
     * @return A floored z-coord.
     */
    @Contract(pure = true)
    public int blockZ() {
        return (int) Math.floor(z);
    }

    /**
     * Get the legacyBlock position of this {@link Location}.
     *
     * @return Floored coordinates that resembles a legacyBlock position.
     */
    @Contract(" -> new")
    public @NotNull BlockPos blockPos() {
        return new BlockPos(blockX(), blockY(), blockZ());
    }

    /**
     * Get the distance squared from this location to the specified location.
     *
     * @param location The other location.
     * @return The distance squared between locations.
     */
    @Contract(pure = true)
    public double distanceSq(@NotNull Location location) {
        return Math.pow(x - location.x, 2) + Math.pow(y - location.y, 2) + Math.pow(z - location.z, 2);
    }

    /**
     * <p>Get the distance from this location to the specified location.</p>
     * <p><b>NOTE</b> Uses expensive square root operation, avoid if possible.</p>
     *
     * @param location The other location.
     * @return The distance between locations.
     */
    @Contract(pure = true)
    public double distance(@NotNull Location location) {
        return Math.sqrt(distanceSq(location));
    }

    /**
     * Creates a copy of this {@link Location} object.
     *
     * @return A location instance with same parameters.
     */
    @Contract(" -> new")
    public @NotNull Location copy() {
        return new Location(x, y, z, yaw, pitch, world);
    }

}
