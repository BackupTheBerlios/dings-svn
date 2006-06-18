/*
 * HintLabel.java
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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Shows part of a text to give a hint about the contents to help learning.
 * Previously class <code>MultilineTextRenderer</code>.
 */
public class HintLabel extends JLabel {
	private final static long serialVersionUID = 1L;

	public final static int MODE_FLASH = 0;
	public final static int MODE_LETTER = 1;
	public final static int MODE_SHUFFLE = 2;
	/** The letters to be shown in view mode MODE_LETTER */
	private int shownLetters = 0;

	/** The real text */
	private String originalText = null;

	/** The hint text color */
	private Color hintColor;

	/** The color to show a result */
	private Color resultColor;

	/**
	 * Empty constructor. All other constructors of parent class
	 * JLabel are hidden.
	 *
	 * @param defaultColor The color to be used to draw the text
	 * @param resultColor The color to be used if the hint should appear like a result
	 */
	public HintLabel(Color aHintColor, Color aResultColor) {
		super();
		setTextColors(aHintColor, aResultColor);
	} //END public HintLabel(Color, Color)

	/**
	 * Sets the text color.
	 * @param aHintColor The color for showing hint texts
	 * @param aResultColor The color for displaying results
	 */
	public void setTextColors(Color aHintColor, Color aResultColor) {
		hintColor = aHintColor;
		resultColor = aResultColor;
	} //END public void setTextColors(Color, Color)

	/**
	 * Setter for the text to be displayed.
	 *
	 * @param String aText - the text to be displayed
	 */
	public void setHintText(String aText) {
		if (null == aText || 0 == aText.trim().length()) {
			originalText = "";
		}
		originalText = aText;
	} //END public void setHintText(String)

	/**
	 * Flashes the text for a short period of time.
	 *
	 * @param flashTime - the amount of time in milliseconds that the text is visible
	 */
	public void doFlash(int flashTime) {
		//create timer
		Timer timer = new Timer(flashTime, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setShownText(originalText, false, false);
			}
		});
		timer.setRepeats(false);
		//show
		setShownText(originalText, true, false);
		//wait and hide
		timer.start();
	} //END void doFlash(int)

	/**
	 * Show the text with shuffled letters (shuffeled within the single words.
	 *
	 * @param int aPercentage - the amount of coverage to be relieved
	 */
	public void doShuffle() {
		StringBuffer shuffeledSB = new StringBuffer();
		String[] theTokens = originalText.split("\\s+");
		List<String> shuffeler = new ArrayList<String>();
		for (int i = 0; i < theTokens.length; i++) {
			//if word is not longer than one letter then no need to shuffle
			if (theTokens[i].length() <= 1) {
				shuffeledSB.append(theTokens[i]);
			}
			else { //word needs to be shuffled
				//remove all previous elements
				shuffeler.clear();
				for (int j = 0; j < theTokens[i].length(); j++) {
					shuffeler.add(theTokens[i].substring(j, j + 1));
				}
				//shuffle
				Collections.shuffle(shuffeler);
				//append to shuffled string
				for (int j = 0; j < shuffeler.size(); j++) {
					shuffeledSB.append(shuffeler.get(j));
				}
			}
			//add a space if needed
			if (i < (theTokens.length -1)) {
				shuffeledSB.append(" ");
			}
		}

		//set the view
		setShownText(shuffeledSB.toString(), true, false);
	} //END public void doShuffle()

	/**
	 * Shows the text with only a portion of the letters visible.
	 *
	 * @return True if not all letters of the text have been shown
	 */
	public boolean doLetters() {
		shownLetters++;
		if (shownLetters < originalText.length()) {
			setShownText(originalText.substring(0, shownLetters), true, false);
			return true;
		}
		doResult();
		return false;
	} //END public boolean doLetters(int)

	/**
	 * Resets the shown letters to none
	 */
	public void resetLetters() {
		shownLetters = 0;
	} //END public void resetLetters()

	/**
	 * Shows the text as a result.
	 */
	public void doResult() {
		setShownText(originalText, true, true);
	} //END public void doResult()

	/**
	 * Hides the text
	 */
	public void doHide() {
		setShownText(originalText, false, false);
	} //END public void doHide()

	/**
	 * Sets the text to be displayed as an html-string
	 * @param aText - the visible text
	 * @param isVisible True if the text should be visible
	 * @param isResult True if the text is to be shown as a result
	 */
	private void setShownText(String aText, boolean isVisible, boolean isResult) {
		if (isResult) {
			setForeground(resultColor);
		} else if (isVisible) {
			setForeground(hintColor);
		} else {
			setForeground(getBackground());
		}
		setText("<html>" + aText + "</html>");
	} //END private void setShownText(String, boolean, boolean)
} //END public class HintLabel extends JLabel
