/*
 * MessageConstants.java
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
package net.vanosten.dings.consts;

import java.util.List;

import net.vanosten.dings.event.AppEvent;

public class MessageConstants {

	/**
	 * Private constructor so it can not be called
	 */
	private MessageConstants() {
		//nothing to initialize
	}

	public enum Message {		
		//Navigation
		N_VIEW_ENTRIES_SELECTION
		, N_VIEW_ENTRY_LEARNONE
		, N_VIEW_ENTRIES_LIST
		, N_VIEW_ENTRY_EDIT
		, N_VIEW_UNITS_LIST
		, N_VIEW_UNIT_EDIT
		, N_VIEW_CATEGORIES_LIST
		, N_VIEW_CATEGORY_EDIT
		, N_VIEW_INFOVOCAB_EDIT
		, N_VIEW_ENTRYTYPES_LIST
		, N_VIEW_ENTRYTYPE_EDIT
		, N_VIEW_ENTRYTYPE_ATTRIBUTES_LIST
		, N_VIEW_ENTRYTYPE_ATTRIBUTE_EDIT
		, N_VIEW_WELCOME
		, N_VIEW_STATISTICS
		//ADings
		, S_EXIT_APPLICATION
		, S_NEW_VOCABULARY
		, S_OPEN_VOCABULARY
		, S_CLOSE_VOCABULARY
		, S_SAVE_VOCABULARY
		, S_SAVE_AS_VOCABULARY
		, S_SAVE_NEEDED
		, S_SHOW_VALIDATION_ERROR
		, S_SHOW_DELETE_ERROR
		, S_SAVE_DIALOG_SIZE
		, S_CHANGE_LOGGING
		, S_CHANGE_LAF
		, D_STATISTICS_SAVE_SET
		, H_ABOUT
		, H_ALL
		//AEditView
		, D_EDIT_VIEW_APPLY
		, D_EDIT_VIEW_DELETE
		, D_EDIT_VIEW_REVERT
		, D_EDIT_VIEW_CHECK_CHANGE
		, D_EDIT_VIEW_CHANGE_ENTRY_TYPE
		//AListView
		, D_LIST_VIEW_NEW
		, D_LIST_VIEW_DELETE
		, D_LIST_VIEW_REFRESH	
		//InfoVocabEditView
		, H_INFOVOCAB_EDIT_VIEW
		//UnitsListView
		, H_UNITS_LIST_VIEW
		//UnitEditView
		, H_UNIT_EDIT_VIEW
		//EntryTypesList
		, H_ENTRYTYPES_LIST_VIEW
		//EntryTypeEditView
		, H_ENTRYTYPE_EDIT_VIEW
		//EntriesListView
		, D_ENTRIES_TOTALS_CHANGED
		, H_ENTRIES_LIST_VIEW
		//EntryEditView
		, H_ENTRY_EDIT_VIEW
		//EntriesSelectionView
		, D_ENTRIES_SELECTION_APPLY
		, D_ENTRIES_SELECTION_REFRESH
		, H_ENTRIES_CHOOSER_VIEW
		//EntryLearnOneView
		, D_ENTRIES_INITIALIZE_LEARNING
		, D_ENTRY_LEARNONE_NEXT
		, D_ENTRY_LEARNONE_REFRESH
		, D_ENTRY_LEARNONE_GETRESULT
		, H_ENTRY_LEARNONE_VIEW
		//PreferencesEditView
		, D_PREFERENCES_EDIT_REVERT
		, D_PREFERENCES_EDIT_APPLY
		//EntryType
		, D_ENTRY_TYPE_CHANGE_ATTRIBUTES
		//SummaryView
		, D_SUMMARY_VIEW_DISPLAY_UNITS
		, D_SUMMARY_VIEW_DISPLAY_CATEGORIES
		, D_SUMMARY_VIEW_DISPLAY_ENTRYTYPES
		, D_SUMMARY_VIEW_DISPLAY_SCORES
		, D_SUMMARY_VIEW_DISPLAY_TIMELINE
		//other
		, D_ENTRIES_RESET_SCORE_ALL
		, D_ENTRIES_RESET_SCORE_SEL
	}

	/**
	 * Returns an AppEvent to show a list of errors, which are contained in a ArrayList.
	 *
	 * @param ArrayList errors - the validation errors to be shown as bullet points
	 * @param String message - the explanation of the errors
	 */
	public final static AppEvent getShowErrorListEvent(List<String> errors, String message) {
		StringBuffer validationErrors = new StringBuffer("<html><p>");
		validationErrors.append(message).append(":</p><ul>");
		for (int i = 0; i < errors.size(); i++) {
			validationErrors.append("<li>");
			validationErrors.append((String)errors.get(i));
			validationErrors.append("</li>");
		}
		validationErrors.append("</ul></html>");

		//send AppEvent
		AppEvent ape = new AppEvent(AppEvent.EventType.STATUS_EVENT);
		ape.setMessage(MessageConstants.Message.S_SHOW_VALIDATION_ERROR);
		ape.setDetails(validationErrors.toString());
		return ape;
	} //END public final static AppEvent getShowErrorListEvent(ArrayList, String)

}   //END public class MessageConstants
