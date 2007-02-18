/*
 * HintPanel.java
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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import net.vanosten.dings.model.Preferences;
import net.vanosten.dings.swing.DingsSwingConstants;
import net.vanosten.dings.utils.Toolbox;
import net.vanosten.dings.utils.Util;

/**
 * Shows part of a text to give a hint about the contents to help learning.
 * And shows the associated controls (button and dropdown)
 * Previously class <code>HintLabel</code> and before <code>MultilineTextRenderer</code>.
 */
public class HintPanel extends JPanel implements HintObservable {
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
	
	/** The label displaying the hint */
	private JLabel hintL;
	
	/** Which hint mode should be used */
	private ChoiceID modeCh;
	
	/** When pressed shows a hint */
	private JButton hintB;

	/** The objects observing for hints */
	private List<HintObserver> observers = new ArrayList<HintObserver>();
	
	private boolean usesSyllables = false;
	
	/**
	 * Empty constructor. All other constructors of parent class
	 * JLabel are hidden.
	 */
	public HintPanel(boolean usesSyllables) {
		super();
		this.setHintTextColors();
		this.usesSyllables = usesSyllables;
		initComponents();
	} //END public HintLabel()
	
	private void initComponents() {
		hintL = new JLabel("");
		
		String[][] modeItems = {
				{Integer.toString(HintPanel.MODE_FLASH), Toolbox.getInstance().getLocalizedString("hp.label.mode_flash")}
				,{Integer.toString(HintPanel.MODE_LETTER), Toolbox.getInstance().getLocalizedString("hp.label.mode_letter")}
				,{Integer.toString(HintPanel.MODE_SHUFFLE), Toolbox.getInstance().getLocalizedString("hp.label.mode_shuffle")}
		};
		modeCh = new ChoiceID();
		modeCh.setItems(modeItems);
		modeCh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (HintPanel.MODE_LETTER == Integer.parseInt(modeCh.getSelectedID())) {
					shownLetters = 0;
				}
			}
		});
		hintB = new JButton(Toolbox.getInstance().getLocalizedString("hp.label.hint_button")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_HINT_BTN, ""));
		hintB.setMnemonic(Toolbox.getInstance().getLocalizedString("hp.mnemonic.hint_button").charAt(0));
		hintB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doHint();
			}
		});
		//put onto panel
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);
		
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbl.setConstraints(hintL, gbc);
		this.add(hintL);
		
		gbc.gridx = 1;
		gbc.weightx = 0.0;
		gbc.insets = new Insets(0,DingsSwingConstants.SP_H_G,0,0);
		gbc.fill = GridBagConstraints.NONE;
		gbl.setConstraints(hintB, gbc);
		this.add(hintB);
		
		gbc.gridx = 2;
		gbl.setConstraints(modeCh, gbc);
		this.add(modeCh);
	}

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
	 */
	private void doFlash() {
		//create timer
		Timer timer = new Timer(Toolbox.getInstance().getPreferencesPointer().getIntProperty(Preferences.LEARN_HINT_FLASH_TIME)
				, new ActionListener() {
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
	private void doShuffle() {
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
	 * @return True if all letters of the text have been shown
	 */
	private boolean doLetters() {
		shownLetters++;
		if (shownLetters < originalText.length()) {
			setShownText(originalText.substring(0, shownLetters), true, false);
			return false;
		}
		doResult();
		return true;
	} //END public boolean doLetters(int)

	/**
	 * Resets the shown letters to none
	 */
	public void reset() {
		shownLetters = 0;
		setShownText(originalText, false, false);
		hintB.setEnabled(true);
	} //END public void resetLetters()

	/**
	 * Shows the text as a result.
	 */
	public void doResult() {
		setHintTextColors();
		setShownText(originalText, true, true);
		hintB.setEnabled(false);
	} //END public void doResult()

	/**
	 * Sets the text to be displayed as an html-string
	 * @param aText - the visible text
	 * @param isVisible True if the text should be visible
	 * @param isResult True if the text is to be shown as a result
	 */
	private void setShownText(String aText, boolean isVisible, boolean isResult) {
		if (isResult) {
			hintL.setForeground(resultColor);
		} else if (isVisible) {
			hintL.setForeground(hintColor);
		} else {
			hintL.setForeground(getBackground());
		}
		//only if the text should be visible and the target uses syllables
		if (isVisible && usesSyllables) {
			hintL.setText(Util.enrichSyllablesWithColor(aText));
		} else {
			hintL.setText("<html>" + aText + "</html>");
		}
	} //END private void setShownText(String, boolean, boolean)

	/**
	 * Displays a hint in the target field.
	 */
	private void doHint() {
		setHintTextColors();
		try {
			int helperMode = Integer.parseInt(modeCh.getSelectedID());
			boolean allShown = false;
			switch(helperMode) {
				case HintPanel.MODE_FLASH:
					doFlash();
					break;
				case HintPanel.MODE_LETTER:
					allShown = doLetters();
					break;
				case HintPanel.MODE_SHUFFLE:
					doShuffle();
					break;
			}
			notifyHintObservers(allShown);
		}
		catch (NumberFormatException e) {
			//TODO: this should never happen, but ...
		}
	} //END private void doHint()
		
	/**
	 * Sets the colors for the hint text according to preferences.
	 * This is done repeately to cope with changes in preferences without restart of learning.
	 */
	private void setHintTextColors() {
		try {
			hintColor = new Color(Toolbox.getInstance().getPreferencesPointer().getIntProperty(Preferences.PROP_COLOR_HINT));
			resultColor = new Color(Toolbox.getInstance().getPreferencesPointer().getIntProperty(Preferences.PROP_COLOR_RESULT));
		}
		catch(NumberFormatException e) {
			hintColor = Color.BLUE;
			resultColor = Color.RED;
		}
	} //END private void setHintTextColors()

	//implements HintObservable
	public void notifyHintObservers(boolean allShown) {
		for (HintObserver observer : observers) {
			observer.hintOccured(allShown);
		}
	} //END public void notifyHintObservers(boolean)

	//implements HintObservable
	public void registerHintObserver(HintObserver observer) {
		observers.add(observer);	
	} //END public void registerHintObserver(HintObserver)
} //END public class HintLabel extends JLabel
