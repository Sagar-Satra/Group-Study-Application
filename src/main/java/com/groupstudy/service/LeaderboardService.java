package com.groupstudy.service;

import com.groupstudy.adt.ListInterface;
import com.groupstudy.implementation.ArrayListImplementation;
import com.groupstudy.implementation.MergeSortImplementation;
import com.groupstudy.model.LeaderboardEntry;
import com.groupstudy.model.StudyRoom;
import com.groupstudy.model.User;

public class LeaderboardService {
	private MergeSortImplementation sorter;
	private ListInterface<LeaderboardEntry> globalLeaderboard;
	
	public LeaderboardService() {
		this.sorter = new MergeSortImplementation();
		this.globalLeaderboard = new ArrayListImplementation<LeaderboardEntry>();
	}
	
	public void addOrUpdateEntry(LeaderboardEntry entry) {
		if (entry == null) {
			throw new IllegalArgumentException("Entry cannot be null");
		}
		
		boolean found = false;
		for (int i=0; i < globalLeaderboard.getLength(); i++) {
			if (globalLeaderboard.get(i).getUserName().equals(entry.getUserName())){
				globalLeaderboard.set(i, entry);
				found = true;
				break;
			}
		}
		
		if (!found) {
			globalLeaderboard.add(entry);
		}
	}
	
	public LeaderboardEntry[] getTopEntries(int n) {
		LeaderboardEntry[] array = listToArray();
		sorter.sort(array);
		
		int size = Math.min(n,  array.length);
		LeaderboardEntry[] top = new LeaderboardEntry[size];
		
		for(int i = 0; i < size; i++) {
			top[i] = array[i];
		}
		return top;
	}
	
	public int getUserRank(String username) {
		LeaderboardEntry[] array = listToArray();
        sorter.sort(array);
        
        for (int i = 0; i < array.length; i++) {
            if (array[i].getUserName().equals(username)) {
                return i + 1;
            }
        }
        
        return -1;
	}
	
	
	public LeaderboardEntry[] getRoomLeaderBoard(StudyRoom room) {
		if(room == null) {
			return new LeaderboardEntry[0];
		}
		
		int roomSize = 0;
		for(User user : room.getAllStatus().keySet()) {
			roomSize++;
		}
		
		LeaderboardEntry[] array = new LeaderboardEntry[roomSize];
		int index = 0;
		for(User user : room.getAllStatus().keySet()) {
			array[index] = new LeaderboardEntry(user.getName(), user.getTrophyCount(), room.getStudyTime(user)/60000,  user.getCurrentStreak());
			index++;
		}
		
		sorter.sort(array);
		return array;
	}
	
	public LeaderboardEntry[] getAllEntries() {
		LeaderboardEntry[] array = listToArray();
		sorter.sort(array);
		return array;
	}
	
	public void clear() {
		globalLeaderboard.clear();
	}
	
	public int getSize() {
		return globalLeaderboard.getLength();
	}
	
	private LeaderboardEntry[] listToArray() {
        LeaderboardEntry[] array = new LeaderboardEntry[globalLeaderboard.getLength()];
        for (int i = 0; i < globalLeaderboard.getLength(); i++) {
            array[i] = globalLeaderboard.get(i);
        }
        return array;
    }
	
}
