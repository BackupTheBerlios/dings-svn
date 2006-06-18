/*
 * ValidatedTextArea.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2006 Rick Gruber-Riemer (dingsbums@vanosten.net)
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

import javax.swing.JTextArea;

import net.vanosten.dings.swing.DingsSwingConstants;

/**
 * A JTextArea wich gives the possibility for visual feedback of whether the value
 * is valid or not.
 * See also ValidatedTextfield for more explanation
 */
public class ValidatedTextArea extends JTextArea {
	private final static long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param rows The number of visible colums
	 */
	public ValidatedTextArea(int rows) {
		super();
		setRows(rows);
		setLineWrap(true);
		setWrapStyleWord(true);
		setBackground(DingsSwingConstants.VALID_INPUT);
	} //END public ValidatedTextArea(int)
	
	/**
	 * Changes the background color based on the validity of the fileds value
	 * @param valid
	 */
	public final void isValueValid(boolean valid) {
		if (valid) {
			setBackground(DingsSwingConstants.VALID_INPUT);
		} else {
			setBackground(DingsSwingConstants.INVALID_INPUT);
		}
	} //END public final void isValueValid(boolean)
} //END public class ValidatedTextArea extends JTextArea
