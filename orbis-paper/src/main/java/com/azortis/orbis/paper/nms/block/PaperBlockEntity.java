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

package com.azortis.orbis.paper.nms.block;

import com.azortis.orbis.block.entity.BlockEntity;
import com.azortis.orbis.util.BlockPos;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;

public class PaperBlockEntity implements BlockEntity {
    private final net.minecraft.world.level.block.entity.BlockEntity handle;
    private final Key key;
    private final BlockPos blockPos;

    @SuppressWarnings("PatternValidation")
    public PaperBlockEntity(net.minecraft.world.level.block.entity.BlockEntity handle) {
        this.handle = handle;
        this.key = Key.key(handle.getMinecraftKeyString());
        this.blockPos = new BlockPos(handle.getBlockPos().getX(), handle.getBlockPos().getY(), handle.getBlockPos().getZ());
    }

    @Override
    public final @NotNull Key key() {
        return key;
    }

    @Override
    public final int x() {
        return blockPos.getX();
    }

    @Override
    public final int y() {
        return blockPos.getY();
    }

    @Override
    public final int z() {
        return blockPos.getZ();
    }

    @Override
    public final @NotNull BlockPos blockPos() {
        return blockPos;
    }

    @Override
    public final @NotNull NBTCompound save(boolean includeMeta) {
        MutableNBTCompound compound = new MutableNBTCompound();
        saveAdditional(compound);
        if (includeMeta) saveMetadata(compound);
        return compound.toCompound();
    }

    protected void saveAdditional(MutableNBTCompound compound) {
        compound.set("id", NBT.String(key.asString()));
    }

    private void saveMetadata(MutableNBTCompound compound) {
        compound.set("x", NBT.Int(x()));
        compound.set("y", NBT.Int(y()));
        compound.set("z", NBT.Int(z()));
    }

    public net.minecraft.world.level.block.entity.BlockEntity getHandle() {
        return handle;
    }
}
