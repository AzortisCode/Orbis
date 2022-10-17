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

package com.azortis.orbis.item;

import com.azortis.orbis.Orbis;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

@Deprecated
public final class LegacyItemRegistry {

    private static final HashMap<String, LegacyItem> KEY_ITEM_MAP = new HashMap<>();
    private static final HashMap<String, LegacyEnchantment> KEY_ENCHANTMENT_MAP = new HashMap<>();
    private static final String ITEMS_DATA = "items.json";
    private static volatile boolean loaded = false;

    public static void init() {
        try {
            if (!loaded) {
                File itemsDataFile = Orbis.getDataFile(ITEMS_DATA);
                assert itemsDataFile != null;
                final JsonObject data = Orbis.getGson().fromJson(new FileReader(itemsDataFile), JsonObject.class);
                loadItems(data);
                loaded = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean containsItemKey(final String key) {
        final String id = key.indexOf(':') == -1 ? "minecraft:" + key : key;
        return KEY_ITEM_MAP.containsKey(id);
    }

    public static boolean containsEnchantmentKey(final String key) {
        final String id = key.indexOf(':') == -1 ? "minecraft:" + key : key;
        return KEY_ENCHANTMENT_MAP.containsKey(id);
    }

    public static LegacyItem fromItemKey(@NotNull String key) {
        final String id = key.indexOf(':') == -1 ? "minecraft:" + key : key;
        return KEY_ITEM_MAP.get(id);
    }

    public static LegacyEnchantment fromEnchantmentKey(@NotNull String key) {
        final String id = key.indexOf(':') == -1 ? "minecraft:" + key : key;
        return KEY_ENCHANTMENT_MAP.get(id);
    }

    public static boolean containsItemKey(@NotNull Key key) {
        return KEY_ITEM_MAP.containsKey(key.asString());
    }

    public static boolean containsEnchantmentKey(@NotNull Key key) {
        return KEY_ENCHANTMENT_MAP.containsKey(key.asString());
    }

    public static LegacyItem fromItemKey(@NotNull Key key) {
        return KEY_ITEM_MAP.get(key.asString());
    }

    public static LegacyEnchantment fromEnchantmentKey(@NotNull Key key) {
        return KEY_ENCHANTMENT_MAP.get(key.asString());
    }

    public static boolean isLoaded() {
        return loaded;
    }

    @SuppressWarnings("PatternValidation")
    private static void loadItems(final JsonObject data) {
        data.entrySet().forEach(entry -> {
            final Key key = Key.key(entry.getKey());
            final JsonObject itemData = entry.getValue().getAsJsonObject();
            final int id = itemData.get("id").getAsInt();
            final int maxStackSize = itemData.get("maxStackSize").getAsInt();
            final int maxDamage = itemData.get("maxDamage").getAsInt();

            if (!itemData.has("blockId")) {
                KEY_ITEM_MAP.put(key.asString(), new LegacyItem(key, id, maxStackSize, maxDamage, null));
            } else {
                final Key blockKey = Key.key(itemData.get("blockId").getAsString());
                KEY_ITEM_MAP.put(key.asString(), new LegacyItem(key, id, maxStackSize, maxDamage, blockKey));
            }
        });
    }

    @SuppressWarnings("PatternValidation")
    private static void loadEnchantments(final JsonObject data) {
        data.entrySet().forEach(entry -> {
            final Key key = Key.key(entry.getKey());
            final JsonObject enchantmentData = entry.getValue().getAsJsonObject();
            final int id = enchantmentData.get("id").getAsInt();
            final int maxLevel = enchantmentData.get("maxLevel").getAsInt();
            final int minLevel = enchantmentData.get("minLevel").getAsInt();

            KEY_ENCHANTMENT_MAP.put(key.asString(), new LegacyEnchantment(key, id, maxLevel, minLevel));
        });

        data.entrySet().forEach(entry -> {
            final Key key = Key.key(entry.getKey());
            final JsonArray incompatibleEnchantments = entry.getValue().getAsJsonObject()
                    .get("incompatibleEnchantments").getAsJsonArray();

            ImmutableSet.Builder<LegacyEnchantment> builder = ImmutableSet.builder();
            for (JsonElement element : incompatibleEnchantments) {
                builder.add(fromEnchantmentKey(element.getAsString()));
            }
            fromEnchantmentKey(key).setIncompatibleEnchantments(builder.build());
        });
    }

}
