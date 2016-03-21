package org.b.v.tools.soccer.tournament;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.b.v.tools.soccer.tournament.model.Group;
import org.b.v.tools.soccer.tournament.model.GroupMember;

public class TournamentPanel extends JPanel {

	private static final long serialVersionUID = 5699773301406172716L;

	public DefaultTableModel model;
	private DefaultTableModel groupModel;
	
    public TournamentPanel() {
    	super(new BorderLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.HORIZONTAL;
    	
    	
        String[] columnNames = {"Thuis","Uit","Groep","Veld","Tijdstip"};
        Object[][] data = {};
        model=new DefaultTableModel(data, columnNames);
        
        final JTable table = new JTable(model);
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
    		List<GroupMember> teams = group.getMembers();
    		for(GroupMember team : teams) {
    			groupModel.addRow(new Object[]{group.getName(),team.getTeamName(),""});
    		}
    	}
    }
 
}
