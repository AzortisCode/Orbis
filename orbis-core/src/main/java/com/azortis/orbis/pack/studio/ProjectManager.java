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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public final class ProjectManager {

    private final File projectsFolder;
    private final Set<String> projects = new HashSet<>();

    private Project activeProject = null;

    public ProjectManager(@NotNull File projectsFolder) {
        this.projectsFolder = projectsFolder;
        if (!projectsFolder.exists()) {
            if (!projectsFolder.mkdirs()) Orbis.getLogger().error("Failed to create projects folder!");
        } else {
            reloadProjects();
        }
    }

    private void reloadProjects() {
        projects.clear();
        File[] projectDirectories = projectsFolder.listFiles(File::isDirectory);
        if (projectDirectories != null) {
            for (File project : projectDirectories) {
                projects.add(project.getName());
            }
        }
    }

    /**
     * Returns a set of project names that are present in the projects' folder.
     *
     * @return An immutable view of project names.
     */
    public Set<String> getProjects() {
        return Set.copyOf(projects);
    }

    /**
     * Gets the current active project being worked on.
     *
     * @return The current {@link Project}, null if none is active.
     */
    @Nullable
    public Project getActiveProject() {
        return activeProject;
    }

    public boolean openProject(@NotNull String projectName) {
        if (activeProject == null) {
            if (projects.contains(projectName)) {
                try {
                    activeProject = new Project(projectName, new File(projectsFolder + "/" + projectName + "/"));
                } catch (IOException ex) {
                    Orbis.getLogger().error("Something went wrong opening project {}", projectName);
                    ex.printStackTrace();
                    return false;
                }
                return true;
            } else {
                Orbis.getLogger().error("Project by name: {} doesn't exist!", projectName);
            }
        } else {
            Orbis.getLogger().error("Can't open a project while another one is open!");
        }
        return false;
    }

    /**
     * Closes the current project gracefully.
     */
    public boolean closeProject() {
        if (activeProject != null && activeProject.close()) {
            activeProject = null;
            return true;
        }
        return false;
    }

    public boolean createProject(@NotNull String projectName) {
        if (activeProject == null) {
            if (!projects.contains(projectName)) {
                File projectFolder = new File(projectsFolder + "/" + projectName + "/");
                if (projectFolder.mkdirs()) {
                    try {
                        activeProject = new Project(projectName, projectFolder);
                    } catch (IOException ex) {
                        Orbis.getLogger().error("Something went wrong creating project {}", projectName);
                        ex.printStackTrace();
                        return false;
                    }
                    reloadProjects();
                    return true;
                } else {
                    Orbis.getLogger().error("Failed to create project folder for {}", projectName);
                }
            } else {
                Orbis.getLogger().error("Project by name: {} already exists!", projectName);
            }
        } else {
            Orbis.getLogger().error("Can't create a project while another one is open!");
        }
        return false;
    }

}
