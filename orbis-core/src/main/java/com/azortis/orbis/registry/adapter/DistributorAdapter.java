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

package com.azortis.orbis.registry.adapter;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.registry.GeneratorRegistry;
import com.azortis.orbis.utils.NamespaceId;
import com.google.gson.*;

import java.lang.reflect.Type;

public class DistributorAdapter implements JsonDeserializer<Distributor> {

    private final GeneratorRegistry<Distributor> distributorRegistry;

    public DistributorAdapter() {
        this.distributorRegistry = Orbis.getGeneratorRegistry(Distributor.class);
    }

    @Override
    public Distributor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        final JsonPrimitive object = jsonElement.getAsJsonObject().getAsJsonPrimitive("providerId");
        final NamespaceId distributorProviderId = context.deserialize(object, NamespaceId.class);
        final Class<? extends Distributor> distributorClass = distributorRegistry.getTypeClass(distributorProviderId);
        return context.deserialize(jsonElement, distributorClass);
    }
}
