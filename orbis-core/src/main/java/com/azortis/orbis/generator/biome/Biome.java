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

package com.azortis.orbis.generator.biome;

import com.azortis.orbis.generator.terrain.Terrain;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import net.kyori.adventure.key.Key;

@Getter
public class Biome {

    private String name;
    private Key derivative;

    @SerializedName("terrain")
    private String terrainName;
    private int baseHeight;
    private Key surfaceBlock;

    private transient Terrain terrain;

    public void setTerrain(Terrain terrain) {
        if (this.terrain == null) this.terrain = terrain;
    }
}
