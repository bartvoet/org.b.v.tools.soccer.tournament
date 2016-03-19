package org.b.v.tools.soccer.tournament.model;

public class Team {
	
	private long id;
	private String teamName;
	private int pointDeviation;
	
	public Team(String teamName) {
		this.teamName = teamName;
	}
	
	public String getName() {
		return teamName;
	}
}
