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

import java.util.ArrayList;

import net.vanosten.dings.event.AppEvent;

public class MessageConstants {

	/**
	 * Private constructor so it can not be called
	 */
	private MessageConstants() {
		//nothing to initialize
	}

	//Navigation
	public final static String N_VIEW_ENTRIES_SELECTION = "N_VIEW_ENTRIES_SELECTION";
	public final static String N_VIEW_ENTRY_LEARNONE = "N_VIEW_ENTRY_LEARNONE";
	public final static String N_VIEW_ENTRIES_LIST = "N_VIEW_ENTRIES_LIST";
	public final static String N_VIEW_ENTRY_EDIT = "N_VIEW_ENTRY_EDIT";
	public final static String N_VIEW_UNITS_LIST = "N_VIEW_UNITS_LIST";
	public final static String N_VIEW_UNIT_EDIT = "N_VIEW_UNIT_EDIT";
	public final static String N_VIEW_CATEGORIES_LIST = "N_VIEW_CATEGORIES_LIST";
	public final static String N_VIEW_CATEGORY_EDIT = "N_VIEW_CATEGORY_EDIT";
	public final static String N_VIEW_INFOVOCAB_EDIT = "N_VIEW_INFOVOCAB_EDIT";
	public final static String N_VIEW_ENTRYTYPES_LIST = "N_VIEW_ENTRYTYPES_LIST";
	public final static String N_VIEW_ENTRYTYPE_EDIT = "N_VIEW_ENTRYTYPE_EDIT";
	public final static String N_VIEW_ENTRYTYPE_ATTRIBUTES_LIST = "N_VIEW_ENTRYTYPE_ATTRIBUTES_LIST";
	public final static String N_VIEW_ENTRYTYPE_ATTRIBUTE_EDIT = "N_VIEW_ENTRYTYPE_ATTRIBUTE_EDIT";
	public final static String N_VIEW_WELCOME = "N_VIEW_WELCOME";
	public final static String N_VIEW_STATISTICS = "N_VIEW_STATISTICS";

	//ADings
	public final static String S_EXIT_APPLICATION = "S_EXIT_APPLICATION";
	public final static String S_NEW_VOCABULARY = "S_NEW_VOCABULARY";
	public final static String S_OPEN_VOCABULARY = "S_OPEN_VOCABULARY";
	public final static String S_CLOSE_VOCABULARY = "S_CLOSE_VOCABULARY";
	public final static String S_SAVE_VOCABULARY = "S_SAVE_VOCABULARY";
	public final static String S_SAVE_AS_VOCABULARY = "S_SAVE_AS_VOCABULARY";
	public final static String S_SAVE_NEEDED = "S_SAVE_NEEDED";
	public final static String S_SHOW_VALIDATION_ERROR = "S_SHOW_VALIDATION_ERROR";
	public final static String S_SHOW_DELETE_ERROR = "S_SHOW_DELETE_ERROR";
	public final static String S_SAVE_DIALOG_SIZE = "S_SAVE_DIALOG_SIZE";
	public final static String S_CHANGE_LOGGING = "S_CHANGE_LOGGING";
	public final static String S_CHANGE_LAF = "S_CHANGE_LAF";
	public final static String D_STATISTICS_SAVE_SET = "D_STATISTICS_SAVE_SET";

	public final static String H_ABOUT = "H_ABOUT";
	public final static String H_ALL = "H_ALL";

	//AEditView
	public final static String D_EDIT_VIEW_APPLY = "D_EDIT_VIEW_APPLY";
	public final static String D_EDIT_VIEW_DELETE = "D_EDIT_VIEW_DELETE";
	public final static String D_EDIT_VIEW_REVERT  = "D_EDIT_VIEW_REVERT";
	public final static String D_EDIT_VIEW_CHECK_CHANGE = "D_EDIT_VIEW_CHECK_CHANGE";
	public final static String D_EDIT_VIEW_CHANGE_ENTRY_TYPE  = "D_EDIT_VIEW_CHANGE_ENTRY_TYPE";

	//AListView
	public final static String D_LIST_VIEW_NEW = "D_LIST_VIEW_NEW";
	public final static String D_LIST_VIEW_DELETE = "D_LIST_VIEW_DELETE";
	public final static String D_LIST_VIEW_REFRESH = "D_LIST_VIEW_REFRESH";

	//InfoVocabEditView
	public final static String H_INFOVOCAB_EDIT_VIEW = "H_INFOVOCAB_EDIT_VIEW";

	//UnitsListView
	public final static String H_UNITS_LIST_VIEW = "H_UNITS_LIST_VIEW";

	//UnitEditView
	public final static String H_UNIT_EDIT_VIEW = "H_UNIT_EDIT_VIEW";

	//EntryTypesList
	public final static String H_ENTRYTYPES_LIST_VIEW = "H_ENTRYTYPES_LIST_VIEW";

	//EntryTypeEditView
	public final static String H_ENTRYTYPE_EDIT_VIEW = "H_ENTRYTYPE_EDIT_VIEW";

	//EntriesListView
	public final static String D_ENTRIES_TOTALS_CHANGED = "D_ENTRIES_TOTALS_CHANGED";
	public final static String H_ENTRIES_LIST_VIEW = "H_ENTRIES_LIST_VIEW";

	//EntryEditView
	public final static String H_ENTRY_EDIT_VIEW = "H_ENTRY_EDIT_VIEW";

	//EntriesSelectionView
	public final static String D_ENTRIES_SELECTION_APPLY = "D_ENTRIES_SELECTION_APPLY";
	public final static String D_ENTRIES_SELECTION_REFRESH = "D_ENTRIES_SELECTION_REFRESH";
	public final static String H_ENTRIES_CHOOSER_VIEW = "H_ENTRIES_CHOOSER_VIEW";

	//EntryLearnOneView
	public final static String D_ENTRIES_INITIALIZE_LEARNING = "D_ENTRIES_INITIALIZE_LEARNING";
	public final static String D_ENTRY_LEARNONE_NEXT = "D_ENTRY_LEARNONE_NEXT";
	public final static String D_ENTRY_LEARNONE_REFRESH = "D_ENTRY_LEARNONE_REFRESH";
	public final static String D_ENTRY_LEARNONE_GETRESULT = "D_ENTRY_LEARNONE_GETRESULT";
	public final static String H_ENTRY_LEARNONE_VIEW = "H_ENTRY_LEARNONE_VIEW";

	//PreferencesEditView
	public final static String D_PREFERENCES_EDIT_REVERT  = "D_PREFERENCES_EDIT_REVERT";
	public final static String D_PREFERENCES_EDIT_APPLY  = "D_PREFERENCES_EDIT_APPLY";

	//EntryType
	public final static String D_ENTRY_TYPE_CHANGE_ATTRIBUTES = "D_ENTRY_TYPE_CHANGE_ATTRIBUTES";

	//SummaryView
	public final static String D_SUMMARY_VIEW_DISPLAY_UNITS = "D_SUMMARY_VIEW_DISPLAY_UNITS";
	public final static String D_SUMMARY_VIEW_DISPLAY_CATEGORIES = "D_SUMMARY_VIEW_DISPLAY_CATEGORIES";
	public final static String D_SUMMARY_VIEW_DISPLAY_ENTRYTYPES = "D_SUMMARY_VIEW_DISPLAY_ENTRYTYPES";
	public final static String D_SUMMARY_VIEW_DISPLAY_SCORES = "D_SUMMARY_VIEW_DISPLAY_SCORES";
	public final static String D_SUMMARY_VIEW_DISPLAY_TIMELINE = "D_SUMMARY_VIEW_DISPLAY_TIMELINE";

	//other
	public final static String D_ENTRIES_RESET_SCORE_ALL = "D_ENTRIES_RESET_SCORE_ALL";
	public final static String D_ENTRIES_RESET_SCORE_SEL = "D_ENTRIES_RESET_SCORE_SEL";

	/**
	 * Returns an AppEvent to show a list of errors, which are contained in a ArrayList.
	 *
	 * @param ArrayList errors - the validation errors to be shown as bullet points
	 * @param String message - the explanation of the errors
	 */
	public final static AppEvent getShowErrorListEvent(ArrayList errors, String message) {
		StringBuffer validationErrors = new StringBuffer("<html><p>");
		validationErrors.append(message).append(":</p><ul>");
		for (int i = 0; i < errors.size(); i++) {
			validationErrors.append("<li>");
			validationErrors.append((String)errors.get(i));
			validationErrors.append("</li>");
		}
		validationErrors.append("</ul></html>");

		//send AppEvent
		AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
		ape.setMessage(MessageConstants.S_SHOW_VALIDATION_ERROR);
		ape.setDetails(validationErrors.toString());
		return ape;
	} //END public final static AppEvent getShowErrorListEvent(ArrayList, String)

}   //END public class MessageConstants
