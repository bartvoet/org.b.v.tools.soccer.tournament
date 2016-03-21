package org.b.v.tools.soccer.tournament;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.b.v.tools.soccer.tournament.extra.EntityFilter;
import org.b.v.tools.soccer.tournament.extra.EntityMapper;
import org.b.v.tools.soccer.tournament.extra.EntityTableModel;
import org.b.v.tools.soccer.tournament.model.Group;
import org.b.v.tools.soccer.tournament.model.GroupMember;

public class GameCreationDialog extends JDialog {

	private static final long serialVersionUID = -7208676901332235553L;
	
	private JComboBox<String> combo = new JComboBox<String>();

	private JButton addButton;
	private JButton cancelButton;

	private GroupRepository gamesRepository;

	private UpdateEvent event;

	private JTable table;
	
	
	
	public GameCreationDialog(GroupRepository gamesRepository,UpdateEvent event) {
        this.gamesRepository = gamesRepository;
        this.event=event;
		setLayout(new BorderLayout());
        initializeTable();
        initializeButtonPanel();
        initializeButtonActions();
        setSize(new Dimension(600,300));
	}


		
	
	private void initializeTable() {
 		
		//data = new EntityTableModel<GroupMember>(groupMemberMapper);
		
        table = new JTable();
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane,BorderLayout.CENTER);

        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
	}


	private void initializeButtonPanel() {
		JPanel buttonPanel = new JPanel();
        
        addButton = new JButton("Toevoegen");
        cancelButton = new JButton("Cancel");
        
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel,BorderLayout.SOUTH);
	}

	private void initializeButtonActions() {
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
				
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameCreationDialog.this.setVisible(false);
			}
		});
		
		
		combo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {

			}
		});
	}

	public void prepareCleanScreen() {
		
		combo.removeAllItems();
		combo.addItem("-");
		for(Group group : gamesRepository.getAllGroups()) {
			combo.addItem(group.getName());
		}
		
	}
}
