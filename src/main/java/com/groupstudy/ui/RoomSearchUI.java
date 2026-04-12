package com.groupstudy.ui;

import com.groupstudy.model.ActionRecord;
import com.groupstudy.model.StudyRoom;
import com.groupstudy.model.User;
import com.groupstudy.service.AuthService;
import com.groupstudy.service.UserStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Room Search screen.
 * Users can search for a room by Room ID and join it.
 * Private rooms require a password to join.
 */
public class RoomSearchUI {

    public static void show(Stage stage) {

        VBox root = new VBox(12);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // ===== Header =====
        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER);

        Button backBtn = new Button("← Back");
        backBtn.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        backBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-padding: 8 16; -fx-cursor: hand; -fx-background-radius: 5;");
        backBtn.setOnAction(e -> LobbyUI.show(stage));

        Label titleLabel = new Label("🔍 Search Room");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        headerRow.getChildren().addAll(backBtn, leftSpacer, titleLabel, rightSpacer);

        Separator separator = new Separator();

        // ===== Search Card =====
        VBox searchCard = new VBox(12);
        searchCard.setAlignment(Pos.CENTER_LEFT);
        searchCard.setPadding(new Insets(20));
        searchCard.setMaxWidth(340);
        searchCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label searchLabel = new Label("Enter Room ID");
        searchLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        searchLabel.setTextFill(Color.web("#34495e"));

        TextField searchField = new TextField();
        searchField.setPromptText("e.g. ROOM-123456");
        searchField.setMaxWidth(300);
        searchField.setStyle("-fx-padding: 10; -fx-background-radius: 5; " +
                "-fx-border-color: #bdc3c7; -fx-border-radius: 5;");

        // result area (initially empty)
        VBox resultArea = new VBox(10);
        resultArea.setAlignment(Pos.CENTER_LEFT);

        // message label
        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);
        messageLabel.setVisible(false);

        // search button
        Button searchBtn = new Button("Search");
        searchBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        searchBtn.setMaxWidth(300);
        searchBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");

        searchBtn.setOnAction(e -> {
            resultArea.getChildren().clear();
            messageLabel.setVisible(false);

            String roomId = searchField.getText().trim();
            if (roomId.isEmpty()) {
                messageLabel.setText("Please enter a Room ID.");
                messageLabel.setTextFill(Color.web("#e74c3c"));
                messageLabel.setVisible(true);
                return;
            }

            // search across all rooms in the lobby
            StudyRoom foundRoom = LobbyUI.findRoomById(roomId);

            if (foundRoom == null) {
                messageLabel.setText("No room found with ID: " + roomId);
                messageLabel.setTextFill(Color.web("#e74c3c"));
                messageLabel.setVisible(true);
            } else if (foundRoom.isClosed()) {
                messageLabel.setText("This room's session has already ended.");
                messageLabel.setTextFill(Color.web("#e67e22"));
                messageLabel.setVisible(true);
            } else if (foundRoom.getCurrentSize() >= foundRoom.getCapacity()) {
                messageLabel.setText("This room is full (" + foundRoom.getCapacity() + "/" + foundRoom.getCapacity() + ").");
                messageLabel.setTextFill(Color.web("#e67e22"));
                messageLabel.setVisible(true);
            } else {
                // room found - show details and join option
                VBox roomDetails = createRoomDetails(stage, foundRoom, messageLabel);
                resultArea.getChildren().add(roomDetails);
            }
        });

        // allow Enter key
        searchField.setOnAction(e -> searchBtn.fire());

        searchCard.getChildren().addAll(searchLabel, searchField, searchBtn, messageLabel, resultArea);

        root.getChildren().addAll(headerRow, separator, searchCard);

        Scene scene = new Scene(root, 600, 700);
        stage.setTitle("Search Room");
        stage.setScene(scene);
    }


    /**
     * Creates a card showing the found room's details with a join button.
     */
    private static VBox createRoomDetails(Stage stage, StudyRoom room, Label messageLabel) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10;");
        card.setMaxWidth(300);

        Label foundLabel = new Label("Room Found!");
        foundLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        foundLabel.setTextFill(Color.web("#27ae60"));

        Label nameLabel = new Label("📚 " + room.getTitle());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        nameLabel.setTextFill(Color.web("#2c3e50"));

        String adminName = room.getAdmin() != null ? room.getAdmin().getName() : "Unknown";
        Label adminLabel = new Label("👑 Admin: " + adminName);
        adminLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        adminLabel.setTextFill(Color.web("#7f8c8d"));

        Label capacityLabel = new Label("👥 " + room.getCurrentSize() + " / " + room.getCapacity());
        capacityLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        capacityLabel.setTextFill(Color.web("#34495e"));

        Label typeLabel = new Label(room.isPrivate() ? "🔒 Private Room" : "🌐 Public Room");
        typeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        typeLabel.setTextFill(Color.web("#7f8c8d"));

        card.getChildren().addAll(foundLabel, nameLabel, adminLabel, capacityLabel, typeLabel);

        if (room.isPrivate()) {
            // show password field for private rooms
            Label passLabel = new Label("Enter room password:");
            passLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            passLabel.setTextFill(Color.web("#34495e"));

            PasswordField passField = new PasswordField();
            passField.setPromptText("Room password");
            passField.setMaxWidth(270);
            passField.setStyle("-fx-padding: 8; -fx-background-radius: 5; " +
                    "-fx-border-color: #bdc3c7; -fx-border-radius: 5;");

            Button joinBtn = createJoinButton(stage, room, messageLabel, passField);
            passField.setOnAction(e -> joinBtn.fire());

            card.getChildren().addAll(passLabel, passField, joinBtn);
        } else {
            // public room - just show join button
            Button joinBtn = createJoinButton(stage, room, messageLabel, null);
            card.getChildren().add(joinBtn);
        }

        return card;
    }

    private static Button createJoinButton(Stage stage, StudyRoom room, Label messageLabel, PasswordField passField) {
        Button joinBtn = new Button("Join Room");
        joinBtn.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        joinBtn.setMaxWidth(270);
        joinBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                "-fx-padding: 8 16; -fx-cursor: hand; -fx-background-radius: 5;");

        joinBtn.setOnAction(e -> {
            User currentUser = UserStore.getInstance().getCurrentUser();
            if (currentUser == null) {
                messageLabel.setText("You must be logged in.");
                messageLabel.setTextFill(Color.web("#e74c3c"));
                messageLabel.setVisible(true);
                return;
            }

            // verify password for private rooms
            if (room.isPrivate() && passField != null) {
                String password = passField.getText();
                if (password.trim().isEmpty()) {
                    messageLabel.setText("Please enter the room password.");
                    messageLabel.setTextFill(Color.web("#e74c3c"));
                    messageLabel.setVisible(true);
                    return;
                }
                AuthService auth = new AuthService();
                if (!room.verifyPassword(password, auth)) {
                    messageLabel.setText("Incorrect password.");
                    messageLabel.setTextFill(Color.web("#e74c3c"));
                    messageLabel.setVisible(true);
                    return;
                }
            }

            // join the room
            room.addUser(currentUser);
            currentUser.recordAction(new ActionRecord(ActionRecord.ActionType.JOIN, room.getTitle()));
            StudyRoomUI.show(stage, room, currentUser);
        });

        return joinBtn;
    }
}
