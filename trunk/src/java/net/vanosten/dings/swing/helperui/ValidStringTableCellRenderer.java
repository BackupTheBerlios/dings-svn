/*
 * ValidStringTableCellRenderer.java
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

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import net.vanosten.dings.swing.DingsSwingConstants;

/*
 * A cell renderer to show a valid String with one background color and an invalid String
 * with another background color.
 * The validity of the string is determined, when the string set as invalid equals to the value.
 * Thyere is most porbably a smarter way to do this.
 */
public class ValidStringTableCellRenderer extends JLabel implements TableCellRenderer {
	
	/** The string to compare to to test for invalidity */
	private String invalid;
	
	public ValidStringTableCellRenderer() {
		super();
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.LEADING);
	} //END public ValidStringTableCellRenderer()
	
	/**
	 * Implements the interface. If the value of the cell is not a string, then nothing happens
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (null == value) {
			setBackground(DingsSwingConstants.INVALID_INPUT);
			return this;
		}
		if (value instanceof String) {
			String theValue = ((String) value).trim();
			setText(theValue);
			if (null == invalid || (false == invalid.equals(theValue))) {
				setBackground(DingsSwingConstants.VALID_INPUT);
			} else {
				setBackground(DingsSwingConstants.INVALID_INPUT);
			}
		}
		return this;
	} //END public Component getTableCellRendereComponent(...)
	
	/**
	 * Sets the String to denote an invalid String
	 * @param invalid
	 */
	public final void setInvalidString(String invalid) {
		this.invalid = invalid;
	} //END public final void setInvalidString(String
} //END public class LevelTableCellRenderer
