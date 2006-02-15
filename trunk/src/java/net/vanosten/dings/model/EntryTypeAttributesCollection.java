/*
 * EntryTypeAttributesCollection.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2002, 03, 04, 2005 Rick Gruber-Riemer (rick@vanosten.net)
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

import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.event.IAppEventHandler;

public class EntryTypeAttributesCollection extends AChoiceCollection {
	/** The id for the actual EntryTypeAttribute */
	private EntryTypeAttribute currentItem = null;

	public EntryTypeAttributesCollection(IAppEventHandler anEventHandler) {
		super(anEventHandler);
	}	//END public EntryTypeAttributesCollection(IAppEventHandler)

	//implements ACollection
	protected void setTagName() {
		tagName = Constants.XML_ENTRYTYPE_ATTRIBUTES;
	} //END protected void setTagName()

	//implements ACollection
	protected void setMessageListView() {
		msgListView = MessageConstants.Message.N_VIEW_ENTRYTYPE_ATTRIBUTES_LIST;
	} //END protected void setMessageListView()

	//implements AChoiceCollection
	protected void setMessageEditView() {
		msgEditView = MessageConstants.Message.N_VIEW_ENTRYTYPE_ATTRIBUTE_EDIT;
	} //END protected void setMessageEditView()

	//implements ACollection
	protected void setItems(Map theEntryTypeAttributes) {
		this.items = theEntryTypeAttributes;

		Set allKeys = items.keySet();
		Iterator iter = allKeys.iterator();
		if (iter.hasNext()) setCurrentItem((String)iter.next());
	} //END protected void setItems(HashMap)

	protected EntryTypeAttribute getCurrentItem() {
		return currentItem;
	} //END protected EntryTypeAttribute getCurrentItem()

	//implements ACollection
	protected void setCurrentItem(String anID) {
		if (null != currentItem) {
			//release the views of the current
			currentItem.releaseViews();
		}
		if (null == anID) {
			currentItem = null;
		}
		else if (items.containsKey(anID)) {
			currentItem = (EntryTypeAttribute)items.get(anID);
			currentItem.setParentController(this);
		}
	} //END private void setCurrentItem(String)

	//implements AChoiceCollection
	protected void deleteItem() {
		deleteItem(currentItem.getId(), false);
	} //END protected void deleteItem()

	//implements ACollection
	//the type attribute is not used for EntryTypeAttributes
	protected void newItem(String aType, boolean isDefault) {
		EntryTypeAttribute newEntryTypeAttribute = null;
		newEntryTypeAttribute = EntryTypeAttribute.newItem(isDefault);
		newEntryTypeAttribute.setEntries(entries);
		items.put(newEntryTypeAttribute.getId(), newEntryTypeAttribute);
		setCurrentItem(newEntryTypeAttribute.getId());
		//save needed
		sendSaveNeeded();
	} //END public void newItem(String)

	//implements ACollection
	protected void refreshListView() {
		Object[][] data = new Object[items.size()][EntryTypeAttribute.getTableDisplayTitles().length];

		EntryTypeAttribute item;
		Set keys = items.keySet();
		Iterator iter = keys.iterator();
		for (int i = 0; i < data.length; i++) {
			item = (EntryTypeAttribute)items.get(iter.next());
			data[i] = item.getTableDisplay();
		}
		if (listView != null) {
			listView.setList(EntryTypeAttribute.getTableDisplayTitles(), data, EntryTypeAttribute.getTableColumnFixedWidth());
		}
		if (null != currentItem) listView.setSelected(currentItem.getId());
	} //END private void resetListView()

	//implements AChoiceCollection
	public String[][] getChoiceProxy() {
		String theIDsAndNames[][] = new String[items.size()][2];
		Set keys = items.keySet();
		EntryTypeAttribute item;
		Iterator iter = keys.iterator();
		for (int i = 0; i < theIDsAndNames.length; i++) {
			item = (EntryTypeAttribute)items.get(iter.next());
			theIDsAndNames[i] = item.getChoiceProxy();
		}
		return theIDsAndNames;
	} //END public String[][] getChoiceProxy()

	/**
	 * Get a pointer to a specific EntryTypeAttribute based
	 * on its id.
	 *
	 * @param String anId - the Id of the EntryTypeAttribute
	 * @return EntryTypeAttribute
	 */
	protected EntryTypeAttribute getEntryTypeAttribute(String anId) {
		return (EntryTypeAttribute)items.get(anId);
	} //END protected EntryTypeAttribute getEntryTypeAttribute(String)

	/**
	 * Sets the pointer to the EntriesCollection and adds it to all EntryTypeAttributes.
	 * Overrides method in AChoiceCollection.
	 */
	protected void setEntries(EntriesCollection theEntries) {
		this.entries = theEntries;
		//set pointers in all attributes
		Set allKeys = items.keySet();
		Iterator iter = allKeys.iterator();
		String thisId;
		EntryTypeAttribute thisItem;
		while (iter.hasNext()) {
			thisId = (String)iter.next();
			thisItem = (EntryTypeAttribute)items.get(thisId);
			thisItem.setEntries(entries);
		}
	} //END protected void setEntries(EntriesCollection)
} //END public class EntryTypeAttributsCollection extends AChoiceCollection

