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

package com.azortis.orbis.generator.biome.complex.requirement;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class MaxStrength extends Requirement {

    private final String tag;
    private final double max;

    public MaxStrength(Key type, String tag, double max) {
        super(type);
        this.tag = tag;
        this.max = max;
    }

    @Override
    public boolean isAchieved(Map<String, Double> context) {
        if (context.containsKey(tag)) {
            return context.get(tag) <= max;
        } else {
            throw new IllegalStateException("Tag " + tag + " wasn't found in context!");
        }
    }

    @Override
    public @NotNull Type getType() {
        return Type.STRENGTH;
    }
}
