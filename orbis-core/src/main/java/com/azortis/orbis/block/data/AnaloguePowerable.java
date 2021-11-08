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

package com.azortis.orbis.block.data;

import com.azortis.orbis.block.property.IntegerProperty;

import java.util.Set;

public interface AnaloguePowerable extends BlockData {

    IntegerProperty POWER = new IntegerProperty("power", Set.of(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15));

    default int getPower(){
        return getProperty(POWER);
    }

    default void setPower(int power){
        setProperty(POWER, power);
    }

    default int getMaximumPower(){
        return 15;
    }

}
