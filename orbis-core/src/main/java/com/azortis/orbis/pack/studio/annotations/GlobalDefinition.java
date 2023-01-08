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

package com.azortis.orbis.pack.studio.annotations;

import org.apiguardian.api.API;

import java.lang.annotation.*;

/**
 * Mark classes of which its schema definitions are reused.
 * This will tell the schema generator to create a global schema definition file for this {@link Class} type.
 * <p>
 * Classes annotated with this cannot access component {@link Entries} for any of its fields, as it is unknown
 * at schema generation if a global definition is used in a {@link com.azortis.orbis.pack.data.Component} environment or not.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public @interface GlobalDefinition {
    /**
     * The name of the definitions file, i.e. "Vec3i" will produce "Vec3i.json"
     * The name should be unique and cannot clash with other definitions, so use truly unique names.
     *
     * @return The name of the definitions file.
     * @since 0.3-Alpha
     */
    String value();
}
