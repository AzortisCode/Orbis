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

package com.azortis.orbis.paper.block;

import com.azortis.orbis.block.Block;
import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.block.property.Property;
import com.azortis.orbis.paper.util.ConversionUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.EntityBlock;
import org.apiguardian.api.API;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * Platform representation of a Block, which will directly interact with NMS blocks, Circumventing the PaperAPI.
 *
 * @since 0.3-Alpha
 * @author Jake Nijssen
 */
@API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = "com.azortis.orbis.paper")
public final class PaperBlock implements Block {

    /**
     * The NMS Block handle.
     */
    private final net.minecraft.world.level.block.Block handle;

    /**
     * The Block resource key.
     */
    private final Key key;

    /**
     * A map, that maps all the NMS BlockStates to ours for this block.
     */
    private final ImmutableMap<net.minecraft.world.level.block.state.BlockState, BlockState> stateMap;

    /**
     * Constructs a block instance from a NMS {@link net.minecraft.world.level.block.Block}
     *
     * @param handle The NMS block to represent.
     * @since 0.3-Alpha
     */
    @SuppressWarnings("PatternValidation")
    PaperBlock(@NotNull net.minecraft.world.level.block.Block handle) {
        this.handle = handle;
        this.key = Key.key(Registry.BLOCK.getKey(handle).toString());

        // Populate the legacyBlock states.
        ImmutableMap.Builder<net.minecraft.world.level.block.state.BlockState, BlockState> stateBuilder = ImmutableMap.builder();
        for (net.minecraft.world.level.block.state.BlockState nativeState : handle.getStateDefinition().getPossibleStates()) {
            stateBuilder.put(nativeState, new PaperBlockState(nativeState, this));
        }
        this.stateMap = stateBuilder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Key key() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int id() {
        return Registry.BLOCK.getId(handle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Block block() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImmutableSet<Property<?>> properties() {
        return handle.getStateDefinition().getProperties()
                .stream().map(ConversionUtils::fromNative).collect(ImmutableSet.toImmutableSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasProperty(@NotNull Property<?> property) {
        return handle.getStateDefinition().getProperties().contains(ConversionUtils.toNative(property));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImmutableMap<String, Property<?>> propertyMap() {
        return handle.getStateDefinition().getProperties().stream()
                .collect(ImmutableMap.toImmutableMap(net.minecraft.world.level.block.state.properties.Property::getName,
                        ConversionUtils::fromNative));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull BlockState defaultState() {
        return Objects.requireNonNull(stateMap.get(handle.defaultBlockState()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int stateId() {
        return defaultState().stateId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull BlockState state() {
        return defaultState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImmutableSet<BlockState> states() {
        return ImmutableSet.copyOf(stateMap.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull BlockState with(@NotNull Map<String, String> properties) {
        net.minecraft.world.level.block.state.BlockState state = handle.defaultBlockState();
        for (String propertyName : properties.keySet()) {
            if (handle.getStateDefinition().getProperty(propertyName) != null) {
                net.minecraft.world.level.block.state.properties.Property<?> property = handle.getStateDefinition().getProperty(propertyName);
                assert property != null;
                state = state.setValue(property, castPropertyValue(property, properties.get(propertyName)));
            }
        }
        return Objects.requireNonNull(stateMap.get(state));
    }

    /**
     * Unsafely casts a string value to the undefined type of the given native property.
     *
     * @param property The native property to cast to.
     * @param valueString The string of the value.
     * @return A cast value for the undefined property.
     * @since 0.3-Alpha
     * @param <T> The type of the property.
     */
    @SuppressWarnings("unchecked")
    private <T> T castPropertyValue(@NotNull net.minecraft.world.level.block.state.properties.Property<?> property, String valueString) {
        Preconditions.checkArgument(property.getValue(valueString).isPresent(), "Invalid property value!");
        return (T) property.getValue(valueString).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasEntity() {
        return handle instanceof EntityBlock;
    }

    //
    // Platform utility methods
    //

    /**
     * Gets the NMS legacyBlock handle associated with this legacyBlock.
     *
     * @return The NMS Block handle for this legacyBlock.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    public @NotNull net.minecraft.world.level.block.Block handle(){
        return handle;
    }

    /**
     * Gets the Paper {@link Material} associated with this block.
     *
     * @return The material enum of this block.
     */
    @Contract(pure = true)
    public @NotNull Material material() {
        return CraftMagicNumbers.getMaterial(handle);
    }

    /**
     * Retrieves the associated {@link PaperBlockState} for the specified NMS {@link net.minecraft.world.level.block.state.BlockState}
     *
     * @param nativeState The native state to convert.
     * @return The associated PaperBlockState.
     * @since 0.3-Alpha
     */
    @SuppressWarnings("ConstantConditions")
    public @NotNull BlockState fromNativeState(@NotNull net.minecraft.world.level.block.state.BlockState nativeState) {
        Preconditions.checkNotNull(stateMap.get(nativeState));
        return stateMap.get(nativeState);
    }

    /**
     * Retrieves the associated {@link net.minecraft.world.level.block.state.BlockState} from the specified {@link PaperBlockState}
     *
     * @param blockState The state to convert.
     * @return The associated NMS state.
     * @since 0.3-Alpha
     */
    public @NotNull net.minecraft.world.level.block.state.BlockState toNativeState(@NotNull BlockState blockState) {
        return ((PaperBlockState)blockState).handle();
    }

}
