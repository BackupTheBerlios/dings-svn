/*
 * LabeledSeparator.java
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
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import net.vanosten.dings.swing.DingsSwingConstants;

public class LabeledSeparator extends JPanel {
	private final static long serialVersionUID = 1L;

	/** The label for showing the title text */
	private JLabel label;

	/**
	 * Default constructor
	 */
	public LabeledSeparator(String aText) {
		super();
		initializeGUI(aText);
	} //END public LabeledSeparator()

	private void initializeGUI(String aText) {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);

		label = new JLabel(aText);
		label.setHorizontalAlignment(SwingConstants.LEADING);
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

		gbl.setConstraints(label, gbc);
		this.add(label);
		//----
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(0, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(separator, gbc);
		this.add(separator);
	} //END private void initializeGUI()

	/**
	 * Sets the displayed text of the separator.
	 *
	 * @param aText
	 */
	public void setText(String aText) {
		label.setText(aText);
	} //END public void setText(String aText)
} //END public class LabeledSeparator extends JPanel
