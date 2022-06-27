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
import com.azortis.orbis.pack.adapter.*;
import com.azortis.orbis.util.maven.Dependency;
import com.azortis.orbis.util.maven.DependencyLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.io.IOException;
import java.net.URL;

@Dependency(group = "com.google.guava", artifact = "guava", version = "31.0.1-jre")
@Dependency(group = "net.lingala.zip4j", artifact = "zip4j", version = "2.7.0")
@Dependency(group = "org.projectlombok", artifact = "lombok", version = "1.18.20")
@Dependency(group = "cloud.commandframework", artifact = "cloud-core", version = "1.7.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-annotations", version = "1.7.0")
@Dependency(group = "cloud.commandframework", artifact = "cloud-services", version = "1.7.0")
@Dependency(group = "io.leangen.geantyref", artifact = "geantyref", version = "1.3.13")
public final class Orbis {

    public static final String VERSION = "0.2-ALPHA";
    public static final String SETTINGS_VERSION = "1";
    public static final String MC_VERSION = "1_19";
    private static final String DOWNLOAD_URL = "https://raw.githubusercontent.com/Articdive/ArticData/" +
            MC_VERSION.replace("_", ".") + "/";

    private static boolean initialized = false;
    private static Platform platform = null;
    private static CommandManager<CommandSender> commandManager = null;
    private static Logger logger;
    private static Gson gson;
    private static MiniMessage miniMessage;

    // Managers
    private static PackManager packManager;

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

            if (platform.mainClass() != null) {
                logger.info("Loading core libraries...");
                DependencyLoader.loadAll(Orbis.class);
            }

            // Register the type adapters to use in the serialization/deserialization of settings in packs.
            gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting()
                    .registerTypeAdapter(Key.class, new KeyAdapter())
                    .registerTypeAdapter(ConfiguredBlock.class, new BlockAdapter())
                    .registerTypeAdapter(Terrain.class, new TerrainAdapter())
                    .registerTypeAdapter(Distributor.class, new DistributorAdapter())
                    .registerTypeAdapter(NoiseGenerator.class, new NoiseGeneratorAdapter()).create();

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
        }
    }

    private static @NotNull Component getPrefixComponent() {
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
}
