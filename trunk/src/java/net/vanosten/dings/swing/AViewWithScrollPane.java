/*
 * AViewWithScrollPane.java
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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.awt.ComponentOrientation;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public abstract class AViewWithScrollPane extends AViewWithButtons {
	
	/** The central panel with the widgets for editing */
	protected JPanel editP; 

	public AViewWithScrollPane(String aTitle, ComponentOrientation aComponentOrientation) {
		super(aTitle, aComponentOrientation);
	} //END public AViewWithScrollPane(String, ComponentOrientation)
	
	//Implements AEditView
	protected void initializeMainP() {
		mainP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		mainP.setLayout(gbl);
		
		//editP
		editP = new JPanel();
		EmptyBorder border = new EmptyBorder(
			DingsSwingConstants.SP_D_TOP
			, DingsSwingConstants.SP_D_LEFT
			, DingsSwingConstants.SP_D_BUTTOM
			, DingsSwingConstants.SP_D_RIGHT
		);
		editP.setBorder(border);
		initializeEditP();
		JScrollPane scrollP = new JScrollPane(editP);
		
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbl.setConstraints(scrollP, gbc);
		mainP.add(scrollP);
	} //END protected void initializeMainP()
	
	protected abstract void initializeEditP();
} //END public abstract class AViewWithScrollPane
