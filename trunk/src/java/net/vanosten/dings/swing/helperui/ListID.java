/*
 * ListID.java
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

import javax.swing.JList;

public class ListID extends JList {
	//private String itemID[] = {};
	private ListIDModel listModel;
	
	public ListID (int rows, int mode) {
		super();
		setVisibleRowCount(rows);
		setSelectionMode(mode);
	}	//END public ListID(int)
	
	public void setListIDModel(ListIDModel aListModel) {
		this.listModel = aListModel; 
		setModel(aListModel);
	}	//public setModel(ListIDModel)
	
	public void setSelectedIDs(String[] theIDs) {
		int[] indices = new int[theIDs.length];
		for (int i = 0; i < theIDs.length; i++) {	
			indices[i] = listModel.getIDIndexPos(theIDs[i]);
		}
		this.setSelectedIndices(indices);
	} //END public void setSelectedIDs(String[])
	
	public void selectID(String anID) {
		int myPos = listModel.getIDIndexPos(anID) ;
		if (myPos >= 0) {
			setSelectedIndex(myPos);
		}
		//else ???????
	}	//END public void selectID(String)
	
	public String getSelectedID() {
		return listModel.getIDAt(getSelectedIndex());
	}	//END public String getSelectedID()
	
	public String[] getSelectedIDs() {
		int [] indices = this.getSelectedIndices();
		return listModel.getIDsAt(indices);
	}	//END public String[] getSelectedIDs()
}	//END public class ListID
