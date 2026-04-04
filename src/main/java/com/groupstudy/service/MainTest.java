package com.groupstudy.service;

import com.groupstudy.model.*;

public class MainTest {

    public static void main(String[] args) throws Exception {

        // ===== 1. Setup Auth Service =====
        AuthService auth = new AuthService();

        // ===== 2. Create Rooms =====
        String rawPassword = "1234";
        String hashed = auth.hashPassword(rawPassword);
        RoomManager manager = new RoomManager();

        StudyRoom privateRoom = new StudyRoom("Final Prep", 5, true, hashed);
        StudyRoom publicRoom = new StudyRoom("Math Self Study", 3, false, null);

        System.out.println("Private Room ID: " + privateRoom.getRoomId());
        System.out.println("Public Room ID: " + publicRoom.getRoomId());
        
        manager.addRoom(privateRoom);
        manager.addRoom(publicRoom);
        
        StudyRoom fetchedPrivate = manager.getRoom(privateRoom.getRoomId());
        StudyRoom fetchedPublic = manager.getRoom(publicRoom.getRoomId());

        System.out.println("Fetched Private Room Title: " + fetchedPrivate.getTitle());
        System.out.println("Fetched Public Room Title: " + fetchedPublic.getTitle());

        // ===== 3. Password Verification =====
        System.out.println("\n=== Password Test ===");
        System.out.println("Correct password: " + privateRoom.verifyPassword("1234", auth));
        System.out.println("Wrong password: " + privateRoom.verifyPassword("wrong", auth));

        // ===== 4. Create Users =====
        User u1 = new User("Alice");
        User u2 = new User("Bob");
        User u3 = new User("Ten");
        User u4 = new User("Jhon");

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

        // ===== 6. Add Users to Private Room =====
        privateRoom.addUser(u1);
        privateRoom.addUser(u2);
        privateRoom.addUser(u3);

        System.out.println("\n=== Contains Test ===");
        System.out.println("Contains Alice: " + privateRoom.containsUser(u1));
        System.out.println("Contains Bob: " + privateRoom.containsUser(u2));
        System.out.println("Contains Ten: " + privateRoom.containsUser(u3));

        // ===== 7. Prevent Multiple Room Join =====
        System.out.println("\n=== Multiple Room Constraint Test ===");
        publicRoom.addUser(u1); // should be blocked
        publicRoom.addUser(u2); // should be blocked
        publicRoom.addUser(u4);

        // ===== 8. Start Timer =====
        System.out.println("\n=== Timer Test (Private Room) ===");
        StudyTimer timer = new StudyTimer();
        timer.start(privateRoom);
        timer.start(publicRoom);

        // Wait → users become STUDYING
        Thread.sleep(6000);

        // ===== 9. PRIVATE ROOM LEFT TEST =====
        System.out.println("\n=== Private Room LEFT Test ===");
        System.out.println(">>> Ten leaves private room");

        privateRoom.removeUser(u3); // should become LEFT

        for (User user : privateRoom.getAllStatus().keySet()) {
            System.out.println(user.getName() + " | " + privateRoom.getStatus(user));
        }

        // ===== 10. Interaction Test =====
        System.out.println("\n=== Interaction Test ===");
        System.out.println(">>> Alice interaction");

        u1.setLastInteractionTime(System.currentTimeMillis());

        Thread.sleep(4000);

        // ===== 11. Study Time Check =====
        System.out.println("\n=== Study Time Result ===");
        System.out.println("Alice: " + privateRoom.getStudyTime(u1) + " ms");
        System.out.println("Bob: " + privateRoom.getStudyTime(u2) + " ms");
        System.out.println("Ten (LEFT, should stop): " + privateRoom.getStudyTime(u3) + " ms");
        System.out.println("John: " + publicRoom.getStudyTime(u4) + " ms");

        // ===== 12. Remove Alice from Private Room (to allow joining public room) =====
        System.out.println("\n=== Prepare Public Room Test ===");
        System.out.println(">>> Removing Alice from private room");
        privateRoom.removeUser(u1);

        // ===== 13. PUBLIC ROOM TEST =====
        System.out.println("\n=== Public Room Remove Test ===");

        publicRoom.addUser(u1);
        publicRoom.addUser(u2);

        System.out.println("Before leaving:");
        for (User user : publicRoom.getAllStatus().keySet()) {
            System.out.println(user.getName());
        }

        // Alice leaves → should be removed
        System.out.println(">>> Alice leaves public room");
        publicRoom.removeUser(u1);

        System.out.println("After leaving:");
        for (User user : publicRoom.getAllStatus().keySet()) {
            System.out.println(user.getName());
        }

        // ===== 14. Final Summary =====
        System.out.println("\n=== Final Summary ===");
        System.out.println("Private Room Users:");
        for (User user : privateRoom.getAllStatus().keySet()) {
            System.out.println(user.getName() + " | " + privateRoom.getStatus(user));
        }

        System.out.println("\nPublic Room Users:");
        for (User user : publicRoom.getAllStatus().keySet()) {
            System.out.println(user.getName() + " | " + publicRoom.getStatus(user));
        }
        
        // ===== 15. Check Room Manager Function =====
        
    }
}
