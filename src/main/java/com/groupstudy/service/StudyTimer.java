package com.groupstudy.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.groupstudy.model.RoomStatus;
import com.groupstudy.model.StudyRoom;
import com.groupstudy.model.User;

public class StudyTimer {
	 // Scheduler that runs a task repeatedly in the background
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // If the user has no interaction for 5 seconds, we consider them as STUDYING
    private static final long IDLE_THRESHOLD_MS = 5000;

    // creating the NotificationService instance once for the entire class
    private NotificationService notificationService = new NotificationService();
    
    // Start the timer logic
    public void start(StudyRoom room) {

        // Run this task every 1 second
        scheduler.scheduleAtFixedRate(() -> {
        	
        	if (room.isSessionOver()) {
                room.endSession();
                scheduler.shutdown();
                return;
            }

            // Current system time (used for all calculations)
            long now = System.currentTimeMillis();

            // Iterate through all users in the room
            for (User user : room.getAllStatus().keySet()) {
            	
            	RoomStatus status = room.getStatus(user);
            	
            	if(status == RoomStatus.LEFT) {
            		continue;
            	}

                // Time since last interaction
                long idle = now - user.getLastInteractionTime();

                // If user has been inactive long enough → STUDYING
                if (idle > IDLE_THRESHOLD_MS) {

                    room.updateStatus(user, RoomStatus.STUDYING);

                    // Calculate how much real time has passed since last update
                    long elapsed = now - user.getLastUpdateTime();

                    // Add that time to total study time
                    room.addStudyTime(user, elapsed);
                    
                    // =========================
                    // CONNECT TO POKEMON SYSTEM
                    // =========================
                    
                    // Convert ms to minutes (avoid 0 by using small threshold)
                    // CURRENT ONE MIN SET AS FAST AS 1 SEC
                    long minutes = 1;
                    
                    // Accumulate leftover time
                    if (minutes > 0) {
                    	
                    	// Update iser's total study minutes
                    	user.addStudyTime(minutes);
                    	
                    	// Get user's current pokemon
                    	if(user.getCurrentPokemon() != null) {
                    	
                    		boolean evolved = user.getCurrentPokemon().addStudyTime(minutes);
                    	
                    		// If evolution happens
                    		if(evolved) {
                    			System.out.println(user.getName() + "'s Pokemon evolved to " + user.getCurrentPokemon().getCurrentName());
                    		
                    			if (user.getCurrentPokemon().createTrophy() != null) {
                    				user.addTrophy(user.getCurrentPokemon().createTrophy());
                    				
                    				// adding trophy notification to the user's notification queue
                    				notificationService.addTrophy(user, user.getCurrentPokemon().getCurrentName());
                    			}
                    		}
                    	}
                    }

                } else {
                    // Otherwise, user is interacting → BREAK
                    room.updateStatus(user, RoomStatus.BREAK);
                }

                // Update last update time for next calculation
                user.setLastUpdateTime(now);

                // Print current state (for demo / debugging)
                System.out.println(
                        user.getName()
                        + " | " + room.getStatus(user)
                        + " | time(ms) = " + room.getStudyTime(user)
                );
            }

            // Separator for readability in console
            System.out.println("----");

        }, 0, 1, TimeUnit.SECONDS);
    }
}
