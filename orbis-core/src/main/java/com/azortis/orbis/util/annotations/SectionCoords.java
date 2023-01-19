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

package com.azortis.orbis.util.annotations;

import java.lang.annotation.*;

/**
 * <p>Marks that the {@link Integer} parameters of the annotated {@link java.lang.reflect.Constructor},
 * {@link java.lang.reflect.Method} or record parameters should be section coordinates.</p>
 *
 * <p>Section coordinates are basically zoomed out real coordinates. A good example that uses section
 * coordinates are BiomeSections, since biomes are saved in 4x4x4 block sections.</p>
 *
 * <p>To get the section coord from a block coordinate you do: {@code int sectionCoord = blockCoord >> 2;}</p>
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface SectionCoords {
}
