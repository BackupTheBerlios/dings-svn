/*
 * IDingsMainWindow.java
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
package net.vanosten.dings.uiif;

import net.vanosten.dings.consts.MessageConstants.Message;

public interface IDingsMainWindow {
	/**
	 * Sets the look and feel based on the preferences.
	 */
	public void setLookAndFeel();
	public void saveWindowLocationAndSize();
	public void showHelp(Message aMessage);
	public void showMessageDialog(String aTitle, String aMessage, int aMessageType);
	public int showOptionDialog(String aTitle, String aMessage, int aMessageType, int anOptionType);
	public String showFileChooser(String aFileName, boolean showOpen);
	public void setApplicationTitle(String aTitle);
	public void setStatusBarStatusText(String aStatusText, String aSelectionText);
	public void hideMainWindow();
	public void setWaitCursor(boolean enable);
	
	//status of menu items
	public void setEntriesMIEnabled(boolean doEnable);
	public void setEntriesOkEnabled(boolean doEnable);
	public void setSaveMIEnabled(boolean doEnable);
	public void setLearningMIsEnabled(boolean aLearnOneStatus);
	public void setResetScoreMIsEnabled(boolean doEnable);
	public void setOpenVocabEnabled(boolean doEnable);
	public void setFileHistory(String[][] filePaths);
	
	//show views
	public IWelcomeView getWelcomeView();
	public ISummaryView getSummaryView();
	public IInfoVocabEditView getInfoVocabEditView();
	public IListView getUnitsListView();
	public IUnitEditView getUnitEditView();
	public IListView getCategoriesListView();
	public IUnitEditView getCategoryEditView();
	public IEntriesListView getEntriesListView();
	public IEntryEditView getEntryEditView();
	public IListView getEntryTypesListView();
	public IEntryTypeEditView getEntryTypeEditView();
	public IListView getEntryTypeAttributesListView();
	public IEntryTypeAttributeEditView getEntryTypeAttributeEditView();
	public IEntryLearnOneView getEntryLearnOneView();
	public IEntriesSelectionView getEntriesSelectionView();
	public ILearnByChoiceView getLearnByChoiceView();
	public void showView(Message aView);
} //END public interface IDingsMainWindow
