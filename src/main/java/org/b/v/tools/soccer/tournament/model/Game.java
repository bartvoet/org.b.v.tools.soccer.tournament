package org.b.v.tools.soccer.tournament.model;

public class Game {
	private Team home;
	private Team other;
	private Score homeScore;
	private Score outScore;

	public Game(Team home,Team out) {
		this.home=home;
		this.other=out;
		this.homeScore=new Score();
		this.outScore=new Score();
	}
	
	public Team getHome() {
		return home;
	}

	public Team getOther() {
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
	
	
	
	
	
	
	//contains 2 teams
	//score attached to each team
	//optional penalties
	//results in points for each team (but can be overwritten if necessary)
	
}
