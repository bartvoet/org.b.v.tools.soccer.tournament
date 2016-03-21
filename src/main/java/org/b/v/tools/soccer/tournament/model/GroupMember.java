package org.b.v.tools.soccer.tournament.model;

import org.b.v.tools.soccer.tournament.extra.Entity;

public class GroupMember extends Entity{
	
	private String teamName;
	private int pointDeviation;
	
	public GroupMember(String teamName) {
		this.teamName = teamName;
	}
	
	
	
	public String getTeamName() {
		return teamName;
	}
}
