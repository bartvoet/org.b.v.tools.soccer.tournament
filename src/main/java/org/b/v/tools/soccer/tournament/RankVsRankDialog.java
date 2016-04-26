package org.b.v.tools.soccer.tournament;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	public RankVsRankDialog(GroupRepository gamesRepository, UpdateEvent updateEvent) {
		event=updateEvent;
		this.gamesRepository = gamesRepository;
		
		this.setLayout(new FlowLayout());
		this.setSize(new Dimension(600,300));
		this.add(new JLabel("Reeks"));
		this.add(homeBox);
		this.add(new JLabel("Positie"));
		this.add(homeRank);
		this.homeRank.setColumns(1);
		this.add(new JLabel("tegen reeks"));
		this.add(outBox);
		this.add(new JLabel("Positie"));
		this.add(outRank);
		this.outRank.setColumns(1);
		
		this.add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				RankingMember home = new RankingMember((Group)homeBox.getSelectedItem(),homeRank());
				RankingMember out = new RankingMember((Group)outBox.getSelectedItem(),outRank());
				RankVsRankDialog.this.gamesRepository.addNoGroupGame(new Game(home,out));
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
	
	public int homeRank() {
		return Integer.parseInt(this.homeRank.getText());
	}

	public int outRank() {
		return Integer.parseInt(this.outRank.getText());
	}

	
	public void prepareCleanScreen() {
		homeBox.removeAllItems();
		outBox.removeAllItems();
		
		for(Group group : this.gamesRepository.getAllGroups()) {
			homeBox.addItem(group);
			outBox.addItem(group);
			//TODO: adding to ...
		}
		
	}
}
