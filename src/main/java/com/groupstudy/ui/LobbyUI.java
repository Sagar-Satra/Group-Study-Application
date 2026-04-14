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
import javafx.scene.control.Label;
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

        logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(profileBtn, spacer, leaderboardBtn, trophyBtn, addBtn, searchBtn, logoutBtn);

        // ===== Room List =====
        roomList = new VBox(20);
        roomList.setPadding(new Insets(20));
        roomList.setAlignment(Pos.TOP_CENTER);

        refreshRoomList();

        // ===== Auto refresh =====
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                RoomManager roomManager = Main.getRoomManager();

                for (String roomId : roomManager.getAllRoom().keySet()) {
                    StudyRoom room = roomManager.getRoom(roomId);
                    if (!room.isClosed() && room.isSessionOver()) {
                        room.endSession();
                    }
                }

                cleanRooms();
                refreshRoomList();
            })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // ===== Button actions =====
        profileBtn.setOnAction(e -> {
            User user = UserStore.getInstance().getCurrentUser();
            if (user != null) UserProfileUI.show(primaryStage, user);
        });

        trophyBtn.setOnAction(e -> {
            User user = UserStore.getInstance().getCurrentUser();
            if (user != null) TrophyCollectionUI.show(primaryStage, user);
        });

        leaderboardBtn.setOnAction(e -> {
            User user = UserStore.getInstance().getCurrentUser();
            if (user != null) {
                LeaderboardService service = Main.getLeaderboardService();
                LeaderboardController.show(primaryStage, user.getName(), null, service);
            }
        });

        addBtn.setOnAction(e -> RoomCreationUI.show(primaryStage));
        searchBtn.setOnAction(e -> RoomSearchUI.show(primaryStage));

        logoutBtn.setOnAction(e -> {
            UserStore.getInstance().logout();
            LoginUI.show(primaryStage);
        });

        root.setTop(topBar);
        root.setCenter(roomList);

        Scene scene = new Scene(root, 600, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lobby");
        primaryStage.show();
    }

    private void refreshRoomList() {

        roomList.getChildren().clear();

        RoomManager roomManager = Main.getRoomManager();
        User currentUser = UserStore.getInstance().getCurrentUser();

        // ===== Sections =====
        VBox myRoomSection = new VBox(10);
        VBox publicRoomSection = new VBox(10);

        Label myTitle = new Label("🏠 My Rooms");
        Label publicTitle = new Label("🌐 Public Rooms");

        myTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        publicTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        for (String roomId : roomManager.getAllRoom().keySet()) {

            StudyRoom room = roomManager.getRoom(roomId);

            RoomCardUI card = new RoomCardUI(room);

            card.setOnMouseClicked(e -> {
                if (currentUser == null) return;
                
                // if the room is full, don't allow user to join, except if the user is admin 
                boolean isAdmin = room.isAdmin(currentUser);
                if (!isAdmin && room.getActiveUserCount() >= room.getCapacity()) {
                		showAlert("Room is Full!", room.getTitle() + " has reached maximum capacity (" + room.getCapacity() + " users).");
                    return;
                }
                room.addUser(currentUser);
                currentUser.setLastInteractionTime(0);
                currentUser.recordAction(new ActionRecord(ActionRecord.ActionType.JOIN, room.getTitle()));

                StudyRoomUI.show(stage, room, currentUser);
            });

            // ===== grouping logic =====

            // My Rooms: show ALL rooms created by user (including private)
            if (room.getAdmin() != null &&
                currentUser != null &&
                room.getAdmin().getName().equals(currentUser.getName())) {

                myRoomSection.getChildren().add(card);

            }
            // Public Rooms: only show non-private rooms
            else if (!room.isPrivate()) {

                publicRoomSection.getChildren().add(card);
            }
        }

        myRoomSection.setAlignment(Pos.TOP_CENTER);
        if (myRoomSection.getChildren().isEmpty()) {
            myRoomSection.getChildren().add(new Label("No rooms created yet."));
        }
        publicRoomSection.setAlignment(Pos.TOP_CENTER);

        // ===== assemble =====
        roomList.getChildren().addAll(
            myTitle,
            myRoomSection,
            publicTitle,
            publicRoomSection
        );
    }

    private void cleanRooms() {
        RoomManager roomManager = Main.getRoomManager();

        for (String roomId : roomManager.getAllRoom().keySet()) {
            StudyRoom room = roomManager.getRoom(roomId);
            if (room.shouldRemove()) {
                roomManager.removeRoom(roomId);
            }
        }
    }

    public static void addRoom(StudyRoom room) {
        Main.getRoomManager().addRoom(room);
    }

    public static StudyRoom findRoomById(String roomId) {
        return Main.getRoomManager().getRoom(roomId);
    }
    
    public static void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}