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

package com.azortis.orbis.pack;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.generator.Dimension;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class PackManager {

    private final File packsDirectory;
    private final Gson gson;
    private final Map<Pack, File> loadedPacks = new HashMap<>();

    public PackManager(File rootDirectory){
        this.packsDirectory = new File(rootDirectory, "/packs/");
        if(!packsDirectory.exists()){
            if(!packsDirectory.mkdirs()) Orbis.getLogger().error("Couldn't create packs folder!");
        }

        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        // Iterate through all folders and files(*.orbis).
        for (File entry : packsDirectory.listFiles()){
            if(entry.isDirectory()){
                File packFile = new File(entry, "pack.json");
                try {
                    Pack pack = gson.fromJson(new FileReader(packFile), Pack.class);
                    loadedPacks.put(pack, entry);
                } catch (FileNotFoundException e) {
                    Orbis.getLogger().error("No pack.json found in: " + entry.getName());
                }

            } else {
                Orbis.getLogger().error("*.orbis Files are not yet supported!");
            }
        }
    }

    public File getPacksDirectory() {
        return packsDirectory;
    }

    @Nullable
    public Pack getPack(String name){
        for (Pack pack : loadedPacks.keySet()){
            if(pack.getName().equalsIgnoreCase(name))return pack;
        }
        return null;
    }

    public Dimension loadDimension(Pack pack){

        return null;
    }

}
