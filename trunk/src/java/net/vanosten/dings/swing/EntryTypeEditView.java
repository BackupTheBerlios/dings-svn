/*
 * EntryTypeEditView.java
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
package net.vanosten.dings.swing;

import java.awt.ComponentOrientation;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.model.EntryType;
import net.vanosten.dings.utils.Toolbox;
import net.vanosten.dings.swing.helperui.ChoiceID;
import net.vanosten.dings.swing.helperui.ValidatedTextField;
import net.vanosten.dings.uiif.IEntryTypeEditView;

public class EntryTypeEditView extends AEditView implements IEntryTypeEditView {
	private final static long serialVersionUID = 1L;

	private ValidatedTextField nameVTF;
	private JCheckBox attribOneEnabledCB, attribTwoEnabledCB, attribThreeEnabledCB, attribFourEnabledCB;
	private ChoiceID attribOneTypeCh, attribTwoTypeCh, attribThreeTypeCh, attribFourTypeCh;


	public EntryTypeEditView(ComponentOrientation aComponentOrientation) {
		super(Toolbox.getInstance().getLocalizedString("viewtitle.edit_entry_type")
				, aComponentOrientation
				, true
				, true
				, MessageConstants.Message.N_VIEW_ENTRYTYPES_LIST);
	} //END public EntryTypeEditView(ComponentOrientation)

	//Implements AEditView
	protected void initializeEditP() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		editP.setLayout(gbl);

		JLabel nameL = new JLabel("Name:");
		nameL.setDisplayedMnemonic(("N").charAt(0));
		nameVTF = new ValidatedTextField(50);
		nameVTF.setToolTipText("Name may not be empty");
		nameL.setLabelFor(nameVTF);
		nameVTF.addKeyListener(this);

		attribOneEnabledCB = new JCheckBox("Attribute 1:");
		attribOneEnabledCB.setMnemonic(("1").charAt(0));
		attribOneEnabledCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onEnabled(1);
			}
		});
		attribTwoEnabledCB = new JCheckBox("Attribute 2:");
		attribTwoEnabledCB.setMnemonic(("2").charAt(0));
		attribTwoEnabledCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onEnabled(2);
			}
		});
		attribThreeEnabledCB = new JCheckBox("Attribute 3:");
		attribThreeEnabledCB.setMnemonic(("3").charAt(0));
		attribThreeEnabledCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onEnabled(3);
			}
		});
		attribFourEnabledCB = new JCheckBox("Attribute 4:");
		attribFourEnabledCB.setMnemonic(("4").charAt(0));
		attribFourEnabledCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onEnabled(4);
			}
		});
		attribOneTypeCh = new ChoiceID();
		attribOneTypeCh.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		attribTwoTypeCh = new ChoiceID();
		attribTwoTypeCh.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		attribThreeTypeCh = new ChoiceID();
		attribThreeTypeCh.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		attribFourTypeCh = new ChoiceID();
		attribFourTypeCh.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				onChange();
			}
		});
		JLabel emptyL = new JLabel(); //a simple place holder

		//put together
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbl.setConstraints(nameL, gbc);
		editP.add(nameL);
		//------
		gbc.gridx = 1;
		gbc.weightx = 1.0d;
		gbc.insets = new Insets (0,DingsSwingConstants.SP_H_G,0,0);
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(nameVTF, gbc);
		editP.add(nameVTF);
		//------
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0d;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_G,0,0,0);
		gbc.gridwidth = 1;
		gbl.setConstraints(emptyL, gbc);
		editP.add(emptyL);
		//------
		gbc.gridx = 1;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_G,DingsSwingConstants.SP_H_G,0,0);
		gbl.setConstraints(attribOneEnabledCB, gbc);
		editP.add(attribOneEnabledCB);
		//------
		gbc.gridx = 2;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_G,DingsSwingConstants.SP_H_C,0,0);
		gbl.setConstraints(attribOneTypeCh, gbc);
		editP.add(attribOneTypeCh);
		//------
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_G,0,0,0);
		gbl.setConstraints(emptyL, gbc);
		editP.add(emptyL);
		//------
		gbc.gridx = 1;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_G,DingsSwingConstants.SP_H_G,0,0);
		gbl.setConstraints(attribTwoEnabledCB, gbc);
		editP.add(attribTwoEnabledCB);
		//------
		gbc.gridx = 2;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_G,DingsSwingConstants.SP_H_C,0,0);
		gbl.setConstraints(attribTwoTypeCh, gbc);
		editP.add(attribTwoTypeCh);
		//------
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_G,0,0,0);
		gbl.setConstraints(emptyL, gbc);
		editP.add(emptyL);
		//------
		gbc.gridx = 1;
		gbc.weighty = 0.0d;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_G,DingsSwingConstants.SP_H_G,0,0);
		gbl.setConstraints(attribThreeEnabledCB, gbc);
		editP.add(attribThreeEnabledCB);
		//------
		gbc.gridx = 2;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_G,DingsSwingConstants.SP_H_C,0,0);
		gbl.setConstraints(attribThreeTypeCh, gbc);
		editP.add(attribThreeTypeCh);
		//------
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_G,0,0,0);
		gbl.setConstraints(emptyL, gbc);
		editP.add(emptyL);
		//------
		gbc.gridx = 1;
		gbc.weighty = 0.0d;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_G,DingsSwingConstants.SP_H_G,0,0);
		gbl.setConstraints(attribFourEnabledCB, gbc);
		editP.add(attribFourEnabledCB);
		//------
		gbc.gridx = 2;
		gbc.insets = new Insets (DingsSwingConstants.SP_V_G,DingsSwingConstants.SP_H_C,0,0);
		gbl.setConstraints(attribFourTypeCh, gbc);
		editP.add(attribFourTypeCh);
		//------
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 3;
		gbc.weighty = 1.0d;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbl.setConstraints(emptyL, gbc);
		editP.add(emptyL);
		//set focus
		nameVTF.requestFocus();
	} //END protected void initializeEditP()

	//implements IEntryTypeEditView
	public void setName(String aName) {
		nameVTF.setText(aName);
	} //END public void setUnitName(String)

	//	implements IEntryTypeEditView
	public String getName() {
		return nameVTF.getText();
	} //END public String getName()

	//	implements IEntryTypeEditView
	public void setAttributeChoices(String[][] theAttributes) {
		isUpdating = true;
		attribOneTypeCh.setItems(theAttributes);
		attribTwoTypeCh.setItems(theAttributes);
		attribThreeTypeCh.setItems(theAttributes);
		attribFourTypeCh.setItems(theAttributes);
		isUpdating = false;
	} //END public void setAttributeChoices(String[][])

	//	implements IEntryTypeEditView
	public void setAttributes(Long[] anId) {
		isUpdating = true;

		attribOneEnabledCB.setSelected(false);
		attribOneTypeCh.setEnabled(false);
		attribOneTypeCh.setSelectedID(Constants.UNDEFINED_ID);
		attribTwoEnabledCB.setEnabled(false);
		attribTwoEnabledCB.setSelected(false);
		attribTwoTypeCh.setEnabled(false);
		attribTwoTypeCh.setSelectedID(Constants.UNDEFINED_ID);
		attribThreeEnabledCB.setEnabled(false);
		attribThreeEnabledCB.setSelected(false);
		attribThreeTypeCh.setEnabled(false);
		attribThreeTypeCh.setSelectedID(Constants.UNDEFINED_ID);
		attribFourEnabledCB.setEnabled(false);
		attribFourEnabledCB.setSelected(false);
		attribFourTypeCh.setEnabled(false);
		attribFourTypeCh.setSelectedID(Constants.UNDEFINED_ID);

		if (null != anId[0]) {
			attribOneEnabledCB.setSelected(true);
			attribOneTypeCh.setEnabled(true);
			attribOneTypeCh.setSelectedID(anId[0]);
			attribTwoEnabledCB.setEnabled(true);
			if (null != anId[1]) {
				attribTwoEnabledCB.setSelected(true);
				attribTwoTypeCh.setEnabled(true);
				attribTwoTypeCh.setSelectedID(anId[1]);
				attribThreeEnabledCB.setEnabled(true);
				if (null != anId[2]) {
					attribThreeEnabledCB.setSelected(true);
					attribThreeTypeCh.setEnabled(true);
					attribThreeTypeCh.setSelectedID(anId[2]);
					attribFourEnabledCB.setEnabled(true);
					if (null != anId[3]) {
						attribFourEnabledCB.setSelected(true);
						attribFourTypeCh.setEnabled(true);
						attribFourTypeCh.setSelectedID(anId[2]);
					}
				}
			}
		}
		isUpdating = false;
	} //END public void setAttributeName(String[])

	// implements IEntryTypeEditView
	public Long[] getAttributes() {
		Long[] theIds = new Long[EntryType.NUMBER_OF_ATTRIBUTES];
		if (attribOneEnabledCB.isSelected()) {
			theIds[0] = attribOneTypeCh.getSelectedID();
		}
		if (attribTwoEnabledCB.isSelected()) {
			theIds[1] = attribTwoTypeCh.getSelectedID();
		}
		if (attribThreeEnabledCB.isSelected()) {
			theIds[2] = attribThreeTypeCh.getSelectedID();
		}
		if (attribFourEnabledCB.isSelected()) {
			theIds[3] = attribFourTypeCh.getSelectedID();
		}
		return theIds;
	} //END public String[] getAttributes()

	//-------------------------------------------

	private void onEnabled(int attribNo) {
		switch (attribNo) {
			case 1:
			attribOneTypeCh.setEnabled(attribOneEnabledCB.isSelected());
			if (attribOneEnabledCB.isSelected()) {
				attribTwoEnabledCB.setEnabled(true);
			}
			else {
				attribOneTypeCh.setSelectedID(null);
			}
			break;

			case 2:
			attribTwoTypeCh.setEnabled(attribTwoEnabledCB.isSelected());
			if (attribTwoEnabledCB.isSelected()) {
				attribThreeEnabledCB.setEnabled(true);
			}
			else {
				attribTwoTypeCh.setSelectedID(null);
			}
			break;

			case 3:
			attribThreeTypeCh.setEnabled(attribThreeEnabledCB.isSelected());
			if (attribThreeEnabledCB.isSelected()) {
				attribFourEnabledCB.setEnabled(true);
			}
			else {
				attribThreeTypeCh.setSelectedID(null);
			}
			break;

			case 4:
			attribFourTypeCh.setEnabled(attribFourEnabledCB.isSelected());
			if (attribFourEnabledCB.isSelected()) {
				//do nothing
			}
			else {
				attribFourTypeCh.setSelectedID(null);
			}
			break;
		}
		//set to changed
		onChange();
	} //END private void onEnabled(int)

	//implements IEntryTypeEditView
	public void setNameIsValueValid(boolean valid) {
		nameVTF.isValueValid(valid);
	} //END public void setNameIsValueValid(boolean)
} //END public class EntryTypeEditView extends AEditView implements IEntryTypeEditView
