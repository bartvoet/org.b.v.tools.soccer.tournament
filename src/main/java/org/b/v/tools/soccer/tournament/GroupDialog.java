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

public class GroupDialog extends JDialog {

	private static final long serialVersionUID = -7208676901332235553L;
	
	private JTextField name = new JTextField();
	private JComboBox<String> combo = new JComboBox<String>();

	private JButton addButton;
	private JButton deleteButton;
	private JButton saveAndExitButton;
	private JButton cancelButton;
	
	private JTable table;

	private GroupRepository gamesRepository;

	private UpdateEvent event;
	
	private Group groupCurrentlyProcessing=new Group("");
	
	private EntityTableModel<GroupMember> data;
	
	
	public GroupDialog(GroupRepository gamesRepository,UpdateEvent event) {
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

	private EntityFilter<GroupMember> filter =
		new EntityFilter<GroupMember>() {
			public List<GroupMember> getEntities() {
				return groupCurrentlyProcessing.getMembers();
			}
	
			public void saveNewEntity(GroupMember entity) {
				if(!entity.containsId()) {
					gamesRepository.enrichWithId(entity);
				}
				groupCurrentlyProcessing.addNewMember(entity);				
			}
	
			public void removeExistingEntity(GroupMember entity) {
				groupCurrentlyProcessing.removeMember(entity);		
			}
		};
	
	private EntityMapper<GroupMember> groupMemberMapper =
		new EntityMapper<GroupMember>() {
			final String[] columnNames = {"Teamnaam",""};
			@SuppressWarnings("rawtypes")
			final Class[] types = new Class [] {java.lang.String.class, java.lang.Boolean.class};
			
			public String[] getColumnNames() {
				return columnNames;
			}

			public Class<?> getType(int columnIndex) {
				return types [columnIndex];
			}

			public Object[] map(GroupMember entity) {
				return new Object[]{entity.getTeamName(),null};
			}
			
			public GroupMember map(Object[] data) {
				return new GroupMember((String)data[0]);
			}

			public Comparable<?> getId(GroupMember entity) {
				return new Long(entity.getId());
			}
			
			public Object[] getDefaultData() {
				return new Object[]{"",null};
			}
			
			public boolean isMarkedToBeDeleted(Object[] data) {
				Object bool = data[1];
				if(bool!=null) {
					return (Boolean)data[1];
				}
				return false;
			}
		};	
		
	
	private void initializeTable() {
 		
		data = new EntityTableModel<GroupMember>(groupMemberMapper);
		
        table = new JTable();
        data.configureTable(table);
        data.load(filter);
        
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
				data.addEmptyEntityFromTable();
				
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				data.removeRows();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GroupDialog.this.setVisible(false);
			}
		});
		
		saveAndExitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupCurrentlyProcessing.changeName(name.getText());
				gamesRepository.createOrSaveGroup(groupCurrentlyProcessing);
				data.dump(filter);
				
				event.update();
				GroupDialog.this.setVisible(false);
			}
		});
		
		combo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String name = (String) combo.getSelectedItem();
				if(name!=null & !"-".equals(name)) {
					GroupDialog.this.name.setText(name);
					groupCurrentlyProcessing = gamesRepository.searchGroupByName(name);
			    	data.load(filter);
				}
			}
		});
	}

	public void prepareCleanScreen() {
		name.setText("");
		groupCurrentlyProcessing=new Group("");
		
		combo.removeAllItems();
		combo.addItem("-");
		for(Group group : gamesRepository.getAllGroups()) {
			combo.addItem(group.getName());
		}
		
		data.clean();
	}
}
