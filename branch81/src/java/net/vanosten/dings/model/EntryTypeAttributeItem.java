/*
 * EntryTypeAttributeItem.java
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

import net.vanosten.dings.consts.Constants;

public class EntryTypeAttributeItem {
	private String id;

	/** Defines the maximal number of an item until now */
	private static int maxId = 0;

	/** This item's name */
	private String name;

	/** Is this default? */
	private boolean irregular = false; //default is not a valid identifier

	public EntryTypeAttributeItem(String theId, String aName, boolean isIrregular) {
		setMaxId(theId);
		this.id = theId;
		this.name = aName;
		this.irregular = isIrregular;
	} //END public EntryTypeAttributeItem(String, String, boolean, boolean)

	/**
	* Construct a new EntryTypeAttributeItem from scratch adding the necessary information.
	*/
	public static EntryTypeAttributeItem newItem(boolean isDefault) {
		String theName = Constants.EMPTY_STRING;
		if (isDefault) {
			theName = "Default";
		}
		return new EntryTypeAttributeItem(getNewId(), theName, false);
	} //END public static EntryTypeAttributeItem NewEntryTypeItem()

	protected String getId() {
		return id;
	} //END protected String getId()

	protected String getName() {
		return name;
	} //END protected String getName()

	/**
	 * Returns a valid id for a new item
	 */
	private static String getNewId() {
		maxId++;
		return (Constants.PREFIX_ENTRYTYPE_ATTRIBUTE_ITEM + maxId);
	} //END private String getNewId()

	private static void setMaxId(String anId) {
		maxId = Math.max(maxId, Integer.parseInt(anId.substring(Constants.PREFIX_ENTRYTYPE_ATTRIBUTE_ITEM.length(),anId.length())));
	} //END private void setMaxId(String)

	/**
	 * Reset the max Id to 0.
	 * E.g. used when creating a new vocabulary after another vocabulary had been opened.
	 */
	protected static void resetMaxId() {
		maxId = 0;
	} //END protected static void resetMaxId()

	/**
	 * @return String[] choiceProxy
	 */
	protected String[] getChoiceProxy() {
		String choiceProxy[] = {id, name};
		return choiceProxy;
	} //END protected String[] getChoiceProxy()

	protected String getXMLString() {
		StringBuffer xml = new StringBuffer();
		xml.append("<").append(Constants.XML_ENTRYTYPE_ATTRIBUTE_ITEM);
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ID, id));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_IRREGULAR, Boolean.toString(irregular)));
		xml.append(">");
		xml.append(name);
		xml.append("</").append(Constants.XML_ENTRYTYPE_ATTRIBUTE_ITEM).append(">");
		return xml.toString();
	} //END protected String getXMLString()

	/**
	 * Translates an array of Object to an instance.
	 * It is assumed that the array has two Strings and a Boolean in the end.
	 * The function is the inverse to method <code>tranlateToObjectArray()</code>.
	 *
	 * @param Object[] theObjectItem
	 * @return EntryTypeAttributeItem - a new instance based on the (3) input objects
	 */
	protected static EntryTypeAttributeItem translateFromObjectArray(Object theObjectItem[]) {
		//get the id as String, the name as String and the irregular as Boolean
		String objId = null;
		String objName = null;
		boolean objIrregular = false;
		try {
			objId = ((String)theObjectItem[0]).trim();
			objName = ((String)theObjectItem[1]).trim();
			objIrregular = ((Boolean) theObjectItem[2]).booleanValue();
		}
		catch (Exception e) {
			//TODO: can we live with doing nothing?
		}
		return new EntryTypeAttributeItem(objId, objName, objIrregular);
	} //END protected static EntryTypeAttributeItem translateFromObjectArray(Object[])

	/**
	 * Translates this instance to an array of Objects.
	 * The function is the inverse to method <code>translateFromObjectArray(Object[])</code>.
	 *
	 * @return Object[] - this instance as an array of three objects (String, String, Boolean)
	 */
	public Object[] getTableDisplay() {
		Object[] returnObj = new Object[3];
		//no copies of obejcts needed for String and Boolean
		returnObj[0] = id;
		returnObj[1] = name;
		returnObj[2] = Boolean.valueOf(irregular);
		return returnObj;
	} //END public Object[] getTableDisplay()

	public static String[] getTableDisplayTitles() {
		String[] titles = {"Name", "Irregular"};
		return titles;
	} //END public static String[] getTableDisplayTitles()

	protected static boolean[] getTableColumnFixedWidth() {
		boolean fixed[] = {false, false};
		return fixed;
	} //END protected static boolean[] getTableColumnFixedWidth()
} //END public class EntryTypeAttributeItem
