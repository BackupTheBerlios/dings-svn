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
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.vanosten.dings.swing.LearnByChoicePane;

public class TextRectangle extends JLabel implements MouseListener, MouseMotionListener {
	private final static long serialVersionUID = 1L;
	
	/** The parent component in charge of drawing etc. */
	private LearnByChoicePane parent;
	
	//the id of the corresponding Entry
	private String id = null;
	
	//TODO: these should be configurable in Preferences
	private static int MIN_WIDTH = 50;
	private static int MIN_HEIGHT = 50;

	private final static Color BACKGROUND_OUT = Color.green;
	private final static Color BACKGROUND_IN = Color.pink;
	private final static Color BACKGROUND_CHOSEN = Color.blue;
	private final static Color BACKGROUND_WRONG = Color.red;
	private Color[] background = {BACKGROUND_OUT, BACKGROUND_IN, BACKGROUND_CHOSEN, BACKGROUND_WRONG};
	
	private final static Color FOREGROUND_OUT = Color.black;
	private final static Color FOREGROUND_IN = Color.black;
	private final static Color FOREGROUND_CHOSEN = Color.white;
	private final static Color FOREGROUND_WRONG = Color.white;
	/** True if the mouse is within this Shape */
	private Color[] foreground = {FOREGROUND_OUT, FOREGROUND_IN, FOREGROUND_CHOSEN, FOREGROUND_WRONG};

	public enum Status {
		OUT
		, IN
		, CHOSEN
		, WRONG
	}
	private Status status = Status.OUT;
	
	public TextRectangle(LearnByChoicePane parent) {
		this.parent = parent;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setOpaque(true); //otherwise the background is not painted
		this.changeStatus(Status.OUT, false); //set the status and implicitely set background and foreground
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setVerticalAlignment(SwingConstants.CENTER);
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
		} else if (status != Status.CHOSEN && status != Status.WRONG && status != newStatus) {
			status = newStatus;
			this.setBackground(background[status.ordinal()]);
			this.setForeground(foreground[status.ordinal()]);
			if (status == Status.CHOSEN) {
				parent.setChosen(this);
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
		parent.paintMapping(this);
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
		super.setText("<html>" + text + "</html>");
	} //END public void setText(String)

	public String getId() {
		return id;
	} //END public String getId()

	public void setId(String id) {
		this.id = id;
	} //END public void setId(String)

	/**
	 * To have an area which is easy to hit with a mouse and at least
	 * as big as the minimum of super
	 */
	@Override
	public Dimension getMinimumSize() {
		Dimension superDim = super.getMinimumSize();
		return new Dimension(Math.max(superDim.width, MIN_WIDTH), Math.max(superDim.width, MIN_HEIGHT));
	} //END public Dimension getMinimumSize()
} //END public class TextRectangle extends JLabel implements MouseListener, MouseMotionListener
