package com.groupstudy.model;

import java.util.Random;

import com.groupstudy.adt.MapInterface;
import com.groupstudy.implementation.HashMapImplementation;

public class StudyRoom {
	
	// Stores each user's current status (STUDYING / BREAK / LEFT)
    // Key: User, Value: Status
    private MapInterface<User, RoomStatus> userStatusMap = new HashMapImplementation<>();

    // Stores accumulated study time for each user (in milliseconds)
    // Key: User, Value: total study time
    private MapInterface<User, Long> studyTimeMap = new HashMapImplementation<>();
    
    private String roomId;
    private String title;
    private int capacity;
    private long startTime;
    private long duration;
    private long endTime;
    private boolean isClosed = false;
    private boolean isPrivate;
    private String passwordHash; // Only use for private room
    private User admin; // The user who created this room
    
    public StudyRoom(String title, int capacity, long duration, boolean isPrivate, String passwordHash) {
    	this.roomId = generateRoomId();
    	this.title = title; 
    	this.capacity = capacity;
    	this.startTime = System.currentTimeMillis();
    	this.duration = duration;
    	this.isPrivate = isPrivate; 
    	this.passwordHash = passwordHash;
    	this.admin = null;
    }
    
    /**
     * Constructor with admin - the user who creates the room becomes admin.
     */
    public StudyRoom(String title, int capacity, long duration, boolean isPrivate, String passwordHash, User admin) {
    	this.roomId = generateRoomId();
    	this.title = title; 
    	this.capacity = capacity;
    	this.startTime = System.currentTimeMillis();
    	this.duration = duration;
    	this.isPrivate = isPrivate; 
    	this.passwordHash = passwordHash;
    	this.admin = admin;
    }

    // Add a user to the room
    // Default status is BREAK and initial study time is 0
    public void addUser(User user) {
    	
    	// User are not allowed to join multiple rooms at same time
    	if(user.getCurrentStatus() == UserStatus.IN_ROOM) {
    		System.out.println(user.getName() + " is already in another room");
    		return;
    	}
        userStatusMap.put(user, RoomStatus.BREAK);
        studyTimeMap.put(user, 0L);
        
        user.setCurrentStatus(UserStatus.IN_ROOM);
        // reset user's activity timers
        user.setLastUpdateTime(System.currentTimeMillis());
        user.setLastInteractionTime(System.currentTimeMillis());
    }

    // Remove user from the room
    public void removeUser(User user) {
        if(isPrivate) {
        	// Private room mark the user that leave the room early as LEFT for the room status
        	// Keep the user in the name list
        	userStatusMap.put(user, RoomStatus.LEFT);
        	
        	// Stop timing but keep the studyTimeMap
        } else {
        	// Public room directly remove the left user
        	userStatusMap.remove(user);
        	studyTimeMap.remove(user);
        }
        
        // Once user leave the room, change the user status back to ONLINE
        user.setCurrentStatus(UserStatus.ONLINE);
    }

    // Check if a user exists in the room
    public boolean containsUser(User user) {
        return userStatusMap.containsKey(user);
    }
    
    private String generateRoomId() {
    	Random rand = new Random();
    	int num = 100000 + rand.nextInt(900000);
    	return "ROOM-" + num;
    }
    
    public String getRoomId() {
    	return roomId;
    }
    
    public boolean isPrivate() {
    	return isPrivate;
    }

    public String getTitle() {
    	return title;
    }
    
    public int getCapacity() {
    	return capacity;
    }
    
    public boolean isSessionOver() {
    	return System.currentTimeMillis() - startTime >= duration;
    }
    
    public boolean isClosed() {
    	return isClosed;
    }
    
    public void endSession() {
    	if (isClosed) return;
    	
    	isClosed = true;
    	endTime = System.currentTimeMillis();

        for (User user : userStatusMap.keySet()) {
            userStatusMap.put(user, RoomStatus.SESSION_ENDED);
            user.setCurrentStatus(UserStatus.ONLINE);
        }

        System.out.println("Room " + roomId + " is now CLOSED.");
    }
    
    // Remove the room from the lobby after 10s the room session ended
    public boolean shouldRemove() {
    	if(!isClosed) return false;
    	
    	return System.currentTimeMillis() - endTime > 10000;
    }
    
    public int getCurrentSize() {
        return userStatusMap.size();
    }
    
    public int getActiveUserCount() {
        int count = 0;
        for (User user : userStatusMap.keySet()) {
            RoomStatus status = userStatusMap.get(user);

            if (status != RoomStatus.LEFT && status != RoomStatus.SESSION_ENDED) {
                count++;
            }
        }
        return count;
    }
    
    public MapInterface<User, RoomStatus> getActiveUsers() {
        MapInterface<User, RoomStatus> active = new HashMapImplementation<>();

        for (User user : userStatusMap.keySet()) {
            RoomStatus status = userStatusMap.get(user);

            if (status != RoomStatus.LEFT && status != RoomStatus.SESSION_ENDED) {
                active.put(user, status);
            }
        }

        return active;
    }
    
    public long getRemainingTime() {
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.max(0, duration - elapsed);
    }
    
    // Add study time (called by timer)
    // deltaMillis = how much time passed since last update
    public void addStudyTime(User user, long deltaMillis) {
        long cur = studyTimeMap.getOrDefault(user, 0L);
        studyTimeMap.put(user, cur + deltaMillis);
    }

    // Get total study time for a user
    public long getStudyTime(User user) {
        return studyTimeMap.getOrDefault(user, 0L);
    }

    // Update user's current status
    public void updateStatus(User user, RoomStatus status) {
        userStatusMap.put(user, status);
    }

    // Get current status of a user
    public RoomStatus getStatus(User user) {
        return userStatusMap.get(user);
    }

    // Get all user-status pairs (used by timer / UI)
    public MapInterface<User, RoomStatus> getAllStatus() {
        return userStatusMap;
    }
    
    public boolean verifyPassword(String inputPassword, com.groupstudy.service.AuthService auth) {
    	if(!isPrivate) return true; // public room no need password
    	return auth.verifyPassword(inputPassword, passwordHash);
    }
    
    // ===== Admin Methods =====
    
    /**
     * Returns the admin (creator) of this room.
     */
    public User getAdmin() {
    	return admin;
    }
    
    /**
     * Sets the admin of this room.
     */
    public void setAdmin(User admin) {
    	this.admin = admin;
    }
    
    /**
     * Checks if a given user is the admin of this room.
     */
    public boolean isAdmin(User user) {
    	if (admin == null || user == null) return false;
    	return admin.equals(user);
    }
    
    /**
     * Admin-only: update the session timer duration.
     * Only the admin can change the timer.
     */
    public boolean updateDuration(User user, long newDuration) {
    	if (!isAdmin(user)) {
    		System.out.println("Only the admin can change the timer.");
    		return false;
    	}
    	this.duration = newDuration;
    	return true;
    }
    
    /**
     * Admin-only: update the room capacity.
     * Only the admin can change capacity.
     */
    public boolean updateCapacity(User user, int newCapacity) {
    	if (!isAdmin(user)) {
    		System.out.println("Only the admin can change the capacity.");
    		return false;
    	}
    	if (newCapacity < getCurrentSize()) {
    		System.out.println("New capacity cannot be less than current number of users.");
    		return false;
    	}
    	this.capacity = newCapacity;
    	return true;
    }
    
    /**
     * Returns list of user names currently in the room.
     * Admin can use this to see who is in the room.
     */
    public String[] getUserNames() {
    	String[] names = new String[getCurrentSize()];
    	int i = 0;
    	for (User user : userStatusMap.keySet()) {
    		names[i] = user.getName();
    		i++;
    	}
    	return names;
    }
}