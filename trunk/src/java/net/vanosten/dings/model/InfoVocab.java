/*
 * InfoVocab.java
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
package net.vanosten.dings.model;

import java.util.ArrayList;
import java.util.Locale;

import net.vanosten.dings.uiif.IInfoVocabEditView;
import net.vanosten.dings.consts.Constants;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Holder for the information of the vocabluary.
 */
public final class InfoVocab extends AItemModel {
	/** The title of this learning stack */
	private String title;

	/** The author of this vocabulary */
	private String author;

	/** Free text notes about this learning stack */
	private String notes;

	/** Copyright info for this learning stack */
	private String copyright;

	/** Free text licence about this learning stack */
	private String licence;

	/** The edit editView */
	private IInfoVocabEditView editView;

	/** The baseLabel (original language) */
	private String baseLabel;

	/** The targetLabel (destination / to learn language) */
	private String targetLabel;

	/** The label for attributes section */
	private String attributesLabel;

	/** The label for Unit */
	private String unitLabel;

	/** The label for Category */
	private String categoryLabel;

	/** The label for Others section*/
	private String othersLabel;

	/** The label for Explanation */
	private String explanationLabel;

	/** The label for Example */
	private String exampleLabel;


	/** The locale incl. orientation (bidi) of Base */
	private Locale baseLocale;

	/** The locale incl. orientation (bidi) of Target */
	private Locale targetLocale;

	/** The locale incl. orientation (bidi) of the attributes */
	private Locale attributesLocale;

	/** The locale incl. orientation (bidi) of Unit */
	private Locale unitLocale;

	/** The locale incl. orientation (bidi) of Category */
	private Locale categoryLocale;

	/** The locale incl. orientation (bidi) of Explanation */
	private Locale explanationLocale;

	/** The locale incl. orientation (bidi) of Example */
	private Locale exampleLocale;

	/** The locale incl. orientation (bidi) of Pronunciation */
	private Locale pronunciationLocale;

	/** The locale incl. orientation (bidi) of Relation */
	private Locale relationLocale;

	/** Field is visible in all views */
	public final static int VISIBILITY_ALWAYS = 0;

	/** Field is visible in one-to-one query and solution views */
	public final static int VISIBILITY_QUERY = 1;

	/** Field is visible in solution views */
	public final static int VISIBILITY_SOLUTION = 2;

	/** Field is not visible in any view (not used)*/
	public final static int VISIBILITY_NEVER = 3;

	/** The visibility of Unit in views */
	private int visibilityAttributes = VISIBILITY_ALWAYS;

	/** The visibility of Unit in views */
	private int visibilityUnit = VISIBILITY_ALWAYS;

	/** The visibility of Category in views */
	private int visibilityCategory = VISIBILITY_ALWAYS;

	/** The visibility of Explanation in views */
	private int visibilityExplanation = VISIBILITY_ALWAYS;

	/** The visibility of Example in views */
	private int visibilityExample = VISIBILITY_ALWAYS;

	/** The visibility of Pronunciation in views */
	private int visibilityPronunciation = VISIBILITY_ALWAYS;

	/** The visibility of Relation in views */
	private int visibilityRelation = VISIBILITY_ALWAYS;
	
	private boolean baseUsesSyllables = false;
	private boolean targetUsesSyllables = false;

	/**
	 * A constructor without any data to make a default.
	 */
	public InfoVocab() {
		this("Learning stack", "", "", "", ""
					,"Base", "Target", "Attributes"
					, "Unit", "Category"
					, "Others", "Explanation", "Example"
					, "en_US", "en_US", "en_US"
					, "en_US", "en_US"
					, "en_US", "en_US"
					, "en_US", "en_US"
					, VISIBILITY_ALWAYS, VISIBILITY_ALWAYS, VISIBILITY_ALWAYS
					, VISIBILITY_ALWAYS, VISIBILITY_ALWAYS
					, VISIBILITY_ALWAYS, VISIBILITY_ALWAYS
					, false, false);
	} //END public InfoVocab()

	/**
	 * The constructor with full data.
	 */
	public InfoVocab(String aTitle, String anAuthor, String theNotes, String aCopyright, String aLicence
					 , String aBaseLabel, String aTargetLabel, String anAttributesLabel
					 , String aUnitLabel, String aCategoryLabel
					 , String anOthersLabel, String anExplanationLabel, String anExampleLabel
					 , String aBaseLocale, String aTargetLocale, String anAttributesLocale
					 , String aUnitLocale, String aCategoryLocale
					 , String anExplanationLocale, String anExampleLocale
					 , String aPronunciationLocale, String aRelationLocale
					 , int aVisibilityAttributes, int aVisibilityUnit, int aVisibilityCategory
					 , int aVisibilityExplanation, int aVisibilityExample
					 , int aVisibilityPronunciation, int aVisibilityRelation
					 , boolean aBaseUsesSyllables, boolean aTargetUsesSyllables) {

		logger = Logger.getLogger("net.vanosten.dings.model.InfoVocab");

		if (null == aTitle || aTitle.equals(Constants.EMPTY_STRING)) {
			this.title = Constants.UNDEFINED;
		}
		else {
			this.title = aTitle;
		}
		if (null == anAuthor || anAuthor.equals(Constants.EMPTY_STRING)) {
			this.author = Constants.UNDEFINED;
		}
		else {
			this.author = anAuthor;
		}
		if (null == theNotes) {
			this.notes = Constants.EMPTY_STRING;
		}
		else {
			this.notes = theNotes;
		}
		if (null == aCopyright) {
			this.copyright = Constants.EMPTY_STRING;
		}
		else {
			this.copyright = aCopyright;
		}
		if (null == aLicence) {
			this.licence = Constants.EMPTY_STRING;
		}
		else {
			this.licence = aLicence;
		}

		//labels
		this.baseLabel = aBaseLabel;
		this.targetLabel = aTargetLabel;
		this.attributesLabel = anAttributesLabel;
		this.unitLabel = aUnitLabel;
		this.categoryLabel = aCategoryLabel;
		this.othersLabel = anOthersLabel;
		this.explanationLabel = anExplanationLabel;
		this.exampleLabel = anExampleLabel;

		//locales
		this.baseLocale = Constants.parseLocale(aBaseLocale);
		this.targetLocale = Constants.parseLocale(aTargetLocale);
		this.attributesLocale = Constants.parseLocale(anAttributesLocale);
		this.unitLocale = Constants.parseLocale(aUnitLocale);
		this.categoryLocale = Constants.parseLocale(aCategoryLocale);
		this.explanationLocale = Constants.parseLocale(anExplanationLocale);
		this.exampleLocale = Constants.parseLocale(anExampleLocale);
		this.pronunciationLocale = Constants.parseLocale(aPronunciationLocale);
		this.relationLocale = Constants.parseLocale(aRelationLocale);

		//visibility
		this.visibilityAttributes = aVisibilityAttributes;
		this.visibilityUnit = aVisibilityUnit;
		this.visibilityCategory = aVisibilityCategory;
		this.visibilityExplanation = aVisibilityExplanation;
		this.visibilityExample = aVisibilityExample;
		this.visibilityPronunciation = aVisibilityPronunciation;
		this.visibilityRelation = aVisibilityRelation;
		
		//syllables
		this.baseUsesSyllables = aBaseUsesSyllables;
		this.targetUsesSyllables = aTargetUsesSyllables;
	} //END public InfoVocab(...)

	//Implements AItemModel
	protected String getXMLString() {
		StringBuffer xml = new StringBuffer();

		xml.append("<").append(Constants.XML_INFO).append(">");
		xml.append(Constants.getXMLTaggedValue(Constants.XML_TITLE, title));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_AUTHOR, author));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_NOTES, notes));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_COPYRIGHT, copyright));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_LICENCE, licence));
		//labels
		xml.append(Constants.getXMLTaggedValue(Constants.XML_BASE_LABEL, baseLabel));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_TRAGET_LABEL, targetLabel));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_ATTRIBUTES_LABEL, attributesLabel));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_UNIT_LABEL, unitLabel));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_CATEGORY_LABEL, categoryLabel));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_OTHERS_LABEL, othersLabel));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_EXPLANATION_LABEL, explanationLabel));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_EXAMPLE_LABEL, exampleLabel));
		//orientation
		xml.append(Constants.getXMLTaggedValue(Constants.XML_BASE_LOCALE, baseLocale.toString()));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_TARGET_LOCALE, targetLocale.toString()));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_ATTRIBUTES_LOCALE, attributesLocale.toString()));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_UNIT_LOCALE, unitLocale.toString()));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_CATEGORY_LOCALE, categoryLocale.toString()));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_EXPLANATION_LOCALE, explanationLocale.toString()));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_EXAMPLE_LOCALE, exampleLocale.toString()));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_PRONUNCIATION_LOCALE, pronunciationLocale.toString()));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_RELATION_LOCALE, relationLocale.toString()));
		//visibility
		xml.append(Constants.getXMLTaggedValue(Constants.XML_VISIBILITY_ATTRIBUTES, Integer.toString(visibilityAttributes)));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_VISIBILITY_UNIT, Integer.toString(visibilityUnit)));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_VISIBILITY_CATEGORY, Integer.toString(visibilityCategory)));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_VISIBILITY_EXPLANATION, Integer.toString(visibilityExplanation)));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_VISIBILITY_EXAMPLE, Integer.toString(visibilityExample)));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_VISIBILITY_PRONUNCIATION, Integer.toString(visibilityPronunciation)));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_VISIBILITY_RELATION, Integer.toString(visibilityRelation)));
		//syllables
		xml.append(Constants.getXMLTaggedValue(Constants.XML_SYLLABLES_BASE, Boolean.toString(baseUsesSyllables)));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_SYLLABLES_TARGET, Boolean.toString(targetUsesSyllables)));
		xml.append("</").append(Constants.XML_INFO).append(">");
		return xml.toString();
	} //END protected String getXMLString()

	protected void setEditView(IInfoVocabEditView anEditView) {
		this.editView = anEditView;
	} //END protected void setEditView(IInfoVocabEditView)

	protected void releaseViews() {
		editView = null;
	} //END protected void releaseViews()

	/**
	 * Tests the required fields for valid contents.
	 *
	 * @return ArrayList - a list of validation errors. Size() = 0 means valid model.
	 */
	public static ArrayList validate(String aTitle
									 , String aBaseLabel
									 , String aTargetLabel
									 , String anAttributesLabel
									 , String aUnitLabel
									 , String aCategoryLabel
									 , String anOthersLabel
									 , String anExplanationLabel
									 , String anExampleLabel) {
		ArrayList<String> errors = new ArrayList<String>();
		if (false == validateString(aTitle, 1)) {
			errors.add("Title may not be empty");
		}
		if (false == validateString(aBaseLabel, 1)) {
			errors.add("Label for Base may not be empty");
		}
		if (false == validateString(aTargetLabel, 1)) {
			errors.add("Label for Target may not be empty");
		}
		if (false == validateString(anAttributesLabel, 1)) {
			errors.add("Label for attributes section may not be empty");
		}
		if (false == validateString(aUnitLabel, 1)) {
			errors.add("Label for Unit may not be empty");
		}
		if (false == validateString(aCategoryLabel, 1)) {
			errors.add("Label for Category may not be empty");
		}
		if (false == validateString(anOthersLabel, 1)) {
			errors.add("Label for others section may not be empty");
		}
		if (false == validateString(anExplanationLabel, 1)) {
			errors.add("Label for Explanation may not be empty");
		}
		if (false == validateString(anExampleLabel, 1)) {
			errors.add("Label for Example may not be empty");
		}
		return errors;
	} //END public static ArrayList validate(String ...)

	/**
	 *Implements AItemModel.
	 */
	protected void updateModel() {
		//get values from editView and trim them
		String titleV = editView.getTitle().trim();
		String baseLabelV = editView.getBaseLabel().trim();
		String targetLabelV = editView.getTargetLabel().trim();
		String attributesLabelV = editView.getAttributesLabel().trim();
		String unitLabelV = editView.getUnitLabel().trim();
		String categoryLabelV = editView.getCategoryLabel();
		String othersLabelV = editView.getOthersLabel();
		String explanationLabelV = editView.getExplanationLabel();
		String exampleLabelV = editView.getExampleLabel();
		//validate where necessary
		ArrayList errors = validate(titleV, baseLabelV, targetLabelV, attributesLabelV
							, unitLabelV, categoryLabelV
							, othersLabelV, explanationLabelV, exampleLabelV);
		//if validation is ok, save the new values.
		if (0 ==  errors.size()) {
			//validated values
			title = titleV;
			baseLabel = baseLabelV;
			targetLabel = targetLabelV;
			attributesLabel = attributesLabelV;
			unitLabel = unitLabelV;
			categoryLabel = categoryLabelV;
			othersLabel = othersLabelV;
			explanationLabel = explanationLabelV;
			exampleLabel = exampleLabelV;
			//not validated values
			author = editView.getAuthor().trim();
			notes = editView.getNotes().trim();
			copyright = editView.getCopyright().trim();
			licence = editView.getLicence().trim();
			baseLocale = Constants.parseLocale(editView.getBaseLocale());
			targetLocale = Constants.parseLocale(editView.getTargetLocale());
			attributesLocale = Constants.parseLocale(editView.getAttributesLocale());
			unitLocale = Constants.parseLocale(editView.getUnitLocale());
			categoryLocale = Constants.parseLocale(editView.getCategoryLocale());
			explanationLocale = Constants.parseLocale(editView.getExplanationLocale());
			exampleLocale = Constants.parseLocale(editView.getExampleLocale());
			pronunciationLocale = Constants.parseLocale(editView.getPronunciationLocale());
			relationLocale = Constants.parseLocale(editView.getRelationLocale());
			visibilityAttributes = editView.getVisibilityAttributes();
			visibilityUnit = editView.getVisibilityUnit();
			visibilityCategory = editView.getVisibilityCategory();
			visibilityExplanation = editView.getVisibilityExplanation();
			visibilityExample = editView.getVisibilityExample();
			visibilityPronunciation = editView.getVisibilityPronunciation();
			visibilityRelation = editView.getVisibilityRelation();
			baseUsesSyllables = editView.isBaseUsesSyllables();
			targetUsesSyllables = editView.isTargetUsesSyllables();
			//save needed and reset
			sendSaveNeeded();
			updateGUI();
		}
	} //END private void updateModel()

	//Implements AItemModel.
	protected void updateGUI() {
		editView.setTitle(title);
		editView.setAuthor(author);
		editView.setNotes(notes);
		editView.setCopyright(copyright);
		editView.setLicence(licence);
		editView.setBaseLabel(baseLabel);
		editView.setTargetLabel(targetLabel);
		editView.setAttributesLabel(attributesLabel);
		editView.setUnitLabel(unitLabel);
		editView.setCategoryLabel(categoryLabel);
		editView.setOthersLabel(othersLabel);
		editView.setExplanationLabel(explanationLabel);
		editView.setExampleLabel(exampleLabel);
		//locales
		editView.setBaseLocale(baseLocale.toString());
		editView.setTargetLocale(targetLocale.toString());
		editView.setAttributesLocale(attributesLocale.toString());
		editView.setUnitLocale(unitLocale.toString());
		editView.setCategoryLocale(categoryLocale.toString());
		editView.setExplanationLocale(explanationLocale.toString());
		editView.setExampleLocale(exampleLocale.toString());
		editView.setPronunciationLocale(pronunciationLocale.toString());
		editView.setRelationLocale(relationLocale.toString());
		//visibility
		editView.setVisibilityAttributes(visibilityAttributes);
		editView.setVisibilityUnit(visibilityUnit);
		editView.setVisibilityCategory(visibilityCategory);
		editView.setVisibilityExplanation(visibilityExplanation);
		editView.setVisibilityExample(visibilityExample);
		editView.setVisibilityPronunciation(visibilityPronunciation);
		editView.setVisibilityRelation(visibilityRelation);
		//syllables
		editView.setBaseUsesSyllables(baseUsesSyllables);
		editView.setTargetUsesSyllables(targetUsesSyllables);

		//user feedback
		editView.setEditing(false, true);
		editView.setTitleIsValueValid(true);
		editView.setBaseLabelIsValueValid(true);
		editView.setTragetLabelIsValueValid(true);
		editView.setAttributesLabelIsValueValid(true);
		editView.setUnitLabelIsValueValid(true);
		editView.setCategoryLabelIsValueValid(true);
		editView.setOthersLabelIsValueValid(true);
		editView.setExplanationLabelIsValueValid(true);
		editView.setExampleLabelIsValueValid(true);
	 } //END protected void updateGUI()

	/**
	 *Implements AItemModel.
	 */
	protected void checkChangeInGUI() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "checkChangeInGUI", author + "," +editView.getAuthor().trim() + ".");
		}
		boolean isValid = validateString(editView.getTitle(), 1)
			&& validateString(editView.getBaseLabel(), 1)
			&& validateString(editView.getTargetLabel(), 1)
			&& validateString(editView.getAttributesLabel(), 1)
			&& validateString(editView.getUnitLabel(), 1)
			&& validateString(editView.getCategoryLabel(), 1)
			&& validateString(editView.getOthersLabel(), 1)
			&& validateString(editView.getExplanationLabel(), 1)
			&& validateString(editView.getExampleLabel(), 1);
		if ((editView.getTitle().trim().equals(title)) &&
			(editView.getAuthor().trim().equals(author)) &&
			(editView.getNotes().trim().equals(notes)) &&
			(editView.getCopyright().trim().equals(copyright)) &&
			(editView.getLicence().trim().equals(licence)) &&
			(editView.getBaseLabel().trim().equals(baseLabel)) &&
			(editView.getTargetLabel().trim().equals(targetLabel)) &&
			(editView.getAttributesLabel().trim().equals(attributesLabel)) &&
			(editView.getUnitLabel().trim().equals(unitLabel)) &&
			(editView.getCategoryLabel().trim().equals(categoryLabel)) &&
			(editView.getOthersLabel().trim().equals(othersLabel)) &&
			(editView.getExplanationLabel().trim().equals(explanationLabel)) &&
			(editView.getExampleLabel().trim().equals(exampleLabel)) &&
			(editView.getBaseLocale().equals(baseLocale.toString())) &&
			(editView.getTargetLocale().equals(targetLocale.toString())) &&
			(editView.getAttributesLocale().equals(attributesLocale.toString())) &&
			(editView.getUnitLocale().equals(unitLocale.toString())) &&
			(editView.getCategoryLocale().equals(categoryLocale.toString())) &&
			(editView.getExplanationLocale().equals(explanationLocale.toString())) &&
			(editView.getExampleLocale().equals(exampleLocale.toString())) &&
			(editView.getPronunciationLocale().equals(pronunciationLocale.toString())) &&
			(editView.getRelationLocale().equals(relationLocale.toString())) &&
			(editView.getVisibilityAttributes() == visibilityAttributes) &&
			(editView.getVisibilityUnit() == visibilityUnit) &&
			(editView.getVisibilityCategory() == visibilityCategory) &&
			(editView.getVisibilityExplanation() == visibilityExplanation) &&
			(editView.getVisibilityExample() == visibilityExample) &&
			(editView.getVisibilityPronunciation() == visibilityPronunciation) &&
			(editView.getVisibilityRelation() == visibilityRelation) &&
			(editView.isBaseUsesSyllables() == baseUsesSyllables) &&
			(editView.isTargetUsesSyllables() == targetUsesSyllables)) {
			editView.setEditing(false, isValid);
		}
		else {
			editView.setEditing(true, isValid);
			editView.setTitleIsValueValid(validateString(editView.getTitle(), 1));
			editView.setBaseLabelIsValueValid(validateString(editView.getBaseLabel(), 1));
			editView.setTragetLabelIsValueValid(validateString(editView.getTargetLabel(), 1));
			editView.setAttributesLabelIsValueValid(validateString(editView.getAttributesLabel(), 1));
			editView.setUnitLabelIsValueValid(validateString(editView.getUnitLabel(), 1));
			editView.setCategoryLabelIsValueValid(validateString(editView.getCategoryLabel(), 1));
			editView.setOthersLabelIsValueValid(validateString(editView.getOthersLabel(), 1));
			editView.setExplanationLabelIsValueValid(validateString(editView.getExplanationLabel(), 1));
			editView.setExampleLabelIsValueValid(validateString(editView.getExampleLabel(), 1));
		}
	} //END protected void checkChangeInGUI()

	//---------------------- Getters for display choices in Entry views -----------

	public String getBaseLabel() {
		return baseLabel;
	} //END public String getBaseLabel()

	public String getTargetLabel() {
		return targetLabel;
	} //END public String getTargetName()

	public String getAttributesLabel() {
		return attributesLabel;
	} //END public String getAttributesLabel()

	public String getUnitLabel() {
		return unitLabel;
	} //END public String getUnitLabel()

	public String getCategoryLabel() {
		return categoryLabel;
	} //END public String getCategoryLabel()

	public String getOthersLabel() {
		return othersLabel;
	} //END public String getOthersLabel()

	public String getExplanationLabel() {
		return explanationLabel;
	} //END public String getExplanationLabel()

	public String getExampleLabel() {
		return exampleLabel;
	} //END public String getExampleLabel()

	public int getVisibilityAttributes() {
		return visibilityAttributes;
	} //END public int getVisibilityAttributes()

	public int getVisibilityUnit() {
		return visibilityUnit;
	} //END public int getVisibilityUnit()

	public int getVisibilityCategory() {
		return visibilityCategory;
	} //END public int getVisibilityCategory()

	public int getVisibilityExplanation() {
		return visibilityExplanation;
	} //END public int getVisibilityExplanation()

	public int getVisibilityExample() {
		return visibilityExample;
	} //END public int getVisibilityExample()

	public int getVisibilityPronunciation() {
		return visibilityPronunciation;
	} //END public int getVisibilityPronunciation()

	public int getVisibilityRelation() {
		return visibilityRelation;
	} //END public int getVisibilityRelation()
	
	public boolean isBaseUsesSyllables() {
		return baseUsesSyllables;
	}
	
	public boolean isTargetUsesSyllables() {
		return targetUsesSyllables;
	}
} //END public class InfoVocab extends DingsItem
