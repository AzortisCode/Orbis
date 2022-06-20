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

package com.azortis.orbis.generator.biome.complex.modifier;

import net.kyori.adventure.key.Key;

import java.util.Map;

public final class RangedLinearModifier extends Modifier {

    private final String tag;
    private final double[] fullRange;
    private final double min;
    private final double max;

    private RangedLinearModifier(Key type, String tag, double[] fullRange, double min, double max) {
        super(type);
        this.tag = tag;
        this.fullRange = fullRange;
        this.min = min;
        this.max = max;
    }

    @Override
    public double modify(double currentStrength, Map<String, Double> noiseContext, Map<String, Double> strengthContext) {
        double tagNoise = noiseContext.get(tag);
        double modifier = 1.0d;

        double fullMin = Math.min(fullRange[0], fullRange[1]);
        double fullMax = Math.max(fullRange[0], fullRange[1]);

        // Checks if the tagNoise isn't between the fullRange, if it is, then the modifier is 1.
        if (!(fullMax >= tagNoise && fullMin <= tagNoise)) {

            // Checks if the tagNoise value is in between min and max, if so, we know it at least won't be 0
            if (tagNoise > min && tagNoise < max) {

                // Checks if it is between the min and fullMin if not, it is between max and fullMax
                // Does a simple linear algebra formula following the format: modifier = slope * tagNoise
                // Slope is 1 / deltaX -> which is always a positive value
                // Its always positive since we make sure to always subtract with the smallest number
                // Which in case of min is always the min since min < fullMin and the opposite with max
                if (tagNoise < fullMin) {
                    double x0 = min + 1.0d;
                    double dx = (fullMin + 1.0d) - x0;
                    double slope = 1.0d / dx;
                    modifier = ((tagNoise + 1.0d) - x0) * slope;
                } else {
                    double x0 = fullMax + 1.0d;
                    double dx = (max + 1.0d) - x0;
                    double slope = 1.0d / dx;
                    modifier = ((tagNoise + 1.0d) - x0) * slope;
                }
            } else {
                modifier = 0.0d; // It is outside any of the ranges and thus 0.
            }
        }
        return modifier * currentStrength;
    }
}
