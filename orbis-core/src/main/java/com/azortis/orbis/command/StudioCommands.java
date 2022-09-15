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

package com.azortis.orbis.command;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.processing.CommandContainer;
import com.azortis.orbis.Orbis;
import com.azortis.orbis.Player;
import com.azortis.orbis.pack.studio.ProjectManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

@CommandContainer
@CommandPermission("orbis.admin")
@CommandMethod(value = "orbis|o studio", requiredSender = CommandSender.class)
public class StudioCommands {

    private final ProjectManager projectManager = Orbis.getProjectManager();
    private final MiniMessage miniMessage = Orbis.getMiniMessage();

    @CommandDescription("Enter the studio world.")
    @CommandMethod(value = "enter", requiredSender = Player.class)
    public void enterStudio(final @NotNull Player player) {
        if (projectCheck(player)) {
            assert projectManager.getActiveProject() != null;
            if (!projectManager.getActiveProject().studioWorld().getViewers().contains(player)) {
                if (!projectManager.getActiveProject().studioWorld().getViewers().contains(player)) {
                    projectManager.getActiveProject().studioWorld().addViewer(player);
                } else {
                    player.sendMessage(miniMessage.deserialize("<prefix> <red>You are already in the studio world!"));
                }
            }
        }
    }

    @CommandDescription("Exit the studio world.")
    @CommandMethod(value = "exit", requiredSender = Player.class)
    public void exitStudio(final @NotNull Player player) {
        if (projectCheck(player)) {
            assert projectManager.getActiveProject() != null;
            if (projectManager.getActiveProject().studioWorld().getViewers().contains(player)) {
                projectManager.getActiveProject().studioWorld().removeViewer(player);
            } else {
                player.sendMessage(miniMessage.deserialize("<prefix> <red>You cannot exit the studio world, since you're not in it!"));
            }
        }
    }

    private boolean projectCheck(final @NotNull CommandSender sender) {
        if (projectManager.getActiveProject() == null) {
            sender.sendMessage(miniMessage.deserialize("<prefix> <red>There is no project currently active!"));
            return false;
        }
        return true;
    }

}
