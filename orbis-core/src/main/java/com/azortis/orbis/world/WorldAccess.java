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

package com.azortis.orbis.world;

import com.azortis.orbis.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Set;

/**
 * Interface to interact with the platforms world.
 */
public interface WorldAccess {

    @NotNull
    String name();

    boolean isWorldLoaded();

    int minHeight();

    int maxHeight();

    @NotNull
    @UnmodifiableView Set<Player> getPlayers();

    boolean isChunkLoaded(int chunkX, int chunkZ);

    @NotNull ChunkAccess getChunk(int chunkX, int chunkZ);


}
