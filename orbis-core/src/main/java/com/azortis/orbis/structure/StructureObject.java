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

package com.azortis.orbis.structure;

import com.azortis.orbis.block.BlockRegistry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record StructureObject(int version, int dataVersion, int width, int height, int depth,
                              @Unmodifiable @NotNull List<BlockState> palette,
                              @Unmodifiable @NotNull List<Block> blocks) {

    public static int VERSION = 1;

    public record BlockState(@NotNull Key type, @Unmodifiable @NotNull Map<String, String> properties) {
        public @NotNull com.azortis.orbis.block.BlockState blockState() {
            return BlockRegistry.fromKey(type).with(properties);
        }

        public @NotNull CompoundBinaryTag toNBT() {
            CompoundBinaryTag tag = CompoundBinaryTag.empty();
            tag = tag.putString("type", type.asString());

            CompoundBinaryTag propertiesTag = CompoundBinaryTag.empty();
            for (Map.Entry<String, String> property : properties.entrySet()) {
                propertiesTag = propertiesTag.putString(property.getKey(), property.getValue());
            }
            return tag.put("properties", propertiesTag);
        }

        @SuppressWarnings("PatternValidation")
        public static BlockState fromNBT(@NotNull CompoundBinaryTag tag) {
            Key type = Key.key(tag.getString("type"));

            CompoundBinaryTag propertiesTag = tag.getCompound("properties");
            Map<String, String> properties = new HashMap<>();
            for (String key : propertiesTag.keySet()) {
                properties.put(key, propertiesTag.getString(key));
            }
            return new BlockState(type, Map.copyOf(properties));
        }

    }

    public record Block(int state, int[] pos) {

        public @NotNull com.azortis.orbis.block.BlockState blockState(@NotNull List<BlockState> palette) {
            return palette.get(state).blockState();
        }

        public @NotNull CompoundBinaryTag toNBT() {
            CompoundBinaryTag tag = CompoundBinaryTag.empty();
            tag = tag.putInt("state", state).putIntArray("pos", pos);
            return tag;
        }

        public static @NotNull Block fromNBT(@NotNull CompoundBinaryTag tag) {
            return new Block(tag.getInt("state"), tag.getIntArray("pos"));
        }

        public int x() {
            return pos[0];
        }

        public int y() {
            return pos[1];
        }

        public int z() {
            return pos[2];
        }
    }

}
