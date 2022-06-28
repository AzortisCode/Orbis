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
import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class BlockState implements ConfiguredBlock {

    private final Block block;
    private final int stateId;
    private final boolean isAir;
    private final boolean isSolid;
    private final boolean isLiquid;
    private final ImmutableMap<Property<?>, Comparable<?>> values;

    // Populated by BlockRegistry
    private Table<Property<?>, Comparable<?>, BlockState> neighbours;

    BlockState(Block block, int stateId, boolean isAir, boolean isSolid, boolean isLiquid, @NotNull ImmutableMap<Property<?>, Comparable<?>> values) {
        this.block = block;
        this.stateId = stateId;
        this.isAir = isAir;
        this.isSolid = isSolid;
        this.isLiquid = isLiquid;
        this.values = values;
    }

    public static BlockState fromStateId(int stateId) {
        return BlockRegistry.fromStateId(stateId);
    }

    @Override
    public Block block() {
        return block;
    }

    @Override
    public int stateId() {
        return stateId;
    }

    public boolean isAir() {
        return isAir;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public boolean isLiquid() {
        return isLiquid;
    }

    public @NotNull ImmutableMap<Property<?>, Comparable<?>> getValues() {
        return values;
    }

    public <T extends Comparable<T>> @NotNull T getValue(Property<T> property) {
        Comparable<?> value = values.get(property);
        if (value == null) {
            throw new IllegalArgumentException("Cannot get property " + property + " because it doesn't exists in " + this);
        } else {
            return property.getType().cast(value);
        }
    }

    public @NotNull <T extends Comparable<T>> Optional<T> getValueOptional(Property<T> property) {
        Comparable<?> value = values.get(property);
        return value == null ? Optional.empty() : Optional.of(property.getType().cast(value));
    }

    public @NotNull <T extends Comparable<T>, V extends T> BlockState setValue(Property<T> property, V value) {
        BlockState state = this.neighbours.get(property, value);
        if (state == null) {
            throw new IllegalArgumentException("Cannot get " + property + " in " + this + " as it doesn't exist");
        }
        return state;
    }

    public @NotNull BlockState setValue(Property<?> property, String value) {
        BlockState state = this.neighbours.get(property, property.getValueFor(value));
        if (state == null) {
            throw new IllegalArgumentException("Cannot get " + property + " in " + this + " as it doesn't exist");
        }
        return state;
    }

    @SuppressWarnings("UnstableApiUsage")
    void populateNeighbours(Map<Map<Property<?>, Comparable<?>>, BlockState> states) {
        if (neighbours == null) {
            Table<Property<?>, Comparable<?>, BlockState> table = HashBasedTable.create();

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

    @Override
    public @NotNull Key key() {
        return block.key();
    }

    @Override
    public int id() {
        return block.id();
    }

    @Override
    public BlockState blockState() {
        return this;
    }

}
