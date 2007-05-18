/*
 * SyllablesUtil.java
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
package net.vanosten.dings.utils;

import java.awt.Color;
import java.util.List;

import net.vanosten.dings.model.Preferences;

public class SyllablesUtil {

	/*-------------------------Syllables----------------------------------------*/
	public final static String AGRAVE = "à";
	public final static String AACUTE = "á";
	public final static String ACIRCUMFLEX = "â";
	public final static String AMACRON = "ā";
	public final static String ABREVE = "ă";
	public final static String ACARON = "ǎ";
	public final static String EGRAVE = "è";
	public final static String EACUTE = "é";
	public final static String ECIRCUMFLEX = "ê";
	public final static String EMACRON = "ē";
	public final static String EBREVE = "ĕ";
	public final static String ECARON = "ě"; //U0011B
	public final static String IGRAVE = "ì";
	public final static String IACUTE = "í";
	public final static String ICIRCUMFLEX = "î";
	public final static String IMACRON = "ī";
	public final static String IBREVE = "ĭ";
	public final static String ICARON = "ǐ";
	public final static String OGRAVE = "ò";
	public final static String OACUTE = "ó";
	public final static String OCIRCUMFLEX = "ô";
	public final static String OMACRON = "ō";
	public final static String OBREVE = "ŏ";
	public final static String OCARON = "ǒ";
	public final static String UGRAVE = "ù";
	public final static String UACUTE = "ú";
	public final static String UCIRCUMFLEX = "û";
	public final static String UMACRON = "ū";
	public final static String UBREVE = "ŭ";
	public final static String UCARON = "ǔ";
	
	public final static String[][] ACCENTS_BY_ACCENTGROUP = {
		{AGRAVE,EGRAVE,IGRAVE,OGRAVE,UGRAVE}
		, {AACUTE,EACUTE,IACUTE,OACUTE,UACUTE}
		, {ACIRCUMFLEX,ECIRCUMFLEX,ICIRCUMFLEX,OCIRCUMFLEX,UCIRCUMFLEX}
		, {AMACRON,EMACRON,IMACRON,OMACRON,UMACRON}
		, {ABREVE,EBREVE,IBREVE,OBREVE,UBREVE}
		, {ACARON,ECARON,ICARON,OCARON,UCARON}
	};
	
	
	public final static String[][] TOOLTIPS_BY_ACCENTGROUP = {
		{Toolbox.getInstance().getLocalizedString("AGRAVE")
				,Toolbox.getInstance().getLocalizedString("EGRAVE")
				,Toolbox.getInstance().getLocalizedString("IGRAVE")
				,Toolbox.getInstance().getLocalizedString("OGRAVE")
				,Toolbox.getInstance().getLocalizedString("UGRAVE")}
		, {Toolbox.getInstance().getLocalizedString("AACUTE")
			,Toolbox.getInstance().getLocalizedString("EACUTE")
			,Toolbox.getInstance().getLocalizedString("IACUTE")
			,Toolbox.getInstance().getLocalizedString("OACUTE")
			,Toolbox.getInstance().getLocalizedString("UACUTE")}
		, {Toolbox.getInstance().getLocalizedString("ACIRCUMFLEX")
			,Toolbox.getInstance().getLocalizedString("ECIRCUMFLEX")
			,Toolbox.getInstance().getLocalizedString("ICIRCUMFLEX")
			,Toolbox.getInstance().getLocalizedString("OCIRCUMFLEX")
			,Toolbox.getInstance().getLocalizedString("UCIRCUMFLEX")}
		, {Toolbox.getInstance().getLocalizedString("AMACRON")
			,Toolbox.getInstance().getLocalizedString("EMACRON")
			,Toolbox.getInstance().getLocalizedString("IMACRON")
			,Toolbox.getInstance().getLocalizedString("OMACRON")
			,Toolbox.getInstance().getLocalizedString("UMACRON")}
		, {Toolbox.getInstance().getLocalizedString("ABREVE")
			,Toolbox.getInstance().getLocalizedString("EBREVE")
			,Toolbox.getInstance().getLocalizedString("IBREVE")
			,Toolbox.getInstance().getLocalizedString("OBREVE")
			,Toolbox.getInstance().getLocalizedString("UBREVE")}
		, {Toolbox.getInstance().getLocalizedString("ACARON")
			,Toolbox.getInstance().getLocalizedString("ECARON")
			, Toolbox.getInstance().getLocalizedString("ICARON")
			,Toolbox.getInstance().getLocalizedString("OCARON")
			,Toolbox.getInstance().getLocalizedString("UCARON")}
	};

	
	public final static String[] COLOR_BY_ACCENTGROUP = {
		Preferences.PROP_SYLLABLE_COLOR_GRAVE
		, Preferences.PROP_SYLLABLE_COLOR_ACUTE
		, Preferences.PROP_SYLLABLE_COLOR_CIRCUMFLEX
		, Preferences.PROP_SYLLABLE_COLOR_MACRON
		, Preferences.PROP_SYLLABLE_COLOR_BREVE
		, Preferences.PROP_SYLLABLE_COLOR_CARON
	};
	
	/**
	 * Private constructor to prevent instantiation
	 */
	private SyllablesUtil() {}
	
	/**
	 * 
	 * @param aToken
	 * @return the position of the first character with an accent within ACCENTS_BY_ACCENTGROUP or -1 if no accent hit 
	 */
	public final static int indexOfFirstAccent(String aToken) {
		for (int c = 0; c < aToken.length(); c++) {		
			for (int i = 0; i < ACCENTS_BY_ACCENTGROUP.length; i++) {
				for (int j = 0; j < ACCENTS_BY_ACCENTGROUP[i].length; j++) {
					if (aToken.substring(c,c+1).equalsIgnoreCase(ACCENTS_BY_ACCENTGROUP[i][j])) {
						return i;
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * 
	 * @param text
	 * @return the same text with html font styles having colors per token depending on the first found accent
	 */
	public final static String enrichSyllablesWithColor(String text) {
		//preallocate StringBuilder
		////one syllable about 3 letters, enriched syllable about 35 letters
		int size = 35;
		if (null != text) {
			size = size * (text.length()/3);
		}
		StringBuilder sb = new StringBuilder(size);
		sb.append("<html>");
		
		//get all tokens
		List<String> tokens = Util.extractTokensBetweenWhitespace(text);
		
		int accentgroup;
		Color aColor;
		for (String token : tokens) {
			if (0 < sb.length()) {
				sb.append(" ");
			}
			accentgroup = indexOfFirstAccent(token);
			if (0 <= accentgroup) {
				aColor = Toolbox.getInstance().getPreferencesPointer().getColorProperty(COLOR_BY_ACCENTGROUP[accentgroup]);
			} else {
				aColor = Toolbox.getInstance().getPreferencesPointer().getColorProperty(Preferences.PROP_SYLLABLE_COLOR_DEFAULT);
			}
			sb.append(Util.enrichStringWithHTMLFontStyle(token, aColor));
		}
		sb.append("</html>");
		return sb.toString();
	}
}
