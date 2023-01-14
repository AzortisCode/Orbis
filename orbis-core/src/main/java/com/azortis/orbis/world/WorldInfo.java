/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2023 Azortis
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

package com.azortis.orbis.world;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IntBinaryTag;
import net.kyori.adventure.nbt.LongBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

/**
 * Stored information for a World using Orbis.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.EXPERIMENTAL, since = "0.3-Alpha")
public final class WorldInfo {

    public static int DATA_VERSION = 1;

    private final int dataVersion;
    private final String packName;
    private final String dimensionFile;
    private final long seed;

    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = {"com.azortis.orbis.world",
            "com.azortis.orbis.pack.studio"})
    public WorldInfo(@NotNull String packName, @NotNull String dimensionFile, long seed) {
        this.dataVersion = DATA_VERSION;
        this.packName = packName;
        this.dimensionFile = dimensionFile;
        this.seed = seed;
    }

    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.world")
    WorldInfo(@NotNull CompoundBinaryTag tag) {
        this.dataVersion = tag.getInt("dataVersion");
        this.packName = tag.getString("packName");
        this.dimensionFile = tag.getString("dimensionFile");
        this.seed = tag.getLong("seed");
    }

    public int dataVersion() {
        return dataVersion;
    }

    public String packName() {
        return packName;
    }

    public String dimensionFile() {
        return dimensionFile;
    }

    public long seed() {
        return seed;
    }

    public WorldInfo seed(long seed) {
        return new WorldInfo(packName, dimensionFile, seed);
    }

    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.world")
    CompoundBinaryTag toNBT() {
        return CompoundBinaryTag.builder()
                .put("dataVersion", IntBinaryTag.of(dataVersion))
                .put("packName", StringBinaryTag.of(packName))
                .put("dimensionFile", StringBinaryTag.of(dimensionFile))
                .put("seed", LongBinaryTag.of(seed))
                .build();
    }

}
