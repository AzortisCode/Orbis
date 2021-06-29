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

package com.azortis.orbis.paper.block.data;

import com.azortis.orbis.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

public class PaperBisected extends PaperBlockData implements Bisected {

    public PaperBisected(BlockData handle) {
        super(handle);
    }

    @Override
    public @NotNull Half getHalf() {
        return fromPaper(((org.bukkit.block.data.Bisected) getHandle()).getHalf());
    }

    @Override
    public void setHalf(@NotNull Half half) {
        ((org.bukkit.block.data.Bisected) getHandle()).setHalf(toPaper(half));
    }

    public static org.bukkit.block.data.Bisected.Half toPaper(Half half){
        return switch (half){
            case TOP -> org.bukkit.block.data.Bisected.Half.TOP;
            case BOTTOM -> org.bukkit.block.data.Bisected.Half.BOTTOM;
        };
    }

    public static Half fromPaper(org.bukkit.block.data.Bisected.Half half){
        return switch (half){
            case TOP -> Half.TOP;
            case BOTTOM -> Half.BOTTOM;
        };
    }

}
