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

import com.azortis.orbis.block.property.Property;
import com.azortis.orbis.paper.OrbisPlugin;
import com.azortis.orbis.utils.NamespaceId;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class PaperBlockState extends PaperStateDefinition implements OldBlockState {
    private BlockData handle;

    public PaperBlockState(NamespaceId material, BlockData handle) {
        super(material);
        this.handle = handle;
    }

    @Override
    public Map<Property<?>, Optional<?>> getValues() {
        return OrbisPlugin.getNMS().getValues(this.handle);
    }

    @Override
    public @Nullable <T> T getValue(Property<T> property) {
        return OrbisPlugin.getNMS().getValue(this.handle, property);
    }

    @Override
    public <T> void setValue(Property<T> property, T value) {
        this.handle = OrbisPlugin.getNMS().setValue(this.handle, property, value);
    }

    @Override
    public OldBlockState clone() {
        try {
            return (OldBlockState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }

    public BlockData getHandle() {
        return handle;
    }

}
