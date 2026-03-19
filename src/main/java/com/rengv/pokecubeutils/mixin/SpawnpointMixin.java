package com.rengv.pokecubeutils.mixin;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class SpawnpointMixin {
    @Inject(method = "setSpawnPoint", at = @At("HEAD"), cancellable = true)
    private void blockSpawnpoint(RegistryKey<World> dimension, BlockPos pos, float angle, boolean forced, boolean sendMessage, CallbackInfo ci) {

        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;

        if (player.getWorld().equals(PokeCubeUtils.EVENT_WORLD) && !forced) {
            player.sendMessage(Utils.format("&cNo se puede establecer un nuevo spawnpoint en el evento."));

            ci.cancel();
        }
    }
}
