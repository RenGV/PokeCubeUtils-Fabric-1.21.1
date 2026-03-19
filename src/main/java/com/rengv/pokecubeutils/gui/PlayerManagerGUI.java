package com.rengv.pokecubeutils.gui;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import com.rengv.pokecubeutils.config.PlayerList;
import com.rengv.pokecubeutils.config.PosData;
import com.rengv.pokecubeutils.utils.EventManager;
import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.minecraft.world.TeleportTarget;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerManagerGUI extends ScreenHandler {
    private final ServerPlayerEntity player;
    private final ServerPlayerEntity target;

    private final SimpleInventory inventory = new SimpleInventory(27);

    public PlayerManagerGUI(int syncId, PlayerInventory inv, ServerPlayerEntity target) {
        super(ScreenHandlerType.GENERIC_9X3, syncId);

        this.player = (ServerPlayerEntity) inv.player;
        this.target = target;

        for (int i = 0; i < 27; i++) {
            this.addSlot(new LockedSlot(inventory, i, 0, 0));
        }

        build();
    }

    private void build() {
        for (int i = 0; i < 27; i++) {
            this.slots.get(i).setStack(createFiller());
        }

        ItemStack head = new ItemStack(Items.PLAYER_HEAD);
        head.set(DataComponentTypes.PROFILE, new ProfileComponent(target.getGameProfile()));
        head.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&e&l" + target.getName().getString()));
        this.slots.get(4).setStack(head);

        ItemStack tp = new ItemStack(Items.ENDER_PEARL);
        tp.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&5&lTeletransportarte hacia jugador"));
        List<Text> tpLore = List.of(
                Utils.format("&7Haz click para teletransportarte"),
                Utils.format("&7hacia el jugador.")
        );
        tp.set(DataComponentTypes.LORE, new LoreComponent(tpLore));
        this.slots.get(9).setStack(tp);

        ItemStack tphere = new ItemStack(Items.ENDER_EYE);
        tphere.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&5&lTeletransportarte jugador hacia tí"));
        List<Text> tphereLore = List.of(
                Utils.format("&7Haz click para teletransportar"),
                Utils.format("&7el jugador hacia tí.")
        );
        tphere.set(DataComponentTypes.LORE, new LoreComponent(tphereLore));
        this.slots.get(10).setStack(tphere);


        ItemStack kill = new ItemStack(Items.SKELETON_SKULL);
        kill.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&c&lMatar jugador"));
        List<Text> killLore = List.of(
                Utils.format("&7Haz click para matar"),
                Utils.format("&7al jugador.")
        );
        kill.set(DataComponentTypes.LORE, new LoreComponent(killLore));
        this.slots.get(11).setStack(kill);

        ItemStack freeze = new ItemStack(Items.ICE);
        freeze.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&b&lCongelar jugador"));
        List<Text> freezeLore = List.of(
                Utils.format("&7Haz click para des/congelar"),
                Utils.format("&7al jugador.")
        );
        freeze.set(DataComponentTypes.LORE, new LoreComponent(freezeLore));
        this.slots.get(12).setStack(freeze);

        ItemStack clear = new ItemStack(Items.BARRIER);
        clear.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&6&lLimpiar inventario"));
        List<Text> clearLore = List.of(
                Utils.format("&7Haz click para vaciar"),
                Utils.format("&7el inventario del jugador.")
        );
        clear.set(DataComponentTypes.LORE, new LoreComponent(clearLore));
        this.slots.get(13).setStack(clear);


        ItemStack kick = new ItemStack(Items.RED_WOOL);
        kick.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&4&lExpulsar jugador"));
        List<Text> kickLore = List.of(
                Utils.format("&7Haz click para expulsar"),
                Utils.format("&7al jugador del evento.")
        );
        kick.set(DataComponentTypes.LORE, new LoreComponent(kickLore));
        this.slots.get(14).setStack(kick);
    }

    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        return false;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player == this.player;
    }

    @Override
    public void onSlotClick(int slot, int button, SlotActionType action, PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity sp)) return;

        if(slot == 9) {
            this.player.teleport(target.getServerWorld(), target.getX(), target.getY(), target.getZ(), target.getYaw(), target.getPitch());
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bTe has teletransportado a &e" + target.getName().getString() + "&b."));
        } else if(slot == 10) {
            target.teleport(sp.getServerWorld(), sp.getX(), sp.getY(), sp.getZ(), sp.getYaw(), sp.getPitch());
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas teletransportado a &e" + target.getName().getString() + " &bhacía ti."));
        } else if(slot == 11) {
            target.kill();
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas matado a &e" + target.getName().getString() + "&b."));
        } else if(slot == 12) {
            if(EventManager.playerFrozen.contains(target.getUuid())) {
                EventManager.playerFrozen.remove(target.getUuid());
                player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas descongelado a &e" + target.getName().getString() + "&b."));
            } else {
                EventManager.playerFrozen.add(target.getUuid());
                player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas congelado a &e" + target.getName().getString() + "&b."));
            }
        } else if(slot == 13) {
            target.getInventory().clear();
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas borrado el inventario de &e" + target.getName().getString() + "&b."));
        } else if(slot == 14) {
            EventManager.leaveBypass.add(target.getUuid());

            ServerWorld spawnWorld = Utils.getWorld(Config.world_spawn);
            PosData spawnCoords = Config.spawn_coords;

            target.teleport(spawnWorld, spawnCoords.x, spawnCoords.y, spawnCoords.z, spawnCoords.yaw, spawnCoords.pitch);
            target.getInventory().clear();
            PlayerList.players.remove(target.getUuid());
            PlayerList.save();

            target.changeGameMode(GameMode.SURVIVAL);
            EventManager.playerFrozen.remove(target.getUuid());

            target.sendMessage(Utils.format("&c¡Has sido expulsado del evento!"));
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas expulsado a &e" + target.getName().getString() + "&b del evento."));
        }
    }

    private ItemStack createFiller() {
        ItemStack filler = new ItemStack(Items.BLACK_STAINED_GLASS_PANE);
        filler.set(DataComponentTypes.CUSTOM_NAME, Utils.format(" "));
        return filler;
    }
}

