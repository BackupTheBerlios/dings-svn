/*
 * ValidatedTextField.java
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
package net.vanosten.dings.swing.helperui;

import javax.swing.JTextField;

import net.vanosten.dings.swing.DingsSwingConstants;

/**
 * A JTextField wich gives the possibility for visual feedback of whether the value
 * is valid or not.
 * Note that javax.swing.InputVerifier is not used because (a) it only works when loosing
 * focus and therefore not on every key-stroke, (b) the validation process should be
 * independent of Swing and be entirely processed in the model.
 */
public class ValidatedTextField extends JTextField {
	/**
	 * Constructor
	 * @param columns The number of visible colums
	 * @param validColor The background color if the value is valid
	 * @param invalidColor The background color if the value is invalid
	 */
	public ValidatedTextField(int columns) {
		super(columns);
		setBackground(DingsSwingConstants.VALID_INPUT);
	} //END public ValidatedTextField(int)
	
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
} //END public class ValidatedTextField extends JTextField
