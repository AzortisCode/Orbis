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
import com.azortis.orbis.util.Invoke;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class PackLoader {
    private static final Map<World, Map<Method, Object>> postInjectionMethods = new HashMap<>();

    /**
     * Loads a {@link Dimension} with the context of the specified {@link World}
     *
     * @param world The context to load the dimension
     * @return A loaded {@link Dimension} instance
     * @throws IOException If any file read operation fails
     */
    @SuppressWarnings("unchecked")
    public static Dimension loadDimension(@NotNull World world)
            throws IOException, IllegalAccessException, NoSuchFieldException, InvocationTargetException,
            NoSuchMethodException, InstantiationException {
        File packFolder = world.getSettingsFolder();
        WorldInfo worldInfo = world.getWorldInfo();
        postInjectionMethods.put(world, new HashMap<>());

        // Load initial Dimension object
        File dimensionFile = new File(packFolder, worldInfo.dimensionFile() + ".json");
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
                        configNameField.setAccessible(true);
                        if (Collection.class.isAssignableFrom(field.getType())) { // TODO maybe add Array support?
                            Collection<String> configNames = (Collection<String>) configNameField.get(dimension);
                            configNameField.setAccessible(false);
                            Inject inject = field.getAnnotation(Inject.class);
                            Collection<Object> fieldObject = inject.collectionType().getConstructor().newInstance();
                            for (String configName : configNames) {
                                File dataFile = world.getData().getDataFile(inject.parameterizedType(), configName);
                                Object collectionObject = Orbis.getGson().fromJson(new FileReader(dataFile), inject.parameterizedType());
                                fieldObject.add(collectionObject);
                            }
                            setField(dimension, field, fieldObject);
                            if (shouldInject(field.getGenericType().getClass())) {
                                for (Object listObject : fieldObject) {
                                    classInjection(world, dimension, dimension, listObject);
                                }
                            }
                        } else {
                            String configName = (String) configNameField.get(dimension);
                            configNameField.setAccessible(false);
                            File dataFile = world.getData().getDataFile(field.getType(), configName);
                            Object fieldObject = Orbis.getGson().fromJson(new FileReader(dataFile), field.getType());
                            setField(dimension, field, fieldObject);

                            if (shouldInject(fieldObject.getClass())) {
                                classInjection(world, dimension, dimension, fieldObject);
                            }
                        }
                    }
                }
            } else if (shouldInject(field, dimension)) {
                defaultInjection(world, dimension, dimension, field);
            }
        }
        for (Map.Entry<Method, Object> entry : postInjectionMethods.get(world).entrySet()) {
            invokeMethod(entry.getValue(), entry.getKey());
        }
        postInjectionMethods.remove(world);
        return dimension;
    }

    // Called if a Class has been annotated with @Inject
    @SuppressWarnings("unchecked")
    private static void classInjection(@NotNull World world, @NotNull Dimension dimension,
                                       @NotNull Object parentObject, @NotNull Object rootObject)
            throws NoSuchFieldException, IllegalAccessException, InvocationTargetException,
            IOException, NoSuchMethodException, InstantiationException {
        for (Method method : getAllMethods(rootObject.getClass())) {
            if (method.isAnnotationPresent(Invoke.class)) {
                if (method.getAnnotation(Invoke.class).when() == Invoke.Order.PRE_INJECTION) {
                    invokeMethod(rootObject, method);
                } else if (method.getAnnotation(Invoke.class).when() == Invoke.Order.POST_INJECTION) {
                    postInjectionMethods.get(world).put(method, rootObject);
                }
            }
        }

        for (Field field : getAllFields(rootObject.getClass())) {
            if (field.isAnnotationPresent(Inject.class)) {
                if (field.getType() == World.class) {
                    setField(rootObject, field, world);
                } else if (field.getType() == Dimension.class) {
                    setField(rootObject, field, dimension);
                } else {
                    Inject inject = field.getAnnotation(Inject.class);
                    if (inject.isChild()) {
                        setField(rootObject, field, parentObject);
                    } else if (!inject.fieldName().equalsIgnoreCase("")) {
                        String name = inject.fieldName();
                        nameInjection(world, rootObject, field, name);
                    }
                }
            }
        }

        for (Method method : getAllMethods(rootObject.getClass())) {
            if (method.isAnnotationPresent(Invoke.class) &&
                    method.getAnnotation(Invoke.class).when() == Invoke.Order.MID_INJECTION) {
                invokeMethod(rootObject, method);
            }
        }

        for (Field field : getAllFields(rootObject.getClass())) {
            if (Collection.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                Collection<Object> fieldObject = (Collection<Object>) field.get(rootObject);
                field.setAccessible(false);
                for (Object collectionObject : fieldObject) {
                    if (shouldInject(collectionObject.getClass())) {
                        classInjection(world, dimension, rootObject, collectionObject);
                    }
                }
            } else if (shouldInject(field, rootObject)) {
                defaultInjection(world, dimension, rootObject, field);
            }
        }

        for (Method method : getAllMethods(rootObject.getClass())) {
            if (method.isAnnotationPresent(Invoke.class) &&
                    method.getAnnotation(Invoke.class).when() == Invoke.Order.POST_CLASS_INJECTION) {
                invokeMethod(rootObject, method);
            }
        }
    }

    // Called if a field in a class that is being injected its class has been annotated with @Inject
    private static void defaultInjection(@NotNull World world, @NotNull Dimension dimension,
                                         @NotNull Object rootObject, @NotNull Field field)
            throws NoSuchFieldException, IllegalAccessException, InvocationTargetException,
            IOException, NoSuchMethodException, InstantiationException {
        field.setAccessible(true);
        Object fieldObject = field.get(rootObject);
        field.setAccessible(false);
        classInjection(world, dimension, rootObject, fieldObject);
    }

    // Called if a field in a class that is being injected has been annotated with @Inject that specifies the name of
    // another field
    @SuppressWarnings("unchecked")
    private static void nameInjection(@NotNull World world, @NotNull Object rootObject,
                                      @NotNull Field field, @NotNull String name)
            throws NoSuchFieldException, IllegalAccessException, IOException,
            NoSuchMethodException, InvocationTargetException, InstantiationException {
        Field configNameField = getDeclaredField(rootObject.getClass(), name);
        configNameField.setAccessible(true);
        if (Collection.class.isAssignableFrom(field.getType())) {
            Collection<String> configNames = (Collection<String>) configNameField.get(rootObject);
            configNameField.setAccessible(false);
            Inject inject = field.getAnnotation(Inject.class);
            Collection<Object> fieldObject = inject.collectionType().getConstructor().newInstance();
            for (String configName : configNames) {
                File dataFile = world.getData().getDataFile(inject.parameterizedType(), configName);
                Object collectionObject = Orbis.getGson().fromJson(new FileReader(dataFile), inject.parameterizedType());
                fieldObject.add(collectionObject);
            }
            setField(rootObject, field, fieldObject);
        } else {
            String configName = (String) configNameField.get(rootObject);
            configNameField.setAccessible(false);
            File dataFile = world.getData().getDataFile(field.getType(), configName);
            Object fieldObject = Orbis.getGson().fromJson(new FileReader(dataFile), field.getType());
            setField(rootObject, field, fieldObject);
        }
    }

    private static boolean shouldInject(final Field field, Object rootObject) throws IllegalAccessException {
        field.setAccessible(true);
        Object fieldObject = field.get(rootObject);
        field.setAccessible(false);
        if (fieldObject == null) {
            return false;
        } else if (field.isAnnotationPresent(Inject.class) && field.getAnnotation(Inject.class).isChild()) {
            // Prevent a StackOverflow
            return false;
        }
        return shouldInject(fieldObject.getClass());
    }

    private static boolean shouldInject(final Class<?> type) {
        boolean inject = type.isAnnotationPresent(Inject.class);
        if (inject) return true;
        Class<?> superType = type;
        while (superType != null) {
            superType = superType.getSuperclass();
            if (superType == Object.class) {
                superType = null;
            } else {
                inject = superType.isAnnotationPresent(Inject.class);
                if (inject) superType = null;
            }
        }
        return inject;
    }

    // Get all the methods from a class type, including ones its inheriting from superclasses
    private static List<Method> getAllMethods(final Class<?> type) {
        List<Method> methods = new ArrayList<>(Arrays.asList(type.getDeclaredMethods()));
        Class<?> superType = type;
        while (superType != null) {
            superType = superType.getSuperclass();
            if (superType == Object.class) {
                superType = null;
            } else {
                methods.addAll(new ArrayList<>(Arrays.asList(type.getDeclaredMethods())));
            }
        }
        return methods;
    }

    // Get all the fields from a class type, including ones its inheriting from superclasses
    private static List<Field> getAllFields(final Class<?> type) {
        List<Field> fields = new ArrayList<>(Arrays.asList(type.getDeclaredFields()));
        Class<?> superType = type;
        while (superType != null) {
            superType = superType.getSuperclass();
            if (superType == Object.class) {
                superType = null;
            } else {
                fields.addAll(new ArrayList<>(Arrays.asList(superType.getDeclaredFields())));
            }
        }
        return fields;
    }

    private static Field getDeclaredField(final Class<?> type, String fieldName) throws NoSuchFieldException {
        Field declaredField = null;
        for (Field field : getAllFields(type)) {
            if (field.getName().equals(fieldName)) {
                declaredField = field;
                break;
            }
        }
        if (declaredField == null) throw new NoSuchFieldException(fieldName);
        return declaredField;
    }

    private static void invokeMethod(@NotNull Object instance, @NotNull Method method)
            throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        method.invoke(instance);
        method.setAccessible(false);
    }

    private static void setField(@NotNull Object instance, @NotNull Field field, @NotNull Object fieldInstance)
            throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, fieldInstance);
        field.setAccessible(false);
    }

}
