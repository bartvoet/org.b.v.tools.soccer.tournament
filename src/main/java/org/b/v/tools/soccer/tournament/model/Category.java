package org.b.v.tools.soccer.tournament.model;

import org.b.v.tools.soccer.tournament.extra.Entity;

public class Category extends Entity {
	
	private static final long serialVersionUID = 1L;
	private String name;

	public Category(String name) {
		this.name=name;
	}
	
	public String name() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
