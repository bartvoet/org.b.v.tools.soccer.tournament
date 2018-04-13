package org.b.v.tools.soccer.tournament;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.HttpURLConnection;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;

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
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        newContentPane.setOpaque(true); 
        setContentPane(newContentPane);
        
        initializeTheMenu();
        initSaveOnExit();
 
        pack();
        setVisible(true);
	}
	
	private void initSaveOnExit() {
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	if(fileState.isNotSaved()) {//needs saving
			    	int confirm = 
			    			JOptionPane.showOptionDialog(frame,
		                        "De file dient nog bewaard worden, druk Yes als je wil bewaren, ander No",
		                        "Druk Cancel als je niet wil sluiten", 
		                        JOptionPane.YES_NO_CANCEL_OPTION,
		                        JOptionPane.QUESTION_MESSAGE, 
		                        null, 
		                        null, 
		                        null);
			    	
	                switch(confirm) {
		                case(JOptionPane.YES_OPTION): {
		                	fileState.save();
		                	TournamentApplication.this.dispose();
		                }
		                case(JOptionPane.NO_OPTION): {
		                	TournamentApplication.this.dispose();
		                }
		                case(JOptionPane.CANCEL_OPTION) :{/*DO NOTHING*/}
	                }
			    } else {
			    	TournamentApplication.this.dispose();
			    }
		    }
		});
	}

	private void initializeTheMenu() {
		JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
		initializeTheFileMenu(menuBar);
		initializeTheAdminstrationMenu(menuBar);
		initializeThePrintMenu(menuBar);
		initializeTheHelpMenu(menuBar);
	}

	private void initializeThePrintMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("Printen");
        menu.setMnemonic(KeyEvent.VK_P); 
        menuBar.add(menu);
        
        JMenuItem printMatches = new JMenuItem("Wedstrijden afdrukken");
        printMatches.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newContentPane.printMatches();
			}
        });
        menu.add(printMatches);
        
        JMenuItem printRanking = new JMenuItem("Klassement afdrukken");
        printRanking.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newContentPane.printRanking();
			}
        });
        menu.add(printRanking);
        
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
	
	private JMenuItem saveTournamentAs = new JMenuItem("Bewaar als");
	private JMenuItem saveTournament = new JMenuItem("Bewaar");
	private JMenuItem openTournament = new JMenuItem("Open");
	private JMenuItem newTournament = new JMenuItem("Nieuw");
	private JMenuItem saveAndSendTournament = new JMenuItem("Verstuur");
    
	
	private void initializeTheFileMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("File");
		
		menu.setMnemonic(KeyEvent.VK_F);
        menu.add(openTournament);
        menu.add(saveTournamentAs);
        menu.add(saveTournament);
        menu.add(newTournament);
        menu.add(saveAndSendTournament);
        menuBar.add(menu);
        
        saveTournament.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        
        
        openTournament.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileState.open();
				refreshScreens();
			}
		});
        openTournament.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        
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
        
        saveAndSendTournament.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					TournamentApplication.this.sendPost();
					postWorker.execute();
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

	}
	
	private SwingWorker<Void, Void> postWorker = new SwingWorker<Void, Void>() {

		@Override
		protected Void doInBackground() throws Exception {
			sendPost();
			sendPostRanking();
			return null;
		}
		
	};
	
	// HTTP POST request
	private int sendPost() throws Exception {

		String url = "http://www.bvminecraft.org:8003/matches.json";
		URL obj = new URL(url);
		java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		
		// Send post request
		con.setDoOutput(true);
		newContentPane.exportJson(gamesRepository.getAllGroups(),
				 gamesRepository.getAllNonGroupGames(), new PrintStream(con.getOutputStream()));
		

		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return responseCode;

	}
	
	private int sendPostRanking() throws Exception {

		String url = "http://www.bvminecraft.org:8003/ranking.json";
		URL obj = new URL(url);
		java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		
		// Send post request
		con.setDoOutput(true);
		newContentPane.exportRankingJson(gamesRepository.getAllGroups(),new PrintStream(con.getOutputStream()));
		

		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return responseCode;

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
						+ "* Winnaar forceren (loting)\n" 
						+ "* Filteren op wedstrijden -1 uur en + 1 uur\n" 
						+ "* Kleuren toekennen aan wedstrijden (gedaan,bezig, ...)\n"
						+ "* Refresh, automatisch lezen van de file"
						);
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