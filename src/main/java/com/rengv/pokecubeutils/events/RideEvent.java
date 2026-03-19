package com.rengv.pokecubeutils.events;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.utils.EventManager;
import com.rengv.pokecubeutils.utils.PermissionHelper;
import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.server.network.ServerPlayerEntity;

public class RideEvent {
    public static void register() {
        CobblemonEvents.RIDE_EVENT_PRE.subscribe(event -> {
            ServerPlayerEntity player = event.getPlayer();

            if(!player.getWorld().equals(PokeCubeUtils.EVENT_WORLD)) return;

            if (PermissionHelper.hasCommandPermission(player.getCommandSource(), "pokecube.bypass")) return;

            if(EventManager.CAN_RIDE) return;

            player.sendMessage(Utils.format("&cNo puedes hacer eso aquí"), true);
            event.cancel();
        });
    }
}
