/*
 * LearnByChoicePane.java
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
package net.vanosten.dings.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.vanosten.dings.consts.RandomUtil;
import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.model.Entry;
import net.vanosten.dings.swing.helperui.TextRectangle;
import net.vanosten.dings.swing.helperui.TextRectangle.Status;

/**
 * A Pane for drawing components for learning by choice
 */
public class LearnByChoicePane extends JPanel {
	private final static long serialVersionUID = 1L;
	
	/** The id of the current Entry to be learned. Not used for type.MAPPING */
	private String currentId = null;
	
	private TextRectangle currentlyMapped = null;
		
	public enum ChoiceType {
		SET
		, MAPPING
		, MULTI
	}
	private ChoiceType type = ChoiceType.SET;
	
	/** The learning direction */
	private boolean baseToTargetDirection = true;
	
	/** The number of columns for MULTI */
	private int numberOfColumns;
	
	/** The horizontal gap between TextRectangles in MAPPING */
	private static int H_GAP = 100; //TODO: this should be configurable in Preferences
	/** The vertical gap between TextRectangles. The double is taken between to and from in SET and MULTI */
	private static int V_GAP = 50; //TODO: this should be configurable in Preferences
	
	/** Pointers to the TextRectangles only for Mapping */
	TextRectangle[] fromRects = null;
	/** Array of "questions" */
	String[] questionStrings = null;
	/** Pointers to the TextRectangles for all */
	TextRectangle[] toRects = null;
	
	IAppEventHandler controller;
	
	public LearnByChoicePane(IAppEventHandler controller) {
		this.controller = controller;
		this.setBackground(Color.WHITE);
	} //END public LearnByChoicePane()
	
	public void setType(ChoiceType type, boolean baseToTarget, int numberOfColumns) {
		this.type = type;
		this.baseToTargetDirection = baseToTarget;
		this.numberOfColumns = numberOfColumns;
	} //END public void setType(ChoiceType)

	public void nextChoices(Entry[] entries) {
		//there is no need to initialize questionStrings for MAPPING and fromRects for all others
		//but it makes the code easier to read and this is not a big waste
		//of resources
		toRects = new TextRectangle[entries.length];
		fromRects = new TextRectangle[entries.length];
		questionStrings = new String[entries.length];
		
		TextRectangle rectTo, rectFrom;

		int[] randomPositions = RandomUtil.getRandomInts(entries.length);
		for (int i = 0; i < entries.length; i++)  {
			rectTo = new TextRectangle(this);
			rectFrom = new TextRectangle(this);
			if (baseToTargetDirection) {
				rectTo.setText(entries[i].getTarget());
				rectFrom.setText(entries[i].getBase());
				questionStrings[i] = entries[i].getBase();
			} else {
				rectTo.setText(entries[i].getBase());
				rectFrom.setText(entries[i].getTarget());
				questionStrings[i] = entries[i].getTarget();
			}
			rectTo.setId(entries[i].getId());
			rectFrom.setId(entries[i].getId());
			toRects[randomPositions[i]] = rectTo;
			if (type == ChoiceType.MAPPING) {
				fromRects[i] = rectFrom;
			}
		}
		
		currentId = entries[0].getId();
		
		int rows = 0;
		int columns = 0;
		switch(type) {
		case SET:
			rows = entries.length;
			columns = 1;
			break;
		case MAPPING:
			rows = entries.length;
			columns = 2;
			break;
		default: //MULTI
			columns = numberOfColumns;
			rows = 0; //for GridLayout(int) 0 menas as many as needed. Math.ceil(entries.length / columns);
			break;
		}
		JPanel gridPanel = new JPanel();
		gridPanel.setOpaque(false);
		GridLayout grid = new GridLayout(rows,columns, H_GAP, V_GAP);
		gridPanel.setLayout(grid);
		for (int i = 0; i < entries.length; i++) {
			if (type == ChoiceType.MAPPING) {
				gridPanel.add(fromRects[i]);
			}
			gridPanel.add(toRects[i]);
		}
		//The overall layout
		this.removeAll();
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		if (type != ChoiceType.MAPPING) {
			JLabel fromL = new JLabel(questionStrings[0]);
			gbl.setConstraints(fromL, gbc);
			this.add(fromL);
			gbc.gridy = 1;
			gbc.insets = new Insets(2* V_GAP,0,0,0);
		}
		gbl.setConstraints(gridPanel, gbc);
		this.add(gridPanel);
	} //END private void initializeChoices()

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//only do custom drawing if we are mapping
		if (ChoiceType.MAPPING == type && null != currentlyMapped) {
			//get the actual width and height of the drawable area
			//which is the component minus its border
			//Insets insets = getInsets();
			//int currentWidth = getWidth() - insets.left - insets.right;
			//int currentHeight = getHeight() - insets.top - insets.bottom;
			
			//Get Graphics2 by copy because the graphics object
			//should have the same state when you're finished painting as it had when started.
			Graphics2D g2 = (Graphics2D)g.create(); //copy g
			
			g2.setPaint(Color.darkGray);
			Rectangle r = currentlyMapped.getBounds();
			g2.draw(new Line2D.Double(r.x + r.width, r.y + r.getCenterY()
					, getMousePosition().getX(), getMousePosition().getY()));
			
			//release the copy's resources
			g2.dispose();
		}
	} //END protected void paintComponent(Graphics)
	
	public void setChosen(TextRectangle rect) {
		if (ChoiceType.SET == type) {
			if (currentId != null && rect.getId().equals(currentId)) {
				//FIXME: display next choice
			} else {
				rect.changeStatus(Status.WRONG, false);
			}
		}
	} //ENd public void setChosen(TextRectangle)
	
	/**
	 * Tells the pane to paint ongoing mapping between a base and a target
	 * @param rect
	 */
	public void paintMapping(TextRectangle rect) {
		currentlyMapped = rect;
		repaint();
	} //END public void paintMapping(...)
	
	public void checkMapping(TextRectangle rect) {
		currentlyMapped = null;
		repaint();
	} //END public void checkMapping(TextRectangle)
	
} //END public class LearnByChoicePane extends JComponent
