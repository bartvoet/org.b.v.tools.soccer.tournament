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
//		if(compare == 0) {
//			return this.member.getTeamName()
//					.compareTo(o.member.getTeamName());
//		}
		return compare;
	}

	public void addGame(Game match) {
		this.matches.add(match);
	}
}
