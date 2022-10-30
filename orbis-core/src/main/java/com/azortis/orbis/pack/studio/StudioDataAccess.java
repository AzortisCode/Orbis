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

package com.azortis.orbis.pack.studio;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.Registry;
import com.azortis.orbis.pack.data.Component;
import com.azortis.orbis.pack.data.ComponentAccess;
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.pack.data.DirectoryDataAccess;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.kyori.adventure.key.Key;
import org.apiguardian.api.API;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * DataAccess meant for a {@link Project} to be able to independently index all the {@link ComponentAccess} so that a compilation
 * error won't result in config suggestions to malfunction. As an added benefit it will also map each directory's {@link Path}
 * with the type of files it contains i.e. /biomes/ will map to {@link com.azortis.orbis.generator.biome.Biome}
 * and /generator/terrain/ will map to {@link com.azortis.orbis.generator.terrain.Terrain}.
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@API(status = API.Status.INTERNAL, since = "0.3-Alpha")
public final class StudioDataAccess extends DirectoryDataAccess {

    /**
     * The Map, that maps all the directories to their respective object type.
     */
    private final Map<Path, Class<?>> directoryTypeMap = new HashMap<>();

    /**
     * Constructs a StudioDataAccess for a {@link Project} to actively reset and reindex to provide
     * accurate config suggestions.
     *
     * @param dataDirectory The root project directory.
     * @since 0.3-Alpha
     */
    StudioDataAccess(@NotNull File dataDirectory) {
        super(dataDirectory);
        reset();
    }

    /**
     * Get the {@link Class} type associated with specified directory {@link Path}.
     *
     * @param directory The path of the directory.
     * @return The corresponding class type found in said directory.
     * @throws IllegalArgumentException If the specified Path is not a directory, not absolute {@link Path#isAbsolute()} or
     *                                  if the directory doesn't have a class type mapped to it.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    public @NotNull Class<?> getType(@NotNull Path directory) throws IllegalArgumentException {
        Preconditions.checkArgument(Files.isDirectory(directory, LinkOption.NOFOLLOW_LINKS), "Must be a directory!");
        Preconditions.checkArgument(directory.isAbsolute(), "Directory must be absolute!");
        Preconditions.checkArgument(directoryTypeMap.containsKey(directory), "Directory does not have type registered!");
        return directoryTypeMap.get(directory);
    }

    /**
     * Resets this {@link DataAccess} making it remap all the folders with their respective types.
     *
     * @throws IllegalStateException If a data path is not absolute, signalling that something up in the chain went wrong.
     * @since 0.3-Alpha
     */
    @Override
    @SuppressWarnings("PatternValidation")
    @Contract(pure = true)
    public void reset() throws IllegalStateException {
        // Make sure the parent class clears out their component registry.
        super.reset();
        directoryTypeMap.clear();

        // Simple mapping for data types that do not support Components.
        for (Map.Entry<Class<?>, String> entry : DATA_TYPES.entrySet()) {
            // If the dataPath ends with ** it means that all the subdirectories should be assigned to this type as well.
            if (entry.getValue().endsWith("**")) {
                Path dataPath = Path.of(dataDirectory.toPath() + entry.getValue().replace("**", "").trim());
                Preconditions.checkState(dataPath.isAbsolute(), "Path is not absolute for "
                        + entry.getKey().getSimpleName());
                searchSubDirectories(dataPath, entry.getKey());
                continue;
            }
            Path dataPath = Path.of(dataDirectory.toPath() + entry.getValue().trim());
            Preconditions.checkState(dataPath.isAbsolute(), "Path is not absolute for "
                    + entry.getKey().getSimpleName());
            // Register the directory as this type. This may also happen if the directory of the path doesn't exist.
            directoryTypeMap.put(dataPath, entry.getKey());
        }

        // Mapping for data types that support components.
        for (Map.Entry<Class<?>, String> entry : GENERATOR_TYPES.entrySet()) {
            final Path dataPath = Path.of(dataDirectory.toPath() + entry.getValue().trim());
            Preconditions.checkState(dataPath.isAbsolute(), "Path is not absolute for "
                    + entry.getKey().getSimpleName());
            directoryTypeMap.put(dataPath, entry.getKey());

            // If the folder of this root type actually exists, we can search if further for possible component folders.
            if (Files.exists(dataPath, LinkOption.NOFOLLOW_LINKS)) {
                try (Stream<Path> dataPaths = Files.list(dataPath)) {
                    // Get all the directories.
                    dataPaths.filter(path -> Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
                            .forEach(path -> {
                                String componentName = path.getFileName().toString().replaceAll("/", "").trim();
                                // Create a Path for the corresponding config file this component folder belongs to.
                                Path componentFile = Path.of(dataPath + componentName + ".json");

                                // Make sure the config file for the component actually exists, otherwise throw an error
                                // informing the user of this "compilation" error.
                                if (Files.exists(componentFile, LinkOption.NOFOLLOW_LINKS) &&
                                        Files.isRegularFile(componentFile, LinkOption.NOFOLLOW_LINKS)) {
                                    try {
                                        // Read a representation of the config file itself, to check to which implementation
                                        // type this Component belongs
                                        JsonObject componentObject = Orbis.getGson()
                                                .fromJson(new FileReader(componentFile.toFile()), JsonObject.class);

                                        // Make sure the type is actually defined, else throw an error informing the user
                                        // of this mistake.
                                        if (componentObject.has("type")) {
                                            Key key = Key.key(componentObject.get("type").getAsString());

                                            // Get the associated registry for the ROOT_TYPE and get the implementation type
                                            // of this component.
                                            Registry<?> typeRegistry = Registry.getRegistry(entry.getKey());
                                            if (typeRegistry.hasType(key)) {
                                                Class<?> type = typeRegistry.getType(key);

                                                // Make sure the type actually uses the Component system, otherwise inform the user.
                                                if (type.isAnnotationPresent(Component.class)) {
                                                    // Construct a component access instance and add it to the registry
                                                    Class<? extends ComponentAccess> componentAccessClass = type
                                                            .getAnnotation(Component.class).value();
                                                    ComponentAccess access = componentAccessClass
                                                            .getConstructor(String.class, DataAccess.class)
                                                            .newInstance(componentName, this);
                                                    registerComponent(componentAccessClass, componentName);
                                                    // Register all the subdirectories for the component.
                                                    searchComponentDirectory(path, access);
                                                } else {
                                                    Orbis.getLogger().error("Type {} is not a component type!", key);
                                                }
                                            } else {
                                                Orbis.getLogger().error("Type {} not found in registry for {}!", key.asString(),
                                                        entry.getKey().getSimpleName());
                                            }
                                        } else {
                                            Orbis.getLogger().error("The datafile for component folder {} doesn't have a type specified!", componentName);
                                        }
                                    } catch (FileNotFoundException ignored) {
                                        Orbis.getLogger().error("Data file for component folder {} for type {} not found!",
                                                componentName, entry.getKey().getSimpleName());
                                    } catch (InvocationTargetException | InstantiationException |
                                             IllegalAccessException |
                                             NoSuchMethodException e) {
                                        // This is definitely a programming error, and thus should throw an error.
                                        Orbis.getLogger().error("Something went wrong when trying to load component access.");
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    Orbis.getLogger().error("Component {} of type {} doesn't have a {}.json file present!",
                                            componentName, entry.getKey().getSimpleName(), componentName);
                                }
                            });
                } catch (IOException ex) {
                    Orbis.getLogger().error("An error occurred while trying to populate the studio data access");
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Recursively register specified directory and all its subdirectories as specified type.
     *
     * @param rootPath The directory path to register type with, and search for subdirectories.
     * @param type     The type to mark the directory and subdirectories as.
     * @throws IllegalArgumentException If given path is not a directory.
     * @since 0.3-Alpha
     */
    @Contract(pure = true)
    private void searchSubDirectories(@NotNull Path rootPath, @NotNull final Class<?> type) throws IllegalArgumentException {
        Preconditions.checkArgument(Files.isDirectory(rootPath, LinkOption.NOFOLLOW_LINKS), "Path must be a directory");
        directoryTypeMap.put(rootPath, type);
        if (Files.exists(rootPath, LinkOption.NOFOLLOW_LINKS)) {
            try (Stream<Path> dataPaths = Files.list(rootPath)) {
                dataPaths.filter(path -> Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
                        .forEach(path -> searchSubDirectories(path, type));
            } catch (IOException ex) {
                Orbis.getLogger().error("An error occurred while trying to populate the studio data access");
                ex.printStackTrace();
            }
        }
    }

    /**
     * Maps all the directories for a component to the types registered in the {@link ComponentAccess}.
     *
     * @param componentPath The path of the component instance its directory.
     * @param access        The ComponentAccess instance of the component.
     * @throws IllegalStateException If given path is not absolute, meaning something up in the chain went wrong.
     */
    @Contract(pure = true)
    private void searchComponentDirectory(@NotNull Path componentPath, @NotNull ComponentAccess access) throws IllegalStateException {
        for (Class<?> dataType : access.dataTypes()) {
            if (access.getDataPath(dataType).equals("/**")) {
                if (access.dataTypes().size() == 1) {
                    directoryTypeMap.put(componentPath, dataType);
                    break;
                }
                Orbis.getLogger().error("ComponentAccess class {} has mapped a type to \"/**\", but has more then one type!",
                        access.getClass().getSimpleName());
            } else if (access.getDataPath(dataType).equals("/") && directoryTypeMap.putIfAbsent(componentPath, dataType) != null) {
                Orbis.getLogger().error("ComponentAccess class {} has mapped two or more types to \"/\"!",
                        access.getClass().getSimpleName());
                continue;
            }
            if (access.getDataPath(dataType).endsWith("**")) {
                final Path dataPath = Path.of(componentPath + access.getDataPath(dataType).replace("**", "").trim());
                Preconditions.checkState(dataPath.isAbsolute(), "Path is not absolute for "
                        + dataType.getSimpleName());
                searchSubDirectories(dataPath, dataType);
            } else {
                final Path dataPath = Path.of(componentPath + access.getDataPath(dataType).trim());
                Preconditions.checkState(dataPath.isAbsolute(), "Path is not absolute for "
                        + dataType.getSimpleName());
                directoryTypeMap.put(dataPath, dataType);
            }
        }
    }
}
