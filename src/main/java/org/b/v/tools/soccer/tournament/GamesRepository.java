package org.b.v.tools.soccer.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

import org.b.v.tools.soccer.tournament.model.Game;

public class GamesRepository {
	
	private Map<String,List<String>> teamsPerGroup = new TreeMap<String,List<String>>(); 
	private AtomicLong groupIds = new AtomicLong();
	
	
	public List<Game> getAllGamesSortedByTime() {
		return null;
	}

	public void createOrUpdateGroup(String text) {
		if(!teamsPerGroup.containsKey(text)) {
			teamsPerGroup.put(text, new ArrayList<String>());
		}
	}

	public void addTeamToGroup(String group,String name) {
		List<String> teams = teamsPerGroup.get(group);
		if(!teams.contains(name)) {	
			teams.add(name);
		}
		
	}
	
	public Map<String,List<String>> getAll(){
		return this.teamsPerGroup;
	}
	
	public Set<String> getAllGroups() {
		return teamsPerGroup.keySet();
	}

	public List<String> getTeamsForAGroup(String team) {
		return teamsPerGroup.get(team);
	}
}
