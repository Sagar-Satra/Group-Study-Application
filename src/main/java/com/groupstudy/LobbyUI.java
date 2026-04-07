package com.groupstudy;

import com.groupstudy.implementation.ArrayListImplementation;
import com.groupstudy.model.StudyRoom;

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
        Button addBtn = new Button("➕");
        Button searchBtn = new Button("🔍");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(profileBtn, spacer, addBtn, searchBtn);
        
        // ===== Room List =====
        roomList = new VBox();
        roomList.setSpacing(20);
        roomList.setPadding(new Insets(20));

        // ===== Data =====
        rooms = new ArrayListImplementation<>();

        if (rooms.getLength() == 0) {
            StudyRoom room1 = new StudyRoom("Final Prep", 5, 10000, false, null);
            StudyRoom room2 = new StudyRoom("Math Study", 4, 15000, false, null);

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
            UserProfileUI.show(primaryStage);
        });
        
        addBtn.setOnAction(e -> {
        	RoomCreationUI.show(primaryStage);
        });
        
        searchBtn.setOnAction(e -> {
        	RoomSearchUI.show(primaryStage);
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
                System.out.println("Enter room: " + room.getTitle());
            });

            roomList.getChildren().add(card);
        }
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

    public static void main(String[] args) {
        launch(args);
    }
}