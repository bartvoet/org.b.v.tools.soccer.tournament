package org.b.v.tools.soccer.tournament;


import java.awt.Dimension;
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
        super(new GridLayout(1,0));
 
        String[] columnNames = {"Team","Team","Groep","Veld","Tijdstip"};
 
        Object[][] data = {};
 
        model=new DefaultTableModel(data, columnNames);
        
        final JTable table = new JTable(model);

        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
 
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }
 
}
