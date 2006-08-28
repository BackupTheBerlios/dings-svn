/*
 * SolutionLabel.java
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

import javax.swing.JLabel;

import java.awt.Color;

import net.vanosten.dings.swing.DingsSwingConstants;

public class SolutionLabel extends JLabel {
	private final static long serialVersionUID = 1L;

	/** Whether the text can be hidden at all */
	private boolean hideable = false;

	/**
	 * The only contructor. All other constructors are hidden and
	 * must be simulated by means of method calls
	 */
	public SolutionLabel() {
		super();
		this.setFont(DingsSwingConstants.SOLUTION_FONT);
	} //END public SolutionLabel()

	/**
	 * Whether the text should be unreadable.
	 * @param boolean - true if the text should be invisible (same color as background)
	 */
	public void setHidden(boolean hidden) {
		if (hidden && hideable) {
			//set foreground color to background color
			Color myForeground = this.getBackground();
			this.setForeground(myForeground);
		}
		else {
			//set foreground color to foreground color of the container
			this.setForeground(this.getParent().getForeground());
		}
	} //END public void setHidden(boolean hidden)

	/**
	 * Whether the text can be made hidden by calling <code>setHidden(true)
	 * @param hideable
	 */
	public void setHideable(boolean hideable) {
		this.hideable = hideable;
	} //END public void setHideable(boolean)
	
	/**
	 * Overrides JLabel.setText(String) by setting the text as an
	 * html text and thereby allow multiple lines.
	 */
	public void setText(String text) {
		super.setText("<html>" + text + "</html>");
	} //END public void setText(String text)
} //END public class SolutionLabel extends JLabel
