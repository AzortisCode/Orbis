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

package com.azortis.orbis.generator.biome;

import net.kyori.adventure.key.Key;

public final class SingleDistributor extends Distributor {

    private SingleDistributor(String name, Key providerKey) {
        super(name, providerKey);
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        return biomes().iterator().next();
    }

    @Override
    public Biome getBiome(double x, double y, double z) {
        return biomes().iterator().next();
    }
}
