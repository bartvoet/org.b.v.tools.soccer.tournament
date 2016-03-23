package org.b.v.tools.soccer.tournament.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.b.v.tools.soccer.tournament.extra.Entity;

public class Group extends Entity  {

	private String name;
	private long id;
	private List<GroupMember> members = new ArrayList<GroupMember>();
	private List<Game> games=new ArrayList<Game>();

	public Group(String name) {
		this.name = name;
	}
	
	public Group(long id,String name) {
		this.id=id;
		this.name=name;
	}

	public String getName() {
		return name;
	}
	
	public void changeName(String name) {
		this.name=name;
	}
	
	public void addNewMember(String name) {
		GroupMember member = new GroupMember(name);
		this.members.add(member);
	}

	public int compareTo(Group o) {
		return new Long(id).compareTo(new Long(o.id));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Group) {
			Group otherGroup = (Group)obj;
			return this.id==otherGroup.id;
		}
		return false;
	}

	public Collection<GroupMember> getMembers() {
		return this.members;
	}

	public void addNewMember(GroupMember entity) {
		this.members.add(entity);
		
	}

	public void removeMember(GroupMember entity) {
		this.members.remove(entity);
	}

	public void updateMember(GroupMember entity) {
		boolean found=false;
		int position=0;
		for(GroupMember corresponding:this.members) {
			if(corresponding.equals(entity)) {
				found=true;
				break;
			}
			position++;
		}
		
		if(found) {
			this.members.set(position, entity);
		}
	}

	public GroupMember getMemberByName(String object) {
		for(GroupMember member : members) {
			if(member.getTeamName().equals(object)) {
				return member;
			}
		}
		return null;
	}

	public List<Game> generateCandidateGames(String name2) {
		List<GroupMember> copy = new ArrayList<GroupMember>(this.getMembers());
		List<Game> tempGames = new ArrayList<Game>();
		
		for(GroupMember member : this.getMembers()) {
			for(GroupMember counter : copy) {
				if(!member.equals(counter)) {
					Game game = new Game(member,counter);
					tempGames.add(game);
				}
			}
			copy.remove(copy.indexOf(member));
		}
		
		return tempGames;
	}

	public void addNewGame(Game entity) {
		if(games.contains(entity)) {
			return;
		}
		
		this.games.add(entity);
	}

	public void removeGame(Game entity) {
		this.games.remove(entity);
	}

	public void updateGame(Game entity) {
		boolean found=false;
		int position=0;
		for(Game corresponding:this.games) {
			if(corresponding.equals(entity)) {
				found=true;
				break;
			}
			position++;
		}
		
		if(found) {
			this.games.set(position, entity);
		}
		
	}

	public Collection<Game> getGames() {
		return this.games;
	}
	
	private Ranking searchRankingByMember(GroupMember member,Collection<Ranking> set) {
		for(Ranking ranking:set) {
			if(member.equals(ranking.getMember())) {
				return ranking;
			}
		}
		return null;
	}
	
	public List<Ranking> calculateRanking() {
		List<Ranking> rankings = new ArrayList<Ranking>();
		
		for(GroupMember member : members) {
			rankings.add(new Ranking(member));
		}
		
		for(Game game:games){
			searchRankingByMember(game.getHome(),rankings).addGame(game);
			searchRankingByMember(game.getOther(),rankings).addGame(game);
		}
		
		Collections.sort(rankings);
		Collections.reverse(rankings);
		
		
		return rankings;
	}
}
