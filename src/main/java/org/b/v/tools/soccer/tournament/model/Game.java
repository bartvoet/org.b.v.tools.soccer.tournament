package org.b.v.tools.soccer.tournament.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.b.v.tools.soccer.tournament.extra.Entity;

public class Game extends Entity {
	private GroupMember home;
	private GroupMember other;
	private int homeScore;
	private int outScore;
	private int homePenalties;
	private int outPenalties;
	private Date time=new Date();
	private String field="A";
	
	private boolean isFinished;



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
	
	public Game withPenalties(int homeScore,int outScore) {
		this.homePenalties=homeScore;
		this.outPenalties=outScore;
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

	private String extractHour(Date date) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		
		if(hour < 10) {
			return "0" + Integer.toString(hour);
		}
		
		return Integer.toString(hour);
	}
	
	private String extractMinutes(Date date) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		
		int minutes = calendar.get(Calendar.MINUTE);
		if(minutes < 10) {
			return "0" + Integer.toString(calendar.get(Calendar.MINUTE));
		}
		
		return Integer.toString(calendar.get(Calendar.MINUTE));
	}
	
	public String getTimeAsString() {
		return extractHour(getTime()) + ":" + extractMinutes(getTime());
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
