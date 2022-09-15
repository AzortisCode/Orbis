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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Class representation of VSCode its *.code-workspace config file.
 */
public final class WorkspaceConfig {

    public static final JsonObject FOLDER_PATH = new JsonObject();
    public static final JsonArray FOLDERS_ARRAY = new JsonArray();
    public static final JsonObject DEFAULT_SETTINGS = new JsonObject();

    static {
        FOLDER_PATH.addProperty("path", ".");
        FOLDERS_ARRAY.add(FOLDER_PATH);

        // Apply the default editor settings.
        DEFAULT_SETTINGS.addProperty("workbench.colorTheme", "Monokai");
        DEFAULT_SETTINGS.addProperty("workbench.preferredDarkColorTheme", "Solarized Dark");
        DEFAULT_SETTINGS.addProperty("workbench.tips.enabled", false);
        DEFAULT_SETTINGS.addProperty("workbench.tree.indent", 24);
        DEFAULT_SETTINGS.addProperty("files.autoSave", "onFocusChange");

        // Json Language settings
        JsonObject jsonSettings = new JsonObject();
        jsonSettings.addProperty("editor.autoIndent", "brackets");
        jsonSettings.addProperty("editor.acceptSuggestionOnEnter", "smart");
        jsonSettings.addProperty("editor.cursorSmoothCaretAnimation", true);
        jsonSettings.addProperty("editor.dragAndDrop", false);
        jsonSettings.addProperty("files.trimTrailingWhitespace", true);
        jsonSettings.addProperty("diffEditor.ignoreTrimWhitespace", true);
        jsonSettings.addProperty("files.trimFinalNewlines", true);
        jsonSettings.addProperty("editor.suggest.showKeywords", false);
        jsonSettings.addProperty("editor.suggest.showSnippets", false);
        jsonSettings.addProperty("editor.suggest.showWords", false);
        JsonObject quickSuggestions = new JsonObject();
        quickSuggestions.addProperty("strings", true);
        jsonSettings.add("editor.quickSuggestions", quickSuggestions);
        jsonSettings.addProperty("editor.suggest.insertMode", "replace");
        DEFAULT_SETTINGS.add("[json]", jsonSettings);
        DEFAULT_SETTINGS.addProperty("json.maxItemsComputed", 30000);
    }

    private final JsonArray folders = FOLDERS_ARRAY;
    private final JsonObject settings;
    private transient final File workspaceFile;

    public WorkspaceConfig(@NotNull JsonObject settings, @NotNull File workspaceFile) {
        this.settings = settings.deepCopy();
        this.workspaceFile = workspaceFile;

        // Clear up the existing schema's since we're going to regenerate this.
        this.settings.remove("json.schemas");
    }

    public void save() throws IOException {
        if (!workspaceFile.exists() && !workspaceFile.createNewFile()) {
            Orbis.getLogger().error("Failed to create new *.code-workspace file!");
            return;
        }
        final String json = Orbis.getGson().toJson(this);
        Files.writeString(workspaceFile.toPath(), json, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

}
