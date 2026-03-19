package com.rengv.pokecubeutils.gui;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.PlayerData;
import com.rengv.pokecubeutils.config.PlayerList;
import com.rengv.pokecubeutils.utils.EventManager;
import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class KitGUI extends ScreenHandler {
    private final ServerPlayerEntity player;

    private final SimpleInventory inventory = new SimpleInventory(54);

    public KitGUI(int syncId, PlayerInventory inv) {
        super(ScreenHandlerType.GENERIC_9X6, syncId);

        this.player = (ServerPlayerEntity) inv.player;

        for (int i = 0; i < 54; i++) {
            if(i >= 45) {
                this.addSlot(new LockedSlot(inventory, i, 0, 0)); // botones
            } else {
                this.addSlot(new Slot(inventory, i, 0, 0)); // normales
            }
        }

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 0, 0));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inv, col, 0, 0));
        }

        build();
    }

    private void build() {
        if(EventManager.kitItems != null) {
            loadInventory(inventory);
        }

        for (int i = 45; i < 54; i++) {
            ItemStack filler = new ItemStack(Items.BLACK_STAINED_GLASS_PANE);
            filler.set(DataComponentTypes.CUSTOM_NAME, Utils.format(" "));
            this.slots.get(i).setStack(filler);
        }

        ItemStack save = new ItemStack(Items.LIME_DYE);
        save.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&a&lGuardar Kit"));
        this.slots.get(45).setStack(save);

        ItemStack give = new ItemStack(Items.CHEST);
        give.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&e&lEntregar Kit"));
        this.slots.get(53).setStack(give);
    }

    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        return super.insertItem(stack, startIndex, endIndex, fromLast);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        Slot slot = this.slots.get(index);

        if (!slot.hasStack()) return ItemStack.EMPTY;

        ItemStack stack = slot.getStack();
        ItemStack copy = stack.copy();

        if (index < 54) {
            if (!this.insertItem(stack, 54, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (!this.insertItem(stack, 0, 45, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }

        return copy;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory == this.inventory && slot.getIndex() < 45;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void onSlotClick(int slot, int button, SlotActionType action, PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity sp)) return;

        if (slot == 45) {
            saveInventory(inventory);
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &bSe ha guardado los items del Kit."));
            return;
        }

        if (slot == 53) {
            for (Map.Entry<UUID, PlayerData> p : PlayerList.players.entrySet()) {
                ServerPlayerEntity target = PokeCubeUtils.SERVER.getPlayerManager().getPlayer(p.getKey());
                if (target == null) continue;

                for (int i = 0; i < 45; i++) {
                    ItemStack stack = inventory.getStack(i);
                    if (!stack.isEmpty()) {
                        target.getInventory().insertStack(stack.copy());
                    }
                }
            }

            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &bEl Kit entregado a los jugadores."));
            return;
        }

        if (slot >= 45 && slot < 54) return;

        super.onSlotClick(slot, button, action, player);
    }

    private static void saveInventory(SimpleInventory inv) {
        NbtList list = new NbtList();
        RegistryWrapper.WrapperLookup lookup = PokeCubeUtils.SERVER.getRegistryManager();

        for (int i = 0; i < 45; i++) {
            ItemStack stack = inv.getStack(i);

            if (!stack.isEmpty()) {
                NbtCompound tag = new NbtCompound();
                tag.putInt("slot", i);

                tag.put("item", stack.encode(lookup));
                list.add(tag);
            }
        }

        EventManager.kitItems = list;
    }

    private static void loadInventory(SimpleInventory inv) {
        RegistryWrapper.WrapperLookup lookup = PokeCubeUtils.SERVER.getRegistryManager();
        NbtList list = EventManager.kitItems;

        for (int i = 0; i < list.size(); i++) {
            NbtCompound tag = list.getCompound(i);

            int slot = tag.getInt("slot");
            Optional<ItemStack> stack = ItemStack.fromNbt(lookup, tag.getCompound("item"));

            stack.ifPresent(itemStack -> inv.setStack(slot, itemStack));
        }
    }
}
