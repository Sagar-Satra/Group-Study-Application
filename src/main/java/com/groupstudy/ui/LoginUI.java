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
 * when we start the application, this screen will appear
 * Users must log in before accessing the lobby and rooms
 */
public class LoginUI {

	// display the login screen, called from Main.java on application startup
    public static void show(Stage stage) {

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // ===== App Title =====
        Label appTitle = new Label("📚 Group Study");
        appTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        appTitle.setTextFill(Color.web("#2c3e50"));

        Label subtitle = new Label("Study together, grow together");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subtitle.setTextFill(Color.web("#7f8c8d"));

        // ===== Login Card =====
        VBox loginCard = new VBox(12);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPadding(new Insets(30));
        loginCard.setMaxWidth(320);
        loginCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label loginTitle = new Label("Login");
        loginTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        loginTitle.setTextFill(Color.web("#2c3e50"));

        // Username field
        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        usernameLabel.setTextFill(Color.web("#34495e"));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(260);
        usernameField.setStyle("-fx-padding: 10; -fx-background-radius: 5; " +
                "-fx-border-color: #bdc3c7; -fx-border-radius: 5;");

        // Password field
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        passwordLabel.setTextFill(Color.web("#34495e"));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(260);
        passwordField.setStyle("-fx-padding: 10; -fx-background-radius: 5; " +
                "-fx-border-color: #bdc3c7; -fx-border-radius: 5;");

        // Error message label (hidden by default)
        Label errorLabel = new Label();
        errorLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        errorLabel.setTextFill(Color.web("#e74c3c"));
        errorLabel.setVisible(false);

        // Login button
        Button loginBtn = new Button("Login");
        loginBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        loginBtn.setMaxWidth(260);
        loginBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                errorLabel.setVisible(true);
                return;
            }

            UserStore store = UserStore.getInstance();

            if (store.login(username, password)) {
                // login successful - go to lobby
                LobbyUI.show(stage);
            } else {
                errorLabel.setText("Invalid username or password.");
                errorLabel.setVisible(true);
            }
        });

        // Allow Enter key to submit
        passwordField.setOnAction(e -> loginBtn.fire());

        // Register link
        HBox registerRow = new HBox(5);
        registerRow.setAlignment(Pos.CENTER);

        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        noAccountLabel.setTextFill(Color.web("#7f8c8d"));

        Button registerLink = new Button("Register");
        registerLink.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        registerLink.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db; " +
                "-fx-cursor: hand; -fx-padding: 0;");
        registerLink.setOnAction(e -> RegisterUI.show(stage));

        registerRow.getChildren().addAll(noAccountLabel, registerLink);

        loginCard.getChildren().addAll(
                loginTitle,
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                errorLabel,
                loginBtn,
                registerRow
        );

        root.getChildren().addAll(appTitle, subtitle, loginCard);

        Scene scene = new Scene(root, 600, 700);
        stage.setTitle("Group Study - Login");
        stage.setScene(scene);
        stage.show();
    }

}
