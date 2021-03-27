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

package com.azortis.orbis.generator.biome;

import com.azortis.orbis.container.Container;
import com.azortis.orbis.util.NamespaceId;

import java.io.File;

public abstract class Distributor {

    private String name;
    private NamespaceId providerId;
    private transient Container container;
    private transient File settingsFolder;

    // Used for GSON deserialization
    protected Distributor() {
    }

    public String getName() {
        return name;
    }

    public NamespaceId getProviderId() {
        return providerId;
    }

    public Distributor(String name, NamespaceId providerId) {
        this.name = name;
        this.providerId = providerId;
    }

    //
    // Container
    //

    public void setContainer(Container container) {
        if(this.container == null)this.container = container;
    }

    public Container getContainer() {
        return container;
    }


    //
    // SettingsFolder
    //

    public void setSettingsFolder(File settingsFolder) {
        if (this.settingsFolder == null) this.settingsFolder = settingsFolder;
    }

    public File getSettingsFolder() {
        return settingsFolder;
    }

    //
    // Abstracted methods
    //

    public abstract void load();

    public abstract Biome getBiomeAt(double x, double z);

    public abstract Biome getBiome(int biomeHash);

}
