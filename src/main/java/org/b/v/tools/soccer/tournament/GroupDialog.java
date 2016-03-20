package org.b.v.tools.soccer.tournament;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.b.v.tools.soccer.tournament.model.GroupMember;

public class GroupDialog extends JDialog {

	private static final long serialVersionUID = -7208676901332235553L;
	
	private DefaultTableModel model;
	private JTextField name = new JTextField();
	private JComboBox<String> combo = new JComboBox<String>();

	private JButton addButton;
	private JButton deleteButton;
	private JButton saveAndExitButton;
	private JButton cancelButton;
	
	private List<Integer> ids;
	
	private JTable table;

	private GamesRepository gamesRepository;

	private UpdateEvent event;
	
	public GroupDialog(GamesRepository gamesRepository,UpdateEvent event) {
        this.gamesRepository = gamesRepository;
        this.event=event;
		setLayout(new BorderLayout());
        initializeGeneralInfo();
        initializeTable();
        initializeButtonPanel();
        initializeButtonActions();
        setSize(new Dimension(600,300));
	}


	private void initializeGeneralInfo() {
		JPanel infoPanel = new JPanel();
        
        name = new JTextField("",10);
        name.setSize(new Dimension(10,10));
        
        infoPanel.add(new JLabel("Naam groep"));
        infoPanel.add(name);
        infoPanel.add(combo);
        add(infoPanel,BorderLayout.NORTH);
	}


	private void initializeTable() {
		String[] columnNames = {"Teamnaam",""};
        //Object[][] data = {{"Baal",null}};

        model=new DefaultTableModel(columnNames,0){
            	Class[] types = new Class [] {
                    java.lang.String.class, java.lang.Boolean.class
                };
     
                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            };
            
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane,BorderLayout.CENTER);

        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
	}


	private void initializeButtonPanel() {
		JPanel buttonPanel = new JPanel();
        
        addButton = new JButton("Toevoegen");
        deleteButton = new JButton("Verwijderen");
        cancelButton = new JButton("Sluiten");
        saveAndExitButton = new JButton("Save en sluiten");
        
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveAndExitButton);
        
        add(buttonPanel,BorderLayout.SOUTH);
	}

	private void initializeButtonActions() {
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.addRow(new Object[]{"",null});
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			List<Integer> toDelete = new ArrayList<Integer>();
			public void actionPerformed(ActionEvent e) {
				for(int row = 0;row < model.getRowCount();row++) {
					Boolean b = (Boolean)model.getValueAt(row, 1);
					if(b!=null && b.booleanValue()) {toDelete.add(row);}
				}
				for(int i : toDelete) {
					model.removeRow(i);
				}
				
				for(int row = 0;row < model.getRowCount();row++) {
					String b = (String)model.getValueAt(row, 0);
					System.out.println("Remaining at " + row + " " + b);
				}
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				GroupDialog.this.setVisible(false);
			}
		});
		
		saveAndExitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String groupName = name.getText();
				gamesRepository.createOrUpdateGroup(name.getText());
				for(int row = 0;row < model.getRowCount();row++) {
					String teamName = (String)model.getValueAt(row, 0);
					gamesRepository.addTeamToGroup(groupName,teamName);
				}
				event.update();
				GroupDialog.this.setVisible(false);
			}
		});
		
		
		combo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String name = (String) combo.getSelectedItem();
				if(name!=null & !"-".equals(name)) {
					System.out.println(name);
					//load group here
					
					GroupDialog.this.name.setText(name);
					
			    	int rowCount = model.getRowCount();
			    	for (int i = rowCount - 1; i >= 0; i--) {
			    		model.removeRow(i);
			    	}

			    	for(GroupMember member : gamesRepository.getTeamsForAGroup(name)) {
			    		model.addRow(new Object[]{member.getTeamName(),null});
			    	}
			    	
				}
			}
		});
	}


	public void prepareCleanScreen() {
		name.setText("");
		
		combo.removeAllItems();
		
		combo.addItem("-");
		for(String groupName : gamesRepository.getAllGroups()) {
			combo.addItem(groupName);
		}
		
    	int rowCount = model.getRowCount();
    	for (int i = rowCount - 1; i >= 0; i--) {
    		model.removeRow(i);
    	}
	}
}
