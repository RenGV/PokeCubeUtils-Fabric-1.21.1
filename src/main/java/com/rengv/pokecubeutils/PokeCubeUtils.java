package com.rengv.pokecubeutils;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.mojang.brigadier.CommandDispatcher;
import com.rengv.pokecubeutils.commands.RandomShinyCommand;
import com.rengv.pokecubeutils.commands.ReloadCommand;
import com.rengv.pokecubeutils.config.Config;
import com.rengv.pokecubeutils.events.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PokeCubeUtils implements ModInitializer {
    public static final String MOD_ID = "pokecubeutils";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static MinecraftServer SERVER;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing PokeCubeUtils");

        Config.load();
        registerEvents();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            SERVER = server;
        });

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, enviroment) -> {
                    registerCommands(dispatcher);
                }
        );

        InfiniteStaminaEvents.register();

        LOGGER.info("PokeCubeUtils succesfully loaded!");
    }

    private void registerEvents() {
        // AttackBlockEvent.register();
        AttackEntityEvent.register();
        BreakBlockEvent.register();
        InteractBlockEvent.register();
        InteractEntityEvent.register();
        UseItemEvent.register();
    }

    private void registerCommands(CommandDispatcher dispatcher) {
        RandomShinyCommand.register(dispatcher);
        ReloadCommand.register(dispatcher);
    }

    public static void sendDebugMessage(String message) {
        LOGGER.info(message);
    }

    public static void sendErrorMessage(String message) {
        LOGGER.error(message);
    }
}
