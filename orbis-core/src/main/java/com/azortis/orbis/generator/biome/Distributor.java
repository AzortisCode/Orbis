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

package com.azortis.orbis.generator.biome;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.pack.Inject;
import com.azortis.orbis.pack.Invoke;
import com.azortis.orbis.pack.studio.annotations.Description;
import com.azortis.orbis.pack.studio.annotations.Entries;
import com.azortis.orbis.pack.studio.annotations.Required;
import com.azortis.orbis.pack.studio.annotations.Typed;
import com.azortis.orbis.util.annotations.AbsoluteCoords;
import com.azortis.orbis.util.annotations.SectionCoords;
import com.azortis.orbis.util.math.Point2i;
import com.azortis.orbis.util.math.Point3i;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.annotations.SerializedName;
import net.kyori.adventure.key.Key;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * The generator object responsible for the distribution of biomes. The class caches values from its implementation
 * since multiple calls made for the same coordinates within set timeframe is very common.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@Typed
@Inject
@API(status = API.Status.EXPERIMENTAL, since = "0.3-Alpha")
@Description("Generator object that handles with the distribution of biomes.")
public abstract class Distributor {

    @Required
    @Description("The name of the distributor instance, must be same as file name without the *.json suffix.")
    protected final String name;

    @Required
    @Description("The type of distributor algorithm to use.")
    protected final Key type;

    @Required
    @Entries(Biome.class)
    @SerializedName("biomes")
    @Description("All the possible biomes from this distributor.")
    private Set<String> biomeNames;

    // Biomes are separate files, so we inject them here.
    @Inject(fieldName = "biomeNames", collectionType = HashSet.class, parameterizedType = Biome.class)
    private transient Set<Biome> biomes;

    private transient LoadingCache<Point2i, BiomeSection> mapCache;
    private transient LoadingCache<Point3i, BiomeSection> sectionCache;

    protected Distributor(@NotNull String name, @NotNull Key type) {
        this.name = name;
        this.type = type;
    }

    @Invoke(when = Invoke.Order.MID_INJECTION)
    private void setup() {
        // Make sets immutable
        this.biomeNames = Set.copyOf(biomeNames);
        this.biomes = Set.copyOf(biomes);

        // Initialize caches
        // TODO find optimum size & expiry time
        if (layout().hasBiomeMap()) {
            mapCache = CacheBuilder.newBuilder()
                    .expireAfterAccess(5L, TimeUnit.SECONDS)
                    .maximumSize(1600)
                    .build(new CacheLoader<>() {
                        @Override
                        public @NotNull BiomeSection load(@NotNull Point2i pos) {
                            return sample(pos.x(), pos.z());
                        }
                    });
        }
        if (layout().hasFullBiomes()) {
            sectionCache = CacheBuilder.newBuilder()
                    .expireAfterAccess(5L, TimeUnit.SECONDS)
                    .maximumSize(8000)
                    .build(new CacheLoader<>() {
                        @Override
                        public @NotNull BiomeSection load(@NotNull Point3i pos) {
                            return sample(pos.x(), pos.y(), pos.z());
                        }
                    });
        }
    }

    public @NotNull String name() {
        return name;
    }

    public @NotNull Key type() {
        return type;
    }

    public @Unmodifiable @NotNull Set<String> biomeNames() {
        return biomeNames;
    }

    public @Unmodifiable @NotNull Set<Biome> biomes() {
        return biomes;
    }

    public @NotNull Biome getBiome(@NotNull String name) throws IllegalArgumentException {
        for (Biome biome : biomes) {
            if (biome.name().equalsIgnoreCase(name)) return biome;
        }
        throw new IllegalArgumentException("Biome by name " + name + " is not registered with this distributor!");
    }

    @AbsoluteCoords
    public @NotNull Biome getBiome(int x, int z) {
        if (!layout().hasBiomeMap()) {
            throw new UnsupportedOperationException("This distributor doesn't support 2d biome maps");
        }
        return getSection(x, z).biome();
    }

    @AbsoluteCoords
    public @NotNull BiomeSection getSection(int x, int z) {
        if (!layout().hasBiomeMap()) {
            throw new UnsupportedOperationException("This distributor doesn't support 2d biome maps");
        }
        try {
            return mapCache.get(new Point2i(x >> 2, z >> 2));
        } catch (ExecutionException ex) {
            Orbis.getLogger().error("Failed to load biome section at block coordinates [{},{}]", x, z);
            throw new RuntimeException(ex);
        }
    }

    @AbsoluteCoords
    public @NotNull Biome getBiome(int x, int y, int z) {
        if (!layout().hasFullBiomes()) return getBiome(x, z);
        return getSection(x, y, z).biome();
    }

    @AbsoluteCoords
    public @NotNull BiomeSection getSection(int x, int y, int z) {
        if (!layout().hasFullBiomes()) return getSection(x, z);
        try {
            return sectionCache.get(new Point3i(x >> 2, y >> 2, z >> 2));
        } catch (ExecutionException ex) {
            Orbis.getLogger().error("Failed to load biome section at block coordinates [{},{},{}]", x, y, z);
            throw new RuntimeException(ex);
        }
    }

    @AbsoluteCoords
    public @NotNull Biome getBiome(double x, double z) {
        if (!layout().hasBiomeMap()) {
            throw new UnsupportedOperationException("This distributor doesn't support 2d biome maps");
        }
        return getSection(x, z).biome();
    }

    @AbsoluteCoords
    public @NotNull BiomeSection getSection(double x, double z) {
        if (!layout().hasBiomeMap()) {
            throw new UnsupportedOperationException("This distributor doesn't support 2d biome maps");
        }
        return getSection((int) x, (int) z);
    }

    @AbsoluteCoords
    public @NotNull Biome getBiome(double x, double y, double z) {
        if (!layout().hasFullBiomes()) return getBiome(x, z);
        return getSection(x, y, z).biome();
    }

    @AbsoluteCoords
    public @NotNull BiomeSection getSection(double x, double y, double z) {
        if (!layout().hasFullBiomes()) return getSection(x, z);
        return getSection((int) x, (int) y, (int) z);
    }

    /**
     * Samples tne underlying distributor for a {@link BiomeSection} at given section coordinates.
     *
     * @param x The x-coordinate.
     * @param z The z-coordinate.
     * @return The biome section for the specified coordinates.
     * @since 0.3-Alpha
     */
    @SectionCoords
    protected abstract BiomeSection sample(int x, int z);

    /**
     * Samples the underlying distributor for a {@link BiomeSection} at given section coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     * @return The biome section for the specified coordinates.
     * @since 0.3-Alpha
     */
    @SectionCoords
    protected abstract BiomeSection sample(int x, int y, int z);

    /**
     * Get the {@link BiomeLayout} this distributor supports/generates.
     *
     * @return The supported/generated biome layout type.
     * @since 0.3-Alpha
     */
    public abstract @NotNull BiomeLayout layout();

}
