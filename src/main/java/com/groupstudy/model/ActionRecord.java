package com.groupstudy.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single user action in the study session.
 * Stored in a LinkedList to maintain chronological order.
 */
public class ActionRecord {

	public enum ActionType {
		JOIN,       // user joined a room
		START,      // user started studying
		BREAK,      // user took a break
		RESUME,     // user resumed studying after break
		LEAVE,      // user left the room
		SESSION_END // session timer expired
	}

	private ActionType actionType;
	private String roomTitle;
	private LocalDateTime timestamp;

	public ActionRecord(ActionType actionType, String roomTitle) {
		if (actionType == null) {
			throw new IllegalArgumentException("Action type cannot be null");
		}
		this.actionType = actionType;
		this.roomTitle = roomTitle;
		this.timestamp = LocalDateTime.now();
	}

	public ActionType getActionType() {
		return actionType;
	}

	public String getRoomTitle() {
		return roomTitle;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * Returns a formatted timestamp string for display.
	 */
	public String getFormattedTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
		return timestamp.format(formatter);
	}

	/**
	 * Returns a user-friendly description of the action.
	 */
	public String getDescription() {
		switch (actionType) {
			case JOIN:
				return "Joined room \"" + roomTitle + "\"";
			case START:
				return "Started studying";
			case BREAK:
				return "Took a break";
			case RESUME:
				return "Resumed studying";
			case LEAVE:
				return "Left the room";
			case SESSION_END:
				return "Session ended";
			default:
				return actionType.name();
		}
	}

	/**
	 * Returns an emoji icon corresponding to the action type.
	 */
	public String getIcon() {
		switch (actionType) {
			case JOIN:     return "🚪";
			case START:    return "📖";
			case BREAK:    return "☕";
			case RESUME:   return "🔄";
			case LEAVE:    return "👋";
			case SESSION_END: return "⏰";
			default:       return "📝";
		}
	}

	@Override
	public String toString() {
		return String.format("[%s] %s %s", getFormattedTime(), getIcon(), getDescription());
	}
}
