package com.rengv.pokecubeutils.events;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.utils.EventManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class FreezePlayerEvent {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if(!EventManager.STARTED) return;

            for(UUID uuid : EventManager.playerFrozen) {
                ServerPlayerEntity spe = PokeCubeUtils.SERVER.getPlayerManager().getPlayer(uuid);

                if(spe == null) continue;

                spe.setVelocity(0, 0, 0);
                spe.velocityModified = true;

                spe.teleport(
                        spe.getServerWorld(),
                        spe.getX(),
                        spe.getY(),
                        spe.getZ(),
                        spe.getYaw(),
                        spe.getPitch()
                );
            }
        });
    }
}
