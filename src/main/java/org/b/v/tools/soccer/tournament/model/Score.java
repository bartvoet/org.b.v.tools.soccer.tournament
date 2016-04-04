package org.b.v.tools.soccer.tournament.model;

import java.io.Serializable;

public class Score  implements Serializable{
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
