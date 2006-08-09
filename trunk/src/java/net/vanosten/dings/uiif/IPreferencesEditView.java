/*
 * IPreferencesEditView.java
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

import java.awt.Color;

public interface IPreferencesEditView extends IView {

	//The preference dialog's window size
	public void setDialogSize(int aWidth, int aHeight);
	public int[] getDialogSize();

	//file encoding
	public void setFileEncoding(String anEncoding);
	public String getFileEncoding();

	//learn hint
	/*
	public final static int LH_COVER_PERCENT_MIN = 0;
	public final static int LH_COVER_PERCENT_MAX = 100;
	public final static int LH_COVER_PERCENT_DEFAULT = 40;
	*/
	public final static int LH_FLASH_TIME_MIN = 0;
	public final static int LH_FLASH_TIME_MAX = 3000;
	public final static int LH_FLASH_TIME_DEFAULT = 500;
	/*
	public void setLearnHintCoverPercent(int thePercentage);
	public int getLearnHintCoverPercent();
	*/
	public void setLearnHintFlashTime(int theFlashTime);
	public int getLearnHintFlashTime();
	public void setLearnHintShuffleByWord(String isEnabled);
	public String getLearnHintShuffleByWord();
	//logging
	public void setLoggingEnabled(String isEnabled);
	public String getLoggingEnabled();
	public void setLoggingToFile(String isEnabled);
	public String getLoggingToFile();
	//look and feel
	public final static boolean SYSTEM_LAF_DEFAULT = true;
	public void setSystemLookAndFeel(boolean isSystem);
	public boolean isSystemLookAndFeel();
	//selection updates
	public boolean isSelUpdInstEditing();
	public boolean isSelUpdInstLearning();
	public void setSelUpdInst(boolean editingEnabled, boolean learningEnabled);
	//statistics
	public boolean isStatsOnQuit();
	public void setStatsOnQuit(boolean statsEnabled);
	//locale
	public String getApplicationLocale();
	public void setApplicationLocale(String aLocale);
	//hint colors
	public Color getHintTextColor();
	public void setHintTextColor(Color aColor);
	public Color getResultTextColor();
	public void setResultTextColor(Color aColor);
	//text lines
	public Integer getLinesBase();
	public void setLinesBase(Integer numberOfLines);
	public Integer getLinesTarget();
	public void setLinesTarget(Integer numberOfLines);
	public Integer getLinesExplanation();
	public void setLinesExplanation(Integer numberOfLines);
	public Integer getLinesExample();
	public void setLinesExample(Integer numberOfLines);
	//check answer
	public boolean isCheckCaseSensitive();
	public void setCheckCaseSensitive(boolean sensitive);
	public boolean isCheckTypeAttributes();
	public void setCheckTypeAttributes(boolean check);
	public boolean isCheckGlobalAttributes();
	public void setCheckGlobalAttributes(boolean check);
	//syllable colors
	public Color getSyllableColorAcute();
	public void setSyllableColorAcute(Color aColor);
	public Color getSyllableColorGrave();
	public void setSyllableColorGrave(Color aColor);
	public Color getSyllableColorCircumflex();
	public void setSyllableColorCircumflex(Color aColor);
	public Color getSyllableColorMacron();
	public void setSyllableColorMacron(Color aColor);
	public Color getSyllableColorBreve();
	public void setSyllableColorBreve(Color aColor);
} //END public interface IPreferencesEditView extends IDetailsView
