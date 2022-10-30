/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2022 Azortis
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

package com.azortis.orbis.generator.terrain;

import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.pack.Inject;
import com.azortis.orbis.pack.studio.annotations.Typed;
import com.azortis.orbis.world.World;
import lombok.Getter;
import net.kyori.adventure.key.Key;

@Getter
@Typed
@Inject
public abstract class Terrain {

    protected final String name;
    protected final Key type;

    @Inject
    private transient World world;
    @Inject(isChild = true)
    private transient Biome biome;

    protected Terrain(String name, Key type) {
        this.name = name;
        this.type = type;
    }

    public abstract double getTerrainHeight(final int x, final int z, double biomeWeight);

    public String name() {
        return name;
    }

    public Key type() {
        return type;
    }

    public World world() {
        return world;
    }

    public Biome biome() {
        return biome;
    }

}
