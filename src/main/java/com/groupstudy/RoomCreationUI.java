package com.groupstudy;

import com.groupstudy.model.StudyRoom;
import com.groupstudy.model.User;
import com.groupstudy.service.AuthService;
import com.groupstudy.service.UserStore;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
 * Room Creation screen.
 * The logged-in user becomes the admin of the room they create.
 * Admin sets: room title, timer duration, capacity, and optional private/password.
 */
public class RoomCreationUI {

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

        Label titleLabel = new Label("➕ Create Room");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        headerRow.getChildren().addAll(backBtn, leftSpacer, titleLabel, rightSpacer);

        Separator separator = new Separator();

        // ===== Form Card =====
        VBox formCard = new VBox(12);
        formCard.setAlignment(Pos.CENTER_LEFT);
        formCard.setPadding(new Insets(20));
        formCard.setMaxWidth(340);
        formCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Room title
        Label roomTitleLabel = new Label("Room Title");
        roomTitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        roomTitleLabel.setTextFill(Color.web("#34495e"));

        TextField roomTitleField = new TextField();
        roomTitleField.setPromptText("e.g. Final Exam Prep");
        roomTitleField.setMaxWidth(300);
        roomTitleField.setStyle("-fx-padding: 10; -fx-background-radius: 5; " +
                "-fx-border-color: #bdc3c7; -fx-border-radius: 5;");

        // Capacity
        Label capacityLabel = new Label("Capacity (max users)");
        capacityLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        capacityLabel.setTextFill(Color.web("#34495e"));

        ComboBox<Integer> capacityBox = new ComboBox<>();
        capacityBox.getItems().addAll(2, 3, 4, 5, 6, 8, 10);
        capacityBox.setValue(5);
        capacityBox.setMaxWidth(300);
        capacityBox.setStyle("-fx-padding: 5; -fx-background-radius: 5;");

        // Timer duration
        Label timerLabel = new Label("Session Duration");
        timerLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        timerLabel.setTextFill(Color.web("#34495e"));

        ComboBox<String> timerBox = new ComboBox<>();
        timerBox.getItems().addAll("30 seconds (demo)", "1 minute", "5 minutes", "10 minutes", "15 minutes", "30 minutes", "1 hour");
        timerBox.setValue("5 minutes");
        timerBox.setMaxWidth(300);
        timerBox.setStyle("-fx-padding: 5; -fx-background-radius: 5;");

        // Private room toggle
        CheckBox privateCheck = new CheckBox("Private Room (requires password)");
        privateCheck.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        privateCheck.setTextFill(Color.web("#34495e"));

        // Password field (shown only when private is checked)
        Label passwordLabel = new Label("Room Password");
        passwordLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        passwordLabel.setTextFill(Color.web("#34495e"));
        passwordLabel.setVisible(false);
        passwordLabel.setManaged(false);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Set a room password");
        passwordField.setMaxWidth(300);
        passwordField.setStyle("-fx-padding: 10; -fx-background-radius: 5; " +
                "-fx-border-color: #bdc3c7; -fx-border-radius: 5;");
        passwordField.setVisible(false);
        passwordField.setManaged(false);

        privateCheck.setOnAction(e -> {
            boolean isPrivate = privateCheck.isSelected();
            passwordLabel.setVisible(isPrivate);
            passwordLabel.setManaged(isPrivate);
            passwordField.setVisible(isPrivate);
            passwordField.setManaged(isPrivate);
        });

        // Error label
        Label errorLabel = new Label();
        errorLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        errorLabel.setTextFill(Color.web("#e74c3c"));
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(300);

        // Admin info
        User currentUser = UserStore.getInstance().getCurrentUser();
        String adminName = currentUser != null ? currentUser.getName() : "Unknown";
        Label adminLabel = new Label("👑 Admin: " + adminName);
        adminLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        adminLabel.setTextFill(Color.web("#7f8c8d"));

        // Create button
        Button createBtn = new Button("Create Room");
        createBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        createBtn.setMaxWidth(300);
        createBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");

        createBtn.setOnAction(e -> {
            String title = roomTitleField.getText().trim();

            if (title.isEmpty()) {
                errorLabel.setText("Please enter a room title.");
                errorLabel.setVisible(true);
                return;
            }

            if (currentUser == null) {
                errorLabel.setText("You must be logged in to create a room.");
                errorLabel.setVisible(true);
                return;
            }

            boolean isPrivate = privateCheck.isSelected();
            String passwordHash = null;

            if (isPrivate) {
                String password = passwordField.getText();
                if (password.trim().isEmpty()) {
                    errorLabel.setText("Private rooms require a password.");
                    errorLabel.setVisible(true);
                    return;
                }
                AuthService auth = new AuthService();
                passwordHash = auth.hashPassword(password);
            }

            // parse duration
            long durationMs = parseDuration(timerBox.getValue());
            int capacity = capacityBox.getValue();

            // create room with current user as admin
            StudyRoom newRoom = new StudyRoom(title, capacity, durationMs, isPrivate, passwordHash, currentUser);

            // add to lobby's room list (using static access)
            LobbyUI.addRoom(newRoom);

            // go back to lobby
            LobbyUI.show(stage);
        });

        formCard.getChildren().addAll(
                roomTitleLabel, roomTitleField,
                capacityLabel, capacityBox,
                timerLabel, timerBox,
                privateCheck,
                passwordLabel, passwordField,
                adminLabel,
                errorLabel,
                createBtn
        );

        root.getChildren().addAll(headerRow, separator, formCard);

        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("Create Room");
        stage.setScene(scene);
    }

    /**
     * Parses the timer dropdown value into milliseconds.
     */
    private static long parseDuration(String value) {
        switch (value) {
            case "30 seconds (demo)": return 30_000;
            case "1 minute":          return 60_000;
            case "5 minutes":         return 300_000;
            case "10 minutes":        return 600_000;
            case "15 minutes":        return 900_000;
            case "30 minutes":        return 1_800_000;
            case "1 hour":            return 3_600_000;
            default:                  return 300_000;
        }
    }
}
