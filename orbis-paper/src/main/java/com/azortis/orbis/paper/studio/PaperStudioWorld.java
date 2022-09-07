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

package com.azortis.orbis.paper.studio;

import com.azortis.orbis.pack.studio.Project;
import com.azortis.orbis.pack.studio.StudioWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;

public final class PaperStudioWorld extends StudioWorld {

    private World nativeWorld;

    public PaperStudioWorld(Project project) {
        super("orbis_studio", new File(Bukkit.getWorldContainer() + "/orbis_studio/"), project);
    }

    public World nativeWorld() {
        return nativeWorld;
    }

    @Override
    public void hotReload() {

    }

    @Override
    public void initialize() {
        WorldCreator worldCreator = new WorldCreator(name())
                .generator(new PaperStudioChunkGenerator(this.project))
                .generateStructures(false)
                .environment(World.Environment.NORMAL);
        nativeWorld = Bukkit.createWorld(worldCreator);
    }

    @Override
    protected void unload() {
        // Get all players out this world.
        for (Player worldPlayer : nativeWorld.getPlayers()) {

        }
        // Unload the world
        Bukkit.getServer().unloadWorld(nativeWorld, false);
        this.nativeWorld = null;
    }
}
