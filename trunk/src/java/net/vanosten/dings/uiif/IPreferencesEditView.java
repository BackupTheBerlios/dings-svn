/*
 * IPreferencesEditView.java
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

public interface IPreferencesEditView extends IView {
	//file encoding (as specified in http://java.sun.com/j2se/1.3/docs/api/java/lang/package-summary.html#charenc)
	public final static String FILE_ENCODING_DEFAULT = "UTF-8";
	public final static String[] FILE_ENCODINGS = {
		"US-ASCII"
		,"ISO-8859-1"
		,FILE_ENCODING_DEFAULT
		,"UTF-16BE"
		,"UTF-16LE"
		,"UTF-16"
	};

	//The preference dialog's window size
	public void setDialogSize(int aWidth, int aHeight);
	public int[] getDialogSize();

	//file encoding
	public void setFileEncoding(String anEncoding);
	public String getFileEncoding();

	//learn hint
	public final static int LH_COVER_PERCENT_MIN = 0;
	public final static int LH_COVER_PERCENT_MAX = 100;
	public final static int LH_COVER_PERCENT_DEFAULT = 40;
	public final static int LH_FLASH_TIME_MIN = 0;
	public final static int LH_FLASH_TIME_MAX = 3000;
	public final static int LH_FLASH_TIME_DEFAULT = 500;
	public void setLearnHintCoverPercent(int thePercentage);
	public int getLearnHintCoverPercent();
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
} //END public interface IPreferencesEditView extends IDetailsView
