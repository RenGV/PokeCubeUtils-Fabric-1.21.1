package com.rengv.pokecubeutils.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import me.lucko.fabric.api.permissions.v0.Permissions;

public class PermissionHelper {
    public static final String INFINITE_STAMINA_PERMISSION = "pokecube.infinite.ride";

    public static boolean hasInfiniteStamina(PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return false;
        }

        // Usa la API de Fabric Permissions (compatible con LuckPerms)
        return Permissions.check(serverPlayer, INFINITE_STAMINA_PERMISSION, false);
    }
}
