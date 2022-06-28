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

package com.azortis.orbis.pack.data;

import com.azortis.orbis.World;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public final class WorldDataAccess extends DataAccess {
    private final World world;

    public WorldDataAccess(World world) {
        this.world = world;
    }

    @Override
    protected @NotNull File getFile(@NotNull String dataPath, @NotNull String entryName) throws FileNotFoundException {
        String trimmedDataPath = dataPath.replace("**", "").trim();
        File file = new File(world.getSettingsFolder() + trimmedDataPath, entryName + ".json");
        if (file.exists()) return file;
        throw new FileNotFoundException("File: " + file.getPath() + " doesn't exist!");
    }

    @Override
    public @NotNull Set<File> getFiles(@NotNull String dataPath) {
        File directory;
        boolean searchSubFolders = dataPath.endsWith("**");
        if (searchSubFolders) {
            directory = new File(world.getSettingsFolder() + dataPath.replace("**", "").trim());
        } else {
            directory = new File(world.getSettingsFolder() + dataPath.trim());
        }
        if (directory.exists()) {
            if (!searchSubFolders) {
                File[] files = directory.listFiles(File::isFile);
                if (files != null) {
                    return new HashSet<>(Arrays.asList(files));
                }
            } else {
                File[] rootFiles = directory.listFiles(File::isFile);
                Set<File> files = new HashSet<>();
                if (rootFiles != null) files.addAll(Arrays.asList(rootFiles));

                // Search all sub folders.
                File[] rootSubFolders = directory.listFiles(File::isDirectory);
                if (rootSubFolders != null) {
                    List<File> subFolders = new ArrayList<>(Arrays.asList(rootSubFolders));
                    ListIterator<File> folderIterator = subFolders.listIterator();
                    while (folderIterator.hasNext()) {
                        File subFolder = folderIterator.next();

                        // First add all the files
                        File[] subFolderFiles = subFolder.listFiles(File::isFile);
                        if (subFolderFiles != null) files.addAll(Arrays.asList(subFolderFiles));

                        // Add sub folders to the iterator
                        File[] subFolderFolders = subFolder.listFiles(File::isDirectory);
                        if (subFolderFolders != null) {
                            Arrays.stream(subFolderFolders).forEach(folderIterator::add);
                        }
                    }
                }
                return files;
            }
        }
        return Collections.emptySet();
    }

    @Override
    public @NotNull Set<String> getEntries(@NotNull String dataPath) {
        Set<File> files = getFiles(dataPath);
        if (!files.isEmpty()) {
            if (!dataPath.endsWith("**")) {
                return files.stream().map(File::getName).map(FilenameUtils::removeExtension).collect(Collectors.toSet());
            } else {
                final String trimmedDataPath = dataPath.replace("**", "").trim();
                return files.stream().map(File::getPath)
                        .map(entry -> entry.substring(entry.indexOf(entry.indexOf(trimmedDataPath))))
                        .map(FilenameUtils::removeExtension).collect(Collectors.toSet());
            }
        }
        return Collections.emptySet();
    }

}
