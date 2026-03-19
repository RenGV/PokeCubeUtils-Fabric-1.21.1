package com.rengv.pokecubeutils.utils;

import com.rengv.pokecubeutils.PokeCubeUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.nio.file.Path;
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

    public static Text format(String line) {
        return Text.literal(line.replace('&', '§'));
    }

    public static ServerWorld getWorld(String worldPath) {
        String idString;

        if (worldPath.equals("world")) {
            idString = "minecraft:overworld";
        } else if (worldPath.endsWith("DIM-1")) {
            idString = "minecraft:the_nether";
        } else if (worldPath.endsWith("DIM1")) {
            idString = "minecraft:the_end";
        } else {
            idString = worldPath.replaceFirst("world/", "").replace("/", ":");
        }

        Identifier id = Identifier.of(idString);
        RegistryKey<World> key = RegistryKey.of(RegistryKeys.WORLD, id);

        return PokeCubeUtils.SERVER.getWorld(key);
    }

    public static String getWorldPath(ServerWorld world) {
        Identifier id = world.getRegistryKey().getValue();
        String idString = id.toString();

        if (idString.equals("minecraft:overworld")) {
            return "world";
        } else if (idString.equals("minecraft:the_nether")) {
            return "world/DIM-1";
        } else if (idString.equals("minecraft:the_end")) {
            return "world/DIM1";
        } else {
            return "world/" + idString.replace(":", "/");
        }
    }

    public static ItemStack getItemStack(String id, int quantity) {
        Identifier identifier = Identifier.tryParse(id);
        if(identifier == null){
            return ItemStack.EMPTY;
        }

        Item item = Registries.ITEM.get(identifier);

        if(item == null || item == Registries.ITEM.get(Identifier.of("minecraft", "air"))){
            return ItemStack.EMPTY;
        }

        return new ItemStack(item, quantity);
    }

    public static boolean isInventoryEmpty(ServerPlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (!stack.isEmpty()) return false;
        }

        for (ItemStack stack : player.getInventory().armor) {
            if (!stack.isEmpty()) return false;
        }

        for (ItemStack stack : player.getInventory().offHand) {
            if (!stack.isEmpty()) return false;
        }

        return true;
    }
}
