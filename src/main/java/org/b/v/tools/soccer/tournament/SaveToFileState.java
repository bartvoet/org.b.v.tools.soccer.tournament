package org.b.v.tools.soccer.tournament;

import java.awt.Component;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SaveToFileState {
	
	public static enum FileState {
		NEW,NEW_NOT_SAVED,LOADED,LOADED_NOT_SAVED
	}
	
	private FileState state = FileState.NEW;
	
	private Component parent;
	private GroupRepository gamesRepository;
	private String currentFile;
	
    private static FileNameExtensionFilter filter = 
    		new FileNameExtensionFilter("Tournament file (csv)", "trm","csv");
	
	public SaveToFileState(Component parent,GroupRepository gamesRepository) {
		this.gamesRepository=gamesRepository;
		this.parent=parent;
	}
	
	public void markChange() {
		switch( this.state ) {
			case NEW: state = FileState.NEW_NOT_SAVED;break;
			case LOADED: state = FileState.LOADED_NOT_SAVED;break;
			default:break;
		}
	}
	
	public void saveAs() {
	 	JFileChooser chooser = new JFileChooser();
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showSaveDialog(parent);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	gamesRepository.persist(chooser.getSelectedFile().getAbsolutePath());
	    	currentFile = chooser.getSelectedFile().getAbsolutePath();
	    	state = FileState.LOADED;
	    	return;
	    }
	    state = FileState.NEW;
	}
	
	public void save() {
	 	if(currentFile!=null) {
	 		gamesRepository.persist(currentFile);
	 		state = FileState.LOADED;
	 	} else {
			JFileChooser chooser = new JFileChooser();
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showSaveDialog(parent);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	gamesRepository.persist(chooser.getSelectedFile().getAbsolutePath());
		    	currentFile = chooser.getSelectedFile().getAbsolutePath();
		    	state = FileState.LOADED;
		    	return;
		    }
	 	}
	}

	public void open() {
		JFileChooser chooser = new JFileChooser();
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(parent);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	gamesRepository.load(chooser.getSelectedFile().getAbsolutePath());
	    	currentFile = chooser.getSelectedFile().getAbsolutePath();
	    	state = FileState.LOADED;
	    };
	}

	public FileState getState() {
		return this.state;
	}

	public String getPath() {
		return this.currentFile;
	}

	public void newTournament() {
		this.currentFile = null;
		this.state = FileState.NEW;
	}
	
}
