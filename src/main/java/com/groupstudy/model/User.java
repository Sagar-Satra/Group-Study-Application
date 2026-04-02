package com.groupstudy.model;

public class User {
	 // Basic user identifier (used as unique key in HashMap)
    private String name;

    // The last time the user interacted with the app
    // Used to determine whether the user is active or idle
    private long lastInteractionTime;

    // The last time we updated this user's study time
    // Used to calculate how much real time has passed
    private long lastUpdateTime;

    public User(String name) {
        this.name = name;

        // Initialize both time stamps to current time
        // So the user starts in a "recently active" state
        long now = System.currentTimeMillis();
        this.lastInteractionTime = now;
        this.lastUpdateTime = now;
    }

    public String getName() {
        return name;
    }

    public long getLastInteractionTime() {
        return lastInteractionTime;
    }

    // Called whenever the user interacts with the app
    // (e.g., clicking a button, switching state, etc.)
    public void setLastInteractionTime(long t) {
        lastInteractionTime = t;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    // Updated by the timer after each cycle
    // Helps track elapsed time for study duration
    public void setLastUpdateTime(long t) {
        lastUpdateTime = t;
    }

    // We use name as the unique identifier for User
    // This ensures HashMap treats users with same name as the same key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User u = (User) o;
        return name.equals(u.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
