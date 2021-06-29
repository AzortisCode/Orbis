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

package com.azortis.orbis.paper.block.data;

import com.azortis.orbis.block.data.FaceAttachable;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

public class PaperFaceAttachable extends PaperBlockData implements FaceAttachable {

    public PaperFaceAttachable(BlockData handle) {
        super(handle);
    }

    @Override
    public @NotNull AttachedFace getAttachedFace() {
        return fromPaper(((org.bukkit.block.data.FaceAttachable) getHandle()).getAttachedFace());
    }

    @Override
    public void setAttachedFace(@NotNull AttachedFace attachedFace) {
        ((org.bukkit.block.data.FaceAttachable) getHandle()).setAttachedFace(toPaper(attachedFace));
    }

    public static org.bukkit.block.data.FaceAttachable.AttachedFace toPaper(AttachedFace attachedFace){
        return switch (attachedFace){
            case FLOOR -> org.bukkit.block.data.FaceAttachable.AttachedFace.FLOOR;
            case WALL -> org.bukkit.block.data.FaceAttachable.AttachedFace.WALL;
            case CEILING -> org.bukkit.block.data.FaceAttachable.AttachedFace.CEILING;
        };
    }

    public static AttachedFace fromPaper(org.bukkit.block.data.FaceAttachable.AttachedFace attachedFace){
        return switch (attachedFace){
            case FLOOR -> AttachedFace.FLOOR;
            case WALL -> AttachedFace.WALL;
            case CEILING -> AttachedFace.CEILING;
        };
    }

}
