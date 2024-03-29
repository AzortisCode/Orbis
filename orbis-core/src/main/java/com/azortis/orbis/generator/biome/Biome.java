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

import com.azortis.orbis.block.ConfiguredBlock;
import com.azortis.orbis.generator.surface.Surface;
import com.azortis.orbis.pack.Inject;
import com.google.gson.annotations.SerializedName;
import net.kyori.adventure.key.Key;

@Inject
public final class Biome {

    private final String name;
    private final Key derivative;

    @SerializedName("surface")
    private final String surfaceName;
    private final int baseHeight;
    private final ConfiguredBlock surfaceBlock;
    private final ConfiguredBlock belowSurfaceBlock;
    @Inject(fieldName = "terrainName")
    private transient Surface surface;

    public Biome(String name, Key derivative, String surfaceName, int baseHeight, ConfiguredBlock surfaceBlock,
                 ConfiguredBlock belowSurfaceBlock) {
        this.name = name;
        this.derivative = derivative;
        this.surfaceName = surfaceName;
        this.baseHeight = baseHeight;
        this.surfaceBlock = surfaceBlock;
        this.belowSurfaceBlock = belowSurfaceBlock;
    }

    public String name() {
        return this.name;
    }

    public Key derivative() {
        return this.derivative;
    }

    public String surfaceName() {
        return this.surfaceName;
    }

    public int baseHeight() {
        return this.baseHeight;
    }

    public ConfiguredBlock surfaceBlock() {
        return this.surfaceBlock;
    }

    public ConfiguredBlock belowSurfaceBlock() {
        return this.belowSurfaceBlock;
    }

    public Surface surface() {
        return this.surface;
    }
}
