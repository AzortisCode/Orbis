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

package com.azortis.orbis.generator.noise;

public class PerlinNoise implements NoiseGenerator, OctaveNoise{

    private final FastNoise noise;

    public PerlinNoise(int seed){
        noise = new FastNoise(seed);
        noise.setNoiseType(FastNoise.NoiseType.Perlin);
        noise.setRotationType3D(FastNoise.RotationType3D.ImproveXZPlanes);
        noise.setFractalOctaves(7);
        noise.setFrequency(1);
    }

    @Override
    public double noise(double x) {
        return noise.getNoise(x, 0);
    }

    @Override
    public double noise(double x, double y) {
        return noise.getNoise(x, 0, y);
    }

    @Override
    public double noise(double x, double y, double z) {
        return noise.getNoise(x, y, z);
    }

    @Override
    public void setOctaves(int octaves) {
        noise.setFractalOctaves(octaves);
    }

}
