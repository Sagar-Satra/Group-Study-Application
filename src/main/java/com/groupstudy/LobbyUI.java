package com.groupstudy;

import com.groupstudy.implementation.ArrayListImplementation;
import com.groupstudy.model.StudyRoom;

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

public class LobbyUI extends Application {

    @Override
    public void start(Stage primaryStage) {

        // ===== Root layout =====
        BorderPane root = new BorderPane();

        // ===== Top Bar =====
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(10);
        topBar.setStyle("-fx-background-color: #eeeeee;");

        // Left side: Profile button
        Button profileBtn = new Button("👤");

        // Right side: Create and Search buttons
        Button addBtn = new Button("➕");
        Button searchBtn = new Button("🔍");

        // Spacer pushes buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Add components into top bar
        topBar.getChildren().addAll(profileBtn, spacer, addBtn, searchBtn);

        // ===== Room List (Center) =====
        VBox roomList = new VBox();
        roomList.setSpacing(20);
        roomList.setPadding(new Insets(20));

        // ===== Sample Data (for testing UI) =====
        ArrayListImplementation<StudyRoom> rooms = new ArrayListImplementation<>();

        // Create sample rooms (duration in milliseconds)
        StudyRoom room1 = new StudyRoom("Final Prep", 5, 60000, false, null);
        StudyRoom room2 = new StudyRoom("Math Study", 4, 70000, false, null);

        rooms.add(room1);
        rooms.add(room2);

        // ===== Generate Room Cards dynamically =====
        for (int i = 0; i < rooms.getLength(); i++) {

            StudyRoom room = rooms.get(i);

            RoomCardUI card = new RoomCardUI(room);

            // Click event (later can navigate to Room UI)
            card.setOnMouseClicked(e -> {
                System.out.println("Enter room: " + room.getTitle());
            });

            roomList.getChildren().add(card);
        }

        // ===== Button Actions =====

        // Navigate to profile page
        profileBtn.setOnAction(e -> {
            System.out.println("Go to profile page");
        });

        // Create new room
        addBtn.setOnAction(e -> {
            System.out.println("Create new room");
        });

        // Search rooms
        searchBtn.setOnAction(e -> {
            System.out.println("Search rooms");
        });

        // ===== Set Layout =====
        root.setTop(topBar);
        root.setCenter(roomList);

        // ===== Scene =====
        Scene scene = new Scene(root, 400, 600);

        primaryStage.setTitle("Lobby");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}