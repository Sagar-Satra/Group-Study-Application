package com.groupstudy.ui;

import com.groupstudy.model.Trophy;
import com.groupstudy.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TrophyCollectionUI {
	
	public static void show(Stage stage, User user) {
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #f5f5f5;");
        root.setPadding(new Insets(20));
        
        // top section -header
        VBox topSection = createTopSection(stage, user);
        
        // center section - trphy grid
        ScrollPane centerSection = createCenterSection(user);
        
        root.setTop(topSection);
        root.setCenter(centerSection);
        
        Scene scene = new Scene(root, 600, 700);
        stage.setScene(scene);
        stage.setTitle("My Trophy Collection - " + user.getName());
	}
	
	// header section creation
	private static VBox createTopSection(Stage stage, User user) {
		VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(0, 0, 15, 0));
        
        // header row to hold Back button and title
        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER);
        
        // back button
        Button backButton = new Button("← Back");
        backButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        backButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                           "-fx-padding: 8 16; -fx-cursor: hand; -fx-background-radius: 5;");
        backButton.setOnAction(e -> LobbyUI.show(stage));
        
        // title 
        Label titleLabel = new Label("🏆 My Trophy Collection");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        // my trophy count
        Label countLabel = new Label(user.getTrophyCount() + " Trophies");
        countLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        countLabel.setTextFill(Color.web("#7f8c8d"));
        
        // spacer between components
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        
        headerRow.getChildren().addAll(backButton, leftSpacer, titleLabel, rightSpacer);
        topBox.getChildren().addAll(headerRow, countLabel);  
        return topBox;           
    } 
	
	// grid to display the trophies
	private static ScrollPane createCenterSection(User user) {
        FlowPane trophyGrid = new FlowPane();
        trophyGrid.setHgap(15);
        trophyGrid.setVgap(15);
        trophyGrid.setPadding(new Insets(20));
        trophyGrid.setAlignment(Pos.CENTER);
        
        // getting all trophies from the users bag
        Trophy[] trophies = user.getAllTrophies();
        
        if (trophies.length == 0) {
          // if empty collection, display below message
            VBox emptyState = new VBox(15);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(50));
            
            Label emptyIcon = new Label("📦");
            emptyIcon.setFont(Font.font("Arial", FontWeight.BOLD, 60));
            
            Label emptyMessage = new Label("No trophies yet!");
            emptyMessage.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            emptyMessage.setTextFill(Color.web("#95a5a6"));
            
            emptyState.getChildren().addAll(emptyIcon, emptyMessage);
            
            ScrollPane scrollPane = new ScrollPane(emptyState);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent;");
            return scrollPane;
        }
        
        // otherwise display all trophies including duplicates
        for (Trophy trophy : trophies) {
            VBox trophyCard = createTrophyCard(trophy);
            trophyGrid.getChildren().add(trophyCard);
        }
        
        ScrollPane scrollPane = new ScrollPane(trophyGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        return scrollPane;
    }
    
	// trophy card to be placed in the centersection grid
    private static VBox createTrophyCard(Trophy trophy) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        card.setPadding(new Insets(12));
        card.setMaxWidth(150);
        
        // getting the image of pokemon
        ImageView trophyImage = new ImageView();
        trophyImage.setFitWidth(100);
        trophyImage.setFitHeight(100);
        trophyImage.setPreserveRatio(true);
        
        try {
            Image image = new Image(TrophyCollectionUI.class.getResourceAsStream(trophy.getImagePath()));
            trophyImage.setImage(image);
        } catch (Exception e) {
            System.err.println("Could not load trophy image: " + trophy.getImagePath());
        }
        
        // Pokemon name
        Label nameLabel = new Label(trophy.getPokemonName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        nameLabel.setTextFill(Color.web("#2c3e50"));
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(100);
        
        // Stage badge
        Label stageLabel = new Label("Stage " + trophy.getEvolutionStage());
        stageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        stageLabel.setTextFill(Color.web("#95a5a6"));
        
        card.getChildren().addAll(trophyImage, nameLabel, stageLabel);
        return card;
    }
	
	
	
}
