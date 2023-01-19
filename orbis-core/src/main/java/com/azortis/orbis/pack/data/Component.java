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

package com.azortis.orbis.pack.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate classes that provide their own {@link ComponentAccess}.
 * If the injector comes across a component annotated with this, it will construct the
 * {@link ComponentAccess} instance and add it to the corresponding {@link DataAccess}.
 * Only works on classes that are implementations of specified types defined in {@link DataAccess#GENERATOR_TYPES}.
 * <p>
 * It will also be used when looking for types to create a json schema for.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {

    /**
     * The type of the {@link ComponentAccess} implementation.
     *
     * @return The type of the {@link ComponentAccess} implementation.
     */
    Class<? extends ComponentAccess> value();
}
