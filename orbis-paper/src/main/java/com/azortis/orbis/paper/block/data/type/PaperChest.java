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

import com.azortis.orbis.block.BlockFace;
import com.azortis.orbis.block.data.type.Chest;
import com.azortis.orbis.paper.block.BlockAdapter;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class PaperChest extends PaperBlockData implements Chest {

    public PaperChest(BlockData handle) {
        super(handle);
    }

    @Override
    public @NotNull BlockFace getFacing() {
        return BlockAdapter.fromPaper(((org.bukkit.block.data.type.Chest) getHandle()).getFacing());
    }

    @Override
    public void setFacing(@NotNull BlockFace facing) {
        ((org.bukkit.block.data.type.Chest) getHandle()).setFacing(BlockAdapter.toPaper(facing));
    }

    @Override
    public @NotNull Set<BlockFace> getFaces() {
        return ((org.bukkit.block.data.type.Chest) getHandle()).getFaces().stream().map(BlockAdapter::fromPaper)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isWaterLogged() {
        return ((org.bukkit.block.data.type.Chest) getHandle()).isWaterlogged();
    }

    @Override
    public void setWaterLogged(boolean waterLogged) {
        ((org.bukkit.block.data.type.Chest) getHandle()).setWaterlogged(waterLogged);
    }

    @Override
    public @NotNull Type getType() {
        return fromPaper(((org.bukkit.block.data.type.Chest) getHandle()).getType());
    }

    @Override
    public void setType(@NotNull Type type) {
        ((org.bukkit.block.data.type.Chest) getHandle()).setType(toPaper(type));
    }

    public static org.bukkit.block.data.type.Chest.Type toPaper(Type type){
        return switch (type){
            case SINGLE -> org.bukkit.block.data.type.Chest.Type.SINGLE;
            case LEFT -> org.bukkit.block.data.type.Chest.Type.LEFT;
            case RIGHT -> org.bukkit.block.data.type.Chest.Type.RIGHT;
        };
    }

    public static Type fromPaper(org.bukkit.block.data.type.Chest.Type type){
        return switch (type){
            case SINGLE -> Type.SINGLE;
            case LEFT -> Type.LEFT;
            case RIGHT -> Type.RIGHT;
        };
    }

}
