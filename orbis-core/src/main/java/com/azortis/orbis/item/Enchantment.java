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

package com.azortis.orbis.item;

import com.google.common.collect.ImmutableSet;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Enchantment {

    private final Key key;
    private final int id;
    private final int maxLevel;
    private final int minLevel;

    private ImmutableSet<Enchantment> incompatibleEnchantments;

    public Enchantment(@NotNull Key key, int id, int maxLevel, int minLevel) {
        this.key = key;
        this.id = id;
        this.maxLevel = maxLevel;
        this.minLevel = minLevel;
    }

    public Key key() {
        return key;
    }

    public int id() {
        return id;
    }

    public int maxLevel() {
        return maxLevel;
    }

    public int minLevel() {
        return minLevel;
    }

    void setIncompatibleEnchantments(ImmutableSet<Enchantment> incompatibleEnchantments) {
        if (this.incompatibleEnchantments == null) {
            this.incompatibleEnchantments = incompatibleEnchantments;
        } else {
            throw new IllegalStateException("Incompatible enchantments already populated in " + this);
        }
    }

    public ImmutableSet<Enchantment> incompatibleEnchantments() {
        return incompatibleEnchantments;
    }

    public boolean isCompatibleWith(@NotNull Enchantment enchantment) {
        return !incompatibleEnchantments.contains(enchantment);
    }

    public boolean isCompatibleWith(@NotNull List<Enchantment> enchantments) {
        boolean compatible = true;
        for (Enchantment enchantment : enchantments) {
            if (!isCompatibleWith(enchantment)) compatible = false;
        }
        return compatible;
    }

}
