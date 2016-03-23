package org.b.v.tools.soccer.tournament.model;

import org.b.v.tools.soccer.tournament.extra.Entity;

public class Game extends Entity {
	private GroupMember home;
	private GroupMember other;
	private int homeScore;
	private int outScore;

	public Game(GroupMember home,GroupMember out) {
		this.home=home;
		this.other=out;
		this.homeScore=0;
		this.outScore=0;
	}
	
	public GroupMember getHome() {
		return home;
	}

	public GroupMember getOther() {
		return other;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public int getOutScore() {
		return outScore;
	}
	
	public Game withScores(int homeScore,int outScore) {
		this.homeScore=homeScore;
		this.outScore=outScore;
		return this;
	}
}
