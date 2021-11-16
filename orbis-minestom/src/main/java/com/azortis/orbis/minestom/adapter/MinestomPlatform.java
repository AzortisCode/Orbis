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

package com.azortis.orbis.minestom.adapter;

import com.azortis.orbis.Platform;
import com.azortis.orbis.block.StateDefinition;
import com.azortis.orbis.container.Container;
import com.azortis.orbis.util.NamespaceId;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.Collection;

public class MinestomPlatform implements Platform {

    @Override
    public String getAdaptation() {
        return null;
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public File getDirectory() {
        return null;
    }

    @Override
    public @Nullable Container getContainer(String name) {
        return null;
    }

    @Override
    public Collection<Container> getContainers() {
        return null;
    }

    @Override
    public StateDefinition getStateDefinition(NamespaceId material) {
        return null;
    }

}
