/*
 * Constants.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2002, 03, 04, 2005 Rick Gruber-Riemer (rick@vanosten.net)
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
package net.vanosten.dings.consts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import java.util.logging.Logger; 
import java.util.logging.Level;

/**
 * Defines the constants for the ADings application.
 * Has static methods to convert from numbers as Strings to boolean values and vice versa.
 *
 * @author  Rick Gruber, <a href="mailto:rick@vanosten.net">rick@vanosten.net</a>
 * @version 1.0.0
 */
public class Constants  {

	/**
	* Private constructor, so constructor can not be called
	 */
	private Constants() {
		//noting to initialize
	}

	/** The lineSeparator to be used. See method Constants.getLineSeparator() */
	private static String lineSeparator = null;

	/**
	 * Gets the system value of the line separator.
	 * @param aBool
	 * @return
	 */
	public static String getLineSeparator() {
		if (null == lineSeparator) {
			lineSeparator = System.getProperty("line.separator");
		}
		if (null == lineSeparator) {
			lineSeparator = "\n";
		}
		return lineSeparator;
	} //END public static String getLineSeparator()
	
	/**
	 * Returns a String as a null value, if the input
	 * String equals to the <code>NULL_STRING</code> constant.
	 * Otherwise the original String is returned.
	 * 
	 * @param String aString - the original String
	 * @return String - null if the original String equals to a specific Constant
	 */
	public static String resolveNullString(String aString) {
		if (aString.equals(NULL_STRING)) {
			return null;
		}
		return aString;
	} //END public String resolveNullString(String)

	/**
	 * Makes an XML-tagged string with a string as variable.
	 *
	 * @param String tag - the name of the XML-tag
	 * @param String value - the enclosed value of the tag
	 * @return String _ XML start and end tag around value
	 */
	public static String getXMLTaggedValue(String aTag, String aValue) {
		StringBuffer xml = new StringBuffer();
		xml.append("<").append(aTag).append(">");
		xml.append(getXMLEntitiesReplacedString(aValue));
		xml.append("</").append(aTag).append(">");
		return xml.toString();
	} //END public static Sting getXMLTaggedValue(String, String)
	
	/**
	 * Makes an XML-tagged string with an int as variable
	 *
	 * @param String tag - the name of the XML-tag
	 * @param String value - the enclosed value of the tag
	 * @return String _ XML start and end tag around value
	 */
	 public static String getXMLTaggedValue(String aTag, int aValue) {
	 	return getXMLTaggedValue(aTag, Integer.toString(aValue));
	 } //END public static String getXMLTaggedValue(String, int)

	/**
	 * Make a String that represents an Attribute within an XML tag.
	 *
	 * @param anAttribute - the name of the attribute
	 * @param aValue - the value of the attribute
	 * @return String - anAttribute = "aValue"
	 */
	public static String getXMLFormattedAttribute(String anAttribute, String aValue) {
		StringBuffer xml = new StringBuffer();
		xml.append(SPACE_STRING);
		xml.append(anAttribute);
		xml.append("=\"").append(getXMLEntitiesReplacedString(aValue)).append("\"");
		return xml.toString();
	} //END public static String getXMLFormattedAttribute(String, String)

	private static String XMLEntityDefList[][] = {{"&amp;", "&"}, {"&lt;", "<"},{"&gt;", ">"}, {"&apos;", "\'"}, {"&quot;","\""}};

	/**
	 * Makes replacements in strings, so the basic XML entity definitions are translated.
	 */
	public static String getXMLEntitiesReplacedString(String input) {
		StringBuffer sb = new StringBuffer(input);

		int index; //the index position of a found String

		//loop through all entities we want to catch
		for (int i = 0; i < XMLEntityDefList.length; i++) {
			index = sb.indexOf(XMLEntityDefList[i][1]);
			while (index >= 0 && (index < sb.length())) {
				//prevent replacing the ampersand in "&amp;" -> infinite loop
				if (index != sb.indexOf("&amp;", index)) {
					//replace the string with an XML representation
					sb.replace(index, index + 1, XMLEntityDefList[i][0]);
				}

				//look for more string
				index = sb.indexOf(XMLEntityDefList[i][1], index + 1);
			}
		}
		//return the StringBuffer as a String
		return sb.toString();
	} //END public static String getXMLEntitiesReplacedString(String)

	/*-------------------------Exception printing------------------------------------------------*/
    
    /**
     * Returns the message and the stack trace elements of a Throwable in a single String.
     *
     * @param e The original Throwable
     * @return The message and stack trace elements delimitted by a line-feed character
     */
    public static String getThrowableStackTrace(Throwable e) {
        StringBuffer sb = new StringBuffer();
        sb.append("Stack trace of error:\n");
        sb.append(e.toString()).append("\n");
        sb.append(e.getMessage()).append("\n");
        StackTraceElement[] elements = e.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            sb.append(elements[i].toString()).append("\n\n");
        }
        return sb.toString();
    } //END public static String getThrowableStackTrace(Throwable)

	/*---- Logging ----------------------------------------------------------------*/

	/** The name of the application logging file */
	public final static String LOGGING_FILE_NAME = "dings.log";
	
	/*---- Messages ----------------------------------------------------------------*/
	//Message type
	public final static int INFORMATION_MESSAGE = 101;
	public final static int ERROR_MESSAGE = 102;
	public final static int WARNING_MESSAGE = 103;
	public final static int QUESTION_MESSAGE = 104;
	
	//option Type
	public final static int YES_NO_OPTION = 201;
	public final static int YES_NO_CANCEL_OPTION = 202;
	
	//result type
	public final static int YES_OPTION = 301;
	public final static int NO_OPTION = 302;
	public final static int CANCEL_OPTION = 303;
	//public final static int OK_OPTION = 304;
	//public final static int CLOSED_OPTION = 305; 
	public final static String CANCELLED_INPUT = "CANCELLED_INPUT";

	/*---- IDs RELATED ------------------------------------------------------------*/

	/** The prefix for an Entry id */
	public final static String PREFIX_ENTRY = "E";

	/** The prefix for a Unit id */
	public final static String PREFIX_UNIT = "U";

	/** The prefix for a Unit id */
	public final static String PREFIX_CATEGORY = "C";

	/** The prefix for a EntryType id */
	public final static String PREFIX_ENTRYTYPE = "ET";
	
	/** The prefix for a EntryTypeAttribute id */
	public final static String PREFIX_ENTRTYPE_ATTRIBUTE = "ETA";

	/** The prefix for an EntryTypeAttributeItem id */
	public final static String PREFIX_ENTRYTYPE_ATTRIBUTE_ITEM = "ETAI";

	/* The id for a new element */
	public final static String ID_NEW = "-1";

	/* The delimitter between attributes in AppEvents */
	public final static String DELIMITTER_APP_EVENT = "#";

	/*---- XML elements ---------------------------------------------------------*/
	//common attributes
	public final static String TRUE = "true";
	public final static String FALSE = "false";
	public final static String UNDEFINED = "?!?";
	public final static String EMPTY_STRING = "";
	public final static String SPACE_STRING = " ";
	public final static String NULL_STRING = "(null)";
	public final static String XML_ATTR_ID = "eid";
	public final static String XML_ATTR_UNIT = "u";
	public final static String XML_ATTR_CATEGORY = "c";
	public final static String XML_ATTR_SCORE = "lv"; //lv is based on level, which was the old name
	public final static String XML_ATTR_STATUS = "st";
	public final static String XML_ATTR_NAME = "n";
	public final static String XML_ATTR_ENTRYTYPE = "et";
	public final static String XML_ATTR_ATTRIBUTEONE = "a1";
	public final static String XML_ATTR_ATTRIBUTETWO = "a2";
	public final static String XML_ATTR_ATTRIBUTETHREE = "a3";
	public final static String XML_ATTR_ATTRIBUTEFOUR = "a4";
	public final static String XML_ATTR_DEFAULTITEM = "di"; //short for defaultItem
	public final static String XML_ATTR_IRREGULAR = "ir"; //short for irregular
	public final static String XML_ATTR_LAST_UPD = "lu";
	public final static String XML_ATTR_LAST_LEARNED = "ll"; //only for Entry
	
	//core info
	public final static String XML_BASE = "o"; //was origin
	public final static String XML_TARGET = "d"; //was destination
	public final static String XML_EXPLANATION = "ep";
	public final static String XML_PRONUNCIATION = "p";
	public final static String XML_EXAMPLE = "ea";
	public final static String XML_RELATION = "r";

	//unit and category
	public final static String XML_NAME = "name";
	public final static String XML_DESCRIPTION = "desc";

	//entry types
	public final static String XML_ENTRYTYPES = "entrytypes";
	public final static String XML_ENTRYTYPE = "entrytype";
	
	//EntryTypeAttribute
	public final static String XML_ENTRYTYPE_ATTRIBUTES = "etattributes";
	public final static String XML_ENTRYTYPE_ATTRIBUTE = "eta";

	//entry types attribute items
	public final static String XML_ENTRYTYPE_ATTRIBUTE_ITEM = "etai";

	//info about vocabulary
	public final static String XML_TITLE = "title";
	public final static String XML_AUTHOR = "author";
	public final static String XML_NOTES = "notes";
	public final static String XML_LICENCE = "licence";
	public final static String XML_COPYRIGHT = "copyright";
	public final static String XML_BASE_LABEL = "blabel";
	public final static String XML_TRAGET_LABEL = "tlabel";
	public final static String XML_ATTRIBUTES_LABEL = "alabel";
	public final static String XML_UNIT_LABEL = "ulabel";
	public final static String XML_CATEGORY_LABEL = "clabel";
	public final static String XML_OTHERS_LABEL = "olabel";
	public final static String XML_EXPLANATION_LABEL = "explabel";
	public final static String XML_EXAMPLE_LABEL = "exlabel";
	public final static String XML_BASE_LOCALE = "bor"; //or is legacy from "orientation"
	public final static String XML_TARGET_LOCALE = "tor";
	public final static String XML_ATTRIBUTES_LOCALE = "aor";
	public final static String XML_UNIT_LOCALE = "uor";
	public final static String XML_CATEGORY_LOCALE = "cor";
	public final static String XML_EXPLANATION_LOCALE = "expor";
	public final static String XML_EXAMPLE_LOCALE = "exor";
	public final static String XML_PRONUNCIATION_LOCALE = "por";
	public final static String XML_RELATION_LOCALE = "ror";
	public final static String XML_VISIBILITY_ATTRIBUTES = "visa";
	public final static String XML_VISIBILITY_UNIT = "visu";
	public final static String XML_VISIBILITY_CATEGORY = "viscat";
	public final static String XML_VISIBILITY_EXPLANATION = "visexp";
	public final static String XML_VISIBILITY_EXAMPLE = "visex";
	public final static String XML_VISIBILITY_PRONUNCIATION = "vispro";
	public final static String XML_VISIBILITY_RELATION = "visrel";
	
	//general
	public final static String XML_VOCABULARY = "vocabulary";
	public final static String XML_INFO = "info";
	public final static String XML_UNIT = "unit";
	public final static String XML_UNITS = "units";
	public final static String XML_CATEGORY = "category";
	public final static String XML_CATEGORIES = "categories";
	public final static String XML_ENTRY = "e";
	public final static String XML_ENTRIES = "entries";
	
	//statistics
	public final static String XML_STATS = "stats";
	public final static String XML_STATS_SET = "sset";
	public final static String XML_STATS_TIMESTAMP = "ts";
	public final static String XML_STATS_NOF_ENTRIES = "sne";
	
	/*---- Date Parsing -------------------------------------------------------*/
	/** The format of a date String */
	public final static String DATE_FORMAT = "yyyyMMdd HH:mm:ss z";
		
	/**
	 * Formats a date as a String with the format 
	 * YYYYMMDD HH24:MI:SS.
	 * @return String date - a date as a formatted String
	 */
	public final static String getDateString(Date aDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		return sdf.format(aDate);
	} //END protected final String getDateString()
	
	/**
	 * Sets a date by converting a String representation of the date.
	 * If something goes wrong, then a default date is used.
	 * 
	 * @param String aDateString - the last updated date as a String (YYYYMMDD HH24:MI:SS)
	 * @param Date defaultDate - a default date if parsing of the String fails
	 * @param Logger aLogger - the logger to log failure to
	 */
	public final static Date getDateFromString(String aDateString, Date defaultDate, Logger aLogger) {
		if (null == aDateString) {
			return defaultDate;
		}
		else {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
				return sdf.parse(aDateString);
			}
			catch(ParseException e) {
				if (aLogger.isLoggable(Level.FINEST)) {
					aLogger.logp(Level.FINEST, "Constants", "getDateFromString", "unable to parse " + aDateString + "; " + e.toString());
				}
				return defaultDate;
			}
		}	
	} //END public final static Date getDateFromString(String, Date, Logger)
	
	/*------------------------- Locales ------------------------------------------*/
	
	/** All supported locales as an array of Strings, where the first element is a
	 * combination of languagecode_COUNTRYCODE (e.g. en_US) and the display name
	 */
	private static String[][] SUPPORTED_LOCALES;
	
	/** The default locale is set to en_US to have best guarantee for support */
	public final static Locale DEFAULT_LOCALE = new Locale("en", "US");
	
	/**
	 * Initializes the array with supported locales.
	 * The current list is built upon the following:
	 * <code>java.util.Locale list[] = java.text.DateFormat.getAvailableLocales();
	 * for (int i = 0; i < list.length; i++) {
	 *     System.out.println("SUPPORTED_LOCALES[x] = new String[2];");
	 *     System.out.println("SUPPORTED_LOCALES[x][0] = \"" + list[i] + "\";");
	 *     System.out.println("SUPPORTED_LOCALES[x][1] = \"" +  list[i].getDisplayName() + "\";");
	 * }
	 * </code>
	 *
	 * @param rb The ResourceBundle with the language specific translation
	 */
	private static void initializeSupportedLocales(ResourceBundle rb) {
		SUPPORTED_LOCALES = new String[95][];
		SUPPORTED_LOCALES[0] = new String[2];
		SUPPORTED_LOCALES[0][0] = "sq_AL";
		SUPPORTED_LOCALES[0][1] = "Albanian (Albania)";
		SUPPORTED_LOCALES[1] = new String[2];
		SUPPORTED_LOCALES[1][0] = "ar_AE";
		SUPPORTED_LOCALES[1][1] = "Arabic (United Arab Emirates)";
		SUPPORTED_LOCALES[2] = new String[2];
		SUPPORTED_LOCALES[2][0] = "ar_BH";
		SUPPORTED_LOCALES[2][1] = "Arabic (Bahrain)";
		SUPPORTED_LOCALES[3] = new String[2];
		SUPPORTED_LOCALES[3][0] = "ar_DZ";
		SUPPORTED_LOCALES[3][1] = "Arabic (Algeria)";
		SUPPORTED_LOCALES[4] = new String[2];
		SUPPORTED_LOCALES[4][0] = "ar_EG";
		SUPPORTED_LOCALES[4][1] = "Arabic (Egypt)";
		SUPPORTED_LOCALES[5] = new String[2];
		SUPPORTED_LOCALES[5][0] = "ar_IQ";
		SUPPORTED_LOCALES[5][1] = "Arabic (Iraq)";
		SUPPORTED_LOCALES[6] = new String[2];
		SUPPORTED_LOCALES[6][0] = "ar_JO";
		SUPPORTED_LOCALES[6][1] = "Arabic (Jordan)";
		SUPPORTED_LOCALES[7] = new String[2];
		SUPPORTED_LOCALES[7][0] = "ar_KW";
		SUPPORTED_LOCALES[7][1] = "Arabic (Kuwait)";
		SUPPORTED_LOCALES[8] = new String[2];
		SUPPORTED_LOCALES[8][0] = "ar_LB";
		SUPPORTED_LOCALES[8][1] = "Arabic (Lebanon)";
		SUPPORTED_LOCALES[9] = new String[2];
		SUPPORTED_LOCALES[9][0] = "ar_LY";
		SUPPORTED_LOCALES[9][1] = "Arabic (Libya)";
		SUPPORTED_LOCALES[10] = new String[2];
		SUPPORTED_LOCALES[10][0] = "ar_MA";
		SUPPORTED_LOCALES[10][1] = "Arabic (Morocco)";
		SUPPORTED_LOCALES[11] = new String[2];
		SUPPORTED_LOCALES[11][0] = "ar_OM";
		SUPPORTED_LOCALES[11][1] = "Arabic (Oman)";
		SUPPORTED_LOCALES[12] = new String[2];
		SUPPORTED_LOCALES[12][0] = "ar_QA";
		SUPPORTED_LOCALES[12][1] = "Arabic (Qatar)";
		SUPPORTED_LOCALES[13] = new String[2];
		SUPPORTED_LOCALES[13][0] = "ar_SA";
		SUPPORTED_LOCALES[13][1] = "Arabic (Saudi Arabia)";
		SUPPORTED_LOCALES[14] = new String[2];
		SUPPORTED_LOCALES[14][0] = "ar_SD";
		SUPPORTED_LOCALES[14][1] = "Arabic (Sudan)";
		SUPPORTED_LOCALES[15] = new String[2];
		SUPPORTED_LOCALES[15][0] = "ar_SY";
		SUPPORTED_LOCALES[15][1] = "Arabic (Syria)";
		SUPPORTED_LOCALES[16] = new String[2];
		SUPPORTED_LOCALES[16][0] = "ar_TN";
		SUPPORTED_LOCALES[16][1] = "Arabic (Tunisia)";
		SUPPORTED_LOCALES[17] = new String[2];
		SUPPORTED_LOCALES[17][0] = "ar_YE";
		SUPPORTED_LOCALES[17][1] = "Arabic (Yemen)";
		SUPPORTED_LOCALES[18] = new String[2];
		SUPPORTED_LOCALES[18][0] = "be_BY";
		SUPPORTED_LOCALES[18][1] = "Byelorussian (Belarus)";
		SUPPORTED_LOCALES[19] = new String[2];
		SUPPORTED_LOCALES[19][0] = "bg_BG";
		SUPPORTED_LOCALES[19][1] = "Bulgarian (Bulgaria)";
		SUPPORTED_LOCALES[20] = new String[2];
		SUPPORTED_LOCALES[20][0] = "ca_ES";
		SUPPORTED_LOCALES[20][1] = "Catalan (Spain)";
		SUPPORTED_LOCALES[21] = new String[2];
		SUPPORTED_LOCALES[21][0] = "zh_CN";
		SUPPORTED_LOCALES[21][1] = "Chinese (China)";
		SUPPORTED_LOCALES[22] = new String[2];
		SUPPORTED_LOCALES[22][0] = "zh_HK";
		SUPPORTED_LOCALES[22][1] = "Chinese (Hong Kong)";
		SUPPORTED_LOCALES[23] = new String[2];
		SUPPORTED_LOCALES[23][0] = "zh_TW";
		SUPPORTED_LOCALES[23][1] = "Chinese (Taiwan)";
		SUPPORTED_LOCALES[24] = new String[2];
		SUPPORTED_LOCALES[24][0] = "hr_HR";
		SUPPORTED_LOCALES[24][1] = "Croatian (Croatia)";
		SUPPORTED_LOCALES[25] = new String[2];
		SUPPORTED_LOCALES[25][0] = "cs_CZ";
		SUPPORTED_LOCALES[25][1] = "Czech (Czech Republic)";
		SUPPORTED_LOCALES[26] = new String[2];
		SUPPORTED_LOCALES[26][0] = "da_DK";
		SUPPORTED_LOCALES[26][1] = "Danish (Denmark)";
		SUPPORTED_LOCALES[27] = new String[2];
		SUPPORTED_LOCALES[27][0] = "nl_BE";
		SUPPORTED_LOCALES[27][1] = "Dutch (Belgium)";
		SUPPORTED_LOCALES[28] = new String[2];
		SUPPORTED_LOCALES[28][0] = "nl_NL";
		SUPPORTED_LOCALES[28][1] = "Dutch (Netherlands)";
		SUPPORTED_LOCALES[29] = new String[2];
		SUPPORTED_LOCALES[29][0] = "de_AT";
		SUPPORTED_LOCALES[29][1] = "German (Austria)";
		SUPPORTED_LOCALES[30] = new String[2];
		SUPPORTED_LOCALES[30][0] = "de_CH";
		SUPPORTED_LOCALES[30][1] = "German (Switzerland)";
		SUPPORTED_LOCALES[31] = new String[2];
		SUPPORTED_LOCALES[31][0] = "de_DE";
		SUPPORTED_LOCALES[31][1] = "German (Germany)";
		SUPPORTED_LOCALES[32] = new String[2];
		SUPPORTED_LOCALES[32][0] = "de_LU";
		SUPPORTED_LOCALES[32][1] = "German (Luxembourg)";
		SUPPORTED_LOCALES[33] = new String[2];
		SUPPORTED_LOCALES[33][0] = "el_GR";
		SUPPORTED_LOCALES[33][1] = "Greek (Greece)";
		SUPPORTED_LOCALES[34] = new String[2];
		SUPPORTED_LOCALES[34][0] = "en_AU";
		SUPPORTED_LOCALES[34][1] = "English (Australia)";
		SUPPORTED_LOCALES[35] = new String[2];
		SUPPORTED_LOCALES[35][0] = "en_CA";
		SUPPORTED_LOCALES[35][1] = "English (Canada)";
		SUPPORTED_LOCALES[36] = new String[2];
		SUPPORTED_LOCALES[36][0] = "en_GB";
		SUPPORTED_LOCALES[36][1] = "English (United Kingdom)";
		SUPPORTED_LOCALES[37] = new String[2];
		SUPPORTED_LOCALES[37][0] = "en_IE";
		SUPPORTED_LOCALES[37][1] = "English (Ireland)";
		SUPPORTED_LOCALES[38] = new String[2];
		SUPPORTED_LOCALES[38][0] = "en_IN";
		SUPPORTED_LOCALES[38][1] = "English (India)";
		SUPPORTED_LOCALES[39] = new String[2];
		SUPPORTED_LOCALES[39][0] = "en_NZ";
		SUPPORTED_LOCALES[39][1] = "English (New Zealand)";
		SUPPORTED_LOCALES[40] = new String[2];
		SUPPORTED_LOCALES[40][0] = "en_ZA";
		SUPPORTED_LOCALES[40][1] = "English (South Africa)";
		SUPPORTED_LOCALES[41] = new String[2];
		SUPPORTED_LOCALES[41][0] = "en_US";
		SUPPORTED_LOCALES[41][1] = "English (United States)";
		SUPPORTED_LOCALES[42] = new String[2];
		SUPPORTED_LOCALES[42][0] = "es_AR";
		SUPPORTED_LOCALES[42][1] = "Spanish (Argentina)";
		SUPPORTED_LOCALES[43] = new String[2];
		SUPPORTED_LOCALES[43][0] = "es_BO";
		SUPPORTED_LOCALES[43][1] = "Spanish (Bolivia)";
		SUPPORTED_LOCALES[44] = new String[2];
		SUPPORTED_LOCALES[44][0] = "es_CL";
		SUPPORTED_LOCALES[44][1] = "Spanish (Chile)";
		SUPPORTED_LOCALES[45] = new String[2];
		SUPPORTED_LOCALES[45][0] = "es_CO";
		SUPPORTED_LOCALES[45][1] = "Spanish (Colombia)";
		SUPPORTED_LOCALES[46] = new String[2];
		SUPPORTED_LOCALES[46][0] = "es_CR";
		SUPPORTED_LOCALES[46][1] = "Spanish (Costa Rica)";
		SUPPORTED_LOCALES[47] = new String[2];
		SUPPORTED_LOCALES[47][0] = "es_DO";
		SUPPORTED_LOCALES[47][1] = "Spanish (Dominican Republic)";
		SUPPORTED_LOCALES[48] = new String[2];
		SUPPORTED_LOCALES[48][0] = "es_EC";
		SUPPORTED_LOCALES[48][1] = "Spanish (Ecuador)";
		SUPPORTED_LOCALES[49] = new String[2];
		SUPPORTED_LOCALES[49][0] = "es_ES";
		SUPPORTED_LOCALES[49][1] = "Spanish (Spain)";
		SUPPORTED_LOCALES[50] = new String[2];
		SUPPORTED_LOCALES[50][0] = "es_GT";
		SUPPORTED_LOCALES[50][1] = "Spanish (Guatemala)";
		SUPPORTED_LOCALES[51] = new String[2];
		SUPPORTED_LOCALES[51][0] = "es_HN";
		SUPPORTED_LOCALES[51][1] = "Spanish (Honduras)";
		SUPPORTED_LOCALES[52] = new String[2];
		SUPPORTED_LOCALES[52][0] = "es_MX";
		SUPPORTED_LOCALES[52][1] = "Spanish (Mexico)";
		SUPPORTED_LOCALES[53] = new String[2];
		SUPPORTED_LOCALES[53][0] = "es_NI";
		SUPPORTED_LOCALES[53][1] = "Spanish (Nicaragua)";
		SUPPORTED_LOCALES[54] = new String[2];
		SUPPORTED_LOCALES[54][0] = "es_PA";
		SUPPORTED_LOCALES[54][1] = "Spanish (Panama)";
		SUPPORTED_LOCALES[55] = new String[2];
		SUPPORTED_LOCALES[55][0] = "es_PE";
		SUPPORTED_LOCALES[55][1] = "Spanish (Peru)";
		SUPPORTED_LOCALES[56] = new String[2];
		SUPPORTED_LOCALES[56][0] = "es_PR";
		SUPPORTED_LOCALES[56][1] = "Spanish (Puerto Rico)";
		SUPPORTED_LOCALES[57] = new String[2];
		SUPPORTED_LOCALES[57][0] = "es_PY";
		SUPPORTED_LOCALES[57][1] = "Spanish (Paraguay)";
		SUPPORTED_LOCALES[58] = new String[2];
		SUPPORTED_LOCALES[58][0] = "es_SV";
		SUPPORTED_LOCALES[58][1] = "Spanish (El Salvador)";
		SUPPORTED_LOCALES[59] = new String[2];
		SUPPORTED_LOCALES[59][0] = "es_UY";
		SUPPORTED_LOCALES[59][1] = "Spanish (Uruguay)";
		SUPPORTED_LOCALES[60] = new String[2];
		SUPPORTED_LOCALES[60][0] = "es_VE";
		SUPPORTED_LOCALES[60][1] = "Spanish (Venezuela)";
		SUPPORTED_LOCALES[61] = new String[2];
		SUPPORTED_LOCALES[61][0] = "et_EE";
		SUPPORTED_LOCALES[61][1] = "Estonian (Estonia)";
		SUPPORTED_LOCALES[62] = new String[2];
		SUPPORTED_LOCALES[62][0] = "fi_FI";
		SUPPORTED_LOCALES[62][1] = "Finnish (Finland)";
		SUPPORTED_LOCALES[63] = new String[2];
		SUPPORTED_LOCALES[63][0] = "fr_BE";
		SUPPORTED_LOCALES[63][1] = "French (Belgium)";
		SUPPORTED_LOCALES[64] = new String[2];
		SUPPORTED_LOCALES[64][0] = "fr_CA";
		SUPPORTED_LOCALES[64][1] = "French (Canada)";
		SUPPORTED_LOCALES[65] = new String[2];
		SUPPORTED_LOCALES[65][0] = "fr_CH";
		SUPPORTED_LOCALES[65][1] = "French (Switzerland)";
		SUPPORTED_LOCALES[66] = new String[2];
		SUPPORTED_LOCALES[66][0] = "fr_FR";
		SUPPORTED_LOCALES[66][1] = "French (France)";
		SUPPORTED_LOCALES[67] = new String[2];
		SUPPORTED_LOCALES[67][0] = "fr_LU";
		SUPPORTED_LOCALES[67][1] = "French (Luxembourg)";
		SUPPORTED_LOCALES[68] = new String[2];
		SUPPORTED_LOCALES[68][0] = "hi_IN";
		SUPPORTED_LOCALES[68][1] = "Hindi (India)";
		SUPPORTED_LOCALES[69] = new String[2];
		SUPPORTED_LOCALES[69][0] = "iw_IL";
		SUPPORTED_LOCALES[69][1] = "Hebrew (Israel)";
		SUPPORTED_LOCALES[70] = new String[2];
		SUPPORTED_LOCALES[70][0] = "hu_HU";
		SUPPORTED_LOCALES[70][1] = "Hungarian (Hungary)";
		SUPPORTED_LOCALES[71] = new String[2];
		SUPPORTED_LOCALES[71][0] = "is_IS";
		SUPPORTED_LOCALES[71][1] = "Icelandic (Iceland)";
		SUPPORTED_LOCALES[72] = new String[2];
		SUPPORTED_LOCALES[72][0] = "it_CH";
		SUPPORTED_LOCALES[72][1] = "Italian (Switzerland)";
		SUPPORTED_LOCALES[73] = new String[2];
		SUPPORTED_LOCALES[73][0] = "it_IT";
		SUPPORTED_LOCALES[73][1] = "Italian (Italy)";
		SUPPORTED_LOCALES[74] = new String[2];
		SUPPORTED_LOCALES[74][0] = "ja_JP";
		SUPPORTED_LOCALES[74][1] = "Japanese (Japan)";
		SUPPORTED_LOCALES[75] = new String[2];
		SUPPORTED_LOCALES[75][0] = "ko_KR";
		SUPPORTED_LOCALES[75][1] = "Korean (South Korea)";
		SUPPORTED_LOCALES[76] = new String[2];
		SUPPORTED_LOCALES[76][0] = "lv_LV";
		SUPPORTED_LOCALES[76][1] = "Latvian (Lettish) (Latvia)";
		SUPPORTED_LOCALES[77] = new String[2];
		SUPPORTED_LOCALES[77][0] = "lt_LT";
		SUPPORTED_LOCALES[77][1] = "Lithuanian (Lithuania)";
		SUPPORTED_LOCALES[78] = new String[2];
		SUPPORTED_LOCALES[78][0] = "mk_MK";
		SUPPORTED_LOCALES[78][1] = "Macedonian (Macedonia)";
		SUPPORTED_LOCALES[79] = new String[2];
		SUPPORTED_LOCALES[79][0] = "no_NO";
		SUPPORTED_LOCALES[79][1] = "Norwegian (Norway)";
		SUPPORTED_LOCALES[80] = new String[2];
		SUPPORTED_LOCALES[80][0] = "no_NO_NY";
		SUPPORTED_LOCALES[80][1] = "Norwegian (Norway,Nynorsk)";
		SUPPORTED_LOCALES[81] = new String[2];
		SUPPORTED_LOCALES[81][0] = "pl_PL";
		SUPPORTED_LOCALES[81][1] = "Polish (Poland)";
		SUPPORTED_LOCALES[82] = new String[2];
		SUPPORTED_LOCALES[82][0] = "pt_BR";
		SUPPORTED_LOCALES[82][1] = "Portuguese (Brazil)";
		SUPPORTED_LOCALES[83] = new String[2];
		SUPPORTED_LOCALES[83][0] = "pt_PT";
		SUPPORTED_LOCALES[83][1] = "Portuguese (Portugal)";
		SUPPORTED_LOCALES[84] = new String[2];
		SUPPORTED_LOCALES[84][0] = "ro_RO";
		SUPPORTED_LOCALES[84][1] = "Romanian (Romania)";
		SUPPORTED_LOCALES[85] = new String[2];
		SUPPORTED_LOCALES[85][0] = "ru_RU";
		SUPPORTED_LOCALES[85][1] = "Russian (Russia)";
		SUPPORTED_LOCALES[86] = new String[2];
		SUPPORTED_LOCALES[86][0] = "sr_YU";
		SUPPORTED_LOCALES[86][1] = "Serbian (Yugoslavia)";
		SUPPORTED_LOCALES[87] = new String[2];
		SUPPORTED_LOCALES[87][0] = "sh_YU";
		SUPPORTED_LOCALES[87][1] = "Serbo-Croatian (Yugoslavia)";
		SUPPORTED_LOCALES[88] = new String[2];
		SUPPORTED_LOCALES[88][0] = "sk_SK";
		SUPPORTED_LOCALES[88][1] = "Slovak (Slovakia)";
		SUPPORTED_LOCALES[89] = new String[2];
		SUPPORTED_LOCALES[89][0] = "sl_SI";
		SUPPORTED_LOCALES[89][1] = "Slovenian (Slovenia)";
		SUPPORTED_LOCALES[90] = new String[2];
		SUPPORTED_LOCALES[90][0] = "sv_SE";
		SUPPORTED_LOCALES[90][1] = "Swedish (Sweden)";
		SUPPORTED_LOCALES[91] = new String[2];
		SUPPORTED_LOCALES[91][0] = "th_TH";
		SUPPORTED_LOCALES[91][1] = "Thai (Thailand)";
		SUPPORTED_LOCALES[92] = new String[2];
		SUPPORTED_LOCALES[92][0] = "th_TH_TH";
		SUPPORTED_LOCALES[92][1] = "Thai (Thailand,TH)";
		SUPPORTED_LOCALES[93] = new String[2];
		SUPPORTED_LOCALES[93][0] = "tr_TR";
		SUPPORTED_LOCALES[93][1] = "Turkish (Turkey)";
		SUPPORTED_LOCALES[94] = new String[2];
		SUPPORTED_LOCALES[94][0] = "uk_UA";
		SUPPORTED_LOCALES[94][1] = "Ukrainian (Ukraine)";		
	} //END private static void initializeSupportedLocales()
	
	/**
	 * The supported locales for influence of bidirectional text and
	 * spell-checking.
	 * Please refer to the Java SDK documentation about support for locales
	 * in the actual JVM.
	 *
	 * @param rb The ResourceBundle with the language specific translation
	 * @return The static constant <code>SUPPORTED_LOCALES</code>
	 */
	public static String[][] getSupportedLocales(ResourceBundle rb) {
		if (null == SUPPORTED_LOCALES) {
			initializeSupportedLocales(rb);
		}
		return SUPPORTED_LOCALES;
	} //END public static String[][] getSupportedLocales(Locale)
	
	/**
	 * Parses a string (e.g. "en_US") to a Locale.
	 * For backwards compatibility (version 0.2) int values can be expected
	 * and are then translated to the DEFAULT_LOCALE.
	 * 
	 * @param localeString
	 * @return A Locale based on the info in the input parameter or
	 *         DEFAULT_LOCALE if something goes wrong.
	 */
	public static Locale parseLocale(String localeString) {
		if (null == localeString) {
			return DEFAULT_LOCALE;
		}
		//check for integer
		try {
			Integer.parseInt(localeString);
			return DEFAULT_LOCALE;
		} catch(NumberFormatException e) {
			//nothing to do. It just wasn't an int
		}
		String[] myLocaleInfo = localeString.split("_");
		if (2 != myLocaleInfo.length) {
			return DEFAULT_LOCALE;
		}
		if (myLocaleInfo[0] == null || myLocaleInfo[1] == null
				|| 0 == myLocaleInfo[0].trim().length() || 0 == myLocaleInfo[1].trim().length()) {
			return DEFAULT_LOCALE;
		}
		return new Locale(myLocaleInfo[0].trim(), myLocaleInfo[1].trim());
	} //END public static Locale parseLocale(String)
} //END public class Constants
