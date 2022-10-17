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

package com.azortis.orbis.codegen;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.codegen.block.BlocksGenerator;
import com.azortis.orbis.codegen.block.PropertiesGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class Generators {
    private static final Logger LOGGER = LoggerFactory.getLogger(Generators.class);
    private static final String BASE_ARTIC_DATA_URL = "https://raw.githubusercontent.com/Articdive/ArticData/" +
            Orbis.MC_VERSION.replace("_", ".") + "/";

    public static void main(String[] args) {
        StringBuilder outputBuilder = new StringBuilder();
        for (String arg : args) {
            if (arg.startsWith("$") || arg.endsWith("$")) {
                outputBuilder.append(arg.replace("$", ""));
            }
            outputBuilder.append(" ");
        }
        final File outputFolder = new File(outputBuilder.toString().trim());
        new PropertiesGenerator(getInputStream("block_properties.json"), outputFolder).generate();
        new BlocksGenerator(getInputStream("blocks.json"), outputFolder).generate();
        //new ItemsGenerator(getInputStream("items.json"), outputFolder).generate();
        //new EnchantmentsGenerator(getInputStream("enchantments.json"), outputFolder).generate();
    }

    private static InputStream getInputStream(String fileName) {
        try {
            return new URL(BASE_ARTIC_DATA_URL + Orbis.MC_VERSION + "_" + fileName).openStream();
        } catch (IOException ex) {
            LOGGER.error("Failed to create InputStream for " + fileName);
        }
        return null;
    }

}
