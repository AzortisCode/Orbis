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
 * A mandatory annotation on all {@link java.lang.reflect.Field}'s and {@link Class}es that are part
 * of the generator. The value inside of this annotation will be shown to the user when hovering over that element.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public @interface Description {

    /**
     * The description text to show when hovering.
     *
     * @return A description about the type/field.
     */
    String value();
}
