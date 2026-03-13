package com.rengv.pokecubeutils.commands;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

import java.util.Set;

public class RideCommand {
    /*public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("ride")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("disable").executes(RideCommand::addChunk))
                        .then(CommandManager.literal("enable").executes(RideCommand::removeChunk))
        );
    }

    private static int addChunk(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        Identifier dim = player.getServerWorld().getRegistryKey().getValue();
        ChunkPos chunk = player.getChunkPos();


        Set<ChunkPos> set = Config.disabled_mount_chunks.computeIfAbsent(dim, k -> new java.util.HashSet<>());

        if (set.add(chunk)) {
            Config.save();
            ctx.getSource().sendFeedback(
                    () -> Text.literal("§cMontura bloqueada en chunk §7[" + chunk.x + "," + chunk.z + "]"),
                    false
            );
        } else {
            ctx.getSource().sendFeedback(
                    () -> Text.literal("§eEste chunk ya estaba bloqueado"),
                    false
            );
        }
        return 1;
    }

    private static int removeChunk(CommandContext<ServerCommandSource> ctx) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        Identifier dim = player.getServerWorld().getRegistryKey().getValue();
        ChunkPos chunk = player.getChunkPos();

        Set<ChunkPos> set = Config.disabled_mount_chunks.get(dim);
        if (set != null && set.remove(chunk)) {
            if (set.isEmpty()) {
                Config.disabled_mount_chunks.remove(dim);
            }
            Config.save();
            ctx.getSource().sendFeedback(
                    () -> Text.literal("§aMontura permitida nuevamente en este chunk"),
                    false
            );
        } else {
            ctx.getSource().sendFeedback(
                    () -> Text.literal("§eEste chunk no estaba bloqueado"),
                    false
            );
        }
        return 1;
    }*/
}
