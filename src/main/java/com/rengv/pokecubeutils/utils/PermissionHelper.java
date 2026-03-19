package com.rengv.pokecubeutils.utils;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class PermissionHelper {
    public static boolean hasCommandPermission(ServerCommandSource source, String permissionNode) {

        if (source.getEntity() instanceof ServerPlayerEntity player) {
            return Permissions.check(player, permissionNode, false);
        }

        return true;
    }
}
