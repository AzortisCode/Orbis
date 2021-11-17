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

package com.azortis.orbis.paper.block;

import com.azortis.orbis.block.BlockState;
import com.azortis.orbis.block.StateDefinition;
import com.azortis.orbis.block.property.Property;
import com.azortis.orbis.paper.OrbisPlugin;
import com.azortis.orbis.utils.NamespaceId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class PaperStateDefinition implements StateDefinition {
    private final NamespaceId material;

    public PaperStateDefinition(NamespaceId material) {
        this.material = material;
    }

    @Override
    public @NotNull NamespaceId getMaterial() {
        return this.material;
    }

    @Override
    public @Nullable Property<?> getProperty(String name) {
        return OrbisPlugin.getNMS().getProperty(this.material, name);
    }

    @Override
    public Map<String, Property<?>> getProperties() {
        return OrbisPlugin.getNMS().getProperties(this.material);
    }

    @Override
    public boolean hasProperty(Property<?> property) {
        return OrbisPlugin.getNMS().hasProperty(this.material, property);
    }

    @Override
    public BlockState getDefaultBlockState() {
        return new PaperBlockState(this.material, OrbisPlugin.getNMS().createBlockState(this.material));
    }

    @Override
    public BlockState getBlockState(Map<Property<?>, Optional<?>> values) {
        return null;
    }
}
