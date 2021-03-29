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

package com.azortis.orbis.generator.point;

/**
 * Credits to k.jpg from FastNoise for making this class.
 * @param <TTag> The type tag of the point gatherer.
 */
public class GatheredPoint<TTag> {
    private final double x, z;
    private final int hash;
    private TTag tag;

    public GatheredPoint(double x, double z, int hash) {
        this.x = x;
        this.z = z;
        this.hash = hash;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public double getHash() {
        return hash;
    }

    public TTag getTag() {
        return tag;
    }

    public void setTag(TTag tag) {
        this.tag = tag;
    }
}