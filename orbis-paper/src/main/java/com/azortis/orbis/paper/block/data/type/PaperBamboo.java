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

import com.azortis.orbis.block.data.type.Bamboo;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

public class PaperBamboo extends PaperBlockData implements Bamboo {

    public PaperBamboo(BlockData handle) {
        super(handle);
    }

    @Override
    public int getAge() {
        return ((org.bukkit.block.data.type.Bamboo) getHandle()).getAge();
    }

    @Override
    public void setAge(int age) {
        ((org.bukkit.block.data.type.Bamboo) getHandle()).setAge(age);
    }

    @Override
    public int getMaximumAge() {
        return ((org.bukkit.block.data.type.Bamboo) getHandle()).getMaximumAge();
    }

    @Override
    public @NotNull Leaves getLeaves() {
        return fromPaper(((org.bukkit.block.data.type.Bamboo) getHandle()).getLeaves());
    }

    @Override
    public void setLeaves(@NotNull Leaves leaves) {
        ((org.bukkit.block.data.type.Bamboo) getHandle()).setLeaves(toPaper(leaves));
    }

    @Override
    public int getStage() {
        return ((org.bukkit.block.data.type.Bamboo) getHandle()).getStage();
    }

    @Override
    public void setStage(int stage) {
        ((org.bukkit.block.data.type.Bamboo) getHandle()).setStage(stage);
    }

    @Override
    public int getMaximumStage() {
        return ((org.bukkit.block.data.type.Bamboo) getHandle()).getMaximumStage();
    }

    public static org.bukkit.block.data.type.Bamboo.Leaves toPaper(Leaves leaves){
        return switch (leaves){
            case NONE -> org.bukkit.block.data.type.Bamboo.Leaves.NONE;
            case SMALL -> org.bukkit.block.data.type.Bamboo.Leaves.SMALL;
            case LARGE -> org.bukkit.block.data.type.Bamboo.Leaves.LARGE;
        };
    }

    public static Leaves fromPaper(org.bukkit.block.data.type.Bamboo.Leaves leaves){
        return switch (leaves){
            case NONE -> Leaves.NONE;
            case SMALL -> Leaves.SMALL;
            case LARGE -> Leaves.LARGE;
        };
    }

}
