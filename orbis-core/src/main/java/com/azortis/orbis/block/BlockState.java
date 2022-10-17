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

package com.azortis.orbis.block;

import com.azortis.orbis.block.property.Property;
import com.google.common.collect.ImmutableMap;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * <p>A representation of a {@link Block} in a state based on the values of the block
 * its properties. This will only affect visuals, for more advanced data see {@link com.azortis.orbis.block.entity.BlockEntity}</p>
 *
 * @since 0.3-Alpha
 * @see <a href="https://minecraft.fandom.com/wiki/Block_states#:~:text=Block%20states%20(also%20known%20as,Metadata)%20to%20define%20a%20block.">BlockStates Wiki</a>
 * for more information..
 * @author Jake Nijssen
 */
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public non-sealed interface BlockState extends ConfiguredBlock {

    /**
     * Check if this state is air.
     *
     * @return If the state is air.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean isAir();

    /**
     * Check if this state is flammable.
     *
     * @return If the state is flammable.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean isFlammable();

    /**
     * Check if this state is liquid.
     *
     * @return If the state is liquid.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean isLiquid();

    /**
     * Check if this state is replaceable.
     *
     * @return If the state is replaceable.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean isReplaceable();

    /**
     * Check if the state is solid.
     *
     * @return If the state is solid.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean isSolid();

    /**
     * Check if the state is colliding.
     *
     * @return If the state is colliding.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    boolean isColliding();

    /**
     * Get the values of this state for the {@link Block} its {@link Property}'s
     *
     * @return An immutable view of the properties and its values for this state.
     * @since 0.3-Alpha
     */
    @Contract(" -> new")
    @NotNull ImmutableMap<Property<?>, Comparable<?>> values();

    /**
     * Get the value of this {@link BlockState} of the given property.
     * <p><b>NOTE</b> the specified property must be existent on this {@link BlockState#block()} otherwise
     * this method will throw an {@link IllegalArgumentException}</p>
     *
     * @param property The property to get the value for.
     * @param <T>      The type of the property.
     * @return The value of the given property for this state.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    <T extends Comparable<T>> @NotNull T get(@NotNull Property<T> property);

    /**
     * Get the value of this {@link BlockState} of the given property wrapped in a {@link Optional},
     * which will be empty if the Block doesn't have this property.
     *
     * @param property The property to get the value for.
     * @param <T>      The type of the property.
     * @return The value of the given property for this state.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    <T extends Comparable<T>> @NotNull Optional<T> getOptional(@NotNull Property<T> property);

    /**
     * Get the {@link BlockState} if specified {@link Property} is changed to the specified value.
     * <p><b>NOTE</b> the specified property must be existent on this {@link BlockState#block()} & the property must contain
     * specified value otherwise this method will throw an {@link IllegalArgumentException}</p>
     *
     * @param property The property to change.
     * @param value    The value of the property.
     * @param <T>      The type of the property.
     * @param <V>      The type of the value.
     * @return The BlockState for the legacyBlock after you set this value.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull <T extends Comparable<T>, V extends T> BlockState set(@NotNull Property<T> property, @NotNull V value);

    /**
     * Get the {@link BlockState} if specified {@link Property} is changed to the specified value.
     * <p><b>NOTE</b> the specified property must be existent on this {@link BlockState#block()} & the property must contain
     * specified value otherwise this method will throw an {@link IllegalArgumentException}</p>
     *
     * @param property The property to change.
     * @param value    A string representation of the property value.
     * @return The BlockState for the legacyBlock after you set this value.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    @NotNull BlockState set(@NotNull Property<?> property, @NotNull String value);

}
