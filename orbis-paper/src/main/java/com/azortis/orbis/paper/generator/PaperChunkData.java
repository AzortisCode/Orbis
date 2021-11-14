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

package com.azortis.orbis.paper.generator;

import com.azortis.orbis.generator.ChunkData;
import com.azortis.orbis.generator.Dimension;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;

import java.util.Locale;

public class PaperChunkData extends ChunkData {

    private final ChunkGenerator.ChunkData handle;

    public PaperChunkData(Dimension dimension, ChunkGenerator.ChunkData handle) {
        super(dimension);
        this.handle = handle;
    }

    @Override
    protected void setBlock(int x, int y, int z, String blockId) {
        if (blockId.equals("water")) {
            handle.setBlock(x, y, z, Material.WATER);
            return;
        }
        handle.setBlock(x, y, z, Material.valueOf(blockId.toUpperCase(Locale.ENGLISH)));
    }

    public ChunkGenerator.ChunkData getHandle() {
        return handle;
    }

}
