/*
 * EntriesListView.java
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

import javax.swing.JOptionPane;

import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.model.Toolbox;
import net.vanosten.dings.swing.helperui.ChoiceID;
import net.vanosten.dings.uiif.IEntriesListView;

public class EntriesListView extends ListView implements IEntriesListView {
	private final static long serialVersionUID = 1L;
	
	/** The names of and ids for EntryTypes to be presented in a dialog */
	private String[][] entryTypes;
	
	public EntriesListView(ComponentOrientation aComponentOrientation) {
		super(Toolbox.getInstance().getLocalizedString("viewtitle.list_entries"), aComponentOrientation);
	} //END public EntriesListView()
	
	protected void setMessages() {
		msgEdit = MessageConstants.Message.N_VIEW_ENTRY_EDIT;
	} //END protected void setMessages()
	
	/**
	 * Overrides the method in ListView to present a dialog with choice possiblities.
	 * If there is more than one element in 
	 */
	protected String getIdTypeForNew() {
		//if there is only one entry type, then choose that one directly
		if (1 == entryTypes.length) {
			return entryTypes[0][0];
		}
		//else allow the user to choose the appropriate entry type
		ChoiceID entryTypesCh = new ChoiceID();
		entryTypesCh.setItems(entryTypes);
		Object[] options = {Toolbox.getInstance().getLocalizedString("label.button.ok")
				, Toolbox.getInstance().getLocalizedString("label.button.cancel")};
		int answer = JOptionPane.showOptionDialog(this
				, entryTypesCh
				, "Please Choose the Entry Type"
				, JOptionPane.OK_CANCEL_OPTION
				, JOptionPane.QUESTION_MESSAGE
				, DingsSwingConstants.getIconForMessageType(Constants.QUESTION_MESSAGE)
				,options //the titles of buttons
				,options[0]); //default button title
	
		//if ok, show entry edit
		if (JOptionPane.OK_OPTION == answer) {
			return entryTypesCh.getSelectedID();
		}
		//else return null
		return null;
	} //END protected String getIdTypeForNew()
	
	/**
	 * Prepares a combobox for choice of entry type, when new is called.
	 *
	 * @param String[][] theEntryTypes - the entry types and respective IDs
	 */
	public void setEntryTypes(String[][] theEntryTypes) {
		this.entryTypes = theEntryTypes;
	} //END public void setEntryTypes(String[][])
} //END public class EntriesListView
