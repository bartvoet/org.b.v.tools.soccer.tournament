package org.b.v.tools.soccer.tournament;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.b.v.tools.soccer.tournament.extra.Entity;
import org.b.v.tools.soccer.tournament.model.Category;
import org.b.v.tools.soccer.tournament.model.Game;
import org.b.v.tools.soccer.tournament.model.Group;
import org.b.v.tools.soccer.tournament.model.GroupMember;
import org.b.v.tools.soccer.tournament.model.RankingMember;
import org.b.v.tools.soccer.tournament.model.Team;

public class GroupRepository {
	
	private List<Category> categories = new ArrayList<Category>();
	private AtomicLong groupIds = new AtomicLong();
	private List<Group> groups = new ArrayList<Group>(); 
	private List<Game> nonGroupGames = new ArrayList<Game>();
	
	
	public Group searchGroupByName(String categoryName,String name) {
		
		for(Category category:categories) {
			if(category.name().equals(categoryName)) {
				for(Group group:category.getGroups()) {
					if(name.equals(group.getName())){
						return group;
					}
				}
			}
		}
		return null;
	}
	
	public Group searchGroupById(Long id) {
		for(Group group:groups) {
			if(id.equals(group.getId())){
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
	
	public void addNoGroupGame(Game game) {
		if(!game.containsId()) {
			enrichWithId(game);
		}
		this.nonGroupGames.add(game);
	}

	public Entity enrichWithId(Entity entity) {
		entity.setId(groupIds.incrementAndGet());
		return entity;
	}
	
	public Collection<Game> getAllNonGroupGames() {
		return this.nonGroupGames;
	}

	public void writeGame(PrintWriter writer,Game game,String suffix) {
		writer.println(game.getId() + "," + "GAME" + suffix + "," 
				+ game.getHome().getRepresentation() + "," 
				+ game.getOther().getRepresentation()+ "," 
				+ game.getHomeScore() + "," + game.getOutScore() + "," 
				+ game.getHomePenalties() + "," + game.getOutPenalties() + "," 
				+ game.getField()  + "," 
				+ game.extractHour() + "," + game.extractMinutes() + ","
				+ game.isFinished() + ","
				);
	}
	
	private Long getLong(String[] tokens,int index) {
		return Long.parseLong(tokens[index]);
	}
	
	private int getInt(String[] tokens,int index) {
		return Integer.parseInt(tokens[index]);
	}
	
	private void setEntity(Entity entity,String[] tokens) {
		entity.setId(getLong(tokens,0));
	}

	private void populateGame(Game game,String[] tokens) {
		game
		.withScores(getInt(tokens,4), getInt(tokens,5))
		.withPenalties(getInt(tokens,6), getInt(tokens,7))
		.atField(tokens[8])
		.onTime(getInt(tokens,9),getInt(tokens,10));

		setEntity(game,tokens);
		
		if("true".equals(tokens[11])) {
			game.finishMatch();
		}
	}
	
	public void load(String name) {
		try {
			FileInputStream fileInput = new FileInputStream(new File(name));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput));
			
			String line;
			Category category=null;
			Group group=null;
			
			//todo detect max id, overwrite collections
			
			this.categories=new ArrayList<Category>();
			this.groups=new ArrayList<Group>();
			this.nonGroupGames=new ArrayList<Game>();
			
			long maxId = 0;
			
					
			while((line = reader.readLine()) != null){
				String[] tokens = line.split(",");
				String type = tokens[1];
				String entityName = tokens[2];
				
				maxId=Long.max(maxId, getLong(tokens,0));
				
				if("CATEGORY".equals(type)) {
					category = new Category(entityName);
					setEntity(category,tokens);
					this.categories.add(category);
				}
				
				if("GROUP".equals(type)) {
					group = new Group(entityName);
					setEntity(group,tokens);
					group.setCategory(category);
					this.groups.add(group);
				}
				
				if("TEAM".equals(type)) {
					GroupMember member = new GroupMember(entityName);
					setEntity(member,tokens);
					group.addNewMember(member);
				}

				if("GAME".equals(type)) {
					Game game = new Game(group.getMemberById(getLong(tokens,2)),
										 group.getMemberById(getLong(tokens,3)));
					populateGame(game,tokens);
					group.addNewGame(game);
				}
				
				if("GAMEGLOBAL".equals(type)) {
					Game game = new Game(
							searchHomeTeam(tokens),
							searchOtherTeam(tokens));
					
					populateGame(game,tokens);
					this.nonGroupGames.add(game);
				}
			}
			reader.close();
			this.groupIds.set(maxId);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private static Pattern scorePattern = Pattern.compile("GR\\((\\d+)-(\\d+)\\)");
	
	private Team searchOtherTeam(String[] tokens) {
		Matcher matcher = scorePattern.matcher(tokens[3]);
		if(matcher.matches()) {
			long id = Long.parseLong(matcher.group(1));
			int rank = Integer.parseInt(matcher.group(2));
			return new RankingMember(this.searchGroupById(id), rank);
		}
		return null;
	}

	private Team searchHomeTeam(String[] tokens) {
		Matcher matcher = scorePattern.matcher(tokens[2]);
		if(matcher.matches()) {
			long id = Long.parseLong(matcher.group(1));
			int rank = Integer.parseInt(matcher.group(2));
			return new RankingMember(this.searchGroupById(id), rank);
		}
		return null;
	}

	public void persist(String name) {
		try {
			FileOutputStream fileOutput = new FileOutputStream(new File(name));
			BufferedOutputStream buffer = new BufferedOutputStream(fileOutput);
			PrintWriter writer = new PrintWriter(buffer);
			
			for(Category category : this.categories) {
				writer.println(category.getId() + "," + "CATEGORY" + "," + category.name() + ",");
				for(Group group : category.getGroups()) {
					writer.println(group.getId() + "," + "GROUP" + "," + group.getName() + ",");
					for(GroupMember member : group.getMembers()) {
						writer.println(member.getId() + "," + "TEAM" + "," + member.getTeamName() + ",");
					}
					for(Game game : group.getGames()) {
						writeGame(writer,game,"");
					}
				}
			}
			
			for(Game game : this.nonGroupGames) {
				writeGame(writer,game,"GLOBAL");
			}
			
			writer.flush();
			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	public void persist(String name) {
		try {
			FileOutputStream fileOutput = new FileOutputStream(new File(name));
			BufferedOutputStream buffer = new BufferedOutputStream(fileOutput);
			ObjectOutputStream objects = new ObjectOutputStream(buffer);
			objects.writeObject(categories);
			objects.writeObject(this.groups);
			objects.writeObject(this.groupIds);
			objects.writeObject(this.nonGroupGames);
			objects.flush();
			objects.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeCsv("/home/bart/test.csv");
		
	}

	public void load(String name) {
		try {
			FileInputStream fileOutput = new FileInputStream(new File(name));
			BufferedInputStream buffer = new BufferedInputStream(fileOutput);
			ObjectInputStream objects = new ObjectInputStream(buffer);
			this.categories = (List<Category>) objects.readObject();
			this.groups = (List<Group>) objects.readObject();
			this.groupIds = (AtomicLong) objects.readObject();
			this.nonGroupGames = (List<Game>) objects.readObject();
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
	}*/

	public void removeNoGroupGame(Game entity) {
		this.nonGroupGames.remove(entity);
		
	}

	public void updateNoGroupGame(Game entity) {
		
	}

	public Game searchGameById(Long id) {
		for(Game game:this.nonGroupGames) {
			if(game.getId().equals(id)) {
				return game;
			}
		}
		return null;
	}

	public Collection<Category> getAllCategories() {
		return this.categories;
	}

	public void addCategory(String text) {
		for(Category category:categories) {
			if(category.name().equals(text)){
				return;
			}
		}
		Category category = new Category(text);
		enrichWithId(category);
		this.categories.add(category);
	}


}
