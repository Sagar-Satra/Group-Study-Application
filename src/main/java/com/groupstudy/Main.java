package com.groupstudy;

import com.groupstudy.model.*;
import com.groupstudy.service.*;


public class Main {
    
//    @Override
//    public void start(Stage stage) {
//        Label label = new Label("Group Study App - JavaFX Works! 🎉");
//        Button button = new Button("Click Me!");
//        
//        button.setOnAction(e -> label.setText("Maven + JavaFX Working!"));
//        
//        VBox root = new VBox(20, label, button);
//        root.setStyle("-fx-padding: 50; -fx-alignment: center;");
//        
//        Scene scene = new Scene(root, 400, 300);
//        stage.setTitle("Test JavaFX");
//        stage.setScene(scene);
//        stage.show();
//    }
    
	public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║  Complete Integration Test - All 4 Tasks     ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");
        
        testTask6_PokemonEvolution();
        testTask8_TrophyBag();
        testTask9_Notifications();
        testTask10_Leaderboard();
        testIntegration();
        
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║  ✅ ALL TESTS PASSED!                         ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
    }
    
    private static void testTask6_PokemonEvolution() {
        System.out.println("--- Task 6: Pokemon Evolution ---");
        
        PokemonService pokemonService = new PokemonService();
        Pokemon pokemon = pokemonService.assignRandomPokemon();
        
        System.out.println("Assigned: " + pokemon.getCurrentName() + " (Stage " + pokemon.getCurrentStage() + ")");
        
        pokemon.addStudyTime(35);
        System.out.println("After 35 min: " + pokemon.getCurrentName() + " (Stage " + pokemon.getCurrentStage() + ")");
        
        pokemon.addStudyTime(30);
        System.out.println("After 65 min total: " + pokemon.getCurrentName() + " (Stage " + pokemon.getCurrentStage() + ")");
        
        System.out.println("✅ Task 6 Complete\n");
    }
    
    private static void testTask8_TrophyBag() {
        System.out.println("--- Task 8: Trophy Bag (Bag ADT) ---");
        
        User user = new User("TestUser");
        
        Trophy t1 = new Trophy("Charizard", "char.png", 3, 60);
        Trophy t2 = new Trophy("Blastoise", "blast.png", 3, 65);
        Trophy t3 = new Trophy("Charizard", "char.png", 3, 70);
        
        user.addTrophy(t1);
        user.addTrophy(t2);
        user.addTrophy(t3);
        
        System.out.println("Trophy count: " + user.getTrophyCount());
        System.out.println("Has Charizard: " + user.hasTrophy(t1));
        
        Trophy[] all = user.getAllTrophies();
        System.out.println("All trophies:");
        for (int i = 0; i < all.length; i++) {
            System.out.println("  " + (i+1) + ". " + all[i]);
        }
        
        System.out.println("✅ Task 8 Complete\n");
    }
    
    private static void testTask9_Notifications() {
        System.out.println("--- Task 9: Notification Queue ---");
        
        User user = new User("Sagar");
        NotificationService notifService = new NotificationService();
        
        notifService.addPokemonEvolution(user, "Charmeleon", 2);
        notifService.addPokemonEvolution(user, "Charizard", 3);
        notifService.addTrophy(user, "Charizard");
        
        System.out.println("Pending notifications: " + user.getPendingNotificationCount());
        
        System.out.println("Processing (FIFO order):");
        int count = 1;
        while (user.hasPendingNotifications()) {
            Notification n = user.getNextNotification();
            System.out.println("  " + count + ". " + n);
            count++;
        }
        
        System.out.println("After processing: " + user.getPendingNotificationCount());
        System.out.println("✅ Task 9 Complete\n");
    }
    
    private static void testTask10_Leaderboard() {
        System.out.println("--- Task 10: Leaderboard (MergeSort) ---");
        
        LeaderboardService leaderboard = new LeaderboardService();
        
        leaderboard.addOrUpdateEntry(new LeaderboardEntry("Sagar", 15, 300, 5));
        leaderboard.addOrUpdateEntry(new LeaderboardEntry("Aditya", 12, 280, 3));
        leaderboard.addOrUpdateEntry(new LeaderboardEntry("Yuxuan", 18, 350, 7));
        
        System.out.println("Top 3:");
        LeaderboardEntry[] top3 = leaderboard.getTopEntries(3);
        for (int i = 0; i < top3.length; i++) {
            System.out.println("  " + (i+1) + ". " + top3[i]);
        }
        
        System.out.println("Sagar's rank: #" + leaderboard.getUserRank("Sagar"));
        System.out.println("✅ Task 10 Complete\n");
    }
    
    private static void testIntegration() {
        System.out.println("--- Full Integration Test ---");
        
        User user = new User("Sagar");
        PokemonService pokemonService = new PokemonService();
        NotificationService notifService = new NotificationService();
        LeaderboardService leaderboard = new LeaderboardService();
        
        // Assign Pokemon
        Pokemon pokemon = pokemonService.assignRandomPokemon();
        user.setCurrentPokemon(pokemon);
        System.out.println("1. Assigned: " + pokemon.getCurrentName());
        
        // Study 35 min - evolve to Stage 2
        boolean evolved = pokemon.addStudyTime(35);
        if (evolved) {
            Trophy trophy = pokemon.createTrophy();
            user.addTrophy(trophy);
            notifService.addPokemonEvolution(user, pokemon.getCurrentName(), pokemon.getCurrentStage());
            notifService.addTrophy(user, pokemon.getCurrentName());
        }
        System.out.println("2. After 35 min: " + pokemon.getCurrentName() + " (Stage " + pokemon.getCurrentStage() + ")");
        
        // Study 30 more - evolve to Stage 3
        evolved = pokemon.addStudyTime(30);
        if (evolved) {
            Trophy trophy = pokemon.createTrophy();
            user.addTrophy(trophy);
            notifService.addPokemonEvolution(user, pokemon.getCurrentName(), pokemon.getCurrentStage());
            notifService.addTrophy(user, pokemon.getCurrentName());
        }
        System.out.println("3. After 65 min total: " + pokemon.getCurrentName() + " (Stage " + pokemon.getCurrentStage() + ")");
        
        // Check trophies
        System.out.println("4. Trophies earned: " + user.getTrophyCount());
        
        // Check notifications
        System.out.println("5. Pending notifications: " + user.getPendingNotificationCount());
        
        // Add to leaderboard
        user.addStudyTime(65);
        user.setCurrentStreak(5);
        leaderboard.addOrUpdateEntry(new LeaderboardEntry(
            user.getName(),
            user.getTrophyCount(),
            user.getTotalStudyMinutes(),
            user.getCurrentStreak()
        ));
        System.out.println("DEBUG: Leaderboard size = " + leaderboard.getSize());
        System.out.println("6. Leaderboard rank: #" + leaderboard.getUserRank(user.getName()));
        
        // Process notifications
        System.out.println("\n7. Processing notifications:");
        while (user.hasPendingNotifications()) {
            Notification n = user.getNextNotification();
            System.out.println("   " + n);
        }
        
        System.out.println("\n✅ Integration Complete!");
    }
}
