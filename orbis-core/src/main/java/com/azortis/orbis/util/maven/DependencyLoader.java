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

package com.azortis.orbis.util.maven;

import com.azortis.orbis.Orbis;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;

/**
 * A class that handles downloading of dependencies for the plugin as well as loading dependencies into the plugin.
 */
public class DependencyLoader {
    private static final AccessLoader INJECTOR;
    private static final Logger logger;

    static {
        INJECTOR = AccessLoader.create((URLClassLoader) Orbis.getPlatform().mainClass().getClassLoader());
        logger = Orbis.getLogger();
    }

    /**
     * Load all specified {@link Dependencies} into the plugin on the object.
     *
     * @param object The object to resolve the dependencies from.
     */
    public static void loadAll(Object object) {
        if (object == null) {
            return;
        }
        loadAll(object.getClass());
    }

    /**
     * Load all specified dependencies into the plugin on the class.
     *
     * @param clazz The class to resolve the dependencies from.
     */
    public static void loadAll(Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        Dependency[] dependencies = clazz.getDeclaredAnnotationsByType(Dependency.class);
        for (Dependency dependency : dependencies) {
            load(dependency);
        }
    }

    /**
     * Load the specified {@link Dependency} into the plugin.
     *
     * @param dependency The dependency to load.
     */
    public static void load(Dependency dependency) {
        logger.debug(String.format("Loading dependency %s:%s:%s from %s", dependency.group(),
                dependency.artifact(), dependency.version(), dependency.repository().url()));
        String name = dependency.artifact() + "-" + dependency.version();

        File saveLocation = new File(getLibFolder(), name + ".jar");
        if (!saveLocation.exists()) {
            try {
                logger.info(String.format("Dependency %s is not present in the libraries folder. Attempting to download...", name));
                URL url = getUrl(dependency);
                try (InputStream stream = url.openStream()) {
                    Files.copy(stream, saveLocation.toPath());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info(String.format("Dependency %s has been downloaded to %s", name, saveLocation.getAbsolutePath()));
        }

        if (!saveLocation.exists()) {
            throw new RuntimeException("Unable to download dependency: " + name);
        }

        try {
            INJECTOR.addURL(saveLocation.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unable to load dependency: " + name, e);
        }

        logger.debug(String.format("Dependency %s has been loaded", name));
    }


    /**
     * Get the {@link File} for the library folder.
     *
     * @return The {@link File} for the library folder.
     */
    public static File getLibFolder() {
        File jarFile;
        jarFile = new File(Orbis.getPlatform().directory() + "/libs/");
        File libFolder = new File(jarFile.getParentFile(), "libs");
        if (!libFolder.exists() && !libFolder.mkdirs()) {
            logger.error("Failed to create /libs/ folder!");
        }
        return libFolder;
    }

    /**
     * Get the {@link URL} for the specified dependency.
     *
     * @param dependency The dependency to get the URL for.
     * @return The {@link URL} for the specified dependency.
     * @throws MalformedURLException If the URL is malformed.
     */
    public static URL getUrl(Dependency dependency) throws MalformedURLException {
        String repo = dependency.repository().url();
        if (!repo.endsWith("/")) {
            repo += "/";
        }
        repo += "%s/%s/%s/%s-%s.jar";

        String url = String.format(repo, dependency.group().replace(".", "/"), dependency.artifact(), dependency.version(),
                dependency.artifact(), dependency.version());
        return new URL(url);
    }
}
