/*
 * EntryEditView.java
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

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;

import java.awt.ComponentOrientation;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.model.InfoVocab;
import net.vanosten.dings.swing.helperui.ChoiceID;
import net.vanosten.dings.swing.helperui.SolutionLabel;
import net.vanosten.dings.swing.helperui.LabeledSeparator;
import net.vanosten.dings.uiif.IEntryEditView;
import net.vanosten.dings.event.AppEvent;

public class EntryEditView extends AEditView implements IEntryEditView {
	private ChoiceID unitsCh, categoriesCh, attributeOneCh, attributeTwoCh, attributeThreeCh, attributeFourCh;
	private JCheckBox statusCB;
	private JTextField baseTF, targetTF, explanationTF, pronunciationTF, exampleTF, relationTF;
	private JLabel attributeOneL, attributeTwoL, attributeThreeL, attributeFourL; //populated based on entry type
	private JLabel baseL, targetL, unitL, categoryL, explanationL, exampleL; //populated based on InfoVocab
	private JLabel pronunciationL, relationL; //visible based on InfoVocab
	private SolutionLabel entryTypeSL;
	private JButton changeEntryTypeB;
	private LabeledSeparator attributesLS, othersLS;
	
	/** Stores the labels and ids of the available enttry types */
	private String[][] entryTypesList;
	private String entryTypeId;

	public EntryEditView(ComponentOrientation aComponentOrientation) {
		super("Edit Entry", aComponentOrientation, true, true, MessageConstants.N_VIEW_ENTRIES_LIST);
	} //End public EntryEditViewer(ComponentOrientation)

	private void initComponents() {
		//separators
		attributesLS = new LabeledSeparator("Attributes");
		othersLS = new LabeledSeparator("Others");
		//labels
		baseL = new JLabel();
		targetL = new JLabel();
		entryTypeSL = new SolutionLabel();
		unitL = new JLabel();
		categoryL = new JLabel();
		explanationL = new JLabel();
		exampleL = new JLabel();
		pronunciationL = new JLabel("Pronunciation");
		relationL = new JLabel("Relation");
		
		attributeOneL = new JLabel("N/A");
		attributeTwoL = new JLabel("N/A");
		attributeThreeL = new JLabel("N/A");
		attributeFourL = new JLabel("N/A");
		attributeOneL.setEnabled(false);
		attributeTwoL.setEnabled(false);
		attributeThreeL.setEnabled(false);		
		attributeFourL.setEnabled(false);		
		
		//base
		baseTF = new JTextField();
		baseTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		//target
		targetTF = new JTextField();
		targetTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		//entry type
		changeEntryTypeB = new JButton("Change Entry Type ...");
		changeEntryTypeB.setMnemonic("C".charAt(0));
		changeEntryTypeB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChangeEntryType();
			}
		});
		//unit
		unitsCh = new ChoiceID();
		unitsCh.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		//category
		categoriesCh = new ChoiceID();
		categoriesCh.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		//status
		statusCB = new JCheckBox("up-to-date");
		statusCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		//attributeOne
		attributeOneCh = new ChoiceID();
		attributeOneCh.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		attributeOneCh.setEnabled(false);
		//attributeTwo
		attributeTwoCh = new ChoiceID();
		attributeTwoCh.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		attributeTwoCh.setEnabled(false);
		//attributeThree
		attributeThreeCh = new ChoiceID();
		attributeThreeCh.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		attributeThreeCh.setEnabled(false);
		//attributeFour
		attributeFourCh = new ChoiceID();
		attributeFourCh.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		attributeFourCh.setEnabled(false);
		//explanation
		explanationTF = new JTextField();
		explanationTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		//example
		exampleTF = new JTextField();
		exampleTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		//pronunciation
		pronunciationTF = new JTextField();
		pronunciationTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});

		//relation
		relationTF = new JTextField();
		relationTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});	
	} //END private void initComponents()
	
	//implements AViewWithScrollPane
	protected void initializeEditP() {
		initComponents();
		
		//static components
		LabeledSeparator basicsLS = new LabeledSeparator("Basics");
		JLabel statusL = new JLabel("Status");
		statusL.setDisplayedMnemonic("S".charAt(0));
		statusL.setLabelFor(statusCB);
		JLabel entryTypeL = new JLabel("Entry Type");
		Insets vghg = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		Insets vght = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_T, 0, 0);
		Insets ls = new Insets(DingsSwingConstants.SP_V_T, 0, DingsSwingConstants.SP_V_G, 0);
		JLabel emptyL = new JLabel(); //an empty label for layout purposes
		//have the entry type text and button close to each other
		JPanel entryTypeP = new JPanel();
		entryTypeP.setLayout(new BoxLayout(entryTypeP, BoxLayout.LINE_AXIS));
		entryTypeP.add(entryTypeSL);
		entryTypeP.add(Box.createRigidArea(new Dimension(DingsSwingConstants.SP_H_G, 0)));
		entryTypeP.add(changeEntryTypeB);
		entryTypeP.add(Box.createHorizontalGlue());
		
		//layout
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		editP = new JPanel();
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
		gbl.setConstraints(baseTF, gbc);
		editP.add(baseTF);
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
		//----entrytype
		gbc.gridx = 0;
		gbc.gridy = 3;
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
		gbl.setConstraints(entryTypeP, gbc);
		editP.add(entryTypeP);
		//----
		gbc.gridx = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 1.0d;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(emptyL, gbc);
		editP.add(emptyL);
		//----attributesLS
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 5;
		gbc.weightx = 0.0d;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = ls;
		gbl.setConstraints(attributesLS, gbc);
		editP.add(attributesLS);
		//----attributeOne
		gbc.gridx = 0;
		gbc.gridy = 5;
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
		gbc.gridy = 6;
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
		gbc.gridy = 7;
		gbc.gridwidth = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = ls;
		gbl.setConstraints(othersLS, gbc);
		editP.add(othersLS);
		//----explanation
		gbc.gridx = 0;
		gbc.gridy = 8;
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
		gbl.setConstraints(explanationTF, gbc);
		editP.add(explanationTF);
		//----example
		gbc.gridx = 0;
		gbc.gridy = 9;
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
		gbl.setConstraints(exampleTF, gbc);
		editP.add(exampleTF);
		//----pronunciation
		gbc.gridx = 0;
		gbc.gridy = 10;
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
		gbl.setConstraints(pronunciationTF, gbc);
		editP.add(pronunciationTF);
		//----relation
		gbc.gridx = 0;
		gbc.gridy = 11;
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
		gbl.setConstraints(relationTF, gbc);
		editP.add(relationTF);
		//----unit
		gbc.gridx = 0;
		gbc.gridy = 12;
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
		gbc.gridy = 13;
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
		gbc.gridy = 14;
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
	} //End private void initializeEditP()
	
	private void onChangeEntryType() {
		//make the GUI
		JPanel dialogP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		dialogP.setLayout(gbl);
		
		JLabel textL = new JLabel();
		textL.setText("<html><p>Choose one of the Entry Types in the list below.</p>"
				+ "<p>Please be aware that all Entry Type related attribute data will be lost!</p></html>");
		ChoiceID entryTypesCh = new ChoiceID();
		//remove the current entry type from the list of available entry types
		String[][] theItems = new String[entryTypesList.length - 1][];
		int a = 0;
		for (int i = 0; i < entryTypesList.length; i++) {
			if (false == entryTypesList[i][0].equals(entryTypeId)) {
				theItems[a] = entryTypesList[i];
				a++;
			}
		}
		entryTypesCh.setItems(theItems);
		//put GUI together
		gbl.setConstraints(textL, gbc);
		dialogP.add(textL);
		gbc.gridy = 1;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(entryTypesCh, gbc);
		dialogP.add(entryTypesCh);		
		
		Object[] options = {"OK", "Cancel"};
		int answer = JOptionPane.showOptionDialog(this,
													dialogP,
													"",
													JOptionPane.OK_CANCEL_OPTION,
													JOptionPane.QUESTION_MESSAGE,
													null,     //don't use a custom Icon
													options,  //the titles of buttons
													options[0]); //default button title
	
		//if ok, show entry edit 
		if (JOptionPane.OK_OPTION == answer) {
			AppEvent evt = new AppEvent(AppEvent.DATA_EVENT);
			evt.setMessage(MessageConstants.D_EDIT_VIEW_CHANGE_ENTRY_TYPE);
			evt.setDetails(entryTypesCh.getSelectedID());
			controller.handleAppEvent(evt);
		}
		//else do nothing
	} //END private void onChangeEntryType()

	public void setBase(String aBase) {
		if (null == aBase) baseTF.setText("");
		else baseTF.setText(aBase);
	} //END public void setBase(String)

	public String getBase() {
		return baseTF.getText();
	} //END public String getBase()

	public void setTarget(String aTarget) {
		if (null == aTarget) targetTF.setText("");
		else targetTF.setText(aTarget);
	} //END public void setTarget(String)

	public String getTarget() {
		return targetTF.getText();
	} //END public String getTarget()
	
	public void setEntryType(String aLabel, String anId) {
		entryTypeSL.setText(aLabel);
		entryTypeId = anId;
	} //END public void setEntryType(String, String)

	public void setExplanation(String aExplanation) {
		if (null == aExplanation) explanationTF.setText("");
		else explanationTF.setText(aExplanation);
	} //END public void setExplanation(String)

	public String getExplanation() {
		return explanationTF.getText();
	} //END public String getExplanation()

	public void setPronunciation(String aPronunciation) {
		if (null == aPronunciation) pronunciationTF.setText("");
		else pronunciationTF.setText(aPronunciation);
	} //END public void setPronunciation(String)

	public String getPronunciation() {
		return pronunciationTF.getText();
	} //END public String getPronunciation()

	public void setExample(String anExample) {
		if (null == anExample) exampleTF.setText("");
		else exampleTF.setText(anExample);
	} //END public void setExample(String)

	public String getExample() {
		return exampleTF.getText();
	} //END public String getExample()

	public void setRelation(String aRelation) {
		if (null == aRelation) relationTF.setText("");
		else relationTF.setText(aRelation);
	} //END public void setRelation(String)

	public String getRelation() {
		return relationTF.getText();
	} //END public String getRelation()

	public void setAttributeId(String anAttributeId, int aNumber) {
		isUpdating = true;
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
		isUpdating = false;
	} //END public void setAttributeId(String, int)

	public void setAttributeName(String anAttributeName, int aNumber) {
		switch (aNumber) {
			case 1:
				attributeOneL.setText(anAttributeName);
				break;
			case 2:
				attributeTwoL.setText(anAttributeName);
				break;
			case 3:
				attributeThreeL.setText(anAttributeName);
				break;
			case 4:
				attributeFourL.setText(anAttributeName);
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

	public void setUnitId(String aUnitId) {
		isUpdating = true;
		unitsCh.setSelectedIndex(0);
		unitsCh.setSelectedID(aUnitId);
		isUpdating = false;
	} //END public void setUnitId(String)

	public String getUnitId() {
		return unitsCh.getSelectedID();
	} //END public String getUnitID()

	public void setCategoryId(String aCategoryId) {
		isUpdating = true;
		categoriesCh.setSelectedIndex(0);
		categoriesCh.setSelectedID(aCategoryId);
		isUpdating = false;
	} //END public void setCategoryId(String)

	public String getCategoryId() {
		return categoriesCh.getSelectedID();
	} //END public String getCategoryId()

	public void setStatus(boolean aStatus) {
		isUpdating = true;
		statusCB.setSelected(aStatus);
		isUpdating = false;
	} //END public void setStatus(boolean)

	public boolean getStatus() {
		return statusCB.isSelected();
	} //END public boolean getStatus()

	public void setUnits(String[][] theUnits) {
		isUpdating = true;
		unitsCh.setItems(theUnits);
		isUpdating = false;
	} //END public void setUnits(String[][])

	public void setCategories(String[][] theCategories) {
		isUpdating = true;
		categoriesCh.setItems(theCategories);
		isUpdating = false;
	} //END public void setCategories(String[][])

	
	/**
	 * Prepares a combobox for choice of entry type, when the dialog for 
	 * change is called. 
	 *
	 * @param String[][] theEntryTypes - the entry types and respective IDs
	 */
	public void setEntryTypes(String[][] theEntryTypes) {
		if (1 == theEntryTypes.length) {
			changeEntryTypeB.setEnabled(false);
		}
		else {
			changeEntryTypeB.setEnabled(true);
		}
		this.entryTypesList = theEntryTypes;
	} //END public void setEntryTypes(String[][])
	
	public void setAttributeItems(String[][] theItems, int aNumber) {
		isUpdating = true;
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
		isUpdating = false;
	} //END public void setAttributeItems(String[][], int)
	
	//implements IEntryEditView
	public void setLabels(String aBaseL, String aTargetL, String anAttributesL, String aUnitL, String aCategoryL
						  , String anOthersL, String anExplanationL, String anExampleL) {
		baseL.setText(aBaseL);
		targetL.setText(aTargetL);
		attributesLS.setText(anAttributesL);
		unitL.setText(aUnitL);
		categoryL.setText(aCategoryL);
		othersLS.setText(anOthersL);
		explanationL.setText(anExplanationL);
		exampleL.setText(anExampleL);
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
		//units and category
		if (InfoVocab.VISIBILITY_NEVER == aUnitVis) {
			unitL.setVisible(false);
			unitsCh.setVisible(false);
		}
		if (InfoVocab.VISIBILITY_NEVER == aCategoryVis) {
			categoryL.setVisible(false);
			categoriesCh.setVisible(false);
		}
		//others
		if (InfoVocab.VISIBILITY_NEVER == anExplanationVis) {
			explanationL.setVisible(false);
			explanationTF.setVisible(false);
		}
		if (InfoVocab.VISIBILITY_NEVER == anExampleVis) {
			exampleL.setVisible(false);
			exampleTF.setVisible(false);
		}
		if (InfoVocab.VISIBILITY_NEVER == aPronunciationVis) {
			pronunciationL.setVisible(false);
			pronunciationTF.setVisible(false);
		}
		if (InfoVocab.VISIBILITY_NEVER == aRelationVis) {
			relationL.setVisible(false);
			relationTF.setVisible(false);
		}
		//others separator is always visible due to status checkbox
	} //END public void setVisibilities(int, int, int, int)
} //END public class EntryEditView extends AEditView implements IEntryEditView