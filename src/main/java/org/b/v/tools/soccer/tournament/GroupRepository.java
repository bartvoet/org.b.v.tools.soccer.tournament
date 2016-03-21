package org.b.v.tools.soccer.tournament;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.b.v.tools.soccer.tournament.model.Group;
import org.b.v.tools.soccer.tournament.model.GroupMember;

public class GroupRepository {
	
	private AtomicLong groupIds = new AtomicLong();
	private List<Group> groups = new ArrayList<Group>(); 
	private List<Group> groupMembers = new ArrayList<Group>(); 
	
	public Group searchGroupByName(String name) {
		for(Group group:groups) {
			if(name.equals(group.getName())){
				return group;
			}
		}
		return null;
	}

	public Collection<Group> getAllGroups() {
		return this.groups;
	}

	public void createOrSaveGroup(Group group) {
		if(!group.containsId()) {
			group.setId(new Long(this.groupIds.incrementAndGet()));
			this.groups.add(group);
		}
	}

	public void enrichWithId(GroupMember entity) {
		entity.setId(groupIds.getAndIncrement());
	}

}
