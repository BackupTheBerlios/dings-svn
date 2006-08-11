package net.vanosten.dings.utils;

import java.awt.Color;

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
		String[] parts = input.trim().split("\\s");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < parts.length; i++) {
			if (0 == parts[i].trim().length()) {
				continue;
			}
			if (0 < sb.length()) { //it is not enough to test for index 0, because there might be whitespace
				sb.append(" ");
			}
			sb.append(parts[i]);
		}
		return sb.toString();
	} //END public static String stripWhitespace(String)

	
	/*-------------------------String to Color conversion----------------------------------------*/
		
	/** The delimitter for Color */
	public final static String COLOR_DELIMITTER = "-";
	
	/**
	 * 
	 * @param rgb String containing integer values for red, green and blue separated by COLOR_DELIMITTER
	 * @return a Color object, if the string could be parsed. Null otherwise.
	 */
	public static Color parseRGB(String rgb) {
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
	
	public final static String[][] ACCENTS_BY_GROUP = {
		{AGRAVE,AACUTE,ACIRCUMFLEX,AMACRON,ABREVE}
		, {EGRAVE,EACUTE,ECIRCUMFLEX,EMACRON,EBREVE}
		, {IGRAVE,IACUTE,ICIRCUMFLEX,IMACRON,IBREVE}
		, {OGRAVE,OACUTE,OCIRCUMFLEX,OMACRON,OBREVE}
		, {UGRAVE,UACUTE,UCIRCUMFLEX,UMACRON,UBREVE}
	};
	
}
