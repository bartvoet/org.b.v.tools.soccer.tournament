package org.b.v.tools.soccer.tournament.extra;

import java.io.Serializable;

public abstract class Entity implements Comparable<Entity>,Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public int compareTo(Entity o) {
		return new Long(id).compareTo(new Long(o.id));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj) {
			return true;
		}
		
		if(obj instanceof Entity) {
			Entity otherGroup = (Entity)obj;
			return this.id==otherGroup.id;
		}
		return false;
	}
	
	public boolean containsId() {
		return !(id==null);
	}
	
	@Override
	public int hashCode() {
		if(id!=null) {
			return id.hashCode();
		}
		
		return super.hashCode();
	}
}
