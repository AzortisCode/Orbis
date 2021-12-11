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

package com.azortis.orbis.paper.nms.impl.block;

import com.azortis.orbis.block.entity.BlockEntity;
import com.azortis.orbis.utils.BlockPos;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull Key key() {
        return key;
    }

    @Override
    public int getX() {
        return blockPos.getX();
    }

    @Override
    public int getY() {
        return blockPos.getY();
    }

    @Override
    public int getZ() {
        return blockPos.getZ();
    }

    @Override
    public BlockPos getBlockPos() {
        return blockPos;
    }

    public net.minecraft.world.level.block.entity.BlockEntity getHandle() {
        return handle;
    }
}
