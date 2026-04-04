package com.groupstudy.service;

import com.groupstudy.model.Pokemon;
import com.groupstudy.model.Trophy;
import java.util.Random;

public class PokemonService {
    
    private Random random;
    
    // Pokemon data: 5 families, 3 stages each
    private static final String[][] POKEMON_DATA = {
	    {"Charmander", "Charmeleon", "Charizard"},
	    {"Bulbasaur", "Ivysaur", "Venusaur"},
	    {"Squirtle", "Wartortle", "Blastoise"},
	    {"Pikachu", "Raichu", "Raichu-Alolan"},
	    {"Caterpie", "Metapod", "Butterfree"},
	    {"Pidgey", "Pidgeotto", "Pidgeot"},
	    {"Geodude", "Graveler", "Golem"},
	    {"Abra", "Kadabra", "Alakazam"},
	    {"Magikarp", "Gyarados", "Mega-Gyarados"},
	    {"Dratini", "Dragonair", "Dragonite"},
	    {"Chikorita", "Bayleef", "Meganium"},
	    {"Weedle", "Kakuna", "Beedrill"},
	    {"Cyndaquil", "Quilava", "Typhlosion"},
	    {"Nidoran-F", "Nidorina", "Nidoqueen"},
	    {"Nidoran-M", "Nidorino", "Nidoking"}
    };

	private static final String[][] IMAGE_PATHS = {
	    {"charmander.png", "charmeleon.png", "charizard.png"},
	    {"bulbasaur.png", "ivysaur.png", "venusaur.png"},
	    {"squirtle.png", "wartortle.png", "blastoise.png"},
	    {"pikachu.png", "raichu.png", "raichu-alolan.png"},
	    {"caterpie.png", "metapod.png", "butterfree.png"},
	    {"pidgey.png", "pidgeotto.png", "pidgeot.png"},
	    {"geodude.png", "graveler.png", "golem.png"},
	    {"abra.png", "kadabra.png", "alakazam.png"},
	    {"magikarp.png", "gyarados.png", "mega-gyarados.png"},
	    {"dratini.png", "dragonair.png", "dragonite.png"},
	    {"chikorita.png", "bayleef.png", "meganium.png"},
	    {"weedle.png", "kakuna.png", "beedrill.png"},
	    {"cyndaquil.png", "quilava.png", "typhlosion.png"},
	    {"nidoran-f.png", "nidorina.png", "nidoqueen.png"},
	    {"nidoran-m.png", "nidorino.png", "nidoking.png"}
	};
    
    public PokemonService() {
        this.random = new Random();
    }
    
    /**
     * Assigns a random Pokemon when user joins study room
     */
    public Pokemon assignRandomPokemon() {
        int index = random.nextInt(POKEMON_DATA.length);
        return new Pokemon(
            POKEMON_DATA[index][0],
            POKEMON_DATA[index],
            IMAGE_PATHS[index]
        );
    }
    
    /**
     * Updates Pokemon progress and returns trophy if earned
     */
    public Trophy updatePokemonProgress(Pokemon pokemon, long studyMinutes) {
        boolean evolved = pokemon.addStudyTime(studyMinutes);
        
        if (evolved && pokemon.getCurrentStage() >= 2) {
            return pokemon.createTrophy();
        }
        
        return null;
    }
}