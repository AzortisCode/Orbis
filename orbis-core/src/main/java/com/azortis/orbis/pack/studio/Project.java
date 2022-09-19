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
import com.google.gson.JsonObject;
import org.apache.commons.lang3.SystemUtils;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * A representation of a studio project environment, manages all things from Schema generation and
 * mapping the schema to the right files, and forces registry schema's to regenerate when a new config data file
 * has been added, to make sure it can be referenced in other files.
 */
public final class Project {

    private final String name;
    private final File directory;
    private boolean closed = false;

    private final WorkspaceConfig workspaceConfig;
    private final File settingsDir;
    private final StudioWorld studioWorld;

    Project(String name, File directory) throws IOException {
        this.name = name;
        this.directory = directory;

        // Initialize code-workspace config for VSCode completions
        File workspaceFile = new File(directory, name + ".code-workspace");
        if (workspaceFile.exists()) {
            JsonObject existingWorkspace = Orbis.getGson().fromJson(new FileReader(workspaceFile), JsonObject.class);
            this.workspaceConfig = new WorkspaceConfig(existingWorkspace.getAsJsonObject("settings"), workspaceFile);
        } else {
            this.workspaceConfig = new WorkspaceConfig(WorkspaceConfig.DEFAULT_SETTINGS, workspaceFile);
            this.workspaceConfig.save();
        }

        this.settingsDir = new File(directory + "/.orbis/");
        if (!settingsDir.exists() && !settingsDir.mkdirs())
            Orbis.getLogger().error("Failed to create /.orbis/ settings directory for {}", name);

        // Create a studio world, so we have a data access point.
        this.studioWorld = Orbis.getPlatform().createStudioWorld(this);

        // Read pack.json in order to get the Dimension file name, if none is specified the project is deemed invalid,
        // and so we cannot load a Dimension tree yet, this means Component schema's cannot be pointed yet.
        this.studioWorld.initialize();


        // Open VSCode environment if the user has it enabled.
        if (Orbis.getSettings().studio().openVSCode() && SystemUtils.IS_OS_WINDOWS) {
            Orbis.getLogger().info("Opening VSCode. You may see output from VSCode, just ignore it.");
            Desktop.getDesktop().open(workspaceFile);
        }
    }

    public String name() {
        return name;
    }

    public File directory() {
        return directory;
    }

    public boolean isClosed() {
        return closed;
    }

    public WorkspaceConfig workspaceConfig() {
        return workspaceConfig;
    }

    public File settingsDir() {
        return settingsDir;
    }

    public StudioWorld studioWorld() {
        return studioWorld;
    }

    boolean close() {
        if (!closed) {
            Orbis.getLogger().info("Closing project {}, clearing viewers...", name);
            studioWorld.clearViewers();
            Orbis.getLogger().info("Unloading studio world...");
            if (studioWorld.unload()) {
                Orbis.getLogger().info("Successfully closed project!");
                closed = true;
                return true;
            }
        }
        return false;
    }

}
