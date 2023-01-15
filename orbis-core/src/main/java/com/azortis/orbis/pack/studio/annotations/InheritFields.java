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
 * <p>Used to mark a {@link Class} whose fields should be seen as a property
 * in the {@link Class} that is extending the annotated {@link Class}.</p>
 *
 * <p><b>Note</b> do not use on abstract classes in combination with {@link Typed} this will
 * otherwise lead to duplicate properties in the schema.</p>
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@API(status = API.Status.STABLE, since = "0.3-Alpha")
public @interface InheritFields {
}
