/*
 * EntryLearnOneView.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2002, 03, 04, 05, 06 Rick Gruber-Riemer (dingsbums@vanosten.net)
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
package net.vanosten.dings.swing;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.model.InfoVocab;
import net.vanosten.dings.model.Preferences;
import net.vanosten.dings.model.Entry.Result;
import net.vanosten.dings.swing.helperui.ChoiceID;
import net.vanosten.dings.swing.helperui.HintObserver;
import net.vanosten.dings.swing.helperui.HintPanel;
import net.vanosten.dings.swing.helperui.InsertCharacterButtonPanel;
import net.vanosten.dings.swing.helperui.LabeledSeparator;
import net.vanosten.dings.swing.helperui.SolutionLabel;
import net.vanosten.dings.swing.helperui.SyllablesLabel;
import net.vanosten.dings.uiif.IEntryLearnOneView;
import net.vanosten.dings.utils.SyllablesUtil;
import net.vanosten.dings.utils.Toolbox;

public class EntryLearnOneView extends AViewWithScrollPane implements IEntryLearnOneView, HintObserver {
	private final static long serialVersionUID = 1L;

	private ChoiceID attributeOneCh, attributeTwoCh, attributeThreeCh, attributeFourCh, categoriesCh, unitsCh;
	private JCheckBox statusCB;
	private JLabel questionSL;
	private SolutionLabel explanationSL, pronunciationSL, exampleSL, relationSL, entryTypeSL;
	private JTextArea answerTA;
	private HintPanel hintPL;
	private JLabel attributeOneL, attributeTwoL, attributeThreeL, attributeFourL;
	private JLabel questionL, hintL, answerL, explanationL, exampleL, pronunciationL, relationL, unitL, categoryL, scoreL;
	private LabeledSeparator attributesLS, othersLS;
	private JButton showB, checkAnswerB, knowB, notKnowB;
	private InsertCharacterButtonPanel charactersP;
	private SyllablesLabel syllablesL;


	/** the result of the learning */
	private Result result = Result.SUCCESS;
	
	//default background colors
	private Color textAreaDefaultBgColor = null;
	private Color choiceDefaultBgColor = null;
	private final static Color CORRECT_COLOR = Color.green;
	private final static Color WRONG_COLOR = Color.red;

	public EntryLearnOneView(ComponentOrientation aComponentOrientation) {
		super(Toolbox.getInstance().getLocalizedString("viewtitle.learn_one"), aComponentOrientation);
		initializeGUI();
		this.setGUIOrientation();
	} //END public EntryEditViewer(ComponentOrientation)
	
	private void initComponents() {
		//separators
		attributesLS = new LabeledSeparator(Toolbox.getInstance().getInfoPointer().getAttributesLabel() + ":");
		othersLS = new LabeledSeparator(Toolbox.getInstance().getInfoPointer().getOthersLabel() + ":");
		//labels
		String questionLabel = Toolbox.getInstance().getInfoPointer().getBaseLabel();
		String answerLabel = Toolbox.getInstance().getInfoPointer().getTargetLabel();
		if (false == Toolbox.getInstance().isTargetAsked()) {
			questionLabel = Toolbox.getInstance().getInfoPointer().getTargetLabel();
			answerLabel = Toolbox.getInstance().getInfoPointer().getBaseLabel();
		}
		entryTypeSL = new SolutionLabel();

		boolean usesSyllables;
		
		questionL = new JLabel(questionLabel + ":");
		usesSyllables = (true == Toolbox.getInstance().isTargetAsked() && Toolbox.getInstance().getInfoPointer().isBaseUsesSyllables())
				|| (false == Toolbox.getInstance().isTargetAsked() && Toolbox.getInstance().getInfoPointer().isTargetUsesSyllables());
		if (usesSyllables) {
			questionSL = new SyllablesLabel();
		} else {
			questionSL = new SolutionLabel();
		}
		//hint
		hintL = new JLabel("Hint:");
		usesSyllables = (false == Toolbox.getInstance().isTargetAsked() && Toolbox.getInstance().getInfoPointer().isBaseUsesSyllables())
				|| (true == Toolbox.getInstance().isTargetAsked() && Toolbox.getInstance().getInfoPointer().isTargetUsesSyllables());
		hintPL = new HintPanel(usesSyllables);
		hintPL.registerHintObserver(this);
		//target
		answerL = new JLabel(answerLabel + ":");
		answerTA = new JTextArea();
		answerTA.setRows(Toolbox.getInstance().getPreferencesPointer().getIntProperty(Preferences.PROP_LINES_TARGET));
		answerTA.setWrapStyleWord(true);
		answerTA.setLineWrap(true);
		invertFocusTraversalBehaviour(answerTA);
		textAreaDefaultBgColor = answerTA.getBackground();
		Document answerDocument = answerTA.getDocument();
		answerDocument.addDocumentListener(new SyllablesDocumentListener());
		//status
		statusCB = new JCheckBox("up-to-date");
		//score
		scoreL = new JLabel("?");
		//units
		unitL = new JLabel(Toolbox.getInstance().getInfoPointer().getUnitLabel() + ":");
		unitsCh = new ChoiceID();
		choiceDefaultBgColor = unitsCh.getBackground();
		//categories
		categoryL = new JLabel(Toolbox.getInstance().getInfoPointer().getCategoryLabel() + ":");
		categoriesCh = new ChoiceID();
		//attributeOne
		attributeOneL = new JLabel("N/A:");
		attributeOneCh = new ChoiceID();
		//attributeTwo
		attributeTwoL = new JLabel("N/A:");
		attributeTwoCh = new ChoiceID();
		//attributeThree
		attributeThreeL = new JLabel("N/A:");
		attributeThreeCh = new ChoiceID();
		//attributeFour
		attributeFourL = new JLabel("N/A:");
		attributeFourCh = new ChoiceID();
		//explanation
		explanationL = new JLabel(Toolbox.getInstance().getInfoPointer().getExplanationLabel() + ":");
		explanationSL = new SolutionLabel();
		//example
		exampleL = new JLabel(Toolbox.getInstance().getInfoPointer().getExampleLabel() + ":");
		exampleSL = new SolutionLabel();
		//pronunciation
		pronunciationL = new JLabel("Pronunciation:");
		pronunciationSL = new SolutionLabel();
		//relation
		relationL = new JLabel("Relation:");
		relationSL = new SolutionLabel();
		//syllables characters
		if ((Toolbox.getInstance().getInfoPointer().isBaseUsesSyllables() && (false == Toolbox.getInstance().isTargetAsked()))
				|| (Toolbox.getInstance().getInfoPointer().isTargetUsesSyllables() && (Toolbox.getInstance().isTargetAsked()))) {
			charactersP = new InsertCharacterButtonPanel(answerTA, SyllablesUtil.ACCENTS_BY_ACCENTGROUP, SyllablesUtil.TOOLTIPS_BY_ACCENTGROUP);
			syllablesL = new SyllablesLabel();
		}
	} //END private void initComponents()

	//Implements AViewWithScrollPane
	protected void initializeEditP() {
		initComponents();
		
		//static components
		LabeledSeparator basicsLS = new LabeledSeparator("Basics");
		JLabel statusL = new JLabel("Status:");
		JLabel scoreLabelL = new JLabel("Score:");
		statusL.setDisplayedMnemonic("S".charAt(0));
		statusL.setLabelFor(statusCB);
		JLabel entryTypeL = new JLabel("Entry Type:");
		Insets vghg = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		Insets vght = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_T, 0, 0);
		Insets ls = new Insets(DingsSwingConstants.SP_V_T, 0, DingsSwingConstants.SP_V_G, 0);
		Insets vthg = new Insets(DingsSwingConstants.SP_V_T, DingsSwingConstants.SP_H_G, 0, 0);
		JLabel emptyL = new JLabel(); //an empty label for layout purposes
		
		JScrollPane targetSP = new JScrollPane(answerTA);
		targetSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		targetSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		//set the visibility after everything has been initialized
		setVisibilities();
		
		//layout
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		editP = new JPanel();
		editP.setBorder(BorderFactory.createEmptyBorder(DingsSwingConstants.SP_V_C
				, DingsSwingConstants.SP_H_C
				, DingsSwingConstants.SP_V_C
				, DingsSwingConstants.SP_H_C));
		editP.setLayout(gbl);
		
		//----basicsLS
		gbc.gridwidth = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = ls;
		gbl.setConstraints(basicsLS, gbc);
		editP.add(basicsLS);
		//----base
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(questionL, gbc);
		editP.add(questionL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(questionSL, gbc);
		editP.add(questionSL);
		//----target
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(answerL, gbc);
		editP.add(answerL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(targetSP, gbc);
		editP.add(targetSP);
		//---- syællables characters
		if (null != charactersP) {
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.LINE_END;
			gbc.insets = vghg;
			gbl.setConstraints(emptyL, gbc);
			editP.add(emptyL);
			//----
			gbc.gridx = 1;
			gbc.gridwidth = 4;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.LINE_START;
			gbc.insets = vghg;
			gbl.setConstraints(charactersP, gbc);
			editP.add(charactersP);	
		}
		//----hint
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(hintL, gbc);
		editP.add(hintL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(hintPL, gbc);
		editP.add(hintPL);
		//----entrytype
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(entryTypeL, gbc);
		editP.add(entryTypeL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(entryTypeSL, gbc);
		editP.add(entryTypeSL);
		//----
		gbc.gridx = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 1.0d;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(emptyL, gbc);
		editP.add(emptyL);
		//----attributesLS
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 5;
		gbc.weightx = 0.0d;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = ls;
		gbl.setConstraints(attributesLS, gbc);
		editP.add(attributesLS);
		//----attributeOne
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(attributeOneL, gbc);
		editP.add(attributeOneL);
		//----
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(attributeOneCh, gbc);
		editP.add(attributeOneCh);
		//----attributeTwo
		gbc.gridx = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vght;
		gbl.setConstraints(attributeTwoL, gbc);
		editP.add(attributeTwoL);
		//----
		gbc.gridx = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(attributeTwoCh, gbc);
		editP.add(attributeTwoCh);
		//----attributeThree
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(attributeThreeL, gbc);
		editP.add(attributeThreeL);
		//----
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(attributeThreeCh, gbc);
		editP.add(attributeThreeCh);
		//----attributeFour
		gbc.gridx = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vght;
		gbl.setConstraints(attributeFourL, gbc);
		editP.add(attributeFourL);
		//----
		gbc.gridx = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(attributeFourCh, gbc);
		editP.add(attributeFourCh);
		//----otherLS
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.gridwidth = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = ls;
		gbl.setConstraints(othersLS, gbc);
		editP.add(othersLS);
		//----explanation
		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(explanationL, gbc);
		editP.add(explanationL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(explanationSL, gbc);
		editP.add(explanationSL);
		//----example
		gbc.gridx = 0;
		gbc.gridy = 11;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(exampleL, gbc);
		editP.add(exampleL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(exampleSL, gbc);
		editP.add(exampleSL);
		//----pronunciation
		gbc.gridx = 0;
		gbc.gridy = 12;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(pronunciationL, gbc);
		editP.add(pronunciationL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(pronunciationSL, gbc);
		editP.add(pronunciationSL);
		//----relation
		gbc.gridx = 0;
		gbc.gridy = 13;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(relationL, gbc);
		editP.add(relationL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(relationSL, gbc);
		editP.add(relationSL);
		//----unit
		gbc.gridx = 0;
		gbc.gridy = 14;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(unitL, gbc);
		editP.add(unitL);
		//----
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(unitsCh, gbc);
		editP.add(unitsCh);
		//----category
		gbc.gridx = 0;
		gbc.gridy = 15;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(categoryL, gbc);
		editP.add(categoryL);
		//----
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(categoriesCh, gbc);
		editP.add(categoriesCh);
		//----status
		gbc.gridx = 0;
		gbc.gridy = 16;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(statusL, gbc);
		editP.add(statusL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(statusCB, gbc);
		editP.add(statusCB);
		//----score
		gbc.gridx = 0;
		gbc.gridy = 17;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vthg;
		gbl.setConstraints(scoreLabelL, gbc);
		editP.add(scoreLabelL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vthg;
		gbl.setConstraints(scoreL, gbc);
		editP.add(scoreL);
	} //END protected void initializeEditP()
	
	/**
	 * Convenience method for setting the visibility of labels and fields
	 * based on the properties of the learning stack info.
	 */
	private void setVisibilities() {
		//convenience to temporarly store visibility info (and reduce number of calls
		int visTypeAttributes = Toolbox.getInstance().getInfoPointer().getVisibilityAttributes();
		int visUnit = Toolbox.getInstance().getInfoPointer().getVisibilityUnit();
		int visCategory = Toolbox.getInstance().getInfoPointer().getVisibilityCategory();
		int visExplanation = Toolbox.getInstance().getInfoPointer().getVisibilityExplanation();
		int visExample = Toolbox.getInstance().getInfoPointer().getVisibilityExample();
		int visPronunciation = Toolbox.getInstance().getInfoPointer().getVisibilityPronunciation();
		int visRelation = Toolbox.getInstance().getInfoPointer().getVisibilityRelation();
		//type attributes
		if (InfoVocab.VISIBILITY_NEVER == visTypeAttributes) {
			attributesLS.setVisible(false);
			attributeOneL.setVisible(false);
			attributeOneCh.setVisible(false);
			attributeTwoL.setVisible(false);
			attributeTwoCh.setVisible(false);
			attributeThreeL.setVisible(false);
			attributeThreeCh.setVisible(false);
			attributeFourL.setVisible(false);
			attributeFourCh.setVisible(false);
		}
		
		//global attributes
		int numberOfNotShownOthers = 0;
		if (InfoVocab.VISIBILITY_NEVER == visUnit) {
			numberOfNotShownOthers++;
			unitL.setVisible(false);
			unitsCh.setVisible(false);
		}
		if (InfoVocab.VISIBILITY_NEVER == visCategory) {
			numberOfNotShownOthers++;
			categoryL.setVisible(false);
			categoriesCh.setVisible(false);
		}
		//others
		if (InfoVocab.VISIBILITY_NEVER == visExplanation) {
			numberOfNotShownOthers++;
			explanationL.setVisible(false);
			explanationSL.setVisible(false);
		}
		else if (InfoVocab.VISIBILITY_SOLUTION == visExplanation) {
			explanationSL.setHideable(true);
		}
		else {
			explanationSL.setHideable(false);
		}
		if (InfoVocab.VISIBILITY_NEVER == visExample) {
			numberOfNotShownOthers++;
			exampleL.setVisible(false);
			exampleSL.setVisible(false);
		}
		else if (InfoVocab.VISIBILITY_SOLUTION == visExample) {
			exampleSL.setHideable(true);
		}
		else {
			exampleSL.setHideable(false);
		}
		if (InfoVocab.VISIBILITY_NEVER == visPronunciation) {
			numberOfNotShownOthers++;
			pronunciationL.setVisible(false);
			pronunciationSL.setVisible(false);
		}
		else if (InfoVocab.VISIBILITY_SOLUTION == visPronunciation) {
			pronunciationSL.setHideable(true);
		}
		else {
			pronunciationSL.setHideable(false);
		}
		if (InfoVocab.VISIBILITY_NEVER == visRelation) {
			numberOfNotShownOthers++;
			relationL.setVisible(false);
			relationSL.setVisible(false);
		}
		else if (InfoVocab.VISIBILITY_SOLUTION == visRelation) {
			relationSL.setHideable(true);
		}
		else {
			relationSL.setHideable(false);
		}
		if (6 == numberOfNotShownOthers) {
			othersLS.setVisible(false);
		}
	} //END private void setVisibilities()

	//implements AViewWithButtons
	protected final void initializeButtonP() {
		buttonsP = new JPanel();
		buttonsP.setLayout(new BoxLayout(buttonsP, BoxLayout.LINE_AXIS));
		buttonsP.add(Box.createHorizontalGlue());
		buttonsP.add(checkAnswerB);
		buttonsP.add(Box.createRigidArea(new Dimension(DingsSwingConstants.SP_H_G, 0)));
		buttonsP.add(showB);
		buttonsP.add(Box.createRigidArea(new Dimension(DingsSwingConstants.SP_H_G, 0)));
		buttonsP.add(knowB);
		buttonsP.add(Box.createRigidArea(new Dimension(DingsSwingConstants.SP_H_C, 0)));
		buttonsP.add(notKnowB);
	} //END protected final void initializeButtonP()
	
	//implements AViewWithButtons
	protected final void initButtonComponents() {
		checkAnswerB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.check_answer")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_MISC_BTN, ""));
		checkAnswerB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.check_answer").charAt(0));
		checkAnswerB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doCheckAnswer();
			}
		});
		showB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.show")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_RESULT_BTN, ""));
		showB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.show").charAt(0));
		showB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doShow();
			}
		});
		knowB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.know")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_KNOWN_BTN, "FIXME"));
		knowB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.know").charAt(0));
		knowB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (result != Result.HELPED) {
					result = Result.SUCCESS;
				}
				sendGetResults();
				sendNext();
			}
		});
		notKnowB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.dont_know")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_UNKNOWN_BTN, "FIXME"));
		notKnowB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.dont_know").charAt(0));
		notKnowB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				result = Result.WRONG;
				sendGetResults();
				sendNext();
			}
		});
	} //END protected final void initButtonComponents()

	/**
	 * Shows the entries.
	 */
	private void doShow() {
		hintPL.doResult();
		explanationSL.setHidden(false);
		pronunciationSL.setHidden(false);
		exampleSL.setHidden(false);
		relationSL.setHidden(false);

		showB.setEnabled(false);
		checkAnswerB.setEnabled(false);
		sendUpdateGUI();
	} //END private void doShow()
	
	private void doCheckAnswer() {
		AppEvent evt = new AppEvent(AppEvent.EventType.DATA_EVENT);
		evt.setMessage(MessageConstants.Message.D_ENTRY_LEARNONE_CHECKANSWER);
		controller.handleAppEvent(evt);
	} //END private void doCheckAnswer()

	/**
	 * Sends the events common to the know and notknow buttons
	 */
	private void sendGetResults() {
		AppEvent apeG = new AppEvent(AppEvent.EventType.DATA_EVENT);
		apeG.setMessage(MessageConstants.Message.D_ENTRY_LEARNONE_GETRESULT);
		controller.handleAppEvent(apeG);
	} //END private void sendGetResults()

	private void sendNext() {
		AppEvent apeN = new AppEvent(AppEvent.EventType.DATA_EVENT);
		apeN.setMessage(MessageConstants.Message.D_ENTRY_LEARNONE_NEXT);
		controller.handleAppEvent(apeN);
	} //END private void sendNext()

	public void sendUpdateGUI() {
		AppEvent ape = new AppEvent(AppEvent.EventType.DATA_EVENT);
		ape.setMessage(MessageConstants.Message.D_ENTRY_LEARNONE_REFRESH);
		controller.handleAppEvent(ape);
	} //END public void sendUpdateGUI()

	/**
	 * Called every time showing the next entry. Called after reset() in ADings.handleAppEvent for
	 * D_ENTRY_LEARNONE_NEXT
	 */
	public boolean init(IAppEventHandler aController) {
		this.controller = aController;
		if (null == controller) {
			return false;
		}
		sendUpdateGUI();
		//set selected indixes to null to hide attribute details
		//convenience to temporarly store visibility info (and reduce number of calls
		int visTypeAttributes = Toolbox.getInstance().getInfoPointer().getVisibilityAttributes();
		int visUnit = Toolbox.getInstance().getInfoPointer().getVisibilityUnit();
		int visCategory = Toolbox.getInstance().getInfoPointer().getVisibilityCategory();
		if (InfoVocab.VISIBILITY_QUERY == visTypeAttributes || InfoVocab.VISIBILITY_SOLUTION == visTypeAttributes) {
			attributeOneCh.setSelectedID(null);
			attributeTwoCh.setSelectedID(null);
			attributeThreeCh.setSelectedID(null);
			attributeFourCh.setSelectedID(null);
		}
		if (InfoVocab.VISIBILITY_QUERY == visUnit || InfoVocab.VISIBILITY_SOLUTION == visUnit) {
			unitsCh.setSelectedID(null);
		}
		if (InfoVocab.VISIBILITY_QUERY == visCategory || InfoVocab.VISIBILITY_SOLUTION == visCategory) {
			categoriesCh.setSelectedID(null);
		}
		return true;
	} //END public boolean init()
	
	//implements IEntryLearnOneView
	public void reset() {
		//reset scoring
		result = Result.SUCCESS;
		//reset the helps
		hintPL.reset();
		showB.setEnabled(true);
		checkAnswerB.setEnabled(true);
		//reset others
		explanationSL.setHidden(true);
		pronunciationSL.setHidden(true);
		exampleSL.setHidden(true);
		relationSL.setHidden(true);
		//reset attributes
		attributeOneL.setEnabled(false);
		attributeOneL.setText("N/A:");
		attributeOneCh.removeAllItems();
		attributeOneCh.setEnabled(false);
		attributeTwoL.setEnabled(false);
		attributeTwoL.setText("N/A:");
		attributeTwoCh.removeAllItems();
		attributeTwoCh.setEnabled(false);
		attributeThreeL.setEnabled(false);
		attributeThreeL.setText("N/A:");
		attributeThreeCh.removeAllItems();
		attributeThreeCh.setEnabled(false);
		attributeFourL.setEnabled(false);
		attributeFourL.setText("N/A:");
		attributeFourCh.removeAllItems();
		attributeFourCh.setEnabled(false);
		//reset for check answer
		answerTA.setBackground(textAreaDefaultBgColor);
		unitsCh.setBackground(choiceDefaultBgColor);
		categoriesCh.setBackground(choiceDefaultBgColor);
		attributeOneCh.setBackground(choiceDefaultBgColor);
		attributeTwoCh.setBackground(choiceDefaultBgColor);
		attributeThreeCh.setBackground(choiceDefaultBgColor);
		attributeFourCh.setBackground(choiceDefaultBgColor);
		knowB.setEnabled(true);
		notKnowB.setEnabled(true);
		answerTA.setText("");
	} //END public void reset()
	
	/**
	 * 
	 * @return the result of this learning. If didn't know, then Result.WRONG.
	 *          If known without help then Result.SUCCESS, else Result.HELPED
	 */
	public Result getResult() {
		return result;
	} //END public Result getResult()

	//---Setters and Getters
	
	public String getAnswer() {
		return answerTA.getText();
	} //ENd public String getAnswer()
		
	private final void setRealBase(String aBase) {
		if (null == aBase) {
			questionSL.setText("");
		}
		else {
			questionSL.setText(aBase);
		}		
	} //END private final void setRealBase(String)
	
	private void setRealTarget(String aTarget) {
		if (null == aTarget) {
			hintPL.setHintText("");
		}
		else {
			hintPL.setHintText(aTarget);
		}
	} //END public void setRealTarget(String)

	public void setBase(String aBase) {
		if (Toolbox.getInstance().isTargetAsked()) {
			setRealBase(aBase);
		} else {
			setRealTarget(aBase);
		}
	} //END public void setBase(String)

	public void setTarget(String aTarget) {
		if (Toolbox.getInstance().isTargetAsked()) {
			setRealTarget(aTarget);
		} else {
			setRealBase(aTarget);
		}
	} //END public void setTarget(String)
	
	public void setEntryType(String aLabel) {
		entryTypeSL.setText(aLabel);
	} //END public void setEntryType(String)

	public void setExplanation(String aExplanation) {
		if (null == aExplanation) {
			explanationSL.setText("");
		}
		else {
			explanationSL.setText(aExplanation);
		}
	} //END public void setExplanation(String)

	public void setPronunciation(String aPronunciation) {
		if (null == aPronunciation) {
			pronunciationSL.setText("");
		}
		else {
			pronunciationSL.setText(aPronunciation);
		}
	} //END public void setPronunciation(String)

	public void setExample(String anExample) {
		if (null == anExample) {
			exampleSL.setText("");
		}
		else {
			exampleSL.setText(anExample);
		}
	} //END public void setExample(String)

	public void setRelation(String aRelation) {
		if (null == aRelation) {
			relationSL.setText("");
		}
		else {
			relationSL.setText(aRelation);
		}
	} //END public void setRelation(String)

	public void setAttributeId(String anAttributeId, int aNumber) {
		switch (aNumber) {
			case 1:
				attributeOneCh.setSelectedIndex(0);
				attributeOneCh.setSelectedID(anAttributeId);
				break;
			case 2:
				attributeTwoCh.setSelectedIndex(0);
				attributeTwoCh.setSelectedID(anAttributeId);
				break;
			case 3:
				attributeThreeCh.setSelectedIndex(0);
				attributeThreeCh.setSelectedID(anAttributeId);
				break;
			case 4:
				attributeFourCh.setSelectedIndex(0);
				attributeFourCh.setSelectedID(anAttributeId);
				break;
		}
	} //END public void setAttributeId(String, int)

	public void setAttributeName(String anAttributeName, int aNumber) {
		switch (aNumber) {
			case 1:
				attributeOneL.setText(anAttributeName + ":");
				break;
			case 2:
				attributeTwoL.setText(anAttributeName + ":");
				break;
			case 3:
				attributeThreeL.setText(anAttributeName + ":");
				break;
			case 4:
				attributeFourL.setText(anAttributeName + ":");
				break;
		}
	} //END public void setAttributeName(String, int)

	public String getAttributeId(int aNumber) {
		switch (aNumber) {
			case 1:
				return attributeOneCh.getSelectedID();
			case 2:
				return attributeTwoCh.getSelectedID();
			case 3:
				return attributeThreeCh.getSelectedID();
			case 4:
				return attributeFourCh.getSelectedID();
		}
		return null;
	} //END public String getAttributeId(int)

	public void setStatus(boolean aStatus) {
		statusCB.setSelected(aStatus);
	} //END public void setStatus(boolean)

	public boolean getStatus() {
		return statusCB.isSelected();
	} //END public boolean getStatus()

	public void setScore(int aScore) {
		scoreL.setText(Integer.toString(aScore));
	} //END public void setScore(int)
	
	public String getUnitId() {
		return unitsCh.getSelectedID();
	} //END public String getUnitId()

	public void setUnitId(String aUnitId) {
		unitsCh.setSelectedIndex(0);
		unitsCh.setSelectedID(aUnitId);
	} //END public void setUnitId(String)
	
	public String getCategoryId() {
		return categoriesCh.getSelectedID();
	} //END public String getCategoryId()

	public void setCategoryId(String aCategoryId) {
		categoriesCh.setSelectedIndex(0);
		categoriesCh.setSelectedID(aCategoryId);
	} //END public void setCategoryId(String)

	public void setUnits(String[][] theUnits) {
		unitsCh.setItems(theUnits);
	} //END public void setUnits(String[][])

	public void setCategories(String[][] theCategories) {
		categoriesCh.setItems(theCategories);
	} //END public void setCategories(String[][])

	public void setAttributeItems(String[][] theItems, int aNumber) {
		switch (aNumber) {
			case 1:
				attributeOneCh.setItems(theItems);
				attributeOneL.setEnabled(true);
				attributeOneCh.setEnabled(true);
				break;
			case 2:
				attributeTwoCh.setItems(theItems);
				attributeTwoL.setEnabled(true);
				attributeTwoCh.setEnabled(true);
				break;
			case 3:
				attributeThreeCh.setItems(theItems);
				attributeThreeL.setEnabled(true);
				attributeThreeCh.setEnabled(true);
				break;
			case 4:
				attributeFourCh.setItems(theItems);
				attributeFourL.setEnabled(true);
				attributeFourCh.setEnabled(true);
				break;
		}
	} //END public void setAttributeItems(String[][], int)
	
	//------------------- check answer related --------------------------
	//implements IEntrlyLearnOneView
	public void setAnswerCorrect(boolean answerCorrect, Boolean[] globalAttributesCorrect, Boolean[] typeAttributesCorrect) {
		boolean allCorrect = true;
		if (answerCorrect) {
			answerTA.setBackground(CORRECT_COLOR);
		} else {
			answerTA.setBackground(WRONG_COLOR);
			allCorrect = false;
		}
		//global attributes
		if (Boolean.TRUE.equals(globalAttributesCorrect[0])) {
			unitsCh.setBackground(CORRECT_COLOR);
		} else if (Boolean.FALSE.equals(globalAttributesCorrect[0])) {
			unitsCh.setBackground(WRONG_COLOR);
			allCorrect = false;
		} // else do nothing i.e. Boolean is null
		if (Boolean.TRUE.equals(globalAttributesCorrect[1])) {
			categoriesCh.setBackground(CORRECT_COLOR);
		} else if (Boolean.FALSE.equals(globalAttributesCorrect[1])) {
			categoriesCh.setBackground(WRONG_COLOR);
			allCorrect = false;
		} // else do nothing i.e. Boolean is null
		
		// type attributes
		if (Boolean.TRUE.equals(typeAttributesCorrect[0])) {
			attributeOneCh.setBackground(CORRECT_COLOR);
		} else if (Boolean.FALSE.equals(typeAttributesCorrect[0])) {
			attributeOneCh.setBackground(WRONG_COLOR);
			allCorrect = false;
		} // else do nothing i.e. Boolean is null
		if (Boolean.TRUE.equals(typeAttributesCorrect[1])) {
			attributeTwoCh.setBackground(CORRECT_COLOR);
		} else if (Boolean.FALSE.equals(typeAttributesCorrect[1])) {
			attributeTwoCh.setBackground(WRONG_COLOR);
			allCorrect = false;
		} // else do nothing i.e. Boolean is null
		if (Boolean.TRUE.equals(typeAttributesCorrect[2])) {
			attributeThreeCh.setBackground(CORRECT_COLOR);
		} else if (Boolean.FALSE.equals(typeAttributesCorrect[2])) {
			attributeThreeCh.setBackground(WRONG_COLOR);
			allCorrect = false;
		} // else do nothing i.e. Boolean is null
		if (Boolean.TRUE.equals(typeAttributesCorrect[3])) {
			attributeFourCh.setBackground(CORRECT_COLOR);
		} else if (Boolean.FALSE.equals(typeAttributesCorrect[3])) {
			attributeFourCh.setBackground(WRONG_COLOR);
			allCorrect = false;
		} // else do nothing i.e. Boolean is null

		//enable / disable buttons
		if (allCorrect) {
			notKnowB.setEnabled(false);
			knowB.setEnabled(true);
		} else {
			knowB.setEnabled(false);
			notKnowB.setEnabled(true);
		}
	}

	//implements HintObserver
	public void hintOccured(boolean allShown) {
		result = Result.HELPED;
		if (allShown) {
			knowB.setEnabled(false);
			doShow();
		}
	} //END public void hintOccured(boolean)
	
	//------------------------ Inner classes for special listeners
	class SyllablesDocumentListener implements DocumentListener {

		public void changedUpdate(DocumentEvent e) {
			//nothing to do		
		}

		public void insertUpdate(DocumentEvent e) {
			doOnUpdate();
		}

		public void removeUpdate(DocumentEvent e) {
			doOnUpdate();
		}
		
		private void doOnUpdate() {
			//update the syllables label
			if ((Toolbox.getInstance().getInfoPointer().isBaseUsesSyllables() && (false == Toolbox.getInstance().isTargetAsked()))
					|| (Toolbox.getInstance().getInfoPointer().isTargetUsesSyllables() && (Toolbox.getInstance().isTargetAsked()))) {
				syllablesL.setText(answerTA.getText());
			}
		}	
	}
} //END public class EntryLearnOneView extends AViewWithScrollPane