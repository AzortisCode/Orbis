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

import com.azortis.orbis.Registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Marks a class as typed, meaning it can have different implementations based on the provided
 * type {@link net.kyori.adventure.key.Key} field. All classes using this typed based system
 * must register a {@link com.azortis.orbis.Registry} for the class using the following method
 * {@link com.azortis.orbis.Registry#addRegistry(Class, Registry)}</p>
 *
 * <p><b>Note</b> It is recommended to locate the registry statically in the class, and use a static block to
 * register the registry.</p>
 *
 * @since 0.3-Alpha
 * @author Jake Nijssen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Typed {
}
