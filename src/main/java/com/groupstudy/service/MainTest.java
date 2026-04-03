package com.groupstudy.service;

import com.groupstudy.model.*;

public class MainTest {

   public static void main(String[] args) throws Exception{
	   // ===== Setup Auth =====
	   AuthService auth = new AuthService();
	   
	   // ===== Create a PRIVATE room and a PUBLIC room =====
	   // private room
	   String rawPassword = "1234";
	   String hashed = auth.hashPassword(rawPassword);
	   
	   StudyRoom privateRoom = new StudyRoom("Final Prep", 5, true, hashed);
	   System.out.println("Private Room ID: " + privateRoom.getRoomId());
	   
	   // public room
	   StudyRoom publicRoom = new StudyRoom("Math Self Study", 3, false, null);
	   System.out.println("Public Room ID: " + publicRoom.getRoomId());
	   
	   // ===== Verify password (test hashing) =====
	   System.out.println("Correct password: " + privateRoom.verifyPassword("1234", auth)); // true
	   
	   System.out.println("Wrong password: " + privateRoom.verifyPassword("wrong", auth)); // false
	   
	   // ===== Add user and add user to rooms=====
	   User u1 = new User("Alice");
	   User u2 = new User("Bob");
	   
	   privateRoom.addUser(u1);
	   privateRoom.addUser(u2);
	   
	   // ===== Check the global status =====
	   User u3 = new User("Ten");
	   
	   System.out.println("User global status: " + u3.getCurrentStatus());
	   
	   u3.login();
	   
	   System.out.println("User global status: " + u3.getCurrentStatus());
	   
	   privateRoom.addUser(u3);
	   
	   System.out.println("User global status: " + u3.getCurrentStatus());
	   
	   privateRoom.removeUser(u3);
	   
	   System.out.println("User global status: " + u3.getCurrentStatus());
	   
	   u3.logout();
	   
	   System.out.println("User global status: " + u3.getCurrentStatus());
	   
	   
	   // ===== Check containsUser =====
	   System.out.println("Contains Alice: " + privateRoom.containsUser(u1));
	   System.out.println("Contains Bob: " + privateRoom.containsUser(u2));
	   
	   // ===== Start timer =====
	   StudyTimer timer = new StudyTimer();
	   timer.start(privateRoom);

	   // ===== Initial state (recent interaction -> BREAK) =====
	   Thread.sleep(2000);
	   
	   // ===== No interaction -> should become STUDYING =====
	   Thread.sleep(6000);
	   
	   // ===== Simulate interaction -> back to BREAK =====
	   System.out.println(">>> Alice interaction");
	   u1.setLastInteractionTime(System.currentTimeMillis());
	   
	   Thread.sleep(4000);
	   
	   
	   // ===== Print final study time =====
	   System.out.println("Final study time (ms): " + privateRoom.getStudyTime(u1) + "ms");
	   
	   System.out.println("Final study time (ms): " + privateRoom.getStudyTime(u2) + "ms");
	   
   }
}