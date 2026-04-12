package com.groupstudy.ui;

import com.groupstudy.model.Pokemon;
import com.groupstudy.model.RoomStatus;
import com.groupstudy.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ParticipantCardUI extends VBox {

	private User user;
	private ImageView pokemonImageView;
	private Label usernameLabel;
	private Label pokemonNameLabel;
	private Label statusLabel;
	private RoomStatus status;
	
	public ParticipantCardUI(User user, RoomStatus status) {
	    this.user = user;
	    this.status = status;
	    setupUI();
	}
	
	private void setupUI() {
		// styling for container
		this.setAlignment(Pos.CENTER);
		this.setSpacing(5);
		this.setPadding(new Insets(10));
		this.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);");
		this.setMaxWidth(120);
		
		
		usernameLabel = new Label(user.getName());
		usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
	    usernameLabel.setTextFill(Color.web("#2c3e50"));
	    usernameLabel.setMaxWidth(110);
	    usernameLabel.setWrapText(false);
	    usernameLabel.setAlignment(Pos.CENTER);
	    this.getChildren().add(usernameLabel);
	    
		// creating view space for pokemon
		pokemonImageView = new ImageView();
		pokemonImageView.setFitWidth(80);
		pokemonImageView.setFitHeight(80);
		pokemonImageView.setPreserveRatio(true);
		this.getChildren().add(pokemonImageView);
		
		// now inserting small pokemon image if user has one
		Pokemon pokemon = user.getCurrentPokemon();
		if (pokemon != null) {
			pokemonNameLabel = new Label(pokemon.getCurrentName());
			pokemonNameLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
		    pokemonNameLabel.setTextFill(Color.web("#7f8c8d"));
		    pokemonNameLabel.setMaxWidth(110);
		    pokemonNameLabel.setAlignment(Pos.CENTER);
		    this.getChildren().add(pokemonNameLabel);
		    
			String imagePath = pokemon.getCurrentImagePath();
			try {
				Image image = new Image(getClass().getResourceAsStream(imagePath));
				pokemonImageView.setImage(image);
			} catch (Exception e) {
				System.err.println("Could not load Pokemon image for user: " + user.getName() + ". Image Path: " + imagePath);
			}
		}
		
	    statusLabel = new Label(getStatusText(status));
	    statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
	    statusLabel.setTextFill(getStatusColor(status));

	    this.getChildren().add(statusLabel);
	}
	
	private String getStatusText(RoomStatus status) {
	    switch (status) {
	        case STUDYING: return "🟢 Studying";
	        case BREAK: return "🟡 Break";
	        case LEFT: return "🔴 Left";
	        case SESSION_ENDED: return "⚫ Ended";
	        default: return "";
	    }
	}

	private Color getStatusColor(RoomStatus status) {
	    switch (status) {
	        case STUDYING: return Color.GREEN;
	        case BREAK: return Color.ORANGE;
	        case LEFT: return Color.RED;
	        case SESSION_ENDED: return Color.GRAY;
	        default: return Color.BLACK;
	    }
	}
	
}
