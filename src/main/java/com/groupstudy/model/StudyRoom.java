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
    private boolean isClosed = false;
    private boolean isPrivate;
    private String passwordHash; // Only use for private room
    
    public StudyRoom(String title, int capacity, long duration, boolean isPrivate, String passwordHash) {
    	this.roomId = generateRoomId();
    	this.title = title; 
    	this.capacity = capacity;
    	this.startTime = System.currentTimeMillis();
    	this.duration = duration;
    	this.isPrivate = isPrivate; 
    	this.passwordHash = passwordHash;
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

        for (User user : userStatusMap.keySet()) {
            userStatusMap.put(user, RoomStatus.SESSION_ENDED);
            user.setCurrentStatus(UserStatus.ONLINE);
        }

        System.out.println("Room " + roomId + " is now CLOSED.");
    }
    
    public int getCurrentSize() {
        return userStatusMap.size();
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
}