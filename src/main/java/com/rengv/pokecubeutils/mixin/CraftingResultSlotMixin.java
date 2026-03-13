package com.rengv.pokecubeutils.mixin;

import com.rengv.pokecubeutils.config.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin {
    @Inject(method = "onTakeItem", at = @At("HEAD"), cancellable = true)
    private void onTakeItem(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        if (player.getWorld().isClient) return;

        Identifier id = Registries.ITEM.getId(stack.getItem());

        if (Config.disabled_craft.contains(id) && !player.hasPermissionLevel(2)) {
            player.sendMessage(Text.literal("§cNo puedes craftear eso."));

            stack.setCount(0);

            ci.cancel();
        }
    }

    @Inject(method = "takeStack", at = @At("HEAD"), cancellable = true)
    private void onTakeStack(int amount, CallbackInfoReturnable<ItemStack> cir) {
        CraftingResultSlot slot = (CraftingResultSlot) (Object) this;
        ItemStack stack = slot.getStack();

        if (stack.isEmpty()) return;

        Identifier id = Registries.ITEM.getId(stack.getItem());

        // Verificar si está bloqueado
        if (Config.disabled_craft.contains(id)) {
            // Devolver ItemStack vacío para cancelar
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Inject(method = "onCrafted(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private void onCrafted(ItemStack stack, CallbackInfo ci) {
        if (stack.isEmpty()) return;

        Identifier id = Registries.ITEM.getId(stack.getItem());

        if (Config.disabled_craft.contains(id)) {
            // Vaciar el stack
            stack.setCount(0);
            ci.cancel();
        }
    }

    @Inject(method = "onCrafted(Lnet/minecraft/item/ItemStack;I)V", at = @At("HEAD"), cancellable = true)
    private void onCraftedWithAmount(ItemStack stack, int amount, CallbackInfo ci) {
        if (stack.isEmpty()) return;

        Identifier id = Registries.ITEM.getId(stack.getItem());

        if (Config.disabled_craft.contains(id)) {
            stack.setCount(0);
            ci.cancel();
        }
    }
}
