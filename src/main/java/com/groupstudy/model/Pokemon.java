package com.groupstudy.model;

public class Pokemon {
	private String baseName;
	private String[] evolutionNames;
	private String[] imagePaths;
	// eg. 1,2,3
	private int currentStage;
	private long minutesStudied;
	
	// some constants
	private static final long STAGE_2_THRESHOLD = 30;
	private static final long STAGE_3_THRESHOLD = 60;
	
	public Pokemon(String baseName, String[] evolutionNames, String[] imagePaths) {
		if (evolutionNames.length != 3 || imagePaths.length != 3) {
			throw new IllegalArgumentException("Need 3 evolution names and 3 image paths");
		}
		this.baseName = baseName;
		this.evolutionNames = evolutionNames;
		this.imagePaths = imagePaths;
		this.currentStage = 1;
		this.minutesStudied = 0;
	}
	
	// some methods to track evolution
	public boolean addStudyTime(long minutes) {
		int previousStage = currentStage;
		minutesStudied += minutes;
		
		if (minutesStudied >= STAGE_3_THRESHOLD && currentStage < 3) {
			currentStage = 3;
		} else if (minutesStudied >= STAGE_2_THRESHOLD && currentStage < 2) {
			currentStage = 2;
		}
		
		return currentStage > previousStage;
	}
	
	public String getCurrentName() {
		return evolutionNames[currentStage - 1];
	}
	
	public String getCurrentImagePath() {
		return imagePaths[currentStage - 1];
	}
	
	public boolean canEvolve() {
		return currentStage < 3;
	}
	
	public long getMinutesUntilNextEvolution() {
		if (currentStage == 1) {
			return Math.max(0, STAGE_2_THRESHOLD - minutesStudied);
		} else if (currentStage ==2) {
			return Math.max(0, STAGE_3_THRESHOLD - minutesStudied);
		}
		return 0;
	}
	
	// creating Trophy
	public Trophy createTrophy() {
		if (currentStage < 2) {
			return null;
		}
		return new Trophy(getCurrentName(), getCurrentImagePath(), currentStage, minutesStudied);
	}
	
	public int getCurrentStage() {
		return currentStage;
	}
	
	public long getMinutesStudied() {
        return minutesStudied;
    }
	
	@Override
    public String toString() {
        return String.format("%s (Stage %d) - %d minutes", 
            getCurrentName(), currentStage, minutesStudied);
    }
	
	
	
}
