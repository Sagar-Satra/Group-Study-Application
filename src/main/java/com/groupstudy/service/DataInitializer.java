package com.groupstudy.service;

import java.time.LocalDate;
import java.util.Random;

import com.groupstudy.model.LeaderboardEntry;
import com.groupstudy.model.Pokemon;
import com.groupstudy.model.StudyRoom;
import com.groupstudy.model.Trophy;
import com.groupstudy.model.User;
import com.groupstudy.model.UserStatus;

/**
 * pre-populates the application with demo data
 * 
 * creates - 5 users with credential
 * - 5 active rooms (public and private)
 * users already in the room
 * - current global leaderboard data
 */
public class DataInitializer {
	private UserStore userStore;
	private RoomManager roomManager;
	private LeaderboardService leaderboardService;
	private PokemonService pokemonService;
	private AuthService authService;
	private Random random;
	
	// existing users
	private static final String[] DEMO_USERNAMES = {"John", "Eve", "Frank", "Joe", "Bob", "Adele"};
	
	
	// constructor calls all the below methods
	public DataInitializer() {
		this.userStore = UserStore.getInstance();
		this.roomManager = new RoomManager();
		this.leaderboardService = new LeaderboardService();
		this.pokemonService = new PokemonService();
		this.authService = new AuthService();
		this.random = new Random();
	}
	
	public void initialize() {
		System.out.println("========== INITIALIZING DEMO DATA ==========");
		
		// first create and register users
		createUsers();
		
		// create study rooms
		createRooms();
		
		// now assign users to rooms and set up Pokemon
		populateRooms();
		
		// now simulate some study progress for variety
		simulateProgress();
		
		System.out.println("========== INITIALIZATION COMPLETE ==========\n");
	}
	
	/**
	 * Creates 15 demo users with default password "demo123".
	 */
	private void createUsers() {
		System.out.println("Creating demo users...");
		
		for (String username : DEMO_USERNAMES) {
			boolean registered = userStore.register(username, "demo123");
			if (registered) {
				User user = userStore.getUser(username);
				
				// Set random streak (0-10 days)
				user.setCurrentStreak(random.nextInt(11));
				
				// Set random last study date (within last week)
				int daysAgo = random.nextInt(7);
				user.setLastStudyDate(LocalDate.now().minusDays(daysAgo));
				
				// Give some users pre-existing trophies (simulating past sessions)
				if (random.nextBoolean()) {
					int trophyCount = random.nextInt(3) + 1;  // 1-3 trophies
					for (int i = 0; i < trophyCount; i++) {
						Pokemon randomPokemon = pokemonService.assignRandomPokemon();
						randomPokemon.addStudyTime(60);  // Max it out to Stage 3
						Trophy trophy = randomPokemon.createTrophy();
						user.addTrophy(trophy);
					}
				}
				
				// Add to global leaderboard
				long randomStudyTime = random.nextInt(3600) + 600;  // 10-60 minutes in seconds
				user.addStudyTime(randomStudyTime);
				
				LeaderboardEntry entry = new LeaderboardEntry(
					username,
					user.getTrophyCount(),
					user.getTotalStudySeconds(),
					user.getCurrentStreak()
				);
				leaderboardService.addOrUpdateEntry(entry);
				
				System.out.println("  ✓ Created user: " + username 
					+ " (Streak: " + user.getCurrentStreak() 
					+ ", Trophies: " + user.getTrophyCount() + ")");
			}
		}
		
		System.out.println();
	}
	
	/**
	 * Creates 5 demo study rooms (mix of public and private).
	 */
	private void createRooms() {
		System.out.println("Creating demo study rooms...");
		
		// Room 1: Public, 30 seconds session
		StudyRoom room1 = new StudyRoom(
			"Quick Study Sprint",
			5,
			300000,  // 30 seconds
			false,
			null
		);
		User admin1 = userStore.getUser("Sagar");
		room1.setAdmin(admin1);
		roomManager.addRoom(room1);
		System.out.println("  ✓ Created room: " + room1.getTitle() + " (" + room1.getRoomId() + ") - Public");
		
		// Room 2: Private, 60 seconds session
		String hashedPassword = authService.hashPassword("study123");
		StudyRoom room2 = new StudyRoom(
			"Private Focus Group",
			4,
			600000,  // 60 seconds
			true,
			hashedPassword
		);
		User admin2 = userStore.getUser("Aditya");
		room2.setAdmin(admin2);
		roomManager.addRoom(room2);
		System.out.println("  ✓ Created room: " + room2.getTitle() + " (" + room2.getRoomId() + ") - Private (password: study123)");
		
		// Room 3: Public, 90 seconds session
		StudyRoom room3 = new StudyRoom(
			"Math Study Group",
			6,
			900000,  // 90 seconds
			false,
			null
		);
		User admin3 = userStore.getUser("Yuxuan");
		room3.setAdmin(admin3);
		roomManager.addRoom(room3);
		System.out.println("  ✓ Created room: " + room3.getTitle() + " (" + room3.getRoomId() + ") - Public");
		
		// Room 4: Public, 120 seconds session
		StudyRoom room4 = new StudyRoom(
			"CS Algorithm Practice",
			8,
			1200000,  // 120 seconds
			false,
			null
		);
		User admin4 = userStore.getUser("John");
		room4.setAdmin(admin4);
		roomManager.addRoom(room4);
		System.out.println("  ✓ Created room: " + room4.getTitle() + " (" + room4.getRoomId() + ") - Public");
		
		// Room 5: Private, 45 seconds session
		String hashedPassword2 = authService.hashPassword("team456");
		StudyRoom room5 = new StudyRoom(
			"Late Night Grind",
			3,
			450000,  // 45 seconds
			true,
			hashedPassword2
		);
		User admin5 = userStore.getUser("Eve");
		room5.setAdmin(admin5);
		roomManager.addRoom(room5);
		System.out.println("  ✓ Created room: " + room5.getTitle() + " (" + room5.getRoomId() + ") - Private (password: team456)");
		
		System.out.println();
	}
	
	
	/**
	 * Assigns users to rooms and gives them Pokemon.
	 */
	private void populateRooms() {
		System.out.println("Populating rooms with users...");
		
		// Get all rooms
		int roomIndex = 0;
		for (String roomId : roomManager.getAllRoom().keySet()) {
			StudyRoom room = roomManager.getRoom(roomId);
			
			// Determine how many users to add (2-4 users per room)
			int userCount = random.nextInt(3) + 2;
			userCount = Math.min(userCount, room.getCapacity());
			
			// Add users to this room
			int usersAdded = 0;
			for (int i = roomIndex * 3; i < DEMO_USERNAMES.length && usersAdded < userCount; i++) {
				String username = DEMO_USERNAMES[i % DEMO_USERNAMES.length];
				User user = userStore.getUser(username);
				
				// Skip if user is already in a room
				if (user.getCurrentStatus() == UserStatus.IN_ROOM) {
					continue;
				}
				
				// Add user to room
				room.addUser(user);
				
				// Assign a random Pokemon
				Pokemon pokemon = pokemonService.assignRandomPokemon();
				user.setCurrentPokemon(pokemon);
				user.incrementPokemonCount();
				
				// Give some Pokemon a head start (simulate varying progress)
				if (random.nextBoolean()) {
					long headStart = random.nextInt(20) + 5;  // 5-25 seconds
					pokemon.addStudyTime(headStart);
				}
				
				System.out.println("  ✓ " + username + " joined " + room.getTitle() 
					+ " with " + pokemon.getCurrentName() 
					+ " (Stage " + pokemon.getCurrentStage() + ")");
				
				usersAdded++;
			}
			
			roomIndex++;
		}
		
		System.out.println();
	}
	
	
	/**
	 * Simulates some study progress to make the demo more interesting.
	 * Gives some users partial progress toward evolution.
	 */
	private void simulateProgress() {
		System.out.println("Simulating study progress...");
		
		for (String roomId : roomManager.getAllRoom().keySet()) {
			StudyRoom room = roomManager.getRoom(roomId);
			
			for (User user : room.getAllStatus().keySet()) {
				// Simulate 5-15 seconds of study time
				long simulatedTime = (random.nextInt(11) + 5) * 1000;  // 5-15 seconds in milliseconds
				room.addStudyTime(user, simulatedTime);
				
				// Update Pokemon progress
				if (user.getCurrentPokemon() != null) {
					long seconds = simulatedTime / 1000;
					boolean evolved = user.getCurrentPokemon().addStudyTime(seconds);
					
					if (evolved) {
						System.out.println("  ✓ " + user.getName() + "'s " 
							+ user.getCurrentPokemon().getCurrentName() 
							+ " evolved to Stage " + user.getCurrentPokemon().getCurrentStage());
						
						// Create trophy if eligible
						Trophy trophy = user.getCurrentPokemon().createTrophy();
						if (trophy != null) {
							user.addTrophy(trophy);
							System.out.println("    🏆 Trophy earned!");
						}
					}
				}
			}
		}
		
		System.out.println();
	}
	
	/**
	 * Returns the room manager (for UI access).
	 */
	public RoomManager getRoomManager() {
		return roomManager;
	}
	
	/**
	 * Returns the leaderboard service (for UI access).
	 */
	public LeaderboardService getLeaderboardService() {
		return leaderboardService;
	}
	
	/**
	 * Returns the user store (for UI access).
	 */
	public UserStore getUserStore() {
		return userStore;
	}
	
	
	
}
