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

package com.azortis.orbis.block.property;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.TreeSet;

public final class IntegerProperty extends Property<Integer> {

    private final int min, max;

    private IntegerProperty(final @NotNull String key, final int min, final int max) {
        super(key, Integer.class, createIntegerSet(min, max));
        this.min = min;
        this.max = max;
    }

    private static Set<Integer> createIntegerSet(int min, int max) {
        Set<Integer> values = new TreeSet<>();
        for (int i = min; i <= max; i++) {
            values.add(i);
        }
        return values;
    }

    static IntegerProperty create(final @NotNull String key, final int min, final int max) {
        return new IntegerProperty(key, min, max);
    }

    @Override
    public @NotNull Integer getValueFor(@NotNull String value) {
        try {
            final int val = Integer.parseInt(value);
            if (!getValues().contains(val)) {
                throw new IllegalArgumentException("Invalid int value: " + value + ". Must be in " + getValues());
            }
            return val;
        } catch (final NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid int value: " + value + ". Not an int.");
        }
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

}
