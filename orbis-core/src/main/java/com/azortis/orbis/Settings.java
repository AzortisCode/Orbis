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

package com.azortis.orbis;

import com.azortis.orbis.util.Location;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

/**
 * Settings class which may be extended by platforms for some basic
 * global settings that are not world/dimension dependent.
 */
@SuppressWarnings("all")
public class Settings {

    private final Studio studio;

    /**
     * Used for Platform implementations of default settings
     *
     * @param settings The default settings
     */
    public Settings(@NotNull Settings settings) {
        this.studio = settings.studio;
    }

    public Settings(@NotNull Studio studio) {
        this.studio = studio;
    }

    public Studio studio() {
        return studio;
    }

    public static class Studio {
        private final boolean openVSCode;
        private final Location fallBackLocation;

        public Studio(boolean openVSCode, @NotNull Location fallBackLocation) {
            this.openVSCode = openVSCode;
            this.fallBackLocation = fallBackLocation;
        }

        public boolean openVSCode() {
            return openVSCode;
        }

        public @NotNull Location fallBackLocation() {
            return fallBackLocation;
        }

        public static Studio defaultStudioSettings() {
            return new Studio(false, new Location(0, 0, 0, 0f, 0f, new WeakReference<>(null)));
        }
    }

    public static Settings defaultSettings() {
        return new Settings(Studio.defaultStudioSettings());
    }

}
