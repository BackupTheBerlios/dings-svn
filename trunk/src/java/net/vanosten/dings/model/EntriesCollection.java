/*
 * EntriesCollection.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Date;
import java.util.Calendar;

import net.vanosten.dings.uiif.IEntriesSelectionView;
import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.event.IAppEventHandler;

import java.util.logging.Logger;
import java.util.logging.Level;

public class EntriesCollection extends ACollection {
	/** The id for the actual entry */
	private Entry currentItem = null;

	/** Acts as a key list (of Strings) when editing */
	private ArrayList chosenKeys = null;

	/** the views served by this collection of models */
	private IEntriesSelectionView selectionView = null;

	/** A pointer to the EntryTypes */
	private EntryTypesCollection entryTypes = null;
	
	/** A pointer to the preferences */
	private Preferences preferences;

	//The holders of pointers to different scores
	private ArrayList scoreOne;
	private ArrayList scoreTwo;
	private ArrayList scoreThree;
	private ArrayList scoreFour;
	private ArrayList scoreFive;
	private ArrayList scoreSix;
	private ArrayList scoreSeven;
	
	//the selections in the IEntriesSelectionView
	int selStatusChoice;
	int selDays;
	int[] selMinMaxScore;
	String[] selUnitsChoice;
	String[] selCategoriesChoice;
	String[] selTypesChoice;

	/** The score of the currentItem when learning */
	private int currentScore = 0;

	/** The score balance in the current learn lesson */
	private int scoreBalance = 0;

	/** The log4j logger */
	private static Logger logger = Logger.getLogger("net.vanosten.dings.model.EntriesCollection");

	public EntriesCollection(IAppEventHandler anEventHandler) {
		super(anEventHandler);
		chosenKeys = new ArrayList();
	} //End public EntriesCollection(IAppEventHandler)

	//implements ACollection
	protected void setTagName() {
		tagName = Constants.XML_ENTRIES;
	} //END protected void setTagName()

	//implements ACollection
	protected void setMessageListView() {
		msgListView = MessageConstants.N_VIEW_ENTRIES_LIST;
	} //END protected void setMessageListView()

	//implements ACollection
	protected void setItems(HashMap theEntries) {
		this.items = theEntries;

		Set allKeys = items.keySet();
		Iterator iter = allKeys.iterator();
		String thisId;
		Entry thisEntry = null;
		while (iter.hasNext()) {
			//set the max id
			thisId = (String)iter.next();
			//give a point er to the EntryType
			thisEntry = (Entry)items.get(thisId);
			thisEntry.setEntryType(entryTypes.getEntryType(thisEntry.getEntryTypeId()), false);
		}

		//set index
		iter = allKeys.iterator();
		if (iter.hasNext()) setCurrentItem((String)iter.next());

		//set chosen keys
		chosenKeys = new ArrayList(items.keySet());
	} //End protected void setItems(HashMap)
	
	protected void setPreferences(Preferences thePreferences) {
		this.preferences = thePreferences;
	} //END protected void setPreferences(Preferences)

	protected Entry getCurrentItem() {
		return currentItem;
	} //END protected Entry getCurrentItem()

	//implements ACollection
	protected void setCurrentItem(String anID) {
		if (null != currentItem) {
			//release the views of the current
			currentItem.releaseViews();
		}
		if (null == anID) {
			currentItem = null;
		}
		else if (items.containsKey(anID)) {
			currentItem = (Entry)items.get(anID);
			currentItem.setParentController(this);
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, this.getClass().getName(), "setCurrentItem", anID);
			}
		}
	} //END private void setCurrentItem(String)

	//Implements ACollection
	protected String selectNewCurrent(String anId) {
		int pos = chosenKeys.indexOf(anId);
		if (pos > 0) {
			return (String)chosenKeys.get(pos -1);
		}
		else if ((0 == pos) && (chosenKeys.size() > 1)) {
			return (String)chosenKeys.get(1);
		}
		else {
			return null;
		}
	} //END protected String selectNewCurrent()

	//Implements ACollection
	protected void removeInOther(String anId) {
		int pos = chosenKeys.indexOf(anId);
		chosenKeys.remove(pos);
	} //END protected void removeInOther(String)

	//implements AChoiceCollection
	protected void deleteItem() {
		deleteItem(currentItem.getId(), false);
	} //END protected void deleteItem()

	//Implements ACollection
	protected boolean checkDeleteAllowed(String anId) {
		//allow allways
		return true;
	} //END protected boolean checkDeleteAllowed(String)

	//implements ACollection
	protected void newItem(String aType, boolean isDefault) {
		Entry newEntry = Entry.newItem(aType);
		items.put(newEntry.getId(), newEntry);
		newEntry.setEntryType(entryTypes.getEntryType(newEntry.getEntryTypeId()), false);
		setCurrentItem(newEntry.getId());
		chosenKeys.add(newEntry.getId());
		//save needed
		sendSaveNeeded();
	} //End public void newItem(String)

	//implements ACollection
	protected void refreshListView() {
		Object[][] data = new Object[chosenKeys.size()][Entry.getTableDisplayTitles().length];

		Entry item;
		Iterator iter = chosenKeys.iterator();
		for (int i = 0; i < data.length; i++) {
			item = (Entry)items.get(iter.next());
			data[i] = item.getTableDisplay();
		}
		if (listView != null) {
			listView.setList(Entry.getTableDisplayTitles(), data, Entry.getTableColumnFixedWidth());
		}
		if (null != currentItem) {
			listView.setSelected(currentItem.getId());
		}
	} //End protected void resetListView()

	//implements ACollection
	public void handleAppEvent(AppEvent evt) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "handleAppEvent()", evt.getMessage() + "; " + evt.getDetails());
		}
		if(evt.isDataEvent()) {
			if (evt.getMessage().equals(MessageConstants.D_LIST_VIEW_NEW)) {
				newItem(evt.getDetails(), false);
				//send navigation event
				AppEvent ape = new AppEvent(AppEvent.NAV_EVENT);
				ape.setMessage(MessageConstants.N_VIEW_ENTRY_EDIT);
				parentController.handleAppEvent(ape);
			}
			else if (evt.getMessage().equals(MessageConstants.D_LIST_VIEW_DELETE)) deleteItem(evt.getDetails(), true);
			else if (evt.getMessage().equals(MessageConstants.D_EDIT_VIEW_DELETE)) deleteItem(currentItem.getId(), false);
			else if (evt.getMessage().equals(MessageConstants.D_LIST_VIEW_REFRESH)) refreshListView();
			//selection
			else if (evt.getMessage().equals(MessageConstants.D_ENTRIES_SELECTION_REFRESH)) {
				refreshSelection();
			}
			else if (evt.getMessage().equals(MessageConstants.D_ENTRIES_SELECTION_APPLY)) {
				//make selection
				ArrayList foo = checkSelectionCriteria();
				if (0 < foo.size()) {
					parentController.handleAppEvent(MessageConstants.getShowErrorListEvent(foo
							, "The selection is not valid"));					
				}
				else {
					placeSelection();
					//give main application chance to react (status of learning menu items, warning if  count = 0)
					//parentController.handleAppEvent(evt); -> not needed now, because all events get propagated
				}
			}
			//edit learnOne view
			else if (evt.getMessage().equals(MessageConstants.D_ENTRY_LEARNONE_NEXT)) {
				nextLearnOne();
				//send to get the view assigned to the new Entry
				parentController.handleAppEvent(evt);
			}
			else if (evt.getMessage().equals(MessageConstants.D_ENTRIES_INITIALIZE_LEARNING)) {
				initializeScores();
			}
			else if (evt.getMessage().equals(MessageConstants.D_ENTRIES_RESET_SCORE_ALL)) {
				resetScores(true, evt.getDetails());
			}
			else if (evt.getMessage().equals(MessageConstants.D_ENTRIES_RESET_SCORE_SEL)) {
				resetScores(false, evt.getDetails());
			}
			else if (evt.getMessage().equals(MessageConstants.D_ENTRY_TYPE_CHANGE_ATTRIBUTES)) {
				//get the details
				StringTokenizer st = new StringTokenizer(evt.getDetails(), Constants.DELIMITTER_APP_EVENT);
				String anEntryTypeId = st.nextToken();
				String oldOneId = Constants.resolveNullString(st.nextToken());
				String oldTwoId = Constants.resolveNullString(st.nextToken());
				String oldThreeId = Constants.resolveNullString(st.nextToken());
				String oldFourId = Constants.resolveNullString(st.nextToken());
				String newOneId = Constants.resolveNullString(st.nextToken());
				String newTwoId = Constants.resolveNullString(st.nextToken());
				String newThreeId = Constants.resolveNullString(st.nextToken());
				String newFourId = Constants.resolveNullString(st.nextToken());
				String defaultOneId = Constants.resolveNullString(st.nextToken());
				String defaultTwoId = Constants.resolveNullString(st.nextToken());
				String defaultThreeId = Constants.resolveNullString(st.nextToken());
				String defaultFourId = Constants.resolveNullString(st.nextToken());
				//update Entries
				Set allKeys = items.keySet();
				Iterator iter = allKeys.iterator();
				Entry thisEntry;
				while (iter.hasNext()) {
					thisEntry = (Entry)items.get(iter.next());
					thisEntry.changeEntryTypeAttributes(
							anEntryTypeId
							, oldOneId
							, oldTwoId
							, oldThreeId
							, oldFourId
							, newOneId
							, newTwoId
							, newThreeId
							, newFourId
							, defaultOneId
							, defaultTwoId
							, defaultThreeId
							, defaultFourId);
				}
			}
			else if (evt.getMessage().equals(MessageConstants.D_EDIT_VIEW_APPLY)) {
				checkCurrentItemSelection(false);
			}
			else if (evt.getMessage().equals(MessageConstants.D_EDIT_VIEW_CHANGE_ENTRY_TYPE)) {
				currentItem.setEntryType(entryTypes.getEntryType(evt.getDetails()), true);
				sendSaveNeeded();
				checkCurrentItemSelection(false);
				//refresh the edit view
				AppEvent newEvt = new AppEvent(AppEvent.NAV_EVENT);
				newEvt.setMessage(MessageConstants.N_VIEW_ENTRY_EDIT);
				parentController.handleAppEvent(newEvt);
			}
		}
		else if (evt.isNavEvent()) {
			if (evt.getMessage().equals(MessageConstants.N_VIEW_ENTRY_EDIT)) {
				editItem(evt.getDetails());
			}
		}
		//TODO: the parentController should be called explicitely after each AppEvent type as an "else"
		//and eventually explicitely within an "else if" if the event has to be propagated even if
		//it has been handled
		if (false == evt.getMessage().equals(MessageConstants.D_ENTRY_TYPE_CHANGE_ATTRIBUTES) &&
				false == evt.getMessage().equals(MessageConstants.D_ENTRIES_RESET_SCORE_ALL) &&
				false == evt.getMessage().equals(MessageConstants.D_ENTRIES_RESET_SCORE_SEL)) {
			parentController.handleAppEvent(evt);
		}
	} //END public void handleAppEvent(AppEvent)

	protected void setEntryTypes(EntryTypesCollection theEntryTypes) {
		this.entryTypes = theEntryTypes;
	} //END protected void setEntryTypes(EntryTypesCollection)
	
	protected int countChosenEntries() {
		return chosenKeys.size();
	} //END protected int countChosenEntries()
	
	private void requestStatusBarUpdate() {
		AppEvent evt = new AppEvent(AppEvent.DATA_EVENT);
		evt.setMessage(MessageConstants.D_ENTRIES_TOTALS_CHANGED);
		parentController.handleAppEvent(evt);		
	} //END private void requestStatusBarUpdate()
	
	//------------------ Statistics --------------------------------------------
	protected int[] getEntriesScoreStats() {
		int[] theStats = new int[Entry.SCORE_MAX];
		Set allKeys = items.keySet();
		Iterator iter = allKeys.iterator();
		//do the counting
		Entry thisEntry;
		String thisID;
		int thisScore = 0;
		while (iter.hasNext()) {
			thisID = (String)iter.next();
			thisEntry = (Entry)items.get(thisID);
			thisScore = thisEntry.getScore();
			theStats[thisScore -1] = theStats[thisScore -1] + 1;
		}
		return theStats;
	} //END protected int[] getEntriesScoreStats()
	
	protected int[] getEntriesPerXStats(boolean onlyChosenEntries, String[] theIds, int type) {
		int[] theStats = new int[theIds.length];
		Set theKeys;
		if (onlyChosenEntries) {
			theKeys = new HashSet(chosenKeys);
		}
		else {
			theKeys = items.keySet();
		}
		Iterator iter = theKeys.iterator();
		//do the counting
		Entry thisEntry;
		String thisID;
		String thisXId;
		while (iter.hasNext()) {
			thisID = (String)iter.next();
			thisEntry = (Entry)items.get(thisID);
			if (StatsCollection.SCORE == type) {
				theStats[thisEntry.getScore() -1]++;
			}
			else {
				switch(type) {
					case StatsCollection.UNIT: thisXId = thisEntry.getUnitId(); break;
					case StatsCollection.CATEGORY: thisXId = thisEntry.getCategoryId(); break;
					default: thisXId = thisEntry.getEntryTypeId(); break;
				}
				for (int i = 0; i < theIds.length; i++) {
					if (thisXId.equals(theIds[i])) {
						theStats[i]++;
						break;
					}
				}
			}
		}
		return theStats;
	} //END protected int[] getEntriesPerXStats(boolean, String[], int)

	//------------------ Learning --------------------------------------------
	
	/**
	 * Resets the level of all entries either in the current selection or in all entries.
	 * If EntriesListView is the current View, then the view is updated
	 * 
	 * @param boolean - if true all entries are reset, else nly the ones in the selection
	 * @param String - the currently shown view
	 */
	private void resetScores(boolean all, String currentView) {
		boolean scoreChanged = false; //whether the score of one of the entries has been changed
		Iterator iter;
		//set the iterator for the right set
		if (all) {
			Set allKeys = items.keySet();
			iter = allKeys.iterator();
		}
		else {
			iter = chosenKeys.iterator();
		}
		//reset
		Entry thisEntry;
		String thisID;
		while (iter.hasNext()) {
			thisID = (String)iter.next();
			thisEntry = (Entry)items.get(thisID);
			if (Entry.SCORE_MIN < thisEntry.getScore()) {
				scoreChanged = true;
			}
			thisEntry.setScore(Entry.SCORE_MIN);
		}
		//send save needed if at least one score has changed
		if (scoreChanged) {
			sendSaveNeeded();
		}
		//place the selection based on the changed scores
		placeSelection();
		//refresh the current view, if it is EntriesListView
		if (null != currentView && currentView.equals(MessageConstants.N_VIEW_ENTRIES_LIST)) {
			refreshListView();
		}
	} //END private void resetScores(boolean, String)

	/**
	 * Initializes the scores. This is needed before learning can be started.
	 */
	private void initializeScores() {
		scoreOne = new ArrayList();
		scoreTwo = new ArrayList();
		scoreThree = new ArrayList();
		scoreFour = new ArrayList();
		scoreFive = new ArrayList();
		scoreSix = new ArrayList();
		scoreSeven = new ArrayList();

		//reset the current score
		currentScore = 0;

		//reset the score balance
		scoreBalance = 0;

		//search in all chosenKeys for scores and distribute them in the ArrayLists
		Iterator iter = chosenKeys.iterator();
		Entry thisEntry;
		int thisScore;
		String thisID;
		while (iter.hasNext()) {
			thisID = (String)iter.next();
			thisEntry = (Entry)items.get(thisID);
			thisScore = thisEntry.getScore();
			if (1 >= thisScore) scoreOne.add(thisID);
			else if (2 == thisScore) scoreTwo.add(thisID);
			else if (3 == thisScore) scoreThree.add(thisID);
			else if (4 == thisScore) scoreFour.add(thisID);
			else if (5 <= thisScore) scoreFive.add(thisID);
			else if (6 <= thisScore) scoreSix.add(thisID);
			else if (7 <= thisScore) scoreSeven.add(thisID);
		}
		//set the first random item
		nextLearnOne();
	} //END private void initializeScores()

	/**
	 * Selects the next Entry to be the currentEntry.
	 * A simple algorithm searches for the next based on the score.
	 * Two strategies are possible, once the score is found:
	 * (a) Randomly choose a position in chosenKeys and then find the next Entry
	 * that matches the score. Or (b) gather all entries of the same score in one ArrayList and then choose
	 * one at random.
	 * (a) uses less memory and is possibly faster, but (b) allows for easy visualization of stats. Therefore (b)
	 * is chosen.
	 */
	private void nextLearnOne() {
		//Set the current item into the right scoreList
		reassignScoreList();

		//get new score and index position
		int nextScore = 0;
		int myIndex = 0;
		boolean goOn = true;
		double scoreRand = 0.0d;
		while(goOn) {
			scoreRand = Math.random();
			if (scoreRand < 0.30) {
				nextScore = 1;
				if (scoreOne.size() > 0) goOn = false;
			}
			else if (scoreRand < 0.55) {
				nextScore = 2;
				if (scoreTwo.size() > 0) goOn = false;
			}
			else if (scoreRand < 0.75) {
				nextScore = 3;
				if (scoreThree.size() > 0) goOn = false;
			}
			else if (scoreRand < 0.80) {
				nextScore = 4;
				if (scoreFour.size() > 0) goOn = false;
			}
			else if (scoreRand < 0.90) {
				nextScore = 5;
				if (scoreFive.size() > 0) goOn = false;
			}
			else if (scoreRand < 0.96) {
				nextScore = 6;
				if (scoreSix.size() > 0) goOn = false;
			}
			else {
				nextScore = 7;
				if (scoreSeven.size() > 0) goOn = false;
			}
		}

		switch(nextScore) {
			case 1: myIndex = (int)Math.floor(Math.random()*scoreOne.size()); break;
			case 2: myIndex = (int)Math.floor(Math.random()*scoreTwo.size()); break;
			case 3: myIndex = (int)Math.floor(Math.random()*scoreThree.size()); break;
			case 4: myIndex = (int)Math.floor(Math.random()*scoreFour.size()); break;
			case 5: myIndex = (int)Math.floor(Math.random()*scoreFive.size()); break;
			case 6: myIndex = (int)Math.floor(Math.random()*scoreSix.size()); break;
			default: myIndex = (int)Math.floor(Math.random()*scoreSeven.size()); break;
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "nextLearnOne()", "Index= " + myIndex + "; score= " + nextScore + "; scoreRand= " + scoreRand);
		}
		//set the new currentItem
		setCurrentLearnEntry(myIndex, nextScore);
		currentScore = nextScore;
	} //END private void nextLearnOne()

	private void setCurrentLearnEntry(int pos, int score) {
		String thisId = null;
		switch (score) {
			case 1: thisId = (String)scoreOne.get(pos); break;
			case 2: thisId = (String)scoreTwo.get(pos); break;
			case 3: thisId = (String)scoreThree.get(pos); break;
			case 4: thisId = (String)scoreFour.get(pos); break;
			case 5: thisId = (String)scoreFive.get(pos); break;
			case 6: thisId = (String)scoreSix.get(pos); break;
			case 7: thisId = (String)scoreSeven.get(pos); break;
		}
		setCurrentItem(thisId);
	} //END private void setCurrentLearnEntry(int, int)

	/**
	 * Moves the currentItem into another holder, if the score has changed.
	 */
	private void reassignScoreList() {
		//move ID from one arraylist to the other if currentScore != SCORE_UNDEFINED
		if (currentScore > 0) {
			//get score of old (current) entry
			int currentEntryScore = currentItem.getScore();
			
			//check whether the currentItem is still part of the selection based on its new score
			//and in this not the case, then do not continue
			if (false == checkCurrentItemSelection(true)) {
				return;
			}

			//correct the score balance
			scoreBalance += currentEntryScore - currentScore;

			//if currentScore and current score from old entry are different
			//then move the ID from one arrayList to the other
			if (currentScore != currentEntryScore) {
				boolean fooB = true;
				//remove
				removeCurrentItemFromScoreList();
				//add
				switch(currentEntryScore) {
					case 1: fooB = scoreOne.add(currentItem.getId()); break;
					case 2: fooB = scoreTwo.add(currentItem.getId()); break;
					case 3: fooB = scoreThree.add(currentItem.getId()); break;
					case 4: fooB = scoreFour.add(currentItem.getId()); break;
					case 5: fooB = scoreFive.add(currentItem.getId()); break;
					case 6: fooB = scoreSix.add(currentItem.getId()); break;
					case 7: fooB = scoreSeven.add(currentItem.getId()); break;
				}
				if (false == fooB && logger.isLoggable(Level.FINEST)) {
					logger.logp(Level.FINEST, this.getClass().getName(), "nextLearnOne()", "Could not add entry: " + currentEntryScore);
				}
			}
		}
	} //END private void reassignScoreList()
	
	//utility method: is called from reassignScoreList
	private void removeCurrentItemFromScoreList() {
		boolean fooB = true; //whether the item could be removed
		
		switch(currentItem.getScore()) {
			case 1: fooB = scoreOne.remove(currentItem.getId()); break;
			case 2: fooB = scoreTwo.remove(currentItem.getId()); break;
			case 3: fooB = scoreThree.remove(currentItem.getId()); break;
			case 4: fooB = scoreFour.remove(currentItem.getId()); break;
			case 5: fooB = scoreFive.remove(currentItem.getId()); break;
			case 6: fooB = scoreSix.remove(currentItem.getId()); break;
			case 7: fooB = scoreSeven.remove(currentItem.getId()); break;
		}
		if (false == fooB && logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "nextLearnOne()", "Could not remove entry: " + currentScore);
		}		
	} //END private void removeCurrentItemFromScoreList()
	
	//utility method
	private Date getLastLearnedBeforeDate() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, -selDays);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		return now.getTime();
	} //END private Date getLastLearnedBeforeDate()

	//------------------ Selection --------------------------------------------
	
	/*
	 * Refreshes the choice in the IEntriesSelectionView to the values chosen the last time.
	 * If none had been chosen before, then the defaults (as defined in the view) are left.
	 */
	private void refreshSelection() {
		//check if selection has been done before.
		//If selUnitsChoice != null, then a selection has been made and selection can be reset
		if (null != selUnitsChoice) {
			selectionView.setStatusChoice(selStatusChoice);
			selectionView.setLastLearnedBefore(selDays);
			selectionView.setMinMaxScore(selMinMaxScore);
			selectionView.setUnitsChoice(selUnitsChoice);
			selectionView.setCategoriesChoice(selCategoriesChoice);
			selectionView.setTypesChoice(selTypesChoice);
		}
		//else do nothing
	} //END private void refreshSelection()

	private void placeSelection() {
		if (null != selectionView) {
			selStatusChoice = selectionView.getStatusChoice();
			//Date
			try {
				selDays = Integer.parseInt(selectionView.getLastLearnedBefore());
			}
			catch (NumberFormatException e) {
				selDays = 0;
			}
			
			//score
			selMinMaxScore = selectionView.getMinMaxScore();
			//selections
			selUnitsChoice = selectionView.getUnitsChoice();
			selCategoriesChoice = selectionView.getCategoriesChoice();
			selTypesChoice = selectionView.getTypesChoice();
			chosenKeys.clear();
			Set allKeys = items.keySet();
			Iterator iter = allKeys.iterator();
			Entry thisEntry;
			boolean selected = false;
			while (iter.hasNext()) {
				thisEntry = (Entry)items.get(iter.next());
				selected = thisEntry.partOfChoice(selStatusChoice, getLastLearnedBeforeDate(), selMinMaxScore
						, selUnitsChoice, selCategoriesChoice, selTypesChoice);
				if (true == selected) {
					chosenKeys.add(thisEntry.getId());
				}
			}
			requestStatusBarUpdate();
		}
		//else error
	} //END private void placeSelection()
	
	/**
	 * Checks whether the selection made in <code>IEntriesSelectionView</code> is valid.
	 * 
	 * @return ArrayList - If there are validation errors then a String for each error is included. Otherwise the List is empty.
	 */
	private ArrayList checkSelectionCriteria() {
		ArrayList errors = new ArrayList();
		//test whether value of lastLearnedBeforeTF is valid
		try {
			Integer.parseInt(selectionView.getLastLearnedBefore());
		}
		catch (NumberFormatException e) {
			errors.add("The value of last learned before has to be a number and equal or greater than 0.");
		}
		//test whether scores are correct
		int[] scores = selectionView.getMinMaxScore();
		if (scores[0] > scores[1]) {
			errors.add("The maximum score must at least be equal to the minimum score.");
		}
		//test the number of units
		if (0 >= selectionView.getUnitsChoice().length) {
			errors.add("At least one unit has to be chosen.");
		}
		//test the number of categories
		if (0 >= selectionView.getCategoriesChoice().length) {
			errors.add("At least one category has to be chosen.");
		}		
		//test the number of entry types
		if (0 >= selectionView.getTypesChoice().length) {
			errors.add("At least one entry type has to be chosen.");
		}		
		return errors;
	} //END private ArrayList checkSelectionCriteria()
	
	/**
	 * Checks whether the current item is still within the selection.
	 * The check is only made, if the preferences state immediate check.
	 * 
	 * @param isLearning - whether the current item is used in learning or editing right now
	 * @return boolean - true if the item is still in the selection
	 */
	private boolean checkCurrentItemSelection(boolean isLearning) {
		String propertyS = Preferences.PROP_SEL_UPD_INST_EDITING; //for the big if clause
		if (isLearning) {
			propertyS = Preferences.PROP_SEL_UPD_INST_LEARNING;
		}
		if (Boolean.valueOf(preferences.getProperty(propertyS)).booleanValue()) {
			if (false == currentItem.partOfChoice(selStatusChoice, getLastLearnedBeforeDate(), selMinMaxScore
					, selUnitsChoice, selCategoriesChoice, selTypesChoice)) {
				//remove from score list if isLEarning == true
				if (isLearning) {
					removeCurrentItemFromScoreList();
				}
				//remove from current selection
				chosenKeys.remove(currentItem.getId());					
				//update status bar
				requestStatusBarUpdate();					
				//do not continue
				return false;
			}
		}
		return true;
	} //END private boolean checkCurrentItemSelection(boolean)

	protected void setSelectionView(IEntriesSelectionView aChooserView) {
		this.selectionView = aChooserView;
	} //END protected void setSelectionView(EntriesSelectionView)

	/**
	 * Controlls whether a selectable item (category, unit, entryType, attribute) is used in one of the entries.
	 * Delegates this to Entry.isSelectionItemUsed(String).
	 */
	protected boolean isItemUsed(String anId) {
		Set allKeys = items.keySet();
		Iterator iter = allKeys.iterator();
		Entry thisEntry;
		while (iter.hasNext()) {
			thisEntry = (Entry)items.get(iter.next());
			if (thisEntry.isItemUsed(anId)) {
				return true;
			}
		}
		return false;
	} //END protected boolean isSelectionItemUsed(String)
} //End public class EntriesCollection
