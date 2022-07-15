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

import com.azortis.orbis.item.ItemFactory;
import com.azortis.orbis.pack.studio.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.UUID;

public class MockPlatform implements Platform {

    private final File directory;

    public MockPlatform() {
        this.directory = new File(System.getProperty("user.dir") + "/test-data/");
    }

    @Override
    public @NotNull String adaptation() {
        return "Test";
    }

    @Override
    public org.slf4j.@NotNull Logger logger() {
        return LoggerFactory.getLogger(BlocksTest.class);
    }

    @Override
    public @NotNull File directory() {
        return this.directory;
    }

    @Override
    public @NotNull ItemFactory itemFactory() {
        return null; // Hella not making this lmao
    }

    @Override
    public @org.jetbrains.annotations.Nullable World getWorld(String name) {
        return null;
    }

    @Override
    public @NotNull Collection<World> worlds() {
        return null;
    }

    @Override
    public void createStudioWorld(@NotNull Project project) {
    }

    @Override
    public @Nullable Player getPlayer(UUID uuid) {
        return null;
    }

    @Override
    public @NotNull Collection<Player> getPlayers() {
        return null;
    }

    @Override
    public @Nullable Class<?> mainClass() {
        // Broken for now, would not recommend using this.
        return null;
    }
}
