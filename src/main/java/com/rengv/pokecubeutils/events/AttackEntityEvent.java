package com.rengv.pokecubeutils.events;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import com.rengv.pokecubeutils.utils.EventManager;
import com.rengv.pokecubeutils.utils.Utils;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class AttackEntityEvent {
    public static void register() {
        AttackEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {
            if(world.isClient()) return ActionResult.PASS;

            if(entity instanceof PlayerEntity && player.getWorld().equals(PokeCubeUtils.EVENT_WORLD) && !EventManager.PVP) {
                player.sendMessage(Utils.format("&cNo puedes hacer eso aquí"), true);
                return ActionResult.FAIL;
            }

            if(entity instanceof MobEntity && player.getWorld().equals(PokeCubeUtils.EVENT_WORLD) && !EventManager.PVE) {
                player.sendMessage(Utils.format("&cNo puedes hacer eso aquí"), true);
                return ActionResult.FAIL;
            }

            Identifier id = Registries.ENTITY_TYPE.getId(entity.getType());

            if(Config.disabled_entity_attack.contains(id) && !player.hasPermissionLevel(2)) {
                player.sendMessage(Utils.format("&cNo puedes hacer eso"), true);
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        }));
    }
}
