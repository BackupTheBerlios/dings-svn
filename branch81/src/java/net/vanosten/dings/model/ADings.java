/*
 * ADings.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2002, 03, 04, 05, 2006 Rick Gruber-Riemer (dingsbums@vanosten.net)
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

import java.io.File;
import java.util.Date;
import java.text.DateFormat;
import java.text.MessageFormat;

import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.consts.MessageConstants.Message;
import net.vanosten.dings.io.VocabularyXMLReader;
import net.vanosten.dings.io.VocabularyXMLWriter;

import net.vanosten.dings.uiif.*;
import net.vanosten.dings.utils.Toolbox;

//Logging with java.util.logging
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;

public abstract class ADings implements IAppEventHandler {
	/** Holds the units */
	private UnitsCollection units;

	/** Holds the categroies */
	private CategoriesCollection categories;

	/** Holds the entries */
	private EntriesCollection entries;

	/** Holds the entry types */
	private EntryTypesCollection entryTypes;

	/** Holds the EntryTypeAttributs */
	private EntryTypeAttributesCollection attributes;

	/** Holds the learning statistics */
	private StatsCollection stats;

	/** Whether safe is needed */
	private boolean saveNeeded = false;

	/** Whether a vocabulary file is already open */
	private boolean vocabOpened = false;

	/** The file name of the currently open vocabulary*/
	private String currentVocabFileName = null;

	/** The main window / gui */
	protected IDingsMainWindow mainWindow;

	/** The learnOneView to be reused */
	private IEntryLearnOneView entryLearnOneView = null;

	/** The logging handler */
	private Handler loggingHandler = null;

	/** The current view */
	private Message currentView = MessageConstants.Message.N_VIEW_WELCOME;

	/**
	 * The logging logger. The base package structure is chosen as name
	 * in order to have the other loggers in the divers classes inherit the settings (level).
	 * */
	private static Logger logger = Logger.getLogger("net.vanosten.dings");

	/**
	 * The Constructor. It sets up the frame with a default set of menus and at
	 * program start the go view is displayed.
	 */
	public ADings(String[] args) {
		//set the parent controller for Preferences
		Toolbox.getInstance().getPreferencesPointer().setParentController(this);

		//locale
		if (logger.isLoggable(Level.INFO)) {
			logger.logp(Level.INFO, this.getClass().getName(), "ADings(String[])"
					, "Locale: " + Toolbox.getInstance().getCurrentLocalePointer().toString());
		}
		//check the arguments
		checkMainArgs(args);


		this.initializeGUI();
		setMainwindowTitle(null);

		//add the first view which is always the view GO
		AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
		ape.setMessage(MessageConstants.Message.N_VIEW_WELCOME);
		this.handleAppEvent(ape);

		//set status information
		setSaveNeeded(false);
		vocabOpened = false;
	} //END public ADings(String[])

	/**
	 * Parses and checks the runtime arguments from the main method
	 * and checks the minimal JVM version.
	 * If something goes wrong the usage is printed and the program exited.
	 *
	 * @param args The arguments form the main method
	 */
	private final void checkMainArgs(String[] args) {
		try {
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, this.getClass().getName(), "checkMainArgs()"
						, "Arguments: " + args.toString());
			}
			//the following code for checking the version is partly taken from JOnAS
			String reqVer = Toolbox.getInstance().getLocalizedString("application.jvmversion");
			try {
				Package p = Package.getPackage("java.lang");
				String implVer = p.getImplementationVersion();
				String specVer = p.getSpecificationVersion();
				Object[] objects = {implVer,specVer,reqVer}; 
				if (logger.isLoggable(Level.FINEST)) {
					logger.logp(Level.FINEST, this.getClass().getName(), "checkMainArgs()"
							, MessageFormat.format(Toolbox.getInstance().getLocalizedString("application.jvm_installed"),objects));
				}
				if (!p.isCompatibleWith(reqVer)) {
					System.err.println(MessageFormat.format(Toolbox.getInstance().getLocalizedString("application.jvm_test_incompatible"), objects));
					System.exit(0);
				}
			} catch (NumberFormatException nfe) {
				Object[] objects = {reqVer};
				System.err.println(MessageFormat.format(Toolbox.getInstance().getLocalizedString("application.jvm_test_unknown"),objects));
			}

			//check arguments
			boolean overrideLogging = false;
			if (args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					if (args[i].equals("--debug")) {
						//prepare for more advanced logging
						overrideLogging = true;
					}
					else if (args[i].equals("--help")) {
						printUsage();
						System.exit(1);
					}
					else {
						printUsage();
						System.exit(1);
					}
				}
			}
			//prepare for more advanced logging
			changeLogging(overrideLogging);
		} catch (Exception e) {
			printUsage();
			System.err.println(Constants.getThrowableStackTrace(e));
			System.exit(1);
		}
	} //END private final void checkMainArgs(String[])

	/**
	 * Prints the usage of the programm with the options to System.err
	 */
	private final void printUsage() {
		StringBuffer sb = new StringBuffer();
		sb.append("Usage: java -jar dingsbums.jar [options]");
		sb.append("\nOptions:");
		sb.append("\n  --debug          force logging messages to console");
		sb.append("\n  --help           show this help");
		System.err.println(sb.toString());
	} //END private final void printUsage()

	/**
	 * Entry point for GUI-toolkit to initialize the GUI elements.
	 */
	protected abstract void initializeGUI();

	//implements IAppEventHandler
	public void handleAppEvent(AppEvent evt) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "handleAppEvent()", evt.getMessage().name());
		}
		if (evt.isStatusEvent()) {
			if (evt.getMessage() == MessageConstants.Message.S_EXIT_APPLICATION) {
				//if the closing of the vocabulary is successful then exit
				if (closeVocabulary()) {
					exit();
				}
			}
			else if (evt.getMessage() == MessageConstants.Message.S_CLOSE_VOCABULARY) {
				if (closeVocabulary()) {
					AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
					ape.setMessage(MessageConstants.Message.N_VIEW_WELCOME);
					this.handleAppEvent(ape);
				}
				else {
					//nothing has to be done
				}
			}
			else if (evt.getMessage() == MessageConstants.Message.S_OPEN_VOCABULARY) {
				if (null != evt.getDetails()) {
					setVocabularyFileName(evt.getDetails());
					readVocabulary();
				}
				else {
					//read the to be chosen file
					if (checkOverwriteVocabulary() && fileChoosen(true)) {
						readVocabulary();
					}
				}
			}
			else if (evt.getMessage() == MessageConstants.Message.S_NEW_VOCABULARY) {
				if (checkOverwriteVocabulary()) {
					makeNewVocabulary();
				}
			}
			else if (evt.getMessage() == MessageConstants.Message.S_SAVE_VOCABULARY) {
				writeVocabulary(false);
			}
			else if (evt.getMessage() == MessageConstants.Message.S_SAVE_AS_VOCABULARY) {
				writeVocabulary(true);
			}
			else if (evt.getMessage() == MessageConstants.Message.S_SAVE_NEEDED) {
				setSaveNeeded(true);
			}
			else if (evt.getMessage() == MessageConstants.Message.S_SHOW_VALIDATION_ERROR) {
				mainWindow.showMessageDialog("Validation Error", evt.getDetails(), Constants.ERROR_MESSAGE);
			}
			else if (evt.getMessage() == MessageConstants.Message.S_SHOW_DELETE_ERROR) {
				mainWindow.showMessageDialog("Deletion not allowed", evt.getDetails(), Constants.ERROR_MESSAGE);
			}
			else if (evt.getMessage() == MessageConstants.Message.S_CHANGE_LOGGING) {
				changeLogging(false);
			}
			else if (evt.getMessage() == MessageConstants.Message.S_CHANGE_LAF) {
				mainWindow.setLookAndFeel();
			}
		}
		else if (evt.isNavEvent()) {
			//show the new view
			if (evt.getMessage() == MessageConstants.Message.N_VIEW_WELCOME) {
				IWelcomeView welcomeView = mainWindow.getWelcomeView();
				welcomeView.init(this);
				mainWindow.showView(MessageConstants.Message.N_VIEW_WELCOME);
			}
			if (evt.getMessage() == MessageConstants.Message.N_VIEW_STATISTICS) {
				ISummaryView summaryView = mainWindow.getSummaryView();
				stats.setStatsView(summaryView);
				summaryView.init(stats);
				mainWindow.showView(MessageConstants.Message.N_VIEW_STATISTICS);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_INFOVOCAB_EDIT) {
				IInfoVocabEditView infoVocabEditView = mainWindow.getInfoVocabEditView();
				Toolbox.getInstance().getInfoPointer().setEditView(infoVocabEditView);
				infoVocabEditView.setAvailableLocales(Constants.getSupportedLocales(null));
				infoVocabEditView.init(Toolbox.getInstance().getInfoPointer());
				mainWindow.showView(MessageConstants.Message.N_VIEW_INFOVOCAB_EDIT);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_UNITS_LIST) {
				IListView listView = mainWindow.getUnitsListView();
				units.setListView(listView);
				listView.init(units);
				mainWindow.showView(MessageConstants.Message.N_VIEW_UNITS_LIST);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_UNIT_EDIT) {
				Unit thisUnit = units.getCurrentItem();
				IUnitEditView unitEditView = mainWindow.getUnitEditView();
				thisUnit.setEditView(unitEditView);
				unitEditView.init(thisUnit);
				mainWindow.showView(MessageConstants.Message.N_VIEW_UNIT_EDIT);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_CATEGORIES_LIST) {
				IListView listView = mainWindow.getCategoriesListView();
				categories.setListView(listView);
				listView.init(categories);
				mainWindow.showView(MessageConstants.Message.N_VIEW_CATEGORIES_LIST);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_CATEGORY_EDIT) {
				Category thisCategory = categories.getCurrentItem();
				IUnitEditView categoryEditView = mainWindow.getCategoryEditView();
				thisCategory.setEditView(categoryEditView);
				categoryEditView.init(thisCategory);
				mainWindow.showView(MessageConstants.Message.N_VIEW_CATEGORY_EDIT);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_ENTRIES_LIST) {
				IEntriesListView listView = mainWindow.getEntriesListView();
				entries.setListView(listView);
				listView.setEntryTypes(entryTypes.getChoiceProxy());
				listView.init(entries);
				mainWindow.showView(MessageConstants.Message.N_VIEW_ENTRIES_LIST);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_ENTRY_EDIT) {
				IEntryEditView entryEditView = mainWindow.getEntryEditView();
				entryEditView.setEntryTypes(entryTypes.getChoiceProxy());
				Entry thisEntry = entries.getCurrentItem();
				thisEntry.setEditView(entryEditView);
				//set EntryType attributes to choose from
				EntryTypeAttribute anAttribute;
				EntryType thisEntryType = entryTypes.getEntryType(thisEntry.getEntryTypeId());
				for (int i = 0; i < EntryType.NUMBER_OF_ATTRIBUTES; i++) {
					if (thisEntryType.getNumberOfAttributes() > i) {
						anAttribute = thisEntryType.getAttribute(i + 1);
						entryEditView.setAttributeName(anAttribute.getName(), i + 1);
						entryEditView.setAttributeItems(anAttribute.getItemsChoiceProxy(), i + 1);
					}
				}
				//set Units to choose from
				entryEditView.setUnits(units.getChoiceProxy());
				//set Categories to choose from
				entryEditView.setCategories(categories.getChoiceProxy());
				//initialize
				entryEditView.init(thisEntry);
				mainWindow.showView(MessageConstants.Message.N_VIEW_ENTRY_EDIT);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_ENTRY_LEARNONE) {
				//learning direction
				boolean goOn = mainWindow.showLearningDirectionDialog();
				if (goOn) {
					//make view
					entryLearnOneView = mainWindow.getEntryLearnOneView();
	
					//prepare entries
					AppEvent initializeEvt = new AppEvent(AppEvent.EventType.DATA_EVENT);
					initializeEvt.setMessage(MessageConstants.Message.D_ENTRIES_INITIALIZE_LEARNING);
					entries.handleAppEvent(initializeEvt);
					//set entry relation
					AppEvent entryEvt = new AppEvent(AppEvent.EventType.DATA_EVENT);
					entryEvt.setMessage(MessageConstants.Message.D_ENTRY_LEARNONE_NEXT);
					this.handleAppEvent(entryEvt);
					//add to panel and show
					mainWindow.showView(MessageConstants.Message.N_VIEW_ENTRY_LEARNONE);
				}
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_ENTRYTYPES_LIST) {
				IListView listView = mainWindow.getEntryTypesListView();
				entryTypes.setListView(listView);
				listView.init(entryTypes);
				mainWindow.showView(MessageConstants.Message.N_VIEW_ENTRYTYPES_LIST);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_ENTRYTYPE_EDIT) {
				EntryType thisEntryType = entryTypes.getCurrentItem();
				IEntryTypeEditView entryTypeEditView = mainWindow.getEntryTypeEditView();
				thisEntryType.setEditView(entryTypeEditView);
				entryTypeEditView.init(thisEntryType);
				mainWindow.showView(MessageConstants.Message.N_VIEW_ENTRYTYPE_EDIT);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_ENTRYTYPE_ATTRIBUTES_LIST) {
				IListView listView = mainWindow.getEntryTypeAttributesListView();
				attributes.setListView(listView);
				listView.init(attributes);
				mainWindow.showView(MessageConstants.Message.N_VIEW_ENTRYTYPE_ATTRIBUTES_LIST);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_ENTRYTYPE_ATTRIBUTE_EDIT) {
				EntryTypeAttribute thisAttribute = attributes.getCurrentItem();
				IEntryTypeAttributeEditView attributeEditView = mainWindow.getEntryTypeAttributeEditView();
				thisAttribute.setEditView(attributeEditView);
				attributeEditView.init(thisAttribute);
				mainWindow.showView(MessageConstants.Message.N_VIEW_ENTRYTYPE_ATTRIBUTE_EDIT);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_ENTRIES_SELECTION) {
				IEntriesSelectionView selectionView = mainWindow.getEntriesSelectionView();
				entries.setSelectionView(selectionView);
				selectionView.setUnitsList(units.getChoiceProxy());
				selectionView.setCategoriesList(categories.getChoiceProxy());
				selectionView.setTypesList(entryTypes.getChoiceProxy());
				selectionView.init(entries);
				mainWindow.showView(MessageConstants.Message.N_VIEW_ENTRIES_SELECTION);
			}
			else if (evt.getMessage() == MessageConstants.Message.N_VIEW_LEARN_BY_CHOICE) {
				ILearnByChoiceView learnByChoiceView = mainWindow.getLearnByChoiceView();
				learnByChoiceView.init(this);
				learnByChoiceView.setEntriesCollection(entries);
				mainWindow.showView(MessageConstants.Message.N_VIEW_LEARN_BY_CHOICE);
			}
			//set the current View
			currentView = evt.getMessage();
			checkMIsStatus();
		}
		else if (evt.isDataEvent()) {
			if (evt.getMessage() == MessageConstants.Message.D_ENTRY_LEARNONE_NEXT) {
				entryLearnOneView.reset();
				Entry thisEntry = entries.getCurrentItem();
				thisEntry.setLearnOneView(entryLearnOneView);
				//set EntryType attributes to choose from
				EntryTypeAttribute anAttribute;
				EntryType thisEntryType = entryTypes.getEntryType(thisEntry.getEntryTypeId());
				for (int i = 0; i < EntryType.NUMBER_OF_ATTRIBUTES; i++) {
					if (thisEntryType.getNumberOfAttributes() > i) {
						anAttribute = thisEntryType.getAttribute(i + 1);
						entryLearnOneView.setAttributeName(anAttribute.getName(), i + 1);
						entryLearnOneView.setAttributeItems(anAttribute.getItemsChoiceProxy(), i + 1);
					}
				}
				//set Units to choose from
				entryLearnOneView.setUnits(units.getChoiceProxy());
				//set Categories to choose from
				entryLearnOneView.setCategories(categories.getChoiceProxy());
				entryLearnOneView.init(thisEntry);
			}
			else if (evt.getMessage() == MessageConstants.Message.D_ENTRY_TYPE_CHANGE_ATTRIBUTES) {
				entries.handleAppEvent(evt);
			}
			else if (evt.getMessage() == MessageConstants.Message.D_ENTRIES_SELECTION_APPLY) {
				if (1 > entries.countChosenEntries()) {
					mainWindow.showMessageDialog("No chosen entries"
							, "You have made a selection that resulted in an empty set"
							, Constants.WARNING_MESSAGE);
				}
				checkLearningMIsStatus();
			}
			else if (evt.getMessage() == MessageConstants.Message.D_ENTRIES_TOTALS_CHANGED) {
				updateStatusBarStatus(null);
			}
			else if (evt.getMessage() == MessageConstants.Message.D_ENTRIES_RESET_SCORE_ALL) {
				if (checkDoReset(true)) {
					evt.setDetails(currentView.name());
					entries.handleAppEvent(evt);
				}
			}
			else if (evt.getMessage() == MessageConstants.Message.D_ENTRIES_RESET_SCORE_SEL) {
				if (checkDoReset(false)) {
					evt.setDetails(currentView.name());
					entries.handleAppEvent(evt);
				}
			}
			else if (evt.getMessage() == MessageConstants.Message.D_STATISTICS_SAVE_SET) {
				DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Toolbox.getInstance().getCurrentLocalePointer());
				Date timestamp = stats.addNewStatisticSet();
				mainWindow.showMessageDialog("", "Learning statistics saved: " + df.format(timestamp), Constants.INFORMATION_MESSAGE);
			} else if (evt.getMessage() == MessageConstants.Message.D_ENTRIES_INITIALIZE_LEARNING) {
				entries.handleAppEvent(evt);
			}
		}
		else if (evt.isHelpEvent()) {
			mainWindow.showHelp(evt.getMessage());
		}
		//else if(evt.isStatusEvent() || evt.isNavEvent()) parentController.handleAppEvent(evt);
	} //END public void handleAppEvent(AppEvent)

	/**
	 * Get a file name to read or write the vocabulary from/to by showing
	 * a file chooser dialog.
	 *
	 * @param boolean isOpen - whether we will read from a file or write to it
	 */
	private boolean fileChoosen(boolean showOpen) {
		//get the initial fileName
		String fileName = null;
		String[][] fileNameHist = Toolbox.getInstance().getPreferencesPointer().getFileHistoryPaths();
		if (0 < fileNameHist.length) {
			fileName = fileNameHist[0][0];
		}
		String chosenFileName = mainWindow.showFileChooser(fileName, showOpen);

		if (null != chosenFileName && false == Constants.CANCELLED_INPUT.equals(chosenFileName)) {
			setVocabularyFileName(chosenFileName);
			return true;
		}
		return false;
	} //END private boolean fileChoosen()

	/**
	 * Checks if save needed and closes the vocabulary by resetting all
	 * entities and fields.
	 *
	 * @param boolean - true if successful close
	 */
	private boolean closeVocabulary() {
		//if there is no open vocabulary, then just return directly
		if (false == vocabOpened) {
			return true;
		}
		boolean writeSuccess = true;

		//eventually save the learning statistics
		if (Toolbox.getInstance().getPreferencesPointer().getBooleanProperty(Preferences.PROP_STATS_QUIT)) {
			stats.addNewStatisticSet();
		}

		//check write of file needed
		if (saveNeeded) {
			int answer = mainWindow.showOptionDialog("Unsafed Changes"
									,"Do you want to save changes before closing the vocabulary?"
									,Constants.INFORMATION_MESSAGE
									,Constants.YES_NO_CANCEL_OPTION); //default button title
			if (Constants.YES_OPTION == answer) {
				writeSuccess = writeVocabulary(false);
			}
			else if (Constants.NO_OPTION == answer) {
				//nothing needed to be done
			}
			else { //CANCEL_OPTION or CLOSED_OPTION
				//make a break and therefore return false
				return false;
			}
		}
		if (writeSuccess) {
			//reset all entities and fields
			entries = null;
			categories = null;
			units = null;
			entryTypes = null;
			stats = null;

			currentVocabFileName = null;
			vocabOpened = false;
			setSaveNeeded(false);
			setMainwindowTitle(null);

			//everything went fine, so retrun true
			return true;
		}
		else { //vocabulary was not written successfully
			return false;
		}
	} //END public void closeVocabulary()

	/**
	 * does the real exit of the program by calling System.exit(0). Before, the properties are safed.
	 */
	private void exit() {
		//store the application's size
		mainWindow.saveWindowLocationAndSize();

		//hide the main window
		mainWindow.hideMainWindow();

		//safe preferences
		Toolbox.getInstance().getPreferencesPointer().setProperty(Preferences.PROP_PROG_VERSION, Toolbox.getInstance().getLocalizedString("application.version"));
		Toolbox.getInstance().getPreferencesPointer().save();

		//exit
		System.exit(0);
	} //END private void exit()

	/**
	 * Sets the file name of the vocabulary and adds it to the list of recent files
	 *
	 * @param String fileName
	 */
	private void setVocabularyFileName(String aFileName) {
		currentVocabFileName = aFileName;
		Toolbox.getInstance().getPreferencesPointer().updateFileHistory(aFileName, true);
		mainWindow.setFileHistory(Toolbox.getInstance().getPreferencesPointer().getFileHistoryPaths());
		setMainwindowTitle(aFileName);
	} //END private void setVocabularyFileName(String)

	/**
	 * Removes the current vocabulary file name from the history.
	 */
	private void removeVocabularyFileName() {
		Toolbox.getInstance().getPreferencesPointer().updateFileHistory(currentVocabFileName, false);
		mainWindow.setFileHistory(Toolbox.getInstance().getPreferencesPointer().getFileHistoryPaths());
	} //END private void removeVocabularyFileName()

	/**
	 * Changes the status of logging between enabled and disabled.
	 * The status is taken from the applications preferences.
	 * If the property does not exist yet, then Level.OFF is used.
	 *
	 * @param boolean overrideLogging - if set to true logging is turned on no matter what the preferences
	 *                                  and logs go to Console.
	 */
	private void changeLogging(boolean overrideLogging) {
		boolean isFileLogging = false;

		//get from preferences, whether logging should be made to file
		if (false == overrideLogging) {
			if (Toolbox.getInstance().getPreferencesPointer().containsKey(Preferences.PROP_LOG_TO_FILE)) {
				isFileLogging = Toolbox.getInstance().getPreferencesPointer().getBooleanProperty(Preferences.PROP_LOG_TO_FILE);
			}
		}
		//else isFileLogging remains false to force logging to console

		if (isFileLogging) {
			String userHome = System.getProperty("user.home");
			if (null == loggingHandler || loggingHandler instanceof ConsoleHandler) {
				try {
					FileHandler fileHandler = new FileHandler(userHome + File.separator + Constants.LOGGING_FILE_NAME
							, 100000
							, 1
							, false);
					fileHandler.setLevel(Level.ALL);
					if (null == loggingHandler) {
						loggingHandler = fileHandler;
						logger.addHandler(loggingHandler);
					}
					else {
						logger.removeHandler(loggingHandler);
						loggingHandler = fileHandler;
						logger.addHandler(loggingHandler); //TODO: can't this be done by just reassigning the pointer?
					}
				}
				catch (Exception e) {
					System.err.println("Logging to File is not possible and Console will be used instead: " + e.toString());
					isFileLogging = false; //in order to initialize logging to Console below
				}
			}
		}
		if (false == isFileLogging) { //explicit if needed to take catch argument above into account
			ConsoleHandler consoleHandler = new ConsoleHandler();
			consoleHandler.setLevel(Level.ALL);
			if (null == loggingHandler) {
				loggingHandler = consoleHandler;
				logger.addHandler(loggingHandler);
			}
			else {
				logger.removeHandler(loggingHandler);
				loggingHandler = consoleHandler;
				logger.addHandler(loggingHandler); //TODO: can't this be done by just reassigning the pointer?
			}
		}

		//set the log level
		if (overrideLogging) {
			logger.setLevel(Level.FINEST);
		}
		else {
			if (Toolbox.getInstance().getPreferencesPointer().containsKey(Preferences.PROP_LOGGING_ENABLED)) {
				boolean logEnabled = Toolbox.getInstance().getPreferencesPointer().getBooleanProperty(Preferences.PROP_LOGGING_ENABLED);
				if (logEnabled) {
					logger.setLevel(Level.FINEST);
				}
				else {
					logger.setLevel(Level.OFF);
				}
			}
			else {
				logger.setLevel(Level.OFF);
			}
		}
	} //END private void changeLoggingEnabled()

	/**
	 * Checks whether a vocabulary is already open.
	 *
	 * @return boolean - true, if no open vocabulary or if the user accepts to overwrite it
	 */
	private boolean checkOverwriteVocabulary() {
		//warn user that he might overwrite vocabulary with unsaved changes
		if (vocabOpened) {
			StringBuffer message = new StringBuffer("A vocabulary is already open. ");
			message.append("Opening a new vocabulary will overwrite the vocabulary.\n");
			if (saveNeeded) {
				message.append("The current vocabulary has unsaved changes!");
			}
			else {
				message.append("The current vocabulary has no unsafed changes!");
			}
			message.append("\n\n");
			message.append("Do you want to abort?");

			int answer = mainWindow.showOptionDialog("Open Vocabulary File"
											,message.toString()
											,Constants.WARNING_MESSAGE
											,Constants.YES_NO_OPTION);
			if (Constants.YES_OPTION == answer) {
				return false;
			}
		}
		return true;
	} //END private boolean checkOpenVocabulary()

	/**
	 * Reads the vocabulary data from xml-files and assigns them to the controllers.
	 */
	private void readVocabulary() {
		VocabularyXMLReader reader = new VocabularyXMLReader();
		boolean goOn = true;
		try {
			mainWindow.setWaitCursor(true);
			this.updateStatusBarStatus("Opening ...");

			reader.setVocabularyFile(currentVocabFileName, Toolbox.getInstance().getPreferencesPointer().getProperty(Preferences.FILE_ENCODING));
			if (reader.readyToExecute()) {
				//reset the max Ids
				resetMaxIds();
				//read the vocabulary
				reader.execute();
			}
		}
		catch (Exception e) {
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, this.getClass().getName(), "readVocabulary()", e.getMessage());
			}
			//remove the file name from the history
			removeVocabularyFileName();
			//show the error dialog
			mainWindow.showMessageDialog("Read resulted in error", e.toString(), Constants.ERROR_MESSAGE);
			goOn = false;
		}
		finally {
			mainWindow.setWaitCursor(false);
		}
		if (goOn) {
			try {
				//set the data in the controllers
				units = new UnitsCollection(this);
				units.setItems(reader.getUnits());
				categories = new CategoriesCollection(this);
				categories.setItems(reader.getCategories());
				attributes = new EntryTypeAttributesCollection(this);
				attributes.setItems(reader.getAttributes());
				entryTypes = new EntryTypesCollection(this);
				entryTypes.setEntryTypeAttributes(attributes);
				entryTypes.setItems(reader.getEntryTypes());
				entries = new EntriesCollection(this);
				//populate entries
				entries.setEntryTypes(entryTypes);
				entries.setCategories(categories);
				entries.setUnits(units);
				entries.setItems(reader.getEntries());
				entries.initializeSelection();
				//set pointer to entries to be able to find entries that use info
				categories.setEntries(entries);
				units.setEntries(entries);
				attributes.setEntries(entries);
				entryTypes.setEntries(entries);

				//InfoVocab
				Toolbox.getInstance().setInfoFromStore(reader.getInfo(), this);

				//statistics
				stats = new StatsCollection(this);
				stats.setItems(reader.getStats());
				stats.setEntries(entries);
				stats.setCategories(categories);
				stats.setEntryTypes(entryTypes);
				stats.setUnits(units);

				//we now have an opened vocabulary
				vocabOpened = true;
				setSaveNeeded(false);

				//show the SummaryView
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_INFOVOCAB_EDIT);
				this.handleAppEvent(ape);
			}
			catch (Exception e) {
				//remove the file name from the history
				removeVocabularyFileName();
				//show the error dialog
				mainWindow.showMessageDialog("Read resulted in error (possibly encoding error)", e.toString(), Constants.ERROR_MESSAGE);
			}
		}
	} //END private void readVocabulary()

	/**
	 * Make a new vocabulary.
	 */
	private void makeNewVocabulary() {
		//we have an open vocabulary
		vocabOpened = true;

		//reset the max Ids
		resetMaxIds();

		units = new UnitsCollection(this);
		units.setDefaultItem();
		categories = new CategoriesCollection(this);
		categories.setDefaultItem();
		attributes = new EntryTypeAttributesCollection(this);
		attributes.setDefaultItem();
		entryTypes = new EntryTypesCollection(this);
		entryTypes.setEntryTypeAttributes(attributes);
		entryTypes.setDefaultItem();
		Toolbox.getInstance().resetInfo(this);
		//populate entries
		entries = new EntriesCollection(this);
		entries.setEntryTypes(entryTypes);
		entries.setCategories(categories);
		entries.setUnits(units);
		entries.initializeSelection();
		//set pointers
		categories.setEntries(entries);
		units.setEntries(entries);
		entryTypes.setEntryTypeAttributes(attributes);
		entryTypes.setEntries(entries);
		attributes.setEntries(entries);

		//statistics
		stats = new StatsCollection(this);
		stats.setEntries(entries);
		stats.setCategories(categories);
		stats.setEntryTypes(entryTypes);
		stats.setUnits(units);

		//show the InfoVocab view
		AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
		ape.setMessage(MessageConstants.Message.N_VIEW_INFOVOCAB_EDIT);
		this.handleAppEvent(ape);
	} //END private void makeNewVocabulary()

	/**
	 * Resets all ID's in the items
	 */
	private void resetMaxIds() {
		Unit.resetMaxId();
		Category.resetMaxId();
		EntryType.resetMaxId();
		EntryTypeAttributeItem.resetMaxId();
		Entry.resetMaxId();
	} //END private void resetMaxIds()

	/**
	 * Writes all vocabulary data to a xml-file.
	 *
	 * @param boolean - whether a file chooser has to be shown first.
	 */
	private boolean writeVocabulary(boolean showFileChooser) {
		boolean success = true;
		boolean doWrite = true;

		//if the file chooser is requested or there is no file path show the file chooser
		if (showFileChooser || null == currentVocabFileName) {
			doWrite = fileChoosen(false);
		}

		if (doWrite) {
			try {
				mainWindow.setWaitCursor(true);
				this.updateStatusBarStatus("Saving ...");
				VocabularyXMLWriter writer = new VocabularyXMLWriter();
				writer.setVocabularyFile(currentVocabFileName, Toolbox.getInstance().getPreferencesPointer().getProperty(Preferences.FILE_ENCODING));

				//set the XML strings
				writer.setXMLElements(Toolbox.getInstance().getLocalizedString("application.dataversion"), units.getXMLString()
								, categories.getXMLString()
								, entries.getXMLString()
								, entryTypes.getXMLString()
								, attributes.getXMLString()
								, Toolbox.getInstance().getInfoPointer().getXMLString()
								, stats.getXMLString());
				if (writer.readyToExecute()) {
					writer.execute();
				}
				setSaveNeeded(false);
			}
			catch (Exception e) {
				mainWindow.showMessageDialog("Save result", e.toString(), Constants.ERROR_MESSAGE);
				success = false;
				//remove the file name from the history
				removeVocabularyFileName();
			}
			finally {
				mainWindow.setWaitCursor(false);
			}
		}
		return success;
	} //END private boolean writeVocabulary(boolean)

	/**
	 * Sets the saveNeeded status. At the same time the enabling and disabling
	 * of the menu items.
	 *
	 * @param boolean status - true if save of the vocabulary is needed
	 */
	private void setSaveNeeded(boolean aStatus) {
		//set the status
		saveNeeded = aStatus;
		this.updateStatusBarStatus(null);

		//set the status of the menu items
		checkMIsStatus();
	} //END private void setSaveNeeded(boolean)

	/**
	 * Shows a dialog box to the user asking whether he really wnats to reset the scores.
	 *
	 * @param boolean all - true if the request is for all entries, otherwise only for current selection
	 * @return boolean - true if the user accepts, that the changes really should be made
	 */
	private boolean checkDoReset(boolean all) {
		String message;
		if (all) {
			message = "Do you really want to reset the score of all entries to 1?";
		}
		else {
			message = "Do you really want to reset the score of all entries in the current selection to 1?";
		}
		int answer = mainWindow.showOptionDialog(""
				,message
				,Constants.WARNING_MESSAGE
				,Constants.YES_NO_OPTION);
		if (Constants.YES_OPTION == answer) {
			return true;
		}
		return false;
	} //END private boolean checkDoReset(boolean)

	/**
	 * Changes the status of the menu items based on the status of the model
	 * and the chosen view
	 */
	private void checkMIsStatus() {
		//set the status of the menu items
		//quit, new, open, preferences, help, about are always enabled
		mainWindow.setSaveMIEnabled(saveNeeded);

		//depending on open vocabulary
		mainWindow.setOpenVocabEnabled(vocabOpened);

		//depending on number of categories, units and entry types > 0
		if ((null != categories && categories.countElements() > 0)
			&& (null != units && units.countElements() > 0)
			&& (null != entryTypes && entryTypes.countElements() > 0)
		) {
			mainWindow.setEntriesMIEnabled(true);
		}
		else {
			mainWindow.setEntriesMIEnabled(false);
		}

		//depending on number of entries > 0
		if (null != entries && entries.countElements() > 0) {
			mainWindow.setEntriesOkEnabled(true);
			if (currentView == MessageConstants.Message.N_VIEW_ENTRY_EDIT ||
					currentView == MessageConstants.Message.N_VIEW_ENTRY_LEARNONE) {
				mainWindow.setResetScoreMIsEnabled(false);
			}
			else {
				mainWindow.setResetScoreMIsEnabled(true);
			}
		}
		else {
			mainWindow.setEntriesOkEnabled(false);
			mainWindow.setResetScoreMIsEnabled(false);
		}

		//depending on the number of chosen entries
		checkLearningMIsStatus();
	} //END private void checkMIsStatus()

	/**
	 * Checks the number of chosen entries and sets the status of the menu items
	 * for learning accordingly.
	 */
	private void checkLearningMIsStatus() {
		boolean enableLearnOneByOne = false;
		if (null != entries && entries.countChosenEntries() > 0) {
			//learn one by one is ok if at least 1 is chosen
			enableLearnOneByOne = true;
			//TODO: extend this for learning mulitiple entries (based on numbers in preferences)
		}
		mainWindow.setLearningMIsEnabled(enableLearnOneByOne);
	} //END private void checkLearningMIsStatus()

	/**
	 * Updates the texts of the status bar based on the current status of saving
	 * and selection.
	 *
	 * @param String aStatusText - overrides the text to be displayed based on saving status.
	 */
	private void updateStatusBarStatus(String aStatusText) {
		String theStatus = "";
		StringBuffer theSelection = new StringBuffer();

		if (null != aStatusText) {
			theStatus = aStatusText;
		}
		else {
			if (vocabOpened) {
				//status
				if (saveNeeded){
					theStatus = "Save needed";
				}
				else {
					theStatus = "Saved";
				}
				//selection only if entries is not null, i.e. it is not a new stack
				if (null != entries) {
					theSelection.append("Selection: ");
					theSelection.append(entries.countChosenEntries());
					theSelection.append(" / ");
					theSelection.append(entries.countElements());
				}
			}
			else {
				theStatus = Constants.EMPTY_STRING;
				//no change for theSelection
			}
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "updateStatusBarStatus(String)", "aStatusText: " + aStatusText
					+ ", theStatus: " + theStatus + ", theSelection: " + theSelection.toString());
		}

		//set the strings in the status bar
		mainWindow.setStatusBarStatusText(theStatus, theSelection.toString());
	} //END private void updateStatusBarStatus(String)
	
	/**
	 * Sets the title in the window bar of the main window
	 * @param filePath
	 */
	private void setMainwindowTitle(String filePath) {
		String appName = Toolbox.getInstance().getLocalizedString("application.name");
		if (null == filePath) {
			mainWindow.setApplicationTitle(appName);
		} else {
			Object[] args = {filePath,appName};
			mainWindow.setApplicationTitle(MessageFormat.format(Toolbox.getInstance().getLocalizedString("application.title_long"),args));
		}
	}
} //END public abstract class ADings extends IAppEventHandler
