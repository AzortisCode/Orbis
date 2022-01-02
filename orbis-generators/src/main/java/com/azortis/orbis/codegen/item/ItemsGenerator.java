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

package com.azortis.orbis.codegen.item;

import com.azortis.orbis.codegen.OrbisCodeGenerator;
import com.azortis.orbis.item.Item;
import com.azortis.orbis.item.ItemRegistry;
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

public class ItemsGenerator extends OrbisCodeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsGenerator.class);

    public ItemsGenerator(InputStream inputStream, File outputFolder) {
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

        JsonObject items = GSON.fromJson(new InputStreamReader(inputStream), JsonObject.class);

        ClassName item = ClassName.get(Item.class);
        ClassName itemRegistry = ClassName.get(ItemRegistry.class);

        ClassName itemsClassName = ClassName.get("com.azortis.orbis.item", "Items");
        AnnotationSpec suppressAll =
                AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "$S", "all").build();
        TypeSpec.Builder itemsClass = TypeSpec.classBuilder(itemsClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(suppressAll)
                .addJavadoc("This class has been autogenerated. Should not be called before Orbis is initialized!");

        for (Map.Entry<String, JsonElement> entry : items.entrySet()) {
            final String key = entry.getKey();
            final String name = entry.getValue().getAsJsonObject().get("mojangName").getAsString();

            itemsClass.addField(
                    FieldSpec.builder(item, name)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .addAnnotation(NotNull.class)
                            .initializer("$T.fromItemKey($S)", itemRegistry, key)
                            .build());
        }
        JavaFile itemsFile = JavaFile.builder("com.azortis.orbis.item", itemsClass.build()).build();
        writeFiles(List.of(itemsFile));
    }
}