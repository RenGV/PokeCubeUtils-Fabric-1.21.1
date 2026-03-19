package com.rengv.pokecubeutils.events;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import com.rengv.pokecubeutils.config.PlayerList;
import com.rengv.pokecubeutils.config.PosData;
import com.rengv.pokecubeutils.utils.Utils;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import static com.rengv.pokecubeutils.PokeCubeUtils.EVENT_WORLD;
import static com.rengv.pokecubeutils.utils.EventManager.enterBypass;
import static com.rengv.pokecubeutils.utils.EventManager.leaveBypass;

public class PreventWorldChangeEvent {
    public static void register() {
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(
                (player, origin, destination) -> {
                    ServerWorld dest = destination;
                    ServerWorld from = origin;

                    // Bloquear entrar al mundo evento
                    if(dest.getRegistryKey().equals(EVENT_WORLD.getRegistryKey()) && !PlayerList.players.containsKey(player.getUuid())) {
                        if(enterBypass.remove(player.getUuid())) return;

                        ServerWorld spawnWorld = Utils.getWorld(Config.world_spawn);
                        PosData spawnCoords = Config.spawn_coords;

                        player.getServer().execute(() -> player.teleport(spawnWorld, spawnCoords.x, spawnCoords.y, spawnCoords.z, spawnCoords.yaw, spawnCoords.pitch));
                        player.sendMessage(Utils.format("&cNo puedes entrar al mundo del evento."));
                    }

                    // Bloquear salir del mundo evento
                    if (from.getRegistryKey().equals(EVENT_WORLD.getRegistryKey()) && !dest.getRegistryKey().equals(EVENT_WORLD.getRegistryKey())) {
                        if(leaveBypass.remove(player.getUuid())) return;

                        ServerWorld eventWorld = Utils.getWorld(Config.world_event);
                        PosData eventCoords = Config.event_coords;

                        player.getServer().execute(() -> player.teleport(eventWorld, eventCoords.x, eventCoords.y, eventCoords.z, eventCoords.yaw, eventCoords.pitch));
                        player.sendMessage(Utils.format("&cNo puedes salir del mundo del evento. Usa: &e/evento leave"));
                    }
                }
        );
    }
}
