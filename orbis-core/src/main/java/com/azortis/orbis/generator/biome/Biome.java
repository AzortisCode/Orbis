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

package com.azortis.orbis.generator.biome;

import com.azortis.orbis.generator.terrain.Terrain;
import com.azortis.orbis.util.NamespaceId;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Biome {

    private String name;
    private NamespaceId derivative;
    @SerializedName("terrain")
    private String terrainName;
    private int baseHeight;
    private NamespaceId surfaceBlock;

    private transient Terrain terrain;

    public String getName() {
        return name;
    }

    public NamespaceId getDerivative() {
        return derivative;
    }

    public String getTerrainName() {
        return terrainName;
    }

    public void setTerrain(Terrain terrain){
        if(this.terrain == null)this.terrain = terrain;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    public NamespaceId getSurfaceBlock() {
        return surfaceBlock;
    }
}
