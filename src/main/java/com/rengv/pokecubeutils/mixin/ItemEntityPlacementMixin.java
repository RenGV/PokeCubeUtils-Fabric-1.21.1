package com.rengv.pokecubeutils.mixin;

import com.rengv.pokecubeutils.config.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemEntityPlacementMixin {
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void onUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        if (world.isClient()) return;

        ItemStack stack = context.getStack();
        Item item = stack.getItem();
        Identifier id = Registries.ITEM.getId(item);
        PlayerEntity player = context.getPlayer();

        if (Config.disabled_block_place.contains(id) && !player.hasPermissionLevel(2)) {
            ((ServerPlayerEntity)player).networkHandler.sendPacket(new GameMessageS2CPacket(Text.literal("§cNo puedes hacer eso"), true));

            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
