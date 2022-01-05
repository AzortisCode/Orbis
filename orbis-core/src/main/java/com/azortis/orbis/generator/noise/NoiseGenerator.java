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

@Inject
public abstract class NoiseGenerator {

    protected final Key type;
    protected final long seed;
    protected final double frequency;

    @Inject
    private transient World world;

    public NoiseGenerator(Key type, long seed, double frequency) {
        this.type = type;
        this.seed = seed;
        this.frequency = frequency;
    }

    public abstract double noise(double x);

    public abstract double noise(double x, double z);

    public abstract double noise(double x, double y, double z);

    protected long getNoiseSeed() {
        return ((world.getWorldInfo().seed()) << 16) & (seed << 32); // Really just a fancy way to mix 2 seeds
    }

}
