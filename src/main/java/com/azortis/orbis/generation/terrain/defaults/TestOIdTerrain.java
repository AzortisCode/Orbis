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

package com.azortis.orbis.generation.terrain.defaults;

import com.azortis.orbis.generation.terrain.OIdTerrain;
import com.azortis.orbis.noise.NoiseGenerator;

public class TestOIdTerrain implements OIdTerrain {

    private final int baseHeight = 100;

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public double getTerrainHeight(int x, int z, NoiseGenerator noise) {
        double height = noise.noise(x / 800f, z / 800f) * 80;
        height += noise.noise(x / 100f, z / 100f) * 10;
        height += noise.noise(x / 20f, z / 20f) * 0.5f;
        return baseHeight + height;
    }

}
