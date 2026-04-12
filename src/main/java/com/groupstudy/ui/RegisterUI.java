package com.groupstudy.ui;

import com.groupstudy.service.UserStore;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Registration screen where new users can create an account.
 * Data is stored in-memory via UserStore.
 */
public class RegisterUI {

    public static void show(Stage stage) {

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // ===== App Title =====
        Label appTitle = new Label("📚 Group Study");
        appTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        appTitle.setTextFill(Color.web("#2c3e50"));

        Label subtitle = new Label("Create your account");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subtitle.setTextFill(Color.web("#7f8c8d"));

        // ===== Register Card =====
        VBox registerCard = new VBox(12);
        registerCard.setAlignment(Pos.CENTER);
        registerCard.setPadding(new Insets(30));
        registerCard.setMaxWidth(320);
        registerCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label registerTitle = new Label("Register");
        registerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        registerTitle.setTextFill(Color.web("#2c3e50"));

        // Username field
        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        usernameLabel.setTextFill(Color.web("#34495e"));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Choose a username");
        usernameField.setMaxWidth(260);
        usernameField.setStyle("-fx-padding: 10; -fx-background-radius: 5; " +
                "-fx-border-color: #bdc3c7; -fx-border-radius: 5;");

        // Password field
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        passwordLabel.setTextFill(Color.web("#34495e"));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Choose a password");
        passwordField.setMaxWidth(260);
        passwordField.setStyle("-fx-padding: 10; -fx-background-radius: 5; " +
                "-fx-border-color: #bdc3c7; -fx-border-radius: 5;");

        // Confirm password field
        Label confirmLabel = new Label("Confirm Password");
        confirmLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        confirmLabel.setTextFill(Color.web("#34495e"));

        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Re-enter your password");
        confirmField.setMaxWidth(260);
        confirmField.setStyle("-fx-padding: 10; -fx-background-radius: 5; " +
                "-fx-border-color: #bdc3c7; -fx-border-radius: 5;");

        // Message label for errors / success
        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        messageLabel.setVisible(false);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(260);

        // Register button
        Button registerBtn = new Button("Create Account");
        registerBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        registerBtn.setMaxWidth(260);
        registerBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");

        registerBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirm = confirmField.getText();

            // Validate inputs
            if (username.trim().isEmpty() || password.trim().isEmpty() || confirm.trim().isEmpty()) {
                messageLabel.setText("Please fill in all fields.");
                messageLabel.setTextFill(Color.web("#e74c3c"));
                messageLabel.setVisible(true);
                return;
            }

            if (password.length() < 3) {
                messageLabel.setText("Password must be at least 3 characters.");
                messageLabel.setTextFill(Color.web("#e74c3c"));
                messageLabel.setVisible(true);
                return;
            }

            if (!password.equals(confirm)) {
                messageLabel.setText("Passwords do not match.");
                messageLabel.setTextFill(Color.web("#e74c3c"));
                messageLabel.setVisible(true);
                return;
            }

            UserStore store = UserStore.getInstance();

            if (store.register(username, password)) {
                messageLabel.setText("Account created! Redirecting to login...");
                messageLabel.setTextFill(Color.web("#27ae60"));
                messageLabel.setVisible(true);

                // Short delay then go to login screen
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
                pause.setOnFinished(event -> LoginUI.show(stage));
                pause.play();
            } else {
                messageLabel.setText("Username already taken. Try a different one.");
                messageLabel.setTextFill(Color.web("#e74c3c"));
                messageLabel.setVisible(true);
            }
        });

        // Allow Enter key to submit
        confirmField.setOnAction(e -> registerBtn.fire());

        // Back to login link
        HBox loginRow = new HBox(5);
        loginRow.setAlignment(Pos.CENTER);

        Label hasAccountLabel = new Label("Already have an account?");
        hasAccountLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        hasAccountLabel.setTextFill(Color.web("#7f8c8d"));

        Button loginLink = new Button("Login");
        loginLink.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        loginLink.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db; " +
                "-fx-cursor: hand; -fx-padding: 0;");
        loginLink.setOnAction(e -> LoginUI.show(stage));

        loginRow.getChildren().addAll(hasAccountLabel, loginLink);

        registerCard.getChildren().addAll(
                registerTitle,
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                confirmLabel, confirmField,
                messageLabel,
                registerBtn,
                loginRow
        );

        root.getChildren().addAll(appTitle, subtitle, registerCard);

        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("Group Study - Register");
        stage.setScene(scene);
    }
}
