package org.b.v.tools.soccer.tournament;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.b.v.tools.soccer.tournament.model.Game;
import org.b.v.tools.soccer.tournament.model.Group;
import org.b.v.tools.soccer.tournament.model.GroupMember;

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
    		Collection<GroupMember> teams = group.getMembers();
    		for(GroupMember team : teams) {
    			groupModel.addRow(new Object[]{group.getName(),team.getTeamName(),""});
    		}
    	}
    }

	public void refreshGames(Collection<Group> allGroups) {
	   	int rowCount = gameModel.getRowCount();
    	for (int i = rowCount - 1; i >= 0; i--) {
    		gameModel.removeRow(i);
    	}
    	
    	for(Group group :allGroups) {
    		Collection<Game> teams = group.getGames();
    		for(Game team : teams) {
    			gameModel.addRow(new Object[]{team.getHome().getTeamName(),
    									  team.getOther().getTeamName(),
    									  group.getName(),
    									  team.getField(),
    									  team.getTimeAsString(),
    									  team.getHomeScore() + " - " + team.getOutScore(),
    									  team.isFinished()?"ja":"nee"});
    		}
    	}
	}
 
}
