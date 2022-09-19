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

package com.azortis.orbis.util;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Used to annotate classes where the injector should look for fields to inject,
 * and fields to inject instances in the {@link com.azortis.orbis.generator.Dimension} object tree.
 * Subclasses will inherit this annotation.
 */
@Inherited
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

    /**
     * The implementation of a {@link Collection} that should be used when initializing
     * this {@link java.lang.reflect.Field}. Only works on fields that also have a fieldName that refers to a field
     * that has a {@link Collection} of parameterized type {@link String}.
     * Note the implementation must be a mutable one, if you need an immutable view use {@link Invoke} to change
     * the implementation after injection has taken place!
     *
     * @return The implementation type of {@link Collection} that should be used to initialize the field
     */
    Class<? extends Collection> collectionType() default ArrayList.class;

    /**
     * The parameterized type of the {@link Collection} of the {@link java.lang.reflect.Field},
     * this is necessary due to Java type erasure. Only works on fields that have a
     * collectionType and fieldName specified.
     *
     * @return The parameterized type of the {@link java.lang.reflect.Field}
     * @see <a href="https://docs.oracle.com/javase/tutorial/java/generics/erasure.html">Java type erasure</a>
     */
    Class<?> parameterizedType() default Object.class;
}
