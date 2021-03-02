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

package com.azortis.orbis.instance;

import com.azortis.orbis.OrbisMine;
import com.azortis.orbis.OrbisSettings;
import com.azortis.orbis.generator.Dimension;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstanceManager {

    private final Map<String, OrbisInstance> loadedInstances = new HashMap<>();

    public InstanceManager(OrbisSettings settings) {
        settings.getDefaultInstances().forEach(this::createInstance);
    }

    public OrbisInstance getInstance(@NotNull String name) {
        return loadedInstances.get(name);
    }

    public List<OrbisInstance> getInstances() {
        return new ArrayList<>(loadedInstances.values());
    }

    @SuppressWarnings("UnusedReturnValue")
    public OrbisInstance createInstance(@NotNull InstanceSettings settings) {
        final Dimension dimension = OrbisMine.getDimensionRegistry().loadDimension(settings.getDimensionName());
        final OrbisInstance instance = new OrbisInstance(settings.getName(), dimension, settings.getStorageLocation());
        loadedInstances.put(settings.getName(), instance);
        return instance;
    }

}
