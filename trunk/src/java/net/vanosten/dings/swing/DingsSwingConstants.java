/*
 * DingsSwingConstants.java
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
package net.vanosten.dings.swing;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import net.vanosten.dings.consts.Constants;

public class DingsSwingConstants {

	private static Logger logger = Logger.getLogger("net.vanosten.dings.swing.DingsSwingConstants");

	/** Set private in order to prevent instantiations */
	private DingsSwingConstants() {
		//nothing to initialize
	} //ENDprivate DingsSwingConstants()

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

	/*------------------- Colors ------------------------------*/
	//The primary color two of the Metal L&F.
	//TODO: Can eventually be replaced by direct access javax.swing.plaf.metal.DefaultMetalTheme.getPrimary2()
	//but take into account whether the L&F is really a Metal derivate
	public final static Color COLOR_PRIMARY_1 = new Color(102,102,153);
	public final static Color COLOR_PRIMARY_2 = new Color(153,153,204);
	public final static Color COLOR_PRIMARY_3 = new Color(204,204,255);

	//The colors for background to indicate valid / invalid input
	public final static Color VALID_INPUT = new Color(255,255,153);
	public final static Color INVALID_INPUT = new Color(255,153,153);

	/*-------------------------FONTS------------------------------------------------*/

	protected static Font TITLE_ONE_FONT;

	protected static Font TITLE_TWO_FONT;

	protected static Font DEFAULT_FONT;

	public static Font STATUS_BAR_FONT;

	/** The font used to show hideable text in entries (e.g. text for example */
	public static Font SOLUTION_FONT;

	/**
	 * Sets the static Fonts (e.g. TITLE_ONE_FONT) relative to the default font size.
	 *
	 * @param Font defaultFont - the default font size of the application
	 */
	public static void setFontSizes(Font defaultFont) {
		DEFAULT_FONT = defaultFont;
		String fontName = defaultFont.getName();
		int fontSize = defaultFont.getSize();
		TITLE_ONE_FONT = new Font(fontName, Font.BOLD, (int)(fontSize * 1.5));
		TITLE_TWO_FONT = new Font(fontName, Font.BOLD, (int)(fontSize * 1.2));
		STATUS_BAR_FONT = new Font(fontName, Font.PLAIN, fontSize);
		SOLUTION_FONT = new Font(fontName, Font.ITALIC, fontSize);
	} //END public static setFontSizes(Font)

	/*------------------- Get images ------------------------------*/

	//home grown
	public final static String IMG_DINGS_32 = "dings32.png";

	//gtk2.4 stock images
	//menu items
	public final static String IMG_CLOSE_MI = "fileclose.png"; //in actions16
	public final static String IMG_EXIT_MI = "exit.png"; //in actions16
	public final static String IMG_HELP_MI = "help_index.png"; //in apps16
	public final static String IMG_PREFERENCES_MI = "configure.png"; //in actions16
	public final static String IMG_OPEN_MI = "fileopen.png"; //in actions16
	public final static String IMG_SAVE_MI = "filesave.png"; //in actions16
	public final static String IMG_SAVE_AS_MI = "filesaveas.png"; //in actions16
	public final static String IMG_NEW_MI = "filenew.png"; //in actions16
	public final static String IMG_INFO_MI = "hwinfo.png"; //in apps16
	public final static String IMG_STATS_MI = "kchart.png"; //in apps16
	public final static String IMG_EMPTY_MI = "empty_16x16.png"; //in apps16
	//dialog
	public final static String IMG_INFO_DLG = "messagebox_info.png"; //in actions48
	public final static String IMG_ERROR_DLG = "messagebox_critical.png"; //in actions48
	public final static String IMG_QUESTION_DLG = "help.png"; //in actions48
	public final static String IMG_WARNING_DLG = "messagebox_warning.png"; //in actions48
	//buttons
	public final static String IMG_EMPTY_BTN = "empty_22x1.png"; //in actions22
	public final static String IMG_APPLY_BTN = "apply.png"; //in actions22
	public final static String IMG_ADD_BTN = "edit_add.png"; //in actions22
	public final static String IMG_REMOVE_BTN = "edit_remove.png"; //in actions22
	public final static String IMG_DELETE_BTN = "editdelete.png"; //in actions22;
	public final static String IMG_RESET_BTN = "undo.png"; //in actions22
	public final static String IMG_OPEN_BTN = "fileopen22.png"; //in actions22
	public final static String IMG_NEW_BTN = "filenew22.png"; //in actions22
	public final static String IMG_CLOSE_BTN = "fileclose22.png"; //in actions22
	public final static String IMG_EDIT_BTN = "edit.png"; //in actions22
	public final static String IMG_BACK_BTN = "back.png"; //in actions22
	public final static String IMG_REDRAW_BTN = "reload.png"; //in actions22
	public final static String IMG_UP_BTN = "up.png"; //in actions22
	public final static String IMG_DOWN_BTN = "down.png"; //in actions22
	public final static String IMG_KNOWN_BTN = "ledgreen.png"; //in actions22
	public final static String IMG_UNKNOWN_BTN = "ledred.png"; //in actions22
	public final static String IMG_HINT_BTN = "ktip.png"; //in apps22
	public final static String IMG_RESULT_BTN = "kview.png"; //in apps22


	/**
	 * Gets an ImageIcon by providing the resource path and a description.
	 *
	 * @param String path - the relative resource file path for the image
	 * @param String description - allows assistive technologies to help a visually impaired
	 * user understand what information the icon conveys
	 */
	public static ImageIcon createImageIcon(String path, String description) {
		String fullPath = "images/" + path;
		URL imgURL = Constants.class.getClassLoader().getResource(fullPath);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		}
		else {
			if (logger.isLoggable(Level.FINEST)) {
				logger.logp(Level.FINEST, Constants.class.getName(), "createImageIcon", "Could not load image: " + fullPath);
			}
			return null;
		}
	} //END public static ImageIcon createImageIcon(String, String)

	/**
	 * Gets an ImcageIcon for a message dialog by providing the message type
	 * @param aMessageType
	 * @return
	 */
	public static ImageIcon getIconForMessageType(int aMessageType) {
		if (Constants.QUESTION_MESSAGE == aMessageType) {
			return createImageIcon(IMG_QUESTION_DLG, "FIXME");
		} else if (Constants.WARNING_MESSAGE == aMessageType) {
			return createImageIcon(IMG_WARNING_DLG, "FIXME");
		} else if (Constants.ERROR_MESSAGE == aMessageType) {
			return createImageIcon(IMG_ERROR_DLG, "FIXME");
		} else {
			//must be INFORMATION_MESSAGE
			return createImageIcon(IMG_QUESTION_DLG, "FIXME");
		}
	} //END public static ImageIcon getIconForMessageType(int)

} //END public class DingsSwingConstants
