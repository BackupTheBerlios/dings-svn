/*
 * DingsSwingConstants.java
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

import java.awt.Color;
import java.awt.Insets;

public class DingsSwingConstants {
	
	/** Set private in order to prevent instantiations */
	private DingsSwingConstants() {
	} //private DingsSwingConstants()
	
	/*-------------------JGoodies Spacing ---------------------*/
	public final static String V_C = "3dlu"; //vertical component
	public final static String V_G = "9dlu"; //vertial group
	public final static String H_C = "3dlu"; //horizontal component
	public final static String H_G = "7dlu"; //horizontal group
	public final static String H_S = "1dlu"; //horizontal statusbar
	
	/*------------------- Spacing -----------------------------*/
	/*
	 * SP = SPACING
	 * D = DIALOG
	 * H = HORIZONTAL
	 * V = VERTICAL
	 * L = LABEL
	 * G = GROUP
	 * T = THEME
	 * C = COMPONENT
	 * COM = COMMAND
	 */
	public final static int SP_D_TOP = 12; //TODO: Shouldn't it be 11?
	public final static int SP_D_LEFT = 12;
	public final static int SP_D_RIGHT = 11;
	public final static int SP_D_BUTTOM = 11;

	public final static int SP_H_G = 12; //between labels and buttons or 
	public final static int SP_H_C = 6; //between buttons
	public final static int SP_H_T = 24; //between button groups
	public final static int SP_V_G = 12; //between groups of elements
	public final static int SP_V_C = 6; //between groups of related RadioButtons or Checkbuttons
	public final static int SP_V_T = 24; //intro text and different themes
	public final static int SP_V_COM = 18; //before command buttons 
	
	public final static Insets INSETS_DEFAULT = new Insets(5,0,6,0); //FIXME: this should be deleted
	
	/*------------------- Colors ------------------------------*/
	//The primary color two of the Metal L&F.
	//TODO: Can eventually be replaced by direct access javax.swing.plaf.metal.DefaultMetalTheme.getPrimary2()
	//but take into account whether the L&F is really a Metal derivate
	public final static Color COLOR_PRIMARY_1 = new Color(102,102,153);
	public final static Color COLOR_PRIMARY_2 = new Color(153,153,204);
	public final static Color COLOR_PRIMARY_3 = new Color(204,204,255);

} //END public class DingsSwingConstants
