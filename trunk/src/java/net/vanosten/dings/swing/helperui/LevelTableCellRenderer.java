/*
 * LevelTableCellRenderer.java
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

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Color;

public class LevelTableCellRenderer extends JLabel implements TableCellRenderer {
	private final static long serialVersionUID = 1L;
	
	
	public LevelTableCellRenderer() {
		super();
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.CENTER);
	} //END public LevelTableCellRenderer()
	
	/**
	 * Implements the interface
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (null == value) {
			//log error
			return this;
		}
		if (value instanceof Integer) {
			Integer myInt = (Integer)value;
			setText(myInt.toString());
			int tempInt = myInt.intValue();
			setForeground(Color.white);
			switch(tempInt) {
				case 1: setBackground(new Color(165,42,42)); break;
				case 2: setBackground(new Color(255,0,0)); break;
				case 3: setBackground(new Color(255,124,0)); break;
				case 4: setBackground(new Color(255,200,0)); break;
				case 5: setBackground(new Color(255,255,0)); break;
				case 6: setBackground(new Color(0,255,0)); break;
				default: setBackground(new Color(42,165,42));
			}
		} else {
			//TODO: log error
			setText("error");
		}
		return this;
	} //END public Component getTableCellRendereComponent(...)
} //END public class LevelTableCellRenderer
