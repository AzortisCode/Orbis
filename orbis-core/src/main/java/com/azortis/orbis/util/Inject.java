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

package com.azortis.orbis.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate classes where the injector should look for fields to inject,
 * and fields to inject instances in the {@link com.azortis.orbis.generator.Dimension} object tree
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Inject {
    /**
     * If the {@link Class} of the {@link java.lang.reflect.Field} is the parent instance of this object.
     * Only works on fields!
     *
     * @return If this field is a reference to the parent object
     */
    boolean isChild() default false;

    /**
     * The fieldName of a {@link String} typed {@link java.lang.reflect.Field} whose value will be used to
     * find the correct configuration file in the {@link com.azortis.orbis.pack.Pack} being loaded to construct
     * an instance of the annotated field. Only works on fields with a type
     * supported by {@link com.azortis.orbis.Registry}!
     *
     * @return The fieldName of a {@link String} typed {@link java.lang.reflect.Field}
     */
    String fieldName() default "";
}
