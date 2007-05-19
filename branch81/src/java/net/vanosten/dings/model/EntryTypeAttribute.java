/*
 * EntryTypeAttribute.java
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.vanosten.dings.uiif.IEntryTypeAttributeEditView;
import net.vanosten.dings.consts.Constants;

import java.util.logging.Logger;

public final class EntryTypeAttribute extends AIdItemModel {

	/** This entryTypeAttribute's name */
	private String name;

	/** The id of the default item */
	private String defaultItem;

	/** All related EntryTypeAttributeItems in an Array */
	private EntryTypeAttributeItem items[];

	/** Defines the maximal number of an EntryTypeAttribute until now */
	private static int maxId = 0;

	/** A pointer to the entries */
	private EntriesCollection entries;

	/** The edit view */
	private IEntryTypeAttributeEditView editView;

	public EntryTypeAttribute(String anId, String aName, String aDefaultItem, String aLastUpd
							  , EntryTypeAttributeItem theItems[]) {
		setMaxId(anId);
		this.id = anId;

		if (null == aName) this.name = Constants.UNDEFINED;
		else this.name = aName;
		setLastUpd(aLastUpd);

		//check for existing items and set defaultItem
		if (null == theItems || 0 == theItems.length) {
			items = new EntryTypeAttributeItem[1];
			EntryTypeAttributeItem newItem = EntryTypeAttributeItem.newItem(true);
			items[0] = newItem;
			defaultItem = newItem.getId();
		}
		else {
			items = theItems;
			//check correct default value
			boolean existing = false; //whether the input defaultITem really exists
			for (int i = 0; i < items.length; i++) {
				if (items[i].getId().equals(aDefaultItem)) {
					existing = true;
					break;
				}
			}
			if (true == existing) {
				defaultItem = aDefaultItem;
			}
			else {
				//take the first available EntryTypeAttributeItem
				defaultItem = items[0].getId();
			}
		}

		logger = Logger.getLogger("net.vanosten.dings.model.EntryTypeAttribute");
	} //END public EntryTypeAttribute(String, String, EntryTypeAttributeItem[], String)

	/**
	 * Checks and sets the highest Id
	 */
	private static void setMaxId(String thisId) {
		maxId = Math.max(maxId, Integer.parseInt(thisId.substring(Constants.PREFIX_ENTRTYPE_ATTRIBUTE.length(),thisId.length())));
	} //END private static void setMaxId(string)

	/**
	 * Returns a valid id for a new item
	 */
	private static String getNewId() {
		maxId++;
		return (Constants.PREFIX_ENTRTYPE_ATTRIBUTE + maxId);
	} //END private static String getNewId()

	/**
	 * Reset the max Id to 0.
	 * E.g. used when creating a new vocabulary after another vocabulary had been opened.
	 */
	protected static void resetMaxId() {
		maxId = 0;
	} //END protected static void resetMaxId()

	/**
	 * Construct a new EntryTypeAttribute from scratch adding the necessary information.
	 */
	protected static EntryTypeAttribute newItem(boolean isDefault) {
		EntryTypeAttributeItem newItems[] = new EntryTypeAttributeItem[1];
		EntryTypeAttributeItem newItem = EntryTypeAttributeItem.newItem(isDefault);
		newItems[0] = newItem;
		if (isDefault) {
			return new EntryTypeAttribute(getNewId(), "Default", newItem.getId(), null, newItems);

		}
		return new EntryTypeAttribute(getNewId(), Constants.EMPTY_STRING, newItem.getId(), null, newItems);
	} //END protected static EntryTypeAttribute newItem(boolean)

	/**
	 * Implements AItemModel.
	 */
	protected String getXMLString() {
		StringBuffer xml = new StringBuffer();
		xml.append("<").append(Constants.XML_ENTRYTYPE_ATTRIBUTE);
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ID, id));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_NAME, name));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_DEFAULTITEM, defaultItem));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_LAST_UPD, this.getLastUpdString()));
		xml.append(">");
		for (int i = 0; i < items.length; i++) {
			xml.append(items[i].getXMLString());
		}
		xml.append("</").append(Constants.XML_ENTRYTYPE_ATTRIBUTE).append(">");
		return xml.toString();
	} //END protected String getXMLString()

	/**
	 * Sets a pointer to the entries collection
	 * @param EntriesCollection theEntries
	 */
	protected void setEntries(EntriesCollection theEntries) {
		this.entries = theEntries;
	} //ENd protected void setEntries(EntriesCollection)

	/**
	 * Returns this unit as an array containing a String with the ID and the name
	 *
	 * @return String[][] choiceProxy
	 */
	protected String[] getChoiceProxy() {
		String choiceProxy[] = {id, name};
		return choiceProxy;
	} //END protected String[] getChoiceProxy()

	protected Object[] getTableDisplay(){
		Object[] display = {id, name};
		return display;
	} //END protected Object[] getTableDisplay()

	protected static String[] getTableDisplayTitles() {
		String[] titles = {"Name"};
		return titles;
	} //END protected static String[] getTableDisplayTitles()

	protected static boolean[] getTableColumnFixedWidth() {
		boolean fixed[] = {false};
		return fixed;
	} //END protected static boolean[] getTableColumnFixedWidth()

	/**
	 * To display the items of this attribute in a list or combobox.
	 *
	 * @return
	 */
	protected String[][] getItemsChoiceProxy() {
		String proxy[][] = new String[items.length][2];
		for (int i = 0; i < items.length; i++) {
			proxy[i] = items[i].getChoiceProxy();
		}
		return proxy;
	} //END protected String[][] getItemsChoiceProxy()

	/**
	 * Lets you set the edit view
	 *
	 * @param IEntryTypeAttributeEditView aView
	 */
	protected void setEditView(IEntryTypeAttributeEditView aView)  {
		editView = aView;
	} //END protected void setEditView(IEntryTypeAttributeEditView)

	//implements AItemModel
	protected void releaseViews() {
		editView = null;
	} //protected void releaseViews()

	/**
	 * Tests the required fields for valid contents.
	 *
	 * @return List<String> - a list of validation errors. Size() = 0 means valid model.
	 */
	public static List<String> validate(String anId, String aName) {
		List<String> errors = new ArrayList<String>();
		String idError = validateId(Constants.PREFIX_ENTRTYPE_ATTRIBUTE, anId);
		if (null != idError) errors.add(idError);
		if (1 > aName.length()) {
			errors.add("Name may not be empty");
		}
		return errors;
	} //END public static List<String> validate(String, String)

	private List<String> validate(String anId
									, String aName
									, EntryTypeAttributeItem[] someItems
									, String aDefaultItem) {
		//initialize errors by testing the id and name
		List<String> errors = validate(anId, aName);
		if (false == validateString(aName, 1)) {
			errors.add("Name may not be empty");
		}
		//The test for number of items and default are not necessary in the GUI, because
		//EntryTypeAttributeEditView takes care of this by disabling the deleteItemB
		//check that there is at least one item
		if (null == someItems || 1 > someItems.length) {
			errors.add("There must be at least one item");
		} else {
			if (false == validateItemsHaveDefault(someItems, aDefaultItem)) {
				errors.add("The default item does not exist");
			}
			if (null != validateItemNames(someItems)) {
				errors.add("The items must have unique names");
			}
		}
		return errors;
	} //END private ArrayList validate(...)

	/**
	 * Validates that the default item actually exists
	 * @param someItems
	 * @param aDefaultItem
	 * @return false if the id of the default item is not one of the ids in the items
	 */
	private boolean validateItemsHaveDefault(EntryTypeAttributeItem[] someItems, String aDefaultItem) {
		for (int i = 0; i < someItems.length; i++) {
			if (aDefaultItem.equals(someItems[i].getId())) {
				return true;
			}
		}
		return false;
	} //END private boolean validateItemsHaveDefault(EntryTypeAttributeItem[]

	/**
	 * Validates a set of EntryTypeAttributeItems
	 * @param someItems
	 * @return The ID of an item if its name is null, an empty String or has the same name as another item
	 */
	private String validateItemNames(EntryTypeAttributeItem[] someItems) {
		Set<String> theSet = new HashSet<String>(someItems.length);
		theSet.add(null); //make sure no other name is null
		theSet.add(Constants.EMPTY_STRING); //make sure no other name is empty
		for (int i = 0; i < someItems.length; i++) {
			if (false == theSet.add(someItems[i].getName().trim())) {
				return someItems[i].getId();
			}
		}
		return null;
	} //END private String validateItemNames(EntryTypeAttributeItem[]

	//implements AItemModel.
	protected void updateModel() {
		//get values from editView and trim them
		String nameV = editView.getName().trim();
		String defaultItemV = editView.getDefaultItem().trim();
		EntryTypeAttributeItem[] itemsV = translateToAttributeItems(editView.getItems());
		//validate where necessary
		List<String> errors = validate(id, nameV, itemsV, defaultItemV);
		//check that no items are deleted, that actually are used in entries
		boolean found = false;
		for (int i = 0; i < items.length; i++) {
			found = false;
			for (int j = 0; j < itemsV.length; j++) {
				if (items[i].getId().equals(itemsV[j].getId())) {
					found = true;
					break;
				}
			}
			if (false == found) {
				if (entries.isItemUsed(items[i].getId())) {
					errors.add("The item may not be deleted, because it is in use: \""
							+ items[i].getName()
							+ "\". Sorry, you have to press the Reset button to correct this.");
				}
			}
		}

		//if validation is ok, save the new values.
		if (0 ==  errors.size()) {
			//validated values
			name = nameV;
			defaultItem = defaultItemV;
			items = itemsV;
			//not validated values
			//(none)
			//save needed and reset
			sendSaveNeeded();
			updateGUI();
		}
	} //END private void updateModel()

	//Implements AItemModel
	protected void updateGUI() {
		editView.setName(name);
		editView.setItems(tranlateFromAttributeItems()); //items have to be set before default item
		editView.setDefaultItem(defaultItem);
		editView.setItemTableNotEdited();

		//visual feedback
		editView.setEditing(false, true);
		editView.setNameIsValueValid(true);
	 }	//END protected void updateGUI()

	//Implements AItemModel
	protected void checkChangeInGUI() {
		boolean isValid = validateString(editView.getName(), 1)
			&& (null == validateItemNames(translateToAttributeItems(editView.getItems())));
		if ((editView.getName().trim().equals(name))
			&& (editView.getDefaultItem().trim().equals(defaultItem))
			&& (false == editView.isItemTableEdited())) {
			editView.setEditing(false, isValid);
		}
		else {
			editView.setEditing(true, isValid);
		}
		//validation
		editView.setNameIsValueValid(validateString(editView.getName(), 1));
		editView.setItemNameIsValueValid(validateItemNames(translateToAttributeItems(editView.getItems())));
	} //END protected void checkChangeInGUI()

	protected String getName() {
		return name;
	} //END protected String getName()

	protected String getDefaultItem() {
		return defaultItem;
	} //END protected String getDefaultItem()

	/**
	 * Constructs EntryTypeAttributeItems from a two-dimensional
	 * String array by calling a static function in EntryTypeAttributItem.
	 */
	private EntryTypeAttributeItem[] translateToAttributeItems(Object[][] theObjectItems) {
		EntryTypeAttributeItem[] returnItems = new EntryTypeAttributeItem[theObjectItems.length];
		for (int i = 0; i < returnItems.length; i++) {
			returnItems[i] = EntryTypeAttributeItem.translateFromObjectArray(theObjectItems[i]);
		}
		return returnItems;
	} //END private EntryTypeAttributeItem[] translateToAttributeItems(String[][])

	private Object[][] tranlateFromAttributeItems() {
		Object[][] returnObj = new Object[items.length][];
		for (int i = 0; i < returnObj.length; i++) {
			returnObj[i] = items[i].getTableDisplay();
		}
		return returnObj;
	} //END private Object[][] tranlateFromAttributeItems()
} //END public class EntryTypeAttribute extends AIdItemModel

