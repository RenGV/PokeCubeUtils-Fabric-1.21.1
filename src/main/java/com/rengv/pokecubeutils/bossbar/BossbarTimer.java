package com.rengv.pokecubeutils.bossbar;

import com.rengv.pokecubeutils.utils.EventManager;
import com.rengv.pokecubeutils.utils.Utils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class BossbarTimer {
    private static int tickCounter = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            //float timeBase = (float) EventBossbar.timeLeft;

            if (EventBossbar.bossBar == null) return;

            tickCounter++;

            if (tickCounter >= 20) { // 20 ticks = 1 segundo
                tickCounter = 0;

                EventBossbar.timeLeft--;

                if (EventBossbar.timeLeft <= 0) {
                    EventManager.CAN_JOIN_EVENT = false;

                    EventBossbar.bossBar.setPercent(0f);
                    EventBossbar.bossBar.clearPlayers();
                    EventBossbar.bossBar = null;
                    return;
                }

                float percent = EventBossbar.timeLeft / 300f;

                EventBossbar.bossBar.setPercent(percent);

//                EventBossbar.bossBar.setName(
//                        Utils.format("&6El evento empieza en " + EventBossbar.timeLeft + " s")
//                );
            }
        });
    }
}
