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

package com.azortis.orbis.paper.block.entity;

import com.azortis.orbis.block.entity.MobSpawnerEntity;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;

public class PaperMobSpawnerEntity extends PaperBlockEntity implements MobSpawnerEntity {
    private final BaseSpawner spawner;

    public PaperMobSpawnerEntity(BlockEntity handle) {
        super(handle);
        this.spawner = ((SpawnerBlockEntity) handle).getSpawner();
    }

    @Override
    public int getDelay() {
        return spawner.spawnDelay;
    }

    @Override
    public void setDelay(int delay) {
        spawner.spawnDelay = delay;
    }


}
