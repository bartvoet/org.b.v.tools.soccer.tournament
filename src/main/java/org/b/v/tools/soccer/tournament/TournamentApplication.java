package org.b.v.tools.soccer.tournament;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TournamentApplication extends JFrame implements UpdateEvent {

	private static final long serialVersionUID = -2747948660004275050L;

	private final TournamentPanel newContentPane = new TournamentPanel();
	private final GroupRepository gamesRepository = new GroupRepository();
	private final CategoryDialog categoryDialog =new CategoryDialog(gamesRepository,this);
	private final GroupDialog groupDialog=new GroupDialog(gamesRepository,this);
	private final GameCreationDialog gameDialog=new GameCreationDialog(gamesRepository,this);
	private final RankVsRankDialog rankVsRankDialog=new RankVsRankDialog(gamesRepository,this);
	
		
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
		initializeTheHelpMenu(menuBar);
	}

	
	private void initializeTheAdminstrationMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("Acties");
        menu.setMnemonic(KeyEvent.VK_A);
 
        JMenuItem addCategory = new JMenuItem("Categorien beheren");
        addCategory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				categoryDialog.prepareCleanScreen();
				categoryDialog.setVisible(true);
			}
        });
        
        JMenuItem addGroups = new JMenuItem("Reeksen beheren");
        addGroups.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupDialog.prepareCleanScreen();
				groupDialog.setVisible(true);
			}
		});
        
        JMenuItem addGroupVsGroup = new JMenuItem("Reeks tegen reeks");
        addGroupVsGroup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rankVsRankDialog.prepareCleanScreen();
				rankVsRankDialog.setVisible(true);
			}
		});

        JMenuItem addTerrain = new JMenuItem("Wedstrijden beheren");
        addTerrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameDialog.prepareCleanScreen();
				gameDialog.setVisible(true);
			}
		});
        
        menu.add(addCategory);
        menu.add(addGroups);
        menu.add(addTerrain);
        menu.add(addGroupVsGroup);
        menuBar.add(menu);
	}

	private SaveToFileState fileState = new SaveToFileState(this, gamesRepository);
	
    private static FileNameExtensionFilter filter = 
    		new FileNameExtensionFilter("Tournament file", "trm");

	private JMenuItem saveTournamentAs = new JMenuItem("Bewaar als");
	private JMenuItem saveTournament = new JMenuItem("Bewaar");
	private JMenuItem openTournament = new JMenuItem("Open");
	private JMenuItem newTournament = new JMenuItem("Nieuw");
    
	
	private void initializeTheFileMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("File");
		
		menu.setMnemonic(KeyEvent.VK_F);
        menu.add(openTournament);
        menu.add(saveTournamentAs);
        menu.add(saveTournament);
        menu.add(newTournament);
        menuBar.add(menu);
        
        saveTournament.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        
        
        openTournament.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileState.open();
				refreshScreens();
			}
		});
        
        saveTournamentAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileState.saveAs();
				updateTitleBarWithState();
			}
		});
        
        saveTournament.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileState.save();
				updateTitleBarWithState();
			}
		});
        
        newTournament.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileState.newTournament();
				registerUpdate();
			}
		});

	}
	
	private void initializeTheHelpMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem todo = new JMenuItem("Todo");
        todo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog todo=new JDialog();
				todo.setSize(new Dimension(600,300));
				JTextArea text = new JTextArea();
				text.setText(
						  "TODO's:\n"
						+ "* Refining rules of points" );
				todo.add(text);
				todo.setVisible(true);
			}
		});
        menu.add(todo);
        
        
        menu.add(new JMenuItem("Info"));
        menuBar.add(menu);
	}
	

	private static TournamentApplication frame;
    
	public static void main(String[] args) {
    	 frame = new TournamentApplication();
    }

	public void registerUpdate() {
		fileState.markChange();
		refreshScreens();
	}

	private void refreshScreens() {
		this.newContentPane.refreshGroups(gamesRepository.getAllGroups());
		this.newContentPane.refreshGames(gamesRepository.getAllGroups(),
										 gamesRepository.getAllNonGroupGames());

		updateTitleBarWithState();
	}

	private void updateTitleBarWithState() {
		switch(fileState.getState()) {
			case NEW:setTitle("Tornooi (nieuw)");break;
			case NEW_NOT_SAVED:setTitle("Tornooi (nieuw) *");break;
			case LOADED:setTitle(fileState.getPath());break;
			case LOADED_NOT_SAVED:setTitle(fileState.getPath() + "*");break;
		}
	}
}