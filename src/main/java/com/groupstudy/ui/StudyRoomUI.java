package com.groupstudy.ui;

import com.groupstudy.Main;
import com.groupstudy.controller.LeaderboardController;
import com.groupstudy.model.ActionRecord;
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

public class StudyRoomUI extends BorderPane {
	// Main data
	private StudyRoom room; 
	private User currentUser; 
	private Pokemon myPokemon;
	private Stage stage;
	
	// Services
	private NotificationService notificationService;
	
	// UI update timers
	private Timeline uiRefreshTimer;
	private Timeline roomTimer;
	
	// Layout components
	private Label roomTitleLabel;
	private Label timerLabel;
	private Button leaveButton;
	
	private ImageView myPokemonImageView;
	private Label myPokemonNameLabel;
	private Label myPokemonStageLabel;
	private ProgressBar pokemonEvolutionProgress;
	private Label progressLabel;
	private Label completionLabel;
	
	private HBox participantsContainer;
	private Label participantCountLabel;
	
	private Button roomLeaderboardButton;
	private Button breakButton;
	
	// Study tracking
	private boolean onBreak = false;
	private int previousLeaderboardPosition = -1;
	private boolean hasRecordedStart = false;
	
	public StudyRoomUI(StudyRoom room, User currentUser) {
		this.room = room;
		this.currentUser = currentUser;
		this.notificationService = new NotificationService();
		
		// ===== ALWAYS assign NEW Pokemon when joining room =====
		PokemonService pokemonService = new PokemonService();
		this.myPokemon = pokemonService.assignRandomPokemon();
		currentUser.setCurrentPokemon(this.myPokemon);
		currentUser.incrementPokemonCount();
		System.out.println("✅ Assigned new Pokemon: " + myPokemon.getCurrentName() + " to " + currentUser.getName());
		
		setupUI();
		startUIRefreshTimer();
		showRoomCountdownTimer();
	}
	
	private void setupUI() {
		setStyle("-fx-background-color: #f5f5f5;");
		setPadding(new Insets(10));
		
		setTop(createTopSection());
		setCenter(createCenterSection());
		setBottom(createBottomSection());
	}
	
	private VBox createTopSection() {
		VBox topBox = new VBox(5);
		topBox.setAlignment(Pos.CENTER);
		topBox.setPadding(new Insets(3, 4, 3, 4));
		
		HBox headerRow = new HBox();
		headerRow.setAlignment(Pos.CENTER);
		
		leaveButton = new Button("⬅Leave");
		leaveButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
		leaveButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
		                   "-fx-padding: 8 16; -fx-cursor: hand; -fx-background-radius: 5;");
		leaveButton.setOnAction(e -> handleLeave());
		
		roomTitleLabel = new Label(room.getTitle());
		roomTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		roomTitleLabel.setTextFill(Color.web("#2c3e50"));
		
		long remainingSeconds = room.getRemainingTime() / 1000;
		timerLabel = new Label("⏳ " + formatTime(remainingSeconds));
		timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		timerLabel.setTextFill(Color.web("#e67e22"));
		
		Region leftSpacer = new Region();
		Region rightSpacer = new Region();
		HBox.setHgrow(leftSpacer, Priority.ALWAYS);
		HBox.setHgrow(rightSpacer, Priority.ALWAYS);
		
		headerRow.getChildren().addAll(leaveButton, leftSpacer, roomTitleLabel, rightSpacer, timerLabel);
		
		Separator separator = new Separator();
		separator.setStyle("-fx-background-color: #bdc3c7;");
		
		topBox.getChildren().addAll(headerRow, separator);
		return topBox; 
	}
	
	private VBox createCenterSection() {
		VBox centerBox = new VBox(2);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setPadding(new Insets(3, 4, 3, 4));
		
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
		
		Label myPokemonLabel = new Label("My Pokemon");
		myPokemonLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		myPokemonLabel.setTextFill(Color.web("#7f8c8d"));
		
		myPokemonImageView = new ImageView();
		myPokemonImageView.setFitWidth(150);
		myPokemonImageView.setFitHeight(150);
		myPokemonImageView.setPreserveRatio(true);
		updatePokemonImage();
		
		myPokemonNameLabel = new Label(myPokemon.getCurrentName());
		myPokemonNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		myPokemonNameLabel.setTextFill(Color.web("#2c3e50"));
		
		myPokemonStageLabel = new Label("Stage " + myPokemon.getCurrentStage());
		myPokemonStageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
		myPokemonStageLabel.setTextFill(Color.web("#95a5a6"));
		
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
		
		if (myPokemon.getCurrentStage() == 3) {
			long totalSeconds = myPokemon.getSecondsStudied();
			completionLabel = new Label("Evolution Complete! " + totalSeconds + " sec studied");
			completionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
			completionLabel.setTextFill(Color.web("#27ae60"));
			section.getChildren().add(completionLabel);
		} else {
			progressLabel = new Label(String.format("Evolution Progress (%d / %d sec)",
				myPokemon.getSecondsStudied(),
				myPokemon.getNextEvolutionThreshold()));
			progressLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
			progressLabel.setTextFill(Color.web("#7f8c8d"));
			
			pokemonEvolutionProgress = new ProgressBar();
			pokemonEvolutionProgress.setMaxWidth(380);
			pokemonEvolutionProgress.setMinHeight(18);
			
			double progress = myPokemon.getEvolutionProgress() / 100.0;
			pokemonEvolutionProgress.setProgress(progress);
			
			pokemonEvolutionProgress.setStyle(
				"-fx-accent: #27ae60;" +
				"-fx-control-inner-background: #ecf0f1;"
			);
			
			Label percentLabel = new Label(String.format("%.0f%%", myPokemon.getEvolutionProgress()));
			percentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
			percentLabel.setTextFill(Color.web("#3498db"));
			
			section.getChildren().addAll(progressLabel, pokemonEvolutionProgress, percentLabel);
		}
		
		return section;
	}
	
	private VBox createBottomSection() {
		VBox bottomBox = new VBox(10);
		bottomBox.setAlignment(Pos.TOP_CENTER);
		bottomBox.setPadding(new Insets(3, 4, 3, 4));
		
		VBox participantsSection = createParticipantsSection();
		HBox buttonBox = createButtonSection();
		
		bottomBox.getChildren().addAll(participantsSection, buttonBox);
		return bottomBox;
	}
	
	private VBox createParticipantsSection() {
		VBox section = new VBox(15);
		section.setAlignment(Pos.CENTER);
		section.setPadding(new Insets(20));
		section.setMinHeight(200);
		section.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
		        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 1);");
		
		participantCountLabel = new Label("Other Participants (0):");
		participantCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		participantCountLabel.setTextFill(Color.web("#2c3e50"));
		
		participantsContainer = new HBox(15);
		participantsContainer.setAlignment(Pos.CENTER);
		
		section.getChildren().addAll(participantCountLabel, participantsContainer);
		return section;
	}
	
	private HBox createButtonSection() {
		HBox buttonBox = new HBox(15);
		buttonBox.setAlignment(Pos.CENTER);
		
		roomLeaderboardButton = new Button("🏆 Check Room Leaderboard");
		roomLeaderboardButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		roomLeaderboardButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; " +
		                   "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");
		roomLeaderboardButton.setOnAction(e -> showRoomLeaderboard());
		
		breakButton = new Button("Take Break");
		breakButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
		breakButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; " +
		                   "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");
		breakButton.setOnAction(e -> handleBreak());
		
		buttonBox.getChildren().addAll(roomLeaderboardButton, breakButton);
		return buttonBox;
	}
	
	private void startUIRefreshTimer() {
		uiRefreshTimer = new Timeline(
			new KeyFrame(Duration.seconds(1), e -> {
				// only check for start recording and if the user is not on break
				if (!hasRecordedStart && !onBreak) {
					currentUser.recordAction(new ActionRecord(ActionRecord.ActionType.START, room.getTitle()));
					hasRecordedStart = true;
				}
				// refresh rest of the UI, leaving the current user's stats
				updatePokemonDisplay();
				updateParticipantsDisplay();
				checkLeaderboardPosition();
				
				if (currentUser.hasPendingNotifications()) {
					NotificationPopup.showNext(getStage(), currentUser);
				}
				
			})
		);
		uiRefreshTimer.setCycleCount(Timeline.INDEFINITE);
		uiRefreshTimer.play();
	}
	
	private void showRoomCountdownTimer() {
		roomTimer = new Timeline(
			new KeyFrame(Duration.seconds(1), e -> {
				long remainingSeconds = room.getRemainingTime() / 1000;
				timerLabel.setText("⏳ " + formatTime(remainingSeconds));
				
				if (room.isSessionOver()) {
					handleSessionEnd();
				}
			})
		);
		roomTimer.setCycleCount(Timeline.INDEFINITE);
		roomTimer.play();
	}
	
	private void updatePokemonDisplay() {
		updatePokemonImage();
		
		myPokemonNameLabel.setText(myPokemon.getCurrentName());
		myPokemonStageLabel.setText("Stage " + myPokemon.getCurrentStage());
		
		VBox newProgressSection = createProgressSection();
		
		VBox pokemonCard = (VBox) ((VBox) getCenter()).getChildren().get(0);
		pokemonCard.getChildren().set(4, newProgressSection);
	}
	
	private void updatePokemonImage() {
		String imagePath = myPokemon.getCurrentImagePath();
		try {
			Image image = new Image(getClass().getResourceAsStream(imagePath));
			myPokemonImageView.setImage(image);
		} catch (Exception e) {
			System.err.println("Could not load Pokemon image: " + imagePath);
		}
	}
	
	private void updateParticipantsDisplay() {
		participantsContainer.getChildren().clear();
		
		participantCountLabel.setText("Other Participants (" + (room.getCurrentSize() - 1) + "):");
		
		for (User user : room.getAllStatus().keySet()) {
			if (!user.equals(currentUser)) {
				ParticipantCardUI card = new ParticipantCardUI(user, room.getStatus(user));
				participantsContainer.getChildren().add(card);
			}
		}	
	}
	
	private void checkLeaderboardPosition() {
		LeaderboardService leaderboardService = Main.getLeaderboardService();
		int currentPosition = leaderboardService.getUserRankInRoom(room, currentUser.getName());
		
		if (currentPosition == 1 && previousLeaderboardPosition != 1) {
			notificationService.addRankTop(currentUser);
		}
		
		previousLeaderboardPosition = currentPosition;
	}
	
	private void showRoomLeaderboard() {
		Stage roomLeaderBoardPopup = new Stage();
		LeaderboardService leaderboardService = Main.getLeaderboardService();
		LeaderboardController.show(roomLeaderBoardPopup, currentUser.getName(), room, leaderboardService);
	}
	
	private void handleBreak() {
		if (!onBreak) {
			onBreak = true;
			breakButton.setText("Resume");
			breakButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
			        "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");
			currentUser.setManualBreakTrack(true);
			room.updateStatus(currentUser, RoomStatus.BREAK);
			
			currentUser.recordAction(new ActionRecord(ActionRecord.ActionType.BREAK, room.getTitle()));

		} else {
			onBreak = false;
			breakButton.setText("Take Break");
			breakButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; " +
			                   "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");
			currentUser.setManualBreakTrack(false);
			room.updateStatus(currentUser, RoomStatus.STUDYING);
			
			currentUser.recordAction(new ActionRecord(ActionRecord.ActionType.RESUME, room.getTitle()));
		}
	}
	
	private void handleLeave() {
		if (uiRefreshTimer != null) uiRefreshTimer.stop();
		if (roomTimer != null) roomTimer.stop();
		
		currentUser.recordAction(new ActionRecord(ActionRecord.ActionType.LEAVE, room.getTitle()));
		currentUser.updateStreak();
		
		// ===== Set Pokemon to NULL when leaving =====
		currentUser.setCurrentPokemon(null);
		currentUser.setManualBreakTrack(false);
		System.out.println("Set Pokemon to NULL for " + currentUser.getName() + " (Trophies kept: " + currentUser.getTrophyCount() + ")");
		
		notificationService.clearOnRoomExit(currentUser);
		room.removeUser(currentUser);
		LobbyUI.show(getStage());
	}
	
	private void handleSessionEnd() {
		if (uiRefreshTimer != null) uiRefreshTimer.stop();
		if (roomTimer != null) roomTimer.stop();
		
		currentUser.recordAction(new ActionRecord(ActionRecord.ActionType.SESSION_END, room.getTitle()));
		currentUser.updateStreak();
		
		long finalStudySeconds = room.getStudyTime(currentUser) / 1000;
		
		notificationService.addSessionEnd(currentUser, (int) finalStudySeconds);
		NotificationPopup.showNext(getStage(), currentUser);
		
		// ===== Set Pokemon to NULL when session ends =====
		currentUser.setCurrentPokemon(null);
		currentUser.setManualBreakTrack(false);
		System.out.println("Session ended. Set Pokemon to NULL for " + currentUser.getName() + " (Trophies kept: " + currentUser.getTrophyCount() + ")");
		
		notificationService.clearOnRoomExit(currentUser);
		LobbyUI.show(getStage());
	}
	
	private Stage getStage() {
		return stage;
	}
	
	private String formatTime(long totalSeconds) {
		long hours = totalSeconds / 3600;
		long minutes = (totalSeconds % 3600) / 60;
		long seconds = totalSeconds % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	public static void show(Stage stage, StudyRoom room, User user) {
		StudyRoomUI studyRoomUI = new StudyRoomUI(room, user);
		studyRoomUI.stage = stage;
		Scene scene = new Scene(studyRoomUI, 600, 700);
		stage.setScene(scene);
		stage.setTitle("Study Room - " + room.getTitle());
	}
}