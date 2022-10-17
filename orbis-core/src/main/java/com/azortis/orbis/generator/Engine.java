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

package com.azortis.orbis.generator;

import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class Engine {

    protected final World world;

    public Engine(World world) {
        this.world = world;
    }

    @Deprecated
    public abstract void generateChunk(int chunkX, int chunkZ, @NotNull ChunkData chunkData);

    public World world() {
        return world;
    }

    public Dimension dimension() {
        return world.getDimension();
    }

    public Distributor distributor() {
        return world.getDimension().distributor();
    }

}
