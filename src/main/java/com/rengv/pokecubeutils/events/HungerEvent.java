package com.rengv.pokecubeutils.events;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.PlayerData;
import com.rengv.pokecubeutils.config.PlayerList;
import com.rengv.pokecubeutils.utils.EventManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;

public class HungerEvent {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if(!EventManager.STARTED) return;

            if(!EventManager.HUNGER) {
                for(Map.Entry<UUID, PlayerData> p : PlayerList.players.entrySet()) {
                    UUID uuid = p.getKey();

                    ServerPlayerEntity spe = PokeCubeUtils.SERVER.getPlayerManager().getPlayer(uuid);

                    if(spe == null) continue;

                    spe.getHungerManager().setFoodLevel(20);
                    spe.getHungerManager().setSaturationLevel(20.0f);
                }
            }
        });
    }
}
