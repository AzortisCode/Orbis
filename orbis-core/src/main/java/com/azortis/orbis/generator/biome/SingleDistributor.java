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

package com.azortis.orbis.generator.biome;

import com.azortis.orbis.pack.studio.annotations.Description;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

@Description("Distributor that only distributes one biome.")
public final class SingleDistributor extends Distributor {

    private SingleDistributor(@NotNull String name, @NotNull Key providerKey) {
        super(name, providerKey);
    }

    @Override
    protected @NotNull BiomeSection sample(int x, int z) {
        return new BiomeSection(biomes().iterator().next(), 1.0D, Collections.emptyMap());
    }

    @Override
    protected @NotNull BiomeSection sample(int x, int y, int z) {
        throw new UnsupportedOperationException("This distributor only supports 2d biomes");
    }

    @Override
    public @NotNull BiomeLayout layout() {
        return BiomeLayout.SURFACE;
    }
}
