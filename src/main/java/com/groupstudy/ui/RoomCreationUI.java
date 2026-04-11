package com.groupstudy.ui;

import com.groupstudy.model.StudyRoom;
import com.groupstudy.service.AuthService;
import com.groupstudy.implementation.ArrayListImplementation;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RoomCreationUI {

    public static void show(Stage stage) {

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        // ===== Inputs =====
        TextField nameField = new TextField();
        nameField.setPromptText("Room Name");

        TextField capacityField = new TextField();
        capacityField.setPromptText("Capacity (e.g. 5)");

        TextField durationField = new TextField();
        durationField.setPromptText("Duration in seconds (e.g. 60)");

        // ===== Public / Private toggle =====
        CheckBox privateCheck = new CheckBox("Private Room");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setVisible(false);

        privateCheck.setOnAction(e -> {
            passwordField.setVisible(privateCheck.isSelected());
        });

        // ===== Buttons =====
        Button createBtn = new Button("Create");
        Button backBtn = new Button("Back");

        Label message = new Label();

        // ===== Create logic =====
        createBtn.setOnAction(e -> {

            try {
                String name = nameField.getText();
                int capacity = Integer.parseInt(capacityField.getText());
                int duration = Integer.parseInt(durationField.getText()) * 1000;

                boolean isPrivate = privateCheck.isSelected();

                String passwordHash = null;

                if (isPrivate) {
                    String password = passwordField.getText();

                    if (password == null || password.isEmpty()) {
                        message.setText("Password required for private room");
                        return;
                    }

                    AuthService auth = new AuthService();
                    passwordHash = auth.hashPassword(password);
                }

                // ⭐ create room
                StudyRoom newRoom = new StudyRoom(
                        name,
                        capacity,
                        duration,
                        isPrivate,
                        passwordHash
                );

                // add to global room list
                ArrayListImplementation<StudyRoom> rooms = LobbyUI.getRooms();
                rooms.add(newRoom);

                System.out.println("Room created: " + name);

                // go back to lobby
                LobbyUI.show(stage);

            } catch (Exception ex) {
                message.setText("Invalid input");
            }
        });

        // ===== Back =====
        backBtn.setOnAction(e -> {
            LobbyUI.show(stage);
        });

        // ===== Layout =====
        root.getChildren().addAll(
                new Label("Create Room"),
                nameField,
                capacityField,
                durationField,
                privateCheck,
                passwordField,
                createBtn,
                backBtn,
                message
        );

        Scene scene = new Scene(root, 400, 600);
        stage.setScene(scene);
    }
}