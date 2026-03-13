package com.rengv.pokecubeutils.events;

import com.rengv.pokecubeutils.config.Config;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

public class UseItemEvent {
    public static void register() {
        UseItemCallback.EVENT.register(((player, world, hand) -> {
            ItemStack item = player.getStackInHand(hand);
            if (item.isEmpty()) return TypedActionResult.pass(item);

            Identifier id = Registries.ITEM.getId(item.getItem());

            boolean disabled = Config.disabled_item_use.contains(id) && !player.hasPermissionLevel(2);

            if (!disabled) {
                return TypedActionResult.pass(item);
            }

            if (world.isClient()) {
                return TypedActionResult.fail(item);
            }

            player.sendMessage(Text.literal("§cNo puedes hacer eso"), true);

            return TypedActionResult.fail(item);
        }));
    }
}
