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

package com.azortis.orbis.generator.biome.complex.layer;

import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.biome.complex.modifier.Modifier;
import com.azortis.orbis.generator.biome.complex.requirement.Requirement;
import com.azortis.orbis.util.Inject;
import com.azortis.orbis.util.Invoke;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

@Inject
public final class BiomeLayer extends Layer<Biome> {

    @SerializedName("biome")
    private final String biomeName;
    private transient Biome biome;

    @Inject
    private transient Dimension dimension;

    public BiomeLayer(Set<Requirement> requirements, boolean useDefaultModifier, Set<Modifier> modifiers, String biomeName) {
        super(requirements, useDefaultModifier, modifiers);
        this.biomeName = biomeName;
    }

    @Invoke(when = Invoke.Order.MID_INJECTION)
    private void loadBiome() {

    }

    @Override
    public Class<Biome> getType() {
        return null;
    }

    @Override
    public Biome getObject() {
        return null;
    }
}
