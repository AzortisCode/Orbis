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

package com.azortis.orbis.generator.noise;

import com.azortis.orbis.World;
import com.azortis.orbis.util.Inject;
import net.kyori.adventure.key.Key;

public abstract class NoiseGenerator {

    private Key type;
    private long seed;
    private double frequency;

    private @Inject
    transient World world;

    public double sample(double x) {
        return sample(x, seed);
    }

    public double sample(double x, double z) {
        return sample(x, z, seed);
    }

    public double sample(double x, double y, double z) {
        return sample(x, y, z, seed);
    }

    protected abstract double sample(double x, long seed);

    protected abstract double sample(double x, double z, long seed);

    protected abstract double sample(double x, double y, double z, long seed);

}
