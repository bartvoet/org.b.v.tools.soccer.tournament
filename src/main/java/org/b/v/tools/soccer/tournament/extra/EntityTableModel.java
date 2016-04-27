package org.b.v.tools.soccer.tournament.extra;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

//extends or composite???
//keep track of rows
//when you save verify the rows that you don't track

public class EntityTableModel<T extends Entity>  {
	
	private DefaultTableModel model;
	private EntityMapper<T> mapper;
	
	private List<EntityTracker> loadedEntities=new ArrayList<EntityTracker>();
	private EntityFilter<T> filter;
	private List<T> entitiesToBeRemoved = new ArrayList<T>();
	
	
	private class EntityTracker {
		private int row;
		private Comparable<?> id;
		private boolean isNew=false;
		private boolean toBeDeleted=false;
		
		public EntityTracker(int row, Comparable<?> id, boolean isNew) {
			super();
			this.row = row;
			this.id = id;
			this.isNew = isNew;
		}
		
		public EntityTracker(int row) {
			this.row=row;
		}

		public void markAsDeleted() {
			this.toBeDeleted=true;
		}

		public boolean isActive() {
			// TODO Auto-generated method stub
			return !this.toBeDeleted;
		}
	}
	
	public EntityTracker existingEntityTracker(int row,T entity) {
		return new EntityTracker(row,mapper.getId(entity),true);
	}
	
	public EntityTracker newEntityTracker(int row) {
		return new EntityTracker(row);
	}
	
	
	private EntityTracker searchActive(int pos) {
		for(EntityTracker tracker : loadedEntities) {
			if(tracker.isActive() && tracker.row==pos) {
				return tracker;
			}
		}
		return null;
	}
	
	private String[] enrichedColumnNames() {
		String[] columnNames = mapper.getColumnNames();
		String[] enriched = new String[columnNames.length + 1];
		
		for(int i=0;i<columnNames.length;i++) {
			enriched[i]=columnNames[i];
		}
		
		return enriched;
	}
	
	public EntityTableModel(final EntityMapper<T> mapper) {
		this.mapper = mapper;
		
		this.model=new DefaultTableModel(enrichedColumnNames(),0){
                public Class getColumnClass(int columnIndex) {
                    if(mapper.getColumnNames().length == columnIndex) {
                    	return Long.class;
                    }
                	
                	return mapper.getType(columnIndex);
                }
            };
        
        this.filter=new EntityFilter<T>() {

			public List<T> getEntities() {
				return new ArrayList<T>();
			}

			public void saveNewEntity(T entity) {
				
			}

			public void removeExistingEntity(T e) {
				
			}

			public void updateEntity(T t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public T searchEntity(Long id) {
				// TODO Auto-generated method stub
				return null;
			}
        };
	}

	public void changeEntityFilter(EntityFilter<T> filter) {
		this.filter=filter;
	}
	
	public Class<?> getColumnClass(int columnIndex) {
		return mapper.getType(columnIndex);
	}
	
	public void load(EntityFilter<T> repository ) {
		this.filter=repository;
		
    	int rowCount = model.getRowCount();
    	for (int i = rowCount - 1; i >= 0; i--) {
    		model.removeRow(i);
    	}
		
		Collection<T> storedEntities = this.filter.getEntities();
		for(T entity:storedEntities) {
			Object row[] = mapper.map(entity);
			Object actual[] = new Object[row.length + 1];
			for(int i=0;i<row.length;i++) {
				actual[i] = row[i];
			}
			actual[row.length]=entity.getId();
			model.addRow(actual);
		}
	}
	
	public void add(List<T> newEntities, EntityFilter<T> repository ) {
		this.filter=repository;

		for(T entity:newEntities) {
			Object row[] = mapper.map(entity);
			Object actual[] = new Object[row.length + 1];
			for(int i=0;i<row.length;i++) {
				actual[i] = row[i];
			}
			actual[row.length]=entity.getId();
			model.addRow(actual);
		}
	}
	
	public void dump(EntityFilter<T> repository ) {
		//this.filter=repository;
		
		for(int row = 0;row < model.getRowCount();row++) {
			Object[] data = getRowData(row);
			if(data[data.length-1]==null) {
				filter.saveNewEntity(mapper.map(data));
			} else {
				Long id = (Long)data[data.length-1];
				T t = filter.searchEntity(id);
				mapper.map(t, data);
//				T t = mapper.map(data);
//				t.setId((Long)data[data.length-1]);
				filter.updateEntity(t);
			}
		}
		
		for(T toBeDeleted : entitiesToBeRemoved) {
			filter.removeExistingEntity(toBeDeleted);
		}
	}

	public DefaultTableModel getTable() {
		return this.model;
	}

//	public void removeEntityFromTable(int i) {
//		model.removeRow(i);
//	}

	public void addEmptyEntityFromTable() {
		model.addRow(mapper.getDefaultData());
	}
	
	Object[] getRowData(int row) {
		Object[] data=new Object[model.getColumnCount()];
		for(int i=0;i< model.getColumnCount();i++) {
			data[i]=model.getValueAt(row, i);
		}
		return data;
	}

	public void removeRows() {
		List<Integer> toDelete = new ArrayList<Integer>();
		for(int row = 0;row < model.getRowCount();row++) {
			Boolean b = mapper.isMarkedToBeDeleted(getRowData(row));
			if(b!=null && b.booleanValue()) {toDelete.add(row);}
		}
		for(int i : toDelete) {
			Object[] data = getRowData(i);
			if(data[data.length-1]!=null) {
				T entity = mapper.map(data);
				entity.setId((Long)data[data.length-1]);
				entitiesToBeRemoved.add(entity);
			}
			
			model.removeRow(i);
		}
		
//		for(int row = 0;row < model.getRowCount();row++) {
//			String b = (String)model.getValueAt(row, 0);
//			System.out.println("Remaining at " + row + " " + b);
//		}
		
	}
	
	public List<T> getMarkedEntities() {
		List<Integer> markedRows = new ArrayList<Integer>();
		List<T> selectedEntities = new ArrayList<T>();
		for(int row = 0;row < model.getRowCount();row++) {
			Boolean b = mapper.isMarkedToBeDeleted(getRowData(row));
			if(b!=null && b.booleanValue()) {markedRows.add(row);}
		}
		for(int i : markedRows) {
			Object[] data = getRowData(i);
			if(data[data.length-1]!=null) {
				T entity = filter.searchEntity((Long)data[data.length-1]);
				mapper.map(entity,data);
				selectedEntities.add(entity);
			} else {
				T entity = mapper.map(data);
				selectedEntities.add(entity);
			}
		}
		
		return selectedEntities;
		
	}

	public void clean() {
		int rowCount = model.getRowCount();
    	for (int i = rowCount - 1; i >= 0; i--) {
    		model.removeRow(i);
    	}
		
	}

	public void configureTable(JTable table) {
		table.setModel(model);
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn( tcm.getColumn(tcm.getColumnCount()-1));
	}
	
	
}
