package com.rengv.pokecubeutils.gui;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonMechanics;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.bossbar.EventBossbar;
import com.rengv.pokecubeutils.config.Config;
import com.rengv.pokecubeutils.config.PlayerData;
import com.rengv.pokecubeutils.config.PlayerList;
import com.rengv.pokecubeutils.config.PosData;
import com.rengv.pokecubeutils.utils.EventBackup;
import com.rengv.pokecubeutils.utils.EventManager;
import com.rengv.pokecubeutils.utils.Utils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EventGUI extends ScreenHandler {
    private final ServerPlayerEntity player;

    private final SimpleInventory inventory = new SimpleInventory(27);

    public EventGUI(int syncId, PlayerInventory inv) {
        super(ScreenHandlerType.GENERIC_9X3, syncId);

        this.player = (ServerPlayerEntity) inv.player;

        for (int i = 0; i < 27; i++) {
            this.addSlot(new LockedSlot(inventory, i, 0, 0));
        }

        build();
    }

    private void build() {
        for (int i = 0; i < 27; i++) {
            this.slots.get(i).setStack(createFiller());
        }

        ItemStack ride = new ItemStack(Items.LEAD);
        ride.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&e&lMontar Pokémon"));
        List<Text> rideLore = List.of(
                Utils.format("&7Haz click para habilitar/deshabilitar"),
                Utils.format("&7la montura de los jugadores con sus pokémon."),
                Utils.format(""),
                Utils.format(EventManager.CAN_RIDE ? "&a&lHabilitado" : "&c&lDeshabilitado")
        );
        ride.set(DataComponentTypes.LORE, new LoreComponent(rideLore));
        this.slots.get(0).setStack(ride);

        ItemStack pokemon = Utils.getItemStack("cobblemon:poke_ball", 1);
        pokemon.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&e&lUsar Pokémon"));
        List<Text> pokemonLore = List.of(
                Utils.format("&7Haz click para habilitar/deshabilitar"),
                Utils.format("&7el uso de los pokémon en el evento."),
                Utils.format(""),
                Utils.format(EventManager.CAN_USE_POKEMON ? "&a&lHabilitado" : "&c&lDeshabilitado")
        );
        pokemon.set(DataComponentTypes.LORE, new LoreComponent(pokemonLore));
        this.slots.get(1).setStack(pokemon);

        ItemStack placeBlock = new ItemStack(Items.GRASS_BLOCK);
        placeBlock.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&2&lConstrucción"));
        List<Text> placeBlockLore = List.of(
                Utils.format("&7Haz click para des/habilitar que"),
                Utils.format("&7los jugadores puedan construir."),
                Utils.format(""),
                Utils.format(EventManager.CAN_BUILD ? "&a&lHabilitado" : "&c&lDeshabilitado")
        );
        placeBlock.set(DataComponentTypes.LORE, new LoreComponent(placeBlockLore));
        this.slots.get(2).setStack(placeBlock);

        ItemStack breakBlock = new ItemStack(Items.DIRT);
        breakBlock.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&4&lDestrucción"));
        List<Text> breakBlockLore = List.of(
                Utils.format("&7Haz click para des/habilitar que"),
                Utils.format("&7los jugadores puedan destruir."),
                Utils.format(""),
                Utils.format(EventManager.CAN_BREAK ? "&a&lHabilitado" : "&c&lDeshabilitado")
        );
        breakBlock.set(DataComponentTypes.LORE, new LoreComponent(breakBlockLore));
        this.slots.get(3).setStack(breakBlock);

        ItemStack pvp = new ItemStack(Items.IRON_SWORD);
        pvp.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&3&lPVP"));
        List<Text> pvpLore = List.of(
                Utils.format("&7Haz click para des/habilitar que"),
                Utils.format("&7los jugadores se puedan atacar."),
                Utils.format(""),
                Utils.format(EventManager.PVP ? "&a&lHabilitado" : "&c&lDeshabilitado")
        );
        pvp.set(DataComponentTypes.LORE, new LoreComponent(pvpLore));
        this.slots.get(4).setStack(pvp);

        ItemStack pve = new ItemStack(Items.GOLDEN_SWORD);
        pve.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&5&lPVE"));
        List<Text> pveLore = List.of(
                Utils.format("&7Haz click para des/habilitar que"),
                Utils.format("&7los jugadores puedan atacar mobs."),
                Utils.format(""),
                Utils.format(EventManager.PVE ? "&a&lHabilitado" : "&c&lDeshabilitado")
        );
        pve.set(DataComponentTypes.LORE, new LoreComponent(pveLore));
        this.slots.get(5).setStack(pve);

        ItemStack infiniteRide = new ItemStack(Items.SADDLE);
        infiniteRide.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&6&lRide Infinito"));
        List<Text> infiniteRideLore = List.of(
                Utils.format("&7Haz click para des/habilitar la montura"),
                Utils.format("&7infinita en los Pokémon de los jugadores."),
                Utils.format(""),
                Utils.format(EventManager.INFINITE_RIDE_STAMINE ? "&a&lHabilitado" : "&c&lDeshabilitado")
        );
        infiniteRide.set(DataComponentTypes.LORE, new LoreComponent(infiniteRideLore));
        this.slots.get(6).setStack(infiniteRide);

        ItemStack hunger = new ItemStack(Items.COOKED_BEEF);
        hunger.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&f&lHambre"));
        List<Text> hungerLore = List.of(
                Utils.format("&7Haz click para des/habilitar el hambre"),
                Utils.format("&7en los jugadores del evento."),
                Utils.format(""),
                Utils.format(EventManager.HUNGER ? "&a&lHabilitado" : "&c&lDeshabilitado")
        );
        hunger.set(DataComponentTypes.LORE, new LoreComponent(hungerLore));
        this.slots.get(7).setStack(hunger);

        ItemStack retrievePokemon = Utils.getItemStack("cobblemon:citrine_ball", 1);
        retrievePokemon.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&e&lRecuperar Pokémon"));
        List<Text> retrievePokemonLore = List.of(
                Utils.format("&7Haz click para devolver todos"),
                Utils.format("&7los pokémon a sus pokeballs.")
        );
        retrievePokemon.set(DataComponentTypes.LORE, new LoreComponent(retrievePokemonLore));
        this.slots.get(8).setStack(retrievePokemon);

        ItemStack spawn = new ItemStack(Items.BEACON);
        spawn.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&b&lEstablecer Spawn"));
        List<Text> spawnLore = List.of(
                Utils.format("&7Haz click para establecer el punto de"),
                Utils.format("&7aparición de los jugadores que se unan.")
        );
        spawn.set(DataComponentTypes.LORE, new LoreComponent(spawnLore));
        this.slots.get(9).setStack(spawn);

        ItemStack player = new ItemStack(Items.PLAYER_HEAD);
        player.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&9&lAdministrar jugadores"));
        List<Text> playerLore = List.of(
                Utils.format("&7Haz click para realizar"),
                Utils.format("&7diferentes acciones a un"),
                Utils.format("&7jugador del evento.")
        );
        player.set(DataComponentTypes.LORE, new LoreComponent(playerLore));
        this.slots.get(10).setStack(player);

        ItemStack kit = new ItemStack(Items.DIAMOND_CHESTPLATE);
        kit.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&4&lKit para jugadores"));
        List<Text> kitLore = List.of(
                Utils.format("&7Haz click para entregar"),
                Utils.format("&7objetos a los jugadores.")
        );
        kit.set(DataComponentTypes.LORE, new LoreComponent(kitLore));
        this.slots.get(11).setStack(kit);

        ItemStack clear = new ItemStack(Items.BARRIER);
        clear.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&7&lBorrar inventario"));
        List<Text> clearLore = List.of(
                Utils.format("&7Haz click para borrar el"),
                Utils.format("&7inventario de los jugadores.")
        );
        clear.set(DataComponentTypes.LORE, new LoreComponent(clearLore));
        this.slots.get(12).setStack(clear);

        ItemStack invite = new ItemStack(Items.NAME_TAG);
        invite.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&5&lInvitar jugadores"));
        List<Text> inviteLore = List.of(
                Utils.format("&7Haz click para permitir que"),
                Utils.format("&7más jugadores puedan entrar"),
                Utils.format("&7al evento.")
        );
        invite.set(DataComponentTypes.LORE, new LoreComponent(inviteLore));
        this.slots.get(13).setStack(invite);

        ItemStack gamemode = new ItemStack(Items.CRAFTING_TABLE);
        gamemode.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&6&lCambiar tu Modo de Juego"));
        String gamemodeString = "&a&lSupervivencia";
        GameMode gameMode = EventManager.REAL_GAMEMODE.getOrDefault(this.player.getUuid(), this.player.interactionManager.getGameMode());
        switch (gameMode) {
            case SURVIVAL -> gamemodeString = "&a&lSupervivencia";
            case CREATIVE -> gamemodeString = "&c&lCreativo";
            case SPECTATOR -> gamemodeString = "&9&lEspectador";
        }
        List<Text> gamemodeLore = List.of(
                Utils.format("&7Haz click para cambiar tu"),
                Utils.format("&7modo de juego."),
                Utils.format(""),
                Utils.format("&eEstás en modo: " + gamemodeString)
        );
        gamemode.set(DataComponentTypes.LORE, new LoreComponent(gamemodeLore));
        this.slots.get(14).setStack(gamemode);

        ItemStack time = new ItemStack(Items.CLOCK);
        time.set(DataComponentTypes.CUSTOM_NAME, Utils.format("&5&lCambiar tiempo"));
        List<Text> timeLore = List.of(
                Utils.format("&7Haz click para cambiar"),
                Utils.format("&7el tiempo del mundo evento.")
        );
        time.set(DataComponentTypes.LORE, new LoreComponent(timeLore));
        this.slots.get(15).setStack(time);

        ItemStack manage = new ItemStack(EventManager.STARTED ? Items.RED_WOOL : Items.LIME_WOOL);
        manage.set(DataComponentTypes.CUSTOM_NAME, Utils.format(EventManager.STARTED ? "&c&lTerminar evento" : "&a&lEmpezar evento"));
        List<Text> manageLore = List.of(
                Utils.format("&7Haz click para " + (EventManager.STARTED ? "&cterminar" : "&ainiciar")),
                Utils.format("&7el evento y los jugadores se " + (EventManager.STARTED ? "retiren." : "unan."))
        );
        manage.set(DataComponentTypes.LORE, new LoreComponent(manageLore));
        this.slots.get(22).setStack(manage);
    }

    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        return false;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player == this.player;
    }

    @Override
    public void onSlotClick(int slot, int button, SlotActionType action, PlayerEntity player) {
        if(!(player instanceof ServerPlayerEntity sp)) return;

        if(slot == 0) {
            EventManager.CAN_RIDE = !EventManager.CAN_RIDE;
            String enabled = EventManager.CAN_RIDE ? "habilitado" : "deshabilitado";
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas &e" + enabled + " &blas monturas de los Pokémon."));
            build();
        } else if(slot == 1) {
            EventManager.CAN_USE_POKEMON = !EventManager.CAN_USE_POKEMON;
            String enabled = EventManager.CAN_USE_POKEMON ? "habilitado" : "deshabilitado";
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas &e" + enabled + " &bel uso de los Pokémon."));
            build();
        } else if(slot == 2) {
            EventManager.CAN_BUILD = !EventManager.CAN_BUILD;
            String enabled = EventManager.CAN_BUILD ? "habilitado" : "deshabilitado";
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas &e" + enabled + " &bla construcción bloques en los jugadores."));
            build();
        } else if(slot == 3) {
            EventManager.CAN_BREAK = !EventManager.CAN_BREAK;
            String enabled = EventManager.CAN_BREAK ? "habilitado" : "deshabilitado";
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas &e" + enabled + " &bla destrucción de bloques en los jugadores."));
            build();
        } else if(slot == 4) {
            EventManager.PVP = !EventManager.PVP;
            String enabled = EventManager.PVP ? "habilitado" : "deshabilitado";
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas &e" + enabled + " &bel ataque entre los jugadores."));
            build();
        } else if(slot == 5) {
            EventManager.PVE = !EventManager.PVE;
            String enabled = EventManager.PVE ? "habilitado" : "deshabilitado";
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas &e" + enabled + " &bel ataque entre los jugadores y los mobs."));
            build();
        } else if(slot == 6) {
            EventManager.INFINITE_RIDE_STAMINE = !EventManager.INFINITE_RIDE_STAMINE;
            String enabled = EventManager.INFINITE_RIDE_STAMINE ? "habilitado" : "deshabilitado";
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas &e" + enabled + " &bque los jugadores con rango tengan montura infinita."));
            build();
        } else if(slot == 7) {
            EventManager.HUNGER = !EventManager.HUNGER;
            String enabled = EventManager.HUNGER ? "habilitado" : "deshabilitado";
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas &e" + enabled + " &bque los jugadores puedan tener hambre."));
            build();
        } else if(slot == 8) {
            int total = 0;
            for(Map.Entry<UUID, PlayerData> p : PlayerList.players.entrySet()) {
                if(p.getValue().isManager()) continue;

                UUID uuid = p.getKey();

                ServerPlayerEntity spe = PokeCubeUtils.SERVER.getPlayerManager().getPlayer(uuid);

                if(spe == null) continue;

                PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(spe);
                for (int i = 0; i < party.size(); i++) {
                    Pokemon pokemon = party.get(i);

                    if (pokemon != null) pokemon.recall();
                }
                total++;
            }

            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas devuelto todos los Pokémon de " + total + " jugadores a sus pokeballs."));
            build();
        } else if(slot == 9) {
            PosData pos = new PosData(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
            Config.event_coords = pos;
            Config.save();
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas establecido el punto de aparición."));
        } else if(slot == 10) {
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, p) -> new PlayersManagerGUI(syncId, inv),
                    Utils.format("&9&lJugadores en Evento")
            ));
        } else if(slot == 11) {
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, p) -> new KitGUI(syncId, inv),
                    Utils.format("  &5&lEstablecer kit de evento")
            ));
        } else if(slot == 12) {
            int total = 0;
            for(Map.Entry<UUID, PlayerData> p : PlayerList.players.entrySet()) {
                if(p.getValue().isManager()) continue;

                UUID uuid = p.getKey();

                ServerPlayerEntity spe = PokeCubeUtils.SERVER.getPlayerManager().getPlayer(uuid);

                if(spe == null) continue;

                spe.getInventory().clear();
                total++;
            }

            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas eliminado el inventario de " + total + " jugadores."));
            build();
        } else if(slot == 13) {
            if(!EventManager.STARTED) {
                player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &cNo hay ningún evento activo."));
                return;
            }

            if(EventManager.CAN_JOIN_EVENT) {
                player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &aLos jugadores ya se pueden unir al evento."));
                return;
            }

            EventBossbar.start(PokeCubeUtils.SERVER, "&bEvento en curso, usa &e/evento join &bpara unirte" ,60);
            EventManager.CAN_JOIN_EVENT = true;
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas enviado la invitación a los jugadores."));

            ((ServerPlayerEntity) player).closeHandledScreen();
        } else if(slot == 14) {
            GameMode gameMode = EventManager.REAL_GAMEMODE.getOrDefault(player.getUuid(), ((ServerPlayerEntity) player).interactionManager.getGameMode());

            if(gameMode.equals(GameMode.SURVIVAL)) {
                EventManager.REAL_GAMEMODE.put(player.getUuid(), GameMode.CREATIVE);
                player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas cambiado tu modo de juego a &c&lCreativo&b."));
            }
            if(gameMode.equals(GameMode.CREATIVE)) {
                EventManager.REAL_GAMEMODE.put(player.getUuid(), GameMode.SPECTATOR);
                player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas cambiado tu modo de juego a &9&lEspectador&b."));
            }
            if(gameMode.equals(GameMode.SPECTATOR)) {
                EventManager.REAL_GAMEMODE.put(player.getUuid(), GameMode.SURVIVAL);
                player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas cambiado tu modo de juego a &a&lSupervivencia&b."));
            }

            build();
        } else if(slot == 15) {
            if(PokeCubeUtils.EVENT_WORLD instanceof ServerWorld serverWorld) {
                long time = serverWorld.getTimeOfDay() % 24000;

                if(time < 5000) {
                    serverWorld.setTimeOfDay(5000);
                } else if (time < 12000) {
                    serverWorld.setTimeOfDay(12000);
                } else if (time < 15000) {
                    serverWorld.setTimeOfDay(15000);
                } else {
                    serverWorld.setTimeOfDay(0);
                }

                player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas cambiado el tiempo del mundo evento."));
            }
        } else if(slot == 22){
            if (EventManager.STARTED){
                if(EventManager.CAN_JOIN_EVENT) {
                    player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &cRecién ha empezado el evento, no se puede finalizar el evento."));
                    return;
                }

                EventManager.STARTED = false;
                ServerWorld spawnWorld = Utils.getWorld(Config.world_spawn);
                PosData spawnCoords = Config.spawn_coords;

                for(Map.Entry<UUID, PlayerData> p : PlayerList.players.entrySet()) {
                    if(p.getValue().isManager()) continue;

                    UUID uuid = p.getKey();
                    ServerPlayerEntity spe = PokeCubeUtils.SERVER.getPlayerManager().getPlayer(uuid);

                    if(spe == null) continue;

                    try {
                        EventBackup.loadPlayerData(spe);
                        EventBackup.loadPokemon(spe);

                        EventManager.leaveBypass.add(uuid);

                        spe.teleport(spawnWorld, spawnCoords.x, spawnCoords.y, spawnCoords.z, spawnCoords.yaw, spawnCoords.pitch);
                        //spe.getInventory().clear();

                        PlayerList.players.remove(uuid);
                        PlayerList.save();

                        spe.changeGameMode(GameMode.SURVIVAL);
                        EventManager.playerFrozen.remove(uuid);

                        spe.sendMessage(Utils.format("&e¡Gracias por participar del evento!"));
                    } catch (IOException e) {
                        player.sendMessage(Utils.format("&cHa ocurrido un error, no puedes salir el evento. &eAbre un ticket en &9Discord&e."));
                        PokeCubeUtils.LOGGER.error(e.getMessage());
                    }
                }

                ((ServerPlayerEntity) player).closeHandledScreen();
                return;
            }

            EventManager.STARTED = true;
            EventManager.CAN_JOIN_EVENT = true;
            EventBossbar.start(PokeCubeUtils.SERVER, "&a¡Un nuevo evento ha iniciado! &eUsa &b/evento join", 300);
            player.sendMessage(Utils.format("&e&l[&a&lEvento&e&l] &r&bHas empezado el evento."));

            ((ServerPlayerEntity) player).closeHandledScreen();
            build();
        }
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        if(!(player instanceof ServerPlayerEntity spe)) return;

        UUID uuid = spe.getUuid();

        GameMode real = EventManager.REAL_GAMEMODE.remove(uuid);

        if(real != null) {
            spe.changeGameMode(real);
        }
    }

    private ItemStack createFiller() {
        ItemStack filler = new ItemStack(Items.BLACK_STAINED_GLASS_PANE);
        filler.set(DataComponentTypes.CUSTOM_NAME, Utils.format(" "));
        return filler;
    }
}
