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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

public record BiomeSection(@NotNull Biome biome, double biomeStrength,
                           @Unmodifiable @NotNull Map<Biome, Double> biomeStrengths,
                           @Unmodifiable @NotNull Map<String, Double> strengthMap) {

    public BiomeSection(@NotNull Biome biome, double biomeStrength,
                        @NotNull Map<Biome, Double> biomeStrengths, @NotNull Map<String, Double> strengthMap) {
        this.biome = biome;
        this.biomeStrength = biomeStrength;
        this.biomeStrengths = Map.copyOf(biomeStrengths);
        this.strengthMap = Map.copyOf(strengthMap);
    }

}
