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

package com.azortis.orbis.minestom.adapter;

import com.azortis.orbis.Adapter;
import com.azortis.orbis.block.data.BlockData;
import com.azortis.orbis.container.Container;
import com.azortis.orbis.util.NamespaceId;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.Collection;

public class MinestomAdapter implements Adapter {

    @Override
    public String getAdaptation() {
        return null;
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public File getDirectory() {
        return null;
    }

    @Override
    public @Nullable Container getContainer(String name) {
        return null;
    }

    @Override
    public Collection<Container> getContainers() {
        return null;
    }

    @Override
    public Object getBlockTypeAdapter() {
        return null;
    }

    @Override
    public BlockData createBlockData(NamespaceId material) {
        return null;
    }
}
