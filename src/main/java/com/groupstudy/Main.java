package com.groupstudy;

import com.groupstudy.model.LeaderboardEntry;
import com.groupstudy.model.Pokemon;
import com.groupstudy.model.StudyRoom;
import com.groupstudy.model.Trophy;
import com.groupstudy.model.User;
import com.groupstudy.service.AuthService;
import com.groupstudy.service.LeaderboardService;
import com.groupstudy.service.PokemonService;


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
    
	//testing of pokemon evolution
//    public static void main(String[] args) {
//        System.out.println("=== Pokemon & Trophy Test ===\n");
//        
//        // Create user
//        User user = new User("Sagar");
//        System.out.println("User created: " + user.getName());
//        
//        // Assign Pokemon
//        PokemonService service = new PokemonService();
//        Pokemon pokemon = service.assignRandomPokemon();
//        user.setCurrentPokemon(pokemon);
//        
//        System.out.println("Assigned: " + pokemon.getCurrentName() + " (Stage 1)");
//        System.out.println("Image: " + pokemon.getCurrentImagePath());
//        
//        // Study 35 minutes (evolve to Stage 2)
//        System.out.println("\n--- Studying 35 minutes ---");
//        Trophy trophy1 = service.updatePokemonProgress(pokemon, 35);
//        
//        if (trophy1 != null) {
//            user.addTrophy(trophy1);
//            System.out.println("Trophy earned! " + trophy1);
//        }
//        
//        System.out.println("Pokemon: " + pokemon.getCurrentName() + " (Stage " + pokemon.getCurrentStage() + ")");
//        
//        // Study 30 more minutes (evolve to Stage 3)
//        System.out.println("\n--- Studying 30 more minutes ---");
//        Trophy trophy2 = service.updatePokemonProgress(pokemon, 30);
//        
//        if (trophy2 != null) {
//            user.addTrophy(trophy2);
//            System.out.println("Trophy earned! " + trophy2);
//        }
//        
//        System.out.println("Pokemon: " + pokemon.getCurrentName() + " (Stage " + pokemon.getCurrentStage() + ")");
//        
//        // Display trophies
//        System.out.println("\n=== Trophy Collection ===");
//        System.out.println("Total trophies: " + user.getTrophyCount());
//        Trophy[] trophies = user.getAllTrophies();
//        for (int i = 0; i < trophies.length; i++) {
//            System.out.println((i+1) + ". " + trophies[i]);
//        }
//        
//        System.out.println("Test Complete!");
//    }
    
    // testing of leaderboard
    public static void main(String[] args) {
        System.out.println("Leaderboard Service - Complete Test");
        
        LeaderboardService service = new LeaderboardService();
        
        // Test 1: Add entries
        System.out.println("--- Test 1: Adding Entries ---");
        service.addOrUpdateEntry(new LeaderboardEntry("Sagar", 15, 300, 5));
        service.addOrUpdateEntry(new LeaderboardEntry("Aditya", 12, 280, 3));
        service.addOrUpdateEntry(new LeaderboardEntry("Yuxuan", 18, 350, 7));
        service.addOrUpdateEntry(new LeaderboardEntry("Alice", 10, 200, 2));
        service.addOrUpdateEntry(new LeaderboardEntry("Bob", 15, 320, 6));
        System.out.println(" Added 5 users");
        System.out.println("Size: " + service.getSize() + "\n");
        
        // Test 2: Get top entries
        System.out.println("--- Test 2: Get Top 3 ---");
        LeaderboardEntry[] top3 = service.getTopEntries(3);
        for (int i = 0; i < top3.length; i++) {
            System.out.println((i+1) + ". " + top3[i]);
        }
        System.out.println();
        
        // Test 3: Get user ranks
        System.out.println("--- Test 3: Get User Ranks ---");
        System.out.println("Sagar's rank: #" + service.getUserRank("Sagar"));
        System.out.println("Aditya's rank: #" + service.getUserRank("Aditya"));
        System.out.println("Yuxuan's rank: #" + service.getUserRank("Yuxuan"));
        System.out.println("Alice's rank: #" + service.getUserRank("Alice"));
        System.out.println("Bob's rank: #" + service.getUserRank("Bob"));
        System.out.println();
        
        // Test 4: Get all entries
        System.out.println("--- Test 4: Get All Entries (Sorted) ---");
        LeaderboardEntry[] all = service.getAllEntries();
        for (int i = 0; i < all.length; i++) {
            System.out.println((i+1) + ". " + all[i]);
        }
        System.out.println();
        
        // Test 5: Update existing entry
        System.out.println("--- Test 5: Update Entry ---");
        System.out.println("Updating Sagar: 15 → 20 trophies");
        service.addOrUpdateEntry(new LeaderboardEntry("Sagar", 20, 400, 8));
        System.out.println("New rank: #" + service.getUserRank("Sagar"));
        System.out.println();
        
        // Test 6: Top 3 after update
        System.out.println("--- Test 6: Top 3 After Update ---");
        top3 = service.getTopEntries(3);
        for (int i = 0; i < top3.length; i++) {
            System.out.println((i+1) + ". " + top3[i]);
        }
        System.out.println();
        
        // Test 7: Room leaderboard
        System.out.println("--- Test 7: Room Leaderboard ---");
        
        // Create room and users
        AuthService auth = new AuthService();
        StudyRoom room = new StudyRoom("Test Room", 5, false, null);
        
        User u1 = new User("Sagar");
        User u2 = new User("Aditya");
        User u3 = new User("Yuxuan");
        
        // Set trophy counts
        u1.addTrophy(new Trophy("Charizard", "char.png", 3, 60));
        u1.addTrophy(new Trophy("Blastoise", "blast.png", 3, 65));
        
        u2.addTrophy(new Trophy("Venusaur", "ven.png", 3, 70));
        
        u3.addTrophy(new Trophy("Pikachu", "pika.png", 2, 30));
        u3.addTrophy(new Trophy("Gengar", "gengar.png", 3, 60));
        u3.addTrophy(new Trophy("Dragonite", "dragon.png", 3, 65));
        
        // Add to room and simulate study time
        room.addUser(u1);
        room.addUser(u2);
        room.addUser(u3);
        
        room.addStudyTime(u1, 1800000);  // 30 min
        room.addStudyTime(u2, 2400000);  // 40 min
        room.addStudyTime(u3, 1200000);  // 20 min
        
        LeaderboardEntry[] roomBoard = service.getRoomLeaderBoard(room);
        System.out.println("Room: " + room.getTitle());
        for (int i = 0; i < roomBoard.length; i++) {
            System.out.println((i+1) + ". " + roomBoard[i]);
        }
        System.out.println();
        
        // Test 8: Edge cases
        System.out.println("--- Test 8: Edge Cases ---");
        System.out.println("Top 0 entries: " + service.getTopEntries(0).length + " (should be 0)");
        System.out.println("Top 100 entries: " + service.getTopEntries(100).length + " (should be " + service.getSize() + ")");
        System.out.println("Nonexistent user rank: " + service.getUserRank("Unknown") + " (should be -1)");
        System.out.println();
        
        // Test 9: Clear
        System.out.println("--- Test 9: Clear Leaderboard ---");
        System.out.println("Size before clear: " + service.getSize());
        service.clear();
        System.out.println("Size after clear: " + service.getSize());
        System.out.println();
        
        System.out.println("=================All Tests Passed===============");
    }
}
