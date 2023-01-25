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

package com.azortis.orbis.generator.framework;

import com.azortis.orbis.exception.CoordsOutOfBoundsException;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.biome.BiomeLayout;
import com.azortis.orbis.generator.biome.BiomeSection;
import com.azortis.orbis.util.annotations.AbsoluteCoords;
import com.azortis.orbis.util.annotations.SectionCoords;
import com.azortis.orbis.world.ChunkAccess;
import com.azortis.orbis.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * <p>A snapshot of a chunk being generated, will store vital information about generation context
 * that can be passed from/to {@link ChunkStage} & {@link WorldStage}'s.</p>
 *
 * <p>During {@link ChunkStage}'s the snapshot supports reading/writing chunk data, during this
 * it should be assumed that {@link ChunkSnapshot#isFinished()} returns false. The snapshot object is still
 * used in {@link WorldStage}'s but can no longer read/write chunk data, all the context functionality is still
 * supported however. During these stages a {@link WorldSnapshot} should be used instead.</p>
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
public abstract class ChunkSnapshot implements ChunkAccess { // TODO detach this from chunk access

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
     * Or refers to the {@link com.azortis.orbis.generator.biome.Distributor} if coords are out of
     * this chunk.
     *
     * @param x The absolute block x-coordinate.
     * @param z The absolute block z-coordinate.
     * @return The biome section of given block coordinates.
     * @throws IllegalStateException If the pipeline doesn't have surface biomes.
     * @since 0.3-Alpha
     */
    @AbsoluteCoords
    public @NotNull BiomeSection getSection(final int x, final int z) throws IllegalStateException {
        if (engine.biomeLayout() == BiomeLayout.FULL) {
            throw new IllegalStateException("The engine only has 3d biomes");
        } else if (!checkBounds(x, z))
            return dimension.distributor().getSection(x, z);
        int originX = chunkX() << 4;
        int originZ = chunkZ() << 4;

        int xIndex = Math.abs(x - originX) >> 2;
        int zIndex = Math.abs(z - originZ) >> 2;
        return biomeMap[xIndex + (zIndex << 2)];
    }

    @SectionCoords
    void setSection(final int x, final int z, @NotNull BiomeSection section) throws CoordsOutOfBoundsException,
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
     * Or refers to the {@link com.azortis.orbis.generator.biome.Distributor} if coords are out of
     * this chunk. If the biome layout is {@link BiomeLayout#SURFACE} then it refers to the 2d map.
     *
     * @param x The absolute block x-coordinate.
     * @param y The absolute block y-coordinate.
     * @param z The absolute block z-coordinate.
     * @return The biome section of given block coordinates.
     */
    @AbsoluteCoords
    public @NotNull BiomeSection getSection(final int x, final int y, final int z) {
        if (engine.biomeLayout() == BiomeLayout.SURFACE) {
            return getSection(x, z);
        } else if (!checkBounds(x, y, z))
            return dimension.distributor().getSection(x, y, z);
        int originX = chunkX() << 4;
        int originZ = chunkZ() << 4;

        int xIndex = Math.abs(x - originX) >> 2;
        int yIndex = Math.abs(y - dimension.minHeight()) >> 2;
        int zIndex = Math.abs(z - originZ) >> 2;
        return biomeSections[xIndex + yIndex * (dimension.verticalSize() >> 2) + (zIndex << 2)];
    }

    @SectionCoords
    void setSection(final int x, final int y, final int z, @NotNull BiomeSection section)
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

    /**
     * If the snapshot is finished, meaning reading & writing chunk information no longer works
     * and will throw a {@link IllegalStateException}. The snapshot object will purely act as a context object
     * for {@link WorldStage}'s where some information about the generated biomes & heightmaps may still be of use.
     *
     * @return If the snapshot is finished, meaning some operations
     * no longer work and throw an {@link IllegalStateException}
     * @since 0.3-Alpha
     */
    public abstract boolean isFinished();

}
