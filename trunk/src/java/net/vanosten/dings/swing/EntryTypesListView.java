/*
 * EntryTypesListView.java
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
package net.vanosten.dings.swing;

import java.awt.ComponentOrientation;

import net.vanosten.dings.consts.MessageConstants;

public class EntryTypesListView extends ListView {

	public EntryTypesListView(ComponentOrientation aComponentOrientation) {
		super("Entry Types", aComponentOrientation);
	} //END public UnitsListView(CompoenentOrientation)
	
	protected void setMessages() {
		msgEdit = MessageConstants.N_VIEW_ENTRYTYPE_EDIT;
	} //END protected void setMessages()
} //END public class EntryTypesListView extends ListView
