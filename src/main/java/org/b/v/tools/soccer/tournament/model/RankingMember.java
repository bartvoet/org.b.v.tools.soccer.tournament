package org.b.v.tools.soccer.tournament.model;

import org.b.v.tools.soccer.tournament.extra.Entity;

public class RankingMember extends Entity implements Team  {

	private static final long serialVersionUID = 8938311908762090143L;
	private Group group;
	private int rank;

	public RankingMember(Group group,int rank) {
		this.rank=rank;
		this.group=group;
	}
	
	@Override
	public String getTeamName() {
		Team team = group.getTeamAtRank(rank); 
		if(team==null) {
			return "/";
		}
		
		return group.getCategory().name() + " "
				+ group.getName() 
				+ "(" + rank + ") "  
				+  (group.isFinished()?team.getTeamName():"?");
	}
	
	public String getRepresentation() {
		return group.getCategory().name() + "-" +  group.getName() + "-" + rank;
	}

}
