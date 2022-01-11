/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2021  Azortis
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

import com.azortis.orbis.Platform;
import com.azortis.orbis.World;
import com.azortis.orbis.item.ItemFactory;
import com.azortis.orbis.paper.item.PaperItemFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class PaperPlatform implements Platform {

    private final OrbisPlugin plugin;
    private final PaperItemFactory itemFactory;

    public PaperPlatform(OrbisPlugin plugin) {
        this.plugin = plugin;
        this.itemFactory = new PaperItemFactory();
    }

    @Override
    public @NotNull String adaptation() {
        return "Bukkit";
    }

    @Override
    public @NotNull Logger logger() {
        return plugin.getSLF4JLogger();
    }

    @Override
    public @NotNull File directory() {
        return plugin.getDataFolder();
    }

    @Override
    public @NotNull ItemFactory itemFactory() {
        return itemFactory;
    }

    @Nullable
    @Override
    public World getWorld(String name) {
        return plugin.getWorld(name);
    }

    @Override
    public @NotNull Collection<World> worlds() {
        return new ArrayList<>(plugin.getWorlds());
    }

    @Override
    public @NotNull Class<?> getMainClass() {
        return plugin.getClass();
    }

}
