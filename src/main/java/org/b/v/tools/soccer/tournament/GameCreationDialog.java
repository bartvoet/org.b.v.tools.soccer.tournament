package org.b.v.tools.soccer.tournament;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.b.v.tools.soccer.tournament.extra.EntityFilter;
import org.b.v.tools.soccer.tournament.extra.EntityMapper;
import org.b.v.tools.soccer.tournament.extra.EntityTableModel;
import org.b.v.tools.soccer.tournament.model.Game;
import org.b.v.tools.soccer.tournament.model.Group;

public class GameCreationDialog extends JDialog {

	private static final long serialVersionUID = -7208676901332235553L;
	
	private JComboBox<String> combo = new JComboBox<String>();

	private JButton addButton;
	private JButton cancelButton;

	private GroupRepository gamesRepository;

	private UpdateEvent event;

	private JTable table;
	private EntityTableModel<Game> games;
	
	private JButton generateButton;

	private JButton saveAndExitButon;
	
	private JButton removeButton;
	
	private JButton switchButton;
	
	
	
	public GameCreationDialog(GroupRepository gamesRepository,UpdateEvent event) {
        this.gamesRepository = gamesRepository;
        this.event=event;
		setLayout(new BorderLayout());
        initializeTable();
        initializeGeneralInfo();
        initializeButtonPanel();
        initializeButtonActions();
        prepareCleanScreen();
        setSize(new Dimension(600,300));
	}

	private void initializeGeneralInfo() {
		JPanel infoPanel = new JPanel();
        infoPanel.add(new JLabel("Naam groep"));
        infoPanel.add(combo);
        add(infoPanel,BorderLayout.NORTH);
	}
		
	private Group group;
	
	private static Pattern scorePattern = Pattern.compile("\\s*(\\d+)\\s*-\\s*(\\d+)\\s*");
	private static Pattern timePattern = Pattern.compile("\\s*(\\d+)\\s*:\\s*(\\d+)\\s*");
	
	private EntityMapper<Game> groupMemberMapper =
			new EntityMapper<Game>() {
				final String[] columnNames = {"Thuis","Uit","Score","Penalties","Veld","Tijdstip","Gedaan",""};
				@SuppressWarnings("rawtypes")
				final Class[] types = new Class [] {
						java.lang.String.class,
						java.lang.String.class,
						java.lang.String.class,
						java.lang.String.class,
						java.lang.String.class,
						java.lang.String.class,
						java.lang.Boolean.class,
						java.lang.Boolean.class};
				
				public String[] getColumnNames() {
					return columnNames;
				}

				public Class<?> getType(int columnIndex) {
					return types [columnIndex];
				}

	
				public Object[] map(Game entity) {
					return new Object[]{entity.getHome().getTeamName(),
										entity.getOther().getTeamName(),
										entity.getHomeScore() + " - " + entity.getOutScore(),
										entity.getHomePenalties() + " - " + entity.getOutPenalties(),
										entity.getField(),
										entity.getTimeAsString(),
										entity.isFinished(),
										null};
				}
				
				public Game map(Object[] data) {
					Game game = new Game(
									group.getMemberByName((String)data[0]),
									group.getMemberByName((String)data[1]))
							.withScores(parseFirstScore(data[2]), parseSecondScore(data[2]))
							.withPenalties(parseFirstScore(data[3]), parseSecondScore(data[3]))
							.atField((String)data[4])
							.onTime(parseTime((String)data[5]));
					Boolean isFinished = (Boolean)data[6];
					
					if(isFinished != null && isFinished.booleanValue()) {
						game.finishMatch();
					}
					return game;
				}
				
				@Override
				public Game map(Game entity, Object[] data) {
					entity
						.withScores(parseFirstScore(data[2]), parseSecondScore(data[2]))
						.withPenalties(parseFirstScore(data[3]), parseSecondScore(data[3]))
						.atField((String)data[4])
						.onTime(parseTime((String)data[5]));
					
					
					Boolean isFinished = (Boolean)data[6];
					
					if(isFinished != null && isFinished.booleanValue()) {
						entity.finishMatch();
					}
					return entity;
				}

				private Date parseTime(String string) {
					Matcher matcher = timePattern.matcher(string);
					if(matcher.matches()) {
						DateFormat format = new SimpleDateFormat("hh:mm");
						try {
							return format.parse(string);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					return new Date();
				}

				private int parseFirstScore(Object object) {
					String score = (String)object;
					Matcher matcher = scorePattern.matcher(score);
					if(matcher.matches()) {
						return Integer.parseInt(matcher.group(1));
					}
					
					return 0;
				}

				private int parseSecondScore(Object object) {
					String score = (String)object;
					Matcher matcher = scorePattern.matcher(score);
					if(matcher.matches()) {
						return Integer.parseInt(matcher.group(2));
					}
					return 0;
				}

				public Comparable<?> getId(Game entity) {
					return new Long(entity.getId());
				}
				
				public Object[] getDefaultData() {
					return new Object[]{"","","0-0","0-0","","",null};
				}
				
				public boolean isMarkedToBeDeleted(Object[] data) {
					Object bool = data[7];
					if(bool!=null) {
						return (Boolean)data[7];
					}
					return false;
				}

			};	

	
	
	private void initializeTable() {
 		
		games = new EntityTableModel<Game>(groupMemberMapper);
		
        table = new JTable();
        games.configureTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane,BorderLayout.CENTER);

        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
	}


	private void initializeButtonPanel() {
		JPanel buttonPanel = new JPanel();
        
		generateButton = new JButton("Genereer");
        addButton = new JButton("Toevoegen");
        saveAndExitButon = new JButton("OK");
        cancelButton = new JButton("Cancel");
        removeButton = new JButton("Verwijderen");
        switchButton = new JButton("Wisselen");
        
        buttonPanel.add(generateButton);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(saveAndExitButon);
        buttonPanel.add(cancelButton);
        buttonPanel.add(switchButton);
        
        add(buttonPanel,BorderLayout.SOUTH);
	}

	private class GameEntityFilter implements EntityFilter<Game>{

		public Collection<Game> getEntities() {
			return group.getGames();
		}

		public void saveNewEntity(Game entity) {
			gamesRepository.enrichWithId(entity);
			group.addNewGame(entity);
		}

		public void removeExistingEntity(Game entity) {
			group.removeGame(entity);
		}

		public void updateEntity(Game entity) {
			group.updateGame(entity);
		}

		@Override
		public Game searchEntity(Long id) {
			return group.getGameById(id);
		}
		
	}
	
	private class GlobalGameEntityFilter implements EntityFilter<Game>{

		public Collection<Game> getEntities() {
			return gamesRepository.getAllNonGroupGames();
		}

		public void saveNewEntity(Game entity) {
			gamesRepository.enrichWithId(entity);
			gamesRepository.addNoGroupGame(entity);
		}

		public void removeExistingEntity(Game entity) {
			gamesRepository.removeNoGroupGame(entity);
		}

		public void updateEntity(Game entity) {
			gamesRepository.updateNoGroupGame(entity);
		}

		public Game searchEntity(Long id) {
			return gamesRepository.searchGameById(id);
		}
		
	} 
	
	
	private void initializeButtonActions() {
		
		generateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = (String)combo.getSelectedItem();
				
				String[] split = name.split(":");
				String category = split[0];
				String groupName = split[1];
				
				group = gamesRepository.searchGroupByName(category,groupName);
				games.add(group.generateCandidateGames(name),new GameEntityFilter());
			}
		});

		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				games.addEmptyEntityFromTable();
			}
		});
		
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//games.removeRows();
			}
		});
		
		saveAndExitButon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				games.dump(new GameEntityFilter());
				event.registerUpdate();
				GameCreationDialog.this.setVisible(false);
			}
		});
				
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameCreationDialog.this.setVisible(false);
			}
		});
		
		switchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Collection<Game> markedGames = games.getMarkedEntities();
				for(Game game:markedGames) {
					game.swithHomeAndOut();
				}
				event.registerUpdate();
				GameCreationDialog.this.setVisible(false);
			}
		});
		
		
		combo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String name = (String) combo.getSelectedItem();
				
				if("-".equals(name)) {
					games.load(new GlobalGameEntityFilter());
				} else if(name!=null) {
					String[] split = name.split(":");
					String category = split[0];
					String groupName = split[1];
					
					group = gamesRepository.searchGroupByName(category,groupName);
					if(group!=null) {
						games.load(new GameEntityFilter());
					}
				} else {
					games.load(new GlobalGameEntityFilter());
				}
			}
		});
	}

	public void prepareCleanScreen() {
		group = new Group("");
		
		combo.removeAllItems();
		combo.addItem("-");
		for(Group group : gamesRepository.getAllGroups()) {
			combo.addItem(group.getCategory().name() +":"  + group.getName());
		}
		
		games.clean();
	}
	
}
