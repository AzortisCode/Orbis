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

import com.azortis.orbis.generator.noise.Noise;
import com.azortis.orbis.pack.studio.annotations.Min;
import com.azortis.orbis.pack.studio.annotations.Required;

public record NoiseLayer(@Required String tag, @Required Noise noise, @Required @Min(1) int precision) {

    public double sample(double x, double z) {
        return (double) Math.round(precision * noise.noise(x, z)) / precision;
    }

}
