package com.groupstudy.service;

import com.groupstudy.model.*;

public class MainTest {

    public static void main(String[] args) throws Exception {

        System.out.println("=========== SYSTEM TEST START ===========\n");

        // ===== 1. Setup Auth Service =====
        AuthService auth = new AuthService();

        // ===== 2. Create Rooms =====
        String hashed = auth.hashPassword("1234");
        RoomManager manager = new RoomManager();

        StudyRoom privateRoom = new StudyRoom("Final Prep", 5, 6000, true, hashed);
        StudyRoom publicRoom = new StudyRoom("Math Self Study", 3, 7000, false, null);

        manager.addRoom(privateRoom);
        manager.addRoom(publicRoom);

        System.out.println("Private Room: " + privateRoom.getTitle());
        System.out.println("Public Room: " + publicRoom.getTitle());

        // ===== 3. Password Verification =====
        System.out.println("\n=== Password Test ===");
        System.out.println(privateRoom.verifyPassword("1234", auth));
        System.out.println(privateRoom.verifyPassword("wrong", auth));

        // ===== 4. Create Users =====
        User u1 = new User("Alice");
        User u2 = new User("Bob");
        User u3 = new User("Ten");
        User u4 = new User("Jhon");

        // ===== Assign Pokemon =====
        u1.setCurrentPokemon(new Pokemon(
                "TestMon",
                new String[]{"Stage1", "Stage2", "Stage3"},
                new String[]{"img1", "img2", "img3"}
        ));

        u2.setCurrentPokemon(new Pokemon(
                "BobMon",
                new String[]{"B1", "B2", "B3"},
                new String[]{"img1", "img2", "img3"}
        ));

        System.out.println("\nPokemon Assigned:");
        System.out.println(u1.getName() + ": " + u1.getCurrentPokemon());
        System.out.println(u2.getName() + ": " + u2.getCurrentPokemon());

        // ===== 5. Global Status Flow =====
        System.out.println("\n=== Global Status Test ===");
        System.out.println("Initial: " + u3.getCurrentStatus());

        u3.login();
        System.out.println("After login: " + u3.getCurrentStatus());

        privateRoom.addUser(u3);
        System.out.println("After entering room: " + u3.getCurrentStatus());

        privateRoom.removeUser(u3);
        System.out.println("After leaving room: " + u3.getCurrentStatus());

        u3.logout();
        System.out.println("After logout: " + u3.getCurrentStatus());

        // ===== 6. Add Users =====
        privateRoom.addUser(u1);
        privateRoom.addUser(u2);
        privateRoom.addUser(u3);

        System.out.println("\n=== Contains Test ===");
        System.out.println(privateRoom.containsUser(u1));
        System.out.println(privateRoom.containsUser(u2));
        System.out.println(privateRoom.containsUser(u3));

        // ===== 7. Multiple Room Constraint =====
        System.out.println("\n=== Multiple Room Constraint ===");
        publicRoom.addUser(u1);
        publicRoom.addUser(u2);
        publicRoom.addUser(u4);

        // ===== 8. Start Timer =====
        System.out.println("\n=== Timer Start ===");
        StudyTimer timer = new StudyTimer();
        timer.start(privateRoom);

        // ===== Wait for Stage 2 =====
        Thread.sleep(35000);

        System.out.println("\n=== Stage 2 Evolution Check ===");
        checkEvolutionAndTrophy(privateRoom);

        // ===== Remove one user =====
        System.out.println("\n=== Remove Ten ===");
        privateRoom.removeUser(u3);

        // ===== Interaction Test =====
        System.out.println("\n=== Interaction Test (Alice) ===");
        u1.setLastInteractionTime(System.currentTimeMillis());

        Thread.sleep(30000);

        // ===== Wait for Stage 3 =====
        System.out.println("\n=== Stage 3 Evolution Check ===");
        checkEvolutionAndTrophy(privateRoom);

        // ===== Study Time =====
        System.out.println("\n=== Study Time ===");
        System.out.println("Alice: " + privateRoom.getStudyTime(u1));
        System.out.println("Bob: " + privateRoom.getStudyTime(u2));
        System.out.println("Ten: " + privateRoom.getStudyTime(u3));

        // ===== Public Room Test =====
        System.out.println("\n=== Public Room Test ===");
        privateRoom.removeUser(u1);

        publicRoom.addUser(u1);
        publicRoom.addUser(u2);

        System.out.println("Before:");
        for (User u : publicRoom.getAllStatus().keySet()) {
            System.out.println(u.getName());
        }

        publicRoom.removeUser(u1);

        System.out.println("After:");
        for (User u : publicRoom.getAllStatus().keySet()) {
            System.out.println(u.getName());
        }

        // ===== Final Summary =====
        System.out.println("\n=== Final Status ===");
        for (User u : privateRoom.getAllStatus().keySet()) {
            System.out.println(u.getName() + " | " + privateRoom.getStatus(u));
        }

        // ===== Trophy Check =====
        System.out.println("\n=== Trophy Check ===");
        for (Trophy t : u1.getAllTrophies()) {
            System.out.println(t);
        }

        System.out.println("\n=========== TEST END ===========");
    }

    // evolution + trophy
    private static void checkEvolutionAndTrophy(StudyRoom room) {
        for (User user : room.getAllStatus().keySet()) {
            Pokemon p = user.getCurrentPokemon();

            if (p != null) {
                System.out.println(user.getName() + ": " + p);

                if (p.getCurrentStage() >= 2) {
                	Trophy t = p.createTrophy();
                    if (t != null && !user.hasTrophy(t)) {
                        user.addTrophy(t);
                    }
                }
            }
        }
    }
}