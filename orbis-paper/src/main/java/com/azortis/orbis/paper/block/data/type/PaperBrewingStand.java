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

import com.azortis.orbis.block.data.type.BrewingStand;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PaperBrewingStand extends PaperBlockData implements BrewingStand {

    public PaperBrewingStand(BlockData handle) {
        super(handle);
    }

    @Override
    public boolean hasBottle(int bottle) {
        return ((org.bukkit.block.data.type.BrewingStand) getHandle()).hasBottle(bottle);
    }

    @Override
    public void setBottle(int bottle, boolean has) {
        ((org.bukkit.block.data.type.BrewingStand) getHandle()).setBottle(bottle, has);
    }

    @Override
    public @NotNull Set<Integer> getBottles() {
        return ((org.bukkit.block.data.type.BrewingStand) getHandle()).getBottles();
    }

    @Override
    public int getMaximumBottles() {
        return ((org.bukkit.block.data.type.BrewingStand) getHandle()).getMaximumBottles();
    }
}
