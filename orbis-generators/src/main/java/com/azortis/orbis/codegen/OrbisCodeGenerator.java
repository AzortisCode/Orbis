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

package com.azortis.orbis.codegen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.javapoet.JavaFile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public abstract class OrbisCodeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrbisCodeGenerator.class);
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    protected final InputStream inputStream;
    protected final File outputFolder;

    public OrbisCodeGenerator(InputStream inputStream, File outputFolder) {
        this.inputStream = inputStream;
        this.outputFolder = outputFolder;
    }

    public abstract void generate();

    protected void writeFiles(@NotNull List<JavaFile> files){
        for (JavaFile javaFile : files){
            try {
                javaFile.writeTo(this.outputFolder);
            } catch (IOException ex){
                LOGGER.error("An error occurred while trying to write source code to the filesystem.");
            }
        }
    }

    private String toConstant(String namespaceId){
        return namespaceId.replace("minecraft:", "").toUpperCase(Locale.ENGLISH);
    }

}
