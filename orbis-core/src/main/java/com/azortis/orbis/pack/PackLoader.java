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

import com.azortis.orbis.Orbis;
import com.azortis.orbis.generator.Dimension;
import com.azortis.orbis.pack.data.Component;
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.world.World;
import com.azortis.orbis.world.WorldInfo;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class PackLoader { // TODO Properly document this class, so it becomes less confusing
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
        File packFolder = world.settingsDirectory();
        WorldInfo worldInfo = world.getWorldInfo();
        postInjectionMethods.put(world, new HashMap<>());

        // Load initial Dimension object
        File dimensionFile = new File(packFolder, worldInfo.dimensionFile() + ".json");
        Dimension dimension = Orbis.getGson().fromJson(new FileReader(dimensionFile), Dimension.class);

        // Reset the data access.
        world.data().reset();

        // Loop through all fields of the Dimension class, and build out the object tree.
        for (Field field : dimension.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                if (field.getType() == World.class) {
                    setField(dimension, field, world);
                } else {
                    // Root dimension object doesn't support children yet so do a less general injection
                    String name = field.getAnnotation(Inject.class).fieldName();
                    if (!name.equalsIgnoreCase("")) {
                        Field dataPathNameField = dimension.getClass().getDeclaredField(name);
                        dataPathNameField.setAccessible(true);
                        if (Collection.class.isAssignableFrom(field.getType())) { // TODO maybe add Array support?
                            Collection<String> dataPaths = (Collection<String>) dataPathNameField.get(dimension);
                            dataPathNameField.setAccessible(false);
                            Inject inject = field.getAnnotation(Inject.class);
                            Collection<Object> fieldObject = inject.collectionType().getConstructor().newInstance();
                            Map<Object, String> componentNames = new HashMap<>();
                            boolean checkComponents = false;
                            for (String dataPath : dataPaths) {
                                File dataFile = world.data().getDataFile(inject.parameterizedType(), dataPath);
                                Object collectionObject = Orbis.getGson().fromJson(new FileReader(dataFile), inject.parameterizedType());
                                fieldObject.add(collectionObject);
                                if (collectionObject.getClass().isAnnotationPresent(Component.class)
                                        && DataAccess.GENERATOR_TYPES.containsKey(inject.parameterizedType())) {
                                    componentNames.put(collectionObject, FilenameUtils.removeExtension(dataFile.getName()));
                                    checkComponents = true;
                                }
                            }
                            setField(dimension, field, fieldObject);
                            if (shouldInject(inject.parameterizedType())) {
                                if (checkComponents) {
                                    for (Object collectionObject : fieldObject) {
                                        if (collectionObject.getClass().isAnnotationPresent(Component.class)) {
                                            String componentName = componentNames.get(collectionObject);
                                            world.data().registerComponent(collectionObject.getClass()
                                                    .getAnnotation(Component.class).value(), componentName);
                                            classInjection(world, dimension, dimension, collectionObject, componentName);
                                        } else {
                                            classInjection(world, dimension, dimension, collectionObject, null);
                                        }
                                    }
                                } else {
                                    for (Object collectionObject : fieldObject) {
                                        classInjection(world, dimension, dimension, collectionObject, null);
                                    }
                                }
                            }
                        } else {
                            String dataPath = (String) dataPathNameField.get(dimension);
                            dataPathNameField.setAccessible(false);
                            File dataFile = world.data().getDataFile(field.getType(), dataPath);
                            Object fieldObject = Orbis.getGson().fromJson(new FileReader(dataFile), field.getType());
                            setField(dimension, field, fieldObject);

                            if (shouldInject(fieldObject.getClass())) {
                                if (fieldObject.getClass().isAnnotationPresent(Component.class)
                                        && DataAccess.GENERATOR_TYPES.containsKey(field.getType())) {
                                    String componentName = FilenameUtils.removeExtension(dataFile.getName());
                                    world.data().registerComponent(fieldObject.getClass()
                                            .getAnnotation(Component.class).value(), componentName);
                                    classInjection(world, dimension, dimension, fieldObject, componentName);
                                } else {
                                    classInjection(world, dimension, dimension, fieldObject, null);
                                }
                            }
                        }
                    }
                }
            } else if (shouldInject(field, dimension)) {
                field.setAccessible(true);
                Object fieldObject = field.get(dimension);
                field.setAccessible(false);
                classInjection(world, dimension, dimension, fieldObject, null);
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
                                       @NotNull Object parentObject, @NotNull Object rootObject,
                                       @Nullable String componentName)
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

        Map<Object, String> componentNames = new HashMap<>();

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
                        if (componentName != null) {
                            nameInjection(world, rootObject, field, name, componentName);
                        } else {
                            componentNames.putAll(nameInjection(world, rootObject, field, name, null));
                        }
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

        boolean checkComponents = !componentNames.isEmpty();
        for (Field field : getAllFields(rootObject.getClass())) {
            if (Collection.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                Collection<Object> fieldObject = (Collection<Object>) field.get(rootObject);
                field.setAccessible(false);
                for (Object collectionObject : fieldObject) {
                    if (shouldInject(collectionObject.getClass())) {
                        if (checkComponents && componentNames.containsKey(collectionObject)) {
                            world.data().registerComponent(collectionObject.getClass().getAnnotation(Component.class).value(),
                                    componentNames.get(collectionObject));
                            classInjection(world, dimension, rootObject, collectionObject, componentNames.get(collectionObject));
                        } else {
                            classInjection(world, dimension, rootObject, collectionObject, componentName);
                        }
                    }
                }
            } else if (shouldInject(field, rootObject)) {
                field.setAccessible(true);
                Object fieldObject = field.get(rootObject);
                field.setAccessible(false);

                if (checkComponents && componentNames.containsKey(fieldObject)) {
                    world.data().registerComponent(fieldObject.getClass().getAnnotation(Component.class).value(),
                            componentNames.get(fieldObject));
                    classInjection(world, dimension, rootObject, fieldObject, componentNames.get(fieldObject));
                } else {
                    classInjection(world, dimension, rootObject, fieldObject, componentName);
                }
            }
        }

        for (Method method : getAllMethods(rootObject.getClass())) {
            if (method.isAnnotationPresent(Invoke.class) &&
                    method.getAnnotation(Invoke.class).when() == Invoke.Order.POST_CLASS_INJECTION) {
                invokeMethod(rootObject, method);
            }
        }
    }

    // Called if a field in a class that is being injected has been annotated with @Inject that specifies the name of
    // another field
    @SuppressWarnings("unchecked")
    private static @NotNull Map<Object, String> nameInjection(@NotNull World world, @NotNull Object rootObject,
                                                              @NotNull Field field, @NotNull String name, @Nullable String componentName)
            throws NoSuchFieldException, IllegalAccessException, IOException,
            NoSuchMethodException, InvocationTargetException, InstantiationException {
        Map<Object, String> componentNames = new HashMap<>();
        Field dataPathNameField = getDeclaredField(rootObject.getClass(), name);
        dataPathNameField.setAccessible(true);
        if (Collection.class.isAssignableFrom(field.getType())) {
            Collection<String> dataPaths = (Collection<String>) dataPathNameField.get(rootObject);
            dataPathNameField.setAccessible(false);
            Inject inject = field.getAnnotation(Inject.class);
            Collection<Object> fieldObject = inject.collectionType().getConstructor().newInstance();
            for (String dataPath : dataPaths) {
                if (world.data().isComponentType(inject.parameterizedType())) {
                    if (componentName != null) {
                        File dataFile = world.data().getDataFile(inject.parameterizedType(), componentName, dataPath);
                        Object collectionObject = Orbis.getGson().fromJson(new FileReader(dataFile), inject.parameterizedType());
                        fieldObject.add(collectionObject);
                    } else
                        throw new IllegalStateException("Parameterized type is a component type, but a name hasn't been passed down");
                } else {
                    File dataFile = world.data().getDataFile(inject.parameterizedType(), dataPath);
                    Object collectionObject = Orbis.getGson().fromJson(new FileReader(dataFile), inject.parameterizedType());
                    fieldObject.add(collectionObject);

                    if (collectionObject.getClass().isAnnotationPresent(Component.class)
                            && DataAccess.GENERATOR_TYPES.containsKey(inject.parameterizedType())) {
                        String collectionComponentName = FilenameUtils.removeExtension(dataFile.getName());
                        componentNames.put(collectionObject, collectionComponentName);
                    }
                }
            }
            setField(rootObject, field, fieldObject);
        } else {
            String dataPath = (String) dataPathNameField.get(rootObject);
            dataPathNameField.setAccessible(false);

            if (world.data().isComponentType(field.getType())) {
                if (componentName != null) {
                    File dataFile = world.data().getDataFile(field.getType(), componentName, dataPath);
                    Object fieldObject = Orbis.getGson().fromJson(new FileReader(dataFile), field.getType());
                    setField(rootObject, field, fieldObject);
                } else
                    throw new IllegalStateException("Field type is a component type, but a name hasn't been passed down");
            } else {
                File dataFile = world.data().getDataFile(field.getType(), dataPath);
                Object fieldObject = Orbis.getGson().fromJson(new FileReader(dataFile), field.getType());
                setField(rootObject, field, fieldObject);

                if (fieldObject.getClass().isAnnotationPresent(Component.class)
                        && DataAccess.GENERATOR_TYPES.containsKey(field.getType())) {
                    String fieldComponentName = FilenameUtils.removeExtension(dataFile.getName());
                    componentNames.put(fieldObject, fieldComponentName);
                }
            }
        }
        return componentNames;
    }

    private static boolean shouldInject(final @NotNull Field field, Object rootObject) throws IllegalAccessException {
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

    private static boolean shouldInject(final @NotNull Class<?> type) {
        boolean inject = type.isAnnotationPresent(Inject.class);
        if (inject)
            return true; // TODO check if searching superclasses is still necessary since @Inject has been annotated with @Inherited
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
    private static @NotNull List<Method> getAllMethods(final @NotNull Class<?> type) {
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
    private static @NotNull List<Field> getAllFields(final @NotNull Class<?> type) {
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

    private static @NotNull Field getDeclaredField(final Class<?> type, String fieldName) throws NoSuchFieldException {
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

    private static void invokeMethod(@NotNull Object classInstance, @NotNull Method method)
            throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        method.invoke(classInstance);
        method.setAccessible(false);
    }

    private static void setField(@NotNull Object classInstance, @NotNull Field field, @NotNull Object fieldInstance)
            throws IllegalAccessException {
        field.setAccessible(true);
        field.set(classInstance, fieldInstance);
        field.setAccessible(false);
    }

}
