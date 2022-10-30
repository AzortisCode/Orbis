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

package com.azortis.orbis.paper.util;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.block.Block;
import com.azortis.orbis.block.BlockRegistry;
import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.block.property.*;
import com.azortis.orbis.entity.Player;
import com.azortis.orbis.item.LegacyItem;
import com.azortis.orbis.paper.OrbisPlugin;
import com.azortis.orbis.paper.PaperPlatform;
import com.azortis.orbis.paper.block.PaperBlock;
import com.azortis.orbis.paper.block.PaperBlockState;
import com.azortis.orbis.paper.studio.PaperStudioWorld;
import com.azortis.orbis.paper.world.PaperWorld;
import com.azortis.orbis.paper.world.PaperWorldAccess;
import com.azortis.orbis.util.Direction;
import com.azortis.orbis.util.Location;
import com.azortis.orbis.world.WorldAccess;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.apiguardian.api.API;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Locale;

/**
 * An utilities class for converting Core types to NMS/PaperAPI types.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.INTERNAL, since = "0.3-Alpha")
public final class ConversionUtils {

    private static final PaperPlatform PLATFORM;
    private static final ImmutableBiMap<Property<?>, net.minecraft.world.level.block.state.properties.Property<?>> PROPERTY_BI_MAP;

    //
    // Property conversions
    //

    static {
        // Lazily load the platform instance.
        PLATFORM = OrbisPlugin.getPlatform();
    }

    static {
        // Cursed way of finding each NMS property and map them to ours,
        // no other solution is possible due to the Spigot mappings.
        ImmutableBiMap.Builder<Property<?>, net.minecraft.world.level.block.state.properties.Property<?>> propertyBuilder = ImmutableBiMap.builder();

        // Get all the properties registered in NMS
        for (final Field field : BlockStateProperties.class.getDeclaredFields()) {
            try {
                Object propertyObj = field.get(BlockStateProperties.class);
                if (propertyObj instanceof net.minecraft.world.level.block.state.properties.BooleanProperty nativeBooleanProperty) {
                    // If the NMS property is a boolean variant, it is just a matter of matching the right names.
                    BooleanProperty booleanProperty = null;
                    for (Property<?> property : PropertyRegistry.PROPERTIES.values()) {
                        if (property instanceof BooleanProperty boolProp
                                && property.key().equalsIgnoreCase(nativeBooleanProperty.getName())) {
                            booleanProperty = boolProp;
                            break;
                        }
                    }
                    if (booleanProperty == null) {
                        Orbis.getLogger().error("Couldn't find a boolean property match for {}", nativeBooleanProperty.getName());
                    } else {
                        propertyBuilder.put(booleanProperty, nativeBooleanProperty);
                    }
                } else if (propertyObj instanceof net.minecraft.world.level.block.state.properties.IntegerProperty nativeIntegerProperty) {
                    // If the NMS property is an integer property, check if the names are equal + the min and max line up.
                    IntegerProperty integerProperty = null;
                    for (Property<?> property : PropertyRegistry.PROPERTIES.values()) {
                        if (property instanceof IntegerProperty intProp
                                && property.key().equalsIgnoreCase(nativeIntegerProperty.getName())
                                && intProp.min() == nativeIntegerProperty.min
                                && intProp.max() == nativeIntegerProperty.max) {
                            integerProperty = intProp;
                            break;
                        }
                    }
                    if (integerProperty == null) {
                        Orbis.getLogger().error("Couldn't find an integer property match for {}, with min={} and max={}",
                                nativeIntegerProperty.getName(), nativeIntegerProperty.min, nativeIntegerProperty.max);
                    } else {
                        propertyBuilder.put(integerProperty, nativeIntegerProperty);
                    }
                } else if (propertyObj instanceof DirectionProperty nativeDirectionProperty) {
                    // NMS decided that it is a good idea to have an EnumProperty variant called DirectionProperty
                    // we catch this as a precaution, and do the same operations as EnumProperty without the generics.
                    EnumProperty<Direction> directionProperty = null;
                    for (Property<?> property : PropertyRegistry.PROPERTIES.values()) {
                        if (property instanceof EnumProperty<?> directProp && directProp.type() == Direction.class
                                && directProp.key().equalsIgnoreCase(nativeDirectionProperty.getName())) {
                            Collection<net.minecraft.core.Direction> nativeDirections = nativeDirectionProperty.getPossibleValues();
                            if (nativeDirections.size() == directProp.values().size()) {
                                boolean match = true;
                                for (net.minecraft.core.Direction direction : nativeDirections) {
                                    if (!directProp.names().contains(direction.getSerializedName().toLowerCase(Locale.ENGLISH))) {
                                        match = false;
                                    }
                                }
                                if (match) {
                                    //noinspection unchecked
                                    directionProperty = (EnumProperty<Direction>) directProp;
                                }
                            }
                        }
                    }
                    if (directionProperty == null) {
                        Orbis.getLogger().error("Couldn't find a direction property match for {}", nativeDirectionProperty);
                    } else {
                        propertyBuilder.put(directionProperty, nativeDirectionProperty);
                    }
                } else if (propertyObj instanceof net.minecraft.world.level.block.state.properties.EnumProperty<?> nativeEnumProperty) {
                    // If the property is an Enum property, we have to match the amount of values plus make sure their
                    // serialized names are similar.
                    EnumProperty<?> enumProperty = null;
                    for (Property<?> property : PropertyRegistry.PROPERTIES.values()) {
                        if (property instanceof EnumProperty<?> enumProp
                                && property.key().equalsIgnoreCase(nativeEnumProperty.getName())) {
                            @SuppressWarnings("unchecked")
                            Collection<Enum<?>> nativeValues = (Collection<Enum<?>>) nativeEnumProperty.getPossibleValues();
                            if (nativeValues.size() == enumProp.names().size()) {
                                boolean match = true;
                                for (Enum<?> nativeEnum : nativeValues) {
                                    // Some vanilla property enums somehow use Uppercase, so we have to account for that.
                                    if (!enumProp.names().contains(((StringRepresentable) nativeEnum).getSerializedName().toLowerCase(Locale.ENGLISH))) {
                                        match = false;
                                        break;
                                    }
                                }
                                if (match) {
                                    enumProperty = enumProp;
                                }
                            }
                        }
                    }
                    if (enumProperty == null) {
                        Orbis.getLogger().error("Couldn't find an enum property match for {}", nativeEnumProperty);
                    } else {
                        propertyBuilder.put(enumProperty, nativeEnumProperty);
                    }
                }
            } catch (IllegalAccessException ex) {
                Orbis.getLogger().error("A fatal error has occurred when mapping Block properties.");
                ex.printStackTrace();
            }
        }
        PROPERTY_BI_MAP = propertyBuilder.build();
    }

    /**
     * Converts a NMS {@link net.minecraft.world.level.block.state.properties.Property} to the Orbis equivalent property.
     *
     * @param nativeProperty The NMS property to convert.
     * @return The corresponding Orbis {@link Property}
     * @throws IllegalArgumentException If the native property is not a valid one.
     */
    @SuppressWarnings("ConstantConditions")
    @Contract(pure = true)
    public static @NotNull Property<?> fromNative(
            @NotNull net.minecraft.world.level.block.state.properties.Property<?> nativeProperty) throws IllegalArgumentException {
        Preconditions.checkArgument(PROPERTY_BI_MAP.inverse().containsKey(nativeProperty), "Invalid native block state property!");
        return PROPERTY_BI_MAP.inverse().get(nativeProperty);
    }

    /**
     * Converts an Orbis {@link Property} to the NMS equivalent.
     *
     * @param property The orbis property to convert.
     * @return The corresponding NMS {@link net.minecraft.world.level.block.state.properties.Property}
     * @throws IllegalArgumentException If the property is not a valid one.
     */
    @SuppressWarnings("ConstantConditions")
    @Contract(pure = true)
    public static @NotNull net.minecraft.world.level.block.state.properties.Property<?> toNative(Property<?> property) throws IllegalArgumentException {
        Preconditions.checkArgument(PROPERTY_BI_MAP.containsKey(property), "Invalid block state property!");
        return PROPERTY_BI_MAP.get(property);
    }

    //
    // Block conversions
    //

    /**
     * Gets the {@link Block} for a Paper {@link Material} enum.
     *
     * @param material The block material enum.
     * @return The associated block.
     * @throws IllegalArgumentException If the material is not a block.
     */
    @Contract(pure = true)
    public static @NotNull Block fromPaper(@NotNull Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(material.isBlock(), "Material is not a block!");
        return BlockRegistry.fromKey(material.getKey());
    }

    /**
     * Gets the Paper {@link Material} enum for specified {@link Block}.
     *
     * @param block The block.
     * @return The associated material enum.
     */
    @Contract(pure = true)
    public static @NotNull Material toPaper(@NotNull Block block) {
        return ((PaperBlock) block).material();
    }

    /**
     * Gets the {@link Block} from a NMS {@link net.minecraft.world.level.block.Block} handle.
     *
     * @param nativeBlock The native NMS block.
     * @return The associated block.
     */
    @Contract(pure = true)
    public static @NotNull Block fromNative(@NotNull net.minecraft.world.level.block.Block nativeBlock) {
        return BlockRegistry.fromKey(Registry.BLOCK.getKey(nativeBlock).toString());
    }

    /**
     * Gets the NMS {@link net.minecraft.world.level.block.Block} handle of specified {@link Block}.
     *
     * @param block The block.
     * @return The NMS handle of given block.
     */
    @Contract(pure = true)
    public static @NotNull net.minecraft.world.level.block.Block toNative(@NotNull Block block) {
        return ((PaperBlock) block).handle();
    }


    @Contract(pure = true)
    public static @NotNull BlockState fromPaper(@NotNull BlockData data) {
        return BlockRegistry.fromStateId(net.minecraft.world.level.block.Block.getId(((CraftBlockData) data).getState()));
    }

    @Contract("_ -> new")
    public static @NotNull BlockData toPaper(@NotNull BlockState state) {
        return ((PaperBlockState) state).createBlockData();
    }

    @Contract(pure = true)
    public static @NotNull BlockState fromNative(@NotNull net.minecraft.world.level.block.state.BlockState nativeState) {
        return BlockRegistry.fromStateId(net.minecraft.world.level.block.Block.getId(nativeState));
    }

    @Contract(pure = true)
    public static @NotNull net.minecraft.world.level.block.state.BlockState toNative(@NotNull BlockState state) {
        return ((PaperBlockState) state).handle();
    }

    //
    // Deprecated
    //

    @Deprecated
    public static Material getMaterial(@NotNull LegacyItem legacyItem) {
        return CraftMagicNumbers.getMaterial(Registry.ITEM.get(ResourceLocation.tryParse(legacyItem.key().asString())));
    }

    @Deprecated
    public static LegacyItem getItem(@NotNull Material material) {
        return LegacyItem.fromKey(Registry.ITEM.getKey(CraftMagicNumbers.getItem(material)).toString());
    }

    //
    // TBD
    //

    @Contract("_ -> new")
    public static @NotNull Location fromPaper(@NotNull org.bukkit.Location nativeLoc) {
        if (!nativeLoc.isWorldLoaded()) throw new IllegalStateException("World unloaded");
        return new Location(nativeLoc.getX(), nativeLoc.getY(), nativeLoc.getZ(),
                nativeLoc.getYaw(), nativeLoc.getPitch(),
                new WeakReference<>(PLATFORM.getWorldAccess(nativeLoc.getWorld())));
    }

    @Contract("_ -> new")
    public static @NotNull org.bukkit.Location toPaper(@NotNull Location location) {
        return new org.bukkit.Location(toPaper(location.getWorld()), location.x(), location.y(), location.z(), location.yaw(), location.pitch());
    }

    @Contract(pure = true)
    public static @NotNull org.bukkit.World toPaper(@NotNull WorldAccess worldAccess) throws IllegalArgumentException {
        org.bukkit.World nativeWorld;
        if (worldAccess instanceof PaperWorld world) {
            nativeWorld = world.nativeWorld();
        } else if (worldAccess instanceof PaperStudioWorld studioWorld) {
            nativeWorld = studioWorld.nativeWorld();
        } else if (worldAccess instanceof PaperWorldAccess paperWorldAccess) {
            nativeWorld = paperWorldAccess.handle();
        } else throw new IllegalArgumentException(worldAccess + " isn't a valid implementation for the paper module!");
        return nativeWorld;
    }

    @Contract(pure = true)
    public static @NotNull Player.GameMode fromPaper(@NotNull GameMode nativeMode) {
        return switch (nativeMode) {
            case CREATIVE -> Player.GameMode.CREATIVE;
            case SURVIVAL -> Player.GameMode.SURVIVAL;
            case ADVENTURE -> Player.GameMode.ADVENTURE;
            case SPECTATOR -> Player.GameMode.SPECTATOR;
        };
    }

    @Contract(pure = true)
    public static @NotNull GameMode toPaper(@NotNull Player.GameMode gameMode) {
        return switch (gameMode) {
            case CREATIVE -> GameMode.CREATIVE;
            case SURVIVAL -> GameMode.SURVIVAL;
            case ADVENTURE -> GameMode.ADVENTURE;
            case SPECTATOR -> GameMode.SPECTATOR;
        };
    }

}
