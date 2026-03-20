package com.rengv.pokecubeutils.events;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import com.rengv.pokecubeutils.config.PlayerList;
import com.rengv.pokecubeutils.utils.EventManager;
import com.rengv.pokecubeutils.utils.Utils;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BreakBlockEvent {
    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if(world.isClient()) return true;

            if(player.getWorld().equals(PokeCubeUtils.EVENT_WORLD) && !EventManager.CAN_BREAK){
                if(PlayerList.players.get(player.getUuid()).isManager()) return true;

                player.sendMessage(Utils.format("&cNo puedes hacer eso aquí"), true);
                return false;
            }

            Block block = state.getBlock();
            Identifier id = Registries.BLOCK.getId(block);

            if(Config.disabled_block_break.contains(id) && !player.hasPermissionLevel(2)) {
                player.sendMessage(Utils.format("&cNo puedes hacer eso"), true);
                return false;
            }

            return true;
        });
    }
}
