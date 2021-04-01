/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2021  Azortis
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

package com.azortis.orbis.generator.terrain;

import com.azortis.orbis.container.Container;
import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public abstract class Terrain {

    protected String name;
    protected String providerId;
    private transient Container container;
    private transient Biome biome;

    // Used for deserialization for gson, more stable.
    private Terrain(){}

    public Terrain(String name, String providerId){
        this.name = name;
        this.providerId = providerId;
    }

    public void setContainer(Container container) {
        if(this.container == null) this.container = container;
    }

    public final void setBiome(Biome biome){
        if (biome != null) this.biome = biome;
    }

    public abstract double getTerrainHeight(final int x, final int z, double biomeWeight, NoiseGenerator noise);

}
