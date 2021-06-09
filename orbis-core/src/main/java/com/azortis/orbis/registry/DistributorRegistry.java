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
import com.azortis.orbis.container.Container;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.biome.SimpleDistributor;
import com.azortis.orbis.generator.biome.complex.ComplexDistributor;
import com.azortis.orbis.util.NamespaceId;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistributorRegistry implements GeneratorRegistry<Distributor> {

    private static final String DISTRIBUTOR_DIRECTORY = "/generators/distributor/";
    private static final Map<NamespaceId, Class<? extends Distributor>> distributorClasses = new HashMap<>();

    static {
        distributorClasses.put(new NamespaceId("orbis:complex"), ComplexDistributor.class);
        distributorClasses.put(new NamespaceId("orbis:simple"), SimpleDistributor.class);
    }

    public DistributorRegistry(){
    }

    @Override
    public Distributor loadType(Container container, String name, Object... context) {
        File distributorFile = new File(container.getSettingsFolder(), DISTRIBUTOR_DIRECTORY + name + ".json");
        try {
            Distributor distributor = Orbis.getGson().fromJson(new FileReader(distributorFile), Distributor.class);
            distributor.setContainer(container);
            distributor.setSettingsFolder(new File(container.getSettingsFolder(), DISTRIBUTOR_DIRECTORY + name + "/"));
            distributor.load();
            return distributor;
        }catch (FileNotFoundException ex){
            Orbis.getLogger().error("Distributor file {} not found", name);
        }
        return null;
    }

    @Override
    public List<String> getEntries(Container container) {
        return null;
    }

    @Override
    public void createFolders(Container container) {
        if(!new File(container.getSettingsFolder(), DISTRIBUTOR_DIRECTORY).mkdirs()){
            Orbis.getLogger().error("Unable to create distributor directory for container {}", container.getName());
        }
    }

    @Override
    public File getFolder(Container container) {
        return new File(container.getSettingsFolder(), DISTRIBUTOR_DIRECTORY);
    }

    @Override
    public void registerTypeClass(NamespaceId namespaceId, Class<? extends Distributor> typeClass) {
        //TODO do this I'm too lazy
    }

    @Override
    public Class<? extends Distributor> getTypeClass(NamespaceId namespaceId) {
        return distributorClasses.get(namespaceId);
    }

    @Override
    public List<NamespaceId> getTypeNamespaceIds() {
        return new ArrayList<>(distributorClasses.keySet());
    }
}
