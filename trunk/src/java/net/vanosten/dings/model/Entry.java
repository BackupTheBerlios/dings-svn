/*
 * Entry.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2002, 03, 04, 2005 Rick Gruber-Riemer (rick@vanosten.net)
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
import java.util.Date;

import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.event.AppEvent;

import net.vanosten.dings.uiif.IEntryEditView;
import net.vanosten.dings.uiif.IEntryLearnOneView;

import java.util.logging.Logger;
import java.util.logging.Level;

public final class Entry extends AIdItemModel {
	/** The minimal score an Entry can have */
	public final static int SCORE_MIN = 1;
	/** The maximal score an Entry can have */
	public final static int SCORE_MAX = 7;  //if you change this, you also have to adapt EntriesCollection, EntryEditVIew, LevelTableCellRenderer

	//Constants for status for making selection
	public final static int STATUS_SELECT_ALL = 0;
	public final static int STATUS_SELECT_UPTODATE = 1;
	public final static int STATUS_SELECT_NEEDSEDITING = 2;

	//the elements
	private String base;
	private String target;
	private String pronunciation;
	private String explanation;
	private String relation;
	private String example;

	//the attributes;
	private boolean status = false;
	private int score;
	private String unitId;
	private String categoryId;
	private String attributeOneId;
	private String attributeTwoId;
	private String attributeThreeId;
	private String attributeFourId;

	/** The date when this Entry has been learned the last time */
	private Date lastLearned;

	/** Defines the maximal number of an item until now */
	private static int maxId = 0;

	/** The edit view */
	private IEntryEditView editView;

	/** The learnOne view */
	private IEntryLearnOneView learnOneView;

	/** A pointer to the EntryType of this Entry */
	private EntryType entryType;

	/** The entry type id.
	 * In contrast to the entryType pointer this is stored in XML.
	 * The pointer is for convenience only. */
	private String entryTypeId;

	/** A static pointer to InfoVocab */
	private static InfoVocab infoVocab;

	/**
	 * Whether the view mode is editing or learning.
	 * This could theoretically be done by quering which view is null. but this is not save,
	 * if releaseViews() is not called in some situation.
	 */
	private boolean isEditView = false;

	public Entry(String anId, boolean aStatus, int aScore, String aUnitId, String aCategoryId, String anEntryTypeId
					, String anAttributeOne, String anAttributeTwo, String anAttributeThree, String anAttributeFour
					, String aLastUpd, String aLastLearned
					, String aBase, String aTarget
					, String aPronunciation, String anExplanation
					, String anExample, String aRelation) {
		logger = Logger.getLogger("net.vanosten.dings.model.Entry");
		setMaxId(anId);
		this.id = anId;
		setScore(aScore);
		this.status = aStatus;
		this.unitId = aUnitId;
		this.categoryId = aCategoryId;
		this.entryTypeId = anEntryTypeId;
		this.attributeOneId = anAttributeOne;
		this.attributeTwoId = anAttributeTwo;
		this.attributeThreeId = anAttributeThree;
		this.attributeFourId = anAttributeFour;
		this.setLastUpd(aLastUpd);
		this.lastLearned = Constants.getDateFromString(aLastLearned, new Date(0), logger);
		this.base = aBase;
		this.target = aTarget;

		//the rest of the String may be null
		if (null == aPronunciation) this.pronunciation = Constants.EMPTY_STRING;
		else this.pronunciation = aPronunciation;
		if (null == anExplanation) this.explanation = Constants.EMPTY_STRING;
		else this.explanation = anExplanation;
		if (null == anExample) this.example = Constants.EMPTY_STRING;
		else this.example = anExample;
		if (null == aRelation) this.relation = Constants.EMPTY_STRING;
		else this.relation = aRelation;
	} //END public Entry(...)

	/**
	 * Checks and sets the highest Id
	 */
	private static void setMaxId(String thisId) {
		maxId = Math.max(maxId, Integer.parseInt(thisId.substring(Constants.PREFIX_ENTRY.length(),thisId.length())));
	} //END private static void setMaxId(string)

	/**
	 * Reset the max Id to 0.
	 * E.g. used when creating a new vocabulary after another vocabulary had been opened.
	 */
	protected static void resetMaxId() {
		maxId = 0;
	} //END protected static void resetMaxId()

	/**
	 * Returns a valid id for a new item
	 */
	private static String getNewId() {
		maxId++;
		return (Constants.PREFIX_ENTRY + maxId);
	} //END private static String getNewId()

	protected static Entry newItem(String anEntryTypeID) {
		return new Entry(getNewId(), false, SCORE_MIN, Constants.UNDEFINED, Constants.UNDEFINED, anEntryTypeID
									, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING
									, null, null
									, Constants.UNDEFINED, Constants.UNDEFINED
									, Constants.EMPTY_STRING, Constants.EMPTY_STRING
									, Constants.EMPTY_STRING, Constants.EMPTY_STRING);
	} //END protected static Entry newItem(String)

	//implements AItemModel
	protected String getXMLString() {
		StringBuffer xml = new StringBuffer();

		xml.append("<").append(Constants.XML_ENTRY);
		//common attributes
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ENTRYTYPE, entryTypeId));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ID, id));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_UNIT, unitId));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_CATEGORY, categoryId));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_SCORE, Integer.toString(score)));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_STATUS, Boolean.toString(status)));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_LAST_UPD, this.getLastUpdString()));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_LAST_LEARNED, Constants.getDateString(lastLearned)));
		if (entryType.getNumberOfAttributes() > 0) {
			xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ATTRIBUTEONE, attributeOneId));
		}
		if (entryType.getNumberOfAttributes() > 1) {
			xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ATTRIBUTETWO, attributeTwoId));
		}
		if (entryType.getNumberOfAttributes() > 2) {
			xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ATTRIBUTETHREE, attributeThreeId));
		}
		if (entryType.getNumberOfAttributes() > 3) {
			xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ATTRIBUTEFOUR, attributeFourId));
		}
		xml.append(">");
		//rest
		xml.append(Constants.getXMLTaggedValue(Constants.XML_BASE, base));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_TARGET, target));
		if (null == explanation || false == explanation.equals(Constants.EMPTY_STRING)) {
			xml.append(Constants.getXMLTaggedValue(Constants.XML_EXPLANATION, explanation));
		}
		if (null == pronunciation || false == pronunciation.equals(Constants.EMPTY_STRING)) {
			xml.append(Constants.getXMLTaggedValue(Constants.XML_PRONUNCIATION, pronunciation));
		}
		if (null == example || false == example.equals(Constants.EMPTY_STRING)) {
			xml.append(Constants.getXMLTaggedValue(Constants.XML_EXAMPLE, example));
		}
		if (null == relation || false == relation.equals(Constants.EMPTY_STRING)) {
			xml.append(Constants.getXMLTaggedValue(Constants.XML_RELATION, relation));
		}
		xml.append("</").append(Constants.XML_ENTRY).append(">");
		return xml.toString();
	} //END protecected String getXMLString(String)


	//implements AIdModel
	protected Object[] getTableDisplay(){
		Object[] display = {id, Boolean.valueOf(status), new Integer(score), base, target};
		return display;
	} //END protected Object[] getTableDisplay()

	//overrides AIdModel with a real implementation
	protected static String[] getTableDisplayTitles() {
		String[] titles = {"Status", "Score", infoVocab.getBaseLabel(), infoVocab.getTargetLabel()};
		return titles;
	} //END protected static String[] getTableDisplayTitles()

	protected static boolean[] getTableColumnFixedWidth() {
		boolean fixed[] = {true, true, false, false};
		return fixed;
	} //END protected static boolean[] getTableColumnFixedWidth()

	/**
	 * Checks the bounds before setting the score.
	 * @param aScore
	 */
	protected void setScore(int aScore) {
		if (aScore > SCORE_MAX) {
			this.score = SCORE_MAX;
		}
		else if (aScore < SCORE_MIN) {
			this.score = SCORE_MIN;
		}
		else this.score = aScore;
	} //END protected void setScore(int)

	protected int getScore() {
		return score;
	} //END protected int getScore()

	protected void setEditView(IEntryEditView aView)  {
		editView = aView;
		isEditView = true;
	} //END protected void setEditView(IEntryEditView)

	protected void setLearnOneView(IEntryLearnOneView aLearnOneView)  {
		learnOneView = aLearnOneView;
		isEditView = false;
	} //END protected void setLearnOneView(IEntryLearnOneView)

	/**
	 * Releases the pointer to its view.
	 */
	protected void releaseViews() {
		editView = null;
		learnOneView = null;
	} //END protected void releaseViews()

	/**
	 * Tests the required fields for valid contents.
	 *
	 * @return ArrayList - a list of validation errors. Size() = 0 means valid model.
	 */
	public static ArrayList validate(String anId, String anOrigin, String aDestination) {
		//TODO: implement this the right way (score, attributes based on EntryType)
		ArrayList errors = new ArrayList();
		String idError = validateId(Constants.PREFIX_ENTRY, anId);
		if (null != idError) errors.add(idError);
		if (false == validateString(anOrigin, 1)) {
			errors.add("Base may not be empty");
		}
		if (false == validateString(aDestination, 1)) {
			errors.add("Target may not be empty");
		}
		return errors;
	} //END public static ArrayList validate(...)

	/**
	 * Updates the model with data from the edit view.
	 *
	 * @return String - null, if everything could be updated, or a string indicating, what was wrong.
	 */
	protected void updateModel() {
		if (isEditView) {
			//get values from editView and trim them
			String originV = editView.getBase().trim();
			String destinationV = editView.getTarget().trim();
			//validate where necessary
			ArrayList errors = validate(id, originV, destinationV);
			//if validation is ok, save the new values.
			if (0 ==  errors.size()) {
				//validated values
				base = originV;
				target = destinationV;
				//not validated values
				if (entryType.getNumberOfAttributes() > 0) {
					attributeOneId = editView.getAttributeId(1);
				}
				if (entryType.getNumberOfAttributes() > 1) {
					attributeTwoId = editView.getAttributeId(2);
				}
				if (entryType.getNumberOfAttributes() > 2) {
					attributeThreeId = editView.getAttributeId(3);
				}
				if (entryType.getNumberOfAttributes() > 3) {
					attributeFourId = editView.getAttributeId(4);
				}
				explanation = editView.getExplanation().trim();
				example = editView.getExample().trim();
				pronunciation = editView.getPronunciation().trim();
				relation = editView.getRelation().trim();
				unitId = editView.getUnitId();
				categoryId = editView.getCategoryId();
				status = editView.getStatus();
				//save needed and reset
				sendSaveNeeded();
				updateGUI();
			}
		}
	} //END protected void updateModel()

	private void getResultLearnOneView() {
		//update status
		status = learnOneView.getStatus();
		//set the new score
		int rate = -1;  //failed
		if (learnOneView.isSuccess()) {
			if (learnOneView.isHintUsed()) {
				rate = 0;
			}
			else rate = 1;
		}
		setScore(score + rate); //method checks boundaries for score
		//set lastLearned to current time
		lastLearned = new Date();
		//safe needed
		sendSaveNeeded();
	} //END private void getResultLearnOneView();

	//implements AModel
	protected void updateGUI() {
		if (isEditView) {
			editView.setUnitId(unitId);
			editView.setCategoryId(categoryId);
			editView.setStatus(status);
			if (entryType.getNumberOfAttributes() > 0) {
				editView.setAttributeId(attributeOneId, 1);
			}
			if (entryType.getNumberOfAttributes() > 1) {
				editView.setAttributeId(attributeTwoId, 2);
			}
			if (entryType.getNumberOfAttributes() > 2) {
				editView.setAttributeId(attributeThreeId, 3);
			}
			if (entryType.getNumberOfAttributes() > 3) {
				editView.setAttributeId(attributeFourId, 4);
			}
			editView.setBase(base);
			editView.setTarget(target);
			editView.setExplanation(explanation);
			editView.setExample(example);
			editView.setRelation(relation);
			editView.setPronunciation(pronunciation);
			editView.setEntryType(entryType.getName(), entryType.getId());

			//visual feedback
			editView.setEditing(false, true);
			editView.setBaseIsValueValid(true);
			editView.setTargetIsValueValid(true);
		}
		else { //learnOneView
			learnOneView.setUnitId(unitId);
			learnOneView.setCategoryId(categoryId);
			learnOneView.setStatus(status);
			learnOneView.setScore(score);
			if (entryType.getNumberOfAttributes() > 0) {
				learnOneView.setAttributeId(attributeOneId, 1);
			}
			if (entryType.getNumberOfAttributes() > 1) {
				learnOneView.setAttributeId(attributeTwoId, 2);
			}
			if (entryType.getNumberOfAttributes() > 2) {
				learnOneView.setAttributeId(attributeThreeId, 3);
			}
			if (entryType.getNumberOfAttributes() > 3) {
				learnOneView.setAttributeId(attributeFourId, 4);
			}
			learnOneView.setBase(base);
			learnOneView.setTarget(target);
			learnOneView.setExplanation(explanation);
			learnOneView.setExample(example);
			learnOneView.setRelation(relation);
			learnOneView.setPronunciation(pronunciation);
			learnOneView.setEntryType(entryType.getName());
		}
	} //END private void updateGUI()

	//Implements AItemModel
	protected void checkChangeInGUI() {
		boolean isEditing = false;
		boolean isValid = validateString(editView.getBase(), 1) && validateString(editView.getTarget(), 1);

		if (false == editView.getUnitId().trim().equals(unitId)) {
			isEditing = true;
		}
		if (false == editView.getCategoryId().trim().equals(categoryId)) {
			isEditing = true;
		}
		if (editView.getStatus() != status) {
			isEditing = true;
		}
		if (entryType.getNumberOfAttributes() > 0) {
			if (false == editView.getAttributeId(1).trim().equals(attributeOneId)) isEditing = true;
		}
		if (entryType.getNumberOfAttributes() > 1) {
			if (false == editView.getAttributeId(2).trim().equals(attributeTwoId)) isEditing = true;
		}
		if (entryType.getNumberOfAttributes() > 2) {
			if (false == editView.getAttributeId(3).trim().equals(attributeThreeId)) isEditing = true;
		}
		if (entryType.getNumberOfAttributes() > 3) {
			if (false == editView.getAttributeId(4).trim().equals(attributeFourId)) isEditing = true;
		}
		if (false == editView.getBase().trim().equals(base)) {
			isEditing = true;
		}
		if (false == editView.getTarget().trim().equals(target)) {
			isEditing = true;
		}
		if (false == editView.getExplanation().trim().equals(explanation)) {
			isEditing = true;
		}
		if (false == editView.getExample().trim().equals(example)) {
			isEditing = true;
		}
		if (false == editView.getRelation().trim().equals(relation)) {
			isEditing = true;
		}
		if (false == editView.getPronunciation().trim().equals(pronunciation)) {
			isEditing = true;
		}

		editView.setEditing(isEditing, isValid);
		//validation
		editView.setBaseIsValueValid(validateString(editView.getBase(), 1));
		editView.setTargetIsValueValid(validateString(editView.getTarget(), 1));
	} //END protected void checkChangeInGUI()

	/**
	 * Handle application events
	 *
	 * @param AppEvent evt
	 */
	public void handleAppEvent(AppEvent evt) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "handleAppEvent()", evt.getMessage() + "; " + evt.getDetails());
		}
		if (evt.isDataEvent()) {
			if (evt.getMessage().equals(MessageConstants.D_EDIT_VIEW_APPLY)) {
				updateModel();
				//give entries chance to check selection
				parentController.handleAppEvent(evt);
			}
			else if (evt.getMessage().equals(MessageConstants.D_EDIT_VIEW_REVERT)) {
				updateGUI();
			}
			else if (evt.getMessage().equals(MessageConstants.D_EDIT_VIEW_CHECK_CHANGE)) {
				checkChangeInGUI();
			}
			else if (evt.getMessage().equals(MessageConstants.D_EDIT_VIEW_DELETE)) {
				parentController.handleAppEvent(evt);
			}
			else if (evt.getMessage().equals(MessageConstants.D_ENTRY_LEARNONE_GETRESULT)) {
				getResultLearnOneView();
			}
			else if (evt.getMessage().equals(MessageConstants.D_ENTRY_LEARNONE_REFRESH)) {
				updateGUI();
			}
			else if (evt.getMessage().equals(MessageConstants.D_ENTRY_LEARNONE_NEXT)) {
				parentController.handleAppEvent(evt);
			}
			else if (evt.getMessage().equals(MessageConstants.D_EDIT_VIEW_CHANGE_ENTRY_TYPE)) {
				parentController.handleAppEvent(evt);
			}
		}
		else parentController.handleAppEvent(evt);
		/*
		if (isEditView) {
			//reuse functionality from AEditView
			super.handleAppEvent(evt);
		}
		else { //learnOneView
			if (evt.isDataEvent()) {
				else if (evt.getMessage().equals(MessageConstants.D_ENTRY_LEARNONE_SHOWALL)) {
					showAllLearnOneView();
				}
			}
			*/
	} //END public void handleAppEvent(AppEvent)

	/**
	 * Controlls whether a selectable item (Category, Unit, EntryType, EntryTypeAttributeItem) is used in this Entry.
	 * Whether it is a category, a unit or an entryType is checked based on the prefix of the id.
	 */
	protected boolean isItemUsed(String anId) {
		boolean inUse = false;
		if (anId.startsWith(Constants.PREFIX_UNIT)) {
			inUse = anId.equals(unitId);
		}
		else if (anId.startsWith(Constants.PREFIX_CATEGORY)) {
			inUse = anId.equals(categoryId);
		}
		else if (anId.startsWith(Constants.PREFIX_ENTRYTYPE_ATTRIBUTE_ITEM)) {
			ArrayList attributeIds = new ArrayList(3);
			if (null != attributeOneId) {
				attributeIds.add(attributeOneId);
				if (null != attributeTwoId) {
					attributeIds.add(attributeTwoId);
					if (null != attributeThreeId) {
						attributeIds.add(attributeThreeId);
						if (null != attributeFourId) {
							attributeIds.add(attributeFourId);
						}
					}
				}
			}
			inUse = attributeIds.contains(anId);
		}
		//EntryType must be after EntryTypeAttributeItem, because the PREFIX contains the ET
		else if (anId.startsWith(Constants.PREFIX_ENTRYTYPE)) {
			inUse = anId.equals(entryTypeId);
		}
		return inUse;
	} //END protected boolean isItemUsed(String)

	/**
	 * Determines whether this entry shall be part of the current choice.
	 * If a parameter is null, then all entries can make part of the choice based on this particular parameter.
	 *
	 * @return boolean - whether this entry is part of choice, i.e. passes the criterias
	 */
	protected boolean partOfChoice(int theStatus, Date lastLearnedBefore, int[] minMaxScores
			, String[] theUnitIds, String[] theCategoryIds, String[] theEntryTypeIds) {
		boolean check;
		//status
		check = false;
		if (STATUS_SELECT_ALL == theStatus) {
			check = true;
		}
		else if ((true == status) && (STATUS_SELECT_UPTODATE == theStatus)) {
			check = true;
		}
		else if ((false == status) && (STATUS_SELECT_NEEDSEDITING == theStatus)) {
			check = true;
		}
		if (false == check) return false;
		//levels
		if (score < minMaxScores[0] || score > minMaxScores[1]) {
			return false;
		}
		//date
		if (lastLearned.after(lastLearnedBefore)) {
			return false;
		}
		//unit
		if (null != theUnitIds) {
			if (0 >= theUnitIds.length) {
				return false;
			}
			else {
				check = false;
				for (int i = 0; i < theUnitIds.length; i++) {
					if (unitId.equals(theUnitIds[i])) {
						check = true;
						break;
					}
				}
				if (false == check) return false;
			}
		}
		//category
		if (null != theCategoryIds) {
			if (0 >= theCategoryIds.length) {
				return false;
			}
			else {
				check = false;
				for (int i = 0; i < theCategoryIds.length; i++) {
					if (categoryId.equals(theCategoryIds[i])) {
						check = true;
						break;
					}
				}
				if (false == check) return false;
			}
		}
		//entry type
		if (null != theEntryTypeIds) {
			if (0 >= theEntryTypeIds.length) {
				return false;
			}
			else {
				check = false;
				for (int i = 0; i < theEntryTypeIds.length; i++) {
					if (entryTypeId.equals(theEntryTypeIds[i])) {
						check = true;
						break;
					}
				}
				if (false == check) return false;
			}
		}
		//all passed, so we just return true
		return true;
	} //END protected boolean partOfChoice(...)

	/**
	 * Sets the EntryType for reference
	 *
	 * @param EntryType - a pointer to the entry Type
	 * @param boolean - true if the attributes should be reset, because the entryType was changed
	 * 					in the GUI.
	 */
	protected void setEntryType(EntryType aType, boolean reset) {
		this.entryType = aType;
		this.entryTypeId = entryType.getId();
		if (reset) {
			attributeOneId = Constants.EMPTY_STRING;
			attributeTwoId = Constants.EMPTY_STRING;
			attributeThreeId = Constants.EMPTY_STRING;
			attributeFourId = Constants.EMPTY_STRING;
		}
		//set the attributes to default value, if they are EMPTY_STRING
		if ((entryType.getNumberOfAttributes() > 0)
				&& (null == attributeOneId || Constants.EMPTY_STRING.equals(attributeOneId))) {
			attributeOneId = entryType.getAttribute(1).getDefaultItem();
		}
		if ((entryType.getNumberOfAttributes() > 1)
				&& (null == attributeTwoId || Constants.EMPTY_STRING.equals(attributeTwoId))) {
			attributeTwoId = entryType.getAttribute(2).getDefaultItem();
		}
		if ((entryType.getNumberOfAttributes() > 2)
				&& (null == attributeThreeId || Constants.EMPTY_STRING.equals(attributeThreeId))) {
			attributeThreeId = entryType.getAttribute(3).getDefaultItem();
		}
		if ((entryType.getNumberOfAttributes() > 3)
				&& (null == attributeFourId || Constants.EMPTY_STRING.equals(attributeFourId))) {
			attributeFourId = entryType.getAttribute(4).getDefaultItem();
		}
	} //END protected void setEntryType(EntryType)

	/**
	 * Sets the InfoVocab
	 */
	protected static void setInfoVocab(InfoVocab anInfoVocab) {
		infoVocab = anInfoVocab;
	} //END protected static void setInfoVocab(InfoVocab)

	/**
	 * Used in EntriesCollection so the entryType can be set in Entry.setEntryType(EntryType)
	 */
	protected String getEntryTypeId() {
		return entryTypeId;
	} //END protected String getEntryTypeId()

	protected String getCategoryId() {
		return categoryId;
	} //END protected String getCategoryId()

	protected String getUnitId() {
		return unitId;
	} //END protected String getUnitId()

	/**
	 * Changes the attributes of the Entry according to the new attributes of the EntryType.
	 * If an attribute did not exist then a default is applied.
	 * Else the existing attribute is assigned.
	 */
	protected void changeEntryTypeAttributes(String anEntryTypeId
										  , String oldOneId
										  , String oldTwoId
										  , String oldThreeId
										  , String oldFourId
										  , String newOneId
										  , String newTwoId
										  , String newThreeId
										  , String newFourId
										  , String defaultOneId
										  , String defaultTwoId
										  , String defaultThreeId
										  , String defaultFourId) {
		if (anEntryTypeId.equals(this.entryTypeId)) {
			//save the existing attributes
			HashMap oldRelation = new HashMap(EntryType.NUMBER_OF_ATTRIBUTES);
			if (null != oldOneId) {
				oldRelation.put(oldOneId, attributeOneId);
			}
			if (null != oldTwoId) {
				oldRelation.put(oldTwoId, attributeTwoId);
			}
			if (null != oldThreeId) {
				oldRelation.put(oldThreeId, attributeThreeId);
			}
			if (null != oldFourId) {
				oldRelation.put(oldFourId, attributeFourId);
			}
			//set the attributes according to the new attributes and sequence of attributes
			if (null != newOneId) {
				attributeOneId = (String) oldRelation.get(oldOneId);
				if (null == attributeOneId) {
					attributeOneId = defaultOneId;
				}
			}
			if (null != newTwoId) {
				attributeTwoId = (String) oldRelation.get(oldTwoId);
				if (null == attributeTwoId) {
					attributeTwoId = defaultTwoId;
				}
			}
			if (null != newThreeId) {
				attributeThreeId = (String) oldRelation.get(oldThreeId);
				if (null == attributeThreeId) {
					attributeThreeId = defaultThreeId;
				}
			}
			if (null != newFourId) {
				attributeFourId = (String) oldRelation.get(oldFourId);
				if (null == attributeFourId) {
					attributeFourId = defaultFourId;
				}
			}
		}
		//else do nothing
	} //END protected void changeEntryTypeAttributes(...)
} //END public abstract class Entrz implements DingsItem