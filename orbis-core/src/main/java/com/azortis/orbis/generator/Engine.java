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

package com.azortis.orbis.generator;

import com.azortis.orbis.generator.biome.BiomeLayout;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.pack.Inject;
import com.azortis.orbis.pack.studio.annotations.Description;
import com.azortis.orbis.pack.studio.annotations.Required;
import com.azortis.orbis.pack.studio.annotations.Typed;
import com.azortis.orbis.world.World;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

@Typed
@Description("The foundation of the generator, that organizes the generation pipeline.")
public abstract class Engine {

    @Required
    @Description("The type of generator engine to use for this dimension.")
    private final Key type;

    @Inject
    protected transient World world;

    @Inject
    protected transient Dimension dimension;

    protected Engine(@NotNull Key type) {
        this.type = type;
    }

    public abstract void generateChunk(int chunkX, int chunkZ, @NotNull GeneratorChunkAccess chunkAccess);

    public abstract @NotNull BiomeLayout biomeLayout();

    public Key type() {
        return type;
    }

    public World world() {
        return world;
    }

    public Dimension dimension() {
        return dimension;
    }

    public Distributor distributor() {
        return world.getDimension().distributor();
    }

}
