package org.b.v.tools.soccer.tournament;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.b.v.tools.soccer.tournament.extra.Entity;
import org.b.v.tools.soccer.tournament.model.Group;

public class GroupRepository {
	
	private AtomicLong groupIds = new AtomicLong();
	private List<Group> groups = new ArrayList<Group>(); 
	
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

	public void enrichWithId(Entity entity) {
		entity.setId(groupIds.getAndIncrement());
	}

	public void persist(String name) {
		try {
			FileOutputStream fileOutput = new FileOutputStream(new File(name));
			BufferedOutputStream buffer = new BufferedOutputStream(fileOutput);
			ObjectOutputStream objects = new ObjectOutputStream(buffer);
			objects.writeObject(this.groups);
			objects.writeObject(this.groupIds);
			objects.flush();
			objects.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void load(String name) {
		try {
			FileInputStream fileOutput = new FileInputStream(new File(name));
			BufferedInputStream buffer = new BufferedInputStream(fileOutput);
			ObjectInputStream objects = new ObjectInputStream(buffer);
			this.groups = (List<Group>) objects.readObject();
			this.groupIds = (AtomicLong) objects.readObject();
			objects.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
