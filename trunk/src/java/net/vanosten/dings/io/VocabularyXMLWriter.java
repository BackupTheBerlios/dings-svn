/*
 * VocabularyXMLWriter.java
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
package net.vanosten.dings.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

import net.vanosten.dings.consts.Constants;

/**
 * This class handels xml-writing for ADings. i.e. it can write a xml-file (of a specific structure).
 *
 * @author Rick Gruber, <a href="mailto:rick@vanosten.net">rick@vanosten.net</a>
 * @version 1.0.0, 2001-03-20
 */
public class VocabularyXMLWriter implements IOHandler {

	/** Points to the vocabulary file */
	private File vocabularyFile;

	/** The encoding of this file */
	private String encoding;

	/** Stores the xml-information to be written */
	private ArrayList xmlList;

	/**
	 * Empty consturctor.
	 */
	public VocabularyXMLWriter() {
		super();
	}

	//implements IOHandler
	public void setVocabularyFile(String fileName, String anEncoding) throws Exception {
		try {
			//assert file name
			if (fileName == null || fileName.equals("")) {
				throw new Exception("the fileName must not be null or \"\"");
			}
			//see if file ok and read-writeable
			vocabularyFile = new File(fileName);
			encoding = anEncoding;
		} catch (Exception e) { //a SecurityException or one thrown above
			vocabularyFile = null;
			throw new Exception("VocabularyXMLWriter.setVocabularyFile(String): " + e.toString());
		}
	} //End setVocabularyFile(String)

	/**
	 * Gives access to the lessons and entries as xml-Strings.
	 * The method is directly used to construct the collection of xml-lines
	 * that consists the whole output.
	 *
	 * @param HashMap - the units in a List
	 * @param HashMap - the entries in a List
	 * @param EntryTypesCollection - the entryTypes
	 * @param InfoVocab - the infos about the vocabulary
	 */
	public void setXMLElements(String version
			, String unitsXML
			, String categoriesXML
			, String entriesXML
			, String entryTypesXML
			, String entryTypeAttributesXML
			, String infoXML
			, String statsXML) throws Exception {
		//assert that lessons and entries are not null
		if (null == unitsXML) {
			throw new Exception("VocabularyXMLWriter.setXMLElements(): unitsXML may not be null.");
		}
		if (null == categoriesXML) {
			throw new Exception("VocabularyXMLWriter.setXMLElements(): categoriesXML may not be null.");
		}
		if (null == entriesXML) {
			throw new Exception("VocabularyXMLWriter.setXMLElements(): entriesXML may not be null.");
		}
		if (null == entryTypesXML) {
			throw new Exception("VocabularyXMLWriter.setXMLElements(): entryTypesXML may not be null.");
		}
		if (null == entryTypeAttributesXML) {
			throw new Exception("VocabularyXMLWriter.setXMLElements(): entryTypeAttributesXML may not be null.");
		}
		if (null == infoXML) {
			throw new Exception("VocabularyXMLWriter.setXMLElements(): infoXML may not be null.");
		}
		if (null == statsXML) {
			throw new Exception("VocabularyXMLWriter.setXMLElements(): statsXML may not be null.");
		}
		try {
			xmlList = new ArrayList();
			xmlList.add("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
			xmlList.add("<" + Constants.XML_VOCABULARY + " version=\""+version+"\">");
			xmlList.add(infoXML);
			xmlList.add(entryTypeAttributesXML);
			xmlList.add(entryTypesXML);
			xmlList.add(unitsXML);
			xmlList.add(categoriesXML);
			xmlList.add(entriesXML);
			xmlList.add(statsXML);
			xmlList.add("</" + Constants.XML_VOCABULARY + ">");
		}
		catch (IndexOutOfBoundsException e) {
			xmlList = null;
			throw new Exception("VocabularyXMLWriter.setXMLElements(): there was a problem when adding: " + e.toString());
		}
	} //End public void setXMLElements(HashMap, HashMap, EntryTypesCollection, InfoVocab)

	/**
	 * Controls whether everything is ok to execute <code>writeVocabulary()</code>.
	 * For the time being this is just to be sure we have a valid file.
	 *
	 * @return boolean - true if everything is ok.
	 */
	public boolean readyToExecute() {
		boolean allOk = true;
		if (vocabularyFile == null)
			allOk = false;
		if (xmlList == null)
			allOk = false;
		return allOk;
	} //End readyToExecute()

	/**
	 * This method writes a vocabulary-xml-file.
	 * If an error occures during writing an exception is casted.
	 *
	 * @exception Exception - The method casts an exeption if something goes wrong. The casted exception wraps others.
	 */
	public void execute() throws Exception {
		//helpers
		BufferedWriter out = null;

		//Create BufferedWriter
		try {
			FileOutputStream fos = new FileOutputStream(vocabularyFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
			out = new BufferedWriter(osw);
			Iterator iter = xmlList.iterator();
			while (iter.hasNext()) {
				out.write((String) iter.next());
				out.newLine();
			}
			out.flush();
		}
		catch (FileNotFoundException e) {
			throw new Exception("Problem in XMLHandler.execute ->\n"
						+ "Could not instantiate output-writer: " + e.toString());
		}
		catch (IOException e) {
			throw new Exception("Problem in XMLHandler.execute ->\n"
						+ "IOException:\n" + e.toString());
		} finally {
			if (null != out) {
				out.close();
			}
		}
	} //END public void execute() throws Exception
} //END public class VocabularyXMLWriter implements IOHandler
