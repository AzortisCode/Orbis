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

package com.azortis.orbis;

import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;

public final class WorldInfo {

    private final int dataVersion;
    private final String packName;
    private final String dimensionFile;
    private final long seed;

    WorldInfo(int dataVersion, @NotNull String packName, @NotNull String dimensionFile, long seed) {
        this.dataVersion = dataVersion;
        this.packName = packName;
        this.dimensionFile = dimensionFile;
        this.seed = seed;
    }

    @SuppressWarnings("ConstantConditions")
    WorldInfo(@NotNull NBTCompound tag) {
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

    NBTCompound serialize() {
        MutableNBTCompound tag = new MutableNBTCompound();
        tag.set("dataVersion", NBT.Int(dataVersion));
        tag.set("packName", NBT.String(packName));
        tag.set("dimensionFile", NBT.String(dimensionFile));
        tag.set("seed", NBT.Long(seed));
        return tag.toCompound();
    }

}
