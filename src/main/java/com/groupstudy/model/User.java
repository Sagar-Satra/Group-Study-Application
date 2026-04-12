package com.groupstudy.model;

import com.groupstudy.adt.BagInterface;
import com.groupstudy.adt.ListInterface;
import com.groupstudy.adt.QueueInterface;
import com.groupstudy.implementation.LinkedListImplementation;
import com.groupstudy.implementation.QueueImplementation;
import com.groupstudy.implementation.ResizableArrayBag;

import java.time.LocalDate;

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
    private long totalStudySeconds;
    private int currentStreak;
    private UserStatus currentStatus;
    // to track if user manually clicked break during study session
    private boolean manualBreakTrack = false;

    // new field to keep track of each user's notification queue
    private QueueInterface<Notification> notificationQueue;
    
    // ===== Action History using Singly Linked List =====
    // Stores chronological record of all user actions (join, break, resume, leave, etc.)
    // LinkedListImplementation is our custom singly linked list ADT
    private ListInterface<ActionRecord> actionHistory;
    
    // ===== Streak Tracking =====
    // Tracks the last date the user completed a study session
    private LocalDate lastStudyDate;
    
    
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
        this.totalStudySeconds = 0;
        this.currentStreak = 0;
        this.currentStatus = UserStatus.OFFLINE;
        
        // initialize notification related field
        this.notificationQueue = new QueueImplementation<Notification>();
        
        // initialize action history as empty linked list
        this.actionHistory = new LinkedListImplementation<ActionRecord>();
        
        // initialize streak tracking
        this.lastStudyDate = null;
        
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
    
    public Pokemon getCurrentPokemon() {
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
    public void addStudyTime(long seconds) {
        this.totalStudySeconds += seconds;
    }
    
    // get total study time in seconds
    public long getTotalStudySeconds() {
		return totalStudySeconds;
	}
    
    // get total study time in minutes
    public long getTotalStudyMinutes() {
		return totalStudySeconds / 60;
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
    
    
    // ===== Action History Methods (LinkedList) =====
    
    /**
     * Records an action to the user's history.
     * Uses add() on the linked list which appends to the end,
     * so the list stays in chronological order.
     */
    public void recordAction(ActionRecord action) {
    	if (action != null) {
    		actionHistory.add(action);
    	}
    }
    
    /**
     * Returns all action records as a ListInterface.
     * Can be traversed from index 0 to getLength()-1 for chronological display.
     */
    public ListInterface<ActionRecord> getActionHistory() {
    	return actionHistory;
    }
    
    /**
     * Returns the total number of recorded actions.
     */
    public int getActionCount() {
    	return actionHistory.getLength();
    }
    
    /**
     * Clears the action history.
     */
    public void clearActionHistory() {
    	actionHistory.clear();
    }
    
    
    // ===== Streak Tracking Methods =====
    
    /**
     * Updates the study streak.
     * Called when a study session completes.
     * If the user studied yesterday, increment streak.
     * If the user studied today already, keep streak as is.
     * Otherwise, reset streak to 1 (new streak starts today).
     */
    public void updateStreak() {
    	LocalDate today = LocalDate.now();
    	
    	if (lastStudyDate == null) {
    		// first time studying
    		currentStreak = 1;
    	} else if (lastStudyDate.equals(today)) {
    		// already studied today, streak stays same
    	} else if (lastStudyDate.equals(today.minusDays(1))) {
    		// studied yesterday, increment streak
    		currentStreak++;
    	} else {
    		// missed a day or more, reset to 1
    		currentStreak = 1;
    	}
    	
    	lastStudyDate = today;
    }
    
    /**
     * Returns the last date the user studied.
     */
    public LocalDate getLastStudyDate() {
    	return lastStudyDate;
    }
    
    /**
     * Sets the last study date (used for testing/demo).
     */
    public void setLastStudyDate(LocalDate date) {
    	this.lastStudyDate = date;
    }
        
    // to keep track of manual break taken in room and account for it in study timer
    public boolean isManualBreakTrack() {
		return manualBreakTrack;
	}

	public void setManualBreakTrack(boolean manualBreakTrack) {
		this.manualBreakTrack = manualBreakTrack;
	}

	@Override
	public String toString() {
		return String.format("User{name='%s', trophies=%d, pokemon=%s, studyTime=%ds}",
			name, getTrophyCount(), 
			(currentPokemon != null ? currentPokemon.getCurrentName() : "none"),
			totalStudySeconds);
	}
}
