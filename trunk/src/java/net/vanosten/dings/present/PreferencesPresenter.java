/*
 * PreferencesPresenter.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2006 Rick Gruber-Riemer (dingsbums@vanosten.net)
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
package net.vanosten.dings.present;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.model.Preferences;
import net.vanosten.dings.uiif.IPreferencesEditView;
import net.vanosten.dings.utils.Toolbox;
import net.vanosten.dings.utils.Util;

public class PreferencesPresenter extends APresenter {

	/** The edit view */
	private IPreferencesEditView editView;
	
	public PreferencesPresenter() {
		logger = Logger.getLogger(PreferencesPresenter.class.getName());
	}

	/**
	 * Lets you set the edit view
	 *
	 * @param ICategoryEditView aView
	 */
	public void setEditView(IPreferencesEditView aView) {
		editView = aView;
	} //END public void setEditView(IPreferencesEditView)

	//implements AItemModel
	protected void releaseViews() {
		editView = null;
	} //END protected void releaseViews()

	//Implements AModel
	protected void updateGUI() {
		Preferences prefs = Toolbox.getInstance().getPreferencesPointer();
		try {
			//file encoding
			editView.setFileEncoding(prefs.getProperty(Preferences.FILE_ENCODING));
			//look and feel
			editView.setSystemLookAndFeel(prefs.getBooleanProperty(Preferences.PROP_SYSTEM_LAF));
			//learn hints
			//editView.setLearnHintCoverPercent(Integer.parseInt(props.getProperty(LEARN_HINT_COVER_PERCENT)));
			editView.setLearnHintFlashTime(Integer.parseInt(prefs.getProperty(Preferences.LEARN_HINT_FLASH_TIME)));
			editView.setLearnHintShuffleByWord(prefs.getProperty(Preferences.LEARN_HINT_SHUFFLE_WORD));
			editView.setHintTextColor(new Color(Integer.parseInt(prefs.getProperty(Preferences.PROP_COLOR_HINT))));
			editView.setResultTextColor(new Color(Integer.parseInt(prefs.getProperty(Preferences.PROP_COLOR_RESULT))));
			//logging
			editView.setLoggingEnabled(prefs.getProperty(Preferences.PROP_LOGGING_ENABLED));
			editView.setLoggingToFile(prefs.getProperty(Preferences.PROP_LOG_TO_FILE));
			//selection updates
			editView.setSelUpdInst(prefs.getBooleanProperty(Preferences.PROP_SEL_UPD_INST_EDITING), prefs.getBooleanProperty(Preferences.PROP_SEL_UPD_INST_LEARNING));
			//stats on quit
			editView.setStatsOnQuit(prefs.getBooleanProperty(Preferences.PROP_STATS_QUIT));
			//locale
			editView.setApplicationLocale(prefs.getProperty(Preferences.PROP_LOCALE));
			//text lines
			editView.setLinesBase(Integer.valueOf(prefs.getProperty(Preferences.PROP_LINES_BASE)));
			editView.setLinesTarget(Integer.valueOf(prefs.getProperty(Preferences.PROP_LINES_TARGET)));
			editView.setLinesExplanation(Integer.valueOf(prefs.getProperty(Preferences.PROP_LINES_EXPLANATION)));
			editView.setLinesExample(Integer.valueOf(prefs.getProperty(Preferences.PROP_LINES_EXAMPLE)));
			//check answer
			editView.setCheckCaseSensitive(prefs.getBooleanProperty(Preferences.PROP_CHECKANSWER_CASE_SENSITIVE));
			editView.setCheckGlobalAttributes(prefs.getBooleanProperty(Preferences.PROP_CHECKANSWER_GLOBAL_ATTRIBUTES));
			editView.setCheckTypeAttributes(prefs.getBooleanProperty(Preferences.PROP_CHECKANSWER_TYPE_ATTRIBUTES));
			//syllable colors
			editView.setSyllableColorAcute(Util.parseRGBToColor(prefs.getProperty(Preferences.PROP_SYLLABLE_COLOR_ACUTE)));
			editView.setSyllableColorGrave(Util.parseRGBToColor(prefs.getProperty(Preferences.PROP_SYLLABLE_COLOR_GRAVE)));
			editView.setSyllableColorCircumflex(Util.parseRGBToColor(prefs.getProperty(Preferences.PROP_SYLLABLE_COLOR_CIRCUMFLEX)));
			editView.setSyllableColorMacron(Util.parseRGBToColor(prefs.getProperty(Preferences.PROP_SYLLABLE_COLOR_MACRON)));
			editView.setSyllableColorBreve(Util.parseRGBToColor(prefs.getProperty(Preferences.PROP_SYLLABLE_COLOR_BREVE)));
			editView.setSyllableColorCaron(Util.parseRGBToColor(prefs.getProperty(Preferences.PROP_SYLLABLE_COLOR_CARON)));
			editView.setSyllableColorDefault(Util.parseRGBToColor(prefs.getProperty(Preferences.PROP_SYLLABLE_COLOR_DEFAULT)));
		}
		catch (NumberFormatException e) {
			//TODO: log this
		}
	 } //END protected void updateGUI()

	//Implements AModel
	protected void updateModel() {
		Preferences prefs = Toolbox.getInstance().getPreferencesPointer();
		//file encoding
		prefs.setProperty(Preferences.FILE_ENCODING, editView.getFileEncoding());
		//look and feel
		boolean oldLAF = prefs.getBooleanProperty(Preferences.PROP_SYSTEM_LAF);
		if (oldLAF != editView.isSystemLookAndFeel()) {
			prefs.setProperty(Preferences.PROP_SYSTEM_LAF, Boolean.toString(editView.isSystemLookAndFeel()));
			AppEvent ape1 = new AppEvent(AppEvent.EventType.STATUS_EVENT);
			ape1.setMessage(MessageConstants.Message.S_CHANGE_LAF);
			parentController.handleAppEvent(ape1);
		}
		//learn hints
		prefs.setProperty(Preferences.LEARN_HINT_FLASH_TIME, Integer.toString(editView.getLearnHintFlashTime()));
		prefs.setProperty(Preferences.LEARN_HINT_SHUFFLE_WORD, editView.getLearnHintShuffleByWord());
		prefs.setProperty(Preferences.PROP_COLOR_HINT, Integer.toString(editView.getHintTextColor().getRGB()));
		prefs.setProperty(Preferences.PROP_COLOR_RESULT, Integer.toString(editView.getResultTextColor().getRGB()));
		//logging
		String oldEnabled = prefs.getProperty(Preferences.PROP_LOGGING_ENABLED);
		String newEnabled = editView.getLoggingEnabled();
		String oldToFile = prefs.getProperty(Preferences.PROP_LOG_TO_FILE);
		String newToFile = editView.getLoggingToFile();
		if (false == oldEnabled.equals(newEnabled) || false == oldToFile.equals(newToFile)) {
			prefs.setProperty(Preferences.PROP_LOGGING_ENABLED, newEnabled); //must come before event
			prefs.setProperty(Preferences.PROP_LOG_TO_FILE, newToFile); //must come before event
			AppEvent ape2 = new AppEvent(AppEvent.EventType.STATUS_EVENT);
			ape2.setMessage(MessageConstants.Message.S_CHANGE_LOGGING);
			parentController.handleAppEvent(ape2);
		}
		//resetting score
		prefs.setProperty(Preferences.PROP_SEL_UPD_INST_EDITING, Boolean.toString(editView.isSelUpdInstEditing()));
		prefs.setProperty(Preferences.PROP_SEL_UPD_INST_LEARNING, Boolean.toString(editView.isSelUpdInstLearning()));
		//stats on quit
		prefs.setProperty(Preferences.PROP_STATS_QUIT, Boolean.toString(editView.isStatsOnQuit()));
		//locale
		prefs.setProperty(Preferences.PROP_LOCALE, editView.getApplicationLocale());
		//text lines
		prefs.setProperty(Preferences.PROP_LINES_BASE, editView.getLinesBase().toString());
		prefs.setProperty(Preferences.PROP_LINES_TARGET, editView.getLinesTarget().toString());
		prefs.setProperty(Preferences.PROP_LINES_EXPLANATION, editView.getLinesExplanation().toString());
		prefs.setProperty(Preferences.PROP_LINES_EXAMPLE, editView.getLinesExample().toString());
		//check answer
		prefs.setProperty(Preferences.PROP_CHECKANSWER_CASE_SENSITIVE, Boolean.toString(editView.isCheckCaseSensitive()));
		prefs.setProperty(Preferences.PROP_CHECKANSWER_GLOBAL_ATTRIBUTES, Boolean.toString(editView.isCheckGlobalAttributes()));
		prefs.setProperty(Preferences.PROP_CHECKANSWER_TYPE_ATTRIBUTES, Boolean.toString(editView.isCheckTypeAttributes()));
		//syllable color
		prefs.setProperty(Preferences.PROP_SYLLABLE_COLOR_ACUTE, Util.convertRGB(editView.getSyllableColorAcute()));
		prefs.setProperty(Preferences.PROP_SYLLABLE_COLOR_GRAVE, Util.convertRGB(editView.getSyllableColorGrave()));
		prefs.setProperty(Preferences.PROP_SYLLABLE_COLOR_CIRCUMFLEX, Util.convertRGB(editView.getSyllableColorCircumflex()));
		prefs.setProperty(Preferences.PROP_SYLLABLE_COLOR_MACRON, Util.convertRGB(editView.getSyllableColorMacron()));
		prefs.setProperty(Preferences.PROP_SYLLABLE_COLOR_BREVE, Util.convertRGB(editView.getSyllableColorBreve()));
		prefs.setProperty(Preferences.PROP_SYLLABLE_COLOR_CARON, Util.convertRGB(editView.getSyllableColorCaron()));
		prefs.setProperty(Preferences.PROP_SYLLABLE_COLOR_DEFAULT, Util.convertRGB(editView.getSyllableColorDefault()));
	} //END protected void updateModel()

	//Overrides AModel
	public void handleAppEvent(AppEvent evt) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "handleAppEvent", evt.getMessage().name());
		}
		if (evt.getMessage() == MessageConstants.Message.D_PREFERENCES_EDIT_APPLY) {
			updateModel();
		}
		else if (evt.getMessage() == MessageConstants.Message.D_PREFERENCES_EDIT_REVERT) {
			updateGUI();
		}
		else if (evt.getMessage() == MessageConstants.Message.S_SAVE_DIALOG_SIZE) {
			saveDialogSize();
		}
		else parentController.handleAppEvent(evt);
	} //END public void handleAppEvent(AppEvent)

	/**
	 * Saves the size information of the dialog showing the preferences
	 */
	private void saveDialogSize() {
		Preferences prefs = Toolbox.getInstance().getPreferencesPointer();
		int dialogSize[] = editView.getDialogSize();
		prefs.setProperty(Preferences.PROP_PREF_DIALOG_WIDTH, Integer.toString(dialogSize[0]));
		prefs.setProperty(Preferences.PROP_PREF_DIALOG_HEIGHT, Integer.toString(dialogSize[1]));
	} //END private void saveDialogSize()
}
