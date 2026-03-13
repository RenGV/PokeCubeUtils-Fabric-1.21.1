package com.rengv.pokecubeutils.events;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class AttackEntityEvent {
    public static void register() {
        AttackEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {
            if(world.isClient()) return ActionResult.PASS;

            Identifier id = Registries.ENTITY_TYPE.getId(entity.getType());

            if(Config.disabled_entity_attack.contains(id) && !player.hasPermissionLevel(2)) {
                ((ServerPlayerEntity)player).networkHandler.sendPacket(new GameMessageS2CPacket(Text.literal("§cNo puedes hacer eso"), true));
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        }));
    }
}
