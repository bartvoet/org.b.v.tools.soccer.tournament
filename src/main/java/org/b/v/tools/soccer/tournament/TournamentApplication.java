package org.b.v.tools.soccer.tournament;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

//stap 1: data updaten 

//TODO: menu tournament - team -game

public class TournamentApplication extends JFrame implements UpdateEvent {

	private static final long serialVersionUID = -2747948660004275050L;

	private final TournamentPanel newContentPane = new TournamentPanel();
	private final GamesRepository gamesRepository = new GamesRepository();
	private final GroupDialog groupDialog=new GroupDialog(gamesRepository,this);
	private final TeamDialog teamDialog = new TeamDialog();
	
	

	
	public TournamentApplication() {
		super("Tornooi");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        newContentPane.setOpaque(true); 
        setContentPane(newContentPane);
        
        initializeTheMenu();
 
        pack();
        setVisible(true);
	}

	private void initializeTheMenu() {
		JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
		initializeTheFileMenu(menuBar);
		initializeTheAdminstrationMenu(menuBar);
	}

	private void initializeTheAdminstrationMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("Acties");
        menu.setMnemonic(KeyEvent.VK_A);
 
        JMenuItem addGroups = new JMenuItem("Groepen maken");
        addGroups.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupDialog.prepareCleanScreen();
				groupDialog.setVisible(true);
			}
		});
        
        JMenuItem addTerrain = new JMenuItem("Velden beheren");
        addTerrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
        
        menu.add(addGroups);
        menu.add(addTerrain);
        menuBar.add(menu);
	}

	private void initializeTheFileMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.add(new JMenuItem("Open"));
        menu.add(new JMenuItem("Save"));
        menuBar.add(menu);
	}
	
    public static void main(String[] args) {
        
    	TournamentApplication frame = new TournamentApplication();

    	
        Object[][] data = {
    	        {"Baal",       "Zemst",       "U17", "A", "12:30"},
    	        {"Sterrebeek", "Ramsel",      "U17", "B", "12:30"},
    	        {"Zemst",      "Sterrebeek",  "U17", "C", "13:00"},
    	        {"Baal",       "Ramsel",      "U17", "A", "12:30"},
    	        {"Zemst",      "Ramsel",      "U17", "A", "12:30"},
    	        {"Zemst",      "Ramsel",      "U17", "A", "12:30"}
            };
        
        for(Object[] row : data) {
        	frame.newContentPane.model.addRow(row);
        }
    }

	public void update() {
		this.newContentPane.refreshGroups(gamesRepository.getAll());
	}
    
    //aanmaken van groep
    //aanmaken van matchen na creatie van group
}