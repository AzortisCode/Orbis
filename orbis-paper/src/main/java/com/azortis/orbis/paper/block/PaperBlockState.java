/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2023 Azortis
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

package com.azortis.orbis.paper.block;

import com.azortis.orbis.block.Block;
import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.block.property.Property;
import com.azortis.orbis.paper.util.ConversionUtils;
import com.azortis.orbis.util.Nameable;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.kyori.adventure.key.Key;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.apiguardian.api.API;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Platform representation of a BlockState, that wraps around the internal NMS
 * {@link net.minecraft.world.level.block.state.BlockState}. See {@link PaperBlock}
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.paper")
public final class PaperBlockState implements BlockState {

    /**
     * The NMS handle for this BlockState
     */
    private final net.minecraft.world.level.block.state.BlockState handle;

    /**
     * The parent Block of this state.
     */
    private final PaperBlock block;

    /**
     * Constructs a PaperBlockState from a NMS state, and the parent NMS block wrapper.
     *
     * @param handle The NMS handle for this state.
     * @param block  The parent block of this state.
     * @since 0.3-Alpha
     */
    PaperBlockState(@NotNull net.minecraft.world.level.block.state.BlockState handle, @NotNull PaperBlock block) {
        this.handle = handle;
        this.block = block;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Key key() {
        return block.key();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int id() {
        return block.id();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Block block() {
        return block;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int stateId() {
        return net.minecraft.world.level.block.Block.getId(handle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull BlockState state() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAir() {
        return handle.isAir();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFlammable() {
        return handle.getMaterial().isFlammable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLiquid() {
        return handle.getMaterial().isLiquid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReplaceable() {
        return handle.getMaterial().isReplaceable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSolid() {
        return handle.getMaterial().isSolid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isColliding() {
        return block.handle().hasCollision;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImmutableMap<Property<?>, Comparable<?>> values() {
        return handle.getValues().entrySet().stream().map(this::getPropertyValueEntry)
                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<Property<?>, Comparable<?>> getPropertyValueEntry(
            @NotNull Map.Entry<net.minecraft.world.level.block.state.properties.Property<?>, @NotNull Comparable<?>> nativeEntry) {
        return Map.entry(ConversionUtils.fromNative(nativeEntry.getKey()), getValue(nativeEntry.getKey(), nativeEntry.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Comparable<T>> @NotNull T get(@NotNull Property<T> property) {
        net.minecraft.world.level.block.state.properties.Property<?> nativeProperty = ConversionUtils.toNative(property);
        return (T) getValue(nativeProperty, handle.getValue(nativeProperty));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({"unchecked", "OptionalGetWithoutIsPresent"})
    @Override
    public @NotNull <T extends Comparable<T>> Optional<T> getOptional(@NotNull Property<T> property) {
        net.minecraft.world.level.block.state.properties.Property<?> nativeProperty = ConversionUtils.toNative(property);
        return Optional.of((T) getValue(nativeProperty, handle.getOptionalValue(nativeProperty).get()));
    }

    private Comparable<?> getValue(@NotNull net.minecraft.world.level.block.state.properties.Property<?> nativeProperty,
                                   @NotNull Comparable<?> value) {
        if (nativeProperty instanceof net.minecraft.world.level.block.state.properties.EnumProperty<?>) {
            String enumValue = ((StringRepresentable) value).getSerializedName();
            return ConversionUtils.fromNative(nativeProperty).getValue(enumValue);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull <T extends Comparable<T>, V extends T> BlockState set(@NotNull Property<T> property, @NotNull V value) {
        net.minecraft.world.level.block.state.properties.Property<?> nativeProperty = ConversionUtils.toNative(property);
        Object finalValue = value;
        if (nativeProperty instanceof EnumProperty<?> enumProperty) {
            Optional<?> optionalValue = enumProperty.getValue(getEnumName(enumProperty, ((Nameable) value).serializedName()));
            Preconditions.checkArgument(optionalValue.isPresent(), "Invalid property value!");
            finalValue = optionalValue.get();
        }
        return Objects.requireNonNull(block.fromNativeState(set(nativeProperty, (Comparable<?>) finalValue)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull BlockState set(@NotNull Property<?> property, @NotNull String value) {
        net.minecraft.world.level.block.state.properties.Property<?> nativeProperty = ConversionUtils.toNative(property);
        Optional<?> optionalValue;
        if (nativeProperty instanceof EnumProperty<?> enumProperty) {
            optionalValue = enumProperty.getValue(getEnumName(enumProperty, value));
        } else {
            optionalValue = nativeProperty.getValue(value);
        }
        Preconditions.checkArgument(optionalValue.isPresent(), "Invalid property value");
        return Objects.requireNonNull(block.fromNativeState(set(nativeProperty, (Comparable<?>) optionalValue.get())));
    }

    /**
     * Gets the right string value for a {@link EnumProperty} from a lowercase value.
     * This is to make sure it will check out, even with NMS property enums whose serialized names are uppercase.
     *
     * @param nativeProperty The enum property to check against.
     * @param lowerCaseName  The lowercase serialized name.
     * @return The right lower case or uppercase string value of the property.
     */
    private String getEnumName(@NotNull EnumProperty<?> nativeProperty,
                               @NotNull String lowerCaseName) {
        String match = "";
        for (Enum<?> nativeEnum : nativeProperty.getPossibleValues()) {
            if (((StringRepresentable) nativeEnum).getSerializedName().equalsIgnoreCase(lowerCaseName)) {
                match = ((StringRepresentable) nativeEnum).getSerializedName();
            }
        }
        return match;
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>, V extends T> net.minecraft.world.level.block.state.BlockState set(
            @NotNull net.minecraft.world.level.block.state.properties.Property<?> nativeProperty,
            @NotNull Comparable<?> value) {
        return handle.setValue((net.minecraft.world.level.block.state.properties.Property<T>) nativeProperty,
                (V) value);
    }

    //
    // Platform utility methods
    //

    /**
     * Gets the handle associated with this state.
     *
     * @return The NMS BlockState handle for this state.
     */
    @Contract(pure = true)
    public @NotNull net.minecraft.world.level.block.state.BlockState handle() {
        return handle;
    }

    /**
     * Creates a {@link BlockData} instance to use in the PaperAPI.
     *
     * @return A fresh bloated {@link BlockData} instance.
     */
    @Contract(" -> new")
    public @NotNull BlockData createBlockData() {
        return CraftBlockData.createData(handle);
    }

}
