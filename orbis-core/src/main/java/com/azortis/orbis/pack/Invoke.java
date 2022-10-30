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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate a method in a {@link Class} that has been annotated with {@link Inject}
 * this method will be called before depending on {@link Order}
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Invoke {
    /**
     * When the method should be invoked, see {@link Order}
     *
     * @since 0.3-Alpha
     */
    Order when() default Order.POST_CLASS_INJECTION;

    enum Order {
        /**
         * Before any injection of the class takes place
         */
        PRE_INJECTION,

        /**
         * After the fields annotated with {@link Inject} have been injected in the {@link Class}
         * but before any of the field objects itself have been injected
         */
        MID_INJECTION,

        /**
         * After all the injection of this class and its subsequent children have taken place
         */
        POST_CLASS_INJECTION,

        /**
         * After all the injection has taken place
         */
        POST_INJECTION
    }

}
