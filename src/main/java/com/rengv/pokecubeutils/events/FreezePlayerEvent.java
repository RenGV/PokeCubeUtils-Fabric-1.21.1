package com.rengv.pokecubeutils.events;

import com.rengv.pokecubeutils.utils.EventManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class FreezePlayerEvent {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if(EventManager.playerFrozen.contains(player.getUuid())) {
                    player.setVelocity(0, 0, 0);
                    player.velocityModified = true;

                    player.teleport(
                            player.getServerWorld(),
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            player.getYaw(),
                            player.getPitch()
                    );
                }
            }
        });
    }
}
