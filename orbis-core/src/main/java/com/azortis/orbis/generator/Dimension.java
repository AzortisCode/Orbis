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

import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.framework.ChunkStage;
import com.azortis.orbis.generator.framework.WorldStage;
import com.azortis.orbis.pack.Inject;
import com.azortis.orbis.pack.Validate;
import com.azortis.orbis.pack.studio.annotations.*;
import com.azortis.orbis.world.World;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

@Description("The dimension object which sets the foundational blueprint for a world.")
public final class Dimension {

    //
    // Dimension data
    //

    @Required
    @Description("The name of the dimension.")
    private final String name;

    @Inject
    private transient World world;

    @Required
    @Min(-1024)
    @Max(1023)
    @Description("The minimum build height.")
    private int minHeight;

    @Required
    @Min(-1023)
    @Max(1024)
    @Description("The maximum build height")
    private int maxHeight;

    //
    // Generation stages
    //

    /**
     * The {@link Distributor} is a special stage, as it is <b>always</b> required,
     * hence why it exists as a static object in the dimension tree.
     */
    @Required
    @Entries(Distributor.class)
    @SerializedName("distributor")
    @Description("The root biome distributor to use for this dimension.")
    private String distributorName;
    @Inject(fieldName = "distributorName")
    private transient Distributor distributor;

    @Required
    @ArrayType(ChunkStage.class)
    @Description("The stages in correct order that should be executed during chunk generation," +
            "\nin these stages the generator will only write to current chunk and has no access to neighbouring chunks.")
    private List<ChunkStage> chunkStages;

    @Required
    @ArrayType(WorldStage.class)
    @Description("""
            The stages in correct order that should be executed after chunk generation,
            in these stages the chunk has been finalized, and the generator has access to all chunks.
            This access is required for generating structures and features that span across multiple chunks.\s""")
    private List<WorldStage> worldStages;

    public Dimension(@NotNull String name) {
        this.name = name;
    }

    @Validate
    private void validate() {
        if (verticalSize() % 4 != 0) {
            // TODO throw error, vertical size must be dividable by 4 due to biome maps.
        }
    }

    /**
     * The name of the dimension. i.e. "overworld".
     *
     * @return The name of the dimension.
     * @since 0.3-Alpha
     */
    public String name() {
        return this.name;
    }

    /**
     * The world this {@link Dimension} instance currently belongs to.
     *
     * @return The {@link World} of this {@link Dimension}.
     * @since 0.3-Alpha
     */
    public @NotNull World world() {
        return this.world;
    }

    /**
     * Checks if {@link Dimension} instance is loaded (properly).
     *
     * @return If the Dimension object has been loaded (properly).
     * @since 0.3-Alpha
     */
    public boolean isLoaded() {
        return world == null || distributor == null;
    }

    /**
     * Get the minimum build height of this dimension.
     *
     * @return The minimum build height.
     * @since 0.3-Alpha
     */
    public int minHeight() {
        return this.minHeight;
    }

    /**
     * Get the maximum build height of this dimension.
     *
     * @return The maximum build height.
     * @since 0.3-Alpha
     */
    public int maxHeight() {
        return this.maxHeight;
    }

    /**
     * Get the vertical range of this dimension.
     *
     * @return The vertical block range.
     * @since 0.3-Alpha
     */
    public int verticalSize() {
        return maxHeight - minHeight + 1;
    }

    public @NotNull Distributor distributor() {
        return this.distributor;
    }

    public @Unmodifiable List<ChunkStage> chunkStages() {
        return List.copyOf(chunkStages);
    }

    public @Unmodifiable List<WorldStage> worldStages() {
        return List.copyOf(worldStages);
    }
}
