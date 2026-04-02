package com.groupstudy.service;

import java.security.MessageDigest;

public class AuthService {

	// Hash a password using SHA-256
	public String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			byte[] hash = md.digest(password.getBytes());
			
			StringBuilder hex = new StringBuilder();
			
			for (byte b : hash) {
				hex.append(String.format("%02x", b));
			}
			
			return hex.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
		
	// Compare raw password with hashed password
	public boolean verifyPassword(String input, String storedHash) {
		return hashPassword(input).equals(storedHash);
	}
}