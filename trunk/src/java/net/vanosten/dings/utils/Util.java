package net.vanosten.dings.utils;

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

}
