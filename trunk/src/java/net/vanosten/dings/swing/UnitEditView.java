/*
 * UnitEditView.java
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

import java.awt.ComponentOrientation;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import net.vanosten.dings.uiif.IUnitEditView;

public class UnitEditView extends AEditView implements IUnitEditView {
	private JTextField nameTF;
	private JTextArea descriptionTA;
	
	public UnitEditView(String aTitle, ComponentOrientation aComponentOrientation, String aMessage) {
		super(aTitle, aComponentOrientation, true, true, aMessage);
	} //End public UnitEditView(String, ComponentOrientation, String)

	//Implements AEditView
	protected void initializeEditP() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		editP.setLayout(gbl);

		JLabel nameL = new JLabel("Name:");
		nameTF = new JTextField(50);
		nameTF.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		nameL.setDisplayedMnemonic("N".charAt(0));
		nameL.setLabelFor(nameTF);

		JLabel descriptionL = new JLabel("Description:");
		descriptionTA = new JTextArea();
		descriptionTA.setLineWrap(true);
		descriptionTA.setWrapStyleWord(true);
		descriptionTA.setRows(5);
		descriptionTA.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent evt) {
			}
			public void keyReleased(KeyEvent evt) {
				onChange();
			}
			public void keyPressed(KeyEvent evt) {
			}
		});
		descriptionL.setDisplayedMnemonic("E".charAt(0));
		descriptionL.setLabelFor(descriptionTA);
		JScrollPane descriptionSP = new JScrollPane(descriptionTA);
		descriptionSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		//put together
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbl.setConstraints(nameL, gbc);
		editP.add(nameL);
		//------
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(0, DingsSwingConstants.SP_H_G, 0, 0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(nameTF, gbc);
		editP.add(nameTF);
		//------
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(descriptionL, gbc);
		editP.add(descriptionL);
		//------
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(descriptionSP, gbc);
		editP.add(descriptionSP);
		//set focus
		nameTF.requestFocus();
	}	//End protected void initializeEditP()
	
	public void setName(String aName) {
		nameTF.setText(aName);
	}	//END public void setUnitName(String)
	
	public String getName() {
		return nameTF.getText();
	}	//END public String getName()
	
	public void setDescription(String aDescription) {
		descriptionTA.setText(aDescription);
	}	//END public void setDescription(String)
	
	public String getDescription() {
		return descriptionTA.getText();
	}	//END public String getDescription()
}	//END public class UnitEditView
