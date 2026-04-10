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
	    {"/images/charmander.png", "/images/charmeleon.png", "/images/charizard.png"},
	    {"/images/bulbasaur.png", "/images/ivysaur.png", "/images/venusaur.png"},
	    {"/images/squirtle.png", "/images/wartortle.png", "/images/blastoise.png"},
	    {"/images/pikachu.png", "/images/raichu.png", "/images/raichu-alolan.png"},
	    {"/images/caterpie.png", "/images/metapod.png", "/images/butterfree.png"},
	    {"/images/pidgey.png", "/images/pidgeotto.png", "/images/pidgeot.png"},
	    {"/images/geodude.png", "/images/graveler.png", "/images/golem.png"},
	    {"/images/abra.png", "/images/kadabra.png", "/images/alakazam.png"},
	    {"/images/magikarp.png", "/images/gyarados.png", "/images/mega-gyarados.png"},
	    {"/images/dratini.png", "/images/dragonair.png", "/images/dragonite.png"},
	    {"/images/chikorita.png", "/images/bayleef.png", "/images/meganium.png"},
	    {"/images/weedle.png", "/images/kakuna.png", "/images/beedrill.png"},
	    {"/images/cyndaquil.png", "/images/quilava.png", "/images/typhlosion.png"},
	    {"/images/nidoran-f.png", "/images/nidorina.png", "/images/nidoqueen.png"},
	    {"/images/nidoran-m.png", "/images/nidorino.png", "/images/nidoking.png"}
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