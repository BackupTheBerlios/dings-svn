/*
 * AItemModel.java
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
	 * Sends an AppEvent to set save neded.
	 */
	protected void sendSaveNeeded() {
		AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
		ape.setMessage(MessageConstants.Message.S_SAVE_NEEDED);
		parentController.handleAppEvent(ape);
	} //END protected void sendSaveNeeded()

	/**
	 * Checks for changes in the GUI relative to the values in the model.
	 */
	protected abstract void checkChangeInGUI();

	//Overrides AModel
	public void handleAppEvent(AppEvent evt) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "handleAppEvent", evt.getMessage().name());
		}
		if (evt.isDataEvent()) {
			if (evt.getMessage() == MessageConstants.Message.D_EDIT_VIEW_APPLY) {
				updateModel();
			}
			else if (evt.getMessage() == MessageConstants.Message.D_EDIT_VIEW_REVERT) {
				updateGUI();
			}
			else if (evt.getMessage() == MessageConstants.Message.D_EDIT_VIEW_CHECK_CHANGE) {
				checkChangeInGUI();
			}
			else if (evt.getMessage() == MessageConstants.Message.D_EDIT_VIEW_DELETE) {
				parentController.handleAppEvent(evt);
			}
		}
		else parentController.handleAppEvent(evt);
	} //END public void handleAppEvent(AppEvent)
} //END public abstract class AItemModel extends AModel
