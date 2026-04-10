package com.groupstudy.ui;

import com.groupstudy.model.Pokemon;
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
	
	public ParticipantCardUI(User user) {
		this.user = user;
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
		this.setMaxWidth(80);
		
		// creating view space for pokemon
		pokemonImageView = new ImageView();
		pokemonImageView.setFitWidth(50);
		pokemonImageView.setFitHeight(50);
		pokemonImageView.setPreserveRatio(true);
		
		// now inserting small pokemon image if user has one
		Pokemon pokemon = user.getCurrentPokemon();
		if (pokemon != null) {
			String imagePath = pokemon.getCurrentImagePath();
			try {
				Image image = new Image(getClass().getResourceAsStream(imagePath));
				pokemonImageView.setImage(image);
			} catch (Exception e) {
				System.err.println("Could not load Pokemon image for user: " + user.getName() + ". Image Path: " + imagePath);
			}
		}
		usernameLabel = new Label(user.getName());
		usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
	    usernameLabel.setTextFill(Color.web("#2c3e50"));
	    usernameLabel.setMaxWidth(70);
	    usernameLabel.setWrapText(false);
	    usernameLabel.setAlignment(Pos.CENTER);
	    
	    this.getChildren().addAll(pokemonImageView, usernameLabel);
	}
	
	
	
}
