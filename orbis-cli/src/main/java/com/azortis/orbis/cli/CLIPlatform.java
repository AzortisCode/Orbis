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

package com.azortis.orbis.cli;

import com.azortis.orbis.Platform;
import com.azortis.orbis.Settings;
import com.azortis.orbis.entity.Player;
import com.azortis.orbis.pack.studio.Project;
import com.azortis.orbis.pack.studio.StudioWorld;
import com.azortis.orbis.util.Scheduler;
import com.azortis.orbis.world.World;
import com.azortis.orbis.world.WorldAccess;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.UUID;

public final class CLIPlatform implements Platform {

    @Override
    public @NotNull String adaptation() {
        return null;
    }

    @Override
    public @NotNull Logger logger() {
        return null;
    }

    @Override
    public @NotNull File directory() {
        return null;
    }

    @Override
    public @NotNull Scheduler scheduler() {
        return null;
    }

    @Override
    public @Nullable World getWorld(@NotNull String name) {
        return null;
    }

    @Override
    public @NotNull ImmutableSet<World> worlds() {
        return null;
    }

    @Override
    public @Nullable WorldAccess getWorldAccess(@NotNull String name) {
        return null;
    }

    @Override
    public @NotNull ImmutableSet<WorldAccess> worldAccesses() {
        return null;
    }

    @Override
    public @NotNull StudioWorld createStudioWorld(@NotNull Project project) {
        return null;
    }

    @Override
    public @Nullable Player getPlayer(@NotNull UUID uuid) {
        return null;
    }

    @Override
    public @NotNull ImmutableSet<Player> getPlayers() {
        return null;
    }

    @Override
    public @Nullable Class<?> mainClass() {
        return null;
    }

    @Override
    public @NotNull Class<? extends Settings> settingsClass() {
        return null;
    }

    @Override
    public @NotNull Settings defaultSettings() {
        return null;
    }
}
