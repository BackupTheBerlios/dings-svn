/*
 * TextRectangle.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2006 Rick Gruber-Riemer (rick@vanosten.net)
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

import net.vanosten.dings.swing.LearnByChoicePane;

public class TextRectangle extends JLabel implements MouseListener {
	private final static long serialVersionUID = 1L;
	
	/** The parent component in charge of drawing etc. */
	private LearnByChoicePane parent;
	
	//the id of the corresponding Entry
	private String id = null;
	
	/** Sensitive on user interaction */
	private boolean sensitive = true;
	
	private final static Color BG_OUT = Color.yellow;
	private final static Color BG_IN = Color.pink;
	private final static Color BG_CHOSEN = Color.blue;
	private final static Color BG_CORRECT_RESULT = Color.green;
	private final static Color BG_WRONG_RESULT = Color.red;
	private final static Color BG_QUESTION = Color.white;
	private Color[] background = {BG_OUT, BG_IN, BG_CHOSEN, BG_CORRECT_RESULT, BG_WRONG_RESULT, BG_QUESTION};
	
	private final static Color FG_OUT = Color.black;
	private final static Color FG_IN = Color.black;
	private final static Color FG_CHOSEN = Color.white;
	private final static Color FG_CORRECT_RESULT = Color.white;
	private final static Color FG_WRONG_RESULT = Color.white;
	private final static Color FG_QUESTION = Color.black;
	/** True if the mouse is within this Shape */
	private Color[] foreground = {FG_OUT, FG_IN, FG_CHOSEN, FG_CORRECT_RESULT, FG_WRONG_RESULT, FG_QUESTION};

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
		Border emptyB = BorderFactory.createEmptyBorder(10,10,10,10);
		Border lineB = BorderFactory.createLineBorder(Color.black, 3);
		this.setBorder(BorderFactory.createCompoundBorder(lineB, emptyB));
	} //END public TextRectangle(JComponent)
	
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
			this.setBackground(background[status.ordinal()]);
			this.setForeground(foreground[status.ordinal()]);
		} else if (status != newStatus && status.ordinal() < Status.CHOSEN.ordinal() && sensitive) {
			status = newStatus;
			this.setBackground(background[status.ordinal()]);
			this.setForeground(foreground[status.ordinal()]);
			if (status == Status.CHOSEN) {
				parent.checkChosen(this);
			}
		}
	} //END public void changeStatus(int, boolean)

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
		if (0 == text.trim().indexOf("<html>")) {
			super.setText(text.trim());
		} else {
			super.setText("<html>" + text.trim() + "</html>");
		}
	} //END public void setText(String)

	public String getId() {
		return id;
	} //END public String getId()

	public void setId(String id) {
		this.id = id;
	} //END public void setId(String)

	public void setSensitive(boolean sensitive) {
		this.sensitive = sensitive;
	} //ENd public void setSensitive(boolean)
} //END public class TextRectangle extends JLabel implements MouseListener
