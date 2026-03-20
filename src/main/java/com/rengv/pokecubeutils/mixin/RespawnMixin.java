package com.rengv.pokecubeutils.mixin;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import com.rengv.pokecubeutils.config.PlayerList;
import com.rengv.pokecubeutils.config.PosData;
import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class RespawnMixin {
    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void onRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;

        RegistryKey<World> eventKey = PokeCubeUtils.EVENT_WORLD.getRegistryKey();

        player.getServer().execute(() -> {
            if (!player.isAlive()) return;

            ServerWorld currentWorld = player.getServerWorld();

            if (PlayerList.players.containsKey(player.getUuid())) {
                if (currentWorld.getRegistryKey().equals(eventKey)) {
                    return;
                }

                ServerWorld eventWorld = Utils.getWorld(Config.world_event);
                PosData eventCoords = Config.event_coords;

                if (eventWorld != null) {
                    player.teleport(
                            eventWorld,
                            eventCoords.x,
                            eventCoords.y,
                            eventCoords.z,
                            eventCoords.yaw,
                            eventCoords.pitch
                    );
                    return;
                }
            }

            if (currentWorld.getRegistryKey().equals(eventKey)) {
                ServerWorld spawnWorld = Utils.getWorld(Config.world_spawn);
                PosData spawnCoords = Config.spawn_coords;

                if (spawnWorld != null) {
                    player.teleport(
                            spawnWorld,
                            spawnCoords.x,
                            spawnCoords.y,
                            spawnCoords.z,
                            spawnCoords.yaw,
                            spawnCoords.pitch
                    );
                }
            }
        });
    }
}
