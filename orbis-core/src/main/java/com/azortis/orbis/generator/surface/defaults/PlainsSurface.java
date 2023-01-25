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

package com.azortis.orbis.generator.surface.defaults;

import com.azortis.orbis.generator.framework.ChunkSnapshot;
import com.azortis.orbis.generator.noise.Noise;
import com.azortis.orbis.generator.surface.Surface;
import com.azortis.orbis.pack.Inject;
import com.google.gson.annotations.SerializedName;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class PlainsSurface extends Surface {

    @SerializedName("noise")
    private final String noiseName;

    @Inject(fieldName = "noiseName")
    private transient Noise noise;

    private PlainsSurface(String name, Key type, String noiseName) {
        super(name, type);
        this.noiseName = noiseName;
    }

    @Override
    public double getSurfaceHeight(int x, int z, @NotNull ChunkSnapshot snapshot) {
        double height = noise.noise(x / 400f, z / 400f) * 30;
        height += noise.noise(x / 50f, z / 50f) * 3;
        height += Math.abs(noise.noise(x / 12f, z / 12f) * 1);
        return super.getBiome().baseHeight() + height;
    }
}
