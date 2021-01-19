package com.azortis.orbis;

import com.azortis.orbis.generator.OrbisChunkGenerator;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.extras.optifine.OptifineSupport;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.storage.StorageLocation;
import net.minestom.server.storage.StorageManager;
import net.minestom.server.storage.systems.FileStorageSystem;
import net.minestom.server.utils.Position;
import net.minestom.server.world.DimensionType;

import java.util.UUID;

public class OrbisBootstrap {

    public static void main(String[] args){
        MinecraftServer minecraftServer = MinecraftServer.init();

        StorageManager storageManager = MinecraftServer.getStorageManager();
        storageManager.defineDefaultStorageSystem(FileStorageSystem::new);

        StorageLocation storageLocation = storageManager.getLocation("chunk_data");

        InstanceContainer instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer(DimensionType.OVERWORLD, storageLocation);
        instanceContainer.setChunkGenerator(new OrbisChunkGenerator());
        instanceContainer.enableAutoChunkLoad(true);
        for (int x = -16; x <= 16; x++){
            for (int z = -16; z <= 16; z++){
                instanceContainer.loadChunk(x, z);
            }
        }
        instanceContainer.saveChunksToStorage();

        OptifineSupport.enable();

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addEventCallback(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            player.setSkin(PlayerSkin.fromUsername(player.getUsername()));
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Position(0, 180, 0));
        });

        globalEventHandler.addEventCallback(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();
            player.setGameMode(GameMode.CREATIVE);
            player.setAllowFlying(true);
            player.setFlyingSpeed(2);
        });

        MinecraftServer.getCommandManager().register(new StopCommand());

        minecraftServer.start("127.0.0.1", 25565, (playerConnection, responseData) -> {
            responseData.setMaxPlayer(0);
            responseData.setOnline(MinecraftServer.getConnectionManager().getOnlinePlayers().size());
            responseData.addPlayer("A name", UUID.randomUUID());
            responseData.addPlayer("Could be some message", UUID.randomUUID());
            responseData.setDescription("IP test: " + playerConnection.getRemoteAddress());
        });
    }

}
