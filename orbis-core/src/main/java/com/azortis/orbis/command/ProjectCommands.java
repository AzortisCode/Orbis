/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2023 Azortis
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

package com.azortis.orbis.command;

import cloud.commandframework.annotations.*;
import cloud.commandframework.annotations.processing.CommandContainer;
import cloud.commandframework.annotations.suggestions.Suggestions;
import cloud.commandframework.context.CommandContext;
import com.azortis.orbis.Orbis;
import com.azortis.orbis.entity.Player;
import com.azortis.orbis.pack.studio.ProjectManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@CommandContainer
@CommandPermission("orbis.admin")
@CommandMethod(value = "orbis|o project", requiredSender = CommandSender.class)
public final class ProjectCommands {

    private final ProjectManager manager = Orbis.getProjectManager();
    private final MiniMessage miniMessage = Orbis.getMiniMessage();

    @CommandDescription("Returns a list of currently available projects.")
    @CommandMethod("list")
    public void projectList(final @NotNull CommandSender sender) {
        if (!manager.getProjects().isEmpty()) {
            final TextComponent.Builder builder = Component.text()
                    .append(miniMessage.deserialize("<prefix> <grey>The current available projects are:"));

            for (String projectName : manager.getProjects()) {
                builder.append(miniMessage
                        .deserialize("<newline><dark_grey>* <hover:show_text:'<grey>Click to <green>open</green><grey>!'>" +
                                        "<click:run_command:/orbis project open " + projectName + "><green><project></click></hover>",
                                TagResolver.resolver("project", Tag.selfClosingInserting(Component.text(projectName)))));
            }
            sender.sendMessage(builder.build());
            return;
        }
        sender.sendMessage(miniMessage.deserialize("<prefix> <red>There are currently no projects available!"));
    }

    @CommandDescription("Opens specified project for editing.")
    @CommandMethod("open <project>")
    public void openProject(final @NotNull CommandSender sender,
                            final @NotNull @Argument(value = "project", suggestions = "projects",
                                    description = "The name of the project") String project,
                            final @Flag(value = "no-world",
                                    description = "If no studio world should be loaded.") boolean noWorld) {
        if (manager.getActiveProject() == null) {
            if (manager.getProjects().contains(project)) {
                sender.sendMessage(miniMessage.deserialize("<prefix> <grey>Opening project..."));
                if (!manager.openProject(project, !noWorld)) {
                    sender.sendMessage(miniMessage.deserialize("<prefix> <red>Failed to open project!"));
                } else if (!noWorld) {
                    sender.sendMessage(miniMessage.deserialize("<prefix> <green>Successfully opened project!"));
                    if (sender instanceof Player player) {
                        // Adding player who opened the project as viewer.
                        manager.getActiveProject().studioWorld().addViewer(player);
                    }
                }
                return;
            } else {
                sender.sendMessage(miniMessage.deserialize("<prefix> <red>Project by name <dark_red>" + project + " <red>doesn't exist!"));
            }
            return;
        }
        sender.sendMessage(miniMessage.deserialize("<prefix> <red>Can't open a project while another one is active!"));
    }

    @CommandDescription("Saves and closes the currently active project.")
    @CommandMethod("close")
    public void closeProject(final @NotNull CommandSender sender) {
        if (manager.getActiveProject() != null) {
            sender.sendMessage(miniMessage.deserialize("<prefix> <grey>Closing project..."));
            if (manager.closeProject()) {
                sender.sendMessage(miniMessage.deserialize("<prefix> <green>Project successfully closed!"));
            } else {
                sender.sendMessage(miniMessage.deserialize("<prefix> <red>Failed to close project!"));
            }
            return;
        }
        sender.sendMessage(miniMessage.deserialize("<prefix> <red>There is no active project!"));
    }

    @CommandDescription("Creates a new project with the specified name and opens it.")
    @CommandMethod("create <projectName>")
    public void createProject(final @NotNull CommandSender sender,
                              final @NotNull @Argument(value = "projectName",
                                      description = "The name of the project") String projectName,
                              final @Flag(value = "no-world",
                                      description = "If no studio world should be loaded.") boolean noWorld) {
        if (manager.getActiveProject() == null) {
            if (!manager.getProjects().contains(projectName)) {
                sender.sendMessage(miniMessage.deserialize("<prefix> <grey>Creating and opening project..."));
                if (!manager.createProject(projectName, !noWorld)) {
                    sender.sendMessage(miniMessage.deserialize("<prefix> <red>Failed to create project!"));
                    return;
                } else if (!noWorld) {
                    if (sender instanceof Player player) {
                        // Adding player who opened the project as viewer.
                        manager.getActiveProject().studioWorld().addViewer(player);
                    }
                }
                sender.sendMessage(miniMessage.deserialize("<prefix> <green>Successfully created & opened project!"));
                return;
            }
            sender.sendMessage(miniMessage.deserialize("<prefix> <red>Project by name <dark_red>" + projectName + " <red>already exists!"));
            return;
        }
        sender.sendMessage(miniMessage.deserialize("<prefix> <red>Cannot create project while another one is active!"));
    }

    @Suggestions("projects")
    public @NotNull List<String> projectNames(CommandContext<CommandSender> context, String input) {
        return manager.getProjects().stream().toList();
    }

}
