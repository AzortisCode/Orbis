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
import com.google.common.collect.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class BlockImpl implements Block {
    private final NamespaceId key;
    private final int id;
    private final int stateId;
    private final boolean isAir;
    private final boolean isSolid;
    private final boolean isLiquid;
    private final ImmutableMap<Property<?>, Comparable<?>> values;
    private final Block defaultBlock;
    private Table<Property<?>, Comparable<?>, Block> neighbours;

    BlockImpl(NamespaceId key, int id, int stateId, boolean isAir, boolean isSolid, boolean isLiquid,
              @NotNull ImmutableMap<Property<?>, Comparable<?>> values,
              @Nullable Block defaultBlock) {
        this.key = key;
        this.id = id;
        this.stateId = stateId;
        this.isAir = isAir;
        this.isSolid = isSolid;
        this.isLiquid = isLiquid;
        this.values = values;
        this.defaultBlock = defaultBlock;
    }

    @Override
    public @NotNull NamespaceId getKey() {
        return key;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getStateId() {
        return stateId;
    }

    @Override
    public boolean isAir() {
        return isAir;
    }

    @Override
    public boolean isSolid() {
        return isSolid;
    }

    @Override
    public boolean isLiquid() {
        return isLiquid;
    }

    @Override
    public @NotNull ImmutableSet<Property<?>> getProperties() {
        return values.keySet();
    }

    @Override
    public boolean hasProperty(Property<?> property) {
        return values.containsKey(property);
    }

    @Override
    public @NotNull ImmutableMap<Property<?>, Comparable<?>> getValues() {
        return values;
    }

    @Override
    public <T extends Comparable<T>> @NotNull T getValue(Property<T> property) {
        Comparable<?> value = values.get(property);
        if (value == null) {
            throw new IllegalArgumentException("Cannot get property " + property + " because it doesn't exists in " + this);
        } else {
            return property.getType().cast(value);
        }
    }

    @Override
    public @NotNull <T extends Comparable<T>> Optional<T> getValueOptional(Property<T> property) {
        Comparable<?> value = values.get(property);
        return value == null ? Optional.empty() : Optional.of(property.getType().cast(value));
    }

    @Override
    public @NotNull <T extends Comparable<T>, V extends T> Block setValue(Property<T> property, V value) {
        Block block = this.neighbours.get(property, value);
        if (block == null) {
            throw new IllegalArgumentException("Cannot get " + property + " in " + this + " as it doesn't exist");
        }
        return block;
    }

    @Override
    public @NotNull Block getDefault() {
        return this.defaultBlock == null ? this : this.defaultBlock;
    }

    @SuppressWarnings("UnstableApiUsage")
    void populateNeighbours(Map<Map<Property<?>, Comparable<?>>, BlockImpl> states) {
        if (neighbours == null) {
            Table<Property<?>, Comparable<?>, Block> table = HashBasedTable.create();

            for (Map.Entry<Property<?>, Comparable<?>> entry : this.values.entrySet()) {
                Property<?> property = entry.getKey();

                for (Comparable<?> value : property.getValues()) {
                    if (value != entry.getValue()) {
                        table.put(property, value, states.get(createStateKey(property, value)));
                    }
                }
            }
            this.neighbours = table.isEmpty() ? table : ArrayTable.create(table);
        } else {
            throw new IllegalStateException("Neighbours already populated in " + this);
        }
    }

    private Map<Property<?>, Comparable<?>> createStateKey(Property<?> property, Comparable<?> value) {
        Map<Property<?>, Comparable<?>> stateKey = new HashMap<>(values);
        stateKey.replace(property, value);
        return ImmutableMap.copyOf(stateKey);
    }

}
