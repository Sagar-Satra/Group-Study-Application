package com.groupstudy;

import com.groupstudy.model.StudyRoom;
import com.groupstudy.service.RoomManager;
import com.groupstudy.adt.ListInterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LobbyUI extends Application {

    private RoomManager manager = new RoomManager();

    @Override
    public void start(Stage stage) {

        initRooms();

        VBox root = new VBox(20);
        root.setStyle("-fx-padding: 30;");

        ListInterface<StudyRoom> rooms = manager.getPublicRooms();

        System.out.println("length = " + rooms.getLength());
        for (int i = 0; i < rooms.getLength(); i++) {
            StudyRoom room = rooms.get(i);

            RoomCardUI card = new RoomCardUI(room);

            card.setOnMouseClicked(e -> {
                System.out.println("Enter room: " + room.getTitle());
            });

            root.getChildren().add(card);
        }

        Scene scene = new Scene(root, 350, 500);

        stage.setTitle("Lobby");
        stage.setScene(scene);
        stage.show();
    }

    private void initRooms() {
        manager.addRoom(new StudyRoom("Final Prep", 5, 60000, false, null));
        manager.addRoom(new StudyRoom("Math Study", 4, 70000, false, null));
    }

    public static void main(String[] args) {
        launch();
    }
}