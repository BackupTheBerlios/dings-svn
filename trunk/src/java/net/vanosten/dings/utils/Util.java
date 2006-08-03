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

}
