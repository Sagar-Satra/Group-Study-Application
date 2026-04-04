package com.groupstudy;

import com.groupstudy.model.Pokemon;
import com.groupstudy.model.Trophy;
import com.groupstudy.model.User;
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
    
    public static void main(String[] args) {
        System.out.println("=== Pokemon & Trophy Test ===\n");
        
        // Create user
        User user = new User("Sagar");
        System.out.println("User created: " + user.getName());
        
        // Assign Pokemon
        PokemonService service = new PokemonService();
        Pokemon pokemon = service.assignRandomPokemon();
        user.setCurrentPokemon(pokemon);
        
        System.out.println("Assigned: " + pokemon.getCurrentName() + " (Stage 1)");
        System.out.println("Image: " + pokemon.getCurrentImagePath());
        
        // Study 35 minutes (evolve to Stage 2)
        System.out.println("\n--- Studying 35 minutes ---");
        Trophy trophy1 = service.updatePokemonProgress(pokemon, 35);
        
        if (trophy1 != null) {
            user.addTrophy(trophy1);
            System.out.println("Trophy earned! " + trophy1);
        }
        
        System.out.println("Pokemon: " + pokemon.getCurrentName() + " (Stage " + pokemon.getCurrentStage() + ")");
        
        // Study 30 more minutes (evolve to Stage 3)
        System.out.println("\n--- Studying 30 more minutes ---");
        Trophy trophy2 = service.updatePokemonProgress(pokemon, 30);
        
        if (trophy2 != null) {
            user.addTrophy(trophy2);
            System.out.println("Trophy earned! " + trophy2);
        }
        
        System.out.println("Pokemon: " + pokemon.getCurrentName() + " (Stage " + pokemon.getCurrentStage() + ")");
        
        // Display trophies
        System.out.println("\n=== Trophy Collection ===");
        System.out.println("Total trophies: " + user.getTrophyCount());
        Trophy[] trophies = user.getAllTrophies();
        for (int i = 0; i < trophies.length; i++) {
            System.out.println((i+1) + ". " + trophies[i]);
        }
        
        System.out.println("Test Complete!");
    }
}
