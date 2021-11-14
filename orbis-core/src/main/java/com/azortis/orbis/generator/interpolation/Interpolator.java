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

package com.azortis.orbis.generator.interpolation;

/**
 * This class uses Cyberpwn his starcast algorithm, and the idea of using a HeightProvider also comes from them.
 */
public class Interpolator {

    private double lerp(double a, double b, double f) {
        return a + (f * (b - a));
    }

    private double blerp(double a, double b, double c, double d, double tx, double ty) {
        return lerp(lerp(a, b, tx), lerp(c, d, tx), ty);
    }

    private double getBilinearNoise(int x, int z, double scale, HeightProvider provider) {
        int fx = (int) Math.floor(x / scale);
        int fz = (int) Math.floor(z / scale);
        int x1 = (int) Math.round(fx * scale);
        int z1 = (int) Math.round(fz * scale);
        int x2 = (int) Math.round((fx + 1) * scale);
        int z2 = (int) Math.round((fz + 1) * scale);

        double px = rangeScale(0, 1, x1, x2, x);
        double pz = rangeScale(0, 1, z1, z2, z);

        return blerp(
                provider.getHeight(x1, z1),
                provider.getHeight(x2, z1),
                provider.getHeight(x1, z2),
                provider.getHeight(x2, z2),
                px, pz);
    }

    private double rangeScale(double amin, double amax, double bmin, double bmax, double b) {
        return amin + ((amax - amin) * ((b - bmin) / (bmax - bmin)));
    }

    private double getStarCast(int x, int z, double scale, HeightProvider provider) {
        double m = (360 / 9d);
        double v = 0;

        for (int i = 0; i < 360; i += m) {
            double sin = Math.sin(Math.toRadians(i));
            double cos = Math.cos(Math.toRadians(i));
            double cx = x + ((scale * cos) - (scale * sin));
            double cz = z + ((scale * sin) + (scale * cos));
            v += provider.getHeight(cx, cz);
        }
        return v / 9d;
    }

    public int getFinalHeight(int x, int z, double scale, HeightProvider provider) {
        return (int) getStarCast(x, z, scale, (xx, zz) -> getBilinearNoise((int) xx, (int) zz, scale, provider));
    }

}
