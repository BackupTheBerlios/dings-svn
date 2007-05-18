/*
 * Util.java
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.vanosten.dings.model.Preferences;

/**
 * Diverse public static utility methods.
 * May not contain references to Toolbox in static fields because of circular dependency.
 */
public class Util {

	/**
	 * The logging logger. The base package structure is chosen as name
	 * in order to have the other loggers in the divers classes inherit the settings (level).
	 * */
	private static Logger logger = Logger.getLogger("net.vanosten.dings.utils.Util");
	
	/**
	 * Private constructor to prevent instantiation
	 */
	private Util() {}

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
	
	/**
	 * @return true if the version in the resource bundel is newer then the one in properties.
	 */
	public final static boolean isNewProgramVersion() {
		boolean newProgVersion = false;
		int versionNew = Util.convertVerstionStringToInt(Toolbox.getInstance().getLocalizedString("application.version"));
		int versionOld = Util.convertVerstionStringToInt(Toolbox.getInstance().getPreferencesPointer().getProperty(Preferences.PROP_PROG_VERSION));
		newProgVersion = versionNew >= versionOld;
		return newProgVersion;
	}
	
	/**
	 * Converts a version in format "x.y.z" to an int: z + 100*y + 10000*x
	 * @param version
	 * @return
	 */
	public final static int convertVerstionStringToInt(String version) {
		int intVersion = 0;
		if (null != version) {
			String[] versionParts = version.split(".");
			if (3 == versionParts.length) {
				try {
					intVersion = Integer.parseInt(versionParts[2]);
					intVersion += Integer.parseInt(versionParts[1]) * 100;
					intVersion += Integer.parseInt(versionParts[0]) * 10000;
				} catch (NumberFormatException e) {
					logger.info("The version " + version + " is not parsable");
					intVersion = 0;
				}
			}
		}
		return intVersion;
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
	
	//------------------------------ HTML ------------------------------------
	
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
