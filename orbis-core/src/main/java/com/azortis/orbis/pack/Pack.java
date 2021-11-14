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

package com.azortis.orbis.pack;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class Pack {

    // Required
    private String name;
    private String author;
    @SerializedName("dimension")
    private String dimensionFile;
    private String packVersion;

    // Optional
    private List<PackContributor> contributors;
    private String description;

    // Used for gson deserialization
    private Pack() {
    }

    public Pack(String name, String author, String dimensionFile, String packVersion) {
        this.name = name;
        this.author = author;
        this.dimensionFile = dimensionFile;
        this.packVersion = packVersion;
    }
}
