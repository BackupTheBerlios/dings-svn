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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.Map;

import javax.swing.JPanel;

import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.model.Entry;
import net.vanosten.dings.swing.helperui.TextRectangle;
import net.vanosten.dings.swing.helperui.TextRectangle.Status;

/**
 * A Pane for drawing components for learning by choice
 */
public class LearnByChoicePane extends JPanel {
	private final static long serialVersionUID = 1L;
	
	/** A map of all current choices on the left side for mapping or all for the rest */
	//Map<String, TextRectangle> leftChoices = null;
	
	/** A map of all current choices on the right side for ChoiceType.MAPPING. Not used for others */
	//Map<String, TextRectangle> rightChoices = null;
	
	/** The id of the current Entry to be learned. Not used for type.MAPPING */
	private String currentId = null;
		
	public enum ChoiceType {
		SET
		, MAPPING
		, MULTI
	}
	private ChoiceType type = ChoiceType.SET;
	
	IAppEventHandler controller;
	
	public LearnByChoicePane(IAppEventHandler controller) {
		this.controller = controller;
		this.setBackground(Color.WHITE);
		
		//no layout manager as laying out components is done with absolute coordinates
		this.setLayout(null);
		initializeChoices(null);
	} //END public LearnByChoicePane()
	
	public void setType(ChoiceType type) {
		this.type = type;
	} //END public void setType(ChoiceType)

	public void initializeChoices(Entry[] entries) {
		TextRectangle foo = new TextRectangle(this);
		foo.setText("this is Foo :-");
		foo.setId("1");
		TextRectangle goo = new TextRectangle(this);
		goo.setText("this is another text for you from Goo");
		goo.setId("2");
		
		currentId = foo.getId();
		
		Dimension size = foo.getPreferredSize();
		foo.setBounds(100, 200, size.width, size.height);
		add(foo);
		size = goo.getPreferredSize();
		goo.setBounds(100, 400, size.width, size.height);
		add(goo);
	} //END private void initializeChoices()

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//only do custom drawing if we are mapping
		if (ChoiceType.MAPPING == type) {
			//get the actual width and height of the drawable area
			//which is the component minus its border
			//Insets insets = getInsets();
			//int currentWidth = getWidth() - insets.left - insets.right;
			//int currentHeight = getHeight() - insets.top - insets.bottom;
			
			//Get Graphics2 by copy because the graphics object
			//should have the same state when you're finished painting as it had when started.
			Graphics2D g2 = (Graphics2D)g.create(); //copy g
			
			//the real stuff of painting the TextRectangles
			
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
	}
} //END public class LearnByChoicePane extends JComponent
