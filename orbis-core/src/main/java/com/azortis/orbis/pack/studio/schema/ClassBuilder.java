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

package com.azortis.orbis.pack.studio.schema;

import com.azortis.orbis.Orbis;
import com.azortis.orbis.Registry;
import com.azortis.orbis.pack.data.Component;
import com.azortis.orbis.pack.data.DataAccess;
import com.azortis.orbis.pack.studio.Project;
import com.azortis.orbis.pack.studio.StudioDataAccess;
import com.azortis.orbis.pack.studio.annotations.*;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

public final class ClassBuilder extends SchemaBuilder {

    private final Class<?> type;
    private final boolean isGenerator;
    private final boolean isTyped;
    private final String componentName;

    ClassBuilder(@NotNull Project project, @NotNull StudioDataAccess data, @NotNull SchemaManager schemaManager,
                 @NotNull File schemaFile, @NotNull Class<?> type, @Nullable String name) {
        super(project, data, schemaManager, schemaFile);
        this.type = type;
        this.isGenerator = DataAccess.GENERATOR_TYPES.containsKey(type);
        this.isTyped = type.isAnnotationPresent(Typed.class);
        if (data.isComponentType(type)) {
            Preconditions.checkNotNull(name, "Type is a component data type, but no instance name has been provided");
            this.componentName = name;
        } else {
            this.componentName = null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected @NotNull JsonObject generateSchema() {
        JsonObject schema = new JsonObject();

        // Basic root schema properties
        schema.addProperty("$schema", "http://json-schema.org/draft-07/schema");

        // Definitions are shared down the chain, as they only are created once
        JsonObject definitions = new JsonObject();
        buildProperties(schema, definitions, type, componentName);

        // If a schema is typed, we get all the implementations in the registry
        // and build it all into one single schema using an allOf
        if (isTyped) {
            if (!schema.get("properties").getAsJsonObject().has("type")) {
                Orbis.getLogger().error("Class {} is marked as @Typed, but doesn't have a \"type\" field defined!",
                        type.getSimpleName());
            } else {
                JsonArray allOf = new JsonArray();

                Map<Key, Class<?>> typeMap = (Map<Key, Class<?>>) Registry.getRegistry(type).getTypeMap();
                for (Map.Entry<Key, Class<?>> entry : typeMap.entrySet()) {
                    JsonObject ifThen = new JsonObject();

                    // Build the if object, where a type key is matched.
                    JsonObject ifType = new JsonObject();
                    JsonObject ifPropertiesType = new JsonObject();
                    JsonObject typeProperty = new JsonObject();
                    typeProperty.addProperty("const", entry.getKey().asString());
                    ifPropertiesType.add("type", typeProperty);
                    ifType.add("properties", ifPropertiesType);
                    ifThen.add("if", ifType);

                    // Build the then object
                    JsonObject then = new JsonObject();
                    if (isGenerator && entry.getValue().isAnnotationPresent(Component.class)) {
                        JsonArray thenAllOf = new JsonArray();

                        for (String name : data.getComponentNames(entry.getValue())) {
                            JsonObject ifThenName = new JsonObject();

                            // Build if condition for this name
                            JsonObject ifName = new JsonObject();
                            JsonObject ifPropertiesName = new JsonObject();
                            JsonObject nameProperty = new JsonObject();
                            nameProperty.addProperty("const", name);
                            ifPropertiesName.add("name", nameProperty);
                            ifName.add("properties", ifPropertiesName);
                            ifThenName.add("if", ifName);

                            // Build the schema for this type with this component name instance
                            JsonObject thenName = new JsonObject();
                            buildProperties(thenName, definitions, entry.getValue(), name);
                            ifThenName.add("then", thenName);

                            // Add the matcher and corresponding sub-schema to the allOf array
                            thenAllOf.add(ifThenName);
                        }
                        then.add("allOf", thenAllOf);

                    } else buildProperties(then, definitions, entry.getValue(), componentName);

                    // Add the matcher to the allOf object
                    ifThen.add("then", then);
                    allOf.add(ifThen);
                }
            }
        }

        // Make sure nothing evaluated gets accepted in the data files.
        schema.addProperty("unevaluatedProperties", false);
        if (definitions.size() > 0) schema.add("$defs", definitions);
        return schema;
    }

    private void buildProperties(@NotNull JsonObject root, @NotNull JsonObject definitions,
                                 @NotNull Class<?> type, @Nullable String name) {
        root.addProperty("description", getTypeDescription(type));
        root.addProperty("type", "object");

        JsonObject properties = new JsonObject();
        JsonArray required = new JsonArray();

        for (Field field : type.getDeclaredFields()) {
            if (isProperty(field)) {
                String propertyName = field.getName();
                if (field.isAnnotationPresent(SerializedName.class)) {
                    propertyName = field.getAnnotation(SerializedName.class).value();
                }

                JsonObject property = buildProperty(field, definitions, name);
                if (property.has("!required")) {
                    if (root.has("!definition") && type.isAnnotationPresent(SupportAnonymous.class)) {
                        String anonymousName = type.getAnnotation(SupportAnonymous.class).value();
                        if (!propertyName.equals(anonymousName)) {
                            required.add(propertyName);
                        }
                        property.remove("!required");
                    } else {
                        required.add(propertyName);
                        property.remove("!required");
                    }
                }
                properties.add(propertyName, property);
            }
        }
        root.add("properties", properties);
        if (required.size() > 0) root.add("required", required);
    }

    private JsonObject buildProperty(@NotNull Field field, @NotNull JsonObject definitions, @Nullable String name) {
        Class<?> type = field.getType();
        JsonObject property = new JsonObject();
        String schemaType = getSchemaType(type);
        property.addProperty("type", schemaType);
        StringBuilder description = new StringBuilder();
        description.append(getFieldDescription(field));

        rootSwitch:
        {
            switch (schemaType) {
                case "integer" -> {
                    long min = 0L;
                    long max = 0L;

                    if (type.equals(byte.class) || type.equals(Byte.class)) {
                        min = getIntegerMin(field, Byte.MIN_VALUE);
                        max = getIntegerMax(field, Byte.MAX_VALUE);
                    } else if (type.equals(short.class) || type.equals(Short.class)) {
                        min = getIntegerMin(field, Short.MIN_VALUE);
                        max = getIntegerMax(field, Short.MAX_VALUE);
                    } else if (type.equals(int.class) || type.equals(Integer.class)) {
                        min = getIntegerMin(field, Integer.MIN_VALUE);
                        max = getIntegerMax(field, Integer.MAX_VALUE);
                    } else if (type.equals(long.class) || type.equals(Long.class)) {
                        min = getIntegerMin(field, Long.MIN_VALUE);
                        max = getIntegerMax(field, Long.MAX_VALUE);
                    }

                    property.addProperty("minimum", min);
                    property.addProperty("maximum", max);
                    description.append("\nMinimum value: ").append(min).append("\n Maximum value: ").append(max);
                }
                case "number" -> {
                    double min = 0d;
                    double max = 0d;

                    if (type.equals(float.class) || type.equals(Float.class)) {
                        min = getNumberMin(field, Float.MIN_VALUE);
                        max = getNumberMax(field, Float.MAX_VALUE);
                    } else if (type.equals(double.class) || type.equals(Double.class)) {
                        min = getNumberMin(field, Double.MIN_VALUE);
                        max = getNumberMax(field, Double.MAX_VALUE);
                    }

                    property.addProperty("minimum", min);
                    property.addProperty("maximum", max);
                    description.append("\nMinimum value: ").append(min).append(".\n Maximum value: ")
                            .append(max).append(".");
                }
                case "string" -> {
                    if (type.isEnum()) {
                        // TODO add enum global-definition support + description support.
                        JsonArray enums = new JsonArray();
                        for (Object constant : type.getEnumConstants()) {
                            String enumName = ((Enum<?>) constant).name();
                            enums.add(enumName);
                        }
                        property.add("enum", enums);
                        description.append("Must be a valid ").append(type.getSimpleName()).append(".");
                    } else if (field.isAnnotationPresent(Entries.class)) {
                        Class<?> entryType = field.getAnnotation(Entries.class).value();
                        property.remove("type");
                        if (data.isComponentType(entryType)) {
                            if (name == null)
                                throw new NullPointerException("Field type is a component type, but no name is passed!");
                            property.addProperty("$ref", getSchemaReference(schemaManager
                                    .getSchema(entryType, name)));
                        } else {
                            property.addProperty("$ref", getSchemaReference(schemaManager.getSchema(entryType)));
                        }
                        description.append("Must be a valid ").append(entryType.getSimpleName()).append(".");
                    } else if (field.getName().equals("type") && field.getDeclaringClass()
                            .isAnnotationPresent(Typed.class) && type.equals(Key.class)) {
                        Class<?> typedClass = field.getDeclaringClass();
                        JsonArray enums = new JsonArray();
                        Registry.getRegistry(typedClass).getTypeMap().keySet().forEach(key -> enums.add(key.asString()));
                        property.add("enum", enums);
                        description.append("\nMust be a valid ").append(typedClass.getSimpleName())
                                .append(" implementation.");
                    } else {
                        if (field.isAnnotationPresent(Min.class)) {
                            long minLength = field.getAnnotation(Min.class).value();
                            if (minLength > 0) {
                                property.addProperty("minLength", minLength);
                                description.append("\nMinimum length: ").append(minLength).append(".");
                            }
                        }

                        if (field.isAnnotationPresent(Max.class)) {
                            long maxLength = field.getAnnotation(Max.class).value();
                            if (maxLength > 0) {
                                property.addProperty("maxLength", maxLength);
                                description.append("\nMaximum length: ").append(maxLength).append(".");
                            }
                        }
                    }
                }
                case "array" -> {
                    // TODO Add Array support + Min & Max support for Strings/numbers/integers in the array
                    if (field.isAnnotationPresent(CollectionType.class)) {
                        Class<?> collectionType = field.getAnnotation(CollectionType.class).value();
                        String collectionSchemaType = getSchemaType(collectionType);

                        JsonObject items = new JsonObject();
                        items.addProperty("type", collectionSchemaType);

                        switch (collectionSchemaType) {
                            case "string" -> {
                                if (collectionType.isEnum()) {
                                    // TODO add enum global-definition support + description support.
                                    JsonArray enums = new JsonArray();
                                    for (Object constant : collectionType.getEnumConstants()) {
                                        String enumName = ((Enum<?>) constant).name();
                                        enums.add(enumName);
                                    }
                                    items.add("enum", enums);
                                    description.append("Must be valid ").append(collectionType.getSimpleName())
                                            .append("'s.");
                                } else if (field.isAnnotationPresent(Entries.class)) {
                                    Class<?> entryType = field.getAnnotation(Entries.class).value();
                                    items.remove("type");
                                    if (data.isComponentType(entryType)) {
                                        if (name == null)
                                            throw new NullPointerException(
                                                    "Field type is a component type, but no name is passed!");
                                        items.addProperty("$ref", getSchemaReference(schemaManager
                                                .getSchema(entryType, name)));
                                    } else {
                                        items.addProperty("$ref", getSchemaReference(schemaManager.getSchema(entryType)));
                                    }
                                    description.append("Must be valid ").append(entryType.getSimpleName()).append("'s.");
                                }
                            }
                            case "object" -> {
                                if (collectionType.isAnnotationPresent(GlobalDefinition.class)) {
                                    items.addProperty("$ref", getSchemaReference(schemaManager
                                            .getSchema(collectionType)));
                                } else {
                                    String definitionName = collectionType.getCanonicalName();
                                    if (!definitions.has(definitionName)) {
                                        buildDefinition(definitionName, definitions, collectionType, name);
                                    }

                                    items.addProperty("$ref", "#/definitions/" + definitionName);
                                }
                            }
                            case "array" -> {
                                Orbis.getLogger().error("Field {} has an collection nested in a collection! Unsupported!",
                                        field.getName());
                                break rootSwitch;
                            }
                        }

                        // TODO maybe add support for Unique items if needed?
                        if (field.isAnnotationPresent(Min.class)) {
                            long minItems = field.getAnnotation(Min.class).value();
                            if (minItems > 0) {
                                property.addProperty("minItems", minItems);
                                description.append("\nMinimum amount of items: ").append(minItems).append(".");
                            }
                        }

                        if (field.isAnnotationPresent(Max.class)) {
                            long maxItems = field.getAnnotation(Max.class).value();
                            if (maxItems > 0) {
                                property.addProperty("maxItems", maxItems);
                                description.append("\nMaximum amount of items: ").append(maxItems).append(".");
                            }
                        }

                        property.add("items", items);
                    } else {
                        Orbis.getLogger().error("Field {} is a derivative of a Collection but doesn't " +
                                "have @CollectionType annotated", field.getName());
                    }
                }
                case "object" -> {
                    if (type.isAnnotationPresent(GlobalDefinition.class)) {
                        property.addProperty("$ref", getSchemaReference(schemaManager.getSchema(type)));
                    } else {
                        String definitionName = type.getCanonicalName();
                        if (!definitions.has(definitionName)) {
                            buildDefinition(definitionName, definitions, type, name);
                        }

                        property.addProperty("$ref", "#/definitions/" + definitionName);
                    }
                }
                default -> Orbis.getLogger().error("Invalid schema type {}", schemaType);
            }
        }
        property.addProperty("description", description.toString());
        if (field.isAnnotationPresent(Required.class)) property.addProperty("!required", true);
        return property;
    }

    @SuppressWarnings("unchecked")
    private void buildDefinition(@NotNull String definitionName, @NotNull JsonObject definitions,
                                 @NotNull Class<?> type, @Nullable String name) {
        JsonObject definition = new JsonObject();
        definitions.add(definitionName, definition); // To prevent recursive definitions, we add it first, then build
        definition.addProperty("!definition", true); // For @SupportAnonymous
        buildProperties(definition, definitions, type, name);
        if (type.isAnnotationPresent(Typed.class)) {
            if (!definition.get("properties").getAsJsonObject().has("type")) {
                Orbis.getLogger().error("Class {} is marked as @Typed, but doesn't have a \"type\" field defined!",
                        type.getSimpleName());
            } else {
                JsonArray allOf = new JsonArray();

                Map<Key, Class<?>> typeMap = (Map<Key, Class<?>>) Registry.getRegistry(type).getTypeMap();
                for (Map.Entry<Key, Class<?>> entry : typeMap.entrySet()) {
                    // Types using components cannot exist in a definition
                    if (!entry.getValue().isAnnotationPresent(Component.class)) {
                        JsonObject ifThen = new JsonObject();

                        // Build the if object, where a type key is matched.
                        JsonObject ifType = new JsonObject();
                        JsonObject ifPropertiesType = new JsonObject();
                        JsonObject typeProperty = new JsonObject();
                        typeProperty.addProperty("const", entry.getKey().asString());
                        ifPropertiesType.add("type", typeProperty);
                        ifType.add("properties", ifPropertiesType);
                        ifThen.add("if", ifType);

                        // Build the then object
                        JsonObject then = new JsonObject();
                        buildProperties(then, definitions, entry.getValue(), componentName);

                        // Add the matcher to the allOf object
                        ifThen.add("then", then);
                        allOf.add(ifThen);
                    }
                }
                definition.add("allOf", allOf);
            }
        }
    }

    private double getNumberMax(@NotNull Field field, double max) {
        if (field.isAnnotationPresent(Max.class)) {
            Max maxAnn = field.getAnnotation(Max.class);
            return Math.min(maxAnn.floating(), max);
        }
        return max;
    }

    private double getNumberMin(@NotNull Field field, double min) {
        if (field.isAnnotationPresent(Min.class)) {
            Min minAnn = field.getAnnotation(Min.class);
            return Math.max(minAnn.floating(), min);
        }
        return min;
    }

    private long getIntegerMax(@NotNull Field field, long max) {
        if (field.isAnnotationPresent(Max.class)) {
            Max maxAnn = field.getAnnotation(Max.class);
            return Math.min(maxAnn.value(), max);
        }
        return max;
    }

    private long getIntegerMin(@NotNull Field field, long min) {
        if (field.isAnnotationPresent(Min.class)) {
            Min minAnn = field.getAnnotation(Min.class);
            return Math.max(minAnn.value(), min);
        }
        return min;
    }

    private String getSchemaType(@NotNull Class<?> type) {
        if (type.equals(String.class) || type.isEnum() || type.equals(Key.class)) {
            return "string";
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return "boolean";
        } else if (type.equals(double.class) || type.equals(Double.class) || type.equals(float.class)
                || type.equals(Float.class)) {
            return "number";
        } else if (type.equals(byte.class) || type.equals(Byte.class) || type.equals(short.class)
                || type.equals(Short.class) || type.equals(int.class) || type.equals(Integer.class)
                || type.equals(long.class) || type.equals(Long.class)) {
            return "integer";
        } else if (type.isAssignableFrom(Collection.class)) {
            return "array";
        }
        return "object";
    }

    private String getTypeDescription(@NotNull Class<?> type) {
        if (type.isAnnotationPresent(Description.class)) {
            return type.getAnnotation(Description.class).value();
        }
        Orbis.getLogger().warn("{} Doesn't have a @Description defined!", type.getSimpleName());
        return "Description missing.";
    }

    private String getFieldDescription(@NotNull Field field) {
        if (field.isAnnotationPresent(Description.class)) {
            return field.getAnnotation(Description.class).value();
        }
        Orbis.getLogger().warn("{} Doesn't have a @Description defined!", field.getName());
        return "Description missing.";
    }

    private boolean isProperty(@NotNull Field field) {
        return !(Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers()) ||
                field.isAnnotationPresent(Ignore.class));
    }

}
