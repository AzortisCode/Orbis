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

package com.azortis.orbis;

import com.azortis.orbis.entity.Player;
import com.azortis.orbis.pack.studio.Project;
import com.azortis.orbis.pack.studio.StudioWorld;
import com.azortis.orbis.util.Scheduler;
import com.azortis.orbis.world.World;
import com.azortis.orbis.world.WorldAccess;
import com.google.common.collect.ImmutableSet;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.UUID;

/**
 * The entry point for everything that is implemented on the platform.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public interface Platform {

    /**
     * Get the name of the platform adaptation. i.e. "Paper" for the PaperMC adaptation.
     *
     * @return The platform adaptation name.
     */
    @NotNull
    String adaptation();

    @NotNull
    Logger logger();

    /**
     * Get the directory where Orbis is located.
     *
     * @return The orbis root directory.
     */
    @NotNull
    File directory();

    /**
     * Get the instance of the platform {@link Scheduler}.
     *
     * @return The platform scheduler.
     * @since 0.3-Alpha
     */
    @NotNull
    Scheduler scheduler();

    @Nullable
    World getWorld(@NotNull String name);

    @NotNull
    ImmutableSet<World> worlds();

    @Nullable
    WorldAccess getWorldAccess(@NotNull String name);

    @NotNull
    ImmutableSet<WorldAccess> worldAccesses();

    @NotNull
    StudioWorld createStudioWorld(@NotNull Project project);

    @Nullable
    Player getPlayer(@NotNull UUID uuid);

    @NotNull
    ImmutableSet<Player> getPlayers();

    @Nullable
    Class<?> mainClass();

    @NotNull
    Class<? extends Settings> settingsClass();

    @NotNull
    Settings defaultSettings();

}
