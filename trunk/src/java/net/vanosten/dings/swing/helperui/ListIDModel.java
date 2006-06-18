/*
 * ListIDModel.java
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

import javax.swing.AbstractListModel;

public class ListIDModel extends AbstractListModel {
	private final static long serialVersionUID = 1L;

	String objects[][] = null;

	public ListIDModel(String[][] theObjects) {
		super();
		this.objects = theObjects;
		fireIntervalAdded(this,0,getSize() - 1);
	} //END public ListIDModel(String[][])

	/**
	 * Implements abstract method.
	 */
	public int getSize() {
		return objects.length;
	} //END public int getSize()

	/**
	 * Implements abstract method.
	 * Returns the display name!
	 */
	public Object getElementAt(int pos) {
		return objects[pos][1];
	} //END public Object getElementAt(int)

	public String getIDAt(int pos) {
		return objects[pos][0];
	} //END public String getIDAt(int)

	/**
	 * Returns the index pos of an ID or -1 if ID cannot be found
	 */
	public int getIDIndexPos(String anID) {
		for (int i = 0; i < objects.length; i++) {
			if (anID.equals(objects[i][0])) {
				return i;
			}
		}
		return -1;
	} //END public int getIDIndexPos(String)

	public String[] getIDsAt(int[] pos) {
		String IDs[] = new String[pos.length];
		for (int i = 0; i < pos.length; i++) {
			IDs[i] = objects[pos[i]][0];
		}
		return IDs;
	} //END public String[] getIDsAt(int[])

} //END public class IDListModel extends AbstractListModel
