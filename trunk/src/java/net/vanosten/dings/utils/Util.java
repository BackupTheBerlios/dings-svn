package net.vanosten.dings.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.vanosten.dings.model.Preferences;

/**
 * Diverse public static utility methods.
 */
public class Util {

	/**
	 * Strip different kinds of excess whitespace from string
	 * @param input
	 * @return a string with no whitespace in front and end and only one space between the parts.
	 */
	public static String stripWhitespace(String input) {
		StringBuilder sb = new StringBuilder();
		List<String> tokens = extractTokensBetweenWhitespace(input);
		for (String token : tokens) {
			if (0 < sb.length()) { //it is not enough to test for index 0, because there might be whitespace
				sb.append(" ");
			}
			sb.append(token);
		}
		return sb.toString();
	} //END public static String stripWhitespace(String)
	
	/**
	 * 
	 * @param input
	 * @return a list of Strings, which do not contain whitespace, and which where part of the whitespace delimitted input string
	 */
	public static List<String> extractTokensBetweenWhitespace(String input) {
		List<String> tokens = new ArrayList<String>();
		String[] parts = input.trim().split("\\s");
		for (int i = 0; i < parts.length; i++) {
			if (0 == parts[i].trim().length()) {
				continue;
			}
			tokens.add(parts[i].trim());
		}
		return tokens;
	}

	
	/*-------------------------String to Color conversion----------------------------------------*/
		
	/** The delimitter for Color */
	public final static String COLOR_DELIMITTER = "-";
	
	/**
	 * 
	 * @param rgb String containing integer values for red, green and blue separated by COLOR_DELIMITTER
	 * @return a Color object, if the string could be parsed. Null otherwise.
	 */
	public static Color parseRGBToColor(String rgb) {
		if (null == rgb) {
			return null;
		}
		String[] elements = rgb.split(COLOR_DELIMITTER);
		if (3 != elements.length) {
			//FIXME: log this
			return null;
		}
		int red = 0;
		int blue = 0;
		int green = 0;
		try {
			red = Integer.parseInt(elements[0]);
			green = Integer.parseInt(elements[1]);
			blue = Integer.parseInt(elements[2]);
		} catch (NumberFormatException e) {
			//FIXME: log this
			return null;
		}
		return new Color(red, green, blue);
	}
	
	/**
	 * 
	 * @param rgb
	 * @return a String containing integer values for red, green and blue separated by COLOR_DELIMITTER.
	 *         Null if rgb is null.
	 */
	public static String convertRGB(Color rgb) {
		if (null == rgb) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(rgb.getRed()).append(COLOR_DELIMITTER);
		sb.append(rgb.getGreen()).append(COLOR_DELIMITTER);
		sb.append(rgb.getBlue());
		return sb.toString();
	}
	
	/**
	 * 
	 * @param aColor
	 * @return a String representing the color in hexadecimal format for html
	 */
	public final static String convertColorToHexString(Color aColor) {
		String str = Integer.toHexString(aColor.getRGB() & 0xFFFFFF);
		str = ("#" + "000000".substring(str.length()) + str.toUpperCase());
		return str;
	}
	
	/*-------------------------Syllables----------------------------------------*/
	public final static String AGRAVE = "à";
	public final static String AACUTE = "á";
	public final static String ACIRCUMFLEX = "â";
	public final static String AMACRON = "ā";
	public final static String ABREVE = "ă";
	public final static String EGRAVE = "è";
	public final static String EACUTE = "é";
	public final static String ECIRCUMFLEX = "ê";
	public final static String EMACRON = "ē";
	public final static String EBREVE = "ĕ";
	public final static String IGRAVE = "ì";
	public final static String IACUTE = "í";
	public final static String ICIRCUMFLEX = "î";
	public final static String IMACRON = "ī";
	public final static String IBREVE = "ĭ";
	public final static String OGRAVE = "ò";
	public final static String OACUTE = "ó";
	public final static String OCIRCUMFLEX = "ô";
	public final static String OMACRON = "ō";
	public final static String OBREVE = "ŏ";
	public final static String UGRAVE = "ù";
	public final static String UACUTE = "ú";
	public final static String UCIRCUMFLEX = "û";
	public final static String UMACRON = "ū";
	public final static String UBREVE = "ŭ";
	
	public final static String[][] ACCENTS_BY_LETTERGROUP = {
		{AGRAVE,AACUTE,ACIRCUMFLEX,AMACRON,ABREVE}
		, {EGRAVE,EACUTE,ECIRCUMFLEX,EMACRON,EBREVE}
		, {IGRAVE,IACUTE,ICIRCUMFLEX,IMACRON,IBREVE}
		, {OGRAVE,OACUTE,OCIRCUMFLEX,OMACRON,OBREVE}
		, {UGRAVE,UACUTE,UCIRCUMFLEX,UMACRON,UBREVE}
	};
	
	public final static String[][] ACCENTS_BY_ACCENTGROUP = {
		{AGRAVE,EGRAVE,IGRAVE,OGRAVE,UGRAVE}
		, {AACUTE,EACUTE,IACUTE,OACUTE,UACUTE}
		, {ACIRCUMFLEX,ECIRCUMFLEX,ICIRCUMFLEX,OCIRCUMFLEX,UCIRCUMFLEX}
		, {AMACRON,EMACRON,IMACRON,OMACRON,UMACRON}
		, {ABREVE,EBREVE,IBREVE,OBREVE,UBREVE}
	};
	
	public final static String[] COLOR_BY_ACCENTGROUP = {
		Preferences.PROP_SYLLABLE_COLOR_GRAVE
		, Preferences.PROP_SYLLABLE_COLOR_ACUTE
		, Preferences.PROP_SYLLABLE_COLOR_CIRCUMFLEX
		, Preferences.PROP_SYLLABLE_COLOR_MACRON
		, Preferences.PROP_SYLLABLE_COLOR_BREVE
	};
	
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
		List<String> tokens = extractTokensBetweenWhitespace(text);
		
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
			sb.append(enrichStringWithHTMLFontStyle(token, aColor));
		}
		sb.append("</html>");
		return sb.toString();
	}
	
	/**
	 * @param text
	 * @param aColor
	 * @return the input text surrounded by html 3.2 font tag including the color as a hexadecimal string
	 */
	public final static String enrichStringWithHTMLFontStyle(String text, Color aColor) {
		StringBuilder sb = new StringBuilder(27+text.length());
		sb.append("<font color=");
		sb.append(convertColorToHexString(aColor));
		sb.append(">");
		sb.append(text);
		sb.append("</font>");
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @param text
	 * @return the input text surrounded by a html-tag
	 */
	public final static String enrichStringWithHTML(String text) {
		StringBuilder sb = new StringBuilder(13+text.length());
		sb.append("<html>").append(text).append("</html>");
		return sb.toString();
	}
}
