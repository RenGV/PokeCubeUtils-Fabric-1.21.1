package com.rengv.pokecubeutils.events;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import java.util.Set;

public class AttackBlockEvent {
    public static void register() {
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
//            if(world.isClient()) return ActionResult.PASS;

//            PokeCubeUtils.sendDebugMessage("AttackBlockCallback");
//
//            ItemStack stack = player.getStackInHand(hand);
//            return TypedActionResult.pass(stack).getResult();
            return null;
        });
    }
}
