/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2022 Azortis
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

import org.apache.commons.io.FilenameUtils;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A directory based {@link DataAccess}, which uses local directories as its data root.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.pack")
public class DirectoryDataAccess extends DataAccess {
    protected final File dataDirectory;

    public DirectoryDataAccess(File dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    @Override
    protected @NotNull File getFile(@NotNull String dataPath, @NotNull String entryName) throws FileNotFoundException {
        String trimmedDataPath = dataPath.replace("**", "").trim();
        File file = new File(this.dataDirectory + trimmedDataPath, entryName + ".json");
        if (file.exists()) return file;
        throw new FileNotFoundException("File: " + file.getPath() + " doesn't exist!");
    }

    @Override
    public @NotNull @Unmodifiable Set<File> getFiles(@NotNull String dataPath) {
        File directory;
        boolean searchSubDirs = dataPath.endsWith("**");
        if (searchSubDirs) {
            directory = new File(this.dataDirectory + dataPath.replace("**", "").trim());
        } else {
            directory = new File(this.dataDirectory + dataPath.trim());
        }
        if (directory.exists()) {
            if (!searchSubDirs) {
                File[] files = directory.listFiles(File::isFile);
                if (files != null) {
                    return Set.of(files);
                }
            } else {
                File[] rootFiles = directory.listFiles(File::isFile);
                Set<File> files = new HashSet<>();
                if (rootFiles != null) files.addAll(Arrays.asList(rootFiles));

                // Search all sub folders.
                File[] rootSubDirs = directory.listFiles(File::isDirectory);
                if (rootSubDirs != null) {
                    List<File> subDirectories = new ArrayList<>(Arrays.asList(rootSubDirs));
                    ListIterator<File> directoryIterator = subDirectories.listIterator();
                    while (directoryIterator.hasNext()) {
                        File subDirectory = directoryIterator.next();

                        // First add all the files
                        File[] subDirectoryFiles = subDirectory.listFiles(File::isFile);
                        if (subDirectoryFiles != null) files.addAll(Arrays.asList(subDirectoryFiles));

                        // Add sub folders to the iterator
                        File[] subDirectoryDirs = subDirectory.listFiles(File::isDirectory);
                        if (subDirectoryDirs != null) {
                            Arrays.stream(subDirectoryDirs).forEach(directoryIterator::add);
                        }
                    }
                }
                return Set.copyOf(files);
            }
        }
        return Collections.emptySet();
    }

    @Override
    public @NotNull @Unmodifiable Set<String> getEntries(@NotNull String dataPath) {
        Set<File> files = getFiles(dataPath);
        if (!files.isEmpty()) {
            if (!dataPath.endsWith("**")) {
                return files.stream().map(File::getName).map(FilenameUtils::removeExtension)
                        .collect(Collectors.toUnmodifiableSet());
            } else {
                final String trimmedDataPath = dataPath.replace("**", "").trim();
                return files.stream().map(File::getPath)
                        .map(entry -> entry.substring(entry.indexOf(entry.indexOf(trimmedDataPath))))
                        .map(FilenameUtils::removeExtension).collect(Collectors.toUnmodifiableSet());
            }
        }
        return Collections.emptySet();
    }

}
