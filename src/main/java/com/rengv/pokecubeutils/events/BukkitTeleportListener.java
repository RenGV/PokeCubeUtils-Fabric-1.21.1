package com.rengv.pokecubeutils.events;


import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.config.PlayerList;
import com.rengv.pokecubeutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

import static com.rengv.pokecubeutils.utils.EventManager.enterBypass;
import static com.rengv.pokecubeutils.utils.EventManager.leaveBypass;

public class BukkitTeleportListener implements Listener {

    private String eventWorldName = null;

    public BukkitTeleportListener() {
        // Obtener el nombre del mundo evento en formato Bukkit (ej: "world_event")
        if(PokeCubeUtils.EVENT_WORLD == null) {
            PokeCubeUtils.LOGGER.warn("The world of events was not defined");
            return;
        }

        this.eventWorldName = "world/" + PokeCubeUtils.EVENT_WORLD.getRegistryKey().getValue().toString().replace(":", "/");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if(eventWorldName == null) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;

        World fromWorld = from.getWorld();
        World toWorld = to.getWorld();
        if (fromWorld == null || toWorld == null) return;

        boolean destIsEvent = toWorld.getName().equals(eventWorldName);
        boolean originIsEvent = fromWorld.getName().equals(eventWorldName);

        // Bloquear ENTRAR al mundo evento
        if (destIsEvent && !PlayerList.players.containsKey(uuid)) {
            if (enterBypass.remove(uuid)) return;

            event.setCancelled(true);
            player.sendMessage(Utils.format("&cNo puedes entrar al mundo del evento.").getString());

            return;
        }

        // Bloquear SALIR del mundo evento
        if (originIsEvent && !destIsEvent && PlayerList.players.containsKey(uuid)) {
            if (leaveBypass.remove(uuid)) return;

            event.setCancelled(true);
            player.sendMessage(Utils.format("&cNo puedes salir del mundo del evento. Usa: &e/evento leave").getString());
        }
    }
}
