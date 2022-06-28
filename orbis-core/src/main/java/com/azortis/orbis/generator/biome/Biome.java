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

import com.azortis.orbis.block.ConfiguredBlock;
import com.azortis.orbis.generator.terrain.Terrain;
import com.azortis.orbis.util.Inject;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.key.Key;

@Accessors(fluent = true)
@Getter
@Inject
public final class Biome {

    private final String name;
    private final Key derivative;

    @SerializedName("terrain")
    private final String terrainName;
    private final int baseHeight;
    private final ConfiguredBlock surfaceBlock;
    private final ConfiguredBlock belowSurfaceBlock;
    @Inject(fieldName = "terrainName")
    private transient Terrain terrain;

    public Biome(String name, Key derivative, String terrainName, int baseHeight, ConfiguredBlock surfaceBlock,
                 ConfiguredBlock belowSurfaceBlock) {
        this.name = name;
        this.derivative = derivative;
        this.terrainName = terrainName;
        this.baseHeight = baseHeight;
        this.surfaceBlock = surfaceBlock;
        this.belowSurfaceBlock = belowSurfaceBlock;
    }

}
