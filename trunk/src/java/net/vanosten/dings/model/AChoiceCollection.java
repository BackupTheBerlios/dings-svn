/*
 * AChoiceCollection.java
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

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.event.AppEvent;

public abstract class AChoiceCollection extends ACollection {
	
	/** The message for getting the edit view */
	protected String msgEditView = null;
	
	/** A pointer to teh EntriesCollection */
	protected EntriesCollection entries = null;
	
	public AChoiceCollection(IAppEventHandler anEventHandler) {
		super(anEventHandler);
		setMessageEditView();
	}	//End public AChoiceCollection(IAppEventHandler)
	
	/**
	 * Sets the name of the edit view message.
	 */
	protected abstract void setMessageEditView();
	
	/**
	 * Sets the pointer to the EntriesCollection.
	 */
	protected void setEntries(EntriesCollection theEntries) {
		this.entries = theEntries;
	} //END protected void setEntries(EntriesCollection)
	
	//Implements ACollection
	protected String selectNewCurrent(String anId) {
		ArrayList foo = new ArrayList(items.keySet());
		int pos = foo.indexOf(anId);
		if (pos > 0) {
			return (String)foo.get(pos -1);
		} 
		else if ((0 == pos) && (foo.size() > 1)) {
			return (String)foo.get(1);
		}
		else {
			return null;
		} 
	} //END protected String selectNewCurrent()
		
	//Implements ACollection
	protected void removeInOther(String anId) {
		//Do nothing for AChoiceCollection subclasses
	} //END protected void removeInOther(String)
	
	//Implements ACollection
	protected boolean checkDeleteAllowed(String anId) {
		//there has to be at least one item
		//this should be handled by ListView.java
		if (1 >= items.size()) {
			//send AppEvent
			AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
			ape.setMessage(MessageConstants.S_SHOW_DELETE_ERROR);
			ape.setDetails("There has to be at least one item!");
			parentController.handleAppEvent(ape);
			return false;
		}
		//the item may not be used in any Entry
		if (entries.isItemUsed(anId)) {
			//send AppEvent
			AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
			ape.setMessage(MessageConstants.S_SHOW_DELETE_ERROR);
			ape.setDetails("The item is used in one or several entries!");
			parentController.handleAppEvent(ape);
			return false;
		}
		return true;
	} //END protected boolean checkDeleteAllowed(String)
	
	/**
	 * Set a default item. If there already are one or several items, then nothing happens.
	 */
	protected void setDefaultItem() {
		//prevent settig a default when there actually are elements
		if (0 == items.size()) {
			newItem(null, true);
		}
	} //END protected void setDefaultItem()

	//implements ACollection
	public void handleAppEvent(AppEvent evt) {
		//if (logger.isDebugEnabled()) {
			//logger.debug("handleAppEvent(): " + evt.getMessage() + "; " + evt.getDetails());
		//}
		if(evt.isDataEvent()) {
			if (evt.getMessage().equals(MessageConstants.D_LIST_VIEW_NEW)) {
				newItem(null, false);
				//send navigation event
				AppEvent ape = new AppEvent(AppEvent.NAV_EVENT);
				ape.setMessage(msgEditView);
				parentController.handleAppEvent(ape);  
			}
			else if (evt.getMessage().equals(MessageConstants.D_LIST_VIEW_DELETE)) deleteItem(evt.getDetails(), true);
			else if (evt.getMessage().equals(MessageConstants.D_EDIT_VIEW_DELETE)) deleteItem();
			else if (evt.getMessage().equals(MessageConstants.D_LIST_VIEW_REFRESH)) refreshListView();
		}
		else if (evt.isNavEvent()) {
			if (evt.getMessage().equals(msgEditView)) {
				editItem(evt.getDetails());
			}
			parentController.handleAppEvent(evt);
		}
		else parentController.handleAppEvent(evt);
	}	//END public void handleAppEvent(AppEvent)

	public abstract String[][] getChoiceProxy();

} //END public abstract class AChoiceCollection extends ACollection
