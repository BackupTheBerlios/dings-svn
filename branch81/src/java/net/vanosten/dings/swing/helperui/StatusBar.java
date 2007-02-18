/*
 * StatusBar.java
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
package net.vanosten.dings.swing.helperui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import java.awt.FlowLayout;

import net.vanosten.dings.swing.DingsSwingConstants;

public class StatusBar extends JPanel {
	private final static long serialVersionUID = 1L;

	/** The status of saving */
	private JLabel statusL;

	/** The numbers of entries and chosen entries */
	private JLabel selectionL;

	public StatusBar() {
		super();
		initializeGUI();
	} //END public StatusBar()

	private void initializeGUI() {
		statusL = new JLabel(" ");
		selectionL = new JLabel(" "); //needs to have a blank for display

		//set fonts
		statusL.setFont(DingsSwingConstants.STATUS_BAR_FONT);
		selectionL.setFont(DingsSwingConstants.STATUS_BAR_FONT);

		//panels
		JPanel statusP = new JPanel(new FlowLayout(FlowLayout.LEADING, DingsSwingConstants.SP_H_C,0));
		JPanel selectionP = new JPanel(new FlowLayout(FlowLayout.LEADING, DingsSwingConstants.SP_H_C,0));
		statusP.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		selectionP.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		statusP.add(statusL);
		selectionP.add(selectionL);

		//layout
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();

		this.setLayout(gbl);

		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1.0d;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(statusP, gbc);
		this.add(statusP);
		//----
		gbc.weightx = 3.0d;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(selectionP,gbc);
		this.add(selectionP);
	} //END private void initializeGUI()

	public void setStatusText(String aStatusText, String aSelectionText) {
		if(null == aStatusText || 1 > aStatusText.length()) {
			statusL.setText(" "); //make sure statusbar height is ok
		}
		else {
			statusL.setText(aStatusText);
		}
		if(null == aSelectionText || 1 > aSelectionText.length()) {
			selectionL.setText(" ");
		}
		else {
			selectionL.setText(aSelectionText);
		}
	} //END public void setStatusText(String, String)
} //END public class StatusBar extends JPanel
