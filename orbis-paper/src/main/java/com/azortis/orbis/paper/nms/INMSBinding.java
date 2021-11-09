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

package com.azortis.orbis.paper.nms;

import com.azortis.orbis.block.property.Property;
import com.azortis.orbis.util.NamespaceId;
import org.bukkit.block.data.BlockData;

import java.util.Map;
import java.util.Set;

public interface INMSBinding {

    Map<Property<?>, ?> getPropertyMap(BlockData blockData);

    Property<?> getProperty(NamespaceId material, String name);

    Map<String, Property<?>> getProperties(NamespaceId material);

    boolean hasProperty(NamespaceId material, Property<?> property);

    <T> T getValue(BlockData blockData, Property<T> property);

    <T> void setValue(BlockData blockData, Property<T> property, T value);

}
