package com.rengv.pokecubeutils.utils;

import com.rengv.pokecubeutils.PokeCubeUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

import static net.minecraft.util.Formatting.strip;

public class Utils {
    public static boolean hasEnoughTokens(String playerName, int amount) {
        if (playerName == null || amount <= 0) return false;
        if (PokeCubeUtils.SERVER == null) return false;

        ServerPlayerEntity player = PokeCubeUtils.SERVER.getPlayerManager().getPlayer(playerName);

        if (player == null) return false;

        // Token template
        ItemStack token = new ItemStack(Items.SUNFLOWER);
        token.set(DataComponentTypes.CUSTOM_NAME, Text.literal("§e§lFicha"));
        token.set(DataComponentTypes.LORE, new LoreComponent(List.of(
                Text.literal(""),
                Text.literal("§7Úsalo en el Casino")
        )));

        Text templateName = token.get(DataComponentTypes.CUSTOM_NAME);
        LoreComponent templateLore = token.get(DataComponentTypes.LORE);

        int found = 0;

        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() != token.getItem()) continue;

            Text stackName = stack.get(DataComponentTypes.CUSTOM_NAME);
            if (templateName != null) {
                if (stackName == null) continue;
                if (!strip(stackName.getString())
                        .equals(strip(templateName.getString()))) continue;
            }

            if (templateLore != null) {
                LoreComponent stackLore = stack.get(DataComponentTypes.LORE);
                if (stackLore == null) continue;
                if (!stripLore(stackLore).equals(stripLore(templateLore))) continue;
            }

            found += stack.getCount();
            if (found >= amount) return true;
        }

        return false;
    }

    private static List<String> stripLore(LoreComponent lore) {
        return lore.lines().stream()
                .map(line -> strip(line.getString()))
                .toList();
    }
}
