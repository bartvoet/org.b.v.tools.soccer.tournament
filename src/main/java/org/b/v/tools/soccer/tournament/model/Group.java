package org.b.v.tools.soccer.tournament.model;

public class Group {

	private String name;
	private long id;

	public Group(long id,String name) {
		this.id=id;
		this.name=name;
	}

	public String getName() {
		return name;
	}
	
	public long getId() {
		return id;
	}
}
