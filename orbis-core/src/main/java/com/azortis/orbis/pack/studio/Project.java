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

package com.azortis.orbis.pack.studio;

/**
 * A representation of a studio project environment, manages all things from Schema generation and
 * mapping the schema to the right files, and forces registry schema's to regenerate when a new config data file
 * has been added, to make sure it can be referenced in other files.
 */
public final class Project {

    private final StudioWorld world;

    public Project(StudioWorld world) {
        this.world = world;
    }
}
