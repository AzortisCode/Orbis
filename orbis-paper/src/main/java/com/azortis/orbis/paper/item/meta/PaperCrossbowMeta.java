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

import com.azortis.orbis.item.ItemStack;
import com.azortis.orbis.item.meta.CrossbowMeta;
import com.azortis.orbis.paper.item.PaperItemMeta;
import com.azortis.orbis.paper.item.PaperItemStack;
import com.google.common.collect.ImmutableList;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTType;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PaperCrossbowMeta extends PaperItemMeta implements CrossbowMeta {
    private final org.bukkit.inventory.meta.CrossbowMeta crossbowHandle;

    public PaperCrossbowMeta(ItemMeta handle) {
        super(handle);
        this.crossbowHandle = (org.bukkit.inventory.meta.CrossbowMeta) handle;
    }

    @Override
    public void addChargedProjectile(@NotNull ItemStack projectile) {
        crossbowHandle.addChargedProjectile(((PaperItemStack) projectile).getHandle());
    }

    @Override
    public @NotNull List<ItemStack> getChargedProjectiles() {
        ImmutableList.Builder<ItemStack> chargedProjectiles = ImmutableList.builder();
        crossbowHandle.getChargedProjectiles().forEach(itemStack ->
                chargedProjectiles.add(PaperItemStack.fromPaper(itemStack)));
        return chargedProjectiles.build();
    }

    @Override
    public boolean hasChargedProjectiles() {
        return crossbowHandle.hasChargedProjectiles();
    }

    @Override
    public void setChargedProjectiles(@Nullable List<ItemStack> chargedProjectiles) {
        if (chargedProjectiles == null || chargedProjectiles.isEmpty()) {
            crossbowHandle.setChargedProjectiles(null);
        } else {
            List<org.bukkit.inventory.ItemStack> paperChargedProjectiles = chargedProjectiles.stream()
                    .map(itemStack -> (PaperItemStack) itemStack)
                    .map(PaperItemStack::getHandle).collect(Collectors.toList());
            crossbowHandle.setChargedProjectiles(paperChargedProjectiles);
        }
    }

    @Override
    protected MutableNBTCompound serialize() {
        MutableNBTCompound compound = super.serialize();
        List<NBTCompound> chargedProjectiles = new ArrayList<>();
        for (ItemStack chargedProjectile : getChargedProjectiles()) {
            chargedProjectiles.add(((PaperItemStack) chargedProjectile).serialize());
        }
        compound.set("ChargedProjectiles", NBT.List(NBTType.TAG_Compound, chargedProjectiles));
        compound.set("Charged", NBT.Boolean(hasChargedProjectiles()));
        return compound;
    }
}
