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

package com.azortis.orbis.pack;

import com.azortis.orbis.Orbis;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import net.lingala.zip4j.ZipFile;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class PackManager {

    private final Map<Pack, File> loadedPacks = new HashMap<>();

    public PackManager(File rootDirectory) {
        File packsDirectory = new File(rootDirectory, "/packs/");
        if (!packsDirectory.exists()) {
            if (!packsDirectory.mkdirs()) Orbis.getLogger().error("Couldn't create packs folder!");
        }

        // Iterate through all folders and files(*.orbis).
        for (File entry : Objects.requireNonNull(packsDirectory.listFiles())) {
            if (!entry.isDirectory()) {
                loadPack(entry);
            }
        }
    }

    @Nullable
    public Pack getPack(String name) {
        for (Pack pack : loadedPacks.keySet()) {
            if (pack.getName().equalsIgnoreCase(name)) return pack;
        }
        return null;
    }

    public void extractPack(File destinationFolder, Pack pack) {
        try {
            new ZipFile(loadedPacks.get(pack)).extractAll(destinationFolder.getPath());
        } catch (IOException ex) {
            Orbis.getLogger().error("Failed to extract pack.");
        }
    }

    private void loadPack(File packFile) {
        try {
            ZipFile zipFile = new ZipFile(packFile);
            if (zipFile.isValidZipFile()) {
                InputStream inputStream = zipFile.getInputStream(zipFile.getFileHeader("pack.json"));
                byte[] buffer = ByteStreams.toByteArray(inputStream);
                Reader reader = CharSource.wrap(new String(buffer)).openStream();
                Pack pack = Orbis.getGson().fromJson(reader, Pack.class);
                loadedPacks.put(pack, packFile);
            }
        } catch (IOException ex) {
            Orbis.getLogger().error("Couldn't load pack: {}", packFile.getName());
        }
    }

}
