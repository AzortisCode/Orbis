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

package com.azortis.orbis.codegen.block;

import com.azortis.orbis.block.Block;
import com.azortis.orbis.codegen.OrbisCodeGenerator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.javapoet.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class BlocksGenerator extends OrbisCodeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlocksGenerator.class);

    public BlocksGenerator(InputStream inputStream, File outputFolder) {
        super(inputStream, outputFolder);
    }

    @Override
    public void generate() {
        if (this.inputStream == null) {
            LOGGER.error("Failed to find blocks data file");
            return;
        }
        if (!outputFolder.exists() && !outputFolder.mkdirs()) {
            LOGGER.error("Output folder doesn't exist and failed to create it!");
            return;
        }

        JsonObject blocks = GSON.fromJson(new InputStreamReader(inputStream), JsonObject.class);

        ClassName block = ClassName.get(Block.class);

        ClassName blocksClassName = ClassName.get("com.azortis.orbis.block", "Blocks");
        AnnotationSpec suppressAll =
                AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "all").build();
        TypeSpec.Builder blocksClass = TypeSpec.classBuilder(blocksClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(suppressAll)
                .addJavadoc("This class has been autogenerated. Should not be called before Orbis is initialized!");

        for (Map.Entry<String, JsonElement> entry : blocks.entrySet()) {
            final String key = entry.getKey();
            final String name = entry.getValue().getAsJsonObject().get("mojangName").getAsString();

            blocksClass.addField(
                    FieldSpec.builder(block, name)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .addAnnotation(NotNull.class)
                            .initializer("$T.fromKey($S)", block, key)
                            .build());
        }
        JavaFile blocksFile = JavaFile.builder("com.azortis.orbis.block", blocksClass.build()).build();
        writeFiles(List.of(blocksFile));
    }
}
