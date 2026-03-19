package com.rengv.pokecubeutils.gui;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.PlayerData;
import com.rengv.pokecubeutils.config.PlayerList;
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
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayersManagerGUI extends ScreenHandler {
    private final ServerPlayerEntity player;

    private final SimpleInventory inventory = new SimpleInventory(54);

    public PlayersManagerGUI(int syncId, PlayerInventory inv) {
        super(ScreenHandlerType.GENERIC_9X6, syncId);

        this.player = (ServerPlayerEntity) inv.player;

        for (int i = 0; i < 54; i++) {
            this.addSlot(new LockedSlot(inventory, i, 0, 0));
        }

        build();
    }

    private void build() {
        for (int i = 0; i < 54; i++) {
            this.slots.get(i).setStack(createFiller());
        }

        int slot = 0;

        for(Map.Entry<UUID, PlayerData> p : PlayerList.players.entrySet()) {
            if(p.getValue().isManager()) continue;

            UUID uuid = p.getKey();
            String name = p.getValue().getName();
            ServerPlayerEntity spe = PokeCubeUtils.SERVER.getPlayerManager().getPlayer(uuid);

            if(spe == null) continue;

            ItemStack head = new ItemStack(Items.PLAYER_HEAD);
            head.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&e&l" + name));
            head.set(DataComponentTypes.PROFILE, new ProfileComponent(spe.getGameProfile()));
            List<Text> headLore = List.of(
                    Utils.format(""),
                    Utils.format("&aHaz click para administrar jugador")
            );
            head.set(DataComponentTypes.LORE, new LoreComponent(headLore));

            this.slots.get(slot).setStack(head);
            slot++;
        }
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

        int slotAction = 0;

        for(Map.Entry<UUID, PlayerData> p : PlayerList.players.entrySet()) {
            if(p.getValue().isManager()) continue;

            if(slot == slotAction) {
                UUID uuid = p.getKey();
                String name = p.getValue().getName();
                ServerPlayerEntity spe = PokeCubeUtils.SERVER.getPlayerManager().getPlayer(uuid);

                if(spe == null) return;
                player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                        (syncId, inv, pe) -> new PlayerManagerGUI(syncId, inv, spe),
                        Utils.format("&9&lAcciones para &e&l" + name)
                ));

                break;
            }
            slotAction++;
        }
    }

    private ItemStack createFiller() {
        ItemStack filler = new ItemStack(Items.BLACK_STAINED_GLASS_PANE);
        filler.set(DataComponentTypes.CUSTOM_NAME, Utils.format(" "));
        return filler;
    }
}
