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
import net.vanosten.dings.utils.Toolbox;

public class EntryTypeAttributeItem {
	private Long id;

	/** This item's name */
	private String name;

	/** Is this default? */
	private boolean irregular = false; //default is not a valid identifier

	public EntryTypeAttributeItem(Long theId, String aName, boolean isIrregular) {
		this.id = theId;
		this.name = aName;
		this.irregular = isIrregular;
	} //END public EntryTypeAttributeItem(String, String, boolean, boolean)

	/**
	* Construct a new EntryTypeAttributeItem from scratch adding the necessary information.
	*/
	public static EntryTypeAttributeItem newItem() {
		return new EntryTypeAttributeItem(Toolbox.getInstance().nextId(), Constants.UNDEFINED, false);
	} //END public static EntryTypeAttributeItem NewEntryTypeItem()

	protected Long getId() {
		return id;
	}

	protected String getName() {
		return name;
	} //END protected String getName()

	/**
	 * @return String[] choiceProxy
	 */
	protected String[] getChoiceProxy() {
		String choiceProxy[] = {id.toString(), name};
		return choiceProxy;
	} //END protected String[] getChoiceProxy()

	protected String getXMLString() {
		StringBuffer xml = new StringBuffer();
		xml.append("<").append(Constants.XML_ENTRYTYPE_ATTRIBUTE_ITEM);
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ID, id.toString()));
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
		Long objId = null;
		String objName = null;
		boolean objIrregular = false;
		try {
			objId = ((Long)theObjectItem[0]);
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
