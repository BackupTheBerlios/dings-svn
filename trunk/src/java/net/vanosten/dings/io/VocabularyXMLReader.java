/*
 * VocabularyXMLReader.java
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
package net.vanosten.dings.io;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.vanosten.dings.model.*;
import net.vanosten.dings.utils.Util;
import net.vanosten.dings.consts.Constants;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * This class handels xml-reading for ADings using the pull parser.
 */
public class VocabularyXMLReader implements IOHandler {

	/** Points to the vocabulary file */
	private File vocabularyFile;

	/** The encoding of this file */
	private String encoding;

	/** Holds the units in a collection */
	private HashMap units;

	/** Holds the categories in a collection */
	private HashMap categories;

	/** Holds the entries in a collection */
	private HashMap entries;

	/** Holds the entry types */
	private HashMap entryTypes;

	/** Holds the attributes */
	private HashMap attributes;

	/** Holds the learning statistics */
	private Map<Date,StatisticSet> stats;

	/** Holds the info about the vocabulary */
	private InfoVocab info;

	//DingsItems
	private Unit aUnit;
	private Category aCategory;
	private EntryType anEntryType;
	private EntryTypeAttributeItem anEntryTypeAttributeItem;
	private Entry anEntry;
	private EntryTypeAttribute anEntryTypeAttribute;

	//Info
	private String title;
	private String author;
	private String notes;
	private String copyright;
	private String licence;
	private String blabel = "Base";
	private String tlabel = "Target";
	private String alabel = "Attributes";
	private String ulabel = "Unit";
	private String clabel = "Category";
	private String olabel = "Others";
	private String explabel = "Explanation";
	private String exlabel = "Example";
	private String blocale;
	private String tlocale;
	private String alocale;
	private String ulocale;
	private String clocale;
	private String explocale;
	private String exlocale;
	private String plocale;
	private String rlocale;
	private int visa = InfoVocab.VISIBILITY_ALWAYS;
	private int visu = InfoVocab.VISIBILITY_ALWAYS;
	private int viscat = InfoVocab.VISIBILITY_ALWAYS;
	private int visexp = InfoVocab.VISIBILITY_ALWAYS;
	private int visex = InfoVocab.VISIBILITY_ALWAYS;
	private int vispro = InfoVocab.VISIBILITY_ALWAYS;
	private int visrel = InfoVocab.VISIBILITY_ALWAYS;
	private boolean baseUsesSyllables = false;
	private boolean targetUsesSyllables = false;

	//common attributes
	private Long id;
	private String name;
	private String description;
	private String lastUpd; //also used in statistics for timestamp
	private String lastLearned;
	private Color color;

	//Entry tags
	private String base = Constants.EMPTY_STRING;
	private String target = Constants.EMPTY_STRING;
	private String explanation = Constants.EMPTY_STRING;
	private String pronunciation = Constants.EMPTY_STRING;
	private String example = Constants.EMPTY_STRING;
	private String relation = Constants.EMPTY_STRING;

	//attributes EntryType
	private Long[] attributeIds;

	//attriubutes for EntryTypeAttribute
	private Long aId; //needed because EntryTypeAttributeItems have also id
	private String aLastUpd; //needed because EntryTypeAttributeItems have also lastUpd
	private Long di;
	private ArrayList attributeItems;

	//attributes EntryTypeAttributeItem
	private boolean irregular = false;
	//attributes Entry
	private int score; //also used in statistics to store current score stats
	private boolean status;
	private Long unit;
	private Long category;
	private Long entryType;
	private Long attributeOne;
	private Long attributeTwo;
	private Long attributeThree;
	private Long attributeFour;

	//statistics
	private StatisticSet statSet;
	private int[] numberOfEntries = new int[Entry.SCORE_MAX];

	//helper
	private String currentValue;
	private String foo;

	/** The log4j logger */
	private static Logger logger = Logger.getLogger("VocabularyXMLReader");

	/**
	 * An empty constructor. An DefaultHandler cannot be implemented with static metods only and
	 * without non-static fields.
	 */
	public VocabularyXMLReader() {
		super();
	}   //END VocabularyXMLReader()

	/**
	 * Returns the units in a collection with id as keys
	 *
	 * @return HashMap - the units
	 */
	public HashMap getUnits() {
		return units;
	}   //END public HashMap getUnits()

	/**
	 * Returns the categories in a collection with id as keys
	 *
	 * @return HashMap - the categories
	 */
	public HashMap getCategories() {
		return categories;
	}   //END public HashMap getCategories()

	/**
	 * Returns the entries in a Collection with id as keys
	 *
	 * @return HashMap - the entries
	 */
	public HashMap getEntries() {
		return entries;
	}   //END public HashMap getEntries()

	/**
	 * Returns the EntryTypeAttributes in a Collection with id as keys
	 *
	 * @return HashMap - the EntryTypeAttributes
	 */
	public HashMap getAttributes() {
		return attributes;
	}   //END public HashMap getAttributes()

	/**
	 * Returns the entryTypes in a collection with id as key
	 *
	 * @return
	 */
	public HashMap getEntryTypes() {
		return entryTypes;
	}   //END public HashMap getEntryTypes()

	public InfoVocab getInfo() {
		return info;
	}   //END public InfoVocab getInfo()

	public Map<Date,StatisticSet> getStats() {
		return stats;
	} //END public Map<Date,StatisticSet> getStats()

	//implements IOHandler
	public void setVocabularyFile(String fileName, String anEncoding) throws Exception {
		try {
			//assert file name
			if (fileName == null || fileName.equals(""))
				throw new Exception("the fileName must not be null or \"\"");
			//see if file ok and read-writeable
			vocabularyFile = new File(fileName);
			if (vocabularyFile.isFile() == false)
				throw new Exception("file does not exist or is not a valid file: " + fileName);
			if (vocabularyFile.canRead() == false || vocabularyFile.canWrite() == false)
				throw new Exception("file is not both readable and writeable: " + fileName);
			//set encoding
			encoding = anEncoding;
		}
		catch (Exception e) {   //a SecurityException or one thrown above
			vocabularyFile = null;
			throw new Exception("IOHandler.setVocabularyFile(String): " + e.toString());
		}
	} //END setVocabularyFile(String)

	/**
	 * Controls whether everything is ok to execute <code>readVocabulary()</code>.
	 * For the time being this is just to be sure we have a valid file.
	 *
	 * @return boolean - true if everything is ok.
	 */
	public boolean readyToExecute() {
		boolean allOk = true;
		if (vocabularyFile == null) allOk = false;
		return allOk;
	}   //END readyToExecute()

	/**
	 * This method reads a given vocabulary-xml-file and puts the data back to the definition and unit controllers.
	 * If an error occures during parsing an exception is casted.
	 *
	 * @exception Exception - The method casts an exeption if something goes wrong. The casted exception wraps others.
	 */
	public void execute() throws Exception {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(this.getClass().getName(), "execute");
		}
		//reset the unit and entry holders as well as the key holders
		units = new HashMap();
		categories = new HashMap();
		entries = new HashMap(89);
		entryTypes = new HashMap();
		attributes = new HashMap();
		stats = new HashMap<Date,StatisticSet>();

		//helpers
		boolean hasException = false;
		StringBuffer exceptionSB = new StringBuffer("Problem in VocabularyXMLReader.execute ->\n");

		//pull parser
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(false); //no namespaces in Vocabulary XML
		XmlPullParser xpp = factory.newPullParser();

		//Provide full exception handling
		try {
			FileInputStream fis = new FileInputStream(vocabularyFile);
			InputStreamReader isr = new InputStreamReader(fis, encoding);
			xpp.setInput(isr);
		}
		catch (IOException e) {
			exceptionSB.append("Could not instantiate parser: ").append("\n").append(e.toString());
			hasException = true;
		}

		if (false == hasException) {
			try {
				processDocument(xpp);
			}
			catch (IOException e) {
				exceptionSB.append("IOException while parsing:\n").append(e.toString());
				hasException = true;
			}
		}

		//if something went wrong, cast an exception.
		//the controllers should probably be reset in the application.
		//or it could be done here?
		if (hasException) {
			throw new Exception(exceptionSB.toString());
		}
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(this.getClass().getName(), "execute");
		}
	}   //END execute()

	/**
	 * Traverses the document by pulling the next elements.
	 *
	 * @param XmlPullParser xpp - the pull parser used
	 */
	private void processDocument(XmlPullParser xpp) throws Exception {
		//boolean leave = false;
		int eventType = xpp.getEventType();
		do {
			if(eventType == XmlPullParser.START_DOCUMENT) {
				//System.out.println("Start document");
			} else if(eventType == XmlPullParser.END_DOCUMENT) {
				//System.out.println("End document");
			} else if(eventType == XmlPullParser.START_TAG) {
				processStartElement(xpp);
			} else if(eventType == XmlPullParser.END_TAG) {
				processEndElement(xpp);
			} else if(eventType == XmlPullParser.TEXT) {
				processText(xpp);
			}
			eventType = xpp.next();
		} while (eventType != XmlPullParser.END_DOCUMENT);
	}   //END private void processDocument(XmlPullParser) throws Exception

	/**
	 * Gets the current text and makes some encoding changes.
	 * @param anEvent
	 */
	private void processText(XmlPullParser xpp) {
		currentValue = xpp.getText().trim();
	} //END private void processText(XmlPullParser)

	public void processStartElement(XmlPullParser xpp) {
		String localName = xpp.getName();
		String namespace = xpp.getNamespace();

		//Attributes for Unit
		if (localName.equals(Constants.XML_UNIT)) {
			id = Long.valueOf(xpp.getAttributeValue(namespace, Constants.XML_ATTR_ID));
			lastUpd = xpp.getAttributeValue(namespace,Constants.XML_ATTR_LAST_UPD);
		}

		//Attributes for Category
		if (localName.equals(Constants.XML_CATEGORY)) {
			id = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ID));
			lastUpd = xpp.getAttributeValue(namespace,Constants.XML_ATTR_LAST_UPD);
		}

		//Attributes for EntryType
		else if (localName.equals(Constants.XML_ENTRYTYPE)) {
			name = xpp.getAttributeValue(namespace,Constants.XML_ATTR_NAME);
			id = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ID));
			lastUpd = xpp.getAttributeValue(namespace,Constants.XML_ATTR_LAST_UPD);
			//if attribute does not exist then the parser returns null, which is ok
			attributeIds = new Long[EntryType.NUMBER_OF_ATTRIBUTES];
			attributeIds[0] = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ATTRIBUTEONE));
			attributeIds[1] = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ATTRIBUTETWO));
			attributeIds[2] = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ATTRIBUTETHREE));
			attributeIds[3] = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ATTRIBUTEFOUR));
		}

		//attributes for EntryTypeAttribute
		else if (localName.equals(Constants.XML_ENTRYTYPE_ATTRIBUTE)) {
			aId = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ID));
			name = xpp.getAttributeValue(namespace,Constants.XML_ATTR_NAME);
			di = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_DEFAULTITEM));
			aLastUpd = xpp.getAttributeValue(namespace,Constants.XML_ATTR_LAST_UPD);
			attributeItems = new ArrayList();
		}

		//Attributes for EntryTypeAttributeItem
		else if (localName.equals(Constants.XML_ENTRYTYPE_ATTRIBUTE_ITEM)) {
			id = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ID));
			irregular = Boolean.TRUE.equals(xpp.getAttributeValue(namespace,Constants.XML_ATTR_IRREGULAR));
		}

		//Attributes common to all Entries
		else if (localName.equals(Constants.XML_ENTRY)) {
			entryType = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ENTRYTYPE));
			id = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ID));
			unit = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_UNIT));
			category = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_CATEGORY));
			foo = xpp.getAttributeValue(namespace,Constants.XML_ATTR_SCORE);
			if (foo != null) score = Integer.parseInt(foo);
			else score = 0;
			status = Boolean.parseBoolean(xpp.getAttributeValue(namespace,Constants.XML_ATTR_STATUS));
			//equals to null if attribute does not exist
			attributeOne = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ATTRIBUTEONE));
			attributeTwo = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ATTRIBUTETWO));
			attributeThree = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ATTRIBUTETHREE));
			attributeFour = Long.valueOf(xpp.getAttributeValue(namespace,Constants.XML_ATTR_ATTRIBUTEFOUR));

			lastUpd = xpp.getAttributeValue(namespace,Constants.XML_ATTR_LAST_UPD);
			lastLearned = xpp.getAttributeValue(namespace,Constants.XML_ATTR_LAST_LEARNED);
		}

		//Attributes for StatisticSet
		else if (localName.equals(Constants.XML_STATS_SET)) {
			numberOfEntries = new int[Entry.SCORE_MAX];
			lastUpd = xpp.getAttributeValue(namespace,Constants.XML_STATS_TIMESTAMP);
		}
		else if (localName.equals(Constants.XML_STATS_NOF_ENTRIES)) {
			score = Integer.parseInt(xpp.getAttributeValue(namespace, Constants.XML_ATTR_SCORE));
		}
		//reset currentValue
		currentValue = null;
	} //END processStartElement(XmlPullParser)

	public void processEndElement(XmlPullParser xpp) throws Exception {
		String localName = xpp.getName();
		//Entry tags
		if (localName.equals(Constants.XML_BASE)) base = currentValue;
		else if (localName.equals(Constants.XML_TARGET)) target = currentValue;
		else if (localName.equals(Constants.XML_EXPLANATION)) explanation = currentValue;
		else if (localName.equals(Constants.XML_PRONUNCIATION)) pronunciation = currentValue;
		else if (localName.equals(Constants.XML_EXAMPLE)) example = currentValue;
		else if (localName.equals(Constants.XML_RELATION)) relation = currentValue;

		//EntryTypeAttributeItem
		else if (localName.equals(Constants.XML_ENTRYTYPE_ATTRIBUTE_ITEM)) {
			anEntryTypeAttributeItem = new EntryTypeAttributeItem(id, currentValue, irregular);
			attributeItems.add(anEntryTypeAttributeItem);
		}
		//EntryTypeAttribute
		else if (localName.equals(Constants.XML_ENTRYTYPE_ATTRIBUTE)) {
			List<String> errors = EntryTypeAttribute.validate(name);
			if (errors.size() > 0) {
				throw new Exception(errors.toString());
			}
			else {
				//transform ArrayList of attribute items to array
				EntryTypeAttributeItem theItems[] = new EntryTypeAttributeItem[attributeItems.size()];
				theItems = (EntryTypeAttributeItem[])attributeItems.toArray(theItems);
				//initialize
				anEntryTypeAttribute = new EntryTypeAttribute(aId, name, di, aLastUpd, theItems);
				attributes.put(aId, anEntryTypeAttribute);
				attributeItems = null;
			}
		}

		//EntryType
		else if (localName.equals(Constants.XML_ENTRYTYPE)) {
			List<String> errors = EntryType.validate(name);
			if (errors.size() > 0) {
				throw new Exception(errors.toString());
			}
			else {
				anEntryType = new EntryType(id, name, attributeIds, lastUpd);
				entryTypes.put(id, anEntryType);
			}
		}
		//Unit
		else if (localName.equals(Constants.XML_NAME)) name = currentValue;
		else if (localName.equals(Constants.XML_DESCRIPTION)) description = currentValue;
		else if (localName.equals(Constants.XML_COLOR)) color = Util.parseRGBToColor(currentValue);
		else if (localName.equals(Constants.XML_UNIT)) {
			List<String> errors = Unit.validate(name);
			if (errors.size() > 0) {
				throw new Exception(errors.toString());
			}
			else {
				aUnit = new Unit(id, lastUpd, name, description, color);
				units.put(id, aUnit);
			}
		}
		//Category
		else if (localName.equals(Constants.XML_CATEGORY)) {
			List<String> errors = Category.validate(name);
			if (errors.size() > 0) {
				throw new Exception(errors.toString());
			}
			else {
				aCategory = new Category(id, lastUpd, name, description, color);
				categories.put(id, aCategory);
			}
		}
		//Entry
		else if (localName.equals(Constants.XML_ENTRY)) {
			List<String> errors = Entry.validate(base, target);
			if (errors.size() > 0) {
				throw new Exception(errors.toString());
			}
			else {
				anEntry = new Entry(id, status, score, unit, category, entryType
										, attributeOne, attributeTwo, attributeThree, attributeFour
										, lastUpd, lastLearned
										, base, target
										, pronunciation, explanation
										, example, relation);
				entries.put(id, anEntry);
			}
			resetCommonEntryTags();
		}
		//info
		else if (localName.equals(Constants.XML_TITLE)) {
			title = currentValue;
		}
		else if (localName.equals(Constants.XML_AUTHOR)) {
			author = currentValue;
		}
		else if (localName.equals(Constants.XML_NOTES)) {
			notes = currentValue;
		}
		else if (localName.equals(Constants.XML_COPYRIGHT)) {
			copyright = currentValue;
		}
		else if (localName.equals(Constants.XML_LICENCE)) {
			licence = currentValue;
		}
		else if (localName.equals(Constants.XML_BASE_LABEL)) {
			blabel = currentValue;
		}
		else if (localName.equals(Constants.XML_TRAGET_LABEL)) {
			tlabel = currentValue;
		}
		else if (localName.equals(Constants.XML_ATTRIBUTES_LABEL)) {
			alabel = currentValue;
		}
		else if (localName.equals(Constants.XML_UNIT_LABEL)) {
			ulabel = currentValue;
		}
		else if (localName.equals(Constants.XML_CATEGORY_LABEL)) {
			clabel = currentValue;
		}
		else if (localName.equals(Constants.XML_OTHERS_LABEL)) {
			olabel = currentValue;
		}
		else if (localName.equals(Constants.XML_EXPLANATION_LABEL)) {
			explabel = currentValue;
		}
		else if (localName.equals(Constants.XML_EXAMPLE_LABEL)) {
			exlabel = currentValue;
		}
		else if (localName.equals(Constants.XML_BASE_LOCALE)) {
			blocale = currentValue;
		}
		else if (localName.equals(Constants.XML_TARGET_LOCALE)) {
			tlocale = currentValue;
		}
		else if (localName.equals(Constants.XML_ATTRIBUTES_LOCALE)) {
			alocale = currentValue;
		}
		else if (localName.equals(Constants.XML_UNIT_LOCALE)) {
			ulocale = currentValue;
		}
		else if (localName.equals(Constants.XML_CATEGORY_LOCALE)) {
			clocale = currentValue;
		}
		else if (localName.equals(Constants.XML_EXPLANATION_LOCALE)) {
			explocale = currentValue;
		}
		else if (localName.equals(Constants.XML_EXAMPLE_LOCALE)) {
			exlocale = currentValue;
		}
		else if (localName.equals(Constants.XML_PRONUNCIATION_LOCALE)) {
			plocale = currentValue;
		}
		else if (localName.equals(Constants.XML_RELATION_LOCALE)) {
			rlocale = currentValue;
		}
		else if (localName.equals(Constants.XML_VISIBILITY_ATTRIBUTES)) {
			visa = getValidatedVisibility(Integer.parseInt(currentValue));
		}
		else if (localName.equals(Constants.XML_VISIBILITY_UNIT)) {
			visu = getValidatedVisibility(Integer.parseInt(currentValue));
		}
		else if (localName.equals(Constants.XML_VISIBILITY_CATEGORY)) {
			viscat = getValidatedVisibility(Integer.parseInt(currentValue));
		}
		else if (localName.equals(Constants.XML_VISIBILITY_EXPLANATION)) {
			visexp = getValidatedVisibility(Integer.parseInt(currentValue));
		}
		else if (localName.equals(Constants.XML_VISIBILITY_EXAMPLE)) {
			visex = getValidatedVisibility(Integer.parseInt(currentValue));
		}
		else if (localName.equals(Constants.XML_VISIBILITY_PRONUNCIATION)) {
			vispro = getValidatedVisibility(Integer.parseInt(currentValue));
		}
		else if (localName.equals(Constants.XML_VISIBILITY_RELATION)) {
			visrel = getValidatedVisibility(Integer.parseInt(currentValue));
		}
		else if (localName.equals(Constants.XML_SYLLABLES_BASE)) {
			baseUsesSyllables = Boolean.parseBoolean(currentValue);
		}
		else if (localName.equals(Constants.XML_SYLLABLES_TARGET)) {
			targetUsesSyllables = Boolean.parseBoolean(currentValue);
		}
		else if (localName.equals(Constants.XML_INFO)) {
			ArrayList errors = InfoVocab.validate(title, blabel, tlabel, alabel, ulabel, clabel, olabel, explabel, exlabel);
			if (errors.size() > 0) {
				throw new Exception(errors.toString());
			}
			else {
				info = new InfoVocab(title, author, notes, copyright, licence
						, blabel, tlabel, alabel
						, ulabel, clabel
						, olabel, explabel, exlabel
						, blocale, tlocale, alocale
						, ulocale, clocale
						, explocale, exlocale
						, plocale, rlocale
						,visa,visu,viscat
						,visexp,visex
						,vispro,visrel
						,baseUsesSyllables
						,targetUsesSyllables);
			}
		}
		else if (localName.equals(Constants.XML_STATS_NOF_ENTRIES)) {
			numberOfEntries[score - 1] = Integer.parseInt(currentValue);
		}
		else if (localName.equals(Constants.XML_STATS_SET)) {
			statSet = new StatisticSet(Constants.getDateFromString(lastUpd, new Date(0), logger), numberOfEntries);
			stats.put(statSet.getTimeStamp(), statSet);
		}
	} //END processEndElements(XmlPullParser) throws Exception
		
	/**
	 * Makes sure that a visibility from the information store (xml) is not larger than the 
	 * allowed number. This was introduced in version 0.4.2 for safely simplifying the
	 * visibility structure.
	 * 
	 * @param aVisibility
	 * @return
	 */
	private int getValidatedVisibility(int aVisibility) {
		if (aVisibility > InfoVocab.VISIBILITY_NEVER) {
			return InfoVocab.VISIBILITY_ALWAYS;
		}
		return aVisibility;
	}


	/**
	 * Resets common entry tags
	 */
	private void resetCommonEntryTags() {
		explanation = Constants.EMPTY_STRING;
		pronunciation = Constants.EMPTY_STRING;
		example = Constants.EMPTY_STRING;
		relation = Constants.EMPTY_STRING;
		attributeOne = null;
		attributeTwo = null;
		attributeThree = null;
		attributeFour = null;
	} //END private void resetCommonEntryTags()
} //END public class VocabularyXMLReader implements IOHandler
