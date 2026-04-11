package com.groupstudy.controller;

import com.groupstudy.model.LeaderboardEntry;
import com.groupstudy.model.StudyRoom;
import com.groupstudy.service.LeaderboardService;
import com.groupstudy.ui.LobbyUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class LeaderboardController {
	
	// mapping FXML elements here - connected by fx:id
	@FXML private TableView<LeaderboardRow> leaderboardTable;
    @FXML private TableColumn<LeaderboardRow, Integer> rankColumn;
    @FXML private TableColumn<LeaderboardRow, String> usernameColumn;
    @FXML private TableColumn<LeaderboardRow, Integer> trophyColumn;
    @FXML private TableColumn<LeaderboardRow, String> studyTimeColumn;
    @FXML private Label userRankLabel;
    
    // some variables which get initialized when this screen is called
    private Stage stage;
    private LeaderboardService leaderboardService;
    private String currentUsername;
    private StudyRoom currentRoom;
    
    
    /**
     * below is static method to show leaderboard screen
     * called from lobbyUI when user clicks trophy button
     * 
     */
    public static void show(Stage stage, String username, StudyRoom room) {
	    	try {
	    		FXMLLoader loader = new FXMLLoader(
	    				LeaderboardController.class.getResource("/fxml/LeaderboardView.fxml"));
	    		// puts loaded UI into the scene(canvas) with Load FXML, create controller and call initialize()
	    		Scene scene = new Scene(loader.load(), 400,600);
	    		
	    		// gets the instance of the controller, which was created automatically
	    		LeaderboardController controller = loader.getController();
	    		controller.stage = stage;
	    		controller.currentUsername = username;
	    		
	    		// creating the leaderboardService to get the data
    			// controller.leaderboardService = new LeaderboardService();
        		controller.leaderboardService = createTestLeaderboard();
	    		if (room != null) {
	    			System.out.println("inside the leadeboard controller");
	    			controller.currentRoom = room;
	    			// load room users data after every layout is set up
	    			controller.loadRoomLeaderboardData(room);
	    			stage.setTitle("Room Leaderboard - " + room.getTitle());
	    		} else {
	    			controller.currentRoom = null;
	        		// load global users data after every layout is set up
	        		controller.loadGlobalLeaderboardData();
	        		stage.setTitle("Global Leaderboard");
	    		}
	    		stage.setScene(scene);
	    		stage.show();
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		System.err.println("Error loading leaderboard: " + e.getMessage());
	    	}
    }
    
    /**
     * below method is automatically called by javafx after fxml loads
     * sets up table columns
     */
    @FXML
    public void initialize() {
    	setupTableColumns();
    }
    
    /**
     * method to configure how each column displays each data of the row - a rule
     */
    private void setupTableColumns() {
    	// rank column displays integer rank
        rankColumn.setCellValueFactory(data -> 
            new SimpleIntegerProperty(data.getValue().getRank()).asObject()
        );
        
        // username column - displays string username
        usernameColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getUsername())
        );
        
        // Trophy column - displays integer trophy count
        trophyColumn.setCellValueFactory(data -> 
            new SimpleIntegerProperty(data.getValue().getTrophyCount()).asObject()
        );
        
        // Study time column - displays formatted time string
        studyTimeColumn.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().getStudyTime())
        );
        
        // Center-align all columns
        rankColumn.setStyle("-fx-alignment: CENTER;");
        usernameColumn.setStyle("-fx-alignment: CENTER;");
        trophyColumn.setStyle("-fx-alignment: CENTER;");
        studyTimeColumn.setStyle("-fx-alignment: CENTER;");
    }

    
    /**
     * below method fetches data from leaderboard service and populate table for Global leaderboard
     */
    private void loadGlobalLeaderboardData() {
	    	// get all entries which are sorted in descending order
	    	LeaderboardEntry[] entries = leaderboardService.getAllEntries();
	    	
	    	// need to create observable list for JavaFX table 
	    	ObservableList<LeaderboardRow> rows = FXCollections.observableArrayList();
	    	
	    	// convert entries into table rows
	    	for (int i=0; i < entries.length; i++) {
	    		LeaderboardEntry entry = entries[i];
	    		LeaderboardRow row = new LeaderboardRow(i+1, entry.getUserName(), entry.getTrophyCount(), entry.getStudyMinutes());
	    		rows.add(row);
	    	}
	    	
	    	// set data to table
	    	leaderboardTable.setItems(rows);
	    	//display current user's rank
	    	displayUserRank();
    }
    
    
    /**
     * below method fetches data from leaderboard service and populate table for room leaderboard
     */
    private void loadRoomLeaderboardData(StudyRoom room) {
	    	// get all entries which are sorted in descending order
	    	LeaderboardEntry[] entries = leaderboardService.getRoomLeaderBoard(room);
	    	
	    	// need to create observable list for JavaFX table 
	    	ObservableList<LeaderboardRow> rows = FXCollections.observableArrayList();
	    	
	    	// convert entries into table rows
	    	for (int i=0; i < entries.length; i++) {
	    		LeaderboardEntry entry = entries[i];
	    		LeaderboardRow row = new LeaderboardRow(i+1, entry.getUserName(), entry.getTrophyCount(), entry.getStudyMinutes());
	    		rows.add(row);
	    	}
	    	
	    	// set data to table
	    	leaderboardTable.setItems(rows);
	    	//display current user's rank
	    	displayUserRank();
    }
    
    
    /**
     * find and display current user's rank
     */
    private void displayUserRank() {
	    	int rank;
	    	
	    	if (currentRoom != null) {
	    		// get current user rank in the current room 
	    		rank = leaderboardService.getUserRankInRoom(currentRoom, currentUsername);
	    	} else {
	    		// get current user rank globally
	    		rank = leaderboardService.getUserRankGlobally(currentUsername);
	    	}
    
	    	if (rank == -1) {
	        userRankLabel.setText("Not Ranked");
	    } else {
	        userRankLabel.setText("#" + rank);
	    }
    }
    
    /**
     * back button handler - will return to lobby
     * 
     */
    @FXML
    private void onBack() {
	    	if (currentRoom != null) {
	    		// for room leaderboard back action, close the popup
	    		stage.close();
	    	} else {
	    		// for global LB, go back to lobby
	    		LobbyUI.show(stage);
	    	}   
    }
    
    /**
     * an inner class representing one row in the table
     */
    public static class LeaderboardRow {
        private int rank;
        private String username;
        private int trophyCount;
        private String studyTime;
        
        public LeaderboardRow(int rank, String username, int trophyCount, long studyMinutes) {
            this.rank = rank;
            this.username = username;
            this.trophyCount = trophyCount;
            
            // formating study time as "Xh Ym"
            long hours = studyMinutes / 60;
            long mins = studyMinutes % 60;
            
            if (hours > 0) {
                this.studyTime = hours + "h " + mins + "m";
            } else {
                this.studyTime = mins + "m";
            }
        }
        
        public int getRank() {
            return rank;
        }
        
        public String getUsername() {
            return username;
        }
        
        public int getTrophyCount() {
            return trophyCount;
        }
        
        public String getStudyTime() {
            return studyTime;
        }
    }
    
    private static LeaderboardService createTestLeaderboard() {
        LeaderboardService service = new LeaderboardService();
        
        service.addOrUpdateEntry(new LeaderboardEntry("Yuxuan", 18, 350, 7));
        service.addOrUpdateEntry(new LeaderboardEntry("Aditya", 15, 300, 5));
        service.addOrUpdateEntry(new LeaderboardEntry("Sagar", 12, 280, 3));
        service.addOrUpdateEntry(new LeaderboardEntry("TestUser", 10, 200, 2));
        service.addOrUpdateEntry(new LeaderboardEntry("Alice", 8, 150, 1));
        
        return service;
    }
}
