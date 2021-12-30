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

import com.azortis.orbis.block.Block;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Item implements Keyed {

    private final Key key;
    private final int id;
    private final int maxStackSize;
    private final int maxDamage;
    private final Key blockKey;

    Item(@NotNull Key key, int id, int maxStackSize, int maxDamage, @Nullable Key blockKey) {
        this.key = key;
        this.id = id;
        this.maxStackSize = maxStackSize;
        this.maxDamage = maxDamage;
        this.blockKey = blockKey;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    public int id() {
        return id;
    }

    public int maxStackSize() {
        return maxStackSize;
    }

    public int maxDamage() {
        return maxDamage;
    }

    public boolean isDamageable() {
        return maxDamage > 0;
    }

    public boolean isStackable() {
        return !isDamageable() && maxStackSize > 1;
    }

    public boolean hasBlock() {
        return blockKey != null;
    }

    public @Nullable Key blockKey() {
        return blockKey;
    }

    public @Nullable Block block() {
        return blockKey == null ? null : Block.fromKey(blockKey);
    }

    public static Item fromKey(@NotNull String key) {
        return ItemRegistry.fromItemKey(key);
    }

    public static Item fromKey(@NotNull Key key) {
        return ItemRegistry.fromItemKey(key);
    }

}
