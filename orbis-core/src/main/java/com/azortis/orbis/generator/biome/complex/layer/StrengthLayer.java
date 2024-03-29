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

package com.azortis.orbis.generator.biome.complex.layer;

import com.azortis.orbis.generator.biome.complex.modifier.Modifier;
import com.azortis.orbis.pack.studio.annotations.ArrayType;
import com.azortis.orbis.pack.studio.annotations.Min;
import com.azortis.orbis.pack.studio.annotations.Required;

import java.util.Map;
import java.util.Set;

public record StrengthLayer(@Required String tag, @ArrayType(Modifier.class) Set<Modifier> modifiers,
                            @Required @Min(1) int precision) {

    public double calculate(Map<String, Double> noiseContext, Map<String, Double> strengthContext) {
        double strength = 1.0d;
        for (Modifier modifier : modifiers) {
            strength = modifier.modify(strength, noiseContext, strengthContext);
        }
        return (double) Math.round(precision * strength) / precision;
    }

}
