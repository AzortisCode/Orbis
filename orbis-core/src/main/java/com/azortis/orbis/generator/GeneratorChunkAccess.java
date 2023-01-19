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

package com.azortis.orbis.generator;

import com.azortis.orbis.world.ChunkAccess;
import com.azortis.orbis.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * A chunk access in the context of generation, will store vital information about generation context
 * that can be passed from one part of the generator to the other.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
public abstract class GeneratorChunkAccess implements ChunkAccess {

    protected final World world;
    protected final Dimension dimension;
    protected final Engine engine;

    public GeneratorChunkAccess(@NotNull World world, @NotNull Dimension dimension, @NotNull Engine engine) {
        this.world = world;
        this.dimension = dimension;
        this.engine = engine;
    }

    public World world() {
        return world;
    }

    public Dimension dimension() {
        return dimension;
    }

    public Engine engine() {
        return engine;
    }
}
