/*
 * IInfoVocabEditView.java
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
package net.vanosten.dings.uiif;

public interface IInfoVocabEditView extends IDetailsView {
	public void setTitle(String aTitle);
	public String getTitle();
	public void setAuthor(String anAuthor);
	public String getAuthor();
	public void setNotes(String theNotes);
	public String getNotes();
	public void setCopyright(String aCopyright);
	public String getCopyright();
	public void setLicence(String aLicence);
	public String getLicence();
	public void setBaseLabel(String aLabel);
	public String getBaseLabel();
	public void setTargetLabel(String aLabel);
	public String getTargetLabel();
	public void setAttributesLabel(String aLabel);
	public String getAttributesLabel();
	public void setUnitLabel(String aLabel);
	public String getUnitLabel();
	public void setCategoryLabel(String aLabel);
	public String getCategoryLabel();
	public void setOthersLabel(String aLabel);
	public String getOthersLabel();
	public void setExplanationLabel(String aLabel);
	public String getExplanationLabel();
	public void setExampleLabel(String aLabel);
	public String getExampleLabel();
	public void setBaseLocale(String aLocale);
	public String getBaseLocale();
	public void setTargetLocale(String aLocale);
	public String getTargetLocale();
	public void setAttributesLocale(String aLocale);
	public String getAttributesLocale();
	public void setUnitLocale(String aLocale);
	public String getUnitLocale();
	public void setCategoryLocale(String aLocale);
	public String getCategoryLocale();
	public void setExplanationLocale(String aLocale);
	public String getExplanationLocale();
	public void setExampleLocale(String aLocale);
	public String getExampleLocale();
	public void setPronunciationLocale(String aLocale);
	public String getPronunciationLocale();
	public void setRelationLocale(String aLocale);
	public String getRelationLocale();
	public void setVisibilityAttributes(int aVisibility);
	public int getVisibilityAttributes();
	public void setVisibilityUnit(int aVisibility);
	public int getVisibilityUnit();
	public void setVisibilityCategory(int aVisibility);
	public int getVisibilityCategory();
	public void setVisibilityExplanation(int aVisibility);
	public int getVisibilityExplanation();
	public void setVisibilityExample(int aVisibility);
	public int getVisibilityExample();
	public void setVisibilityPronunciation(int aVisibility);
	public int getVisibilityPronunciation();
	public void setVisibilityRelation(int aVisibility);
	public int getVisibilityRelation();
	public void setAvailableLocales(String[][] theLocales);
	public boolean isBaseUsesSyllables();
	public void setBaseUsesSyllables(boolean isUsed);
	public boolean isTargetUsesSyllables();
	public void setTargetUsesSyllables(boolean isUsed);
	//validation
	public void setTitleIsValueValid(boolean valid);
	public void setBaseLabelIsValueValid(boolean valid);
	public void setTragetLabelIsValueValid(boolean valid);
	public void setAttributesLabelIsValueValid(boolean valid);
	public void setUnitLabelIsValueValid(boolean valid);
	public void setCategoryLabelIsValueValid(boolean valid);
	public void setOthersLabelIsValueValid(boolean valid);
	public void setExplanationLabelIsValueValid(boolean valid);
	public void setExampleLabelIsValueValid(boolean valid);
} //END public interface IInfoVocabEditView extends IDetailsView
