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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

import net.vanosten.dings.utils.RandomUtil;
import net.vanosten.dings.model.Entry;
import net.vanosten.dings.model.Entry.Result;
import net.vanosten.dings.swing.helperui.TextRectangle;
import net.vanosten.dings.swing.helperui.TextRectangle.Status;

/**
 * A Pane for drawing components for learning by choice
 */
public class LearnByChoicePane extends JPanel implements MouseMotionListener {
	private final static long serialVersionUID = 1L;
	
	/** The id of the current Entry to be learned. Not used for type.MATCHING */
	private TextRectangle currentQuestion = null;
	
	/** The panel containing the potential answer TextRectangles */
	private JPanel gridPanel = null;
		
	public enum ChoiceType {
		SET
		, MATCH
		, MULTI
	}
	private ChoiceType type = ChoiceType.SET;
	
	/** The learning direction */
	private boolean baseToTargetDirection = true;
	
	/** The number of columns for MULTI */
	private int numberOfColumns;
	
	/** The horizontal gap between TextRectangles in MATCH */
	private static int H_GAP = 100; //TODO: this should be configurable in Preferences
	/** The vertical gap between TextRectangles. The double is taken between to and from in SET and MULTI */
	private static int V_GAP = 20; //TODO: this should be configurable in Preferences
	
	/** Pointers to the TextRectangles only for MATCH */
	private TextRectangle[] questionRects = null;
	/** Pointers to the TextRectangles for answers */
	private TextRectangle[] answerRects = null;
	
	/** Indexes in questionRects and answerRects, that are solved for MATCH */
	private Map<Integer,Integer> matchedIndex;
	
	/** The learning results */
	Map<String,Result> results = null;
	
	LearnByChoiceView controller;
	
	/** The stroke for painting matched pairs */
	private final static BasicStroke MATCHED_STROKE = new BasicStroke(4.0f);
	/** The dashing for painting the ongoing matching */
    private final static float[] MATCHING_DASH = {2.0f};
	/** the stroke for painting ongoing matching */
	private final static BasicStroke MATCHING_STROKE = new BasicStroke(2.0f, 
            BasicStroke.CAP_BUTT, 
            BasicStroke.JOIN_MITER, 
            10.0f, MATCHING_DASH, 0.0f);
	
	public LearnByChoicePane(LearnByChoiceView controller) {
		this.controller = controller;
		this.setBackground(Color.WHITE);
		this.addMouseMotionListener(this);
	} //END public LearnByChoicePane()
	
	public void setType(ChoiceType type, boolean baseToTarget, int numberOfColumns) {
		this.type = type;
		this.baseToTargetDirection = baseToTarget;
		this.numberOfColumns = numberOfColumns;
	} //END public void setType(ChoiceType)

	public void nextChoices(Entry[] entries) {
		//there is no need to initialize questionStrings for MATCH and questionRects for all others
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
		
		//do initialization stuff depending on type
		switch(type) {
		case SET:
			initializeSet();
			break;
		case MATCH:
			initializeMatching();
			break;
		default: break; //do nothing
		}
		
		int rows = 0;
		int columns = 0;
		switch(type) {
		case SET:
			rows = entries.length;
			columns = 1;
			break;
		case MATCH:
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
			if (type == ChoiceType.MATCH) {
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
		if (type != ChoiceType.MATCH) {
			gbl.setConstraints(currentQuestion, gbc);
			this.add(currentQuestion);
			gbc.gridy = 1;
			gbc.insets = new Insets(2* V_GAP,0,0,0);
		}
		gbl.setConstraints(gridPanel, gbc);
		this.add(gridPanel);
		repaint();
	} //END private void initializeChoices()
	
	/**
	 * Initialize stuff related to type SET
	 */
	private void initializeSet() {
		currentQuestion = new TextRectangle(this);
		currentQuestion.setId(questionRects[0].getId());
		currentQuestion.setText(questionRects[0].getText());
		currentQuestion.changeStatus(Status.QUESTION, false);
	} //END private void initializeSet()
	
	private void initializeMatching() {
		currentQuestion = null;
		matchedIndex = new TreeMap<Integer,Integer>();
	} //END private void initializeMatching()

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);		
		//only do custom drawing if we are matching
		if (ChoiceType.MATCH == type) {
			//Get Graphics2 by copy because the graphics object
			//should have the same state when you're finished painting as it had when started.
			Graphics2D g2 = (Graphics2D)g.create(); //copy g
			
			Line2D currentLine;
			double startX, startY, endX, endY;
			
			//draw the lines for the existing matches
			if (0 != matchedIndex.size()) {
				g2.setPaint(Color.green);
				g2.setStroke(MATCHED_STROKE);
				int answerPos;
				for (int questPos : matchedIndex.keySet()) {
					answerPos = matchedIndex.get(questPos);
					startX = questionRects[questPos].getX() + questionRects[questPos].getWidth() + gridPanel.getX();
					startY = questionRects[questPos].getY() + questionRects[questPos].getHeight()/2 + gridPanel.getY();
					endX = answerRects[answerPos].getX() + gridPanel.getX();
					endY = answerRects[answerPos].getY() + answerRects[answerPos].getHeight()/2 + gridPanel.getY();
					currentLine = new Line2D.Double(startX, startY, endX, endY);
					g2.draw(currentLine);
				}
			}
						
			//draw a line if a question is chosen
			if (null != currentQuestion) {
				g2.setPaint(Color.darkGray);
				g2.setStroke(MATCHING_STROKE);
				startX = currentQuestion.getX() + currentQuestion.getWidth() + gridPanel.getX();
				startY = currentQuestion.getY() + currentQuestion.getHeight()/2 + gridPanel.getY();
				endX = getMousePosition().getX();
				endY = getMousePosition().getY();
				currentLine = new Line2D.Double(startX, startY, endX, endY);
				g2.draw(currentLine);
			}
			
			//release the copy's resources
			g2.dispose();
		}
	} //END protected void paintComponent(Graphics)
	
	/** 
	 * Checks whether the chosen/picked answer corresponds to the question for
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
		} else if (ChoiceType.MATCH == type) {
			//check whether part of questions
			boolean isQuestion = false;
			for (int i = 0; i < questionRects.length; i++) {
				if (questionRects[i] == answer) {
					isQuestion = true;
					if (null != currentQuestion) {
						currentQuestion.changeStatus(Status.OUT, false);
					}
					currentQuestion = answer;
					break;
				}
			}
			//if not part of questions, then it is an answer
			if (false == isQuestion) {
				if (null == currentQuestion) {
					answer.changeStatus(Status.IN, false);
				} else {
					//we have a new match
					if (answer.getId().equals(currentQuestion.getId())) {
						int questionIndex = getIndexPos(currentQuestion.getId(), questionRects);
						int answerIndex = getIndexPos(currentQuestion.getId(), answerRects);
						matchedIndex.put(questionIndex, answerIndex);
						//TODO: learning results
						if (matchedIndex.size() == questionRects.length) {
							controller.showNext();
						}
						currentQuestion.setSensitive(false);
						currentQuestion.changeStatus(Status.CORRECT_RESULT, false);
						answer.setSensitive(false);
						answer.changeStatus(Status.CORRECT_RESULT, false);
						repaint();
					} else { //there was no match between question and answer
						currentQuestion.setSensitive(true);
						currentQuestion.changeStatus(Status.OUT, false);
						answer.setSensitive(true);
						answer.changeStatus(Status.OUT, false);
						//TODO: learning results
					}
					currentQuestion = null;
				}
			}
		}
	} //END public void checkChosen(TextRectangle)

	/**
	 * 
	 * @param anId
	 * @param rectangles
	 * @return the index position of the TextRectangle with id = anId within the
	 *          submitted array of TextRectangles. -1 if not found
	 */
	private int getIndexPos(String anId, TextRectangle[] rectangles) {
		for (int i = 0; i < rectangles.length; i++) {
			if (rectangles[i].getId().equals(anId)) {
				return i;
			}
		}
		return -1;
	} //END private int getIndexPos(String, TextRectangle[])

	//implements MouseMotionListener
	public void mouseDragged(MouseEvent e) {
		//nothing to do	
	} //ENd public void mouseDragged(MouseEvent)

	//implements MouseMotionListener
	public void mouseMoved(MouseEvent e) {
		this.repaint();	
	} //END public void mouseMoved(MouseEvent)
} //END public class LearnByChoicePane extends JPanel implements MouseMotionListener
