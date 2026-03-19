package com.rengv.pokecubeutils.bossbar;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class BossbarJoinHandler {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;

            if (EventBossbar.bossBar != null) {
                EventBossbar.bossBar.addPlayer(player);
            }
        });
    }
}
