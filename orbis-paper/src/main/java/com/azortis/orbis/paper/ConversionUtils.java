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

package com.azortis.orbis.paper;

import com.azortis.orbis.Player;
import com.azortis.orbis.util.Location;
import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public final class ConversionUtils {

    private static final PaperPlatform platform;

    static {
        platform = OrbisPlugin.getPlatform();
    }

    @NotNull
    public static Location fromNative(@NotNull org.bukkit.Location nativeLoc) {
        if (!nativeLoc.isWorldLoaded()) throw new IllegalStateException("World unloaded");
        return new Location(nativeLoc.getX(), nativeLoc.getY(), nativeLoc.getZ(),
                nativeLoc.getYaw(), nativeLoc.getPitch(),
                new WeakReference<>(platform.getWorldAccess(nativeLoc.getWorld())));
    }

    @NotNull
    public static org.bukkit.Location toNative(@NotNull Location location) {
        return new org.bukkit.Location(((PaperWorldAccess) location.getWorld()).handle(),
                location.x(), location.y(), location.z(), location.yaw(), location.pitch());
    }

    @NotNull
    public static Player.GameMode fromNative(@NotNull GameMode nativeMode) {
        return switch (nativeMode) {
            case CREATIVE -> Player.GameMode.CREATIVE;
            case SURVIVAL -> Player.GameMode.SURVIVAL;
            case ADVENTURE -> Player.GameMode.ADVENTURE;
            case SPECTATOR -> Player.GameMode.SPECTATOR;
        };
    }

    @NotNull
    public static GameMode toNative(@NotNull Player.GameMode gameMode) {
        return switch (gameMode) {
            case CREATIVE -> GameMode.CREATIVE;
            case SURVIVAL -> GameMode.SURVIVAL;
            case ADVENTURE -> GameMode.ADVENTURE;
            case SPECTATOR -> GameMode.SPECTATOR;
        };
    }

}
