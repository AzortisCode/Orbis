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
import com.azortis.orbis.item.meta.ItemMeta;
import com.azortis.orbis.paper.item.meta.PaperCrossbowMeta;
import com.azortis.orbis.paper.nms.ConversionUtils;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;

public class PaperItemStack implements ItemStack {
    private final org.bukkit.inventory.ItemStack handle;
    private final Item item;

    public PaperItemStack(@NotNull org.bukkit.inventory.ItemStack handle) {
        this.handle = handle;
        this.item = ConversionUtils.getItem(handle.getType());
    }

    public PaperItemStack(@NotNull Item item) {
        this.handle = new org.bukkit.inventory.ItemStack(ConversionUtils.getMaterial(item));
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

    @Override
    public boolean hasItemMeta() {
        return handle.hasItemMeta();
    }

    @Override
    public @NotNull ItemMeta getItemMeta() {
        org.bukkit.inventory.meta.ItemMeta paperMeta = handle.getItemMeta();
        if (paperMeta instanceof CrossbowMeta) { //TODO add more meta's once implemented
            return new PaperCrossbowMeta(paperMeta);
        }
        return new PaperItemMeta(paperMeta);
    }

    @Override
    public void setItemMeta(@Nullable ItemMeta meta) {
        if (meta == null) handle.setItemMeta(null);
        assert meta instanceof PaperItemMeta : "ItemMeta isn't an instance of PaperItemMeta!";
        handle.setItemMeta(((PaperItemMeta) meta).getHandle());
    }

    @Override
    @SuppressWarnings("all")
    public @NotNull ItemStack clone() {
        return new PaperItemStack(handle.clone());
    }

    @Override
    public @NotNull NBTCompound serialize() {
        MutableNBTCompound compound = new MutableNBTCompound();
        compound.set("id", NBT.String(item.key().asString()));
        compound.set("Count", NBT.Int(getAmount()));
        if (hasItemMeta()) {
            compound.set("tag", ((PaperItemMeta) getItemMeta()).serialize().toCompound());
        }
        return compound.toCompound();
    }

    public org.bukkit.inventory.ItemStack handle() {
        return handle;
    }

    public static PaperItemStack fromPaper(org.bukkit.inventory.ItemStack itemStack) {
        return new PaperItemStack(itemStack);
    }

}
