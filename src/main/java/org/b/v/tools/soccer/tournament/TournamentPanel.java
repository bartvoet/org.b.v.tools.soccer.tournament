package org.b.v.tools.soccer.tournament;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.print.PrinterException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.b.v.tools.soccer.tournament.model.Game;
import org.b.v.tools.soccer.tournament.model.Group;
import org.b.v.tools.soccer.tournament.model.Ranking;

public class TournamentPanel extends JPanel {

	private static final long serialVersionUID = 5699773301406172716L;

	private DefaultTableModel gameModel;
	private DefaultTableModel groupModel;
	
	private RowTable table;
	private RowTable groupTable;
	
    public TournamentPanel() {
    	super(new BorderLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	c.fill = GridBagConstraints.HORIZONTAL;
    	
    	
        String[] columnNames = {"Thuis","Uit","Groep","Veld","Tijdstip","Score","Penalties","Beeindigd"};
        Object[][] data = {};
        gameModel=new DefaultTableModel(data, columnNames);
        
        table = new RowTable(gameModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        
        
        String[] groupColumnNames = {"Groep","Team","Punten"};
        Object[][] groupEata = {};
        
        groupModel=new DefaultTableModel(groupEata, groupColumnNames);
        groupTable = new RowTable(groupModel);   
        groupTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        groupTable.setFillsViewportHeight(true);
        JScrollPane datascrollPane = new JScrollPane(groupTable);
        
        add(scrollPane,BorderLayout.CENTER);
        add(datascrollPane,BorderLayout.EAST);
    }
    
	private static Color rgb(int r,int g,int b) {
		return new Color(r,g,b);
	}
    
	private Color[] AVAILABLE_COLORS = 
			new Color[] {Color.LIGHT_GRAY,Color.CYAN,Color.ORANGE,
						Color.YELLOW,rgb(144,238,144),rgb(127,255,212),
						rgb(176,196,222),rgb(152,251,152),rgb(250,164,96),
						rgb(102,205,170)};
	
	private int counter=0;
	private Map<Integer,Color> colorsForGroup = new TreeMap<Integer,Color>();
	
	private Color colorForGroup(int id) {
		if(!this.colorsForGroup.containsKey(id)) {
			int number = counter % AVAILABLE_COLORS.length;
			counter++;
			colorsForGroup.put(id, AVAILABLE_COLORS[number]);
		}
		return colorsForGroup.get(id);
	}
    
    public void refreshGroups(Collection<Group> teamsPerGroup){
    	
    	int rowsToDelete = groupModel.getRowCount();
    	for (int i = rowsToDelete - 1; i >= 0; i--) {
    		groupModel.removeRow(i);
    	}
    	
    	int rowCount=0;
    	
    	for(Group group :teamsPerGroup) {
    		Collection<Ranking> rankings = group.calculateRanking();
    		groupModel.addRow(new Object[]{group.getCategory().name() + " " + group.getName()});
    		groupTable.setRowColor(rowCount, colorForGroup(group.getId().intValue()));
    		rowCount++;
    		for(Ranking ranking : rankings) {
    			groupModel.addRow(new Object[]{"",
    						ranking.getMember().getTeamName(),
    						ranking.getPoints()});
    			groupTable.setRowColor(rowCount, colorForGroup(group.getId().intValue()));
    			rowCount++;
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
	   	int numberOfRowsToDelete = gameModel.getRowCount();
    	for (int i = numberOfRowsToDelete - 1; i >= 0; i--) {
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
    	
    	int counter = 0;
    	
		for(Combination team : allMatches) {
			
			gameModel.addRow(new Object[]{
									  team.game.getHome().getTeamName(),
									  team.game.getOther().getTeamName(),
									  team.getGroupName(),
									  team.game.getField(),
									  team.game.getTimeAsString(),
									  team.game.getHomeScore() + " - " + team.game.getOutScore(),
									  team.game.getHomePenalties() + " - " + team.game.getOutPenalties(),
									  team.game.isFinished()?"ja":"nee"});
			
			if(team.group!=null) {
				table.setRowColor(counter,colorForGroup(team.group.getId().intValue()) );
			}
			
			counter++;
		}
	}

	
	public Collection<Combination> allCombinations(Collection<Group> allGroups,Collection<Game> nonGroupGames) {
  	
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
    	
    	return allMatches;
	}
	
	private String node(String name,String value) {
		return "\"" + name + "\":" + "\"" + value + "\",";
	}
	
	private String lastNode(String name,String value) {
		return "\"" + name + "\":" + "\"" + value + "\"";
	}
	
	public String colorAsHexValue(Color color) {
		return  String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public void exportJson(Collection<Group> allGroups,Collection<Game> nonGroupGames,PrintStream stream) {
		stream.println("[");
		boolean first = true;
		for(Combination team : allCombinations(allGroups,nonGroupGames)) {
			if(!first) {
				stream.print(",");
			}
			first = false;
			stream.println(
					"{" +
					node("home",team.game.getHome().getTeamName()) +
					node("out", team.game.getOther().getTeamName()) +
					node("group",team.getGroupName()) +
					node("field",team.game.getField()) +
					node("time",team.game.getTimeAsString()) +
					node("score",team.game.getHomeScore() + " - " + team.game.getOutScore()) +
					node("penalties",team.game.getHomePenalties() + " - " + team.game.getOutPenalties()) +
					node("finished",(team.game.isFinished()?"ja":"nee")) +
					lastNode("groupColor", "" + (team.group !=null ?
													(colorAsHexValue(colorForGroup(team.group.getId().intValue())))
													:"#ffffff")
							) +
					"}"
			);
			

		}
		stream.print("]");
	}
	
	public void exportRankingJson(Collection<Group> teamsPerGroup,PrintStream stream) {
		stream.println("[");
		boolean firstGroup = true;
		
		
		for(Group group :teamsPerGroup) {
			if(!firstGroup) {
				stream.print(",");
			}
			stream.print("{");
			firstGroup=false;
    		Collection<Ranking> rankings = group.calculateRanking();
    		stream.print(node("group",group.getCategory().name() + " " + group.getName()));
    		stream.print(node("color","" + (colorAsHexValue(colorForGroup(group.getId().intValue())))));
    		
    		boolean first = true;
    		stream.println("\"teams\":[");
    		for(Ranking ranking : rankings) {
    			if(!first) {
    				stream.print(",");
    			}
    			first=false;

    			stream.print("{");
    			stream.print(node("team",ranking.getMember().getTeamName()));
    			stream.print(lastNode("points","" + ranking.getPoints()));
    			stream.print("}");
    		}
    		stream.println("]}");
    	}
		stream.println("]");
	}

	
	public void printMatches() {
		try {
			this.table.print();
		} catch (PrinterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printRanking() {
		try {
			this.groupTable.print();
		} catch (PrinterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
