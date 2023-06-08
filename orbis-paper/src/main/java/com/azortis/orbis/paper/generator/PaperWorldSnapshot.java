/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2023 Azortis
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

import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.generator.framework.Engine;
import com.azortis.orbis.generator.framework.WorldSnapshot;
import com.azortis.orbis.world.World;
import org.jetbrains.annotations.NotNull;

public final class PaperWorldSnapshot extends WorldSnapshot {

    private final org.bukkit.World handle;

    public PaperWorldSnapshot(@NotNull World world, @NotNull Dimension dimension,
                              @NotNull Engine engine, @NotNull org.bukkit.World handle) {
        super(world, dimension, engine);
        this.handle = handle;
    }


}
