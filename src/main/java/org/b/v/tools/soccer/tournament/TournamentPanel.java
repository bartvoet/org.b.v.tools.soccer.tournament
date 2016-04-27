package org.b.v.tools.soccer.tournament;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.b.v.tools.soccer.tournament.model.Game;
import org.b.v.tools.soccer.tournament.model.Group;
import org.b.v.tools.soccer.tournament.model.Ranking;

public class TournamentPanel extends JPanel {

	private static final long serialVersionUID = 5699773301406172716L;

	private DefaultTableModel gameModel;
	private DefaultTableModel groupModel;
	
    public TournamentPanel() {
    	super(new BorderLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.HORIZONTAL;
    	
    	
        String[] columnNames = {"Thuis","Uit","Groep","Veld","Tijdstip","Score","Beeindigd"};
        Object[][] data = {};
        gameModel=new DefaultTableModel(data, columnNames);
        
        final JTable table = new JTable(gameModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        
        
        String[] groupColumnNames = {"Groep","Team","Punten"};
        Object[][] groupEata = {};//{{"U17","Baal","10"}};
        
        groupModel=new DefaultTableModel(groupEata, groupColumnNames);
        final JTable groupTable = new JTable(groupModel);   
        groupTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        groupTable.setFillsViewportHeight(true);
        JScrollPane datascrollPane = new JScrollPane(groupTable);
        
        add(scrollPane,BorderLayout.CENTER);
        add(datascrollPane,BorderLayout.EAST);
    }
    
    public void refreshGroups(Collection<Group> teamsPerGroup){
    	
    	int rowCount = groupModel.getRowCount();
    	for (int i = rowCount - 1; i >= 0; i--) {
    		groupModel.removeRow(i);
    	}
    	
    	for(Group group :teamsPerGroup) {
    		Collection<Ranking> rankings = group.calculateRanking();
    		groupModel.addRow(new Object[]{group.getCategory().name() + " " + group.getName()});
    		for(Ranking ranking : rankings) {
    			groupModel.addRow(new Object[]{"",
    						ranking.getMember().getTeamName(),
    						ranking.getPoints()});
    		}
    	}
    }

    private class Combination {
    	public Group group;
    	public Game game;

    	public Combination(Group group, Game team) {
			this.group = group;
			this.game = team;
		}
    	
    	public Combination(Game game) {
    		this.game=game;
    	}

		public String getGroupName(){
    		return group==null?"":group.getCategory().name() + " " + group.getName();
    	}
    }
    
	public <T> void refreshGames(Collection<Group> allGroups,Collection<Game> nonGroupGames) {
	   	int rowCount = gameModel.getRowCount();
    	for (int i = rowCount - 1; i >= 0; i--) {
    		gameModel.removeRow(i);
    	}
    	
    	List<Combination> allMatches = new ArrayList<Combination>();
    	
    	for(Group group :allGroups) {
    		Collection<Game> teams = group.getGames();
    		for(Game team : teams) {
    			allMatches.add(new Combination(group,team));
    		}
    	}
    	
    	for(Game team: nonGroupGames) {
    		allMatches.add(new Combination(team));
    	}
    	
    	Collections.sort(allMatches, new Comparator<Combination>() {
    		public int compare(Combination o1, Combination o2) {
    			return o1.game.getTime().compareTo(o2.game.getTime());
    		}
    	});
    	
		for(Combination team : allMatches) {
			gameModel.addRow(new Object[]{
									  team.game.getHome().getTeamName(),
									  team.game.getOther().getTeamName(),
									  team.getGroupName(),
									  team.game.getField(),
									  team.game.getTimeAsString(),
									  team.game.getHomeScore() + " - " + team.game.getOutScore(),
									  team.game.isFinished()?"ja":"nee"});
		}
		
	}
 
}
