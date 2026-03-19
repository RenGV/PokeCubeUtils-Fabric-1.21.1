package com.rengv.pokecubeutils.events;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import com.rengv.pokecubeutils.utils.Utils;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class InteractBlockEvent {
    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if(world.isClient()) return ActionResult.PASS;

            Block block = world.getBlockState(hitResult.getBlockPos()).getBlock();
            Identifier id = Registries.BLOCK.getId(block);

            if(id.equals(Identifier.of("minecraft:ender_chest")) && player.getWorld().equals(PokeCubeUtils.EVENT_WORLD)) {
                player.sendMessage(Utils.format("&cNo puedes hacer eso aquí"), true);
                return ActionResult.FAIL;
            }

            if(Config.disabled_block_interact.contains(id) && !player.hasPermissionLevel(2)) {
                ((ServerPlayerEntity)player).networkHandler.sendPacket(new GameMessageS2CPacket(Text.literal("§cNo puedes hacer eso"), true));
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });
    }
}
