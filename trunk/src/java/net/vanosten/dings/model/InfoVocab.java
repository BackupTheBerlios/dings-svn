/*
 * InfoVocab.java
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
package net.vanosten.dings.model;

import java.util.ArrayList;
import java.util.Locale;

import net.vanosten.dings.uiif.IInfoVocabEditView;
import net.vanosten.dings.consts.Constants;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Holder for the information of the vocabluary.
 *
 * @author  Rick Gruber, <a href="mailto:rick@vanosten.net">rick@vanosten.net</a>
 * @version 1.0.0
 */
public final class InfoVocab extends AItemModel {
	/** The title of this learning stack */
	private String title;

    /** The author of this vocabulary */
    private String author;

    /** Free text notes about this vocabulary */
    private String notes;
    
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
    public final static int VISIBILITY_QUERY_ONE = 1;
    
    /** Field is visible all solution views */
    public final static int VISIBILITY_SOLUTION_ALL = 2;
    
    /** Field is visible all solution views for many-to-many */
    public final static int VISIBILITY_SOLUTION_MANY = 3;
    
    /** Field is visible in one-to-one solution views */
    public final static int VISIBILITY_SOLUTION_ONE = 4;
    
    /** Field is visible only in edit and solution views */
    public final static int VISIBILITY_EDITING = 5;
    
    /** Field is not visible in any view */
    public final static int VISIBILITY_NEVER = 6;
    
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
    
    /**
     * A constructor without any data to make a default.
     */
    public InfoVocab() {
    	this("Learning stack", "", ""
    				,"Base", "Target", "Attributes"
    				, "Unit", "Category"
					, "Others", "Explanation", "Example"
					, "en_US", "en_US", "en_US"
					, "en_US", "en_US"
					, "en_US", "en_US"
					, "en_US", "en_US"
					, VISIBILITY_ALWAYS, VISIBILITY_ALWAYS, VISIBILITY_ALWAYS
					, VISIBILITY_ALWAYS, VISIBILITY_ALWAYS
					, VISIBILITY_ALWAYS, VISIBILITY_ALWAYS);
    } //END public InfoVocab()

    /**
     * The constructor with full data.
     */
    public InfoVocab(String aTitle, String anAuthor, String theNotes
					 , String aBaseLabel, String aTargetLabel, String anAttributesLabel
					 , String aUnitLabel, String aCategoryLabel
					 , String anOthersLabel, String anExplanationLabel, String anExampleLabel
					 , String aBaseLocale, String aTargetLocale, String anAttributesLocale
					 , String aUnitLocale, String aCategoryLocale
					 , String anExplanationLocale, String anExampleLocale
					 , String aPronunciationLocale, String aRelationLocale
					 , int aVisibilityAttributes, int aVisibilityUnit, int aVisibilityCategory
					 , int aVisibilityExplanation, int aVisibilityExample
					 , int aVisibilityPronunciation, int aVisibilityRelation) {
    	
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
    }	//END public InfoVocab(...)

    //Implements AItemModel
    protected String getXMLString() {
        StringBuffer xml = new StringBuffer();

        xml.append("<").append(Constants.XML_INFO).append(">");
        xml.append(Constants.getXMLTaggedValue(Constants.XML_TITLE, title));
        xml.append(Constants.getXMLTaggedValue(Constants.XML_AUTHOR, author));
		xml.append(Constants.getXMLTaggedValue(Constants.XML_NOTES, notes));
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
		ArrayList errors = new ArrayList();
		if (1 > aTitle.length()) {
			errors.add("Title may not be empty");
		}
		if (1 > aBaseLabel.length()) {
			errors.add("Label for Base may not be empty");
		}
		if (1 > aTargetLabel.length()) {
			errors.add("Label for Target may not be empty");
		}
		if (1 > anAttributesLabel.length()) {
			errors.add("Label for attributes section may not be empty");
		}
		if (1 > aUnitLabel.length()) {
			errors.add("Label for Unit may not be empty");
		}
		if (1 > aCategoryLabel.length()) {
			errors.add("Label for Category may not be empty");
		}
		if (1 > anOthersLabel.length()) {
			errors.add("Label for others section may not be empty");
		}
		if (1 > anExplanationLabel.length()) {
			errors.add("Label for Explanation may not be empty");
		}
		if (1 > anExampleLabel.length()) {
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
			//save needed and reset
			sendSaveNeeded();
			updateGUI();
		}
		else {
			//Show an error message with the validation details
			showValidationErrors(errors);
		}
    }	//END private void updateModel()

    //Implements AItemModel.
    protected void updateGUI() {
        editView.setTitle(title);
        editView.setAuthor(author);
        editView.setNotes(notes);
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
        editView.setEditing(false);
     }	//END protected void updateGUI()
     
	/**
	 *Implements AItemModel.
	 */
	protected void checkChangeInGUI() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.logp(Level.FINEST, this.getClass().getName(), "checkChangeInGUI", author + "," +editView.getAuthor().trim() + ".");
		}
 		if ((editView.getTitle().trim().equals(title)) &&
 			(editView.getAuthor().trim().equals(author)) &&
			(editView.getNotes().trim().equals(notes)) &&
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
 			(editView.getVisibilityRelation() == visibilityRelation)) {
			editView.setEditing(false);
		}
		else {
			editView.setEditing(true);
		}
	} //END protected void checkChangeInGUI()
	
	//---------------------- Getters for display choices in Entry views -----------

	protected String getBaseLabel() {
		return baseLabel;
	} //END protected String getBaseLabel()
	
	protected String getTargetLabel() {
		return targetLabel;
	} //END protected String getTargetName()
	
	protected String getAttributesLabel() {
		return attributesLabel;
	} //END protected String getAttributesLabel()
	
	protected String getUnitLabel() {
		return unitLabel;
	} //END protected String getUnitLabel()
	
	protected String getCategoryLabel() {
		return categoryLabel;
	} //END protected String getCategoryLabel()
	
	protected String getOthersLabel() {
		return othersLabel;
	} //END protected String getOthersLabel()
	
	protected String getExplanationLabel() {
		return explanationLabel;
	} //END protected String getExplanationLabel()
	
	protected String getExampleLabel() {
		return exampleLabel;
	} //END protected String getExampleLabel()
	
	protected int getVisibilityAttributes() {
		return visibilityAttributes;
	} //END protected int getVisibilityAttributes()
	
	protected int getVisibilityUnit() {
		return visibilityUnit;
	} //END protected int getVisibilityUnit()
	
	protected int getVisibilityCategory() {
		return visibilityCategory;
	} //END protected int getVisibilityCategory()
	
	protected int getVisibilityExplanation() {
		return visibilityExplanation;
	} //END protected int getVisibilityExplanation()
	
	protected int getVisibilityExample() {
		return visibilityExample;
	} //END protected int getVisibilityExample()
	
	protected int getVisibilityPronunciation() {
		return visibilityPronunciation;
	} //END protected int getVisibilityPronunciation()
	
	protected int getVisibilityRelation() {
		return visibilityRelation;
	} //END protected int getVisibilityRelation()
}	//END public class InfoVocab extends DingsItem
