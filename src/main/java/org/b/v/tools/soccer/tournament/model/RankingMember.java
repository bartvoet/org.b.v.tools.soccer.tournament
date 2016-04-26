package org.b.v.tools.soccer.tournament.model;

public class RankingMember implements Team {

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
		
		return group.getName() + "(" + rank + ") "  + team.getTeamName();
	}

}
