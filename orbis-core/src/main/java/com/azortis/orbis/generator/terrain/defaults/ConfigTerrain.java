/*
 * MIT License
 *
 * Copyright (c) 2021 Azortis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.azortis.orbis.generator.terrain.defaults;

import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.azortis.orbis.generator.terrain.Terrain;

import java.util.List;

public class ConfigTerrain extends Terrain {

    private List<NoiseLayer> layers;

    public ConfigTerrain(String name, String providerId) {
        super(name, providerId);
    }

    @Override
    public double getTerrainHeight(int x, int z, double biomeWeight, NoiseGenerator noise) {
        double height = 0;
        for (NoiseLayer layer : layers){
            height += (noise.noise(x / layer.zoom, z / layer.zoom) * layer.coefficient);
        }
        return super.getBiome().getBaseHeight() + height;
    }

    private static class NoiseLayer {
        double coefficient;
        double zoom;
    }

}