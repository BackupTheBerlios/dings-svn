/*
 * ACollection.java
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import net.vanosten.dings.uiif.IListView;
import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.consts.MessageConstants.Message;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.event.IAppEventHandler;

public abstract class ACollection implements IAppEventHandler {

	/** Is actually the container holding all items */
	protected Map items = null;

	protected IListView listView = null;

	protected IAppEventHandler parentController = null;

	/** The name of the XML tag for this collection */
	protected String tagName;

	/** The message for getting the list view */
	protected Message msgListView = null;

	/**
	 * The empty constructor.
	 */
	public ACollection(IAppEventHandler anEventHandler) {
		this.parentController = anEventHandler;
		setTagName();
		setMessageListView();
		items = new HashMap();
	} //END public ACollection;

	/**
	 * Sets the name of the tag.
	 */
	protected abstract void setTagName();

	/**
	 * Sets the name of the list view message.
	 */
	protected abstract void setMessageListView();

	/**
	 * Sets the items and and does some processing to prepare the items for use (e.g. max Id).
	 *
	 * @param HashMap units
	 */
	protected abstract void setItems(Map items);

	/**
	 * The collection as an XML string.
	 */
	protected String getXMLString() {
		StringBuffer xml = new StringBuffer();

		xml.append("<").append(tagName).append(">").append(Constants.getLineSeparator());

		//run through collection
		Set keys = items.keySet();
		Iterator iter = keys.iterator();
		AItemModel item;
		while (iter.hasNext()) {
			item = (AItemModel)items.get(iter.next());
			xml.append(item.getXMLString()).append(Constants.getLineSeparator());
		}

		xml.append("</").append(tagName).append(">");
		return xml.toString();
	} //END protected String getXMLString()

	/**
	 * Sets the current item.
	 *
	 * @param String anID - the ID of the unit
	 */
	protected abstract void setCurrentItem(String anID);

	/*
	 * Tries to find the neighbour of an item, which is going to be deleted.
	 *
	 * @returns String - the id of a neighbour or null if the list will be empty
	 */
	protected abstract String selectNewCurrent(String anId);

	/**
	 * Removes an Id or item from another collection besides the items
	 */
	protected abstract void removeInOther(String anId);

	/**
	 * Deletes an item from a request in an edit view.
	 * Has to call deleteItem(String, boolean) with the currentItem.getId() and "false".
	 */
	protected abstract void deleteItem();

	/**
	 * Controls whether it is allowed to delete the item.
	 * E.g. there has to be at least one unit and the unit may not be used in any Entry.
	 */
	protected abstract boolean checkDeleteAllowed(String anId);

	//implements ACollection
	protected void deleteItem(String anId, boolean inList) {
		if (checkDeleteAllowed(anId)) {
			 //Find the category before in List
			String newCurrent = selectNewCurrent(anId);
			//remove
			items.remove(anId);
			removeInOther(anId);
			//set current item
			setCurrentItem(newCurrent);
			//save needed
			sendSaveNeeded();
			//if inList is false, then show list
			if (false == inList) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(msgListView);
				parentController.handleAppEvent(ape);
			}
			else {
				refreshListView();
			}
		}
		//else do nothing. The implementation of checkDeleteAllowed() should show error message
	} //END public deleteItem(String, boolean)

	protected void editItem(String anID) {
		setCurrentItem(anID);
	} //END protected void editItem(String)

	/**
	 * Add a new item.
	 * @param aType - which type it is (only for Entries, null otherwise)
	 * @param isDefault - whether this is a default item (only for selection items -> Unit, Category, EntryType)
	 */
	protected abstract void newItem(String aType, boolean isDefault);

	/**
	 * Sets the list for the listview
	 */
	protected abstract void refreshListView();

	/**
	 * Handles AppEvents
	 */
	public abstract void handleAppEvent(AppEvent evt);

	/**
	 * Counts the number of entry types in this collection.
	 *
	 * @return int the number of elements.
	 */
	protected int countElements() {
		return items.size();
	} //END protected int countElements()

	protected void setListView(IListView aListView) {
		this.listView = aListView;
	} //END protected void setListView(IListView)

	protected void sendSaveNeeded() {
		AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
		ape.setMessage(MessageConstants.Message.S_SAVE_NEEDED);
		parentController.handleAppEvent(ape);
	} //END protected void sendSaveNeeded()
} //END public abstract class ACollection

