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

package com.azortis.orbis.paper.nms;

import com.azortis.orbis.block.BlockState;
import org.bukkit.block.data.BlockData;

public interface INMSBinding {

    BlockState getBlock(BlockData blockData);

    BlockData getBlockData(int stateId);

    default BlockData getBlockData(BlockState stateHolder){
        return getBlockData(stateHolder.getStateId());
    }

}
