/*
 * Unit.java
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

import net.vanosten.dings.consts.Constants;

import java.util.logging.Logger;

public final class Unit extends AUnitCategory {
    
	/** Defines the maximal number of an item until now */
	private static int maxId = 0;
    
    public Unit(String anId, String aLastUpd, String aName, String aDescription) {
    	super(anId, aLastUpd, aName, aDescription);
    	setMaxId(anId);        
		logger = Logger.getLogger("net.vanosten.dings.model.Unit");
    } //END public Unit(String, String, String)
		
	/**
	 * Checks and sets the highest Id
	 */
	private static void setMaxId(String thisId) {
		maxId = Math.max(maxId, Integer.parseInt(thisId.substring(Constants.PREFIX_UNIT.length(),thisId.length())));
	} //END private static void setMaxId(string)

	/** 
	 * Returns a valid id for a new item
	 */
	private static String getNewId() {
		maxId++;
		return (Constants.PREFIX_UNIT + maxId);
	} //END private static String getNewId()
	
	/**
	 * Reset the max Id to 0.
	 * E.g. used when creating a new vocabulary after another vocabulary had been opened.
	 */
	protected static void resetMaxId() {
		maxId = 0;
	} //END protected static void resetMaxId()

    /**
     * Construct a new unit from scratch adding the necessary information.
     */
    protected static Unit newItem(boolean isDefault) {
		if (isDefault) {
			return new Unit(getNewId(), null, "Default", Constants.EMPTY_STRING);
		}
        return new Unit(getNewId(), null, Constants.UNDEFINED, Constants.EMPTY_STRING);
    } //END protected static Unit newItem()
    
    //Implements AItemModel.
    protected String getXMLString() {
        StringBuffer xml = new StringBuffer();
        xml.append("<").append(Constants.XML_UNIT);
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_ID, id));
		xml.append(Constants.getXMLFormattedAttribute(Constants.XML_ATTR_LAST_UPD, this.getLastUpdString()));
		xml.append(">");
        xml.append(Constants.getXMLTaggedValue(Constants.XML_NAME, name));
        xml.append(Constants.getXMLTaggedValue(Constants.XML_DESCRIPTION, description));
        xml.append("</").append(Constants.XML_UNIT).append(">");
        return xml.toString();
    } //END protected String getXMLString()
       
    //implements AUnitCategory
    protected ArrayList validateIt(String anId, String aName) {
    	return validate(anId, aName);
    } //END protected ArrayList validateIt(String, String)

    
    //implements AUnitCategory
    public static ArrayList validate(String anId, String aName) {
    	ArrayList errors = new ArrayList();
    	String idError = validateId(Constants.PREFIX_UNIT, anId);
    	if (null != idError) errors.add(idError);
		if (1 > aName.length()) {
			errors.add("Name may not be empty");
		}
    	return errors;
    } //END public static ArrayList validate(String, String)
} //END public class Unit extends AUnitCategory

