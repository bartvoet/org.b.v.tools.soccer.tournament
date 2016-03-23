package org.b.v.tools.soccer.tournament.extra;

import java.util.Collection;

public interface EntityFilter<T> {

	Collection<T> getEntities();
	void saveNewEntity(T entity);
	void removeExistingEntity(T entity);
	void updateEntity(T entity);

}
