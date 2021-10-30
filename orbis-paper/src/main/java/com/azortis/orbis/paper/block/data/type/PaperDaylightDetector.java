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

import com.azortis.orbis.block.data.type.DaylightDetector;
import com.azortis.orbis.paper.block.data.PaperBlockData;
import org.bukkit.block.data.BlockData;

public class PaperDaylightDetector extends PaperBlockData implements DaylightDetector {

    public PaperDaylightDetector(BlockData handle) {
        super(handle);
    }

    @Override
    public int getPower() {
        return ((org.bukkit.block.data.type.DaylightDetector) getHandle()).getPower();
    }

    @Override
    public void setPower(int power) {
        ((org.bukkit.block.data.type.DaylightDetector) getHandle()).setPower(power);
    }

    @Override
    public int getMaximumPower() {
        return ((org.bukkit.block.data.type.DaylightDetector) getHandle()).getMaximumPower();
    }

    @Override
    public boolean isInverted() {
        return ((org.bukkit.block.data.type.DaylightDetector) getHandle()).isInverted();
    }

    @Override
    public void setInverted(boolean inverted) {
        ((org.bukkit.block.data.type.DaylightDetector) getHandle()).setInverted(inverted);
    }
}
