package org.b.v.tools.soccer.tournament.model;

public class Score {
	private int goals;
	private int penalties;
	
	public Score() {
		goals=0;
		penalties=0;
	}
	
	public int goal() {
		return ++goals;
	}
	
	public int goalScore() {
		return goals;
	}

	
}
