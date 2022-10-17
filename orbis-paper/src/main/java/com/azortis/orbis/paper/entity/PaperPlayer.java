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

package com.azortis.orbis.paper.entity;

import com.azortis.orbis.entity.Player;
import com.azortis.orbis.paper.OrbisPlugin;
import com.azortis.orbis.paper.PaperPlatform;
import com.azortis.orbis.paper.util.ConversionUtils;
import com.azortis.orbis.util.Location;
import com.azortis.orbis.world.WorldAccess;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PaperPlayer implements Player {

    private static final PaperPlatform platform = OrbisPlugin.getPlatform();
    private final org.bukkit.entity.Player handle;

    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.paper")
    public PaperPlayer(org.bukkit.entity.Player handle) {
        this.handle = handle;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return handle.hasPermission(permission);
    }

    @Override
    public void kick() {
        handle.kick();
    }

    @Override
    public void kick(@NotNull Component component) {
        handle.kick(component);
    }

    @Override
    public boolean teleport(@NotNull Location location) {
        return handle.teleport(ConversionUtils.toPaper(location));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> teleportAsync(@NotNull Location location) {
        return handle.teleportAsync(ConversionUtils.toPaper(location));
    }

    @Override
    public @NotNull GameMode getGameMode() {
        return ConversionUtils.fromPaper(handle.getGameMode());
    }

    @Override
    public void setGameMode(@NotNull GameMode gameMode) {
        handle.setGameMode(ConversionUtils.toPaper(gameMode));
    }

    @Override
    public boolean canFly() {
        return handle.getAllowFlight();
    }

    @Override
    public void setAllowFlying(boolean allowFlying) {
        handle.setAllowFlight(allowFlying);
    }

    @Override
    public @NotNull Location getLocation() {
        return ConversionUtils.fromPaper(handle.getLocation());
    }

    @Override
    public @NotNull WorldAccess getWorld() {
        WorldAccess worldAccess = platform.getWorldAccess(handle.getWorld());
        if (worldAccess == null)
            throw new IllegalStateException("The world the player is in, does not have a WorldAccess instance loaded!");
        return worldAccess;
    }

    @Override
    public @NotNull Audience audience() {
        return handle;
    }

    public org.bukkit.entity.Player handle() {
        return handle;
    }

}
