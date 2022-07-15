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

package com.azortis.orbis.pack.studio;

import com.azortis.orbis.World;
import com.azortis.orbis.WorldInfo;
import com.azortis.orbis.pack.Pack;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A world interface that is used to visualize settings when working on a project.
 */
public abstract class StudioWorld extends World {

    protected final Project project;

    /**
     * If the world should render, or just create void chunks.
     */
    private boolean shouldRender = false;

    public StudioWorld(String name, File folder, Project project) {
        super(name, folder, project);
        this.project = project;
    }

    public Project project() {
        return project;
    }

    public void setSeed(long seed) {
        this.worldInfo = new WorldInfo("null", "null", seed);
    }

    @Override
    public void load(long seed) {
        throw new UnsupportedOperationException("The normal loading mechanism cannot be used for a studio world!");
    }

    @Override
    public void installPack(@NotNull Pack pack, boolean override) {
        throw new UnsupportedOperationException("Cannot install a pack for a studio world!");
    }

    /**
     * Called everytime the render mode has updated or the configs have updated.
     * Platform implementation should use this to clear the world, and regenerate chunks or update their
     * biome registries.
     */
    public abstract void hotReload();

    /**
     * Called when the platform should initialize the rendering of the world.
     */
    public abstract void initialize();

    /**
     * Called when the StudioWorld is being unloaded.
     * Platform implementations should use this to delete
     * any native world they have in memory or on disk.
     */
    protected abstract void unload();
}
