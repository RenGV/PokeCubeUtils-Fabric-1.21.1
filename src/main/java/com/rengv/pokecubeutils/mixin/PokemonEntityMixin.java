package com.rengv.pokecubeutils.mixin;

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.utils.PermissionHelper;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PokemonEntity.class, remap = false)
public abstract class PokemonEntityMixin {
    /*@Shadow
    private static TrackedData<Float> RIDE_STAMINA;

    @Inject(
            method = "tick",
            at = @At("TAIL"),
            remap = false
    )
    private void onTick(CallbackInfo ci) {
        PokemonEntity pokemon = (PokemonEntity) (Object) this;

        if (pokemon.getFirstPassenger() instanceof PlayerEntity player) {
            if (PermissionHelper.hasInfiniteStamina(player)) {
                PokeCubeUtils.LOGGER.info("3");
                pokemon.getDataTracker().set(RIDE_STAMINA, 1.0f);
            }
        }
    }*/

    @Inject(
            method = "tick",
            at = @At("TAIL"),
            remap = false
    )
    private void pokecubeutils$infiniteRideStamina(CallbackInfo ci) {
        PokemonEntity pokemon = (PokemonEntity) (Object) this;

        if (!(pokemon.getFirstPassenger() instanceof PlayerEntity player)) return;
        if (!PermissionHelper.hasInfiniteStamina(player)) return;
        if (pokemon.getWorld().isClient()) return;

        pokemon.ifRidingAvailable((behaviour, settings, state) -> {
            state.getStamina().set(1.0f, true);
            pokemon.getDataTracker().set(PokemonEntity.getRIDE_STAMINA(), 1.0f);

            PokeCubeUtils.LOGGER.info(
                    "[InfiniteStamina] {} stamina locked at 1.0",
                    pokemon.getPokemon().getSpecies().getName()
            );
            return null;
        });
    }
}
