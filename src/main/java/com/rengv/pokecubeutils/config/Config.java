package com.rengv.pokecubeutils.config;

import com.google.gson.*;
import com.rengv.pokecubeutils.PokeCubeUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Config {
    private Config() {}

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().registerTypeAdapter(Identifier.class, new IdentifierAdapter()).create();

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("PokeCubeUtils.json");

    public static Set<Identifier> disabled_craft = new HashSet<>();
    public static Set<Identifier> disabled_block_interact = new HashSet<>();
    public static Set<Identifier> disabled_block_place = new HashSet<>();
    public static Set<Identifier> disabled_block_break = new HashSet<>();
    public static Set<Identifier> disabled_entity_interact = new HashSet<>();
    public static Set<Identifier> disabled_entity_attack = new HashSet<>();
    public static Set<Identifier> disabled_item_use = new HashSet<>();
    //public static Map<Identifier, Set<ChunkPos>> disabled_mount_chunks = new HashMap<>();

    private static class ConfigData {
        Set<Identifier> disabled_craft;
        Set<Identifier> disabled_block_interact;
        Set<Identifier> disabled_block_place;
        Set<Identifier> disabled_block_break;
        Set<Identifier> disabled_entity_interact;
        Set<Identifier> disabled_entity_attack;
        Set<Identifier> disabled_item_use;
        //Map<Identifier, Set<ChunkPos>> disabled_mount_chunks;
    }

    public static void load() {
        try {
            if(!Files.exists(CONFIG_PATH)){
                save();
                PokeCubeUtils.sendDebugMessage("Config created: " + CONFIG_PATH);
                return;
            }

            ConfigData data = GSON.fromJson(
                    Files.readString(CONFIG_PATH),
                    ConfigData.class
            );

            if(data != null) {
                apply(data);
            }
        } catch (Exception e) {
            PokeCubeUtils.sendDebugMessage("Failed to load config, using defaults  " + e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(
                    CONFIG_PATH,
                    GSON.toJson(toData())
            );
        } catch (Exception e) {
            PokeCubeUtils.sendDebugMessage("Failed to save config  " + e);
        }
    }

    public static void reload() {
        load();
        PokeCubeUtils.sendDebugMessage("Config reloaded");
    }

    private static void apply(ConfigData d) {
        disabled_craft = d.disabled_craft;
        disabled_block_interact = d.disabled_block_interact;
        disabled_block_place = d.disabled_block_place;
        disabled_block_break = d.disabled_block_break;
        disabled_entity_interact = d.disabled_entity_interact;
        disabled_entity_attack = d.disabled_entity_attack;
        disabled_item_use = d.disabled_item_use;

        /*disabled_mount_chunks = d.disabled_mount_chunks != null
                ? d.disabled_mount_chunks
                : new HashMap<>();*/
    }

    /*private static <T> Set<T> safeSet(Set<T> in) {
        return in != null ? in : new HashSet<>();
    }*/

    private static ConfigData toData() {
        ConfigData d = new ConfigData();

        d.disabled_craft = disabled_craft;
        d.disabled_block_interact = disabled_block_interact;
        d.disabled_block_place = disabled_block_place;
        d.disabled_block_break = disabled_block_break;
        d.disabled_entity_interact = disabled_entity_interact;
        d.disabled_entity_attack = disabled_entity_attack;
        d.disabled_item_use = disabled_item_use;
        //d.disabled_mount_chunks = disabled_mount_chunks;

        return d;
    }

    /*public static class ChunkPosAdapter implements JsonSerializer<ChunkPos>, JsonDeserializer<ChunkPos> {
        @Override
        public JsonElement serialize(
                ChunkPos src, Type typeOfSrc, JsonSerializationContext ctx
        ) {
            return new JsonPrimitive(src.x + "," + src.z);
        }

        @Override
        public ChunkPos deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext ctx
        ) throws JsonParseException {
            String[] split = json.getAsString().split(",");
            return new ChunkPos(
                    Integer.parseInt(split[0].trim()),
                    Integer.parseInt(split[1].trim())
            );
        }
    }*/
}
