package com.rengv.pokecubeutils.mixin;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.PlayerList;
import com.rengv.pokecubeutils.utils.EventManager;
import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.rengv.pokecubeutils.PokeCubeUtils.EVENT_WORLD;

@Mixin(ServerPlayerEntity.class)
public class PreventWorldChangeMixin {
    @Inject(method = "teleportTo", at = @At("HEAD"), cancellable = true)
    private void preventTeleport(TeleportTarget target, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;

        ServerWorld destination = target.world();

        RegistryKey<World> origin = player.getWorld().getRegistryKey();
        RegistryKey<World> dest = destination.getRegistryKey();

        // Bloquear entrar al mundo evento
        if (dest.equals(EVENT_WORLD.getRegistryKey()) && !PlayerList.players.containsKey(player.getUuid())) {
            player.sendMessage(Utils.format("&cNo puedes entrar al mundo del evento."));
            cir.setReturnValue(false);
        }

        // Bloquear salir del mundo evento
        if (origin.equals(EVENT_WORLD.getRegistryKey()) && !dest.equals(EVENT_WORLD.getRegistryKey()) && PlayerList.players.containsKey(player.getUuid())) {
            player.sendMessage(Utils.format("&cNo puedes salir del mundo del evento. Usa: &e/evento leave"));
            cir.setReturnValue(false);
        }
    }
}
