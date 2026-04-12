package com.groupstudy.model;

public class Pokemon {
	private String baseName;
	private String[] evolutionNames;
	private String[] imagePaths;
	// eg. 1,2,3
	private int currentStage;
	private long secondsStudied;
	
	// some constants, stage 1 to 2 = 30 seconds and stage 2 to 3 - 60seconds 
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
		this.secondsStudied = 0;
	}
	
	// some methods to track evolution
	/**
	 * adds study time in seconds and checks for evolution
	 * @param minutes - seconds to add
	 * @return - true if pokemon evolved
	 */
	public boolean addStudyTime(long seconds) {
		int previousStage = currentStage;
		secondsStudied += seconds;
		
		if (secondsStudied >= STAGE_3_THRESHOLD && currentStage < 3) {
			currentStage = 3;
		} else if (secondsStudied >= STAGE_2_THRESHOLD && currentStage < 2) {
			currentStage = 2;
		}
		// return true if evolution happened
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
	
	// returns seconds until next evolution
	public long getSecondsUntilNextEvolution() {
		if (currentStage == 1) {
			return Math.max(0, STAGE_2_THRESHOLD - secondsStudied);
		} else if (currentStage ==2) {
			return Math.max(0, STAGE_3_THRESHOLD - secondsStudied);
		}
		return 0;
	}
	
	// returns evolution progress as a percentage
	public double getEvolutionProgress() {
		if (currentStage == 1) {
			// progress toward Stage 2 (0-30 seconds)
			return Math.min(100.0, (secondsStudied * 100.0) / STAGE_2_THRESHOLD);
		} else if (currentStage == 2) {
			// progress Stage 3 (30-60 seconds)
			double progressInStage = secondsStudied - STAGE_2_THRESHOLD;
			double stageRange = STAGE_3_THRESHOLD - STAGE_2_THRESHOLD;
			return Math.min(100.0, (progressInStage * 100.0) / stageRange);
		}
		return 100.0;  // Stage 3 means maxed out
	}
	
	// creating Trophy if pokemon has reached at least stage 2
	public Trophy createTrophy() {
		if (currentStage < 2) {
			return null;
		}
		return new Trophy(getCurrentName(), getCurrentImagePath(), currentStage, secondsStudied);
	}
	
	public int getCurrentStage() {
		return currentStage;
	}
	
	// returns study time in seconds
	public long getSecondsStudied() {
        return secondsStudied;
    }
	
	// returns study time in minutes for display purpose
	public long getMinutesStudied() {
		return secondsStudied / 60;
	}
	
	/**
	 * getting threshold for the next evolution in seconds.
	 */
	public long getNextEvolutionThreshold() {
		if (currentStage == 1) {
			return STAGE_2_THRESHOLD;
		} else if (currentStage == 2) {
			return STAGE_3_THRESHOLD;
		}
		return STAGE_3_THRESHOLD; 
	}
	
	@Override
	public String toString() {
		return String.format("%s (Stage %d) - %d seconds (%d min)", 
			getCurrentName(), currentStage, secondsStudied, getMinutesStudied());
	}

	
	
}
