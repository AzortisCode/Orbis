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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class WorldDataAccess implements DataAccess {
    private final World world;

    public WorldDataAccess(World world) {
        this.world = world;
    }

    @Override
    public File getDistributorFile(@NotNull String distributorFileName) throws FileNotFoundException {
        File distributorFile = new File(world.getSettingsFolder() + DISTRIBUTOR_FOLDER,
                distributorFileName + ".json");
        if (distributorFile.exists()) {
            return distributorFile;
        }
        throw new FileNotFoundException("No distributor file by the fieldName " + distributorFileName + " exists in " + world);
    }

    @Override
    public @NotNull List<File> getDistributorFiles() {
        return getFileEntries(DISTRIBUTOR_FOLDER);
    }

    @Override
    public @NotNull List<String> getDistributors() {
        return getEntries(getDistributorFiles());
    }

    @Override
    public File getTerrainFile(@NotNull String terrainFileName) throws FileNotFoundException {
        File terrainFile = new File(world.getSettingsFolder() + TERRAIN_FOLDER,
                terrainFileName + ".json");
        if (terrainFile.exists()) {
            return terrainFile;
        }
        throw new FileNotFoundException("No terrain file by the fieldName " + terrainFileName + " exists in " + world);
    }

    @Override
    public @NotNull List<File> getTerrainFiles() {
        return getFileEntries(TERRAIN_FOLDER);
    }

    @Override
    public @NotNull List<String> getTerrainGenerators() {
        return getEntries(getTerrainFiles());
    }

    private @NotNull List<File> getFileEntries(@NotNull String directory) {
        File directoryFile = new File(world.getSettingsFolder() + directory);
        if (directoryFile.exists()) {
            File[] entryFiles = directoryFile.listFiles(File::isFile);
            if (entryFiles != null) {
                return Arrays.asList(entryFiles);
            }
        }
        return Collections.emptyList();
    }

    private @NotNull List<String> getEntries(@NotNull List<File> entryFiles) {
        return entryFiles.stream().map(File::getName).map(FilenameUtils::removeExtension)
                .collect(Collectors.toList());
    }

}
