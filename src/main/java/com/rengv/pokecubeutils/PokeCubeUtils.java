package com.rengv.pokecubeutils;

import com.mojang.brigadier.CommandDispatcher;
import com.rengv.pokecubeutils.bossbar.BossbarJoinHandler;
import com.rengv.pokecubeutils.bossbar.BossbarTimer;
import com.rengv.pokecubeutils.commands.EventCommand;
import com.rengv.pokecubeutils.commands.RandomShinyCommand;
import com.rengv.pokecubeutils.commands.ReloadCommand;
import com.rengv.pokecubeutils.config.Config;
import com.rengv.pokecubeutils.config.PlayerList;
import com.rengv.pokecubeutils.events.*;
import com.rengv.pokecubeutils.utils.Utils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PokeCubeUtils implements ModInitializer {
    public static final String MOD_ID = "pokecubeutils";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static MinecraftServer SERVER;
    public static World EVENT_WORLD;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing PokeCubeUtils");

        Config.load();
        PlayerList.load();
        registerFabricEvents();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            SERVER = server;
            EVENT_WORLD = Utils.getWorld(Config.world_event);
            server.execute(this::registerBukkitEvents);
        });

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, enviroment) -> {
                    registerCommands(dispatcher);
                }
        );

        registerCobblemonEvents();

        LOGGER.info("PokeCubeUtils succesfully loaded!");
    }

    private void registerBukkitEvents() {
        if (Bukkit.getServer() == null) {
            LOGGER.warn("Bukkit still not initialized, retrying...");
            SERVER.execute(this::registerBukkitEvents);
            return;
        }

        LOGGER.info("Bukkit detected, registering events.");

        org.bukkit.plugin.Plugin plugin = Bukkit.getPluginManager().getPlugins()[0];

        Bukkit.getPluginManager().registerEvents(
                new BukkitTeleportListener(),
                plugin
        );
    }

    private void registerFabricEvents() {
        // AttackBlockEvent.register();
        AttackEntityEvent.register();
        BreakBlockEvent.register();
        InteractBlockEvent.register();
        InteractEntityEvent.register();
        UseItemEvent.register();
        FreezePlayerEvent.register();
        HungerEvent.register();
        // PreventWorldChangeEvent.register();

        // BossBar
        BossbarTimer.register();
        BossbarJoinHandler.register();
    }

    private void registerCobblemonEvents() {
        InfiniteStaminaEvents.register();
        RideEvent.register();
        SentPokemonEvent.register();
    }

    private void registerCommands(CommandDispatcher dispatcher) {
        RandomShinyCommand.register(dispatcher);
        ReloadCommand.register(dispatcher);
        EventCommand.register(dispatcher);
    }

    public static void sendDebugMessage(String message) {
        LOGGER.info(message);
    }

    public static void sendErrorMessage(String message) {
        LOGGER.error(message);
    }
}
