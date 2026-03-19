package com.rengv.pokecubeutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import com.rengv.pokecubeutils.config.PlayerData;
import com.rengv.pokecubeutils.config.PlayerList;
import com.rengv.pokecubeutils.config.PosData;
import com.rengv.pokecubeutils.gui.EventGUI;
import com.rengv.pokecubeutils.utils.EventManager;
import com.rengv.pokecubeutils.utils.PermissionHelper;
import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;

import java.util.UUID;

public class EventCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("evento")
                        .then(CommandManager.literal("join")
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();

                                    if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
                                        source.sendError(Utils.format("&cOnly players can run this command!"));
                                        return 0;
                                    }

                                    if(!EventManager.STARTED) {
                                        player.sendMessage(Utils.format("&cNo hay ningún evento activo."));
                                        return 0;
                                    }

                                    if(!Utils.isInventoryEmpty(player)) {
                                        player.sendMessage(Utils.format("&cDebes tener el inventario vacío para entrar al evento."));
                                        return 0;

                                    }

                                    if(!EventManager.CAN_JOIN_EVENT) {
                                        player.sendMessage(Utils.format("&cYa no se puede entrar al evento."));
                                        return 0;
                                    }

                                    UUID uuid = player.getUuid();

                                    if (PlayerList.players.containsKey(uuid)) {
                                        player.sendMessage(Utils.format("&cYa estás en el evento."), false);
                                        return 0;
                                    }

                                    ServerWorld eventWorld = Utils.getWorld(Config.world_event);
                                    PosData eventCoords = Config.event_coords;

                                    EventManager.enterBypass.add(uuid);
                                    player.teleport(eventWorld, eventCoords.x, eventCoords.y, eventCoords.z, eventCoords.yaw, eventCoords.pitch);

                                    player.getAbilities().allowFlying = false;
                                    player.getAbilities().flying = false;
                                    player.sendAbilitiesUpdate();

                                    PlayerData playerData = new PlayerData(player.getName().getString());

                                    PlayerList.players.put(uuid, playerData);
                                    PlayerList.save();

                                    player.sendMessage(Utils.format("&a¡Te has unido al evento!"), false);

                                    return 1;
                                })
                        ).then(CommandManager.literal("leave")
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();

                                    if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
                                        source.sendError(Utils.format("&cOnly players can run this command!"));
                                        return 0;
                                    }

                                    UUID uuid = player.getUuid();

                                    if (!PlayerList.players.containsKey(uuid)) {
                                        player.sendMessage(Utils.format("&cNo estás en el evento."), false);
                                        return 0;
                                    }

                                    EventManager.leaveBypass.add(player.getUuid());

                                    ServerWorld spawnWorld = Utils.getWorld(Config.world_spawn);
                                    PosData spawnCoords = Config.spawn_coords;

                                    player.teleport(spawnWorld, spawnCoords.x, spawnCoords.y, spawnCoords.z, spawnCoords.yaw, spawnCoords.pitch);
                                    player.getInventory().clear();
                                    PlayerList.players.remove(uuid);
                                    PlayerList.save();

                                    player.changeGameMode(GameMode.SURVIVAL);
                                    EventManager.playerFrozen.remove(uuid);

                                    player.sendMessage(Utils.format("&a¡Has salido del evento!"));

                                    return 1;
                                })
                        ).then(CommandManager.literal("spawn")
                                .requires(source -> PermissionHelper.hasCommandPermission(source, "pokecube.command.spawn"))
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();

                                    if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
                                        source.sendError(Utils.format("&cOnly players can run this command!"));
                                        return 0;
                                    }

                                    Config.world_spawn = Utils.getWorldPath(player.getServerWorld());
                                    Config.spawn_coords = new PosData(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
                                    Config.save();
                                    player.sendMessage(Utils.format("&aPunto de spawn establecido."));

                                    return 1;
                                })
                        ).then(CommandManager.literal("gui")
                                .requires(source -> PermissionHelper.hasCommandPermission(source, "pokecube.command.gui"))
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();

                                    if (source.getEntity() instanceof ServerPlayerEntity player) {
                                        if(!player.getWorld().equals(PokeCubeUtils.EVENT_WORLD)) {
                                            player.sendMessage(Utils.format("&cNo estás en el mundo de eventos. Usa: &e/eventos create"));
                                            return 1;
                                        }
                                        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                                                (syncId, inv, p) -> new EventGUI(syncId, inv),
                                                Utils.format("&2&l            Eventos")
                                        ));
                                        return 1;
                                    }

                                    source.sendError(Utils.format("&cOnly players can run this command!"));

                                    return 1;
                                })
                        ).then(CommandManager.literal("create")
                                .requires(source -> PermissionHelper.hasCommandPermission(source, "pokecube.command.create"))
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();

                                    if (source.getEntity() instanceof ServerPlayerEntity player) {
                                        if(!Utils.isInventoryEmpty(player)) {
                                            player.sendMessage(Utils.format("&cDebes tener el inventario vacío para crear un evento."));
                                            return 0;
                                        }

                                        UUID uuid = player.getUuid();

                                        if (PlayerList.players.containsKey(uuid)) {
                                            player.sendMessage(Utils.format("&cYa estás en el evento."), false);
                                            return 0;
                                        }

                                        ServerWorld eventWorld = Utils.getWorld(Config.world_event);
                                        PosData eventCoords = Config.event_coords;

                                        EventManager.enterBypass.add(uuid);
                                        player.teleport(eventWorld, eventCoords.x, eventCoords.y, eventCoords.z, eventCoords.yaw, eventCoords.pitch);

                                        PlayerData playerData = new PlayerData(player.getName().getString(), true);

                                        PlayerList.players.put(uuid, playerData);
                                        PlayerList.save();

                                        player.sendMessage(Utils.format("&a¡Estás creando un nuevo evento! Usa &e/evento gui &apara configurar el evento."), false);
                                        return 1;
                                    }

                                    source.sendError(Utils.format("&cOnly players can run this command!"));

                                    return 1;
                                })
                        )
        );
    }
}
