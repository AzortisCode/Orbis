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

package com.azortis.orbis.registry;

import com.azortis.orbis.OrbisMine;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.biome.Biome;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class DimensionRegistry {

    public static final String NAME_REGEX = "^([a-zA-Z]+)$";
    private final File dimensionsFolder;

    public DimensionRegistry() {
        this.dimensionsFolder = new File(OrbisMine.getDirectory(), "/data/dimensions/");
        if (!this.dimensionsFolder.exists()) {
            if (!this.dimensionsFolder.mkdirs()) {
                OrbisMine.getLogger().error("Couldn't create dimensions folder.");
            }
        }
    }

    public Dimension loadDimension(String dimensionName){
        if(dimensionName.matches(NAME_REGEX)){
            final File dimensionFile = new File(dimensionsFolder, dimensionName + ".json");
            try{
                final Dimension dimension = OrbisMine.getGson().fromJson(new FileReader(dimensionFile), Dimension.class);
                dimension.setTerrain(OrbisMine.getTerrainRegistry().loadTerrain(dimension.getTerrainName(), new Biome()));
                return dimension;
            } catch (FileNotFoundException ex){
                OrbisMine.getLogger().error("No dimension file by the name: " + dimensionName + " exists!");
            }
        } else {
            OrbisMine.getLogger().error("Invalid dimension name: " + dimensionName);
        }
        return null;
    }

    public File getDimensionsFolder() {
        return dimensionsFolder;
    }
}
