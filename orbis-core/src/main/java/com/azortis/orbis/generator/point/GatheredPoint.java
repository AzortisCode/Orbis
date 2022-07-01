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

package com.azortis.orbis.generator.point;

import lombok.Getter;
import lombok.Setter;

/**
 * Credits to k.jpg from FastNoise for making this class.
 *
 * @param <TTag> The type tag of the point gatherer.
 */
@Getter
public class GatheredPoint<TTag> {
    private final double x, z;
    private final int hash;
    @Setter
    private TTag tag;

    public GatheredPoint(double x, double z, int hash) {
        this.x = x;
        this.z = z;
        this.hash = hash;
    }
}