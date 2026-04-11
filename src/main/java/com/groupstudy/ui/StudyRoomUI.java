package com.groupstudy.ui;

import com.groupstudy.LobbyUI;
import com.groupstudy.controller.LeaderboardController;
import com.groupstudy.model.Pokemon;
import com.groupstudy.model.RoomStatus;
import com.groupstudy.model.StudyRoom;
import com.groupstudy.model.User;
import com.groupstudy.service.LeaderboardService;
import com.groupstudy.service.NotificationService;
import com.groupstudy.service.PokemonService;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StudyRoomUI extends BorderPane{
	// main data
	private StudyRoom room; 
	private User currentUser; 
	private Pokemon myPokemon;
	private Stage stage;
	
	// getting services
	private LeaderboardService leaderboardService;
	private NotificationService notificationService;
	
	//getting timers
	private Timeline studyTimer;
	private Timeline roomTimer;
	
	// defining layout variables
	// top section components
	private Label roomTitleLabel;
	private Label timerLabel;
	private Button leaveButton;
	
	//center section for current user's Pokemon display
	private ImageView myPokemonImageView;
	private Label myPokemonNameLabel;
	private Label myPokemonStageLabel;
	private ProgressBar pokemonEvolutionProgress;
	private Label progressLabel;
	private Label completionLabel;
	
	//bottom section  - other users in the room details
	private HBox participantsContainer;
	private Label participantCountLabel;
	
	// UI control buttons
	private Button roomLeaderboardButton;
	private Button breakButton;
	
	// study tracking variables
	private int studyMinutes = 0;
	private boolean onBreak = false;
	private int previousLeaderboardPosition = -1;
	
	// using constructor to initialize the layout and services
	public StudyRoomUI(StudyRoom room, User currentUser) {
		this.room = room;
		this.currentUser = currentUser;
		
		// initialize the pokemon service and assign a random pokemon stage 1 to the user
		PokemonService pokemonService = new PokemonService();
		this.myPokemon = pokemonService.assignRandomPokemon();
		currentUser.setCurrentPokemon(this.myPokemon);
		
		// initialize other services to work
		this.leaderboardService = new LeaderboardService();
		this.notificationService = new NotificationService();
		
		// first setting up UI
		setupUI();
		// get and display the user's study timer
		showUserStudyTimer();
		// get and display the room's remaining time
		showRoomCountdownTimer();
	
	}
	
	// setting the UI layout
	private void setupUI() {
		// first set the background color and styling 
		setStyle("-fx-background-color: #f5f5f5;");
		setPadding(new Insets(10));
		
		// now build sections top, center and bottom
		setTop(createTopSection());
		setCenter(createCenterSection());
		setBottom(createBottomSection());
	}
	
	// top section part
	private VBox createTopSection() {
		VBox topBox = new VBox(5);
		topBox.setAlignment(Pos.CENTER);
		topBox.setPadding(new Insets(3,4,3,4));
		
		// header row container - leave room button, title of room and timer
		HBox headerRow = new HBox();
		headerRow.setAlignment(Pos.CENTER);
		
		// leave button - placed left
		leaveButton = new Button("⬅Leave");
		leaveButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        leaveButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                           "-fx-padding: 8 16; -fx-cursor: hand; -fx-background-radius: 5;");
        leaveButton.setOnAction(e -> handleLeave());
        
        // room name title - placed center
        roomTitleLabel = new Label(room.getTitle());
        roomTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        roomTitleLabel.setTextFill(Color.web("#2c3e50"));
        
        // room timer - placed right
        long remainingSeconds = room.getRemainingTime() / 1000;
        timerLabel = new Label("⏳ " + formatTime(remainingSeconds));
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        timerLabel.setTextFill(Color.web("#e67e22"));
        
        // adding spacer for layout
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        
        headerRow.getChildren().addAll(leaveButton, leftSpacer, roomTitleLabel, rightSpacer, timerLabel);
        
        // adding a separator line between top and center section
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #bdc3c7;");
        
        topBox.getChildren().addAll(headerRow, separator);
        return topBox; 
	}
	
	// centre section
	private VBox createCenterSection() {
		VBox centerBox = new VBox(2);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setPadding(new Insets(3,4,3,4));
		
		// main user's pokemon display card
		VBox myPokemonCard = createPokemonDisplay();
		centerBox.getChildren().add(myPokemonCard);
		return centerBox;
	} 
	
	private VBox createPokemonDisplay() {
		VBox card = new VBox(2);
		card.setAlignment(Pos.CENTER);
		card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
		card.setPadding(new Insets(2));
		card.setMaxWidth(500);
		
		// title of pokemon
		Label myPokemonLabel = new Label("My Pokemon");
		myPokemonLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		myPokemonLabel.setTextFill(Color.web("#7f8c8d"));
		
		// adding space for pokemon image
		myPokemonImageView = new ImageView();
		myPokemonImageView.setFitWidth(150);
		myPokemonImageView.setFitHeight(150);
		myPokemonImageView.setPreserveRatio(true);
		// placing actual image
		updatePokemonImage();
		
		// my pokemon name and stage
		myPokemonNameLabel = new Label(myPokemon.getCurrentName());
		myPokemonNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        myPokemonNameLabel.setTextFill(Color.web("#2c3e50"));
        
        // current stage of the pokemon
        myPokemonStageLabel = new Label("Stage " + myPokemon.getCurrentStage());
        myPokemonStageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        myPokemonStageLabel.setTextFill(Color.web("#95a5a6"));
        
        // evolution progress bar
        VBox progressSection = createProgressSection();
        
        card.getChildren().addAll(
        		myPokemonLabel,
        		myPokemonImageView,
        		myPokemonNameLabel,
        		myPokemonStageLabel,
        		progressSection
        		);
        return card;
	}
	
	private VBox createProgressSection() {
		VBox section = new VBox(5);
		section.setAlignment(Pos.CENTER);
		section.setMaxWidth(400);
		
		// checking if pokemon fully evolved
		if (myPokemon.getCurrentStage() == 3) {
			// show evolution complete message
			completionLabel = new Label("✅ Evolution Complete! " + studyMinutes + " min studied");
			completionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            completionLabel.setTextFill(Color.web("#27ae60"));
            section.getChildren().add(completionLabel);
		} else {
			// if evolution not complete, show progress bar
			long currentMinutes = myPokemon.getMinutesStudied();
			long nextStageMinutesLeft = myPokemon.getMinutesUntilNextEvolution();
			
			// time label
			progressLabel = new Label("Evolution Progress (" + currentMinutes + " / " + nextStageMinutesLeft + " min)");
            progressLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            progressLabel.setTextFill(Color.web("#7f8c8d"));
            
            // progress bar animation
            pokemonEvolutionProgress = new ProgressBar();
            pokemonEvolutionProgress.setPrefWidth(350);
            pokemonEvolutionProgress.setPrefHeight(25);
            pokemonEvolutionProgress.setProgress((double) currentMinutes / nextStageMinutesLeft);
            pokemonEvolutionProgress.setStyle("-fx-accent: #3498db;");
            
            // percentage label
            int percentage = (int) ((double) currentMinutes / nextStageMinutesLeft * 100);
            Label percentLabel = new Label(percentage + "%");
            percentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            percentLabel.setTextFill(Color.web("#3498db"));
			
            section.getChildren().addAll(progressLabel, pokemonEvolutionProgress, percentLabel );		
		}
		return section;
	}
	
	// bottom section to view other participants and room leaderboard
	private VBox createBottomSection() {
		VBox bottomBox = new VBox(5);
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.setPadding(new Insets(3,4,3,4));
		
		// separator
		Separator separator = new Separator();
		separator.setStyle("-fx-background-color: #bdc3c7;");
		
		// participants section
		VBox participantsSection = createParticipantsSection();
		
		// control buttons
		HBox controlButtons = createControlButtons();
		
		bottomBox.getChildren().addAll(separator, participantsSection, controlButtons);
		return bottomBox;
		
	}
	
	// participants populate method to displays all users
	private VBox createParticipantsSection() {
		VBox section = new VBox(5);
		section.setAlignment(Pos.CENTER);
		
		participantCountLabel = new Label("Other Participants (" + (room.getCurrentSize() - 1) + "):"); 
        participantCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        participantCountLabel.setTextFill(Color.web("#34495e"));
        
        participantsContainer = new HBox(15);
        participantsContainer.setAlignment(Pos.CENTER);
        participantsContainer.setPadding(new Insets(5));
        
        updateParticipantsDisplay();
       
        section.getChildren().addAll(participantCountLabel, participantsContainer);
        return section;
	}
	
	// control buttons creation 
	private HBox createControlButtons() {
		HBox buttonBox = new HBox(5);
		buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(5, 0, 5, 0));
        
        // room leaderboard button
        roomLeaderboardButton = new Button("🏆 Check Room Leaderboard");
        roomLeaderboardButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        roomLeaderboardButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; " +
                                  "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");
        roomLeaderboardButton.setOnAction(e -> showRoomLeaderboard());
        
        // take a break button
        breakButton = new Button("Take Break");
        breakButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        breakButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; " +
                           "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");
        breakButton.setOnAction(e -> handleBreak());
        
        buttonBox.getChildren().addAll(roomLeaderboardButton, breakButton);
        return buttonBox;
        
	}
	
	
	// private methods to process the data 
	// method to get current user's study time and update pokemon, other users, and leaderboard
	private void showUserStudyTimer() {
		studyTimer = new Timeline(
				new KeyFrame(Duration.seconds(1), e -> {
					if(!onBreak) {
						// just read study time from Room
						studyMinutes = (int) (room.getStudyTime(currentUser) / 60000);
						
						// update pokemon display
						updatePokemonDisplay();
						
						// update participants list
						updateParticipantsDisplay();
						
						// check leaderboard position
						checkLeaderboardPosition();
						
						// show any pending notifications
						// ============================================= uncomment this later
//						if (currentUser.hasPendingNotifications()) {
//							NotificationPopup.showNext(getStage(), currentUser);
//						}
					}
				})
		);
		studyTimer.setCycleCount(Timeline.INDEFINITE);
		studyTimer.play();
	}
	
	// method to show the remaining time left for the room session to end
	private void showRoomCountdownTimer() {
		roomTimer = new Timeline(
	            new KeyFrame(Duration.seconds(1), e -> {
	            		// read and display the countdown
	                long remainingSeconds = room.getRemainingTime() / 1000;
	                timerLabel.setText("⏳ " + formatTime(remainingSeconds));
	                
	                // Check if session ended
	                if (room.isSessionOver()) {
	                    handleSessionEnd();
	                }
	            })
	        );
	        roomTimer.setCycleCount(Timeline.INDEFINITE);
	        roomTimer.play();
	}
	
	// update the pokemon details at each instance of studied time
	private void updatePokemonDisplay() {
		// update the pokemon image
		updatePokemonImage();
		
		// update name and stage
		myPokemonNameLabel.setText(myPokemon.getCurrentName());
		myPokemonStageLabel.setText("Stage " + myPokemon.getCurrentStage());
		
		// update progress section
		VBox newProgressSection = createProgressSection();
		
		// now replace old progress section with new progress bar
		VBox pokemonCard = (VBox) ((VBox) getCenter()).getChildren().get(0);
        pokemonCard.getChildren().set(4, newProgressSection);
       
	}
	
	// method which updates the pokemon image
	private void updatePokemonImage() {
		String imagePath = myPokemon.getCurrentImagePath();
		try {
			Image image = new Image(getClass().getResourceAsStream(imagePath));
            myPokemonImageView.setImage(image);
		} catch (Exception e) {
			System.err.println("Could not load Pokemon image: " + imagePath);
		}
		
	}
	
	// refresh participants in the room
	private void updateParticipantsDisplay() {
		participantsContainer.getChildren().clear();
		
		// now update the count label
		participantCountLabel.setText("Other Participants (" + (room.getCurrentSize() - 1) + "):");
        
		// get the other users name currently in the room - exclude current user
		for (User user : room.getAllStatus().keySet()) {
            if (!user.equals(currentUser)) {
                ParticipantCardUI card = new ParticipantCardUI(user);
                participantsContainer.getChildren().add(card);
            }
        }	
	}
	
	// method to display notification if the user reaches the top position
	private void checkLeaderboardPosition() {
        int currentPosition = leaderboardService.getUserRankInRoom(room, currentUser.getName());
        
        // Check if reached top position
        if (currentPosition == 1 && previousLeaderboardPosition != 1) {
            notificationService.addRankTop(currentUser);
        }
        
        previousLeaderboardPosition = currentPosition;
    }
	
	// method to show room leaderboard popup
	private void showRoomLeaderboard() {
		Stage roomLeaderBoardPopup = new Stage();
		LeaderboardController.show(roomLeaderBoardPopup, currentUser.getName(), room);
	}
	
	// below are the event handlers
	// method to handle break event
	private void handleBreak() {
		if (!onBreak) {
			onBreak = true;
			breakButton.setText("Resume");
			breakButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                    "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");
			room.updateStatus(currentUser, RoomStatus.BREAK);

		} else {
			onBreak = false;
            breakButton.setText("Take Break");
            breakButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; " +
                               "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");
            room.updateStatus(currentUser, RoomStatus.STUDYING);
		}
	}
	
	// leave event logic
	private void handleLeave() {
		// stop UI timer for the current user when left
		if (studyTimer != null) studyTimer.stop();
		if (roomTimer != null) roomTimer.stop();
		
		// clear all notifications on leaving
		notificationService.clearOnRoomExit(currentUser);
		room.removeUser(currentUser);
		LobbyUI.show(getStage());
	}
	
	// when the room time ends logic
	private void handleSessionEnd() {
		// stop all timers
		if (studyTimer != null) studyTimer.stop();
		if (roomTimer != null) roomTimer.stop();
		
		// add notification to queue
		notificationService.addSessionEnd(currentUser, studyMinutes);
		// show this notification immediately
		// ============================================= uncomment below later
		// NotificationPopup.showNext(getStage(), currentUser);
		
		// now clear all notifications
		notificationService.clearOnRoomExit(currentUser);
		// go back to lobby
		LobbyUI.show(getStage());
	}
	
	// get the original stage to go back to lobby ui
	private Stage getStage() {
        return stage;
    }
	
	//format the time
	private String formatTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
	
		
	// show method which calls the entire UI build methods
	public static void show(Stage stage, StudyRoom room, User user) {
        StudyRoomUI studyRoomUI = new StudyRoomUI(room, user);
        studyRoomUI.stage = stage;
        Scene scene = new Scene(studyRoomUI, 800, 800);
        stage.setScene(scene);
        stage.setTitle("Study Room - " + room.getTitle());
    }
	

}
