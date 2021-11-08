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

package com.azortis.orbis.block.data.type;

import com.azortis.orbis.block.data.BlockData;
import com.azortis.orbis.block.property.IntegerProperty;

import java.util.Set;

public interface TurtleEgg extends BlockData {

    IntegerProperty EGGS = new IntegerProperty("eggs", Set.of(1,2,3,4));
    IntegerProperty HATCH = new IntegerProperty("hatch", Set.of(0,1,2));

    default int getEggs(){
        return getProperty(EGGS);
    }

    default void setEggs(int eggs){
        setProperty(EGGS, eggs);
    }

    default int getMinimumEggs(){
        return 1;
    }

    default int getMaximumEggs(){
        return 4;
    }

    default int getHatch(){
        return getProperty(HATCH);
    }

    default void setHatch(int hatch){
        setProperty(HATCH, hatch);
    }

    default int getMaximumHatch(){
        return 2;
    }

}
