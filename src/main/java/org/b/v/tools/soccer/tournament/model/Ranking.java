package org.b.v.tools.soccer.tournament.model;

import java.util.ArrayList;
import java.util.List;

public class Ranking implements Comparable<Ranking> {
	private GroupMember member;
	private Integer forcedPoints= new Integer(-1);
	private List<Game> matches = new ArrayList<Game>();
	
	public Ranking(GroupMember member) {
		super();
		this.member = member;
	}

	public GroupMember getMember() {
		return member;
	}

	public Integer getPoints() {
		int points=0;
//		if(forcedPoints.intValue() >= 0) {
//			return forcedPoints;
//		} else {
			for(Game game:matches) {
				if(game.isFinished()) {
					if(game.hasWon(this.member)) {
						points=points+3;
					} 
					if(game.isDraw()) {
						points++;
					} 
				}
			}
//		}
		return new Integer(points);
	}
	
	public void forcePoints(int points) {
		this.forcedPoints=points;
	}
	
	public void undoForce() {
		this.forcedPoints=-1;
	}

	public int compareTo(Ranking o) {
		int compare = this.getPoints().compareTo(o.getPoints());
		
		if(compare==0) {
			compare = this.getTotalOfPenalties().compareTo(o.getTotalOfPenalties());
			if(compare== 0) {
				compare = this.getTotalOfGoals().compareTo(o.getTotalOfGoals());
			}
		}
		
		return compare;
	}

	private Integer getTotalOfGoals() {
		int total = 0;
		
		for(Game game:this.matches) {
			total += game.getGoalsFor(this.member);
		}
		
		return new Integer(total);
	}

	private Integer getTotalOfPenalties() {
		int total = 0;
		
		for(Game game:this.matches) {
			total += game.getPenaltiesFor(this.member);
		}
		
		return new Integer(total);
	}

	public void addGame(Game match) {
		this.matches.add(match);
	}
}
