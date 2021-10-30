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

package com.azortis.orbis.paper.block.data.type;

import com.azortis.orbis.block.data.type.CaveVinesPlant;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;

public class PaperCaveVinesPlant extends PaperBlockData implements CaveVinesPlant {

    public PaperCaveVinesPlant(BlockData handle) {
        super(handle);
    }

    @Override
    public boolean isBerries() {
        return ((org.bukkit.block.data.type.CaveVinesPlant) getHandle()).isBerries();
    }

    @Override
    public void setBerries(boolean berries) {
        ((org.bukkit.block.data.type.CaveVinesPlant) getHandle()).setBerries(berries);
    }
}
