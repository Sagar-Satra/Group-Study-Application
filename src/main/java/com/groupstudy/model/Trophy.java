package com.groupstudy.model;

import java.time.LocalDateTime;

public class Trophy {
	private String pokemonName;
	private String imagePath;
	private int evolutionStage;
	private LocalDateTime earnedDate;
	private long studySeconds;
	
	public Trophy(String pokemon, String imagePath, int evolutionStage, long studySeconds) {
		if (evolutionStage < 2 || evolutionStage > 3) {
			throw new IllegalArgumentException("Trophy stage must be 2 or 3");
		}
		this.pokemonName = pokemon;
		this.imagePath = imagePath;
		this.evolutionStage = evolutionStage;
		this.earnedDate = LocalDateTime.now();
		this.studySeconds = studySeconds;
	}
	
	public String getPokemonName() {
		return pokemonName;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	public int getEvolutionStage() {
        return evolutionStage;
    }
    
    public LocalDateTime getEarnedDate() {
        return earnedDate;
    }
    
    public long getStudySeconds() {
        return studySeconds;
    }
    
    /**
	 * Returns study time in minutes for display.
	 */
	public long getStudyMinutes() {
		return studySeconds / 60;
	}
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Trophy)) return false;
        Trophy t = (Trophy) obj;
        return pokemonName.equals(t.pokemonName) && evolutionStage == t.evolutionStage;
    }
    
    @Override
	public int hashCode() {
		return pokemonName.hashCode() * 31 + evolutionStage;
	}
    
    @Override
	public String toString() {
		return pokemonName + " (Stage " + evolutionStage + ") - " + studySeconds + " sec (" + getStudyMinutes() + " min)";
	}
    
    
	
}
