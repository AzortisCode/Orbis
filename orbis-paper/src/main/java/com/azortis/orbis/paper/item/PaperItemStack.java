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

import com.azortis.orbis.item.Item;
import com.azortis.orbis.item.ItemStack;
import com.azortis.orbis.paper.OrbisPlugin;
import org.jetbrains.annotations.NotNull;

public class PaperItemStack implements ItemStack {
    private final org.bukkit.inventory.ItemStack handle;
    private final Item item;

    public PaperItemStack(@NotNull org.bukkit.inventory.ItemStack handle, Item item) {
        this.handle = handle;
        this.item = item;
    }

    public PaperItemStack(@NotNull org.bukkit.inventory.ItemStack handle) {
        this.handle = handle;
        this.item = OrbisPlugin.getNMS().getItem(handle.getType());
    }

    public PaperItemStack(@NotNull Item item) {
        this.handle = new org.bukkit.inventory.ItemStack(OrbisPlugin.getNMS().getMaterial(item));
        this.item = item;
    }

    @Override
    public Item item() {
        return item;
    }

    @Override
    public int getAmount() {
        return handle.getAmount();
    }

    @Override
    public void setAmount(int amount) {
        handle.setAmount(amount);
    }

    public org.bukkit.inventory.ItemStack getHandle() {
        return handle;
    }
}
