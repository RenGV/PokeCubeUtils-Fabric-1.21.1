package com.rengv.pokecubeutils.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PlayerList {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path FILE =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("pokecubeutils")
                    .resolve("players.json");

    private static final Type TYPE = new TypeToken<Map<UUID, PlayerData>>() {}.getType();

    public static Map<UUID, PlayerData> players = new HashMap<>();

    public static void load() {
        try {
            if (!Files.exists(FILE)) {
                save();
                return;
            }

            String json = Files.readString(FILE);

            JsonElement element = JsonParser.parseString(json);
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();

                for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                    UUID uuid = UUID.fromString(entry.getKey());

                    JsonElement value = entry.getValue();

                    PlayerData data = GSON.fromJson(value, PlayerData.class);
                    players.put(uuid, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());

            Files.writeString(
                    FILE,
                    GSON.toJson(players)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
