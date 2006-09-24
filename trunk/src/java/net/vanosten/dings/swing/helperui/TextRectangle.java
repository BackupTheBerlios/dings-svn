/*
 * TextRectangle.java
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import net.vanosten.dings.swing.LearnByChoicePane;
import net.vanosten.dings.swing.LearnByChoicePane.ChoiceType;
import net.vanosten.dings.utils.Util;

public class TextRectangle extends JLabel implements MouseListener {
	private final static long serialVersionUID = 1L;
	
	/** The parent component in charge of drawing etc. */
	private LearnByChoicePane parent;
	
	/** The id of the corresponding Entry */
	private String id = null;
	
	/** Sensitive on user interaction */
	private boolean sensitive = true;
	
	/** Used as a question? */
	private boolean question = false;
	
	/** Use syllables in display? */
	private boolean usesSyllables = false;
	
	/** Used in memory ? */
	private ChoiceType type;
	
	//the card background
	private final static Color BG_OUT = Color.yellow;
	private final static Color BG_IN = Color.pink;
	private final static Color BG_CHOSEN = Color.blue;
	private final static Color BG_CORRECT_RESULT = Color.green;
	private final static Color BG_WRONG_RESULT = Color.red;
	private final static Color BG_QUESTION = new Color(210,210,210);
	private Color[] background = {BG_OUT, BG_IN, BG_CHOSEN, BG_CORRECT_RESULT, BG_WRONG_RESULT, BG_QUESTION};
	
	//the text color for SET, MULTI and MATCH
	private final static Color FG_OUT = Color.black;
	private final static Color FG_IN = Color.black;
	private final static Color FG_CHOSEN = Color.white;
	private final static Color FG_CORRECT_RESULT = Color.white;
	private final static Color FG_WRONG_RESULT = Color.white;
	private final static Color FG_QUESTION = Color.black;
	/** True if the mouse is within this Shape */
	private Color[] foreground = {FG_OUT, FG_IN, FG_CHOSEN, FG_CORRECT_RESULT, FG_WRONG_RESULT, FG_QUESTION};
	//the text color for MEMORY
	private final static Color FG_M_OUT = BG_OUT; //must be the same as BG_OUT
	private final static Color FG_M_IN = BG_IN; //must be the same as BG_IN
	private final static Color FG_M_CHOSEN = BG_CHOSEN; //must be the same as BG_CHOSEN
	private final static Color FG_M_CORRECT_RESULT = Color.white;
	private final static Color FG_M_WRONG_RESULT = Color.white;
	private final static Color FG_M_QUESTION = Color.black;
	/** True if the mouse is within this Shape */
	private Color[] foregroundMemory = {FG_M_OUT, FG_M_IN, FG_M_CHOSEN, FG_M_CORRECT_RESULT, FG_M_WRONG_RESULT, FG_M_QUESTION};

	public enum Status {
		OUT
		, IN
		, CHOSEN
		, CORRECT_RESULT
		, WRONG_RESULT
		, QUESTION
	}
	private Status status = Status.OUT;
	
	public TextRectangle(LearnByChoicePane parent) {
		this.parent = parent;
		this.addMouseListener(this);
		this.setOpaque(true); //otherwise the background is not painted
		this.changeStatus(Status.OUT, false); //set the status and implicitely set background and foreground
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setVerticalAlignment(SwingConstants.CENTER);
		setLineBorder(Color.black, 3);
	} //END public TextRectangle(JComponent)
	
	private void setLineBorder(Color borderColor, int borderWidth) {
		Border emptyB = BorderFactory.createEmptyBorder(10,10,10,10);
		LineBorder lineB = new LineBorder(borderColor, borderWidth);
		this.setBorder(BorderFactory.createCompoundBorder(lineB, emptyB));
	} //END private void setLineBorder(Color)
	
	/**
	 * Changes the status of the component depending on mouse events.
	 * Once the status is set to <code>STATUS_CHOSEN</code> it cannot be changed
	 * by mouse events.
	 * @param newStatus
	 * @param userInteraction true if this method has been invoked by user interaction 
	 */
	public void changeStatus(Status newStatus, boolean userInteraction) {
		if (false == userInteraction) {
			status = newStatus;
			changeAppearance();
		} else if (status != newStatus && status.ordinal() < Status.CHOSEN.ordinal() && sensitive) {
			status = newStatus;
			changeAppearance();
			if (status == Status.CHOSEN) {
				parent.checkChosen(this);
			}
		}
	} //END public void changeStatus(int, boolean)
	
	/**
	 * Changes the background and foreground color.
	 * Depending on whether this is used in memory context or not a different 
	 * foreground color is used.
	 */
	private void changeAppearance() {
		this.setBackground(background[status.ordinal()]);
		if (ChoiceType.MEMORY == type) {
			if (Status.CORRECT_RESULT == status) {
				//do nothing as this gets overridden by setMemoryColorForCorrect(int)
			} else {	
				this.setForeground(foregroundMemory[status.ordinal()]);
				this.setBackground(background[status.ordinal()]);
			}
		} else {
			this.setForeground(foreground[status.ordinal()]);
			this.setBackground(background[status.ordinal()]);
		}
	} //END private void changeAppearance()
	
	/**
	 * Sets the color of the border and the text different for each
	 * pair of correct matches.
	 * @param Color 
	 */
	public void setColorForPair(Color fg) {
		this.setBackground(Color.white);
		this.setLineBorder(fg, 5);
		this.setForeground(fg);
	} //END public void setColorForPair(Color)

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		changeStatus(Status.CHOSEN, true);
	} //END public void mouseClicked(MouseEvent)

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		changeStatus(Status.IN, true);
	} //END public void mouseEntered(MouseEvent)

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		changeStatus(Status.OUT, true);
	} //END public void mouseExited(MouseEvent)

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		//do nothing
	} //END public void mousePressed(MouseEvent)

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		//do nothing
	} //END public void mouseReleased(MouseEvent)

	//implements MouseMotionListener
	public void mouseDragged(MouseEvent e) {
		//do nothing
	} //END public void mouseDragged(MouseEvent)

	//implements MouseMotionListener
	public void mouseMoved(MouseEvent e) {
		//do nothing		
	} //END public void mouseMoved(MouseEvent)

	/**
	 * Sets the text to contain html tags in order to have the text wrap etc
	 */
	@Override
	public void setText(String text) {
		if (0 == text.indexOf("<html>")) {
			super.setText(text);
		} else {
			if (usesSyllables) {
				super.setText(Util.enrichSyllablesWithColor(text));
			} else {
				super.setText("<html>" + text.trim() + "</html>");
			}
		}
	} //END public void setText(String)
	
	public void setUseSyllables(boolean usesSyllables) {
		this.usesSyllables = usesSyllables;
	}

	public String getId() {
		return id;
	} //END public String getId()

	public void setId(String id) {
		this.id = id;
	} //END public void setId(String)

	public void setSensitive(boolean sensitive) {
		this.sensitive = sensitive;
	} //ENd public void setSensitive(boolean)

	public boolean isQuestion() {
		return question;
	} //END public boolean isQuestion()

	public void setQuestion(boolean question) {
		this.question = question;
	} //END public void setQuestion(boolean)

	public void setType(ChoiceType type) {
		this.type = type;
		this.changeStatus(Status.OUT, false); //set the status and implicitely set background and foreground
	} //END public void setType(ChoiceType)

} //END public class TextRectangle extends JLabel implements MouseListener
