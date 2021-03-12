/*
 * MIT License
 *
 * Copyright (c) 2021 Azortis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.azortis.orbis.settings.registry;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.container.Container;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.biome.complex.ComplexDistributor;
import com.azortis.orbis.util.NamespaceId;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistributorRegistry implements GeneratorRegistry<Distributor> {

    private static final String DISTRIBUTOR_DIRECTORY = "/generators/distributor/";
    private final Map<NamespaceId, Class<? extends Distributor>> distributorClasses = new HashMap<>();

    public DistributorRegistry(){
        distributorClasses.put(new NamespaceId("orbis:complex"), ComplexDistributor.class);
    }

    @Override
    public Distributor loadType(Container container, String name, Object context) {
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
        return null;
    }

    @Override
    public void registerTypeClass(NamespaceId namespaceId, Class<? extends Distributor> typeClass) {

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
