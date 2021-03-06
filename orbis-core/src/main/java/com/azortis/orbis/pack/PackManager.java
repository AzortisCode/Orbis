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
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import net.lingala.zip4j.ZipFile;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PackManager {

    private final Map<Pack, File> loadedPacks = new HashMap<>();

    public PackManager(File rootDirectory){
        File packsDirectory = new File(rootDirectory, "/packs/");
        if(!packsDirectory.exists()){
            if(!packsDirectory.mkdirs()) Orbis.getLogger().error("Couldn't create packs folder!");
        }

        // Iterate through all folders and files(*.orbis).
        for (File entry : packsDirectory.listFiles()){
            if(!entry.isDirectory()) {
                loadPack(entry);
            }
        }
    }

    @Nullable
    public Pack getPack(String name){
        for (Pack pack : loadedPacks.keySet()){
            if(pack.getName().equalsIgnoreCase(name))return pack;
        }
        return null;
    }

    public void extractPack(File destinationFolder, Pack pack){
        try {
            new ZipFile(loadedPacks.get(pack)).extractAll(destinationFolder.getPath());
        } catch (IOException ex) {
            Orbis.getLogger().error("Failed to extract pack.");
        }
    }

    private void loadPack(File packFile){
        try {
            ZipFile zipFile = new ZipFile(packFile);
            if(zipFile.isValidZipFile()) {
                InputStream inputStream = zipFile.getInputStream(zipFile.getFileHeader("pack.json"));
                byte[] buffer = ByteStreams.toByteArray(inputStream);
                Reader reader = CharSource.wrap(new String(buffer)).openStream();
                Pack pack = Orbis.getGson().fromJson(reader, Pack.class);
                loadedPacks.put(pack, packFile);
            }
        }catch (IOException ex){
            Orbis.getLogger().error("Couldn't load pack: {}", packFile.getName());
        }
    }

}
