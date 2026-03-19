package com.rengv.pokecubeutils.commands;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.brigadier.CommandDispatcher;
import com.rengv.pokecubeutils.PokeCubeUtils;
import com.rengv.pokecubeutils.utils.PermissionHelper;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomShinyCommand {
    private static final Random random = new Random();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("randomshiny")
                        .requires(source -> PermissionHelper.hasCommandPermission(source, "pokecube.command.randomshiny"))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();

                                    try {
                                        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                        Pokemon pokemon = getRandomNonLegendaryPokemon();

                                        if(pokemon == null) {
                                            source.sendError(Text.literal("§cNo se pudo encontrar un Pokémon válido."));
                                            return 0;
                                        }

                                        pokemon.setShiny(true);

                                        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
                                        party.add(pokemon);

                                        source.sendMessage(Text.literal("§a¡Se ha dado un " + pokemon.getSpecies().getName() + " shiny a " + player.getName().getString() + "!"));

                                        return 1;
                                    }catch (Exception e) {
                                        source.sendError(Text.literal("§cError al ejecutar el comando: " + e.getMessage()));
                                        PokeCubeUtils.sendErrorMessage(e.getMessage());
                                        return 0;
                                    }
                                })
                        )
        );
    }

    private static Pokemon getRandomNonLegendaryPokemon() {
        List<Species> allSpecies = new ArrayList<>();

        PokemonSpecies.getImplemented().forEach(species -> {
            String speciesName = species.getName().toLowerCase();

            if(!EXCLUDED_POKEMON.contains(speciesName)) {
                allSpecies.add(species);
            }
        });

        if(allSpecies.isEmpty()) return null;

        Species randomSpecies = allSpecies.get(random.nextInt(allSpecies.size()));

        return randomSpecies.create(1);
    }

    private static final List<String> EXCLUDED_POKEMON = new ArrayList<>();

    static {
        // Legendarios Gen 1
        EXCLUDED_POKEMON.add("articuno");
        EXCLUDED_POKEMON.add("zapdos");
        EXCLUDED_POKEMON.add("moltres");
        EXCLUDED_POKEMON.add("mewtwo");

        // Míticos Gen 1
        EXCLUDED_POKEMON.add("mew");

        // Legendarios Gen 2
        EXCLUDED_POKEMON.add("raikou");
        EXCLUDED_POKEMON.add("entei");
        EXCLUDED_POKEMON.add("suicune");
        EXCLUDED_POKEMON.add("lugia");
        EXCLUDED_POKEMON.add("ho-oh");

        // Míticos Gen 2
        EXCLUDED_POKEMON.add("celebi");

        // Legendarios Gen 3
        EXCLUDED_POKEMON.add("regirock");
        EXCLUDED_POKEMON.add("regice");
        EXCLUDED_POKEMON.add("registeel");
        EXCLUDED_POKEMON.add("latias");
        EXCLUDED_POKEMON.add("latios");
        EXCLUDED_POKEMON.add("kyogre");
        EXCLUDED_POKEMON.add("groudon");
        EXCLUDED_POKEMON.add("rayquaza");

        // Míticos Gen 3
        EXCLUDED_POKEMON.add("jirachi");
        EXCLUDED_POKEMON.add("deoxys");

        // Legendarios Gen 4
        EXCLUDED_POKEMON.add("uxie");
        EXCLUDED_POKEMON.add("mesprit");
        EXCLUDED_POKEMON.add("azelf");
        EXCLUDED_POKEMON.add("dialga");
        EXCLUDED_POKEMON.add("palkia");
        EXCLUDED_POKEMON.add("heatran");
        EXCLUDED_POKEMON.add("regigigas");
        EXCLUDED_POKEMON.add("giratina");
        EXCLUDED_POKEMON.add("cresselia");

        // Míticos Gen 4
        EXCLUDED_POKEMON.add("phione");
        EXCLUDED_POKEMON.add("manaphy");
        EXCLUDED_POKEMON.add("darkrai");
        EXCLUDED_POKEMON.add("shaymin");
        EXCLUDED_POKEMON.add("arceus");

        // Legendarios Gen 5
        EXCLUDED_POKEMON.add("cobalion");
        EXCLUDED_POKEMON.add("terrakion");
        EXCLUDED_POKEMON.add("virizion");
        EXCLUDED_POKEMON.add("tornadus");
        EXCLUDED_POKEMON.add("thundurus");
        EXCLUDED_POKEMON.add("reshiram");
        EXCLUDED_POKEMON.add("zekrom");
        EXCLUDED_POKEMON.add("landorus");
        EXCLUDED_POKEMON.add("kyurem");

        // Míticos Gen 5
        EXCLUDED_POKEMON.add("victini");
        EXCLUDED_POKEMON.add("keldeo");
        EXCLUDED_POKEMON.add("meloetta");
        EXCLUDED_POKEMON.add("genesect");

        // Legendarios Gen 6
        EXCLUDED_POKEMON.add("xerneas");
        EXCLUDED_POKEMON.add("yveltal");
        EXCLUDED_POKEMON.add("zygarde");

        // Míticos Gen 6
        EXCLUDED_POKEMON.add("diancie");
        EXCLUDED_POKEMON.add("hoopa");
        EXCLUDED_POKEMON.add("volcanion");

        // Legendarios Gen 7
        EXCLUDED_POKEMON.add("type-null");
        EXCLUDED_POKEMON.add("silvally");
        EXCLUDED_POKEMON.add("tapu-koko");
        EXCLUDED_POKEMON.add("tapu-lele");
        EXCLUDED_POKEMON.add("tapu-bulu");
        EXCLUDED_POKEMON.add("tapu-fini");
        EXCLUDED_POKEMON.add("cosmog");
        EXCLUDED_POKEMON.add("cosmoem");
        EXCLUDED_POKEMON.add("solgaleo");
        EXCLUDED_POKEMON.add("lunala");
        EXCLUDED_POKEMON.add("necrozma");

        // Ultra Bestias Gen 7
        EXCLUDED_POKEMON.add("nihilego");
        EXCLUDED_POKEMON.add("buzzwole");
        EXCLUDED_POKEMON.add("pheromosa");
        EXCLUDED_POKEMON.add("xurkitree");
        EXCLUDED_POKEMON.add("celesteela");
        EXCLUDED_POKEMON.add("kartana");
        EXCLUDED_POKEMON.add("guzzlord");
        EXCLUDED_POKEMON.add("poipole");
        EXCLUDED_POKEMON.add("naganadel");
        EXCLUDED_POKEMON.add("stakataka");
        EXCLUDED_POKEMON.add("blacephalon");

        // Míticos Gen 7
        EXCLUDED_POKEMON.add("magearna");
        EXCLUDED_POKEMON.add("marshadow");
        EXCLUDED_POKEMON.add("zeraora");
        EXCLUDED_POKEMON.add("meltan");
        EXCLUDED_POKEMON.add("melmetal");

        // Legendarios Gen 8
        EXCLUDED_POKEMON.add("zacian");
        EXCLUDED_POKEMON.add("zamazenta");
        EXCLUDED_POKEMON.add("eternatus");
        EXCLUDED_POKEMON.add("kubfu");
        EXCLUDED_POKEMON.add("urshifu");
        EXCLUDED_POKEMON.add("regieleki");
        EXCLUDED_POKEMON.add("regidrago");
        EXCLUDED_POKEMON.add("glastrier");
        EXCLUDED_POKEMON.add("spectrier");
        EXCLUDED_POKEMON.add("calyrex");

        // Míticos Gen 8
        EXCLUDED_POKEMON.add("zarude");

        // Legendarios Gen 9
        EXCLUDED_POKEMON.add("wo-chien");
        EXCLUDED_POKEMON.add("chien-pao");
        EXCLUDED_POKEMON.add("ting-lu");
        EXCLUDED_POKEMON.add("chi-yu");
        EXCLUDED_POKEMON.add("koraidon");
        EXCLUDED_POKEMON.add("miraidon");
        EXCLUDED_POKEMON.add("walking-wake");
        EXCLUDED_POKEMON.add("iron-leaves");
        EXCLUDED_POKEMON.add("okidogi");
        EXCLUDED_POKEMON.add("munkidori");
        EXCLUDED_POKEMON.add("fezandipiti");
        EXCLUDED_POKEMON.add("ogerpon");
        EXCLUDED_POKEMON.add("terapagos");

        // Míticos Gen 9
        EXCLUDED_POKEMON.add("pecharunt");
    }
}
