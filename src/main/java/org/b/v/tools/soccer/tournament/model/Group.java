package org.b.v.tools.soccer.tournament.model;

import java.util.ArrayList;
import java.util.List;

import org.b.v.tools.soccer.tournament.extra.Entity;

public class Group extends Entity  {

	private String name;
	private long id;
	private List<GroupMember> members = new ArrayList<GroupMember>();

	public Group(String name) {
		this.name = name;
	}
	
	public Group(long id,String name) {
		this.id=id;
		this.name=name;
	}

	public String getName() {
		return name;
	}
	
	public void changeName(String name) {
		this.name=name;
	}
	
	public void addNewMember(String name) {
		GroupMember member = new GroupMember(name);
		this.members.add(member);
	}

	public int compareTo(Group o) {
		return new Long(id).compareTo(new Long(o.id));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Group) {
			Group otherGroup = (Group)obj;
			return this.id==otherGroup.id;
		}
		return false;
	}

	public List<GroupMember> getMembers() {
		return this.members;
	}

	public void addNewMember(GroupMember entity) {
		this.members.add(entity);
		
	}

	public void removeMember(GroupMember entity) {
		this.members.remove(entity);
	}

	public void updateMember(GroupMember entity) {
		
	}


	
	
}
