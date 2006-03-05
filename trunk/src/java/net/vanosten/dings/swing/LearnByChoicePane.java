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
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import net.vanosten.dings.utils.RandomUtil;
import net.vanosten.dings.model.Entry;
import net.vanosten.dings.model.Entry.Result;
import net.vanosten.dings.swing.helperui.TextRectangle;
import net.vanosten.dings.swing.helperui.TextRectangle.Status;

/**
 * A Pane for drawing components for learning by choice
 */
public class LearnByChoicePane extends JPanel {
	private final static long serialVersionUID = 1L;
	
	/** The id of the current Entry to be learned. Not used for type.MAPPING */
	private TextRectangle currentQuestion = null;
	
	private TextRectangle currentlyMapped = null;
	
	/** The panel containing the potential answer TextRectangles */
	private JPanel gridPanel = null;
		
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
	private static int V_GAP = 20; //TODO: this should be configurable in Preferences
	
	/** Pointers to the TextRectangles only for Mapping */
	TextRectangle[] questionRects = null;
	/** Pointers to the TextRectangles for answers */
	TextRectangle[] answerRects = null;
	
	/** The learning results */
	Map<String,Result> results = null;
	
	LearnByChoiceView controller;
	
	public LearnByChoicePane(LearnByChoiceView controller) {
		this.controller = controller;
		this.setBackground(Color.WHITE);
	} //END public LearnByChoicePane()
	
	public void setType(ChoiceType type, boolean baseToTarget, int numberOfColumns) {
		this.type = type;
		this.baseToTargetDirection = baseToTarget;
		this.numberOfColumns = numberOfColumns;
	} //END public void setType(ChoiceType)

	public void nextChoices(Entry[] entries) {
		//there is no need to initialize questionStrings for MAPPING and questionRects for all others
		//but it makes the code easier to read and this is not a big waste
		//of resources
		answerRects = new TextRectangle[entries.length];
		questionRects = new TextRectangle[entries.length];
		
		TextRectangle rectAnswer, rectQuestion;

		int[] randomPositions = RandomUtil.getRandomInts(entries.length);
		for (int i = 0; i < entries.length; i++)  {
			rectAnswer = new TextRectangle(this);
			rectQuestion = new TextRectangle(this);
			if (baseToTargetDirection) {
				rectAnswer.setText(entries[i].getTarget());
				rectQuestion.setText(entries[i].getBase());
			} else {
				rectAnswer.setText(entries[i].getBase());
				rectQuestion.setText(entries[i].getTarget());
			}
			rectAnswer.setId(entries[i].getId());
			rectQuestion.setId(entries[i].getId());
			answerRects[randomPositions[i]] = rectAnswer;
			questionRects[i] = rectQuestion;
		}
		
		currentQuestion = new TextRectangle(this);
		currentQuestion.setId(questionRects[0].getId());
		currentQuestion.setText(questionRects[0].getText());
		currentQuestion.changeStatus(Status.QUESTION, false);
		
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
		//The overall layout
		this.removeAll();
		
		gridPanel = new JPanel();
		gridPanel.setOpaque(false);
		GridLayout grid = new GridLayout(rows,columns, H_GAP, V_GAP);
		gridPanel.setLayout(grid);
		for (int i = 0; i < entries.length; i++) {
			if (type == ChoiceType.MAPPING) {
				gridPanel.add(questionRects[i]);
			}
			gridPanel.add(answerRects[i]);
		}
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(gbl);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		if (type != ChoiceType.MAPPING) {
			gbl.setConstraints(currentQuestion, gbc);
			this.add(currentQuestion);
			gbc.gridy = 1;
			gbc.insets = new Insets(2* V_GAP,0,0,0);
		}
		gbl.setConstraints(gridPanel, gbc);
		this.add(gridPanel);
		repaint();
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
			int myX = currentlyMapped.getX() + currentlyMapped.getWidth() + gridPanel.getX();
			int myY = currentlyMapped.getY() + currentlyMapped.getHeight()/2 + gridPanel.getY();
			g2.draw(new Line2D.Double(myX, myY, getMousePosition().getX(), getMousePosition().getY()));
			
			//release the copy's resources
			g2.dispose();
		}
	} //END protected void paintComponent(Graphics)
	
	/** 
	 * checks whether the chosen/picked answer corresponds to the question for
	 * SET and MULTI
	 * @param answer
	 */
	public void checkChosen(TextRectangle answer) {
		if (null == results) {
			results = new HashMap<String,Result>();
		}
		if (ChoiceType.SET == type) {
			for (int i = 0; i < answerRects.length; i++) {
				answerRects[i].setSensitive(false);
			}
			if (answer.getId().equals(currentQuestion.getId())) {
				results.put(answer.getId(), Result.SUCCESS);
				controller.processLearningResults(results);
				results = null;
				controller.next();
			} else {
				results.put(answer.getId(), Result.WRONG);
				results.put(currentQuestion.getId(), Result.WRONG);
				controller.processLearningResults(results);
				results = null;
				answer.changeStatus(Status.WRONG_RESULT, false);
				for (int i = 0; i < answerRects.length; i++) {
					if (answerRects[i].getId().equals(currentQuestion.getId())) {
						answerRects[i].changeStatus(Status.CORRECT_RESULT, false);
						break;
					}
				}
				controller.showNext();
			}
		}
	} //ENd public void checkChosen(TextRectangle)
	
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
