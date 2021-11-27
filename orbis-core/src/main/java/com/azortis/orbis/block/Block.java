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

package com.azortis.orbis.block;

import com.azortis.orbis.block.property.Property;
import com.azortis.orbis.utils.NamespaceId;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Block {

    @NotNull NamespaceId getKey();

    int getId();

    int getStateId();

    boolean isAir();

    boolean isSolid();

    boolean isLiquid();

    @NotNull ImmutableSet<Property<?>> getProperties();

    boolean hasProperty(Property<?> property);

    @NotNull ImmutableMap<Property<?>, Comparable<?>> getValues();

    @NotNull <T extends Comparable<T>> T getValue(Property<T> property);

    @NotNull <T extends Comparable<T>> Optional<T> getValueOptional(Property<T> property);

    @NotNull <T extends Comparable<T>, V extends T> Block setValue(Property<T> property, V value);

    @NotNull Block getDefault();

}
