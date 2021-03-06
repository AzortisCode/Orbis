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

package com.azortis.orbis.generator.terrain.defaults;

import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.azortis.orbis.generator.terrain.Terrain;

public class PlainsTerrain extends Terrain {

    public PlainsTerrain(String name, String providerId) {
        super(name, providerId);
    }

    @Override
    public double getTerrainHeight(int x, int z, double biomeWeight, NoiseGenerator noise) {
        double height = noise.noise(x / 400f, z / 400f) * 50;
        height += noise.noise(x / 50f, z / 50f) * 5;
        height += Math.abs(noise.noise(x / 12f, z / 12f) * 1);
        return super.getBiome().getBaseHeight() + height;
    }
}
