package com.azortis.orbis;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandProcessor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StopCommand implements CommandProcessor {

    @NotNull
    @Override
    public String getCommandName() {
        return "stop";
    }

    @Nullable
    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean process(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        MinecraftServer.stopCleanly();
        return true;
    }

    @Override
    public boolean hasAccess(@NotNull Player player) {
        return true;
    }
}
