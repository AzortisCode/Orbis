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

package com.azortis.orbis.pack.adapter;

import com.azortis.orbis.Registry;
import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.google.gson.*;
import net.kyori.adventure.key.Key;

import java.lang.reflect.Type;

public class NoiseGeneratorAdapter implements JsonDeserializer<NoiseGenerator> {

    @Override
    public NoiseGenerator deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        final JsonPrimitive object = json.getAsJsonObject().getAsJsonPrimitive("type");
        final Key noiseKey = context.deserialize(object, Key.class);
        final Class<? extends NoiseGenerator> noiseType = Registry.NOISE_GENERATOR.getType(noiseKey);
        return context.deserialize(json, noiseType);
    }
}
