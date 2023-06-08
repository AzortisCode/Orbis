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

import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.processing.CommandContainer;
import com.azortis.orbis.Orbis;

@SuppressWarnings("unused")
@CommandContainer
@CommandPermission("orbis.admin")
@CommandMethod(value = "orbis|o", requiredSender = CommandSender.class)
public final class BaseCommands {

    @CommandMethod("info")
    public void info(CommandSender sender) {
        sender.sendMessage(Orbis.getMiniMessage().deserialize("<prefix> <gray>Running Orbis version: <green>"
                + Orbis.VERSION + "<gray>."));
    }

}
