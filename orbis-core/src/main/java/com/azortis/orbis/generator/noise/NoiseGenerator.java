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

package com.azortis.orbis.generator.noise;

import com.azortis.orbis.pack.studio.annotations.SupportAnonymous;
import com.azortis.orbis.pack.studio.annotations.Typed;
import com.azortis.orbis.util.Inject;
import com.azortis.orbis.world.World;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Inject
@Typed
@SupportAnonymous
public abstract class NoiseGenerator {

    protected final String name;
    protected final Key type;
    protected final long seed;
    protected final double frequency;

    @Inject
    private transient World world;

    protected NoiseGenerator(@Nullable String name, @NotNull Key type, long seed, double frequency) {
        this.name = name;
        this.type = type;
        this.seed = seed;
        this.frequency = frequency;
    }

    /**
     * Noise generators can either be defined as fields or as a separate generator config file
     * hence why name is an optional value.
     *
     * @return The name of the noise generator
     */
    public boolean hasName() {
        return name != null;
    }

    public String name() {
        return name;
    }

    public Key type() {
        return type;
    }

    public long seed() {
        return seed;
    }

    public abstract double noise(double x);

    public abstract double noise(double x, double z);

    public abstract double noise(double x, double y, double z);

    protected long getNoiseSeed() {
        return ((world.getWorldInfo().seed()) << 16) & (seed << 32); // Really just a fancy way to mix 2 seeds
    }

}
