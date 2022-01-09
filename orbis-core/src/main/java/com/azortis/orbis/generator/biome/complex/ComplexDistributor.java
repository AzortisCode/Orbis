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

package com.azortis.orbis.generator.biome.complex;

import com.azortis.orbis.generator.biome.Biome;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.util.Inject;
import com.azortis.orbis.util.Invoke;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Inject
public class ComplexDistributor extends Distributor {

    private ComplexDistributor(String name, Key providerKey) {
        super(name, providerKey);
    }

    @Invoke
    private void setup() {

    }

    @Override
    public @NotNull Set<Biome> getPossibleBiomes() {
        return null;
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        return null;
    }

    @Override
    public @NotNull Set<Key> getNativePossibleBiomes() {
        return null;
    }

    @Override
    public Key getNativeBiome(int x, int y, int z) {
        return null;
    }

}
