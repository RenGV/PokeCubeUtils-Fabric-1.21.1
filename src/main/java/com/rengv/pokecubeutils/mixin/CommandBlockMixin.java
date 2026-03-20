package com.rengv.pokecubeutils.mixin;

import com.mojang.brigadier.ParseResults;
import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandBlockMixin {
    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    private void blockCommands(ParseResults<ServerCommandSource> parseResults, String command, CallbackInfo ci) {

        ServerCommandSource source = parseResults.getContext().getSource();

        if (!(source.getEntity() instanceof ServerPlayerEntity player))
            return;

        if (!player.getWorld().equals(PokeCubeUtils.EVENT_WORLD))
            return;

        String cmd = command.toLowerCase();

        if (cmd.startsWith("essentials")
                || cmd.startsWith("home")
                || cmd.startsWith("sethome")
                || cmd.startsWith("spawn")
                || cmd.startsWith("fly")
                || cmd.startsWith("pokegift")
                || cmd.startsWith("pokemodifier")
                || cmd.startsWith("wt")
                || cmd.startsWith("wondertrade")
                || cmd.startsWith("pixelgym")
                || cmd.startsWith("medallas")
                || cmd.startsWith("badges")
                || cmd.startsWith("pixelpass")
                || cmd.startsWith("pase")
                || cmd.startsWith("pass")
                || cmd.startsWith("gts")
                || cmd.startsWith("ec")
                || cmd.startsWith("enderchest")
                || cmd.startsWith("deluxemenu")
                || cmd.startsWith("warp")
                || cmd.startsWith("warps")
                || cmd.startsWith("back")
                || cmd.startsWith("menu")
                || cmd.startsWith("rtp")
                || cmd.startsWith("tpa")
                || cmd.startsWith("tpaccept")
                || cmd.startsWith("tpahere")) {

            player.sendMessage(Utils.format("&cNo puedes usar ese comando en el evento."));

            ci.cancel();
        }
    }
}
