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

import com.azortis.orbis.Orbis;
import com.azortis.orbis.WorldAccess;
import com.azortis.orbis.util.Location;
import com.google.gson.*;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

public class LocationAdapter implements JsonDeserializer<Location>, JsonSerializer<Location> {

    @Override
    public Location deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject locationObject = json.getAsJsonObject();
        int x, y, z;
        float yaw, pitch;

        x = locationObject.get("x").getAsInt();
        y = locationObject.get("y").getAsInt();
        z = locationObject.get("z").getAsInt();
        yaw = locationObject.get("yaw").getAsFloat();
        pitch = locationObject.get("pitch").getAsFloat();

        String worldName = locationObject.get("world").getAsString();
        WorldAccess world = Orbis.getPlatform().getWorldAccess(worldName);
        WeakReference<WorldAccess> worldRef;

        if (world != null) {
            worldRef = new WeakReference<>(world);
        } else {
            worldRef = new WeakReference<>(null);
        }
        return new Location(x, y, z, yaw, pitch, worldRef);
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        final JsonObject locationObject = new JsonObject();
        locationObject.addProperty("x", location.x());
        locationObject.addProperty("y", location.y());
        locationObject.addProperty("z", location.z());
        locationObject.addProperty("yaw", location.yaw());
        locationObject.addProperty("pitch", location.pitch());

        if (location.isWorldLoaded()) {
            locationObject.addProperty("world", location.getWorld().name());
        } else {
            locationObject.addProperty("world", "world"); // Refer to default world I guess?
        }

        return locationObject;
    }
}
