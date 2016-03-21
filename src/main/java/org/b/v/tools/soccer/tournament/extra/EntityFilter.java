package org.b.v.tools.soccer.tournament.extra;

import java.util.List;

public interface EntityFilter<T> {

	List<T> getEntities();
	void saveNewEntity(T entity);
	void removeExistingEntity(T entity);

}
