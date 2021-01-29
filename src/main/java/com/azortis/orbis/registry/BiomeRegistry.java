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

import com.azortis.orbis.Orbis;
import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.region.Region;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class BiomeRegistry {

    public static final String NAME_REGEX = "^([a-zA-Z]+)$";
    private final File biomesFolder;

    public BiomeRegistry(){
        this.biomesFolder = new File(Orbis.getDirectory(), "/settings/biomes/");
        if (!this.biomesFolder.exists()) {
            if (!this.biomesFolder.mkdirs()) {
                Orbis.getLogger().error("Couldn't create biomes folder.");
            }
        }
    }

    public Biome loadBiome(String biomeName, Region context){
        if(biomeName.matches(NAME_REGEX)){
            final File biomeFile = new File(biomesFolder, biomeName + ".json");
            try{
                final Biome biome = Orbis.getGson().fromJson(new FileReader(biomeFile), Biome.class);
                biome.setRegion(context);
                biome.getTerrainLayers().forEach(terrainLayer -> terrainLayer.setTerrain(Orbis.getTerrainRegistry()
                        .loadTerrain(terrainLayer.getTerrainName(), biome)));
                return biome;
            }catch (FileNotFoundException ex){
                Orbis.getLogger().error("No biome file by the name: " + biomeName + " exists!");
            }
        } else {
            Orbis.getLogger().error("Invalid biome name: " + biomeName);
        }
        return null;
    }

    public File getBiomesFolder() {
        return biomesFolder;
    }
}
