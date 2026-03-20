package com.rengv.pokecubeutils.mixin;

import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.Config;
import com.rengv.pokecubeutils.config.PlayerList;
import com.rengv.pokecubeutils.utils.EventManager;
import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Shadow
    public abstract Block getBlock();

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void onPlace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        Block block = this.getBlock();
        Identifier id = Registries.BLOCK.getId(block);
        PlayerEntity player = context.getPlayer();


        if(player.getWorld().equals(PokeCubeUtils.EVENT_WORLD) && !EventManager.CAN_BUILD){
            if(PlayerList.players.get(player.getUuid()).isManager()) return;

            player.sendMessage(Utils.format("&cNo puedes hacer eso aquí"), true);
            cir.setReturnValue(ActionResult.FAIL);
        }

        if(Config.disabled_block_place.contains(id) && !player.hasPermissionLevel(2)) {
            ((ServerPlayerEntity)player).networkHandler.sendPacket(new GameMessageS2CPacket(Text.literal("§cNo puedes hacer eso"), true));
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
