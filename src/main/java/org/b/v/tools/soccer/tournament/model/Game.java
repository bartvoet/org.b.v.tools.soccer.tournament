package org.b.v.tools.soccer.tournament.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.b.v.tools.soccer.tournament.extra.Entity;

public class Game extends Entity {
	private static final long serialVersionUID = 1L;
	private Team home;
	private Team other;
	private int homeScore;
	private int outScore;
	private int homePenalties;
	private int outPenalties;
	private TimeOfDay time=new TimeOfDay(0,0);
	private String field="A";
	
	private boolean isFinished;



	public Game(Team home,Team out) {
		this.home=home;
		this.other=out;
		this.homeScore=0;
		this.outScore=0;
	}
	
	public void swithHomeAndOut() {
		Team temp = home;
		this.home = other;
		this.other = temp;
		
		int tempScore = this.homeScore;
		this.homeScore = this.outScore;
		this.outScore=tempScore;
		
		int tempPenalties = this.homePenalties;
		this.homePenalties = this.outPenalties;
		this.outPenalties = tempPenalties;
		
	}
	
	public Team getHome() {
		return home;
	}

	public Team getOther() {
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
	
	public Game withPenalties(int homeScore,int outScore) {
		this.homePenalties=homeScore;
		this.outPenalties=outScore;
		return this;
	}

	public TimeOfDay getTime() {
		return this.time;
	}

	public String getField() {
		return this.field;
	}

	public Game atField(String string) {
		this.field=string;
		return this;
	}

	public Game onTime(TimeOfDay time) {
		this.time=time;
		return this;
	}
	
	public Game onTime(int hour,int second) {
		this.time=new TimeOfDay(hour,second);
		return this;
	}

	public String extractHour() {
		int hour = this.time.getHour();
		
		if(hour < 10) {
			return "0" + Integer.toString(hour);
		}
		
		return Integer.toString(hour);
	}
	
	public String extractMinutes() {
		int minutes = this.time.getMinutes();
		if(minutes < 10) {
			return "0" + minutes;
		}
		
		return Integer.toString(minutes);
	}
	
	public String getTimeAsString() {
		return extractHour() + ":" + extractMinutes();
	}
	
	public void finishMatch() {
		isFinished=true;
	}
	
	public boolean isFinished() {
		return isFinished;
	}

	public boolean isDraw() {
		return (this.getHomeScore() == this.getOutScore());
	}
	
	public boolean hasWon(GroupMember member) {
		if(isDraw()) {
			return false;
		} else {
			if(member.equals(this.home)) {
				return this.homeScore > this.outScore;
			} else {
				return this.outScore > this.homeScore;
			}
		}
	}

	public int getHomePenalties() {
		return this.homePenalties;
	}

	public int getOutPenalties() {
		return this.outPenalties;
	}
	
	public int getPenaltiesFor(GroupMember member) {
		if(this.getHome().equals(member)) {
			return this.getHomePenalties();
		} 
		if(this.getOther().equals(member)) {
			return this.getOutPenalties();
		}
		throw new RuntimeException();
	}
	
	public int getGoalsFor(GroupMember member) {
		if(this.getHome().equals(member)) {
			return this.getHomeScore();
		} 
		if(this.getOther().equals(member)) {
			return this.getOutScore();
		}
		throw new RuntimeException();
	}
}
