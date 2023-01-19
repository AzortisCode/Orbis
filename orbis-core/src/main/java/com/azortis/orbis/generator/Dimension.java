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
import com.azortis.orbis.pack.Inject;
import com.azortis.orbis.pack.studio.annotations.Description;
import com.azortis.orbis.pack.studio.annotations.Entries;
import com.azortis.orbis.pack.studio.annotations.Required;
import com.azortis.orbis.world.World;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

@Description("The dimension object which sets the foundational blueprint for a world.")
public final class Dimension {

    @Required
    @Description("The name of the dimension.")
    private final String name;

    @Required
    @Description("The engine to use for this dimension.")
    private Engine engine;

    @Inject
    private transient World world;


    @Required
    @Entries(Distributor.class)
    @SerializedName("distributor")
    private String distributorName;
    @Inject(fieldName = "distributorName")
    private transient Distributor distributor;


    //
    // Deprecated fields for removal
    //
    @Deprecated
    private int minHeight;

    @Deprecated
    private int maxHeight;

    public Dimension(@NotNull String name) {
        this.name = name;
    }

    /**
     * Checks if {@link Dimension} instance is loaded (properly).
     *
     * @return If the Dimension object has been loaded (properly).
     */
    public boolean isLoaded() {
        return world == null || distributor == null;
    }

    public String name() {
        return this.name;
    }

    /**
     * Get the {@link Engine} this {@link Dimension} uses for its generation pipeline.
     *
     * @return The engine being used.
     * @since 0.3-Alpha
     */
    public Engine engine() {
        return engine;
    }

    @Deprecated
    public int minHeight() {
        return this.minHeight;
    }

    @Deprecated
    public int maxHeight() {
        return this.maxHeight;
    }


    public World world() {
        return this.world;
    }

    public Distributor distributor() {
        return this.distributor;
    }
}
