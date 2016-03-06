package org.b.v.tools.soccer.tournament;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//stap 1: data updaten 

//TODO: menu tournament - team -game

public class TournamentPanel extends JPanel {

	private static final long serialVersionUID = 5699773301406172716L;
	 
	DefaultTableModel model;
	
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
 

    public static void main(String[] args) {
        
    	JFrame frame = new JFrame("Tornooi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        
        final JDialog groupDialog = new JDialog();
        
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.add(new JMenuItem("Open"));
        menu.add(new JMenuItem("Save"));
        menuBar.add(menu);
        
        menu = new JMenu("Acties");
        menu.setMnemonic(KeyEvent.VK_A);
        JMenuItem addGroups = new JMenuItem("Groepen beheren");
        addGroups.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupDialog.setVisible(true);
			}
		});
        
        menu.add(addGroups);
        menu.add(new JMenuItem("Ploegen beheren"));
        menuBar.add(menu);
 
        TournamentPanel newContentPane = new TournamentPanel();
        newContentPane.setOpaque(true); 
        frame.setContentPane(newContentPane);
 
        frame.pack();
        frame.setVisible(true);
    	
        Object[][] data = {
    	        {"Baal",       "Zemst",       "U17", "A", "12:30"},
    	        {"Sterrebeek", "Ramsel",      "U17", "B", "12:30"},
    	        {"Zemst",      "Sterrebeek",  "U17", "C", "13:00"},
    	        {"Baal",       "Ramsel",      "U17", "A", "12:30"},
    	        {"Zemst",      "Ramsel",      "U17", "A", "12:30"},
    	        {"Zemst",      "Ramsel",      "U17", "A", "12:30"}
            };
        
        for(Object[] row : data) {
        	newContentPane.model.addRow(row);
        }
    }
    
    //aanmaken van groep
    //aanmaken van matchen na creatie van group
}