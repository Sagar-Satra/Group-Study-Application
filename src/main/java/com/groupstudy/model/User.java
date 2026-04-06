package com.groupstudy.model;

import com.groupstudy.adt.BagInterface;
import com.groupstudy.adt.QueueInterface;
import com.groupstudy.implementation.QueueImplementation;
import com.groupstudy.implementation.ResizableArrayBag;

public class User {
	
	 // Basic user identifier (used as unique key in HashMap)
    private String name;

    // The last time the user interacted with the app
    // Used to determine whether the user is active or idle
    private long lastInteractionTime;

    // The last time we updated this user's study time
    // Used to calculate how much real time has passed
    private long lastUpdateTime;
    
    // new fields for pokemon trophy and leaderboard tracking
    private BagInterface<Trophy> trophyBag;
    private Pokemon currentPokemon;
    private long totalStudyMinutes;
    private int currentStreak;
    private UserStatus currentStatus;

    // new field to keep track of each user's notification queue
    private QueueInterface<Notification> notificationQueue;
    
    
    public User(String name) {
        this.name = name;

        // Initialize both time stamps to current time
        // So the user starts in a "recently active" state
        long now = System.currentTimeMillis();
        this.lastInteractionTime = now;
        this.lastUpdateTime = now;
        
        // initialize pokemon/trophy related tracking fields
        this.trophyBag = new ResizableArrayBag<Trophy>();
        this.currentPokemon = null;
        this.totalStudyMinutes = 0;
        this.currentStreak = 0;
        this.currentStatus = UserStatus.OFFLINE;
        
        // initialize notification related field
        this.notificationQueue = new QueueImplementation<Notification>();
        
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
    
    public UserStatus getCurrentStatus() {
    	return currentStatus;
    }
    
    public void setCurrentStatus(UserStatus status) {
    	this.currentStatus = status;
    }
    
    public void login() {
    	this.currentStatus = UserStatus.ONLINE;
    }
    
    public void logout() {
    	this.currentStatus = UserStatus.OFFLINE;
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
    
    
    // pokemon methods
    public void setCurrentPokemon(Pokemon pokemon) {
    	this.currentPokemon = pokemon;
    }
    
    public Pokemon getCurrPokemon() {
    	return currentPokemon;
    }
    
    public boolean hasActivePokemon() {
    	return currentPokemon != null;
    }
    
    // trophy methods
    public void addTrophy(Trophy trophy) {
    	trophyBag.add(trophy);
    }
    
    public BagInterface<Trophy> getTrophyBag(){
    	return trophyBag;
    }
    
    public int getTrophyCount() {
        return trophyBag.getCurrentSize();
    }
    
    public Trophy[] getAllTrophies() {
        Object[] objects = trophyBag.toArray();  // Get Object[]
        Trophy[] trophies = new Trophy[objects.length];  // Create new Trophy[]
        
        for (int i = 0; i < objects.length; i++) {
            trophies[i] = (Trophy) objects[i];  // Cast each element to trophy to avoid ClassCast option
        }
        
        return trophies;
    }
    
    public boolean hasTrophy(Trophy trophy) {
        return trophyBag.contains(trophy);
    }
    
    // tracking total study time for leaderboard
    public void addStudyTime(long minutes) {
        this.totalStudyMinutes += minutes;
    }
    
    public long getTotalStudyMinutes() {
        return totalStudyMinutes;
    }
    
    public int getCurrentStreak() {
        return currentStreak;
    }
    
    public void setCurrentStreak(int streak) {
        this.currentStreak = streak;
    }
    
    // methods related to notification queue tracking
    public void addNotification(Notification notification) {
    	notificationQueue.enqueue(notification);
    }
    
    public Notification getNextNotification() {
    	if (!notificationQueue.isEmpty()) {
    		return notificationQueue.dequeue();
    	}
    	return null;
    }
    
    public int getPendingNotificationCount() {
    	return notificationQueue.getSize();
    }
    
    public void clearNotifications() {
    	notificationQueue.clear();
    }
    
    public boolean hasPendingNotifications() {
    	return !notificationQueue.isEmpty();
    }
    
    
    
    @Override
    public String toString() {
        return String.format("User{name='%s', trophies=%d, pokemon=%s}",
            name, getTrophyCount(), 
            (currentPokemon != null ? currentPokemon.getCurrentName() : "none"));
    }
}
