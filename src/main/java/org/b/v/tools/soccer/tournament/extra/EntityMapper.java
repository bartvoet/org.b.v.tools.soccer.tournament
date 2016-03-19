package org.b.v.tools.soccer.tournament.extra;

public interface EntityMapper<T> {

	String[] getColumnNames();

	Class<?> getType(int columnIndex);

	Object[] map(T entity);

	Comparable<T> getId(T entity);

}
