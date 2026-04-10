package com.groupstudy.ui;

import com.groupstudy.model.Notification;
import com.groupstudy.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NotificationPopup {
	
	// method to process and show next notification from user's notification queue
	public static void showNext(Stage stage, User user) {
		if (!user.hasPendingNotifications()) {
			return;  // no notifications to show
		}
		
		Notification notification = user.getNextNotification();
		
		if (notification == null) return;
		
		// show the notification
		showNotificationPopup(stage, "New Notification", notification.getMessage());
	}
	
	// generic method to show every type of notification
	private static void showNotificationPopup(Stage stage, String title, String message) {
		Stage popup = createPopup(stage, title);
		
		VBox content = new VBox(20);
		content.setAlignment(Pos.CENTER);
		content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: white;");
        
        // message label
        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        messageLabel.setTextFill(Color.web("#2c3e50"));
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(280);
        
        // dismiss button
        Button dismissBtn = createDismissButton(popup);
        content.getChildren().addAll(messageLabel, dismissBtn);
        
        Scene scene = new Scene(content, 320, 200);
        popup.setScene(scene);
        popup.show();
	}
	
	// helper methods to create the popup stage
	private static Stage createPopup(Stage stage, String title) {
		Stage popup = new Stage();
		popup.initModality(Modality.NONE);   
        popup.initOwner(stage);    
        popup.setTitle(title);    
        popup.setResizable(false);   
        return popup;     
		
	}
	
	// helper method to create the dismiss button
	private static Button createDismissButton(Stage popup) {
		Button btn = new Button("Gotcha!!");
		btn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                    "-fx-padding: 10 30; -fx-cursor: hand; -fx-background-radius: 5;");
        btn.setOnAction(e -> popup.close());
        return btn;
	}
	
}
