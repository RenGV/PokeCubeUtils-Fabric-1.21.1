package com.rengv.pokecubeutils.utils;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.rengv.pokecubeutils.PokeCubeUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EventBackup {
    private static final Path BASE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("PokeCubeUtils");

    private static final Path PLAYER_DATA = BASE.resolve("PlayerData");
    private static final Path POKEMON_DATA = BASE.resolve("PokemonData");

    public static void savePlayerData(ServerPlayerEntity player) throws IOException {
        RegistryWrapper.WrapperLookup lookup = PokeCubeUtils.SERVER.getRegistryManager();

        Files.createDirectories(PLAYER_DATA);

        NbtCompound tag = new NbtCompound();

        // Inventario
        tag.put("Inventory", player.getInventory().writeNbt(new NbtList()));

        // Ender chest
        tag.put("EnderChest", player.getEnderChestInventory().toNbtList(lookup));

        // XP
        tag.putInt("XpLevel", player.experienceLevel);
        tag.putFloat("XpProgress", player.experienceProgress);

        Path file = PLAYER_DATA.resolve(player.getUuidAsString() + ".dat");

        NbtIo.write(tag, file);
    }

    public static void loadPlayerData(ServerPlayerEntity player) throws IOException{
        RegistryWrapper.WrapperLookup lookup = PokeCubeUtils.SERVER.getRegistryManager();

        Path file = PLAYER_DATA.resolve(player.getUuidAsString() + ".dat");

        if (!Files.exists(file)) return;

        NbtCompound tag = NbtIo.read(file);

        player.getInventory().clear();
        player.getEnderChestInventory().clear();

        // Inventario
        player.getInventory().readNbt(tag.getList("Inventory", 10));

        // Ender chest
        player.getEnderChestInventory().readNbtList(tag.getList("EnderChest", 10), lookup);

        // XP
        player.experienceLevel = tag.getInt("XpLevel");
        player.experienceProgress = tag.getFloat("XpProgress");

        boolean success = !player.getInventory().isEmpty() || tag.getList("Inventory", 10).isEmpty();

        if (success) {
            Files.delete(file);
        } else {
            PokeCubeUtils.LOGGER.error("No se pudo restaurar inventario de " + player.getName().getString());
        }
    }

    public static void savePokemon(ServerPlayerEntity player) throws IOException {
        DynamicRegistryManager drm = PokeCubeUtils.SERVER.getRegistryManager();

        Files.createDirectories(POKEMON_DATA);

        NbtCompound tag = new NbtCompound();

        var storage = Cobblemon.INSTANCE.getStorage();

        // Equipo
        var party = storage.getParty(player);
        tag.put("Party", party.saveToNBT(new NbtCompound(), drm));

        // PC
        var pc = storage.getPC(player);
        tag.put("PC", pc.saveToNBT(new NbtCompound(), drm));

        Path file = POKEMON_DATA.resolve(player.getUuidAsString() + ".dat");

        NbtIo.write(tag, file);
    }

    public static void loadPokemon(ServerPlayerEntity player) throws IOException{
        DynamicRegistryManager drm = PokeCubeUtils.SERVER.getRegistryManager();

        Path file = POKEMON_DATA.resolve(player.getUuidAsString() + ".dat");

        if (!Files.exists(file)) return;

        NbtCompound tag = NbtIo.read(file);

        var storage = Cobblemon.INSTANCE.getStorage();

        // Equipo
        var party = storage.getParty(player);
        party.clearParty();
        var partyTag = tag.getCompound("Party");

        for (int slot = 0; slot < 6; slot++) {
            String key = "Slot" + slot;

            if (!partyTag.contains(key)) continue;

            NbtCompound pokeTag = partyTag.getCompound(key);

            Pokemon pokemon = new Pokemon();
            pokemon.loadFromNBT(drm, pokeTag);

            party.set(slot, pokemon);
        }

        // PC
        var pc = storage.getPC(player);
        pc.clearPC();
        pc.loadFromNBT(tag.getCompound("PC"), drm);
    }
}
