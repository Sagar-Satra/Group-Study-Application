package com.groupstudy;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RoomSearchUI {

    public static void show(Stage stage) {

        VBox root = new VBox(20);
        root.setStyle("-fx-padding: 20;");

        Label label = new Label("Room Search Page");

        Button backBtn = new Button("Back");

        backBtn.setOnAction(e -> {
            LobbyUI.show(stage); // Back to lobby
        });

        root.getChildren().addAll(label, backBtn);

        Scene scene = new Scene(root, 400, 600);

        stage.setScene(scene);
    }
}