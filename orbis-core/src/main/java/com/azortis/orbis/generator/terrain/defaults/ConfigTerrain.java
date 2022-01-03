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

import com.azortis.orbis.generator.noise.OldNoiseGenerator;
import com.azortis.orbis.generator.terrain.Terrain;

import java.util.List;

public class ConfigTerrain extends Terrain {

    private List<NoiseLayer> layers;

    public ConfigTerrain(String name, String providerId) {
        super(name, providerId);
    }

    @Override
    public double getTerrainHeight(int x, int z, double biomeWeight, OldNoiseGenerator noise) {
        double height = 0;
        for (NoiseLayer layer : layers) {
            height += (noise.noise(x / layer.zoom, z / layer.zoom) * layer.coefficient);
        }
        return super.getBiome().getBaseHeight() + height;
    }

    private static class NoiseLayer {
        double coefficient;
        double zoom;
    }

}
