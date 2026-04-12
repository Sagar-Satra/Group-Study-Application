package com.groupstudy;

import com.groupstudy.model.StudyRoom;
import com.groupstudy.service.*;
import com.groupstudy.ui.LoginUI;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    // shared service instances - this follows singleton pattern
	private static RoomManager roomManager;
    private static LeaderboardService leaderboardService;
    private static StudyTimer studyTimer;
    
    @Override
    public void start(Stage primaryStage) {
    		System.out.println("Starting the Group study application...");
    		
    		// initializing demo data and services
    		initializeApplication();
    		
    		// now first show login screen
    		LoginUI.show(primaryStage);
    	
    }
    
    /**
     * initializing application with demo data and service for launch
     * only runs once during application startup
     */
    private void initializeApplication() {
        System.out.println("Initializing application data...");
        
        // creating demo data (5 users, 5 rooms, Pokemons)
        DataInitializer dataInit = new DataInitializer();
        dataInit.initialize();
        
        // getting shared service instances
        roomManager = dataInit.getRoomManager();
        leaderboardService = dataInit.getLeaderboardService();
        
        // start study time for each room
        for (String roomid : roomManager.getAllRoom().keySet()) {
        		StudyRoom room = roomManager.getRoom(roomid);
        		if (!room.isClosed()) {
        			StudyTimer timer = new StudyTimer(leaderboardService);
        			timer.start(room);
        			System.out.println("Started timer for: " + room.getTitle());
        		}
        }
        
        System.out.println("Application initialized successfully!\n");
    }
    
    /**
     * getter for shared RoomManager instance   
     * will be used by UI classes to access rooms    
     */
    public static RoomManager getRoomManager() {
        return roomManager;
    }
    
    /**
     * getter for shared LeaderboardService instance     
     * used by UI classes to access leaderboard data  
     */
    public static LeaderboardService getLeaderboardService() {
        return leaderboardService;
    }
    
    /**
     * getter for shared StudyTimer instance        
     * used by UI classes to access the timer         
     */
    public static StudyTimer getStudyTimer() {
        return studyTimer;
    }
    
    // stop everything once window is closed
    @Override
    public void stop() {
        System.exit(0);
    }
    
    /**
     * main method which launches the JavaFX application and internally calls start() method
     */
    public static void main(String[] args) {
        launch(args);
    }
	
}
