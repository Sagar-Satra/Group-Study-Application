package com.groupstudy.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.groupstudy.model.Status;
import com.groupstudy.model.StudyRoom;
import com.groupstudy.model.User;

public class StudyTimer {
	 // Scheduler that runs a task repeatedly in the background
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // If the user has no interaction for 5 seconds, we consider them as STUDYING
    private static final long IDLE_THRESHOLD_MS = 5000;

    // Start the timer logic
    public void start(StudyRoom room) {

        // Run this task every 1 second
        scheduler.scheduleAtFixedRate(() -> {

            // Current system time (used for all calculations)
            long now = System.currentTimeMillis();

            // Iterate through all users in the room
            for (User user : room.getAllStatus().keySet()) {

                // Time since last interaction
                long idle = now - user.getLastInteractionTime();

                // If user has been inactive long enough → STUDYING
                if (idle > IDLE_THRESHOLD_MS) {

                    room.updateStatus(user, Status.STUDYING);

                    // Calculate how much real time has passed since last update
                    long elapsed = now - user.getLastUpdateTime();

                    // Add that time to total study time
                    room.addStudyTime(user, elapsed);

                } else {
                    // Otherwise, user is interacting → BREAK
                    room.updateStatus(user, Status.BREAK);
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
