/*
 * AUnitCategory.java
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

import java.awt.Color;
import java.util.List;

import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.uiif.IUnitEditView;
import net.vanosten.dings.utils.Util;

/**
 * Implements as much of the common fields and methods of Unit and Category as possible.
 */
public abstract class AUnitCategory extends AIdItemModel {

	/** This unit's name */
	protected String name;

	/** The description */
	protected String description;
	
	/** The color to display */
	protected Color color;

	/** The edit view */
	protected IUnitEditView editView;

	public AUnitCategory(String anId, String aLastUpd, String aName, String aDescription, Color aColor) {
		this.id = anId;
		this.setLastUpd(aLastUpd);

		if (null == aName) {
			this.name = Constants.UNDEFINED;
		} else {
			this.name = aName;
		}

		if (null == aDescription) {
			this.description = Constants.EMPTY_STRING;
		} else {
			this.description = aDescription;
		}
		
		this.color = aColor;
	} //END public AUnitCategory(...)

	/**
	 * Returns this unit as an array containing a String with the ID and the name
	 *
	 * @return String[][] choiceProxy
	 */
	protected String[] getChoiceProxy() {
		String choiceProxy[] = {id, name, description};
		return choiceProxy;
	} //END protected String[] getChoiceProxy()

	protected Object[] getTableDisplay(){
		Object[] display = {id, name, description};
		return display;
	} //END protected Object[] getTableDisplay()


	protected static String[] getTableDisplayTitles() {
		String[] titles = {"Name", "Description"};
		return titles;
	} //END protected static String[] getTableDisplayTitles()

	protected static boolean[] getTableColumnFixedWidth() {
		boolean fixed[] = {false, false};
		return fixed;
	} //END protected static boolean[] getTableColumnFixedWidth()

	/**
	 * Lets you set the edit view
	 *
	 * @param ICategoryEditView aView
	 */
	protected void setEditView(IUnitEditView aView)  {
		editView = aView;
	} //END protected void setEditView(IUnitEditView)

	//implements AItemModel
	protected void releaseViews() {
		editView = null;
	} //protected void releaseViews()

	/**
	 * Hides the static method validate(String, String) in the subcalsses
	 */
	protected abstract List<String> validateIt(String anId, String aName);

	//Implements AModel.
	protected void updateModel() {
		//get values from editView and trim them
		String nameV = editView.getName().trim();
		//validate where necessary
		List<String> errors = validateIt(id, nameV);
		//if validation is ok, save the new values.
		if (0 ==  errors.size()) {
			//validated values
			name = nameV;
			//not validated values
			description = editView.getDescription().trim();
			//save needed and reset
			sendSaveNeeded();
			updateGUI();
		}
	} //END private void updateModel()

	//Implements AModel
	protected void updateGUI() {
		editView.setName(name);
		editView.setDescription(description);

		//visual feedback
		editView.setEditing(false, true);
		editView.setNameIsValueValid(true);
	 } //END protected void updateGUI()

	//Implements AItemModel
	protected void checkChangeInGUI() {
		boolean isValid = validateString(editView.getName(), 1);
		if (editView.getName().trim().equals(name) &&
				(editView.getDescription().trim().equals(description))) {
			editView.setEditing(false, isValid);
		}
		else {
			editView.setEditing(true, isValid);
		}
		//validation
		editView.setNameIsValueValid(isValid);
	} //END protected void checkChangeInGUI()

	protected String constructXMLString(String xmlName) {
		StringBuffer xml = new StringBuffer();
		xml.append("<").append(xmlName);
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ID, id));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_LAST_UPD, this.getLastUpdString()));
		xml.append(">");
		xml.append(Constants.getXMLTaggedValue(Constants.XML_NAME, name));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_DESCRIPTION, description));
		String rgb = Util.convertRGB(color);
		if (null != rgb) {
			xml.append(Constants.getXMLTaggedValue(Constants.XML_COLOR, rgb));
		}
		xml.append("</").append(xmlName).append(">");
		return xml.toString();
	} //END protected String constructXMLString()

} //END public class AUnitCategory extends AIdItemModel

