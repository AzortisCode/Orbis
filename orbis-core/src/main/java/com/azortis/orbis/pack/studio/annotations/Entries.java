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

package com.azortis.orbis.pack.studio.annotations;

import org.apiguardian.api.API;

import java.lang.annotation.*;

/**
 * Forces the {@link String} value of a field or inside a {@link java.util.Collection} to be a reference
 * to a datafile of the specified type.
 * The names for these files are by {@link com.azortis.orbis.pack.data.DataAccess#getDataEntries(Class)},
 * if the specified type is a component type, then it will only produce entries inside a component instance.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public @interface Entries {

    /**
     * The type to validate the data file names against.
     *
     * @return The entries datatype.
     */
    Class<?> value();
}
