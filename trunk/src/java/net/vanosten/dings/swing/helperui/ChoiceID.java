/*
 * ChoiceID.java
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
package net.vanosten.dings.swing.helperui;

import javax.swing.JComboBox;

public class ChoiceID extends JComboBox {
	private String itemID[] = {};

	public ChoiceID () {
		super();
	} //END public ChoiceID()

	/**
	 * Error handling needed
	 */
	public void setItems(String[][] theItems) {
		removeAllItems();
		itemID = new String[theItems.length];
		for (int a = 0; a < theItems.length; a++) {
			itemID[a] = theItems[a][0];
			addItem(theItems[a][1]);
		}
	} //END public void setItems(String[][])

	/**
	 * Selects the element in the ComboBox that corresponds to the id.
	 * If the id is null, then the selection is set to nothing.
	 * @param anID
	 */
	public void setSelectedID(String anID) {
		if (null == anID) {
			setSelectedIndex(-1);
			return;
		}
		for (int i = 0; i < itemID.length; i++) {
			if (anID.equals(itemID[i])) {
				setSelectedIndex(i);
				break;
			}
		}
	} //END public void setSelectedID(String)
	
	/**
	 * Based on the current selected item returns an ID.
	 * 
	 * @return String - the ID of the selected item or null if nothing is slected.
	 */
	public String getSelectedID() {
		//if nothing is selected return null
		if (0 > getSelectedIndex()) {
			return null;
		}
		return itemID[getSelectedIndex()];
	} //END public String getSelectedID()
	
	/**
	 * Removes all Items in the underlying JComboBox and the ids
	 * by overriding to method in JComboBox
	 */
	public void removeAllItems() {
		itemID = new String[0];
		super.removeAllItems();
	} //END public void removeAllItems()
} //END public class ChoiceID extends JComboBox