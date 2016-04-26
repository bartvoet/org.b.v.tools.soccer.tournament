package org.b.v.tools.soccer.tournament;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.b.v.tools.soccer.tournament.model.Category;

public class CategoryDialog extends JDialog {

	private static final long serialVersionUID = -7208676901332235553L;
	
	private JTextField name = new JTextField();
	private JTextArea categories = new JTextArea();

	private GroupRepository gamesRepository;
	private UpdateEvent event;
	
	private JButton addButton;
	private JButton deleteButton;
	private JButton saveAndExitButton;
	private JButton cancelButton;
	
	public CategoryDialog(GroupRepository gamesRepository,UpdateEvent event) {
        this.gamesRepository = gamesRepository;
        this.event=event;
		setLayout(new BorderLayout());
        initializeGeneralInfo();
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
        add(categories,BorderLayout.CENTER);
	}



	private void initializeButtonPanel() {
		JPanel buttonPanel = new JPanel();
        
        addButton = new JButton("Toevoegen");
        deleteButton = new JButton("Verwijderen");
        cancelButton = new JButton("Cancel");
        saveAndExitButton = new JButton("OK");
        
        buttonPanel.add(addButton);
        //buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveAndExitButton);
        
        add(buttonPanel,BorderLayout.SOUTH);
	}

	private void initializeButtonActions() {
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CategoryDialog.this.gamesRepository.addCategory(CategoryDialog.this.name.getText());
				prepareCleanScreen();
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CategoryDialog.this.setVisible(false);
			}
		});
		
		saveAndExitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CategoryDialog.this.event.registerUpdate();
				CategoryDialog.this.setVisible(false);
			}
		});
		

	}

	public void prepareCleanScreen() {
		name.setText("");
		
		StringBuilder builder = new StringBuilder();
		
		for(Category category : gamesRepository.getAllCategories()) {
			  builder.append(category.name() + "\n");
		
		}
		
		categories.setText(builder.toString());
		
	}
}
