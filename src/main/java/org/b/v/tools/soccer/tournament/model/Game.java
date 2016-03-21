package org.b.v.tools.soccer.tournament.model;

public class Game {
	private GroupMember home;
	private GroupMember other;
	private Score homeScore;
	private Score outScore;

	public Game(GroupMember home,GroupMember out) {
		this.home=home;
		this.other=out;
		this.homeScore=new Score();
		this.outScore=new Score();
	}
	
	public GroupMember getHome() {
		return home;
	}

	public GroupMember getOther() {
		return other;
	}

	public Score getHomeScore() {
		return homeScore;
	}

	public Score getOutScore() {
		return outScore;
	}
	
	public Game withScores(Score homeScore,Score outScore) {
		this.homeScore=homeScore;
		this.outScore=outScore;
		return this;
	}
}
