package com.rengv.pokecubeutils.events;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.utils.EventManager;
import com.rengv.pokecubeutils.utils.PermissionHelper;
import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class InfiniteStaminaEvents {
    public static void register() {
        CobblemonEvents.RIDE_EVENT_APPLY_STAMINA.subscribe(event -> {
            ServerPlayerEntity player = event.getPlayer();

            if (!PermissionHelper.hasCommandPermission(player.getCommandSource(), "pokecube.infinite.ride")) return;

            if(player.getWorld().equals(PokeCubeUtils.EVENT_WORLD) && !EventManager.INFINITE_RIDE_STAMINE){
                player.sendMessage(Utils.format("&cStamina infinita desactivada en evento"), true);
                return;
            }

            player.sendMessage(Text.of("§a¡Tu Pokémon no se cansa!"));

            event.setInfiniteStamina();
        });
    }
}
