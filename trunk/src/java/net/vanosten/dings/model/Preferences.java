/*
 * Preferences.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * Copyright (C) 2002, 2003 Rick Gruber (rick@vanosten.net)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.vanosten.dings.model;

import java.util.Properties;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.vanosten.dings.uiif.IPreferencesEditView;
import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.event.AppEvent;

import java.util.logging.Logger;
import java.util.logging.Level;

public class Preferences extends AModel{

	/** The name of the application property file */
	public final static String PROP_FILENAME = ".dings.properties";

	/** The comment in the properties file */
	public final static String PROP_COMMENT = "Properties for the " + ADings.APP_NAME + " application.";
	
	/** The history of opened vocabulary files as a delimitted list of paths*/
	public final static String PROP_FILE_HISTORY = "file_history";
	
	/** Whether or not the system specifc L&F is used. Default is TRUE */
	public final static String PROP_SYSTEM_LAF = "system_laf";
	
	/** The delimitter of the file history */
	private final static String FILE_HISTORY_DELIMITTER = "#";
	
	/** The maximum number of file names in the file history */
	private final static int MAX_NUMBER_FILE_HISTORY = 5;

	/** Whether logging is enabled */
	public final static String PROP_LOGGING_ENABLED = "logging_enabled";

	/** Whether logging is to file or console (default) */
	public final static String PROP_LOG_TO_FILE = "log_to_file";
	
	/** Whether the selection of an entry should be checked instantly when editing an entry */
	public final static String PROP_SEL_UPD_INST_EDITING = "sel_upd_inst_editing";
	
	/** Whether the selection of an entry should be checked instantly when learning an entry */
	public final static String PROP_SEL_UPD_INST_LEARNING = "sel_upd_inst_learning";
	
	/** The width of the application's main window */
	public final static String PROP_APP_WINDOW_WIDTH = "app_window_width";

	/** The height of the application's main window */
	public final static String PROP_APP_WINDOW_HEIGHT = "app_window_height";
	
	/** The x location of the application's main window */
	public final static String PROP_APP_LOCATION_X = "app_location_x";

	/** The y location of the application's main window */
	public final static String PROP_APP_LOCATION_Y = "app_location_y";
	
	/** The width of the preference dialog */
	public final static String PROP_PREF_DIALOG_WIDTH = "pref_dialog_width";

	/** The height of the preference dialog */
	public final static String PROP_PREF_DIALOG_HEIGHT = "pref_dialog_height";
	
	/** Whether learning statistics should be saved on quit */
	public final static String PROP_STATS_QUIT = "stats_quit";

	public final static String FILE_ENCODING = "file_encoding";
	public final static String LEARN_HINT_COVER_PERCENT = "learn_hint_cover_percent";
	public final static String LEARN_HINT_FLASH_TIME = "learn_hint_flash_time";
	public final static String LEARN_HINT_SHUFFLE_WORD = "learn_hint_shuffle_word";

	/** The edit view */
	private IPreferencesEditView editView;

	/** All preferences and properties are stored in a Properties object */
	private Properties props;

	public Preferences() {
		logger = Logger.getLogger("net.vanosten.dings.model.Preferences");
		readProperties();
	} //public Preferences

	/**
	 * Lets you set the edit view
	 *
	 * @param ICategoryEditView aView
	 */
	public void setEditView(IPreferencesEditView aView) {
		editView = aView;
	}	//END public void setEditView(IPreferencesEditView)

	//implements AItemModel
	protected void releaseViews() {
		editView = null;
	} //protected void releaseViews()

	//Implements AModel
	protected void updateGUI() {
		try {
			//file encoding
			editView.setFileEncoding(props.getProperty(FILE_ENCODING));
			//look and feel
			editView.setSystemLookAndFeel(Boolean.valueOf(props.getProperty(Preferences.PROP_SYSTEM_LAF)).booleanValue());
			//learn hints
			editView.setLearnHintCoverPercent(Integer.parseInt(props.getProperty(LEARN_HINT_COVER_PERCENT)));
			editView.setLearnHintFlashTime(Integer.parseInt(props.getProperty(LEARN_HINT_FLASH_TIME)));
			editView.setLearnHintShuffleByWord(props.getProperty(LEARN_HINT_SHUFFLE_WORD));
			//logging
			editView.setLoggingEnabled(props.getProperty(PROP_LOGGING_ENABLED));
			editView.setLoggingToFile(props.getProperty(PROP_LOG_TO_FILE));
			//selection updates
			editView.setSelUpdInst(Boolean.valueOf(props.getProperty(Preferences.PROP_SEL_UPD_INST_EDITING)).booleanValue()
										, Boolean.valueOf(props.getProperty(Preferences.PROP_SEL_UPD_INST_LEARNING)).booleanValue());
			//stats on quit
			editView.setStatsOnQuit(Boolean.valueOf(props.getProperty(Preferences.PROP_STATS_QUIT)).booleanValue());
		}
		catch (NumberFormatException e) {
			//TODO: log this
		}
	 }	//END protected void updateGUI()

	//Implements AModel
	protected void updateModel() {
		//file encoding
		props.setProperty(FILE_ENCODING, editView.getFileEncoding());
		//look and feel
		props.setProperty(PROP_SYSTEM_LAF, String.valueOf(editView.isSystemLookAndFeel()));
		//learn hints
		props.setProperty(LEARN_HINT_COVER_PERCENT, Integer.toString(editView.getLearnHintCoverPercent()));
		props.setProperty(LEARN_HINT_FLASH_TIME, Integer.toString(editView.getLearnHintFlashTime()));
		props.setProperty(LEARN_HINT_SHUFFLE_WORD, editView.getLearnHintShuffleByWord());
		//logging
		String oldEnabled = props.getProperty(PROP_LOGGING_ENABLED);
		String newEnabled = editView.getLoggingEnabled();
		String oldToFile = props.getProperty(PROP_LOG_TO_FILE);
		String newToFile = editView.getLoggingToFile();
		if (false == oldEnabled.equals(newEnabled) || false == oldToFile.equals(newToFile)) {
			props.setProperty(PROP_LOGGING_ENABLED, newEnabled); //must come before event
			props.setProperty(PROP_LOG_TO_FILE, newToFile); //must come before event
			AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
			ape.setMessage(MessageConstants.S_CHANGE_LOGGING);
			parentController.handleAppEvent(ape);
		}
		//resetting score
		props.setProperty(PROP_SEL_UPD_INST_EDITING, String.valueOf(editView.isSelUpdInstEditing()));
		props.setProperty(PROP_SEL_UPD_INST_LEARNING, String.valueOf(editView.isSelUpdInstLearning()));
		//stats on quit
		props.setProperty(PROP_STATS_QUIT, String.valueOf(editView.isStatsOnQuit()));
	} //END protected void updateModel()

	//Overrides AModel
	public void handleAppEvent(AppEvent evt) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "handleAppEvent", evt.getMessage());
		}
		if (evt.getMessage().equals(MessageConstants.D_PREFERENCES_EDIT_APPLY)) {
			updateModel();
		}
		else if (evt.getMessage().equals(MessageConstants.D_PREFERENCES_EDIT_REVERT)) {
			updateGUI();
		}
		else if (evt.getMessage().equals(MessageConstants.S_SAVE_DIALOG_SIZE)) {
			saveDialogSize();
		}
		else parentController.handleAppEvent(evt);
	}	//END public void handleAppEvent(AppEvent)

	/**
	 * Saves the size information of the dialog showing the preferences
	 */
	private void saveDialogSize() {
		int dialogSize[] = editView.getDialogSize();
		props.setProperty(PROP_PREF_DIALOG_WIDTH, Integer.toString(dialogSize[0]));
		props.setProperty(PROP_PREF_DIALOG_HEIGHT, Integer.toString(dialogSize[1]));
	 } //END private void saveDialogSize()

	/**
	 * Mimics the behavior of java.util.Property and passes the request directly to
	 * the Properties object in the class
	 */
	public String getProperty(String thePropertyKey) {
		return props.getProperty(thePropertyKey);
	} //END public String getProperty(String)

	public Object setProperty(String thePropertyKey, String theValue) {
		return props.setProperty(thePropertyKey, theValue);
	} //END public Object setProperty(String, String)

	public boolean containsKey(String aKey) {
		return props.containsKey(aKey);
	} //END public boolean containsKEy(String)

	/**
	 * Reads the properties from a file in the users directory
	 */
	private void readProperties() {
		props = new Properties();
		FileInputStream in = null;
		try {
			String userHome = System.getProperty("user.home");
			in = new FileInputStream(userHome + File.separator + PROP_FILENAME);
			props.load(in);
			in.close();
		}
		catch (Exception e) {
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, this.getClass().getName(), "readProperties()", e.getMessage());
			}
			//do nothing
		}
		finally {
			if (null != in) {
				try {
					in.close();
				}
				catch (IOException e) {
					if (logger.isLoggable(Level.FINEST)) {
						logger.logp(Level.FINEST, this.getClass().getName(), "readProperties()", e.getMessage());
					}
					//do nothing
					
				}
			}
		}
		//assign defaults
		if (!props.containsKey(FILE_ENCODING)) {
			props.setProperty(FILE_ENCODING, IPreferencesEditView.FILE_ENCODING_DEFAULT);
		}
		if (!props.containsKey(LEARN_HINT_COVER_PERCENT)) {
			props.setProperty(LEARN_HINT_COVER_PERCENT, Integer.toString(IPreferencesEditView.LH_COVER_PERCENT_DEFAULT));
		}
		if (!props.containsKey(LEARN_HINT_FLASH_TIME)) {
			props.setProperty(LEARN_HINT_FLASH_TIME, Integer.toString(IPreferencesEditView.LH_FLASH_TIME_DEFAULT));
		}
		if (!props.containsKey(LEARN_HINT_SHUFFLE_WORD)) {
			props.setProperty(LEARN_HINT_SHUFFLE_WORD, Boolean.toString(true));
		}
		if (!props.containsKey(PROP_LOGGING_ENABLED)) {
			props.setProperty(PROP_LOGGING_ENABLED, Boolean.toString(false));
		}
		if (!props.containsKey(PROP_LOG_TO_FILE)) {
			props.setProperty(PROP_LOG_TO_FILE, Boolean.toString(false));
		}
		if (!props.containsKey(PROP_FILE_HISTORY)) {
			props.setProperty(PROP_FILE_HISTORY, "");
		}
		if (!props.containsKey(PROP_SYSTEM_LAF)) {
			props.setProperty(PROP_SYSTEM_LAF, String.valueOf(IPreferencesEditView.SYSTEM_LAF_DEFAULT));
		}
		if (!props.containsKey(PROP_SEL_UPD_INST_EDITING)) {
			props.setProperty(PROP_SEL_UPD_INST_EDITING, Boolean.toString(true));
		}
		if (!props.containsKey(PROP_SEL_UPD_INST_LEARNING)) {
			props.setProperty(PROP_SEL_UPD_INST_LEARNING, Boolean.toString(false));
		}
		if (!props.containsKey(PROP_STATS_QUIT)) {
			props.setProperty(PROP_STATS_QUIT, Boolean.toString(false));
		}
	} //END private void readProperties()
	
	/**
	 * Updates the file history either with a new file path or
	 * a file path to delete and saves it in the preferences.
	 * If the file already exists in the history, then it is deleted
	 * in the history and then added in the first place.
	 *
	 * @param String fileName - the file name to be added or deleted
	 * @param boolean isNew - whether the file name is new or should be deleted
	 */
	protected void updateFileHistory(String aFileName, boolean isNew) {
		//get the existing file history from preferences
		//and load into ArryList
		String[][] history = getFileHistoryPaths();
		ArrayList newHistory = new ArrayList(MAX_NUMBER_FILE_HISTORY);
		for (int i = 0; i < history.length; i++) {
			if (!aFileName.equals(history[i][0])) {
				newHistory.add(history[i][0]);
			}
		}
		if (isNew) {
			//put new file into first position
			newHistory.add(0, aFileName);		
		}
		//remove file names if list too long
		for (int i = MAX_NUMBER_FILE_HISTORY; i < newHistory.size(); i++) {
			newHistory.remove(i);
		}		
		//save file history in preferences
		StringBuffer historySB = new StringBuffer();
		for (int i = 0; i < newHistory.size(); i++) {
			if (i > 0) {
				historySB.append(FILE_HISTORY_DELIMITTER);
			}
			historySB.append(newHistory.get(i));
		}
		props.setProperty(PROP_FILE_HISTORY, historySB.toString());
	} //END protected void updateFileHistory(String, boolean)
	
	/**
	 * Gets the list of the recent opened files in a two dimensional array of Strings.
	 * The first element is the real path, while the second element is a symbolic
	 * path name that takes a maximal lenght into account to be easiliy displayable 
	 * e.g. in a menu item.ds
	 * 
	 * @return String[][] - the paths of the most recent opened files
	 */
	public String[][] getFileHistoryPaths() {
		String[][] history = null;
		if (props.containsKey(PROP_FILE_HISTORY)) {
			StringTokenizer st = new StringTokenizer(props.getProperty(PROP_FILE_HISTORY), FILE_HISTORY_DELIMITTER);
			history = new String[st.countTokens()][];
			for (int i = 0; st.hasMoreTokens() && i < MAX_NUMBER_FILE_HISTORY; i++) {
				history[i] = new String[2];
				history[i][0] = st.nextToken();
				history[i][1] = getDisplayFilePath(history[i][0]);
			}
		}		
		return history;
	} //END public String[][] getFileHistoryPaths()
	
	/**
	 * Return a file path name in a display friendly format.
	 * The String begins with the file name and then an abbreviated form of the
	 * path is added in [] brackets.
	 * The abbreviated path contains the physical or logical drive name,
	 * the first directory and the last directory.
	 * The directories in between are - if existing - denoted by elipses (...)
	 * 
	 * @param String aFilePath - the original name of the file path
	 * @return String - the abbreviated file path
	 */
	private String getDisplayFilePath(String aFilePath) {
		StringBuffer displaySB = new StringBuffer();
		//begin with the file name
		displaySB.append(aFilePath.substring(aFilePath.lastIndexOf(File.separator) + 1));
		//add a blank and the opening bracket
		displaySB.append(" [");
		
		//find all separators and add the positions to an ArrayList
		ArrayList positions = new ArrayList();
		int myPosition = -1;
		do {
			myPosition = aFilePath.indexOf(File.separator, myPosition + 1);
			if (myPosition >= 0) {
				positions.add(new Integer(myPosition));
			}
		} while (myPosition >= 0);
		//transform ArryList to int[] for easier access
		int separatorPositions[] = new int[positions.size()];
		for (int i = 0; i < positions.size(); i++) {
			separatorPositions[i] = ((Integer)positions.get(i)).intValue();
		}
		positions = null; //free for immediate garbage collection
		//make a best guess about the logical or physical drive name
		myPosition = -1; // reset to be used for index position after drive name
		if (1 <= separatorPositions[0]) {
			//=> windows drive e.g. C:\
			myPosition = 0;
		}
		else {
			if (1 == separatorPositions[1]) {
				//=> windows logical network drive e.g. \\appserver01\
				myPosition = 2;
			}
			else {
				//=> unix root directory e.g. /opt/. Well "/" is the root directory
				//but we want a little bit more information here :-)
				myPosition = 1;
			}
		}
		displaySB.append(aFilePath.substring(0, separatorPositions[myPosition] + 1));
		//add ellipses and last directory depending on number of directories
		if (myPosition < separatorPositions.length - 1) {
			//the file is not directly situated in the "root" directory
			if (myPosition < (separatorPositions.length - 2)) {
				//several directories
				displaySB.append("..." + File.separator);
			}
			//append last directory
			displaySB.append(aFilePath.substring(separatorPositions[separatorPositions.length - 2] + 1, separatorPositions[separatorPositions.length - 1] + 1));
		}
		//else do nothing		
		//close the bracket and return the display file name as a String
		displaySB.append("]");
		return displaySB.toString();
	} //END private String getDisplayFilePath(String)

	/**
	 * Saves the properties to a file in the users directory.
	 */
	protected void save() {
		FileOutputStream out = null;
		try {
			String userHome = System.getProperty("user.home");
			out = new FileOutputStream(userHome + File.separator + PROP_FILENAME);
			props.store(out, PROP_COMMENT);
			out.close();
		}
		catch (Exception e) {
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, this.getClass().getName(), "save()", e.getMessage());
			}
			//do nothing
		}
		finally {
			if (null != out) {
				try {
					out.close();
				}
				catch (IOException e) {
					if (logger.isLoggable(Level.FINEST)) {
						logger.logp(Level.FINEST, this.getClass().getName(), "save()", e.getMessage());
					}
					//do nothing
					
				}
			}
		}
	} //END protected void save()
} //END public class Preferences extends AModel
