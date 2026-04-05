package com.groupstudy.model;

public class LeaderboardEntry implements Comparable<LeaderboardEntry>{
	
	// private fields
	private String userName;
	private int trophyCount;
	private long studyMinutes;
	private int streak;
	
	public LeaderboardEntry(String userName, int trophyCount, long studyMinutes, int streak) {
		if(userName == null || userName.trim().isEmpty()) {
			throw new IllegalArgumentException("Username cannot be null or empty");
		}
		this.userName = userName;
		this.trophyCount = trophyCount;
		this.studyMinutes = studyMinutes;
		this.streak = streak;
	}
	
	public String getUserName() {
		return userName;
	}

	public int getTrophyCount() {
		return trophyCount;
	}

	public long getStudyMinutes() {
		return studyMinutes;
	}

	public int getStreak() {
		return streak;
	}
	
	@Override
	public int compareTo(LeaderboardEntry other) {
		// need descending order - higher trophies first
		// First compare by trophies
	    int trophyCompare = Integer.compare(other.trophyCount, this.trophyCount);
	    
	    if (trophyCompare != 0) {
	        return trophyCompare;  // Different trophy counts
	    }
	    
	    // Tie-breaker: compare by study time
	    return Long.compare(other.studyMinutes, this.studyMinutes);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof LeaderboardEntry)) return false;
		LeaderboardEntry e = (LeaderboardEntry) obj;
		return userName.equals(e.userName);
	}
	
	@Override
    public int hashCode() {
        return userName.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("%s - Trophies: %d, Time: %d min, Streak: %d",
            userName, trophyCount, studyMinutes, streak);
    }
  

}
