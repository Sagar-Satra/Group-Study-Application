package com.groupstudy.ui;

import com.groupstudy.model.StudyRoom;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class RoomCardUI extends VBox {

    private Label timeLabel;
    private StudyRoom room;

    public RoomCardUI(StudyRoom room) {

        this.room = room;

        // title row with room type badge
        HBox titleRow = new HBox(8);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(room.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        // private/public badge
        Label typeBadge = new Label(room.isPrivate() ? "🔒 Private" : "🌐 Public");
        typeBadge.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        typeBadge.setTextFill(Color.WHITE);
        typeBadge.setStyle("-fx-background-color: " + (room.isPrivate() ? "#e67e22" : "#27ae60") + "; " +
                "-fx-background-radius: 4; -fx-padding: 2 6;");

        titleRow.getChildren().addAll(titleLabel, typeBadge);

        // admin label
        String adminName = room.getAdmin() != null ? room.getAdmin().getName() : "—";
        Label adminLabel = new Label("🏠️ " + adminName);
        adminLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        adminLabel.setTextFill(Color.web("#7f8c8d"));

        // capacity
        Label capacityLabel = new Label(
                "👥 " + room.getActiveUserCount() + " / " + room.getCapacity()
        );
        capacityLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        capacityLabel.setTextFill(Color.web("#34495e"));

        // timer
        timeLabel = new Label(); 
        timeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        timeLabel.setTextFill(Color.web("#e67e22"));

        updateTime(); 

        this.getChildren().addAll(titleRow, adminLabel, capacityLabel, timeLabel);

        // if the room is full, add a Full label and prevent user to join
        if (room.getCurrentSize() >= room.getCapacity()) {
        		Label fullLabel = new Label("⚠️ ROOM FULL");
        		fullLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            fullLabel.setTextFill(Color.web("#e74c3c"));
            this.getChildren().add(fullLabel);
        }
        
        this.setSpacing(5);

        startTimer();
        
        this.setStyle(
        	    "-fx-border-color: #bdc3c7;" +
        	    "-fx-border-radius: 10;" +
        	    "-fx-padding: 15;" +
        	    "-fx-background-color: white;" +
        	    "-fx-background-radius: 10;" +
        	    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);" +
        	    "-fx-cursor: hand;"
        	);
        
        this.setMaxWidth(350);
    }

    private void updateTime() {
    	if(room.isClosed()) {
    		timeLabel.setText("⏳ Session Ended");
    		return;
    	}else {
    		long remaining = room.getRemainingTime() / 1000;
    		long minutes = remaining / 60;
    		long seconds = remaining % 60;
    		timeLabel.setText(String.format("⏳ %02d:%02d remaining", minutes, seconds));
    	}
    }

    private void startTimer() {

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    updateTime();
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
}