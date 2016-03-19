package org.b.v.tools.soccer.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

import org.b.v.tools.soccer.tournament.model.Game;
import org.b.v.tools.soccer.tournament.model.GroupMember;

public class GamesRepository {
	
	private Map<String,List<GroupMember>> teamsPerGroup = new TreeMap<String,List<GroupMember>>(); 
	private AtomicLong groupIds = new AtomicLong();
	
	
	public List<Game> getAllGamesSortedByTime() {
		return null;
	}

	public void createOrUpdateGroup(String text) {
		if(!teamsPerGroup.containsKey(text)) {
			teamsPerGroup.put(text, new ArrayList<GroupMember>());
		}
	}

	public void addTeamToGroup(String group,String name) {
		List<GroupMember> teams = teamsPerGroup.get(group);
		if(!teams.contains(name)) {	
			teams.add(new GroupMember(name));
		}
		
	}
	
	public Map<String,List<GroupMember>> getAll(){
		return this.teamsPerGroup;
	}
	
	public Set<String> getAllGroups() {
		return teamsPerGroup.keySet();
	}

	public List<GroupMember> getTeamsForAGroup(String team) {
		return teamsPerGroup.get(team);
	}
}
