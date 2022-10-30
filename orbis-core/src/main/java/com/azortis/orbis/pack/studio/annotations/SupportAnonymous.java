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
 * Used by {@link com.azortis.orbis.pack.data.DataAccess#GENERATOR_TYPES} that can be embedded anonymously
 * in other config files, this will override the {@link Required} property on the declared field {@link SupportAnonymous#value()}
 * meaning that that field won't be required in embedded environments, but still will be in environments where it is a
 * standalone file.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public @interface SupportAnonymous {

    /**
     * The name of the field to overrule the {@link Required} when in an embedded environment,
     * defaults to "name".
     *
     * @return The field name to overrule.
     * @since 0.3-Alpha
     */
    String value() default "name";
}
