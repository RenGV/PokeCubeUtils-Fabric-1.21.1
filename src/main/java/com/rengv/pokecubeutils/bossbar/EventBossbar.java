package com.rengv.pokecubeutils.bossbar;

import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class EventBossbar {
    public static ServerBossBar bossBar;
    public static int timeLeft = 0;

    public static void start(MinecraftServer server, String message, int timeLeft) {
        EventBossbar.timeLeft = timeLeft;

        bossBar = new ServerBossBar(
                Utils.format(message),
                BossBar.Color.YELLOW,
                BossBar.Style.PROGRESS
        );

        bossBar.setPercent(1.0f);

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            bossBar.addPlayer(player);
        }
    }
}
