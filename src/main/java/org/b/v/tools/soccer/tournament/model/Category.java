package org.b.v.tools.soccer.tournament.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.b.v.tools.soccer.tournament.extra.Entity;

public class Category extends Entity {
	
	private static final long serialVersionUID = 1L;
	private String name;
	private List<Group> groups = new ArrayList<Group>();

	public Category(String name) {
		this.name=name;
	}
	
	public String name() {
		return this.name;
	}
	
	public void addGroup(Group group) {
		this.groups.add(group);
	}
	
	public void removeGroup(Group group) {
		this.groups.remove(group);
	}

	@Override
	public String toString() {
		return this.name;
	}

	public Collection<Group> getGroups() {
		return this.groups;
	}
	
}
