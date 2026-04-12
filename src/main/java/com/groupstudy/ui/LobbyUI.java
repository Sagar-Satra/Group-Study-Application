package com.groupstudy.ui;

import com.groupstudy.Main;
import com.groupstudy.controller.LeaderboardController;
import com.groupstudy.model.ActionRecord;
import com.groupstudy.model.StudyRoom;
import com.groupstudy.model.User;
import com.groupstudy.service.LeaderboardService;
import com.groupstudy.service.RoomManager;
import com.groupstudy.service.UserStore;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LobbyUI {

    private VBox roomList;
    private Stage stage;
    
    public static void show(Stage stage) {
		LobbyUI lobby = new LobbyUI();
		lobby.stage = stage;
		lobby.buildUI(stage);
	}
    
    public void buildUI(Stage primaryStage) {

        // ===== Root layout =====
        BorderPane root = new BorderPane();

        // ===== Top Bar =====
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(10);
        topBar.setStyle("-fx-background-color: #eeeeee;");

        Button profileBtn = new Button("👤");
        Button trophyBtn = new Button("🏆");
        Button leaderboardBtn = new Button("🥇"); 
        Button addBtn = new Button("➕");
        Button searchBtn = new Button("🔍");
        Button logoutBtn = new Button("🚪 Logout");
        logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                "-fx-padding: 5 10; -fx-cursor: hand; -fx-background-radius: 5; -fx-font-size: 11;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(profileBtn, spacer, leaderboardBtn, trophyBtn, addBtn, searchBtn, logoutBtn);
        
        // ===== Room List =====
        roomList = new VBox();
        roomList.setSpacing(20);
        roomList.setPadding(new Insets(20));
        roomList.setAlignment(Pos.TOP_CENTER);
        
        // get list of rooms and display them in lobby
        refreshRoomList();

        // ===== Timeline (auto update every second) =====
        Timeline timeline = new Timeline(
    			new KeyFrame(Duration.seconds(1), e -> {
    				// get list of rooms from RoomManager
    				RoomManager roomManager = Main.getRoomManager();
    				// checking if the room session ended
    				for (String roomId : roomManager.getAllRoom().keySet()) {
    					StudyRoom room = roomManager.getRoom(roomId);

    					if (!room.isClosed() && room.isSessionOver()) {
    						room.endSession();
    					}
    				}
    				// cleaning expired rooms
    				cleanRooms();
    				// refresh the UI with updated rooms
    				refreshRoomList();
    			})
    		);

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // ===== Button actions =====
        profileBtn.setOnAction(e -> {
			User currentUser = UserStore.getInstance().getCurrentUser();
			if (currentUser != null) {
				UserProfileUI.show(primaryStage, currentUser);
			}
		});
        
        // view trophies action button - uses actual logged-in user
        trophyBtn.setOnAction(e -> {
			User currentUser = UserStore.getInstance().getCurrentUser();
			if (currentUser != null) {
				TrophyCollectionUI.show(primaryStage, currentUser);
			}
		});
        
        // view leaderboard action button - uses actual logged-in user
        leaderboardBtn.setOnAction(e -> {
			User currentUser = UserStore.getInstance().getCurrentUser();
			if (currentUser != null) {
				LeaderboardService leaderboardService = Main.getLeaderboardService();
				LeaderboardController.show(primaryStage, currentUser.getName(), null, leaderboardService);
			}
		});
        
        addBtn.setOnAction(e -> {
			RoomCreationUI.show(primaryStage);
		});
		
		searchBtn.setOnAction(e -> {
			RoomSearchUI.show(primaryStage);
		});
        
        // logout action - clear session and go back to login
        logoutBtn.setOnAction(e -> {
            UserStore.getInstance().logout();
            LoginUI.show(primaryStage);
        });

        // ===== Layout =====
        root.setTop(topBar);
        root.setCenter(roomList);

        Scene scene = new Scene(root, 600, 700);

        primaryStage.setTitle("Lobby");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    // rebuild room cards UI
    private void refreshRoomList() {
		roomList.getChildren().clear();

		RoomManager roomManager = Main.getRoomManager();

		for (String roomId : roomManager.getAllRoom().keySet()) {
			StudyRoom room = roomManager.getRoom(roomId);

			RoomCardUI card = new RoomCardUI(room);

			card.setOnMouseClicked(e -> {
				User currentUser = UserStore.getInstance().getCurrentUser();
				if (currentUser == null) return;
				
				room.addUser(currentUser);
				
				currentUser.recordAction(new ActionRecord(ActionRecord.ActionType.JOIN, room.getTitle()));
				
				// Open study room (Pokemon assigned inside StudyRoomUI constructor)
				StudyRoomUI.show(stage, room, currentUser);
			});

			roomList.getChildren().add(card);
		}
	}

    // helper method to get the stage (window)
    private Stage getStage() {
    		return (Stage) roomList.getScene().getWindow();
    }
    
    // remove rooms after delay
    private void cleanRooms() {
		RoomManager roomManager = Main.getRoomManager();
		
		for (String roomId : roomManager.getAllRoom().keySet()) {
			StudyRoom room = roomManager.getRoom(roomId);
			if (room.shouldRemove()) {
				roomManager.removeRoom(roomId);
			}
		}
	}
    
    /**
     * Static method to add a room to the lobby.
     * Called from RoomCreationUI when a user creates a new room.
     */
    public static void addRoom(StudyRoom room) {
		RoomManager roomManager = Main.getRoomManager();
		
		if (roomManager != null) {
			roomManager.addRoom(room);
			System.out.println("== Added room to lobby: " + room.getTitle());
		}
	}
    
    /**
     * Search for a room by its ID across all rooms in the lobby.
     * Called from RoomSearchUI.
     */
    public static StudyRoom findRoomById(String roomId) {
		if (roomId == null) return null;
		
		RoomManager roomManager = Main.getRoomManager();
		if (roomManager == null) return null;
		
		return roomManager.getRoom(roomId);
	}

}