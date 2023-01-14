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

package com.azortis.orbis.generator.biome.complex.layer;

import com.azortis.orbis.generator.biome.complex.Region;
import com.azortis.orbis.generator.biome.complex.modifier.Modifier;
import com.azortis.orbis.generator.biome.complex.requirement.Requirement;
import com.azortis.orbis.pack.Inject;
import com.azortis.orbis.pack.studio.annotations.Entries;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

@Inject
public final class RegionLayer extends Layer<Region> {

    @Entries(Region.class)
    @SerializedName("region")
    private final String regionName;

    @Inject(fieldName = "regionName")
    private transient Region region;

    public RegionLayer(String regionName, Set<Requirement> requirements, boolean useDefaultModifier, Set<Modifier> modifiers) {
        super(requirements, useDefaultModifier, modifiers);
        this.regionName = regionName;
    }

    @Override
    public Class<Region> getType() {
        return Region.class;
    }

    @Override
    public Region getObject() {
        return region;
    }
}
