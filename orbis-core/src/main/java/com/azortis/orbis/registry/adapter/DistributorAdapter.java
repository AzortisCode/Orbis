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

package com.azortis.orbis.registry.adapter;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.registry.GeneratorRegistry;
import com.azortis.orbis.util.NamespaceId;
import com.google.gson.*;

import java.lang.reflect.Type;

public class DistributorAdapter implements JsonDeserializer<Distributor> {

    private final GeneratorRegistry<Distributor> distributorRegistry;

    public DistributorAdapter() {
        this.distributorRegistry = Orbis.getGeneratorRegistry(Distributor.class);
    }

    @Override
    public Distributor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject object = jsonElement.getAsJsonObject();
        final String distributorNamespaceId = object.getAsJsonObject("distributor").getAsString();
        final Class<? extends Distributor> distributorClass = distributorRegistry.getTypeClass(new NamespaceId(distributorNamespaceId));
        return context.deserialize(jsonElement, distributorClass);
    }
}
