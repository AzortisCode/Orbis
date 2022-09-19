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

import cloud.commandframework.CommandManager;
import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.meta.SimpleCommandMeta;
import com.azortis.orbis.block.BlockRegistry;
import com.azortis.orbis.block.ConfiguredBlock;
import com.azortis.orbis.block.property.PropertyRegistry;
import com.azortis.orbis.command.CommandSender;
import com.azortis.orbis.generator.biome.Distributor;
import com.azortis.orbis.generator.noise.NoiseGenerator;
import com.azortis.orbis.generator.terrain.Terrain;
import com.azortis.orbis.item.ItemFactory;
import com.azortis.orbis.item.ItemRegistry;
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
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Dependency(group = "com.google.guava", artifact = "guava", version = "31.0.1-jre")
@Dependency(group = "net.lingala.zip4j", artifact = "zip4j", version = "2.10.0")
@Dependency(group = "org.projectlombok", artifact = "lombok", version = "1.18.24")
@Dependency(group = "cloud.commandframework", artifact = "cloud-core", version = "1.7.1")
@Dependency(group = "cloud.commandframework", artifact = "cloud-annotations", version = "1.7.1")
@Dependency(group = "cloud.commandframework", artifact = "cloud-services", version = "1.7.1")
@Dependency(group = "io.leangen.geantyref", artifact = "geantyref", version = "1.3.13")
@Dependency(group = "org.apache.commons", artifact = "commons-lang3", version = "3.12.0")
public final class Orbis {

    public static final String VERSION = "0.2-ALPHA";
    public static final String SETTINGS_VERSION = "1";
    public static final String MC_VERSION = "1_19_2";
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
     * Initialize Orbis, internal use only!
     *
     * @param platform The platform instance.
     */
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
                    .registerTypeAdapter(Terrain.class, new TypeAdapter<>(Terrain.class))
                    .registerTypeAdapter(Distributor.class, new TypeAdapter<>(Distributor.class))
                    .registerTypeAdapter(NoiseGenerator.class, new TypeAdapter<>(NoiseGenerator.class));

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

            // Load minecraft data into memory
            PropertyRegistry.init();
            BlockRegistry.init();
            ItemRegistry.init();

            // Init message deserializer
            miniMessage = MiniMessage.builder()
                    .tags(TagResolver.builder()
                            .resolver(StandardTags.defaults())
                            .resolver(TagResolver.resolver("prefix", Tag.selfClosingInserting(Orbis::getPrefixComponent))).build())
                    .build();

            // Load managers
            packManager = new PackManager(platform.directory());
            projectManager = new ProjectManager(new File(platform.directory() + "/projects/"));
        }
    }

    /**
     * Shutdown Orbis, Internal use only!
     */
    public static void shutdown() {
        if (initialized) {
            projectManager.closeProject();
        }
    }

    private static List<Map.Entry<Class<?>, JsonDeserializer<?>>> getRegistryAdapters() {
        List<Map.Entry<Class<?>, JsonDeserializer<?>>> entryList = new ArrayList<>();
        // Search and add type adapters for usage in serialization/deserialization of values to the gson
        Registry.getImmutableRegistry().forEach((access, registry) -> {
            // skip anything that is not a component
            if (access.isAssignableFrom(ComponentAccess.class)) {
                try {
                    // Suppress the warning for unchecked cast of constructor, we checked it in the if statement.
                    @SuppressWarnings("unchecked")
                    // Get the constructor with parameters (String, DataAccess)
                    Constructor<? extends ComponentAccess> constructor = (Constructor<? extends ComponentAccess>) access.getConstructor(String.class, DataAccess.class);

                    // Get the component access class, but due to generics we must cast the access type to the instance to get the correct class
                    // and then upcast it to component access object.
                    ComponentAccess componentAccess = (ComponentAccess) (access.cast(constructor.newInstance(null, null)));

                    // Add all adapters to the list
                    entryList.addAll(componentAccess.adapters().entrySet());
                } catch (NoSuchMethodException e) {
                    logger.error("Class " + access.getTypeName() + " does not implement the public constructor (String.class, DataAccess.class) and its adapters cannot be retrieved");
                    e.printStackTrace();
                } catch (SecurityException e) {
                    logger.error("Unable to access class " + access.getTypeName() + " for adapter retrieval");
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    logger.error("Class " + access.getTypeName() + " threw an error during initialization for adapter retrieval");
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    logger.error("Class " + access.getTypeName() + " is abstract and adapters cannot be retrieved.");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    logger.error("Class " + access.getTypeName() + " could not be accessed during adapter retrieval.");
                    e.printStackTrace();
                }

            }
        });
        return entryList;
    }

    public static @NotNull Component getPrefixComponent() {
        String text = "<dark_gray>[<green>Orbis<dark_gray>]";
        return MiniMessage.miniMessage().deserialize(text);
    }

    /**
     * Initialize the Orbis command, internal use only!
     *
     * @param platformCommandManager The delegating platform command manager.
     */
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

    public static File getDataFile(String dateFileName) {
        if (initialized) {
            try {
                File dataFolder = new File(platform.directory() + "/data/");
                if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                    throw new IllegalStateException("Failed to create data directory");
                } else {
                    dateFileName = MC_VERSION + "_" + dateFileName;
                    File dataFile = new File(dataFolder, dateFileName);
                    if (!dataFile.exists()) {
                        URL downloadUrl = new URL(DOWNLOAD_URL + dateFileName);
                        FileUtils.copyURLToFile(downloadUrl, dataFile);
                    }
                    return dataFile;
                }
            } catch (IOException ex) {
                logger.error("Failed to download {}", dateFileName);
            }
        } else {
            throw new IllegalStateException("Orbis hasn't yet been initialized!");
        }
        return null;
    }

    @NotNull
    public static ItemFactory getItemFactory() {
        return platform.itemFactory();
    }

    public static Platform getPlatform() {
        return platform;
    }

    public static Settings getSettings() {
        return settings;
    }

    public static void reloadSettings() {
        try {
            settings = gson.fromJson(new FileReader(settingsFile), platform.settingsClass());
        } catch (FileNotFoundException e) {
            logger.error("Settings file not found, creating default one...");
            settings = platform.defaultSettings();
            saveSettings();
        }
    }

    public static void saveSettings() {
        try {
            if (!settingsFile.exists() && !settingsFile.createNewFile()) {
                Orbis.getLogger().error("Failed to create settings file!");
            } else {
                final String json = gson.toJson(settings);
                Files.writeString(settingsFile.toPath(), json, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (IOException e) {
            Orbis.getLogger().error("Failed to save settings file!");
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public static Gson getGson() {
        return gson;
    }

    public static PackManager getPackManager() {
        return packManager;
    }

    public static ProjectManager getProjectManager() {
        return projectManager;
    }
}
