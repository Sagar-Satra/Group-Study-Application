package com.groupstudy.service;

import com.groupstudy.Main;
import com.groupstudy.adt.MapInterface;
import com.groupstudy.implementation.HashMapImplementation;
import com.groupstudy.model.StudyRoom;
import com.groupstudy.model.User;
import com.groupstudy.model.UserStatus;

/**
 * In-memory user store that manages registration, login, and logout.
 * Uses our custom HashMapImplementation to store users by username.
 * 
 * This replaces database persistence — all data lives in memory for demo purposes.
 */
public class UserStore {

	// stores all registered users: key = username, value = User object
	private MapInterface<String, User> users;

	// stores hashed passwords: key = username, value = hashed password
	private MapInterface<String, String> passwords;

	// auth service for password hashing
	private AuthService authService;

	// the currently logged-in user (null if no one is logged in)
	private User currentUser;

	// singleton instance so the same store is shared across the app
	private static UserStore instance;

	private UserStore() {
		this.users = new HashMapImplementation<>();
		this.passwords = new HashMapImplementation<>();
		this.authService = new AuthService();
		this.currentUser = null;
	}

	/**
	 * Returns the singleton instance of UserStore.
	 * Creates it on first call.
	 */
	public static UserStore getInstance() {
		if (instance == null) {
			instance = new UserStore();
		}
		return instance;
	}

	/**
	 * Registers a new user with username and password.
	 * 
	 * @return true if registration succeeded, false if username already taken
	 */
	public boolean register(String username, String password) {
		// validate inputs
		if (username == null || username.trim().isEmpty()) {
			return false;
		}
		if (password == null || password.trim().isEmpty()) {
			return false;
		}

		// check if username already exists
		if (users.containsKey(username.trim())) {
			return false;
		}

		// create user and store with hashed password
		String trimmedUsername = username.trim();
		User newUser = new User(trimmedUsername);
		users.put(trimmedUsername, newUser);
		passwords.put(trimmedUsername, authService.hashPassword(password));

		return true;
	}

	/**
	 * Attempts to log in a user with username and password.
	 * 
	 * @return true if login succeeded, false if credentials are wrong
	 */
	public boolean login(String username, String password) {
		if (username == null || password == null) {
			return false;
		}

		String trimmedUsername = username.trim();

		// check if user exists
		if (!users.containsKey(trimmedUsername)) {
			return false;
		}

		// verify password
		String storedHash = passwords.get(trimmedUsername);
		if (!authService.verifyPassword(password, storedHash)) {
			return false;
		}

		// set as current user and update status
		currentUser = users.get(trimmedUsername);
		
		// if the current user is already in a room, remove them and login fresh
		if (currentUser.getCurrentStatus() == UserStatus.IN_ROOM) {
			RoomManager roomManager = Main.getRoomManager();
			for (String roomId : roomManager.getAllRoom().keySet()) {
				StudyRoom room = roomManager.getRoom(roomId);
				if (room.containsUser(currentUser)) {
					room.removeUser(currentUser);
				}
			}
			// set the pokemon to null
			currentUser.setCurrentPokemon(null);
		}
		
		currentUser.login();
		return true;
	}

	/**
	 * Logs out the current user.
	 */
	public void logout() {
		if (currentUser != null) {
			currentUser.logout();
			currentUser = null;
		}
	}

	/**
	 * Returns the currently logged-in user, or null if nobody is logged in.
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * Checks if a user is currently logged in.
	 */
	public boolean isLoggedIn() {
		return currentUser != null;
	}

	/**
	 * Gets a user by username (for looking up other users).
	 */
	public User getUser(String username) {
		return users.get(username);
	}

	/**
	 * Checks if a username is already registered.
	 */
	public boolean userExists(String username) {
		if (username == null) return false;
		return users.containsKey(username.trim());
	}
}
