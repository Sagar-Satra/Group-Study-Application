package com.groupstudy.ui;

import com.groupstudy.implementation.ArrayListImplementation;
import com.groupstudy.model.StudyRoom;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import com.groupstudy.service.AuthService;
import com.groupstudy.model.User;

public class RoomSearchUI {

    public static void show(Stage stage) {

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        TextField searchField = new TextField();
        searchField.setPromptText("Enter room name or ID...");

        VBox resultList = new VBox(10);

        Button backBtn = new Button("Back");

        // ⭐ get SAME rooms from Lobby
        ArrayListImplementation<StudyRoom> rooms = LobbyUI.getRooms();

        // 🔍 search logic
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {

            resultList.getChildren().clear();

            if (newVal == null || newVal.isEmpty()) return;

            for (int i = 0; i < rooms.getLength(); i++) {

                StudyRoom room = rooms.get(i);

                // ⭐ search BOTH public + private
                if (room.getTitle().toLowerCase().contains(newVal.toLowerCase())) {

                    RoomCardUI card = new RoomCardUI(room);

                    card.setOnMouseClicked(e -> {

                        if (room.isPrivate()) {

                            TextInputDialog dialog = new TextInputDialog();
                            dialog.setTitle("Private Room");
                            dialog.setHeaderText("Enter password for " + room.getTitle());

                            dialog.showAndWait().ifPresent(input -> {

                                boolean ok = room.verifyPassword(input, new AuthService());

                                if (ok) {
                                    User user = new User("TestUser");
                                    room.addUser(user);
                                    StudyRoomUI.show(stage, room, user);
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setHeaderText("Wrong Password");
                                    alert.setContentText("Try again.");
                                    alert.show();
                                }
                            });

                        } else {
                            User user = new User("TestUser");
                            room.addUser(user);
                            StudyRoomUI.show(stage, room, user);
                        }
                    });

                    resultList.getChildren().add(card);
                }
            }
        });

        // 🔙 back
        backBtn.setOnAction(e -> {
            LobbyUI.show(stage);
        });

        root.getChildren().addAll(searchField, resultList, backBtn);

        Scene scene = new Scene(root, 400, 600);
        stage.setScene(scene);
    }
}