/*
 * MulitlineTextRenderer.java
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

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Component;
import java.awt.font.LineBreakMeasurer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Collections;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class MultilineTextRenderer extends JPanel {
	private final static int MIN_HEIGHT = 10;

	//view modes
	public final static int MODE_READ = 1;
	public final static int MODE_FLASH = 2;
	public final static int MODE_COVER = 3;
	public final static int MODE_LETTER = 4;
	public final static int MODE_RESULT = 5;
	public final static int MODE_HIDDEN = 6;
	public final static int MODE_SHUFFLE = 7;

	/** The current view mode */
	private int viewMode = MODE_READ;

	/** The letters to be shown in view mode MODE_LETTER */
	private int shownLetters = 0;

	/** Indicates whether the text is visible or not in MODE_FLASH, MODE_READ and MODE_RESULT */
	private boolean textVisible = true;

	/** The percentage of text that should be shown in vertical direction in MODE_COVER */
	private int coveragePercentage = 0;

	/** The minimal text to be displayed to prevent an error with lineMeasurer */
	private final static String MIN_TEXT = " ";

	/** The text to be shown as an AttributedString */
	private transient AttributedString text;

	/** The length of the String */
	private int textLength = MIN_TEXT.length();
	
	/** The real text */
	private String originalText = null;

	/** The font to use */
	private Font textFont;

	/** The place of the border for shading */
	private final static int BORDER_SIZE = 6;   //2 left or above, 2 right of text or below, 2 right or bottom

	public MultilineTextRenderer (Font aTextFont) {
		//help rendering
		setOpaque(true);
		//initialize a minimal lineBreakMeasurerObject
		text = new AttributedString(MIN_TEXT);
		this.textFont = aTextFont;
		text.addAttribute(TextAttribute.FONT, textFont);
	} //END public MultilineTextRenderer

	public void setViewMode(int aViewMode) {
		textVisible = false;
		viewMode = aViewMode;
		if (MODE_LETTER == viewMode) {
			shownLetters = 0;
		}
		else if (MODE_READ == viewMode || MODE_RESULT == viewMode) {
			textVisible = true;
		}
		doValidation();
	} //END public void setViewMode(int)

	/**
	 * Setter for the text to be displayed.
	 *
	 * @param String aText - the text to be displayed
	 */
	public void setText(String aText) {
		if (null == aText) {
			originalText = MIN_TEXT;
		}
		else if(aText.equals("")) {
			originalText = MIN_TEXT;
		}
		originalText = aText;
		textLength = originalText.length();
		text = new AttributedString(originalText);
		text.addAttribute(TextAttribute.FONT, textFont);
		//repaint
		doValidation(); //TODO: is this needed?
	} //END public void setText(String)
	
	private void setAttributedText(String aText) {
		textLength = aText.length();
		text = new AttributedString(aText);
		text.addAttribute(TextAttribute.FONT, textFont);
		//repaint
		doValidation(); //TODO: is this needed?		
	} //END private void setAttributedText(String aText)

	/**
	 * Flashes the text for a short period of time.
	 *
	 * @param flashTime - the amount of time in milliseconds that the text is visible
	 */
	public void doFlash(int flashTime) {
		setAttributedText(originalText);
		viewMode = MODE_FLASH;
		//create timer
		Timer timer = new Timer(flashTime, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				textVisible = false;
				doValidation();
			}
		});
		timer.setRepeats(false);
		//show
		textVisible = true;
		doValidation();
		//wait and hide
		timer.start();
	} //END void doFlash(int)

	/**
	 * Shows the text with only a portion of the letters visible
	 */
	public void doLetters(int theShownLetters) {
		setAttributedText(originalText);
		viewMode = MODE_LETTER;
		textVisible = true;
		shownLetters = theShownLetters;
		doValidation();
	} //END public void doLetters(int)

	/**
	 * Show the specified amount of text in vertical direction.
	 *
	 * @param int aPercentage - the amount of coverage to be relieved
	 */
	public void doCover(int aPercentage) {
		setAttributedText(originalText);
		viewMode = MODE_COVER;
		textVisible = true;
		this.coveragePercentage = aPercentage;
		doValidation();
	} //END public void doCover(int)

	/**
	 * Show the text with shuffled letters (shuffeled within the single words.
	 *
	 * @param int aPercentage - the amount of coverage to be relieved
	 */
	public void doShuffle() {
		StringBuffer shuffeledSB = new StringBuffer();		
		StringTokenizer st = new StringTokenizer(originalText);
		String thisWord = null;
		ArrayList shuffeler = new ArrayList();
		while (st.hasMoreTokens()) {
			thisWord = st.nextToken();
			//if word is not longer than one letter then no need to shuffle
			if (thisWord.length() <= 1) {
				shuffeledSB.append(thisWord);
			}
			else { //word needs to be shuffled
				//remove all previous elements
				shuffeler.clear();
				for (int i = 0; i < thisWord.length(); i++) {
					shuffeler.add(thisWord.substring(i, i + 1));
				}
				//shuffle
				Collections.shuffle(shuffeler);
				//append to shuffled string
				for (int i = 0; i < shuffeler.size(); i++) {
					shuffeledSB.append(shuffeler.get(i));
				}				
			}
			//add a space if needed
			if (st.hasMoreTokens()) {
				shuffeledSB.append(" ");
			}
		}
		
		//set the view
		setAttributedText(shuffeledSB.toString());
		viewMode = MODE_SHUFFLE;
		textVisible = true;
		doValidation();
	} //END public void doShuffle()

	/**
	 * Paints the component
	 *
	 * @Graphics g
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//get a Graphics2 environment
		Graphics2D graphics2D = (Graphics2D) g;

		Dimension mySize = this.getSize();
		float formatWidth = mySize.width;
		float drawPosY = 0;

		if (textVisible) {

			//text color
			if (MODE_RESULT == viewMode) text.addAttribute(TextAttribute.FOREGROUND, Color.red);
			else if (MODE_LETTER == viewMode) {
				text.addAttribute(TextAttribute.FOREGROUND, Color.black, 0, shownLetters);
				text.addAttribute(TextAttribute.FOREGROUND, this.getBackground(), shownLetters, textLength);
			}
			else text.addAttribute(TextAttribute.FOREGROUND, Color.black);

			//prepare text
			AttributedCharacterIterator paragraph = text.getIterator();
			int textStart = paragraph.getBeginIndex();
			int textEnd = paragraph.getEndIndex();
			LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph,
								new FontRenderContext(null, false, false));

			// Set formatting width to width of Component.
			lineMeasurer.setPosition(textStart);

			drawPosY = 2.0f;	//due to border

			// Get lines from lineMeasurer until the entire
			// paragraph has been displayed.
			while (lineMeasurer.getPosition() < textEnd) {

				// Retrieve next layout.
				TextLayout layout = lineMeasurer.nextLayout(formatWidth);
				// Move y-coordinate by the ascent of the layout.
				drawPosY += layout.getAscent();

				// Compute pen x position.  If the paragraph is
				// right-to-left, we want to align the TextLayouts
				// to the right edge of the panel.
				float drawPosX;
				if (layout.isLeftToRight()) {
					drawPosX = 2;  //due to border
				}
				else {
					drawPosX = formatWidth - layout.getAdvance() -2;
				}

				// Draw the TextLayout at (drawPosX, drawPosY).
				layout.draw(graphics2D, drawPosX, drawPosY);

				//Draw the cover if viewMode == MODE_COVER
				if (MODE_COVER == viewMode) {
					graphics2D.setPaint(getBackground());
					float rectHeight = (layout.getAscent() + layout.getDescent()) * coveragePercentage/100;
					graphics2D.fill(new Rectangle2D.Float(2, drawPosY + layout.getDescent() - rectHeight, formatWidth -4, rectHeight));
				}
				// Move y-coordinate in preparation for next layout.
				drawPosY += layout.getDescent() + layout.getLeading();
			} //END while
		} //END if (textVisible)
	} //END public void paintComponent(Graphics)

	/**
	 * Calculates the preferred size based on the width of super and
	 * the height needed to display the inforamtion.
	 * Ovverrides super.getPreferredSize().
	 *
	 * @returns Dimension - the preferred size.
	 */
	public Dimension getPreferredSize() {
		Component myParent = this.getParent();
		Dimension superSize;
		int myWidth = 0;
		while (myParent != null) {
			superSize = myParent.getSize();
			myWidth = superSize.width;
			myParent = myParent.getParent();
		}
		myWidth -= 50;  //a hack to make it working
		int myHeight  = MIN_HEIGHT;
		myHeight = calculateHeight(myWidth);
		return new Dimension(myWidth, myHeight);
	} //END public void getPreferredSize()

	/**
	 * Overrides super.getMinimumSize() and calls getPreferredSize()
	 *
	 * @returns Dimension - the minimal size
	 */
	public Dimension getMinimumSize() {
		return getPreferredSize();
	} //END public void getMinimumSize()

	/**
	 * Overrides super.getMaximumSize() and calls getPreferredSize()
	 *
	 * @returns Dimension - the minimal size
	 */
	public Dimension getMaximumSize() {
		return getPreferredSize();
	} //END public void getMaximumSize()

	/**
	 * Calculates the height needed to dreaw this component.
	 * This method is a short form of the one in paintComponent()
	 *
	 * @param float formatWidth - the width available to write the text
	 * @return int height - the needed height to draw the text
	 */
	private int calculateHeight(float formatWidth) {
		float drawPosY = 0;
		AttributedCharacterIterator paragraph = text.getIterator();
		int textStart = paragraph.getBeginIndex();
		int textEnd = paragraph.getEndIndex();
		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph,
								new FontRenderContext(null, false, false));
		lineMeasurer.setPosition(textStart);
		while (lineMeasurer.getPosition() < textEnd) {
			TextLayout layout = lineMeasurer.nextLayout(formatWidth - BORDER_SIZE);
			drawPosY += layout.getAscent() + layout.getDescent() + layout.getLeading();
		}
		drawPosY += BORDER_SIZE;
		return (int)drawPosY;
	} //END private int calculateHeight(float)

	/**
	 * Forces this component to be shown correctly.
	 * Currently just calls repaint.
	 */
	private void doValidation() {
		this.invalidate();
		Component foo = this.getParent();
		if (null != foo) { //this prevents a NullPointerException when the panel is not drawn yet
			foo.invalidate();
			foo.validate();
			foo.repaint();
		}
	} //private void doValidation()

	public int getTextLength() {
		return textLength;
	} //END public int getTextLength()

} //END public class MultilineTextRenderer extends JPanel
