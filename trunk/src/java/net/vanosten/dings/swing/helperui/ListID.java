/*
 * ListID.java
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
package net.vanosten.dings.swing.helperui;

import javax.swing.JList;

public class ListID extends JList {
	private final static long serialVersionUID = 1L;

	private ListIDModel listModel;

	public ListID (int rows, int mode) {
		super();
		setVisibleRowCount(rows);
		setSelectionMode(mode);
	} //END public ListID(int)

	public void setListIDModel(ListIDModel aListModel) {
		this.listModel = aListModel;
		setModel(aListModel);
	} //END public setModel(ListIDModel)

	public void setSelectedIDs(Long[] theIDs) {
		int[] indices = new int[theIDs.length];
		for (int i = 0; i < theIDs.length; i++) {
			indices[i] = listModel.getIDIndexPos(theIDs[i]);
		}
		this.setSelectedIndices(indices);
	} //END public void setSelectedIDs(String[])

	public void selectID(Long anID) {
		int myPos = listModel.getIDIndexPos(anID) ;
		if (myPos >= 0) {
			setSelectedIndex(myPos);
		}
		//TODO: else ???????
	} //END public void selectID(String)

	public Long getSelectedID() {
		return listModel.getIDAt(getSelectedIndex());
	} //END public String getSelectedID()

	public Long[] getSelectedIDs() {
		int [] indices = this.getSelectedIndices();
		return listModel.getIDsAt(indices);
	} //END public String[] getSelectedIDs()
} //END public class ListID
