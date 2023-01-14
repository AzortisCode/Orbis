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
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public final class Dimension { // No need to check this class if it contains fields that need injection

    @Required
    @Description("The name of the dimension.")
    private final String name;
    private final int minHeight; // Fair, but I mean, is this a dimension property? Answer yes.
    private final int maxHeight; // Fair, max is 1024
    private final int fluidHeight;
    private final int interpolationRadius; // Pretty much bullshit but hey!

    @Inject
    private final transient World world;

    // Distribution
    @Required
    @Entries(Distributor.class)
    @SerializedName("distributor")
    private final String distributorName;
    @Inject(fieldName = "distributorName")
    private final transient Distributor distributor;

    public Dimension(String name, int minHeight, int maxHeight, int fluidHeight, int interpolationRadius, World world,
                     String distributorName, Distributor distributor) {
        this.name = name;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.fluidHeight = fluidHeight;
        this.interpolationRadius = interpolationRadius;
        this.world = world;
        this.distributorName = distributorName;
        this.distributor = distributor;
    }

    /**
     * Checks if {@link Dimension} instance is loaded (properly).
     *
     * @return If the Dimension object has been loaded (properly).
     */
    public boolean isLoaded() {
        return world == null || distributor == null;
    }

}
