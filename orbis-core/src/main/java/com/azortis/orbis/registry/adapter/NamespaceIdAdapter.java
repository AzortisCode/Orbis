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

import com.azortis.orbis.util.NamespaceID;
import com.google.gson.*;

import java.lang.reflect.Type;

public class NamespaceIdAdapter implements JsonSerializer<NamespaceID>, JsonDeserializer<NamespaceID> {

    @Override
    public JsonElement serialize(NamespaceID namespaceID, Type type, JsonSerializationContext context) {
        return context.serialize(namespaceID.getNamespaceId(), String.class);
    }

    @Override
    public NamespaceID deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        String namespacedId = element.getAsJsonObject().getAsJsonObject("namespaceId").getAsString();
        return new NamespaceID(namespacedId);
    }
}
