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

import com.azortis.orbis.exception.CoordsOutOfBoundsException;
import com.azortis.orbis.generator.biome.BiomeSection;
import com.azortis.orbis.util.annotations.AbsoluteCoords;
import com.azortis.orbis.util.annotations.SectionCoords;
import com.azortis.orbis.world.ChunkAccess;
import com.azortis.orbis.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * A snapshot of a chunk being generated, will store vital information about generation context
 * that can be passed from one part of the generator to the other.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
public abstract class ChunkSnapshot implements ChunkAccess {

    protected final World world;
    protected final Dimension dimension;
    protected final Engine engine;

    private final BiomeSection[] biomeMap;
    private final BiomeSection[] biomeSections;

    protected ChunkSnapshot(@NotNull World world, @NotNull Dimension dimension, @NotNull Engine engine) {
        this.world = world;
        this.dimension = dimension;
        this.engine = engine;

        // If the engine pipeline has a 2d biome map, then create an array for all the 4x4 biomeMap sections.
        if (engine.biomeLayout().hasBiomeMap()) this.biomeMap = new BiomeSection[16];
        else this.biomeMap = null;

        // If the Engine pipeline uses 3d biomes in any form, then we create an array for the all the 4x4x4 biome
        // sections supported by the vanilla client.
        if (engine.biomeLayout().hasFullBiomes())
            this.biomeSections = new BiomeSection[16 * (dimension.verticalSize() >> 2)];
        else this.biomeSections = null;
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

    /**
     * Gets the {@link BiomeSection} from the stored 2D biomeMap array for this chunk.
     *
     * @param x The absolute block x-coordinate.
     * @param z The absolute block z-coordinate.
     * @return The stored biome section of given block coordinates.
     * @throws CoordsOutOfBoundsException If the coordinates are not within this chunk.
     */
    @AbsoluteCoords
    public @NotNull BiomeSection getSection(final int x, final int z) throws CoordsOutOfBoundsException {
        if (!checkBounds(x, 0, z))
            throw new CoordsOutOfBoundsException(x, z, this);
        int originX = chunkX() << 4;
        int originZ = chunkZ() << 4;

        int xIndex = Math.abs(x - originX) >> 2;
        int zIndex = Math.abs(z - originZ) >> 2;
        return biomeMap[xIndex + (zIndex << 2)];
    }

    @SectionCoords
    public void setSection(final int x, final int z, @NotNull BiomeSection section) throws CoordsOutOfBoundsException,
            IllegalStateException {
        if (!checkBounds(x, 0, z))
            throw new CoordsOutOfBoundsException(x, z, this);
        int originX = chunkX() << 2;
        int originZ = chunkZ() << 2;

        int xIndex = Math.abs(x - originX);
        int zIndex = Math.abs(z - originZ);
        int index = xIndex + (zIndex << 2);
        if (biomeMap[index] == null) {
            biomeMap[index] = section;
            return;
        }
        throw new IllegalStateException(String.format("The section index of %s of the biomeMap for chunk [%s,%s] " +
                "has already been populated", index, chunkX(), chunkZ()));
    }

    /**
     * Gets the {@link BiomeSection} from the stored 3D biomeSections array for this chunk.
     *
     * @param x The absolute block x-coordinate.
     * @param y The absolute block y-coordinate.
     * @param z The absolute block z-coordinate.
     * @return The stored biome section of given block coordinates.
     * @throws CoordsOutOfBoundsException If the coordinates are not within this chunk.
     */
    @AbsoluteCoords
    public @NotNull BiomeSection getSection(final int x, final int y, final int z) throws CoordsOutOfBoundsException {
        if (!checkBounds(x, y, z)) {
            throw new CoordsOutOfBoundsException(x, y, z, this);
        }
        int originX = chunkX() << 4;
        int originZ = chunkZ() << 4;

        int xIndex = Math.abs(x - originX) >> 2;
        int yIndex = Math.abs(y - dimension.minHeight()) >> 2;
        int zIndex = Math.abs(z - originZ) >> 2;
        return biomeSections[xIndex + yIndex * (dimension.verticalSize() >> 2) + (zIndex << 2)];
    }

    @SectionCoords
    public void setSection(final int x, final int y, final int z, @NotNull BiomeSection section)
            throws CoordsOutOfBoundsException, IllegalStateException {
        if (!checkBounds(x, y, z)) {
            throw new CoordsOutOfBoundsException(x, y, z, this);
        }
        int originX = chunkX() << 2;
        int originZ = chunkZ() << 2;

        int xIndex = Math.abs(x - originX);
        int yIndex = Math.abs(y - (dimension.minHeight() >> 2));
        int zIndex = Math.abs(z - originZ);
        int index = xIndex + yIndex * (dimension.verticalSize() >> 2) + (zIndex << 2);
        if (biomeSections[index] == null) {
            biomeSections[index] = section;
            return;
        }
        throw new IllegalStateException(String.format("The section index of %s of the biomeSections for " +
                "chunk [%s,%s] has already been populated", index, chunkX(), chunkZ()));
    }

}
