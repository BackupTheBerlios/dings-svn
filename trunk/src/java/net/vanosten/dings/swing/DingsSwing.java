/*
 * DingsSwing.java
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

import net.vanosten.dings.model.ADings;

public class DingsSwing extends ADings {
	
	public DingsSwing(String[] args) {
		super(args);
		//nothing to do
	} //END public DingsSwing(String[])
	
	//implements ADings
	protected void initializeGUI() {
		mainWindow = new MainWindow(this, preferences, ComponentOrientation.getOrientation(locale));
	} //END protected void initializeGUI()

	/**
	 * The entry point of the program.
	 * @param args - the command line parameters. There are no arguments needed.
	 */
	public static void main(String[] args) {
		DingsSwing myDings = new DingsSwing(args);
	} //end public static void main(String[])
} //END public class DingsSwing extends ADings
