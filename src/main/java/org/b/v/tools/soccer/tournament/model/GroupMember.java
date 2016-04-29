package org.b.v.tools.soccer.tournament.model;

import org.b.v.tools.soccer.tournament.extra.Entity;

public class GroupMember extends Entity implements Team {
	
	private String teamName;
	
	public GroupMember(String teamName) {
		this.teamName = teamName;
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public void setTeamName(String name) {
		this.teamName=name;
	}

	public String getRepresentation() {
		return this.teamName;
	}
	
}
