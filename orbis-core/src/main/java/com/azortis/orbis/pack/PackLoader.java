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

package com.azortis.orbis.pack;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.World;
import com.azortis.orbis.WorldInfo;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.util.Inject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

public final class PackLoader {

    /**
     * Loads a {@link Dimension} with the context of the specified {@link World}
     *
     * @param world The context to load the dimension
     * @return A loaded {@link Dimension} instance
     * @throws IOException If any file read operation fails
     */
    public static Dimension loadDimension(@NotNull World world)
            throws IOException, IllegalAccessException, NoSuchFieldException {
        File packFolder = world.getSettingsFolder();
        WorldInfo worldInfo = world.getWorldInfo();

        // Load initial Dimension object
        File dimensionFile = new File(packFolder, worldInfo.dimensionFile());
        Dimension dimension = Orbis.getGson().fromJson(new FileReader(dimensionFile), Dimension.class);

        // Loop through all fields of the Dimension class, and build out the object tree.
        for (Field field : dimension.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                if (field.getType() == World.class) {
                    setField(dimension, field, world);
                } else {
                    // Root dimension object doesn't support children yet so do a less general injection
                    String name = field.getAnnotation(Inject.class).fieldName();
                    if (!name.equalsIgnoreCase("")) {
                        Field configNameField = dimension.getClass().getDeclaredField(name);
                        String configName = (String) configNameField.get(dimension);
                        File dataFile = world.getData().getDataFile(field.getType(), configName);
                        Object fieldObject = Orbis.getGson().fromJson(new FileReader(dataFile), field.getType());
                        setField(dimension, field, fieldObject);

                        if (field.getType().isAnnotationPresent(Inject.class)) {
                            // Start a generalized loop for every subsequent object
                            injectDataTree(world, dimension, dimension, fieldObject);
                        }
                    }
                }
            } else if (field.getType().isAnnotationPresent(Inject.class)) {
                // Start a generalized loop for every subsequent object
                field.setAccessible(true);
                Object fieldObject = field.get(dimension);
                field.setAccessible(false);
                injectDataTree(world, dimension, dimension, fieldObject);
            }
        }
        return dimension;
    }

    private static void injectDataTree(@NotNull World world, @NotNull Dimension dimension,
                                       @NotNull Object parentObject, @NotNull Object rootObject)
            throws IOException, IllegalAccessException, NoSuchFieldException {
        for (Field field : rootObject.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                // World and Dimension are considered global injectables
                if (field.getType() == World.class) {
                    setField(rootObject, field, world);
                } else if (field.getType() == Dimension.class) {
                    setField(rootObject, field, dimension);
                } else {
                    Inject inject = field.getAnnotation(Inject.class);
                    if (inject.isChild()) {
                        // Fields that are child will not be explored any further, as that would result in going up the
                        // tree which has 0 use.
                        setField(rootObject, field, parentObject);
                    } else if (!inject.fieldName().equalsIgnoreCase("")) {
                        String name = inject.fieldName();
                        Field configNameField = rootObject.getClass().getDeclaredField(name);
                        String configName = (String) configNameField.get(rootObject);
                        File dataFile = world.getData().getDataFile(field.getType(), configName);
                        Object fieldObject = Orbis.getGson().fromJson(new FileReader(dataFile), field.getType());
                        setField(rootObject, field, fieldObject);

                        if (field.getType().isAnnotationPresent(Inject.class)) {
                            injectDataTree(world, dimension, rootObject, fieldObject);
                        }
                    }
                }
            } else if (field.getType().isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Object fieldObject = field.get(dimension);
                field.setAccessible(false);
                injectDataTree(world, dimension, rootObject, fieldObject);
            }
        }
    }

    private static void setField(@NotNull Object instance, @NotNull Field field, @NotNull Object fieldInstance)
            throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, fieldInstance);
        field.setAccessible(false);
    }

}
