package org.b.v.tools.soccer.tournament.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

//extends or composite???
//keep track of rows
//when you save verify the rows that you don't track

public class EntityTableModel<T>  {
	
	private DefaultTableModel model;
	private EntityMapper<T> mapper;
	
	private List<EntityTracker> loadedEntities=new ArrayList<EntityTracker>();
	
	private class EntityTracker {
		private int row;
		private Comparable<T> id;
		private boolean isNew=false;
		private boolean toBeDeleted=false;
		
		public EntityTracker(int row, Comparable<T> id, boolean isNew) {
			super();
			this.row = row;
			this.id = id;
			this.isNew = isNew;
		}
		
		public EntityTracker(int row) {
			// TODO Auto-generated constructor stub
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
	
	public EntityTableModel(final EntityMapper<T> mapper,DefaultTableModel model) {
		this.mapper = mapper;
		this.model=model;
		this.model.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				switch(e.getType()) {
					case(TableModelEvent.INSERT):
						loadedEntities.add(newEntityTracker(e.getFirstRow()));
						break;
					case(TableModelEvent.DELETE):
						searchActive(e.getFirstRow()).markAsDeleted();
						break;
				}
			}
		});
	}
	
	public Class<?> getColumnClass(int columnIndex) {
		return mapper.getType(columnIndex);
	}
	
	public void load(EntityFilter<T> repository ) {
		List<T> storedEntities = repository.getEntities();
		//loadedEntities = repository.getEntities();
		int rowCounter = 0;
		for(T entity:storedEntities) {
			Object row[] = mapper.map(entity);
			model.addRow(row);
			loadedEntities.add(existingEntityTracker(rowCounter++,entity));
		}
	}
	
	
}
