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

package com.azortis.orbis.item.meta;

import com.azortis.orbis.inventory.ItemFlag;
import com.azortis.orbis.item.LegacyEnchantment;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Deprecated
public interface ItemMeta {

    boolean hasDisplayName();

    @Nullable
    Component getDisplayName();

    void setDisplayName(@Nullable Component displayName);

    @NotNull
    List<Component> getLore();

    void setLore(@NotNull List<Component> lore);

    boolean hasCustomModelData();

    int getCustomModelData();

    void setCustomModelData(int customModelData);

    boolean hasEnchants();

    boolean hasEnchant(@NotNull LegacyEnchantment legacyEnchantment);

    int getEnchantLevel(@NotNull LegacyEnchantment legacyEnchantment);

    @NotNull
    Map<LegacyEnchantment, Integer> getEnchants();

    boolean addEnchant(@NotNull LegacyEnchantment legacyEnchantment, int level, boolean ignoreLevelRestriction);

    boolean removeEnchant(@NotNull LegacyEnchantment legacyEnchantment);

    default boolean hasConflictingEnchant(@NotNull LegacyEnchantment legacyEnchantment) {
        Set<LegacyEnchantment> legacyEnchantments = getEnchants().keySet();
        boolean isConflicting = false;
        for (LegacyEnchantment legacyEnchantment1 : legacyEnchantments) {
            if (legacyEnchantment.isCompatibleWith(legacyEnchantment)) {
                isConflicting = true;
                break;
            }
        }
        return isConflicting;
    }

    void addItemFlags(@NotNull ItemFlag... itemFlags);

    void removeItemFlags(@NotNull ItemFlag... itemFlags);

    @NotNull
    Set<ItemFlag> getItemFlags();

    boolean hasItemFlag(@NotNull ItemFlag itemFlag);

    int getDamage();

    void setDamage(int damage);

    int getRepairCost();

    void setRepairCost(int repairCost);

    boolean isUnbreakable();

    void setUnbreakable(boolean unbreakable);

    //TODO Add Attributes & Placeable/Destroyable key support

}
