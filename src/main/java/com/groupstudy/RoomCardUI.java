package com.groupstudy;

import com.groupstudy.model.StudyRoom;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class RoomCardUI extends VBox {

    private Label timeLabel;
    private StudyRoom room;

    public RoomCardUI(StudyRoom room) {

        this.room = room;

        Label titleLabel = new Label(room.getTitle());

        Label capacityLabel = new Label(
                room.getCurrentSize() + " / " + room.getCapacity()
        );

        timeLabel = new Label(); 

        updateTime(); 

        this.getChildren().addAll(titleLabel, capacityLabel, timeLabel);

        this.setSpacing(5);

        startTimer();
        
        this.setStyle(
        	    "-fx-border-color: black;" +
        	    "-fx-border-radius: 10;" +
        	    "-fx-padding: 15;" +
        	    "-fx-background-color: white;"
        	);
        
        this.setMaxWidth(250);
    }

    private void updateTime() {
    	if(room.isClosed()) {
    		timeLabel.setText("Session Ended");
    	}else {
    		long remaining = room.getRemainingTime() / 1000;
    		timeLabel.setText("Remaining: " + remaining + "s");
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