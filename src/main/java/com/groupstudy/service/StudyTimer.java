package com.groupstudy.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.groupstudy.model.LeaderboardEntry;
import com.groupstudy.model.RoomStatus;
import com.groupstudy.model.StudyRoom;
import com.groupstudy.model.Trophy;
import com.groupstudy.model.User;

public class StudyTimer {
	 // Scheduler that runs a task repeatedly in the background
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // If the user has no interaction for 5 seconds, we consider them as STUDYING
    private static final long IDLE_THRESHOLD_MS = 5000;

    // creating the NotificationService instance once for the entire class
    private NotificationService notificationService = new NotificationService();
    
    // Leaderboard service to update leaderboard rankings
    private LeaderboardService leaderboardService;
    
    
    public StudyTimer(LeaderboardService leaderboardService) {
		this.leaderboardService = leaderboardService;
	}
    
    
    // Start the timer logic 
    // runs every 1 second to update user status and study time 
    public void start(StudyRoom room) {

        // Run this task every 1 second
        scheduler.scheduleAtFixedRate(() -> {
        	
        		if (room.isSessionOver()) {
        			handleSessionEnd(room);
                scheduler.shutdown();
                return;
            }

            // Current system time (used for all calculations)
            long now = System.currentTimeMillis();

            // Iterate through all users in the room
            for (User user : room.getAllStatus().keySet()) {
            	
            		RoomStatus status = room.getStatus(user);
            		// if the user left room, ignore its stats tracking
	            	if(status == RoomStatus.LEFT) {
	            		continue;
	            	}
	            	// if the user clicked manual break, ignore its stats tracking
	            	if (user.isManualBreakTrack()) {
	            		// update this time as well to track break time
	            		user.setLastUpdateTime(now);
	            		continue;
	            	}

                // Time since last interaction
                long idle = now - user.getLastInteractionTime();

                // If user has been inactive long enough → STUDYING
                if (idle > IDLE_THRESHOLD_MS) {

                    room.updateStatus(user, RoomStatus.STUDYING);

                    long elapsedMillisec = 1000;

                    // Add that time to total study time
                    room.addStudyTime(user, elapsedMillisec);
                   
                    // connecting to pokemon system to update
                               
                    // Convert ms to seconds for pokemon evolution tracking
                    long elapsedSeconds = elapsedMillisec / 1000; 
                    
                    
                    // only process if at least 1 second has passed
                    if (elapsedSeconds >= 1) {
                    	
	                    	// Update user's total study time in seconds
	                    	user.addStudyTime(elapsedSeconds);
	                    	
	                    	// Get user's current pokemon and update evolution
	                    	if(user.getCurrentPokemon() != null) {
	                    	
	                    		boolean evolved = user.getCurrentPokemon().addStudyTime(elapsedSeconds);
	                    	
	                    		// If evolution happens
	                    		if(evolved) {
	                    			String pokemonName = user.getCurrentPokemon().getCurrentName();
	                    			int stage = user.getCurrentPokemon().getCurrentStage();
	                    			// send evolution notification to user
	                    			notificationService.addPokemonEvolution(user, pokemonName, stage);
	                    			
	                    			// now create trophy if stage 2 or above and send notification
	                    			Trophy trophy = user.getCurrentPokemon().createTrophy();
	                    			if (trophy != null) {
	                    				user.addTrophy(trophy);
	                    				
	                    				// adding trophy notification to the user's notification queue
	                    				notificationService.addTrophy(user, pokemonName);
	                    			}
	                    		}
	                    	}
	                    	updateGlobalLeaderboard(room, user);
                    }

                } else {
                    // Otherwise, user is interacting → BREAK
                    room.updateStatus(user, RoomStatus.BREAK);
                }
            }

        }, 0, 1, TimeUnit.SECONDS);
    }
    
    /**
	 * updates the global leaderboard with current user data
	 */
	private void updateGlobalLeaderboard(StudyRoom room, User user) {
		// converting study time from milliseconds to seconds for leaderboard
		long studySeconds = user.getTotalStudySeconds();
		
		LeaderboardEntry entry = new LeaderboardEntry(
			user.getName(),
			user.getTrophyCount(),
			studySeconds,  // study time in seconds
			user.getCurrentStreak()
		);
		
		leaderboardService.addOrUpdateEntry(entry);
	}
	
	/**
	 * this handles the end of a study session
	 * updates streaks, sends notifications, and finalizes leaderboard
	 */
	private void handleSessionEnd(StudyRoom room) {
				
		for (User user : room.getAllStatus().keySet()) {
			RoomStatus status = room.getStatus(user);
			
			// Only process users who participated (not LEFT early)
			if (status != RoomStatus.LEFT) {
				
				// Update study streak
				user.updateStreak();
				
				// Calculate final study time in seconds
				long finalStudySeconds = room.getStudyTime(user) / 1000;
				
				// Send session end notification
				notificationService.addSessionEnd(user, (int) finalStudySeconds);
				
				// final leaderboard update
				updateGlobalLeaderboard(room, user);
				
			}
		}
		
		// now call the StudyRoom.java endSession() to update
		room.endSession();
	}
	
	
	
}
