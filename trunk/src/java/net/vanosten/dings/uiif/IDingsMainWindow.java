/*
 * IDingsMainWindow.java
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
package net.vanosten.dings.uiif;

public interface IDingsMainWindow {
	public void saveWindowLocationAndSize();
	public void showHelp(String aMessage);
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
	public void showView(String aView);
} //END public interface IDingsMainWindow
