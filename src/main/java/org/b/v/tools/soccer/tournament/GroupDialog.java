package org.b.v.tools.soccer.tournament;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GroupDialog extends JDialog {

	private static final long serialVersionUID = -7208676901332235553L;
	
	private DefaultTableModel model;
	private JTextField name = new JTextField();

	private JButton addButton;
	private JButton deleteButton;
	private JButton saveAndExitButton;
	private JButton cancelButton;
	
	private JTable table;
	
	
	public GroupDialog() {
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
        add(infoPanel,BorderLayout.NORTH);
	}


	private void initializeTable() {
		String[] columnNames = {"Teamnaam",""};
        Object[][] data = {{"Baal",null}};

        model=new DefaultTableModel(data, columnNames){
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
			}
		});
	}
}
