package com.groupstudy;

import com.groupstudy.controller.LeaderboardController;
import com.groupstudy.implementation.ArrayListImplementation;
import com.groupstudy.model.ActionRecord;
import com.groupstudy.model.StudyRoom;
import com.groupstudy.model.User;
import com.groupstudy.service.PokemonService;
import com.groupstudy.service.UserStore;
import com.groupstudy.ui.StudyRoomUI;
import com.groupstudy.ui.TrophyCollectionUI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LobbyUI extends Application {

    // store rooms
    private static ArrayListImplementation<StudyRoom> rooms = new ArrayListImplementation<>();

    // UI container for room cards
    private VBox roomList;

    @Override
    public void start(Stage primaryStage) {

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

        // ===== Data =====
        // Only add default rooms if list is empty (first launch)
        if (rooms.getLength() == 0) {
            StudyRoom room1 = new StudyRoom("Final Prep", 5, 60000, false, null);
            StudyRoom room2 = new StudyRoom("Math Study", 4, 90000, false, null);

            rooms.add(room1);
            rooms.add(room2);
        }

        // ===== Initial render =====
        refreshRoomList();

        // ===== Timeline (auto update every second) =====
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {

                // check session end
                for (int i = 0; i < rooms.getLength(); i++) {
                    StudyRoom room = rooms.get(i);

                    if (!room.isClosed() && room.isSessionOver()) {
                        room.endSession();
                    }
                }

                // remove expired rooms
                cleanRooms();

                // redraw UI
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
                LeaderboardController.show(primaryStage, currentUser.getName(), null);
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

        Scene scene = new Scene(root, 400, 600);

        primaryStage.setTitle("Lobby");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void show(Stage stage) {

        LobbyUI app = new LobbyUI();
        app.start(stage);
    }
    
    // rebuild room cards UI
    private void refreshRoomList() {

        roomList.getChildren().clear();

        for (int i = 0; i < rooms.getLength(); i++) {

            StudyRoom room = rooms.get(i);

            RoomCardUI card = new RoomCardUI(room);

            card.setOnMouseClicked(e -> {
            	// get the actual logged-in user from UserStore
                User currentUser = UserStore.getInstance().getCurrentUser();
                if (currentUser == null) return;
                
                room.addUser(currentUser);
                
                // record JOIN action in user's action history (LinkedList)
                currentUser.recordAction(new ActionRecord(ActionRecord.ActionType.JOIN, room.getTitle()));
                
                // dummy users to test the UI
                User dummy1 = new User("Aditya");
                User dummy2 = new User("Yuxuan");
                
                // Assign them Pokemon
                PokemonService pokemonService = new PokemonService();
                dummy1.setCurrentPokemon(pokemonService.assignRandomPokemon());
                dummy2.setCurrentPokemon(pokemonService.assignRandomPokemon());
                
                room.addUser(dummy1);
                room.addUser(dummy2);
                StudyRoomUI.show(getStage(), room, currentUser);
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

        for (int i = 0; i < rooms.getLength(); i++) {

            StudyRoom room = rooms.get(i);

            if (room.shouldRemove()) {
                rooms.remove(i);
                i--; // prevent skipping
            }
        }
    }
    
    /**
     * Static method to add a room to the lobby.
     * Called from RoomCreationUI when a user creates a new room.
     */
    public static void addRoom(StudyRoom room) {
    	rooms.add(room);
    }
    
    /**
     * Search for a room by its ID across all rooms in the lobby.
     * Called from RoomSearchUI.
     */
    public static StudyRoom findRoomById(String roomId) {
    	if (roomId == null) return null;
    	for (int i = 0; i < rooms.getLength(); i++) {
    		StudyRoom room = rooms.get(i);
    		if (room.getRoomId().equals(roomId)) {
    			return room;
    		}
    	}
    	return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}