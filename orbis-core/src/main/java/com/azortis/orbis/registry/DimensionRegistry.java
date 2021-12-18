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

package com.azortis.orbis.registry;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.world.Container;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class DimensionRegistry implements Registry<Dimension> {

    public DimensionRegistry() {
    }

    @Override
    public Dimension loadType(Container container, String name, Object... context) {
        File dimensionFile = new File(container.getSettingsFolder(), name + ".json");
        try {
            return Orbis.getGson().fromJson(new FileReader(dimensionFile), Dimension.class);
        } catch (FileNotFoundException ex) {
            Orbis.getLogger().error("Dimension file {} not found!", name);
        }
        return null;
    }

    @Override
    public List<String> getEntries(Container container) {
        return null;
    }

    @Override
    public void createFolders(Container container) {
        //Do nothing, dimensions are root
    }

    @Override
    public File getFolder(Container container) {
        return container.getSettingsFolder();
    }

}
