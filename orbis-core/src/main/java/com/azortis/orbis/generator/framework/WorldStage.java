/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2023 Azortis
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

package com.azortis.orbis.generator.framework;

import com.azortis.orbis.pack.studio.annotations.Required;
import com.azortis.orbis.pack.studio.annotations.Typed;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

@Typed
public abstract class WorldStage {

    @Required
    private final Key type;

    public WorldStage(@NotNull Key type) {
        this.type = type;
    }

    public abstract void apply(@NotNull ChunkSnapshot context, @NotNull WorldSnapshot snapshot);

    public @NotNull Key type() {
        return type;
    }
}
