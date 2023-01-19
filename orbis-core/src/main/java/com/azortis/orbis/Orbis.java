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

package com.azortis.orbis;

import cloud.commandframework.CommandManager;
import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.meta.SimpleCommandMeta;
import com.azortis.orbis.block.ConfiguredBlock;
import com.azortis.orbis.command.CommandSender;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.noise.Noise;
import com.azortis.orbis.generator.surface.Surface;
import com.azortis.orbis.pack.PackManager;
import com.azortis.orbis.pack.adapter.BlockAdapter;
import com.azortis.orbis.pack.adapter.KeyAdapter;
import com.azortis.orbis.pack.adapter.LocationAdapter;
import com.azortis.orbis.pack.adapter.TypeAdapter;
import com.azortis.orbis.pack.data.ComponentAccess;
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.pack.studio.ProjectManager;
import com.azortis.orbis.util.Location;
import com.azortis.orbis.util.maven.Dependency;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The main entry point for the Orbis dimension "library".
 *
 * @author Jake Nijssen
 * @since 0.3-Alpha
 */
@Dependency(group = "com.google.guava", artifact = "guava", version = "31.0.1-jre")
@Dependency(group = "net.lingala.zip4j", artifact = "zip4j", version = "2.10.0")
@Dependency(group = "org.projectlombok", artifact = "lombok", version = "1.18.24")
@Dependency(group = "cloud.commandframework", artifact = "cloud-core", version = "1.8.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-annotations", version = "1.8.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-services", version = "1.8.0")
@Dependency(group = "io.leangen.geantyref", artifact = "geantyref", version = "1.3.13")
@Dependency(group = "org.apache.commons", artifact = "commons-lang3", version = "3.12.0")
@API(status = API.Status.MAINTAINED, since = "0.3-Alpha")
public final class Orbis {

    public static final String VERSION = "0.3-Alpha";
    public static final String SETTINGS_VERSION = "1";
    public static final String MC_VERSION = "1_19_3";
    private static final String DOWNLOAD_URL = "https://raw.githubusercontent.com/Articdive/ArticData/" +
            MC_VERSION.replace("_", ".") + "/";

    private static boolean initialized = false;
    private static Platform platform = null;
    private static File settingsFile = null;
    private static Settings settings = null;
    private static CommandManager<CommandSender> commandManager = null;
    private static Logger logger;
    private static Gson gson;
    private static MiniMessage miniMessage;

    // Managers
    private static PackManager packManager;
    private static ProjectManager projectManager;

    private Orbis() {
    }

    /**
     * Initialize Orbis with the specified {@link Platform} implementation.
     *
     * @param platform The platform instance.
     * @since 0.3-Alpha
     */
    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = {"com.azortis.orbis.paper"})
    public static void initialize(Platform platform) {
        // Only initialize once.
        if (!initialized) {
            initialized = true;
            Orbis.platform = platform;
            logger = platform.logger();
            logger.info("Initializing {} adaptation of Orbis", platform.adaptation());


            // Register the type adapters to use in the serialization/deserialization of settings in packs.
            final GsonBuilder builder = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting()
                    .registerTypeAdapter(Key.class, new KeyAdapter())
                    .registerTypeAdapter(ConfiguredBlock.class, new BlockAdapter())
                    .registerTypeAdapter(Location.class, new LocationAdapter())
                    .registerTypeAdapter(Surface.class, new TypeAdapter<>(Surface.class))
                    .registerTypeAdapter(Distributor.class, new TypeAdapter<>(Distributor.class))
                    .registerTypeAdapter(Noise.class, new TypeAdapter<>(Noise.class));

            getRegistryAdapters().forEach(entry -> builder.registerTypeAdapter(entry.getKey(), entry.getValue()));

            gson = builder.create();

            // Load settings
            settingsFile = new File(platform.directory(), "settings.json");
            if (!settingsFile.exists()) {
                settings = platform.defaultSettings();
                saveSettings();
            } else {
                reloadSettings();
            }

            // Init message deserializer
            miniMessage = MiniMessage.builder()
                    .tags(TagResolver.builder()
                            .resolver(StandardTags.defaults())
                            .resolver(TagResolver.resolver("prefix", Tag.selfClosingInserting(Orbis::getPrefixComponent))).build())
                    .build();

            // Load managers
            packManager = new PackManager(platform.directory());
            projectManager = new ProjectManager(platform.directory());
        }
    }

    /**
     * Shutdowns Orbis.
     *
     * @since 0.3-Alpha
     */
    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = {"com.azortis.orbis.paper"})
    public static void shutdown() {
        if (initialized) {
            projectManager.closeProject();
        }
    }

    /**
     * Registers all the {@link JsonDeserializer} of each {@link DataAccess#GENERATOR_TYPES} its implementation
     * {@link ComponentAccess} with the global {@link Gson} instance. Will also index all the data types with the
     * generator type its {@link Registry}.
     *
     * @return A collection containing each class type and associated JsonDeserializer.
     * @since 0.3-Alpha
     */
    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = {"com.azortis.orbis"})
    private static List<Map.Entry<Class<?>, JsonDeserializer<?>>> getRegistryAdapters() {
        List<Map.Entry<Class<?>, JsonDeserializer<?>>> entryList = new ArrayList<>();
        // Search and add type adapters for usage in serialization/deserialization of values to the gson
        Registry.getRegistries().forEach((type, registry) -> {
            // skip anything that does not support components
            if (DataAccess.GENERATOR_TYPES.containsKey(type)) {
                registry.clearDataTypes();
                for (Class<?> implementationType : registry.getTypes()) {
                    if (implementationType.isAnnotationPresent(com.azortis.orbis.pack.data.Component.class)) {
                        Class<?> accessClass = implementationType
                                .getAnnotation(com.azortis.orbis.pack.data.Component.class).value();
                        try {
                            // Suppress the warning for unchecked cast of constructor, we checked it in the if statement.
                            @SuppressWarnings("unchecked")
                            // Get the constructor with parameters (String, DataAccess)
                            Constructor<? extends ComponentAccess> constructor =
                                    (Constructor<? extends ComponentAccess>)
                                            accessClass.getConstructor(String.class, DataAccess.class);

                            // Get the component access class, but due to generics we must cast the access type to
                            // the instance to get the correct class and then upcast it to component access object.
                            ComponentAccess componentAccess = (ComponentAccess)
                                    (accessClass.cast(constructor.newInstance(null, null)));

                            // Add all adapters to the list
                            entryList.addAll(componentAccess.adapters().entrySet());

                            // Register all data types
                            registry.registerDataTypes(implementationType, componentAccess.dataTypes());
                        } catch (NoSuchMethodException e) {
                            logger.error("Class " + accessClass.getTypeName() +
                                    " does not implement the public constructor (String.class, DataAccess.class) " +
                                    "and its adapters cannot be retrieved");
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            logger.error("Unable to access class " + accessClass.getTypeName() +
                                    " for adapter retrieval");
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            logger.error("Class " + accessClass.getTypeName() + " threw an error during" +
                                    " initialization for adapter retrieval");
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            logger.error("Class " + accessClass.getTypeName() + " is abstract and adapters " +
                                    "cannot be retrieved.");
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            logger.error("Class " + accessClass.getTypeName() + " could not be accessed " +
                                    "during adapter retrieval.");
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
        return entryList;
    }

    public static @NotNull Component getPrefixComponent() {
        return MiniMessage.miniMessage().deserialize("<dark_gray>[<green>Orbis<dark_gray>]");
    }

    /**
     * Initialize the Orbis commands.
     *
     * @param platformCommandManager The delegating platform command manager.
     */
    @API(status = API.Status.INTERNAL, since = "0.3-Alpha", consumers = {"com.azortis.orbis.paper"})
    public static void loadCommands(CommandManager<CommandSender> platformCommandManager) {
        if (initialized && commandManager == null) {
            commandManager = platformCommandManager;
            AnnotationParser<CommandSender> annotationParser = new AnnotationParser<>(commandManager,
                    CommandSender.class, parameters -> SimpleCommandMeta.empty());

            try {
                annotationParser.parseContainers();
            } catch (Exception e) {
                logger.error("Failed to parse command containers");
                e.printStackTrace();
            }
        }
    }

    @API(status = API.Status.STABLE, since = "0.3-Alpha")
    public static Platform getPlatform() {
        return platform;
    }

    @API(status = API.Status.EXPERIMENTAL, since = "0.3-Alpha")
    public static Settings getSettings() {
        return settings;
    }

    @API(status = API.Status.EXPERIMENTAL, since = "0.3-Alpha")
    public static void reloadSettings() {
        try {
            settings = gson.fromJson(new FileReader(settingsFile), platform.settingsClass());
        } catch (FileNotFoundException e) {
            logger.error("Settings file not found, creating default one...");
            settings = platform.defaultSettings();
            saveSettings();
        }
    }

    @API(status = API.Status.EXPERIMENTAL, since = "0.3-Alpha")
    public static void saveSettings() {
        try {
            if (!settingsFile.exists() && !settingsFile.createNewFile()) {
                Orbis.getLogger().error("Failed to create settings file!");
                return;
            }
            final String json = gson.toJson(settings);
            Files.writeString(settingsFile.toPath(), json, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            Orbis.getLogger().error("Failed to save settings file!");
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    @API(status = API.Status.EXPERIMENTAL, since = "0.3-Alpha")
    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    @API(status = API.Status.STABLE, since = "0.3-Alpha")
    public static Gson getGson() {
        return gson;
    }

    @API(status = API.Status.STABLE, since = "0.3-Alpha")
    public static PackManager getPackManager() {
        return packManager;
    }

    @API(status = API.Status.STABLE, since = "0.3-Alpha")
    public static ProjectManager getProjectManager() {
        return projectManager;
    }
}
