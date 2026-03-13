package com.rengv.pokecubeutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ReloadCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("reloadpokecubeutils")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            try {
                                Config.reload();
                                source.sendMessage(Text.literal("§aPokeCubeUtils reloaded!"));
                            } catch (Exception e) {
                                PokeCubeUtils.sendDebugMessage("Failed to reload PokeCubeUtils" + e);
                                source.sendError(Text.literal("§cPokeCubeUtils Reload failed. Check console."));
                            }
                            return 1;
                        })
        );
    }
}
