package com.rengv.pokecubeutils.events;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.rengv.pokecubeutils.utils.PermissionHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class InfiniteStaminaEvents {
    public static void register() {
        CobblemonEvents.RIDE_EVENT_APPLY_STAMINA.subscribe(event -> {
            ServerPlayerEntity player = event.getPlayer();

            if (!PermissionHelper.hasInfiniteStamina(player)) return;

            player.sendMessage(Text.of("§a¡Tu Pokémon no se cansa!"));

            event.setInfiniteStamina();
        });
    }
}
