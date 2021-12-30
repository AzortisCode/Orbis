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

package com.azortis.orbis.item.meta;

import com.azortis.orbis.item.Enchantment;
import com.azortis.orbis.item.ItemFlag;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ItemMeta {

    boolean hasDisplayName();

    @Nullable Component getDisplayName();

    void setDisplayName(@Nullable Component displayName);

    boolean hasLore();

    @Nullable List<Component> getLore();

    void setLore(@Nullable List<Component> lore);

    boolean hasCustomModelData();

    int getCustomModelData();

    void setCustomModelData(int customModelData);

    boolean hasEnchants();

    boolean hasEnchant(@NotNull Enchantment enchantment);

    int getEnchantLevel(@NotNull Enchantment enchantment);

    @NotNull Map<Enchantment, Integer> getEnchants();

    boolean addEnchant(@NotNull Enchantment enchantment, int level, boolean ignoreLevelRestriction);

    boolean removeEnchant(@NotNull Enchantment enchantment);

    default boolean hasConflictingEnchant(@NotNull Enchantment enchantment) {
        Set<Enchantment> enchantments = getEnchants().keySet();
        boolean isConflicting = false;
        for (Enchantment enchantment1 : enchantments) {
            if (enchantment.isCompatibleWith(enchantment)) {
                isConflicting = true;
                break;
            }
        }
        return isConflicting;
    }

    void addItemFlags(@NotNull ItemFlag... itemFlags);

    void removeItemFlags(@NotNull ItemFlag... itemFlags);

    @NotNull Set<ItemFlag> getItemFlags();

    boolean hasItemFlag(@NotNull ItemFlag itemFlag);

    boolean isUnbreakable();

    void setUnbreakable(boolean unbreakable);


}
