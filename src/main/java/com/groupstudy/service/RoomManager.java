package com.groupstudy.service;

import com.groupstudy.adt.*;
import com.groupstudy.implementation.*;
import com.groupstudy.model.*;

public class RoomManager {
	
	// Store all current study rooms
	// Key: roomId value: StudyRoom
	private MapInterface<String, StudyRoom> rooms = new HashMapImplementation<>();
	
	// Add a new room to the lobby
	public void addRoom(StudyRoom room) {
		rooms.put(room.getRoomId(), room);
	}
	
	// Get a existing room
	// Use for room searching function
	public StudyRoom getRoom(String roomId) {
		return rooms.get(roomId);
	}
	
	// Check if certain room exist
	public boolean containsRoom(String roomId) {
		return rooms.containsKey(roomId);
	}
	
	// Remove a room from the lobby
	public void removeRoom(String roomId) {
		rooms.remove(roomId);
	}
	
	// Display all the exist rooms
	public MapInterface<String, StudyRoom> getAllRoom(){
		return rooms;
	}
}
