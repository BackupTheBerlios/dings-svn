/*
 * InfoVocabEidtView.java
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
package net.vanosten.dings.swing;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import java.awt.ComponentOrientation;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.swing.helperui.ChoiceID;
import net.vanosten.dings.uiif.IInfoVocabEditView;

public class InfoVocabEditView extends AEditView implements IInfoVocabEditView {
	private JPanel basicsP;
	private JTextField titleTF, authorTF;
	private JTextArea notesTA;
	private JScrollPane notesSP;
	private JPanel labelsP;
	private JTextField  baseLabelTF, targetLabelTF, attributesLabelTF, unitLabelTF, categoryLabelTF, othersLabelTF, explanationLabelTF, exampleLabelTF;
	private JPanel localeP;
	private ChoiceID baseLocaleCB, targetLocaleCB, attributesLocaleCB;
	private ChoiceID unitLocaleCB, categoryLocaleCB;
	private ChoiceID explanationLocaleCB, exampleLocaleCB;
	private ChoiceID pronunciationLocaleCB, relationLocaleCB;
	private JPanel visibilitiesP;
	private JComboBox visibilityAttributesCB, visibilityUnitCB, visibilityCategoryCB;
	private JComboBox visibilityExplanationCB, visibilityExampleCB, visibilityPronunciationCB, visibilityRelationCB;

	public InfoVocabEditView(ComponentOrientation aComponentOrientation) {
		super("Edit Learning Stack Information", aComponentOrientation, false, false, MessageConstants.N_VIEW_ENTRIES_LIST);
	} //End public InfoVocabEditView(ComponentOrientation)
	
	private final void initComponents() {
		titleTF = new JTextField(50);
		titleTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});		
		authorTF = new JTextField(20);
		authorTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});		
		notesTA = new JTextArea();
		notesTA.setLineWrap(true);
		notesTA.setRows(5);
		notesTA.setWrapStyleWord(true);
		notesTA.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		notesSP = new JScrollPane(notesTA);
		notesSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//labels
		baseLabelTF = new JTextField(20);
		baseLabelTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});		
		targetLabelTF = new JTextField(20);
		targetLabelTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		attributesLabelTF = new JTextField();
		attributesLabelTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		unitLabelTF = new JTextField();
		unitLabelTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		categoryLabelTF = new JTextField();
		categoryLabelTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		othersLabelTF = new JTextField();
		othersLabelTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		explanationLabelTF = new JTextField();
		explanationLabelTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		exampleLabelTF = new JTextField();
		exampleLabelTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		//locales
		baseLocaleCB = new ChoiceID();
		baseLocaleCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		targetLocaleCB = new ChoiceID();
		targetLocaleCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		attributesLocaleCB = new ChoiceID();
		attributesLocaleCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		unitLocaleCB = new ChoiceID();
		unitLocaleCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		categoryLocaleCB = new ChoiceID();
		categoryLocaleCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		explanationLocaleCB = new ChoiceID();
		explanationLocaleCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		exampleLocaleCB = new ChoiceID();
		exampleLocaleCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		pronunciationLocaleCB = new ChoiceID();
		pronunciationLocaleCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		relationLocaleCB = new ChoiceID();
		relationLocaleCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		//visibility
		visibilityAttributesCB = new JComboBox();
		visibilityAttributesCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		visibilityUnitCB = new JComboBox();
		visibilityUnitCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		visibilityCategoryCB = new JComboBox();
		visibilityCategoryCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		visibilityExplanationCB = new JComboBox();
		visibilityExplanationCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		visibilityExampleCB = new JComboBox();
		visibilityExampleCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		visibilityPronunciationCB = new JComboBox();
		visibilityPronunciationCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		visibilityRelationCB = new JComboBox();
		visibilityRelationCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		//set items of CBs
		setVisibilityItems(visibilityAttributesCB);
		setVisibilityItems(visibilityUnitCB);
		setVisibilityItems(visibilityCategoryCB);
		setVisibilityItems(visibilityExplanationCB);
		setVisibilityItems(visibilityExampleCB);
		setVisibilityItems(visibilityPronunciationCB);
		setVisibilityItems(visibilityRelationCB);
	} //END private final void initComponents()
	
	//implements IInfoVocabEditView
	public void setAvailableLocales(String[][] theLocales) {
		isUpdating = true;		
		baseLocaleCB.setItems(theLocales);
		targetLocaleCB.setItems(theLocales);
		attributesLocaleCB.setItems(theLocales);
		unitLocaleCB.setItems(theLocales);
		categoryLocaleCB.setItems(theLocales);
		explanationLocaleCB.setItems(theLocales);
		exampleLocaleCB.setItems(theLocales);
		pronunciationLocaleCB.setItems(theLocales);
		relationLocaleCB.setItems(theLocales);
		isUpdating = false;
	} //END public void setAvailableLocales(String[][]);
	
	private void setVisibilityItems(JComboBox aCB) {
		isUpdating = true;		
		aCB.addItem("always"); //InfoVocab.VISIBILITY_ALWAYS
		aCB.addItem("one-to-one query and all solutions"); //InfoVocab.VISIBILITY_QUERY_ONE
		aCB.addItem("all solutions"); //InfoVocab.VISIBILITY_SOLUTION_ALL
		aCB.addItem("many-to-many solutions"); //InfoVocab.VISIBILITY_SOLUTION_MANY
		aCB.addItem("one-to-one solution"); //InfoVocab.VISIBILITY_SOLUTION_ONE
		aCB.addItem("only editing"); //InfoVocab.VISIBILITY_EDITING
		aCB.addItem("never"); //InfoVocab.VISIBILITY_NEVER
		isUpdating = false;
	} //END private void setVisibilityItems(JComboBox)
	
	//implements AViewWithScrollPane
	protected void initializeEditP() {
		initComponents();
		
		editP = new JPanel();
		editP.setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		editP.add(tabbedPane, BorderLayout.CENTER);

		//basics
		initializeBasicsP();
		tabbedPane.addTab("Basics", basicsP);
		tabbedPane.setMnemonicAt(0, "B".charAt(0));
		//labels
		initializeLabelsP();
		tabbedPane.addTab("Labels", labelsP);
		tabbedPane.setMnemonicAt(1, "L".charAt(0));
		//orientation
		initializeLocaleP();
		tabbedPane.addTab("Language of Text", localeP);
		tabbedPane.setMnemonicAt(2, "O".charAt(0));
		//visibilities
		initializeVisibilitiesP();
		tabbedPane.addTab("Visibility", visibilitiesP);
		tabbedPane.setMnemonicAt(3, "V".charAt(0));		
	} //End private void initializeEditP()
	
	private void initializeBasicsP() {
		basicsP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		basicsP.setLayout(gbl);
		EmptyBorder border = new EmptyBorder(
				DingsSwingConstants.SP_D_TOP
				, DingsSwingConstants.SP_D_LEFT
				, DingsSwingConstants.SP_D_BUTTOM
				, DingsSwingConstants.SP_D_RIGHT
			);
		basicsP.setBorder(border);
		
		JLabel titleL = new JLabel("Title");
		titleL.setDisplayedMnemonic("I".charAt(0));
		titleL.setLabelFor(titleTF);
		JLabel authorL = new JLabel("Author");
		authorL.setDisplayedMnemonic("A".charAt(0));
		authorL.setLabelFor(authorTF);
		JLabel notesL = new JLabel("Notes");
		notesL.setDisplayedMnemonic("N".charAt(0));
		notesL.setLabelFor(notesTA);
				
		//----
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_T, 0, 0, 0);
		gbl.setConstraints(titleL, gbc);
		basicsP.add(titleL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_T, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(titleTF, gbc);
		basicsP.add(titleTF);
		//----
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(authorL, gbc);
		basicsP.add(authorL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(authorTF, gbc);
		basicsP.add(authorTF);
		//----
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(notesL, gbc);
		basicsP.add(notesL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(notesSP, gbc);
		basicsP.add(notesSP);
	} //END private void initializeBasicsP()
	
	private void initializeLabelsP() {
		labelsP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		labelsP.setLayout(gbl);
		EmptyBorder border = new EmptyBorder(
				DingsSwingConstants.SP_D_TOP
				, DingsSwingConstants.SP_D_LEFT
				, DingsSwingConstants.SP_D_BUTTOM
				, DingsSwingConstants.SP_D_RIGHT
			);
		labelsP.setBorder(border);
		
		JLabel baseLabelL = new JLabel("Base");
		baseLabelL.setDisplayedMnemonic("S".charAt(0));
		baseLabelL.setLabelFor(baseLabelTF);
		JLabel targetLabelL = new JLabel("Target");
		targetLabelL.setDisplayedMnemonic("R".charAt(0));
		targetLabelL.setLabelFor(targetLabelTF);
		JLabel attributesLabelL = new JLabel("Attributes Section");
		attributesLabelL.setDisplayedMnemonic("A".charAt(0));
		attributesLabelL.setLabelFor(attributesLabelTF);
		JLabel unitLabelL = new JLabel("Unit");
		unitLabelL.setDisplayedMnemonic("U".charAt(0));
		unitLabelL.setLabelFor(unitLabelTF);
		JLabel categoryLabelL = new JLabel("Category");
		categoryLabelL.setDisplayedMnemonic("C".charAt(0));
		categoryLabelL.setLabelFor(categoryLabelTF);
		JLabel othersLabelL = new JLabel("Others Section");
		othersLabelL.setDisplayedMnemonic("O".charAt(0));
		othersLabelL.setLabelFor(othersLabelTF);
		JLabel explanationLabelL = new JLabel("Explanation");
		explanationLabelL.setDisplayedMnemonic("X".charAt(0));
		explanationLabelL.setLabelFor(explanationLabelTF);
		JLabel exampleLabelL = new JLabel("Example");
		exampleLabelL.setDisplayedMnemonic("M".charAt(0));
		exampleLabelL.setLabelFor(exampleLabelTF);

		//----
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_T, 0, 0, 0);
		gbl.setConstraints(baseLabelL, gbc);
		labelsP.add(baseLabelL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_T, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(baseLabelTF, gbc);
		labelsP.add(baseLabelTF);
		//----
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(targetLabelL, gbc);
		labelsP.add(targetLabelL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(targetLabelTF, gbc);
		labelsP.add(targetLabelTF);
		//----
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(attributesLabelL, gbc);
		labelsP.add(attributesLabelL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(attributesLabelTF, gbc);
		labelsP.add(attributesLabelTF);
		//----
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(unitLabelL, gbc);
		labelsP.add(unitLabelL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(unitLabelTF, gbc);
		labelsP.add(unitLabelTF);
		//----
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(categoryLabelL, gbc);
		labelsP.add(categoryLabelL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(categoryLabelTF, gbc);
		labelsP.add(categoryLabelTF);
		//----
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(othersLabelL, gbc);
		labelsP.add(othersLabelL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(othersLabelTF, gbc);
		labelsP.add(othersLabelTF);
		//----
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(explanationLabelL, gbc);
		labelsP.add(explanationLabelL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(explanationLabelTF, gbc);
		labelsP.add(explanationLabelTF);
		//----
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(exampleLabelL, gbc);
		labelsP.add(exampleLabelL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(exampleLabelTF, gbc);
		labelsP.add(exampleLabelTF);
	} //END private void initializeLabelsP()
	
	private void initializeLocaleP() {
		localeP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		localeP.setLayout(gbl);
		EmptyBorder border = new EmptyBorder(
				DingsSwingConstants.SP_D_TOP
				, DingsSwingConstants.SP_D_LEFT
				, DingsSwingConstants.SP_D_BUTTOM
				, DingsSwingConstants.SP_D_RIGHT
			);
		localeP.setBorder(border);
		
		JLabel baseLocaleL = new JLabel("Base");
		baseLocaleL.setDisplayedMnemonic("S".charAt(0));
		baseLocaleL.setLabelFor(baseLocaleCB);
		JLabel targetLocaleL = new JLabel("Target");
		targetLocaleL.setDisplayedMnemonic("R".charAt(0));
		targetLocaleL.setLabelFor(targetLocaleCB);
		JLabel attributesLocaleL = new JLabel("Attributes");
		attributesLocaleL.setDisplayedMnemonic("A".charAt(0));
		attributesLocaleL.setLabelFor(attributesLocaleCB);
		JLabel unitLocaleL = new JLabel("Unit");
		unitLocaleL.setDisplayedMnemonic("U".charAt(0));
		unitLocaleL.setLabelFor(unitLocaleCB);
		JLabel categoryLocaleL = new JLabel("Category");
		categoryLocaleL.setDisplayedMnemonic("C".charAt(0));
		categoryLocaleL.setLabelFor(categoryLocaleCB);
		JLabel explanationLocaleL = new JLabel("Explanation");
		explanationLocaleL.setDisplayedMnemonic("X".charAt(0));
		explanationLocaleL.setLabelFor(explanationLocaleCB);
		JLabel exampleLocaleL = new JLabel("Example");
		exampleLocaleL.setDisplayedMnemonic("M".charAt(0));
		exampleLocaleL.setLabelFor(exampleLocaleCB);
		JLabel pronunciationLocaleL = new JLabel("Pronunciation");
		pronunciationLocaleL.setDisplayedMnemonic("P".charAt(0));
		pronunciationLocaleL.setLabelFor(pronunciationLocaleCB);
		JLabel relationLocaleL = new JLabel("Relation");
		relationLocaleL.setDisplayedMnemonic("O".charAt(0));
		relationLocaleL.setLabelFor(relationLocaleCB);

		//----
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_T, 0, 0, 0);
		gbl.setConstraints(baseLocaleL, gbc);
		localeP.add(baseLocaleL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_T, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(baseLocaleCB, gbc);
		localeP.add(baseLocaleCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(targetLocaleL, gbc);
		localeP.add(targetLocaleL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(targetLocaleCB, gbc);
		localeP.add(targetLocaleCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(attributesLocaleL, gbc);
		localeP.add(attributesLocaleL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(attributesLocaleCB, gbc);
		localeP.add(attributesLocaleCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(unitLocaleL, gbc);
		localeP.add(unitLocaleL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(unitLocaleCB, gbc);
		localeP.add(unitLocaleCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(categoryLocaleL, gbc);
		localeP.add(categoryLocaleL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(categoryLocaleCB, gbc);
		localeP.add(categoryLocaleCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(explanationLocaleL, gbc);
		localeP.add(explanationLocaleL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(explanationLocaleCB, gbc);
		localeP.add(explanationLocaleCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(exampleLocaleL, gbc);
		localeP.add(exampleLocaleL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(exampleLocaleCB, gbc);
		localeP.add(exampleLocaleCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(pronunciationLocaleL, gbc);
		localeP.add(pronunciationLocaleL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(pronunciationLocaleCB, gbc);
		localeP.add(pronunciationLocaleCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(relationLocaleL, gbc);
		localeP.add(relationLocaleL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(relationLocaleCB, gbc);
		localeP.add(relationLocaleCB);
	} //END private void initializeOrientationsP()
	
	private void initializeVisibilitiesP() {
		visibilitiesP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		visibilitiesP.setLayout(gbl);
		EmptyBorder border = new EmptyBorder(
				DingsSwingConstants.SP_D_TOP
				, DingsSwingConstants.SP_D_LEFT
				, DingsSwingConstants.SP_D_BUTTOM
				, DingsSwingConstants.SP_D_RIGHT
			);
		visibilitiesP.setBorder(border);
		
		JLabel attributesL = new JLabel("Attributes");
		attributesL.setDisplayedMnemonic("A".charAt(0));
		attributesL.setLabelFor(visibilityAttributesCB);
		JLabel unitL = new JLabel("Unit");
		unitL.setDisplayedMnemonic("U".charAt(0));
		unitL.setLabelFor(visibilityUnitCB);
		JLabel categoryL = new JLabel("Category");
		categoryL.setDisplayedMnemonic("C".charAt(0));
		categoryL.setLabelFor(visibilityCategoryCB);
		JLabel explanationL = new JLabel("Explanation");
		explanationL.setDisplayedMnemonic("X".charAt(0));
		explanationL.setLabelFor(visibilityExplanationCB);
		JLabel exampleL = new JLabel("Example");
		exampleL.setDisplayedMnemonic("M".charAt(0));
		exampleL.setLabelFor(visibilityExampleCB);
		JLabel pronunciationL = new JLabel("Pronunciation");
		pronunciationL.setDisplayedMnemonic("P".charAt(0));
		pronunciationL.setLabelFor(visibilityPronunciationCB);
		JLabel relationL = new JLabel("Relation");
		relationL.setDisplayedMnemonic("R".charAt(0));
		relationL.setLabelFor(visibilityRelationCB);

		//----
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_T, 0, 0, 0);
		gbl.setConstraints(attributesL, gbc);
		visibilitiesP.add(attributesL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		//gbc.weightx = 1.0;
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_T, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(visibilityAttributesCB, gbc);
		visibilitiesP.add(visibilityAttributesCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(unitL, gbc);
		visibilitiesP.add(unitL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		//gbc.weightx = 1.0;
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(visibilityUnitCB, gbc);
		visibilitiesP.add(visibilityUnitCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(categoryL, gbc);
		visibilitiesP.add(categoryL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(visibilityCategoryCB, gbc);
		visibilitiesP.add(visibilityCategoryCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(explanationL, gbc);
		visibilitiesP.add(explanationL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(visibilityExplanationCB, gbc);
		visibilitiesP.add(visibilityExplanationCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(exampleL, gbc);
		visibilitiesP.add(exampleL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(visibilityExampleCB, gbc);
		visibilitiesP.add(visibilityExampleCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(pronunciationL, gbc);
		visibilitiesP.add(pronunciationL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(visibilityPronunciationCB, gbc);
		visibilitiesP.add(visibilityPronunciationCB);
		//----
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(relationL, gbc);
		visibilitiesP.add(relationL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(visibilityRelationCB, gbc);
		visibilitiesP.add(visibilityRelationCB);
	} //END private void initializeVisibilitiesP()
	
	public String getTitle() {
		return titleTF.getText();
	} //END public String getTitle()
    
	public void setTitle(String aTitle) {
		titleTF.setText(aTitle);
	} //END public void setTitle(String)
	
	public String getAuthor() {
		return authorTF.getText();
	} //END public String getAuthor()
    
	public void setAuthor(String anAuthor) {
		authorTF.setText(anAuthor);
	} //END public void setAuthor(String)
	
	public String getNotes() {
		return notesTA.getText();
	} //END public String getNotes()
    
	public void setNotes(String theNotes) {
		notesTA.setText(theNotes);
	} //END public void setNotes(String)
    
	public String getBaseLabel() {
		return baseLabelTF.getText();
	} //END public String getBase()
    
	public void setBaseLabel(String aBase) {
		baseLabelTF.setText(aBase);
	} //END public void setBase(String)
	
	public String getTargetLabel() {
		return targetLabelTF.getText();
	} //END public String getTarget()
    
	public void setTargetLabel(String aTarget) {
		targetLabelTF.setText(aTarget);
	} //END public void setTarget(String)
	
	public void setAttributesLabel(String aLabel) {
		attributesLabelTF.setText(aLabel);
	} //END public void setAttributesLabel(String)
	
	public String getAttributesLabel() {
		return attributesLabelTF.getText();
	} //END public String getAttributesLabel()
	
	public void setUnitLabel(String aLabel) {
		unitLabelTF.setText(aLabel);
	} //END public void setUnitLabel(String)
	
	public String getUnitLabel() {
		return unitLabelTF.getText();
	} //END public String getUnitLabel()
	
	public void setCategoryLabel(String aLabel) {
		categoryLabelTF.setText(aLabel);
	} //END public void setCategoryLabel(String)
	
	public String getCategoryLabel() {
		return categoryLabelTF.getText();
	} //END public String getCategoryLabel()
	
	public void setOthersLabel(String aLabel) {
		othersLabelTF.setText(aLabel);
	} //END public void setOthersLabel(String)
	
	public String getOthersLabel() {
		return othersLabelTF.getText();
	} //END public String getOhtersLabel()
	
	public void setExplanationLabel(String aLabel) {
		explanationLabelTF.setText(aLabel);
	} //END public void setExplanationLabel(String)
	
	public String getExplanationLabel() {
		return explanationLabelTF.getText();
	} //END public String getExplanationLabel()
	
	public void setExampleLabel(String aLabel) {
		exampleLabelTF.setText(aLabel);
	} //END public void setExampleLabel(String)
	
	public String getExampleLabel() {
		return exampleLabelTF.getText();
	} //END public String getExampleLabel()

	public void setBaseLocale(String aLocale) {
		isUpdating = true;
		baseLocaleCB.setSelectedID(aLocale);
		isUpdating = false;
	} //END public void setBaseLocale(String)

	public String getBaseLocale() {
		return baseLocaleCB.getSelectedID();
	} //END public String getBaseLocale()

	public void setTargetLocale(String aLocale) {
		isUpdating = true;
		targetLocaleCB.setSelectedID(aLocale);
		isUpdating = false;
	} //END public void setTargetLocale(String)

	public String getTargetLocale() {
		return targetLocaleCB.getSelectedID();
	} //END public String getTargetLocale()

	public void setAttributesLocale(String aLocale) {
		isUpdating = true;
		attributesLocaleCB.setSelectedID(aLocale);
		isUpdating = false;
	} //END public void setAttributesLocale(String)

	public String getAttributesLocale() {
		return attributesLocaleCB.getSelectedID();
	} //END public String getAttributesLocale()

	public void setUnitLocale(String aLocale) {
		isUpdating = true;
		unitLocaleCB.setSelectedID(aLocale);
		isUpdating = false;
	} //END public void setUnitLocale(String)

	public String getUnitLocale() {
		return unitLocaleCB.getSelectedID();
	} //END public String getUnitLocale()

	public void setCategoryLocale(String aLocale) {
		isUpdating = true;
		categoryLocaleCB.setSelectedID(aLocale);
		isUpdating = false;
	} //END public void setCategoryLocale(String)

	public String getCategoryLocale() {
		return categoryLocaleCB.getSelectedID();
	} //END public String getCategoryLocale()

	public void setExplanationLocale(String aLocale) {
		isUpdating = true;
		explanationLocaleCB.setSelectedID(aLocale);
		isUpdating = false;
	} //END public void setExplanationLocale(String)

	public String getExplanationLocale() {
		return explanationLocaleCB.getSelectedID();
	} //END public String getExplanationLocale()

	public void setExampleLocale(String aLocale) {
		isUpdating = true;
		exampleLocaleCB.setSelectedID(aLocale);
		isUpdating = false;
	} //END public void setExampleLocale(String)

	public String getExampleLocale() {
		return exampleLocaleCB.getSelectedID();
	} //END public String getExampleLocale()

	public void setPronunciationLocale(String aLocale) {
		isUpdating = true;
		pronunciationLocaleCB.setSelectedID(aLocale);
		isUpdating = false;
	} //END public void setPronunciationLocale(String)

	public String getPronunciationLocale() {
		return pronunciationLocaleCB.getSelectedID();
	} //END public String getPronunciationLocale()

	public void setRelationLocale(String aLocale) {
		isUpdating = true;
		relationLocaleCB.setSelectedID(aLocale);
		isUpdating = false;
	} //END public void setRelationLocale(String)

	public String getRelationLocale() {
		return relationLocaleCB.getSelectedID();
	} //END public String getRelationLocale()
	
	public void setVisibilityAttributes(int aVisibility) {
		isUpdating = true;		
		visibilityAttributesCB.setSelectedIndex(aVisibility);
		isUpdating = false;
	} //END public void setVisibilityAttributes(int)
	
	public int getVisibilityAttributes() {
		return visibilityAttributesCB.getSelectedIndex();
	} //END public int getVisibilityAttributes()
	
	public void setVisibilityUnit(int aVisibility) {
		isUpdating = true;		
		visibilityUnitCB.setSelectedIndex(aVisibility);
		isUpdating = false;
	} //END public void setVisibilityUnit(int)
	
	public int getVisibilityUnit() {
		return visibilityUnitCB.getSelectedIndex();
	} //END public int getVisibilityUnit()
	
	public void setVisibilityCategory(int aVisibility) {
		isUpdating = true;		
		visibilityCategoryCB.setSelectedIndex(aVisibility);
		isUpdating = false;
	} //END public void setVisibilityCategory(int)
	
	public int getVisibilityCategory() {
		return visibilityCategoryCB.getSelectedIndex();
	} //END public int getVisibilityCategory()
	
	public void setVisibilityExplanation(int aVisibility) {
		isUpdating = true;		
		visibilityExplanationCB.setSelectedIndex(aVisibility);
		isUpdating = false;
	} //END public void setVisibilityExplanation(int)
	
	public int getVisibilityExplanation() {
		return visibilityExplanationCB.getSelectedIndex();
	} //END public int getVisibilityExplanation()
	
	public void setVisibilityExample(int aVisibility) {
		isUpdating = true;		
		visibilityExampleCB.setSelectedIndex(aVisibility);
		isUpdating = false;
	} //END public void setVisibilityExample(int)
	
	public int getVisibilityExample() {
		return visibilityExampleCB.getSelectedIndex();
	} //END public int getVisibilityExample()
	
	public void setVisibilityPronunciation(int aVisibility) {
		isUpdating = true;		
		visibilityPronunciationCB.setSelectedIndex(aVisibility);
		isUpdating = false;
	} //END public void setVisibilityPronunciation(int)
	
	public int getVisibilityPronunciation() {
		return visibilityPronunciationCB.getSelectedIndex();
	} //END public int getVisibilityPronunciation()
	
	public void setVisibilityRelation(int aVisibility) {
		isUpdating = true;		
		visibilityRelationCB.setSelectedIndex(aVisibility);
		isUpdating = false;
	} //END public void setVisibilityRelation(int)
	
	public int getVisibilityRelation() {
		return visibilityRelationCB.getSelectedIndex();
	} //END public int getVisibilityRelation()
}	//END public class InfoVocabEditView extends AEditView implements IInfoVocabEditView
