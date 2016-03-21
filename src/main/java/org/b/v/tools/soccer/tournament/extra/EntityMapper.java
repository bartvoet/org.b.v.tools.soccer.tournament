package org.b.v.tools.soccer.tournament.extra;


public interface EntityMapper<T> {

	String[] getColumnNames();

	Class<?> getType(int columnIndex);

	Object[] map(T entity);
	
	T map(Object[] data);

	Comparable<?> getId(T entity);

	Object[] getDefaultData();
	
	public boolean isMarkedToBeDeleted(Object[] data);

}
