package com.groupstudy;

import com.groupstudy.adt.ListInterface;
import com.groupstudy.model.ActionRecord;
import com.groupstudy.model.User;
import com.groupstudy.service.UserStore;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * User Profile screen showing:
 * - User info and study stats
 * - Daily study streak
 * - Action history (traversed from the LinkedList)
 */
public class UserProfileUI {

    public static void show(Stage stage, User user) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");
        root.setPadding(new Insets(15));

        // ===== Top Section - Header =====
        VBox topSection = createTopSection(stage, user);

        // ===== Center Section - Stats + History =====
        ScrollPane centerSection = createCenterSection(user);

        root.setTop(topSection);
        root.setCenter(centerSection);

        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("Profile - " + user.getName());
        stage.setScene(scene);
    }

    // fallback for old calls without user param
    public static void show(Stage stage) {
        User currentUser = UserStore.getInstance().getCurrentUser();
        if (currentUser != null) {
            show(stage, currentUser);
        } else {
            LoginUI.show(stage);
        }
    }

    private static VBox createTopSection(Stage stage, User user) {
        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(0, 0, 10, 0));

        // header row
        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER);

        Button backButton = new Button("← Back");
        backButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        backButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-padding: 8 16; -fx-cursor: hand; -fx-background-radius: 5;");
        backButton.setOnAction(e -> LobbyUI.show(stage));

        Label titleLabel = new Label("👤 My Profile");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        headerRow.getChildren().addAll(backButton, leftSpacer, titleLabel, rightSpacer);

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #bdc3c7;");

        topBox.getChildren().addAll(headerRow, separator);
        return topBox;
    }

    private static ScrollPane createCenterSection(User user) {
        VBox content = new VBox(15);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.TOP_CENTER);

        // ===== User Info Card =====
        VBox userInfoCard = createUserInfoCard(user);

        // ===== Stats Card =====
        VBox statsCard = createStatsCard(user);

        // ===== Streak Card =====
        VBox streakCard = createStreakCard(user);

        // ===== Action History Card =====
        VBox historyCard = createActionHistoryCard(user);

        content.getChildren().addAll(userInfoCard, statsCard, streakCard, historyCard);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        return scrollPane;
    }

    private static VBox createUserInfoCard(User user) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setMaxWidth(360);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // avatar circle with initial
        Label avatarLabel = new Label(user.getName().substring(0, 1).toUpperCase());
        avatarLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        avatarLabel.setTextFill(Color.WHITE);
        avatarLabel.setAlignment(Pos.CENTER);
        avatarLabel.setMinWidth(70);
        avatarLabel.setMinHeight(70);
        avatarLabel.setMaxWidth(70);
        avatarLabel.setMaxHeight(70);
        avatarLabel.setStyle("-fx-background-color: #3498db; -fx-background-radius: 35;");

        Label nameLabel = new Label(user.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        nameLabel.setTextFill(Color.web("#2c3e50"));

        Label statusLabel = new Label("Status: " + user.getCurrentStatus().name());
        statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        statusLabel.setTextFill(Color.web("#7f8c8d"));

        card.getChildren().addAll(avatarLabel, nameLabel, statusLabel);
        return card;
    }

    private static VBox createStatsCard(User user) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setMaxWidth(360);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label header = new Label("📊 Study Stats");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        header.setTextFill(Color.web("#2c3e50"));

        // stats row
        HBox statsRow = new HBox(15);
        statsRow.setAlignment(Pos.CENTER);

        VBox studyTimeBox = createStatItem("⏱", "Total Time", user.getTotalStudyMinutes() + " min");
        VBox trophyBox = createStatItem("🏆", "Trophies", String.valueOf(user.getTrophyCount()));
        VBox pokemonBox = createStatItem("🎮", "Pokemon",
                user.hasActivePokemon() ? user.getCurrentPokemon().getCurrentName() : "None");

        statsRow.getChildren().addAll(studyTimeBox, trophyBox, pokemonBox);

        card.getChildren().addAll(header, statsRow);
        return card;
    }

    private static VBox createStatItem(String icon, String label, String value) {
        VBox item = new VBox(3);
        item.setAlignment(Pos.CENTER);
        item.setPadding(new Insets(8));
        item.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        item.setMinWidth(90);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        valueLabel.setTextFill(Color.web("#2c3e50"));

        Label nameLabel = new Label(label);
        nameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        nameLabel.setTextFill(Color.web("#95a5a6"));

        item.getChildren().addAll(iconLabel, valueLabel, nameLabel);
        return item;
    }

    private static VBox createStreakCard(User user) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setMaxWidth(360);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label header = new Label("🔥 Study Streak");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        header.setTextFill(Color.web("#2c3e50"));

        // streak display
        HBox streakRow = new HBox(10);
        streakRow.setAlignment(Pos.CENTER);

        int streak = user.getCurrentStreak();

        Label streakNumber = new Label(String.valueOf(streak));
        streakNumber.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        streakNumber.setTextFill(streak > 0 ? Color.web("#e67e22") : Color.web("#bdc3c7"));

        VBox streakInfo = new VBox(2);
        streakInfo.setAlignment(Pos.CENTER_LEFT);

        Label dayLabel = new Label(streak == 1 ? "day" : "days");
        dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        dayLabel.setTextFill(Color.web("#7f8c8d"));

        String streakMessage;
        if (streak == 0) {
            streakMessage = "Start studying to build your streak!";
        } else if (streak < 3) {
            streakMessage = "Good start! Keep it going!";
        } else if (streak < 7) {
            streakMessage = "Nice streak! You're on a roll!";
        } else {
            streakMessage = "Amazing dedication!";
        }

        Label messageLabel = new Label(streakMessage);
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        messageLabel.setTextFill(Color.web("#95a5a6"));
        messageLabel.setWrapText(true);

        streakInfo.getChildren().addAll(dayLabel, messageLabel);
        streakRow.getChildren().addAll(streakNumber, streakInfo);

        // last study date
        String lastStudied = user.getLastStudyDate() != null
                ? "Last studied: " + user.getLastStudyDate().toString()
                : "No sessions yet";
        Label lastStudiedLabel = new Label(lastStudied);
        lastStudiedLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        lastStudiedLabel.setTextFill(Color.web("#bdc3c7"));

        card.getChildren().addAll(header, streakRow, lastStudiedLabel);
        return card;
    }

    /**
     * Creates the action history card by traversing the user's LinkedList of ActionRecords.
     * This demonstrates the LinkedList ADT — we iterate from index 0 to getLength()-1
     * to display all actions in chronological order.
     */
    private static VBox createActionHistoryCard(User user) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setMaxWidth(360);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label header = new Label("📋 Action History");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        header.setTextFill(Color.web("#2c3e50"));

        card.getChildren().add(header);

        // get the linked list from user
        ListInterface<ActionRecord> history = user.getActionHistory();

        if (history.isEmpty()) {
            Label emptyLabel = new Label("No actions recorded yet.");
            emptyLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
            emptyLabel.setTextFill(Color.web("#95a5a6"));
            emptyLabel.setPadding(new Insets(10));
            card.getChildren().add(emptyLabel);
        } else {
            Label countLabel = new Label(history.getLength() + " actions recorded");
            countLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            countLabel.setTextFill(Color.web("#7f8c8d"));
            card.getChildren().add(countLabel);

            // traverse the linked list — show most recent first
            for (int i = history.getLength() - 1; i >= 0; i--) {
                ActionRecord record = history.get(i);
                HBox actionRow = createActionRow(record);
                card.getChildren().add(actionRow);
            }
        }

        return card;
    }

    /**
     * Creates a single row in the action history display.
     */
    private static HBox createActionRow(ActionRecord record) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 10, 6, 10));
        row.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");

        // icon
        Label iconLabel = new Label(record.getIcon());
        iconLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        iconLabel.setMinWidth(25);

        // description and time
        VBox textBox = new VBox(2);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label descLabel = new Label(record.getDescription());
        descLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        descLabel.setTextFill(Color.web("#2c3e50"));

        Label timeLabel = new Label(record.getFormattedTime());
        timeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        timeLabel.setTextFill(Color.web("#95a5a6"));

        textBox.getChildren().addAll(descLabel, timeLabel);

        // action type badge
        Label badgeLabel = new Label(record.getActionType().name());
        badgeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        badgeLabel.setTextFill(Color.WHITE);
        badgeLabel.setPadding(new Insets(2, 6, 2, 6));

        String badgeColor;
        switch (record.getActionType()) {
            case JOIN:        badgeColor = "#3498db"; break;
            case START:       badgeColor = "#27ae60"; break;
            case BREAK:       badgeColor = "#f39c12"; break;
            case RESUME:      badgeColor = "#2ecc71"; break;
            case LEAVE:       badgeColor = "#e74c3c"; break;
            case SESSION_END: badgeColor = "#9b59b6"; break;
            default:          badgeColor = "#95a5a6"; break;
        }
        badgeLabel.setStyle("-fx-background-color: " + badgeColor + "; -fx-background-radius: 4;");

        row.getChildren().addAll(iconLabel, textBox, badgeLabel);
        return row;
    }
}
