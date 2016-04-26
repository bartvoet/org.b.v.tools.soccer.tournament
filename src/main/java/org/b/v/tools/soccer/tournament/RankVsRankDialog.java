package org.b.v.tools.soccer.tournament;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.b.v.tools.soccer.tournament.model.Game;
import org.b.v.tools.soccer.tournament.model.Group;
import org.b.v.tools.soccer.tournament.model.RankingMember;

public class RankVsRankDialog extends JDialog {

	private UpdateEvent event;
	private GroupRepository gamesRepository;
	private JComboBox<Group> homeBox = new JComboBox<Group>();
	private JComboBox<Group> outBox = new JComboBox<Group>();
	
	private JTextArea homeRank=new JTextArea();
	private JTextArea outRank=new JTextArea();
	
	private JButton add = new JButton("Voeg toe");
	private JButton close = new JButton("Sluiten");
	
	private static Pattern rangePattern = Pattern.compile("\\s*(\\d+)\\s*-\\s*(\\d+)\\s*");
	private static Pattern integerPattern = Pattern.compile("\\s*(\\d+)\\s*");
	
	
	public RankVsRankDialog(GroupRepository gamesRepository, UpdateEvent updateEvent) {
		event=updateEvent;
		this.gamesRepository = gamesRepository;
		
		this.setLayout(new FlowLayout());
		this.setSize(new Dimension(600,300));
		this.add(new JLabel("Reeks"));
		this.add(homeBox);
		this.add(new JLabel("Positie"));
		this.add(homeRank);
		this.homeRank.setColumns(3);
		this.add(new JLabel("tegen reeks"));
		this.add(outBox);
		this.add(new JLabel("Positie"));
		this.add(outRank);
		this.outRank.setColumns(3);
		
		this.add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				RankingRange homeRange = homeRank();
				
				for(int i=homeRange.from();i<=homeRange.to();i++) {
					RankingMember home = new RankingMember((Group)homeBox.getSelectedItem(),i);
					RankingMember out = new RankingMember((Group)outBox.getSelectedItem(),i);
					RankVsRankDialog.this.gamesRepository.addNoGroupGame(new Game(home,out));
				}
				
				RankVsRankDialog.this.event.registerUpdate();
				RankVsRankDialog.this.setVisible(false);
			}
		});
		
		this.close.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				RankVsRankDialog.this.setVisible(false);
			}
		});

		this.add(add);
		this.add(close);
	}
	
	private class RankingRange {
		private int from,to;
		public RankingRange(int from,int to) {
			this.from=from;
			this.to=to;
		}
		
		public int from() {
			return from;
		}
		
		public int to() {
			return to;
		}
		
		public int numberOfTeams() {
			return this.to-from;
		}
		
		public boolean isSameLevel(RankingRange other) {
			return numberOfTeams() == other.numberOfTeams();
		}
	}
	
	
	public RankingRange parseRank(JTextArea area) {
		Matcher range = rangePattern.matcher(area.getText());
		
		if(range.matches()) {
			return new RankingRange(Integer.parseInt(range.group(1)),Integer.parseInt(range.group(2)));
		}
		
		int rank = Integer.parseInt(area.getText());
		return new RankingRange(rank,rank);
	}
	
	public RankingRange homeRank() {
		return parseRank(this.homeRank);
	}

	public RankingRange outRank() {
		return parseRank(this.outRank);
	}

	
	public void prepareCleanScreen() {
		homeBox.removeAllItems();
		outBox.removeAllItems();
		
		for(Group group : this.gamesRepository.getAllGroups()) {
			homeBox.addItem(group);
			outBox.addItem(group);
		}
	}
}
