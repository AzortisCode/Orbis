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

package com.azortis.orbis.paper.nms.impl.item;

import com.azortis.orbis.item.Item;
import com.azortis.orbis.item.ItemStack;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PaperItemStack implements ItemStack {
    private final net.minecraft.world.item.ItemStack handle;
    private final Key key;
    private final Item item;

    @SuppressWarnings("PatternValidation")
    public PaperItemStack(@NotNull net.minecraft.world.item.ItemStack handle) {
        this.handle = handle;
        this.key = Key.key(Registry.ITEM.getKey(handle.getItem()).toString());
        this.item = Item.fromKey(key);
    }

    public PaperItemStack(@NotNull Key key) {
        this.handle = new net.minecraft.world.item.ItemStack(Registry.ITEM.get(ResourceLocation.tryParse(key.asString())));
        this.key = key;
        this.item = Item.fromKey(key);
    }

    public PaperItemStack(@NotNull Item item) {
        this.handle = new net.minecraft.world.item.ItemStack(Registry.ITEM.get(ResourceLocation.tryParse(item.key().asString())));
        this.key = item.key();
        this.item = item;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public Item item() {
        return item;
    }

    @Override
    public int getAmount() {
        return handle.getCount();
    }

    @Override
    public void setAmount(int amount) {
        handle.setCount(amount);
    }
}
