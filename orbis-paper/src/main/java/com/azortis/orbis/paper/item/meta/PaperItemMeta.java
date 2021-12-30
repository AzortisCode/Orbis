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

package com.azortis.orbis.paper.item.meta;

import com.azortis.orbis.item.Enchantment;
import com.azortis.orbis.item.ItemFlag;
import com.azortis.orbis.item.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class PaperItemMeta implements ItemMeta {
    private final org.bukkit.inventory.meta.ItemMeta handle;

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
    public boolean hasLore() {
        return handle.hasLore();
    }

    @Override
    public @Nullable List<Component> getLore() {
        return handle.lore();
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
    public boolean isUnbreakable() {
        return handle.isUnbreakable();
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        handle.setUnbreakable(unbreakable);
    }

    public org.bukkit.inventory.meta.ItemMeta getHandle() {
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
            case HIDE_ATTRIBUTES -> org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES;
            case HIDE_DESTROYS -> org.bukkit.inventory.ItemFlag.HIDE_DESTROYS;
            case HIDE_DYE -> org.bukkit.inventory.ItemFlag.HIDE_DYE;
            case HIDE_ENCHANTS -> org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;
            case HIDE_PLACED_ON -> org.bukkit.inventory.ItemFlag.HIDE_PLACED_ON;
            case HIDE_POTION_EFFECTS -> org.bukkit.inventory.ItemFlag.HIDE_POTION_EFFECTS;
            case HIDE_UNBREAKABLE -> org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE;
        };
    }

    private static ItemFlag fromPaper(org.bukkit.inventory.ItemFlag itemFlag) {
        return switch (itemFlag) {
            case HIDE_ENCHANTS -> ItemFlag.HIDE_ENCHANTS;
            case HIDE_ATTRIBUTES -> ItemFlag.HIDE_ATTRIBUTES;
            case HIDE_UNBREAKABLE -> ItemFlag.HIDE_UNBREAKABLE;
            case HIDE_DESTROYS -> ItemFlag.HIDE_DESTROYS;
            case HIDE_PLACED_ON -> ItemFlag.HIDE_PLACED_ON;
            case HIDE_POTION_EFFECTS -> ItemFlag.HIDE_POTION_EFFECTS;
            case HIDE_DYE -> ItemFlag.HIDE_DYE;
        };
    }

}
