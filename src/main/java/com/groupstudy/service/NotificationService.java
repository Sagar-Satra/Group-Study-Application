package com.groupstudy.service;

import com.groupstudy.model.Notification;
import com.groupstudy.model.Notification.NotificationType;
import com.groupstudy.model.User;

public class NotificationService {
	// wrapper methods around user.addNotification() in user.java
	
	public void addPokemonEvolution(User user, String pokemonName, int stage) {
        String message = String.format("🎉 Your Pokemon evolved to %s (Stage %d)!", 
            pokemonName, stage);
        user.addNotification(new Notification(message, NotificationType.POKEMON_EVOLVED));
    }
    
    public void addTrophy(User user, String pokemonName) {
        String message = String.format("🏆 New trophy earned: %s!", pokemonName);
        user.addNotification(new Notification(message, NotificationType.TROPHY_EARNED));
    }
    
    public void addRankTop(User user) {
        String message = "👑 You're #1 on the global leaderboard!";
        user.addNotification(new Notification(message, NotificationType.RANK_TOP));
    }
    
    // wrapper method around user.clearNotifications() in user.java
    public void clearOnRoomExit(User user) {
        user.clearNotifications();
    }
}
