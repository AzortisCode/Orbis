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

package com.azortis.orbis.generator.biome.complex.requirement;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public final class NoiseRanges extends Requirement {

    private final String tag;
    private final Set<Range> ranges;

    private NoiseRanges(Key type, String tag, Set<Range> ranges) {
        super(type);
        this.tag = tag;
        this.ranges = ranges;
    }

    @Override
    public boolean isAchieved(Map<String, Double> context) {
        if (context.containsKey(tag)) {
            double noiseValue = context.get(tag);
            for (Range range : ranges) {
                if (range.min <= noiseValue && range.max >= noiseValue) return true;
            }
        } else {
            throw new IllegalStateException("Tag " + tag + " wasn't found in context!");
        }
        return false;
    }

    @Override
    public @NotNull Type getType() {
        return Type.NOISE;
    }

    public record Range(double min, double max) {
    }

}
