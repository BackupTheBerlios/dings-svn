/*
 * AItemModel.java
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

import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.consts.MessageConstants;

import java.util.logging.Level; 

public abstract class AItemModel extends AModel {
	
	/**
	 * Writes a xml-string based that conforms to the
	 * xml-definition of ADings.
	 *
	 * @return String - this information about the vocabulary as a valid xml-string
	 */
	protected abstract String getXMLString();
	
	/**
	 * Validates an Id.
	 * 
	 * @param aPrefix - the Models prefix
	 * @return String error - a description of the error, or null if validation successful.
	 */
	public static String validateId(String aPrefix, String anId) {
		boolean isValid = true;
		int tempId = 0;
		if (anId.startsWith(aPrefix) == false) {
			isValid = false;
		}
		try {
			tempId = Integer.parseInt(anId.substring(aPrefix.length(), anId.length()));
		}
		catch(NumberFormatException nfe) {
			isValid = false;
		}
		if (tempId <= 0) {
			isValid = false;
		} 
		if (isValid) return null;
		return "The id \"" + anId + "\" is not valid. It must start with \"" + aPrefix + "\"and the number must be > 0.";
	} //END public static String validateId(String)
	
	/**
	 * Sends an AppEvent to show the validation errors, which are contained in a ArrayList.
	 * 
	 * @param errors
	 */
	protected void showValidationErrors(ArrayList errors) {
		parentController.handleAppEvent(MessageConstants.getShowErrorListEvent(errors
				, "The following validation errors occured"));
	} //END protected void showValidationError(ArrayList)
	
	/**
	 * Sends an AppEvent to set save neded.
	 */
	protected void sendSaveNeeded() {
		AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
		ape.setMessage(MessageConstants.S_SAVE_NEEDED);
		parentController.handleAppEvent(ape);
	} //END protected void sendSaveNeeded()
	
	/**
	 * Checks for changes in the GUI relative to the values in the model.
	 */
	protected abstract void checkChangeInGUI();

	//Overrides AModel
	public void handleAppEvent(AppEvent evt) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "handleAppEvent", evt.getMessage());
		}
		if (evt.isDataEvent()) {
			if (evt.getMessage().equals(MessageConstants.D_EDIT_VIEW_APPLY)) {
				updateModel();
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
		}
		else parentController.handleAppEvent(evt);
	}	//END public void handleAppEvent(AppEvent)
} //END public abstract class AItemModel extends AModel
