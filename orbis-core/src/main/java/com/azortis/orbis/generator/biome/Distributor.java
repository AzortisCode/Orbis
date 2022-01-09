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

package com.azortis.orbis.generator.biome;

import com.azortis.orbis.World;
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.util.Inject;
import com.azortis.orbis.util.Invoke;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

@Inject
public abstract class Distributor {

    protected final String name;
    protected final Key type;

    @Inject
    private transient World world;
    private transient File settingsFolder;

    protected Distributor(String name, Key type) {
        this.name = name;
        this.type = type;
    }

    @Invoke(when = Invoke.Order.MID_INJECTION)
    private void setup() {
        // Set up the settings folder so that Distributor implementations can load their datafiles before injection
        this.settingsFolder = new File(world.getSettingsFolder() +
                DataAccess.DISTRIBUTOR_FOLDER + "/" + name + "/");
    }

    public String name() {
        return name;
    }

    public Key type() {
        return type;
    }

    public World world() {
        return world;
    }

    public File settingsFolder() {
        return settingsFolder;
    }

    public abstract @NotNull Set<Biome> getBiomes();

    public abstract Biome getBiome(int x, int y, int z);

    public abstract Biome getBiome(double x, double y, double z);

    public abstract @NotNull Set<Key> getNativeBiomes();

    public abstract Key getNativeBiome(int x, int y, int z);

}
