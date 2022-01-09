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

import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.azortis.orbis.generator.terrain.Terrain;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Interface for fetching {@link com.azortis.orbis.generator.Dimension} datafiles.
 */
public interface DataAccess {
    String BIOMES_FOLDER = "/biomes/";
    String GENERATORS_FOLDER = "/generators/";
    String NOISE_GENERATORS_FOLDER = GENERATORS_FOLDER + "/noise/";
    String DISTRIBUTOR_FOLDER = GENERATORS_FOLDER + "/distributor/";
    String TERRAIN_FOLDER = GENERATORS_FOLDER + "/terrain/";

    default File getDataFile(@NotNull Class<?> type, @NotNull String dataFileName)
            throws FileNotFoundException, IllegalArgumentException {
        if (type == Biome.class) {
            return getBiomeFile(dataFileName);
        } else if (type == NoiseGenerator.class) {
            return getNoiseGeneratorFile(dataFileName);
        } else if (type == Distributor.class) {
            return getDistributorFile(dataFileName);
        } else if (type == Terrain.class) {
            return getTerrainFile(dataFileName);
        }
        throw new IllegalArgumentException("Unsupported datatype " + type);
    }

    File getBiomeFile(@NotNull String biomeFileName) throws FileNotFoundException;

    @NotNull List<File> getBiomeFiles();

    @NotNull List<String> getBiomes();

    File getNoiseGeneratorFile(@NotNull String noiseGeneratorFileName) throws FileNotFoundException;

    @NotNull List<File> getNoiseGeneratorFiles();

    @NotNull List<String> getNoiseGenerators();

    File getDistributorFile(@NotNull String distributorFileName) throws FileNotFoundException;

    @NotNull List<File> getDistributorFiles();

    @NotNull List<String> getDistributors();

    File getTerrainFile(@NotNull String terrainFileName) throws FileNotFoundException;

    @NotNull List<File> getTerrainFiles();

    @NotNull List<String> getTerrainGenerators();

}
