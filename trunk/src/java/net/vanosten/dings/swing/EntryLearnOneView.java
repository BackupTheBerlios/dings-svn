/*
 * EntryLearnOneView.java
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
package net.vanosten.dings.swing;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.Box;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.swing.helperui.HintLabel;
import net.vanosten.dings.swing.helperui.ChoiceID;
import net.vanosten.dings.swing.helperui.SolutionLabel;
import net.vanosten.dings.swing.helperui.LabeledSeparator;
import net.vanosten.dings.uiif.IEntryLearnOneView;
import net.vanosten.dings.model.Preferences;
import net.vanosten.dings.model.InfoVocab;
import net.vanosten.dings.model.Toolbox;

public class EntryLearnOneView extends AViewWithScrollPane implements IEntryLearnOneView {
	private ChoiceID attributeOneCh, attributeTwoCh, attributeThreeCh, attributeFourCh, categoriesCh, unitsCh;
	private JCheckBox statusCB;
	private ChoiceID modeCh; //Which hint mode should be used
	private SolutionLabel baseSL, explanationSL, pronunciationSL, exampleSL, relationSL, entryTypeSL;
	private JTextField targetTF;
	private HintLabel hintHL;
	private JLabel attributeOneL, attributeTwoL, attributeThreeL, attributeFourL;
	private JLabel baseL, hintL, targetL, explanationL, exampleL, pronunciationL, relationL, unitL, categoryL, scoreL;
	private LabeledSeparator attributesLS, othersLS;
	private JButton hintB, showB, knowB, notKnowB;

	private boolean hintUsed = false; //did it require a hint
	private boolean success = false; //entry known?
	
	/** The learning direction is true, if target is asked */
	private boolean targetAsked = true;

	public EntryLearnOneView(ComponentOrientation aComponentOrientation) {
		super(Toolbox.getInstance().getLocalizedString("viewtitle.learn_one"), aComponentOrientation);
		initializeGUI();
		this.setGUIOrientation();
	} //END public EntryEditViewer(ComponentOrientation)
	
	private void initComponents() {
		//separators
		attributesLS = new LabeledSeparator("Attributes");
		othersLS = new LabeledSeparator("Others");
		//base
		baseL = new JLabel();
		baseSL = new SolutionLabel();
		//hint
		hintL = new JLabel("Hint:");
		hintHL = new HintLabel(Color.BLUE, Color.RED);
		//target
		targetL = new JLabel();
		targetTF = new JTextField();
		//entryType
		entryTypeSL = new SolutionLabel();
		//status
		statusCB = new JCheckBox("up-to-date");
		//score
		scoreL = new JLabel("?");
		//units
		unitL = new JLabel();
		unitsCh = new ChoiceID();
		//categories
		categoryL = new JLabel();
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
		explanationL = new JLabel();
		explanationSL = new SolutionLabel();
		//example
		exampleL = new JLabel();
		exampleSL = new SolutionLabel();
		//pronunciation
		pronunciationL = new JLabel("Pronunciation:");
		pronunciationSL = new SolutionLabel();
		//relation
		relationL = new JLabel("Relation:");
		relationSL = new SolutionLabel();		
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
		gbl.setConstraints(baseL, gbc);
		editP.add(baseL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(baseSL, gbc);
		editP.add(baseSL);
		//----target
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghg;
		gbl.setConstraints(targetL, gbc);
		editP.add(targetL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(targetTF, gbc);
		editP.add(targetTF);
		//----hint
		gbc.gridx = 0;
		gbc.gridy = 3;
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
		gbl.setConstraints(hintHL, gbc);
		editP.add(hintHL);
		//----entrytype
		gbc.gridx = 0;
		gbc.gridy = 4;
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
		gbc.gridy = 5;
		gbc.gridwidth = 5;
		gbc.weightx = 0.0d;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = ls;
		gbl.setConstraints(attributesLS, gbc);
		editP.add(attributesLS);
		//----attributeOne
		gbc.gridx = 0;
		gbc.gridy = 6;
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
		gbc.gridy = 7;
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
		gbc.gridy = 8;
		gbc.gridwidth = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = ls;
		gbl.setConstraints(othersLS, gbc);
		editP.add(othersLS);
		//----explanation
		gbc.gridx = 0;
		gbc.gridy = 9;
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
		gbc.gridy = 10;
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
		gbc.gridy = 11;
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
		gbc.gridy = 12;
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
		gbc.gridy = 13;
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
		gbc.gridy = 14;
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
		gbc.gridy = 15;
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
		gbc.gridy = 16;
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

	//implements AViewWithButtons
	protected final void initializeButtonP() {
		buttonsP = new JPanel();
		buttonsP.setLayout(new BoxLayout(buttonsP, BoxLayout.LINE_AXIS));
		buttonsP.add(Box.createHorizontalGlue());
		buttonsP.add(modeCh);
		buttonsP.add(Box.createRigidArea(new Dimension(DingsSwingConstants.SP_H_C, 0)));
		buttonsP.add(hintB);
		buttonsP.add(Box.createRigidArea(new Dimension(DingsSwingConstants.SP_H_T, 0)));
		buttonsP.add(showB);
		buttonsP.add(Box.createRigidArea(new Dimension(DingsSwingConstants.SP_H_G, 0)));
		buttonsP.add(knowB);
		buttonsP.add(Box.createRigidArea(new Dimension(DingsSwingConstants.SP_H_C, 0)));
		buttonsP.add(notKnowB);
	} //END protected final void initializeButtonP()
	
	//implements AViewWithButtons
	protected final void initButtonComponents() {
		String[][] modeItems = {
					{Integer.toString(HintLabel.MODE_FLASH), "Flash"}
					,{Integer.toString(HintLabel.MODE_LETTER), "Letter"}
					,{Integer.toString(HintLabel.MODE_SHUFFLE), "Shuffle"}
		};
		modeCh = new ChoiceID();
		modeCh.setItems(modeItems);
		modeCh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (HintLabel.MODE_LETTER == Integer.parseInt(modeCh.getSelectedID())) {
					hintHL.resetLetters();
				}
			}
		});
		hintB = new JButton("Hint", DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_HINT_BTN, ""));
		hintB.setMnemonic("I".charAt(0));
		hintB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doHint();
			}
		});
		showB = new JButton("Show", DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_RESULT_BTN, ""));
		showB.setMnemonic("S".charAt(0));
		showB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doShow();
			}
		});
		knowB = new JButton("Know it", DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_KNOWN_BTN, "FIXME"));
		knowB.setMnemonic("K".charAt(0));
		knowB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				success = true;
				sendGetResults();
				sendNext();
			}
		});
		notKnowB = new JButton("Don't know", DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_UNKNOWN_BTN, "FIXME"));
		notKnowB.setMnemonic("N".charAt(0));
		notKnowB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				success = false;
				sendGetResults();
				sendNext();
			}
		});
	} //END protected final void initButtonComponents()
	
	/**
	 * Sets the colors for the hint text according to preferences.
	 * This is done repeately to cope with changes in preferences without restart of learning.
	 */
	private void setHintTextColors() {
		Color hintC = null;
		Color resultC = null;
		try {
			hintC = new Color(Integer.parseInt(Toolbox.getInstance().getPreferencesPointer().getProperty(Preferences.PROP_COLOR_HINT)));
			resultC = new Color(Integer.parseInt(Toolbox.getInstance().getPreferencesPointer().getProperty(Preferences.PROP_COLOR_RESULT)));
		}
		catch(NumberFormatException e) {
			hintC = Color.BLUE;
			resultC = Color.RED;
		}
		hintHL.setTextColors(hintC, resultC);
	} //END private void setHintTextColors()

	/**
	 * Displays a hint in the target field.
	 */
	private void doHint() {
		setHintTextColors();
		try {
			int helperMode = Integer.parseInt(modeCh.getSelectedID());
			switch(helperMode) {
				case HintLabel.MODE_FLASH:
					hintHL.doFlash(Integer.parseInt(Toolbox.getInstance().getPreferencesPointer().getProperty(Preferences.LEARN_HINT_FLASH_TIME)));
					break;
				case HintLabel.MODE_LETTER:
					if (false == hintHL.doLetters()) {
						knowB.setEnabled(false);
						doShow();
					}
					break;
				case HintLabel.MODE_SHUFFLE:
					hintHL.doShuffle();
					break;
			}
			hintUsed = true;
		}
		catch (NumberFormatException e) {
			//TODO: this should never happen, but ...
		}
	} //END private void doHint()

	/**
	 * Shows the entries.
	 */
	private void doShow() {
		setHintTextColors();
		hintHL.doResult();
		explanationSL.setHidden(false);
		pronunciationSL.setHidden(false);
		exampleSL.setHidden(false);
		relationSL.setHidden(false);

		hintB.setEnabled(false);
		showB.setEnabled(false);
		sendUpdateGUI();
	} //END private void doShow()

	/**
	 * Sends the events common to the know and notknow buttons
	 */
	private void sendGetResults() {
		AppEvent apeG = new AppEvent(AppEvent.DATA_EVENT);
		apeG.setMessage(MessageConstants.D_ENTRY_LEARNONE_GETRESULT);
		controller.handleAppEvent(apeG);
	} //END private void sendGetResults()

	private void sendNext() {
		AppEvent apeN = new AppEvent(AppEvent.DATA_EVENT);
		apeN.setMessage(MessageConstants.D_ENTRY_LEARNONE_NEXT);
		controller.handleAppEvent(apeN);
	} //END private void sendNext()

	public void sendUpdateGUI() {
		AppEvent ape = new AppEvent(AppEvent.DATA_EVENT);
		ape.setMessage(MessageConstants.D_ENTRY_LEARNONE_REFRESH);
		controller.handleAppEvent(ape);
	} //END public void sendUpdateGUI()

	/**
	 * Initialize when first shown and reset for continuous
	 */
	public boolean init(IAppEventHandler aController) {
		this.controller = aController;
		if (null == controller) {
			return false;
		}
		sendUpdateGUI();
		//set selected indixes to null to hide attribute details
		attributeOneCh.setSelectedID(null);
		attributeTwoCh.setSelectedID(null);
		attributeThreeCh.setSelectedID(null);
		attributeFourCh.setSelectedID(null);
		return true;
	} //END public boolean init()
	
	//implements IEntryLearnOneView
	public void reset() {
		//reset scoring
		hintUsed = false;
		success = false;
		//reset the helps
		hintHL.doHide();
		hintHL.resetLetters();
		hintB.setEnabled(true);
		showB.setEnabled(true);
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
	} //END public void reset()

	public boolean isHintUsed() {
		return hintUsed;
	} //END public boolean isHintUsed()

	public boolean isSuccess() {
		return success;
	} //END public boolean isSuccess()

	//---Setters and Getters
	
	//implements IEntrlyLearnOneView
	public final void setTargetAsked(boolean isTargetAsked) {
		this.targetAsked = isTargetAsked;
	} //END public final void setTargetAsked(boolean)
	
	private final void setRealBase(String aBase) {
		if (null == aBase) {
			baseSL.setText("");
		}
		else {
			baseSL.setText(aBase);
		}		
	} //END private final void setRealBase(String)
	
	public void setRealTarget(String aTarget) {
		if (null == aTarget) {
			hintHL.setHintText("");
		}
		else {
			hintHL.setHintText(aTarget);
		}
	} //END public void setRealTarget(String)

	public void setBase(String aBase) {
		if (targetAsked) {
			setRealBase(aBase);
		} else {
			setRealTarget(aBase);
		}
	} //END public void setBase(String)

	public void setTarget(String aTarget) {
		if (targetAsked) {
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

	public void setUnitId(String aUnitId) {
		unitsCh.setSelectedIndex(0);
		unitsCh.setSelectedID(aUnitId);
	} //END public void setUnitId(String)

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
	
	//implements IEntryLearnOneView
	public void setLabels(String aBaseL, String aTargetL, String anAttributesL, String aUnitL, String aCategoryL
						  , String anOthersL, String anExplanationL, String anExampleL) {
		baseL.setText(aBaseL + ":");
		targetL.setText(aTargetL + ":");
		attributesLS.setText(anAttributesL + ":");
		unitL.setText(aUnitL + ":");
		categoryL.setText(aCategoryL + ":");
		othersLS.setText(anOthersL + ":");
		explanationL.setText(anExplanationL + ":");
		exampleL.setText(anExampleL + ":");
	} //END public void setLabels(String ...)
	
	//implements IEntryEditView
	public void setVisibilities(int anAttributesVis, int aUnitVis, int aCategoryVis
			, int anExplanationVis, int anExampleVis
			, int aPronunciationVis, int aRelationVis) {
		//attributes
		if (InfoVocab.VISIBILITY_NEVER == anAttributesVis) {
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
		//units and categories
		if (InfoVocab.VISIBILITY_EDITING == aUnitVis || InfoVocab.VISIBILITY_NEVER == aUnitVis) {
			unitL.setVisible(false);
			unitsCh.setVisible(false);
		}
		if (InfoVocab.VISIBILITY_EDITING == aCategoryVis || InfoVocab.VISIBILITY_NEVER == aCategoryVis) {
			categoryL.setVisible(false);
			categoriesCh.setVisible(false);
		}
		//others
		if (InfoVocab.VISIBILITY_EDITING == anExplanationVis || InfoVocab.VISIBILITY_NEVER == anExplanationVis) {
			explanationL.setVisible(false);
			explanationSL.setVisible(false);
		}
		else if (InfoVocab.VISIBILITY_SOLUTION_ONE == anExplanationVis) {
			explanationSL.setHideable(true);
		}
		else {
			explanationSL.setHideable(false);
		}
		if (InfoVocab.VISIBILITY_EDITING == anExampleVis || InfoVocab.VISIBILITY_NEVER == anExampleVis) {
			exampleL.setVisible(false);
			exampleSL.setVisible(false);
		}
		else if (InfoVocab.VISIBILITY_SOLUTION_ONE == anExampleVis) {
			exampleSL.setHideable(true);
		}
		else {
			exampleSL.setHideable(false);
		}
		if (InfoVocab.VISIBILITY_EDITING == aPronunciationVis ||InfoVocab.VISIBILITY_NEVER == aPronunciationVis) {
			pronunciationL.setVisible(false);
			pronunciationSL.setVisible(false);
		}
		else if (InfoVocab.VISIBILITY_SOLUTION_ONE == aPronunciationVis) {
			pronunciationSL.setHideable(true);
		}
		else {
			pronunciationSL.setHideable(false);
		}
		if (InfoVocab.VISIBILITY_EDITING == aRelationVis || InfoVocab.VISIBILITY_NEVER == aRelationVis) {
			relationL.setVisible(false);
			relationSL.setVisible(false);
		}
		else if (InfoVocab.VISIBILITY_SOLUTION_ONE == aRelationVis) {
			relationSL.setHideable(true);
		}
		else {
			relationSL.setHideable(false);
		}
		//test whether to hide the others separator, too
		if ((InfoVocab.VISIBILITY_NEVER == anExplanationVis || InfoVocab.VISIBILITY_NEVER == anExplanationVis) &&
				(InfoVocab.VISIBILITY_NEVER == anExampleVis || InfoVocab.VISIBILITY_NEVER == anExampleVis) &&
				(InfoVocab.VISIBILITY_NEVER == aPronunciationVis || InfoVocab.VISIBILITY_NEVER == aPronunciationVis) &&
				(InfoVocab.VISIBILITY_NEVER == aRelationVis || InfoVocab.VISIBILITY_NEVER == aRelationVis) &&
				(InfoVocab.VISIBILITY_NEVER == aUnitVis || InfoVocab.VISIBILITY_NEVER == aUnitVis) &&
				(InfoVocab.VISIBILITY_NEVER == aCategoryVis || InfoVocab.VISIBILITY_NEVER == aCategoryVis)) {
			othersLS.setVisible(false);
		}
	} //END public void setVisibilities(int, int, int, int)
} //END public class EntryLearnOneView extends AViewWithScrollPane