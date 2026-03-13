package com.rengv.pokecubeutils.mixin;

import com.rengv.pokecubeutils.config.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    @Shadow
    public DefaultedList<Slot> slots;

    /**
     * Interceptar al inicio de internalOnSlotClick
     * y verificar si es shift+click bloqueado
     */
    @Inject(
            method = "internalOnSlotClick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onInternalSlotClick(
            int slotIndex,
            int button,
            SlotActionType actionType,
            PlayerEntity player,
            CallbackInfo ci
    ) {
        // Solo procesar shift+click (QUICK_MOVE)
        if (actionType != SlotActionType.QUICK_MOVE) return;
        if (player.getWorld().isClient) return;
        if (slotIndex < 0 || slotIndex >= this.slots.size()) return;

        Slot slot = this.slots.get(slotIndex);

        // Solo CraftingResultSlot
        if (!(slot instanceof CraftingResultSlot)) return;

        ItemStack stack = slot.getStack();
        if (stack.isEmpty()) return;

        Identifier id = Registries.ITEM.getId(stack.getItem());

        // Bloquear si está en la lista
        if (Config.disabled_craft.contains(id) && !player.hasPermissionLevel(2)) {
            if (player instanceof ServerPlayerEntity) {
                player.sendMessage(
                        Text.literal("§c¡No puedes craftear: " + id + "!"),
                        true
                );
            }

            // Cancelar todo el evento de click
            ci.cancel();
        }
    }
}
