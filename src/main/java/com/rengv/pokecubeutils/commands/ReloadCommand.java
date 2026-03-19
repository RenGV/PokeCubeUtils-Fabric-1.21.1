package com.rengv.pokecubeutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import com.rengv.pokecubeutils.events.BukkitTeleportListener;
import com.rengv.pokecubeutils.utils.PermissionHelper;
import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.bukkit.Bukkit;

import static com.rengv.pokecubeutils.PokeCubeUtils.SERVER;

public class ReloadCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("reloadpokecubeutils")
                        .requires(source -> PermissionHelper.hasCommandPermission(source, "pokecube.command.reload"))
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            try {
                                Config.reload();
                                PokeCubeUtils.EVENT_WORLD = Utils.getWorld(Config.world_event);

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
