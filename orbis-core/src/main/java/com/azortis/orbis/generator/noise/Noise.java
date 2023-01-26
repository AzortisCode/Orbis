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

package com.azortis.orbis.generator.noise;

import com.azortis.orbis.pack.Inject;
import com.azortis.orbis.pack.studio.annotations.*;
import com.azortis.orbis.world.World;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Inject
@Typed
@SupportAnonymous
@Description("A source for procedural noise that is used for various algorithms.")
public abstract class Noise {

    @Required
    @Description("The name of the noise instance, must be same as file name without the *.json suffix.")
    protected final String name;

    @Required
    @Description("The type of noise algorithm to use.")
    protected final Key type;

    @Required
    @Description("The unique salt for the noise algorithm, will be mixed with world seed.")
    protected final long salt;

    @Required
    @Min(floating = 0d)
    @Description("The frequency to sample the noise from, used to smooth out the results.")
    protected final double frequency;

    @Inject
    private transient World world;

    protected Noise(@Nullable String name, @NotNull Key type, long salt, double frequency) {
        this.name = name;
        this.type = type;
        this.salt = salt;
        this.frequency = frequency;
    }

    /**
     * Noise generators can either be defined as fields or as a separate generator config file
     * hence why name is an optional value.
     *
     * @return The name of the noise generator
     * @since 0.3-Alpha
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

    public abstract double noise(double x);

    public abstract double noise(double x, double z);

    public abstract double noise(double x, double y, double z);

    protected long getNoiseSeed() {
        return ((world.getWorldInfo().seed()) << 16) & (salt << 32);
    }

}
