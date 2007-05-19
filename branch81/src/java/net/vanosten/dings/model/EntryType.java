/*
 * EntryType.java
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

import java.util.ArrayList;
import java.util.List;

import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.uiif.IEntryTypeEditView;

import java.util.logging.Logger;

public class EntryType extends AIdItemModel {
	private String name;

	private String[] attributeIds;

	/** Defines the maximal number of an item until now */
	private static int maxId = 0;

	/** Defines the number of attributes */
	public final static int NUMBER_OF_ATTRIBUTES = 4;

	/** A pointer to the available attributes */
	private EntryTypeAttributesCollection attributes;

	/** The edit view */
	private IEntryTypeEditView editView;

	public EntryType(String aName, String anId, String[] theAttributeIds, String aLastUpd) {
		logger = Logger.getLogger("net.vanosten.dings.model.EntryType");
		setMaxId(anId);
		this.id = anId;
		this.name = aName;
		this.attributeIds = theAttributeIds;
		this.setLastUpd(aLastUpd);
	} //END public EntryType(...)

	/**
	 * Checks and sets the highest Id
	 */
	private static void setMaxId(String thisId) {
		maxId = Math.max(maxId, Integer.parseInt(thisId.substring(Constants.PREFIX_ENTRYTYPE.length(),thisId.length())));
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
		return (Constants.PREFIX_ENTRYTYPE + maxId);
	} //END private static String getNewId()

	protected static EntryType newItem(boolean isDefault) {
		if (isDefault) {
			return new EntryType( "Default", getNewId(), new String[NUMBER_OF_ATTRIBUTES], null);
		}
		return new EntryType(Constants.EMPTY_STRING, getNewId(), new String[NUMBER_OF_ATTRIBUTES], null);
	} //END protected static EntryType newItem(boolean)

	//Implements AItemModel
	protected String getXMLString() {
		StringBuffer xml = new StringBuffer();

		xml.append("<").append(Constants.XML_ENTRYTYPE);
		//common attributes
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ID, id));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_NAME, name));
		//attributes
		if (null != attributeIds[0]) {
			xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ATTRIBUTEONE, attributeIds[0]));
		}
		if (null != attributeIds[1]) {
			xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ATTRIBUTETWO, attributeIds[1]));
		}
		if (null != attributeIds[2]) {
			xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ATTRIBUTETHREE, attributeIds[2]));
		}
		if (null != attributeIds[3]) {
			xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ATTRIBUTEFOUR, attributeIds[3]));
		}
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_LAST_UPD, this.getLastUpdString()));
		xml.append(">");
		xml.append("</").append(Constants.XML_ENTRYTYPE).append(">");
		return xml.toString();
	} //END protected String getXMLString(String)

	protected String getName() {
		return name;
	} //END protected String getName()

	/**
	 * Used to display in a set of choices for EntryTypes. E.g. when the EntryType of a new Entry has to be chosen.
	 */
	protected String[] getChoiceProxy() {
		String choiceProxy[] = {id, name};
		return choiceProxy;
	} //END protected String[] getChoiceProxy()

	protected Object[] getTableDisplay(){
		Object[] display = {id, name, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING};
		for (int i = 0; i < NUMBER_OF_ATTRIBUTES; i++) {
			if (null != attributeIds[i]) {
				display[i + 2] = attributes.getEntryTypeAttribute(attributeIds[i]).getName();
			}
		}
		return display;
	} //END protected Object[] getTableDisplay()

	/**
	 * Get theEntryTypeAttribute of a specific position.
	 * Null is retruned if the position is not available.
	 *
	 * @param int - the position of the attribute
	 * @return EntryTypeAttribute
	 */
	protected EntryTypeAttribute getAttribute(int aNumber) {
		if (aNumber < 1 || aNumber > this.getNumberOfAttributes()) {
			return null;
		}
		String anId = attributeIds[aNumber - 1];
		return attributes.getEntryTypeAttribute(anId);
	} //END protected EntryTypeAttribute getAttribute(int)

	protected static String[] getTableDisplayTitles() {
		String[] titles = {"Name", "Attribute 1", "Attribute 2", "Attribute 3", "Attribute 4"};
		return titles;
	} //END protected static String[] getTableDisplayTitles()

	protected static boolean[] getTableColumnFixedWidth() {
		boolean fixed[] = {true, false, false, false, false};
		return fixed;
	} //END protected static boolean[] getTableColumnFixedWidth()

	//implements AItemModel
	protected void releaseViews() {
		editView = null;
	} //END protected void releaseViews()

	/**
	 * Tests the required fields for valid contents.
	 *
	 * @return List<String< - a list of validation errors. Size() = 0 means valid model.
	 */
	public static List<String> validate(String anId, String aName) {
		List<String> errors = new ArrayList<String>();
		String idError = validateId(Constants.PREFIX_ENTRYTYPE, anId);
		if (null != idError) errors.add(idError);
		if (false == validateString(aName,1)) {
			errors.add("Name may not be empty");
		}
		return errors;
	} //END public static ArrayList validate(String, String)

	//implements AItemModel
	protected void updateModel() {
		//get values from editView and trim them
		String nameV = editView.getName().trim();
		//validate where necessary
		List<String> errors = validate(id, nameV);
		//if validation is ok, save the new values.
		if (0 ==  errors.size()) {
			//update all entries
			//validated values
			name = nameV;

			//not validated values
			String[] attributeIdsV = editView.getAttributes();

			StringBuffer sb = new StringBuffer();
			sb.append(id);
			int i; //an index in arrays to save some memory and GC
			//append old ids
			for (i = 0; i < NUMBER_OF_ATTRIBUTES; i++) {
				sb.append(Constants.DELIMITTER_APP_EVENT);
				if (null != attributeIds[i]) {
					sb.append(attributeIds[i]);
				}
				else sb.append(Constants.NULL_STRING);
			}
			//rearrange new ids without nulls in the middle
			List<String> newAttribIds = new ArrayList<String>(NUMBER_OF_ATTRIBUTES);
			for (i = 0; i < NUMBER_OF_ATTRIBUTES; i++) {
				if (null != attributeIdsV[i]) {
					newAttribIds.add(attributeIdsV[i]);
				}
			}
			//set the attribute ids
			attributeIds = new String[NUMBER_OF_ATTRIBUTES];
			for (i = 0; i < newAttribIds.size(); i++) {
				attributeIds[i] = (String) newAttribIds.get(i);
			}
			//append new ids
			for (i = 0; i < NUMBER_OF_ATTRIBUTES; i++) {
				sb.append(Constants.DELIMITTER_APP_EVENT);
				if (null != attributeIds[i]) {
					sb.append(attributeIds[i]);
				}
				else sb.append(Constants.NULL_STRING);
			}
			//append default item ids
			String defaultItemId = null;
			for (i = 0; i < NUMBER_OF_ATTRIBUTES; i++) {
				sb.append(Constants.DELIMITTER_APP_EVENT);
				if (null != attributeIds[i]) {
					defaultItemId = attributes.getEntryTypeAttribute(attributeIds[i]).getDefaultItem();
					sb.append(defaultItemId);
				}
				else sb.append(Constants.NULL_STRING);
			}
			//send AppEvent to update all Entries
			AppEvent evt = new AppEvent(AppEvent.EventType.DATA_EVENT);
			evt.setMessage(MessageConstants.Message.D_ENTRY_TYPE_CHANGE_ATTRIBUTES);
			evt.setDetails(sb.toString());
			parentController.handleAppEvent(evt);
			//save needed and reset
			sendSaveNeeded();
			updateGUI();
		}
	} //END protected void updateModel()

	//Implements AItemModel
	protected void checkChangeInGUI() {
		//check for changes in attributes
		//must check fo null first because null.equals(something) throws NullPointerException
		String[] theIds = editView.getAttributes();
		boolean attribUnchanged = true;
		for (int i = 0; i < NUMBER_OF_ATTRIBUTES; i++) {
			if (null == attributeIds[i]) {
				if (null != theIds[i]) {
					attribUnchanged = false;
				}
			}
			else {
				if (false == attributeIds[i].equals(theIds[i])) {
					attribUnchanged = false;
				}
			}
		}
		//determine overall change
		boolean isValid = validateString(editView.getName(), 1);
		if (editView.getName().trim().equals(name) && attribUnchanged) {
			editView.setEditing(false, isValid);
		}
		else {
			editView.setEditing(true, isValid);
		}
		//validation
		editView.setNameIsValueValid(isValid);
	} //END protected void checkChangeInGUI()

	/**
	 * Lets you set the edit view
	 *
	 * @param IEntryTypeEditView aView
	 */
	protected void setEditView(IEntryTypeEditView aView)  {
		editView = aView;
	} //END protected void setEditView(IEntryTypeEditView)

	//implements AItemModel
	protected void updateGUI() {
		editView.setName(name);
		editView.setAttributeChoices(attributes.getChoiceProxy());
		editView.setAttributes(attributeIds);
		editView.setEditing(false, true);
		editView.setNameIsValueValid(true);
	} //END protected void resetView()

	/**
	 * The number of assigned attributes is determined by
	 * checking, if the id pointers are not null.
	 *
	 * @return int
	 */
	protected int getNumberOfAttributes() {
		int noa = 0;
		if (null != attributeIds[0]) {
			noa = 1;
		}
		if (null != attributeIds[1]) {
			noa = 2;
		}
		if (null != attributeIds[2]) {
			noa = 3;
		}
		if (null != attributeIds[3]) {
			noa = 4;
		}
		return noa;
	} //END protected int getNumberOfAttribute()

	/**
	 * Sets the EntryTypeAttributes for reference
	 */
	protected void setEntryTypeAttributes(EntryTypeAttributesCollection theAttributes) {
		attributes = theAttributes;
	} //END protected void setEntryTypeAttributes(EntryTypeAttributesCollection)
} //END public class EntryType extends AIdItemModel
