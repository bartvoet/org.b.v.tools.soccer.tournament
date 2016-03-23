package org.b.v.tools.soccer.tournament.model;

import java.util.Date;

import org.b.v.tools.soccer.tournament.extra.Entity;

public class Game extends Entity {
	private GroupMember home;
	private GroupMember other;
	private int homeScore;
	private int outScore;
	private Date time=new Date();
	private String field="A";

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

	public Date getTime() {
		return this.time;
	}

	public String getField() {
		return this.field;
	}

	public Game atField(String string) {
		this.field=string;
		return this;
	}

	public Game onTime(Date time) {
		this.time=time;
		return this;
	}
}
