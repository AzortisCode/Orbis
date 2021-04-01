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

package com.azortis.orbis.generator.noise;

public class OpenSimplex2S implements NoiseGenerator{

    private final FastNoise noise;

    public OpenSimplex2S(long seed){
        noise = new FastNoise(seed);
        noise.setNoiseType(FastNoise.NoiseType.OpenSimplex2S);
        noise.setFrequency(1);
    }

    @Override
    public double noise(double x) {
        return noise.getNoise(x, 0);
    }

    @Override
    public double noise(double x, double z){
        return noise.getNoise(x, z);
    }

    @Override
    public double noise(double x, double y, double z) {
        return noise.getNoise(x, y, z);
    }
}
