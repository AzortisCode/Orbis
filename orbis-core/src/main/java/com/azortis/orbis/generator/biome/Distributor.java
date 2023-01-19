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

import com.azortis.orbis.pack.Inject;
import com.azortis.orbis.pack.Invoke;
import com.azortis.orbis.pack.studio.annotations.Description;
import com.azortis.orbis.pack.studio.annotations.Entries;
import com.azortis.orbis.pack.studio.annotations.Required;
import com.azortis.orbis.pack.studio.annotations.Typed;
import com.azortis.orbis.world.World;
import com.google.gson.annotations.SerializedName;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Set;

@Typed
@Inject
@Description("Generator object that handles with the distribution of biomes.")
public abstract class Distributor {

    @Required
    @Description("The name of the distributor instance, must be same as file name without the *.json suffix.")
    protected final String name;

    @Required
    @Description("The type of distributor algorithm to use.")
    protected final Key type;


    @Required
    @Entries(Biome.class)
    @SerializedName("biomes")
    @Description("All the possible biomes from this distributor.")
    private Set<String> biomeNames;

    // Biomes are separate files, so we inject them here.
    @Inject(fieldName = "biomeNames", collectionType = HashSet.class, parameterizedType = Biome.class)
    private transient Set<Biome> biomes;

    @Inject
    private transient World world;

    protected Distributor(@NotNull String name, @NotNull Key type) {
        this.name = name;
        this.type = type;
    }

    @Invoke(when = Invoke.Order.MID_INJECTION)
    private void setup() {
        // Make sets immutable
        this.biomeNames = Set.copyOf(biomeNames);
        this.biomes = Set.copyOf(biomes);
    }

    public @NotNull String name() {
        return name;
    }

    public @NotNull Key type() {
        return type;
    }

    public @Unmodifiable @NotNull Set<String> biomeNames() {
        return biomeNames;
    }

    public @Unmodifiable @NotNull Set<Biome> biomes() {
        return biomes;
    }

    public @NotNull Biome getBiome(@NotNull String name) throws IllegalArgumentException {
        for (Biome biome : biomes) {
            if (biome.name().equalsIgnoreCase(name)) return biome;
        }
        throw new IllegalArgumentException("Biome by name " + name + " is not registered with this distributor!");
    }

    public @NotNull World world() {
        return world;
    }

    public abstract Biome getBiome(int x, int y, int z);

    public abstract Biome getBiome(double x, double y, double z);

}
