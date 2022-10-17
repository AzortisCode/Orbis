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

package com.azortis.orbis.pack;

import com.azortis.orbis.pack.studio.annotations.CollectionType;
import com.azortis.orbis.pack.studio.annotations.Description;
import com.azortis.orbis.pack.studio.annotations.Required;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class Pack {

    @Required
    @Description("The name of the pack")
    private final String name;

    @Required
    @Description("The name of the main author")
    private final String author;

    @Required
    @Description("The name of the dimension config file without the *.json")
    @SerializedName("dimension")
    private final String dimensionFile;

    @Required
    @Description("The version string version of the pack")
    private final String packVersion;

    @CollectionType(Contributor.class)
    @Description("Additional pack contributors")
    private final List<Contributor> contributors = new ArrayList<>();

    @Description("The description of the pack")
    private final String description;

    public Pack(@NotNull String name, @NotNull String author,
                @NotNull String dimensionFile, @NotNull String packVersion, @Nullable String description) {
        this.name = name;
        this.author = author;
        this.dimensionFile = dimensionFile;
        this.packVersion = packVersion;
        this.description = description;
    }

    public String name() {
        return name;
    }

    public String author() {
        return author;
    }

    public String dimensionFile() {
        return dimensionFile;
    }

    public String packVersion() {
        return packVersion;
    }

    public List<Contributor> contributors() {
        return contributors;
    }

    public String description() {
        return description;
    }

    public record Contributor(@Required @Description("The name of the contributor") @NotNull String name,
                              @Description("Description of the contribution made by user") @Nullable String contribution) {
    }

}
