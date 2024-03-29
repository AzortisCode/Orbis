/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2022 Azortis
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

package com.azortis.orbis.pack.adapter;

import com.azortis.orbis.Registry;
import com.google.gson.*;
import net.kyori.adventure.key.Key;

import java.lang.reflect.Type;

public final class TypeAdapter<T> implements JsonDeserializer<T> {

    private final Class<T> clazz;

    public TypeAdapter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonPrimitive object = json.getAsJsonObject().getAsJsonPrimitive("type");
        final Key key = context.deserialize(object, Key.class);
        final Class<? extends T> type = Registry.getRegistry(clazz).getType(key);
        return context.deserialize(json, type);
    }

}
