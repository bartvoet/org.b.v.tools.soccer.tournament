package org.b.v.tools.soccer.tournament;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//stap 1: data updaten 

//TODO: menu tournament - team -game

public class TournamentPanel extends JPanel {

	private static final long serialVersionUID = 5699773301406172716L;
	public DefaultTableModel model;
	
    public TournamentPanel() {
        //super(new GridLayout(1,0));
    	super(new BorderLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.HORIZONTAL;
    	
    	
        String[] columnNames = {"Team","Team","Groep","Veld","Tijdstip"};
        Object[][] data = {};
        model=new DefaultTableModel(data, columnNames);
        
        final JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        
        
        String[] groupColumnNames = {"Groep","Team","Punten"};
        Object[][] groupEata = {{"U17","Baal","10"}};
        
        DefaultTableModel groupModel=new DefaultTableModel(groupEata, groupColumnNames);
        final JTable groupTable = new JTable(groupModel);   
        groupTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        groupTable.setFillsViewportHeight(true);
        JScrollPane datascrollPane = new JScrollPane(groupTable);
        
        add(scrollPane,BorderLayout.CENTER);
        add(datascrollPane,BorderLayout.EAST);
    }
 
}
