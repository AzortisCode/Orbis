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

package com.azortis.orbis.paper.item;

import com.azortis.orbis.item.Enchantment;
import com.azortis.orbis.item.ItemFlag;
import com.azortis.orbis.item.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTString;
import org.jglrxavpok.hephaistos.nbt.NBTType;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;

import java.util.*;
import java.util.stream.Collectors;

public class PaperItemMeta implements ItemMeta {
    protected final org.bukkit.inventory.meta.ItemMeta handle;

    public PaperItemMeta(org.bukkit.inventory.meta.ItemMeta handle) {
        this.handle = handle;
    }

    @Override
    public boolean hasDisplayName() {
        return handle.hasDisplayName();
    }

    @Override
    public @Nullable Component getDisplayName() {
        return handle.displayName();
    }

    @Override
    public void setDisplayName(@Nullable Component displayName) {
        handle.displayName(displayName);
    }

    @Override
    public @NotNull List<Component> getLore() {
        return handle.hasLore() ? Objects.requireNonNull(handle.lore()) : Collections.emptyList();
    }

    @Override
    public void setLore(@Nullable List<Component> lore) {
        handle.lore(lore);
    }

    @Override
    public boolean hasCustomModelData() {
        return handle.hasCustomModelData();
    }

    @Override
    public int getCustomModelData() {
        return handle.getCustomModelData();
    }

    @Override
    public void setCustomModelData(int customModelData) {
        handle.setCustomModelData(customModelData);
    }

    @Override
    public boolean hasEnchants() {
        return handle.hasEnchants();
    }

    @Override
    public boolean hasEnchant(@NotNull Enchantment enchantment) {
        return handle.hasEnchant(toPaper(enchantment));
    }

    @Override
    public int getEnchantLevel(@NotNull Enchantment enchantment) {
        return handle.getEnchantLevel(toPaper(enchantment));
    }

    @Override
    public @NotNull Map<Enchantment, Integer> getEnchants() {
        Map<Enchantment, Integer> enchantMap = new HashMap<>();
        for (Map.Entry<org.bukkit.enchantments.Enchantment, Integer> entry : handle.getEnchants().entrySet()) {
            enchantMap.put(Enchantment.fromKey(entry.getKey().getKey()), entry.getValue());
        }
        return enchantMap;
    }

    @Override
    public boolean addEnchant(@NotNull Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        return handle.addEnchant(toPaper(enchantment), level, ignoreLevelRestriction);
    }

    @Override
    public boolean removeEnchant(@NotNull Enchantment enchantment) {
        return handle.removeEnchant(toPaper(enchantment));
    }

    @Override
    public void addItemFlags(@NotNull ItemFlag... itemFlags) {
        org.bukkit.inventory.ItemFlag[] paperItemFlags = (org.bukkit.inventory.ItemFlag[])
                Arrays.stream(itemFlags).map(PaperItemMeta::toPaper).toArray();
        handle.addItemFlags(paperItemFlags);
    }

    @Override
    public void removeItemFlags(@NotNull ItemFlag... itemFlags) {
        org.bukkit.inventory.ItemFlag[] paperItemFlags = (org.bukkit.inventory.ItemFlag[])
                Arrays.stream(itemFlags).map(PaperItemMeta::toPaper).toArray();
        handle.removeItemFlags(paperItemFlags);
    }

    @Override
    public @NotNull Set<ItemFlag> getItemFlags() {
        return handle.getItemFlags().stream().map(PaperItemMeta::fromPaper).collect(Collectors.toSet());
    }

    @Override
    public boolean hasItemFlag(@NotNull ItemFlag itemFlag) {
        return handle.hasItemFlag(toPaper(itemFlag));
    }

    @Override
    public boolean hasDamage() {
        return ((Damageable) handle).hasDamage();
    }

    @Override
    public int getDamage() {
        return ((Damageable) handle).getDamage();
    }

    @Override
    public void setDamage(int damage) {
        ((Damageable) handle).setDamage(damage);
    }

    @Override
    public int getRepairCost() {
        return ((Repairable) handle).getRepairCost();
    }

    @Override
    public void setRepairCost(int repairCost) {
        ((Repairable) handle).setRepairCost(repairCost);
    }

    @Override
    public boolean isUnbreakable() {
        return handle.isUnbreakable();
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        handle.setUnbreakable(unbreakable);
    }

    protected MutableNBTCompound serialize() {
        MutableNBTCompound compound = new MutableNBTCompound();
        MutableNBTCompound displayCompound = new MutableNBTCompound();
        if (hasDisplayName()) {
            assert getDisplayName() != null : "hasDisplayName returns true, but the value is null!";
            displayCompound.set("Name", NBT.String(GsonComponentSerializer.gson().serialize(getDisplayName())));
        }
        if (!getLore().isEmpty()) {
            List<NBTString> lore = new ArrayList<>();
            for (Component loreLine : getLore()) {
                lore.add(NBT.String(GsonComponentSerializer.gson().serialize(loreLine)));
            }
            displayCompound.set("Lore", NBT.List(NBTType.TAG_String, lore));
        }
        if (!displayCompound.isEmpty()) compound.set("display", displayCompound.toCompound());
        if (!getItemFlags().isEmpty()) {
            byte hideFlags = 0;
            for (ItemFlag itemFlag : getItemFlags()) {
                hideFlags |= getBitModifier(itemFlag);
            }
            compound.set("HideFlags", NBT.Byte(hideFlags)); // Officially value is stored as an int, but max value is 127
        }
        if (hasDamage()) {
            compound.set("Damage", NBT.Int(getDamage()));
        }
        compound.set("Unbreakable", NBT.Boolean(isUnbreakable()));
        if (hasCustomModelData()) {
            compound.set("CustomModelData", NBT.Int(getCustomModelData()));
        }
        return compound;
    }

    private byte getBitModifier(ItemFlag itemFlag) {
        return (byte) (1 << itemFlag.ordinal());
    }

    public @NotNull org.bukkit.inventory.meta.ItemMeta getHandle() {
        return handle;
    }

    private static @NotNull org.bukkit.enchantments.Enchantment toPaper(Enchantment enchantment) {
        org.bukkit.enchantments.Enchantment paperEnchant = org.bukkit.enchantments.Enchantment
                .getByKey(NamespacedKey.fromString(enchantment.key().asString()));
        assert paperEnchant != null;
        return paperEnchant;
    }

    private static org.bukkit.inventory.ItemFlag toPaper(ItemFlag itemFlag) {
        return switch (itemFlag) {
            case HIDE_ENCHANTS -> org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;
            case HIDE_ATTRIBUTES -> org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES;
            case HIDE_UNBREAKABLE -> org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE;
            case HIDE_CAN_DESTROY -> org.bukkit.inventory.ItemFlag.HIDE_DESTROYS;
            case HIDE_CAN_PLACE -> org.bukkit.inventory.ItemFlag.HIDE_PLACED_ON;
            case HIDE_MISCELLANEOUS -> org.bukkit.inventory.ItemFlag.HIDE_POTION_EFFECTS;
            case HIDE_DYE -> org.bukkit.inventory.ItemFlag.HIDE_DYE;
        };
    }

    private static ItemFlag fromPaper(org.bukkit.inventory.ItemFlag itemFlag) {
        return switch (itemFlag) {
            case HIDE_ENCHANTS -> ItemFlag.HIDE_ENCHANTS;
            case HIDE_ATTRIBUTES -> ItemFlag.HIDE_ATTRIBUTES;
            case HIDE_UNBREAKABLE -> ItemFlag.HIDE_UNBREAKABLE;
            case HIDE_DESTROYS -> ItemFlag.HIDE_CAN_DESTROY;
            case HIDE_PLACED_ON -> ItemFlag.HIDE_CAN_PLACE;
            case HIDE_POTION_EFFECTS -> ItemFlag.HIDE_MISCELLANEOUS;
            case HIDE_DYE -> ItemFlag.HIDE_DYE;
        };
    }

}
