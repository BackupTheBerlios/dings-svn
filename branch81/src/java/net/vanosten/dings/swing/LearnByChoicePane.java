/*
 * LearnByChoicePane.java
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
package net.vanosten.dings.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.Timer;

import net.vanosten.dings.utils.RandomUtil;
import net.vanosten.dings.utils.Toolbox;
import net.vanosten.dings.model.Entry;
import net.vanosten.dings.model.Entry.Result;
import net.vanosten.dings.swing.helperui.TextRectangle;
import net.vanosten.dings.swing.helperui.TextRectangle.Status;

/**
 * A Pane for drawing components for learning by choice
 */
public class LearnByChoicePane extends JPanel implements MouseMotionListener {
	private final static long serialVersionUID = 1L;
	
	/** The TextRectangle for the current Entry to be learned. Not used for type.MATCHING */
	private TextRectangle currentQuestion = null;
	/** The TextRectangle currently chosen for matching in type.MEMORY */
	private TextRectangle currentAnswer = null;
	
	/** The panel containing the potential answer TextRectangles */
	private JPanel gridPanel = null;
		
	public enum ChoiceType {
		SET
		, MATCH
		, MULTI
		, MEMORY
	}
	private ChoiceType type = ChoiceType.SET;
	
	/** The number of columns for MULTI */
	private int numberOfColumns;
	
	/** The number of seconds to wait before displaying next question in MULTI */
	private int pauseInterval;
	
	/** The number of seconds to wait before hiding wrong matches for MEMORY */
	private int waitHide;
	
	/** The horizontal gap between TextRectangles in MATCH */
	private static int H_GAP = 100; //TODO: this should be configurable in Preferences
	/** The vertical gap between TextRectangles. The double is taken between to and from in SET and MULTI */
	private static int V_GAP = 20; //TODO: this should be configurable in Preferences
	
	/** Pointers to the TextRectangles only for MATCH */
	private TextRectangle[] questionRects = null;
	/** Pointers to the TextRectangles for answers (and questions for MEMORY) */
	private TextRectangle[] answerRects = null;
	
	/** Indexes in questionRects and answerRects, that are solved for MATCH */
	private Map<Integer,Integer> matchedIndex;
	
	/** Timer for displaying next question after some time in MULTI and resets for MEMORY */
	private Timer myTimer;
	
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
	
	/** The line and text colors for showing pairs of matches for MATCH and MEMORY */
	private final static int[][] COLORS = {
		{0,0,128} //blå (OpenOffice 2.0 standard palette)
		, {0,128,0} //grøn
		, {0,128,128} //turkis
		, {128,0,0} //rød
		, {128,0,128} //magentarød
		, {128,128,0} //brun
		, {128,128,128} //grå
		, {192,192,192} //lysegrå
		, {0,0,255} //lyseblå
		, {0,255,0} //lysegrøn
		, {0,255,255} //lys turkis
		, {255,0,0} //lyserød
		, {255,0,255} //lys magentarød
		, {255,255,0} //gul
		, {0,184,255} //blå 7
		, {0,174,0} //grøn 5
		, {71,184,184} //turkis 4
		, {255,102,51} //orange 2
		, {148,71,148} //magentarød 3
		, {255,255,101} //gul 3
		, {128,76,25} //brun 3
		, {255,153,102} //orange 3
		, {102,102,153} //sun 2
	};

	/**
	 * Constructor
	 * @param controller
	 */
	public LearnByChoicePane(LearnByChoiceView controller) {
		this.controller = controller;
		this.setBackground(Color.WHITE);
		this.addMouseMotionListener(this);
	} //END public LearnByChoicePane()
	
	public void setType(ChoiceType type, int numberOfColumns, int pauseInterval, int waitHide) {
		this.type = type;
		this.numberOfColumns = numberOfColumns;
		this.pauseInterval = pauseInterval;
		this.waitHide = waitHide;
	} //END public void setType(...)

	public void nextChoices(Entry[] entries) {
		//there is no need to initialize questionStrings for MATCH and questionRects for all others
		//but it makes the code easier to read and this is not a big waste
		//of resources
		TextRectangle rectAnswer, rectQuestion;
		if (ChoiceType.MEMORY == type) {
			answerRects = new TextRectangle[entries.length * 2];
		} else {
			answerRects = new TextRectangle[entries.length];
			questionRects = new TextRectangle[entries.length];
		}
		int[] randomPositions = RandomUtil.getRandomInts(answerRects.length);
		
		for (int i = 0; i < entries.length; i++)  {
			rectAnswer = new TextRectangle(this);
			rectQuestion = new TextRectangle(this);
			rectAnswer.setQuestion(false);
			rectQuestion.setQuestion(true);
			if (false == Toolbox.getInstance().isTargetAsked()) {
				rectAnswer.setUseSyllables(Toolbox.getInstance().getInfoPointer().isTargetUsesSyllables());
				rectAnswer.setText(entries[i].getTarget());
				rectQuestion.setUseSyllables(Toolbox.getInstance().getInfoPointer().isBaseUsesSyllables());
				rectQuestion.setText(entries[i].getBase());
			} else {
				rectAnswer.setUseSyllables(Toolbox.getInstance().getInfoPointer().isBaseUsesSyllables());
				rectAnswer.setText(entries[i].getBase());
				rectQuestion.setUseSyllables(Toolbox.getInstance().getInfoPointer().isTargetUsesSyllables());
				rectQuestion.setText(entries[i].getTarget());
			}
			rectAnswer.setId(entries[i].getId());
			rectQuestion.setId(entries[i].getId());
			rectAnswer.setType(type);
			rectQuestion.setType(type);
			if (ChoiceType.MEMORY == type) {
				answerRects[randomPositions[i*2]] = rectAnswer;
				answerRects[randomPositions[i*2+1]] = rectQuestion;
			} else {
				answerRects[randomPositions[i]] = rectAnswer;
				questionRects[i] = rectQuestion;
			}
		}
		
		//do initialization stuff depending on type
		int rows = 0;
		int columns = 0;
		switch(type) {
		case SET:
			initializeSet();
			rows = entries.length;
			columns = 1;
			break;
		case MATCH:
			initializeMatching();
			rows = entries.length;
			columns = 2;
			break;
		case MULTI:
			initializeMulti();
			columns = numberOfColumns;
			rows = 0; //for GridLayout(int) 0 means as many as needed. Math.ceil(entries.length / columns);
			break;
		case MEMORY:
			initializeMemory();
			columns = numberOfColumns;
			rows = 0; //for GridLayout(int) 0 means as many as needed. Math.ceil(entries.length / columns);
		default: break; //do nothing
		}
		
		//The overall layout
		this.removeAll();
		
		gridPanel = new JPanel();
		gridPanel.setOpaque(false);
		GridLayout grid = new GridLayout(rows,columns, H_GAP, V_GAP);
		gridPanel.setLayout(grid);
		for (int i = 0; i < answerRects.length; i++) {
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
		if (ChoiceType.SET == type || ChoiceType.MULTI == type) {
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
	 * Initialize stuff related to type SET and MULTI
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
	
	private void initializeMulti() {
		myTimer = new Timer(pauseInterval * 1000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setNextQuestionForMultiTimer();
			}
		});
		myTimer.setRepeats(false);
		currentQuestion = new TextRectangle(this);
		currentQuestion.changeStatus(Status.QUESTION, false);
		setNextQuestionForMulti(-1);
	} //END private void initializeMulti()
	
	private void initializeMemory() {
		currentQuestion = null;
		myTimer = new Timer(waitHide * 1000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				resetCurrentMemoryChoice();
			}
		});
		myTimer.setRepeats(false);
	} //END private void initializeMemory()

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
				g2.setStroke(MATCHED_STROKE);
				int answerPos;
				for (int questPos : matchedIndex.keySet()) {
					g2.setPaint(questionRects[questPos].getForeground());
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
			checkChosenForSet(answer);
		} else if (ChoiceType.MATCH == type) {
			checkChosenForMatch(answer);
		} else if (ChoiceType.MULTI == type) {
			if (myTimer != null) {
				myTimer.stop();
			}
			checkChosenForMulti(answer);
		} else if (ChoiceType.MEMORY == type) {
			checkChosenForMemory(answer);
		}
	} //END public void checkChosen(TextRectangle)
	
	private void checkChosenForSet(TextRectangle answer) {
		for (int i = 0; i < answerRects.length; i++) {
			answerRects[i].setSensitive(false);
		}
		if (answer.getId().equals(currentQuestion.getId())) {
			results.put(answer.getId(), Result.SUCCESS);
			getNextQuestions();
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
	} //END private void checkChosenForSet(TextRectangle)
	
	private void checkChosenForMatch(TextRectangle answer) {
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
					//learning results
					if (matchedIndex.size() == questionRects.length) {
						//do nothing as the matching was obvious
					} else if (false == results.containsKey(currentQuestion.getId())) {
						results.put(currentQuestion.getId(), Result.SUCCESS);
					} else {//the result was wrong but now correct so we use HELPED
						results.put(currentQuestion.getId(), Result.HELPED);
					}
					//prepare painting
					if (matchedIndex.size() == questionRects.length) {
						getNextQuestions();
					}
					Color myColor = getColorForPosition(matchedIndex.size());
					currentQuestion.setSensitive(false);
					currentQuestion.changeStatus(Status.CORRECT_RESULT, false);
					currentQuestion.setColorForPair(myColor);
					answer.setSensitive(false);
					answer.changeStatus(Status.CORRECT_RESULT, false);
					answer.setColorForPair(myColor);
					repaint();
				} else { //there was no match between question and answer
					currentQuestion.setSensitive(true);
					currentQuestion.changeStatus(Status.OUT, false);
					answer.setSensitive(true);
					answer.changeStatus(Status.OUT, false);
					//learning results
					results.put(currentQuestion.getId(), Result.WRONG);
					results.put(answer.getId(), Result.WRONG);
				}
				currentQuestion = null;
			}
		}
	} //END private void checkChosenForMatch(TextRectangle)
	
	private void checkChosenForMulti(TextRectangle answer) {
		int questPos = getIndexPos(currentQuestion.getId(), questionRects);
		if (answer.getId().equals(currentQuestion.getId())) {
			questionRects[questPos] = null;
			answer.setSensitive(false);
			answer.changeStatus(Status.CORRECT_RESULT, false);
			int notNulls = countNotNull(questionRects);
			if (0 == notNulls) {
				results.put(answer.getId(), Result.HELPED);
				getNextQuestions();
			} else if (false == results.containsKey(currentQuestion.getId())) {
				results.put(answer.getId(), Result.SUCCESS);
			} else {
				results.put(answer.getId(), Result.HELPED);
			}
		} else {
			results.put(answer.getId(), Result.WRONG);
			results.put(currentQuestion.getId(), Result.WRONG);
			answer.changeStatus(Status.OUT, false);
		}
		setNextQuestionForMulti(questPos);
	} //END private void checkChosenForMulti(TextRectangle)
	
	private void checkChosenForMemory(TextRectangle answer) {
		if (null != currentAnswer) {
			//the matching was wrong and the user started to pick new cards
			//before the timer has resetted the currently selected
			if (myTimer.isRunning()) {
				myTimer.stop();
			}
			resetCurrentMemoryChoice();
		}
		if (null == currentQuestion) {
			currentQuestion = answer;
			currentQuestion.setSensitive(false);
			currentQuestion.changeStatus(Status.QUESTION, false);
		} else {
			currentAnswer = answer;
			if (currentQuestion.getId().equals(answer.getId())) {
				//set them to right answer
				currentQuestion.changeStatus(Status.CORRECT_RESULT, false);
				currentAnswer.changeStatus(Status.CORRECT_RESULT, false);
				currentAnswer.setSensitive(false);
				results.put(currentQuestion.getId(), Result.HELPED); //MEMORY does not influence score
				Color myColor = getColorForPosition(results.size());
				currentQuestion.setColorForPair(myColor);
				currentAnswer.setColorForPair(myColor);
				currentQuestion = null;
				currentAnswer = null;
				if (results.size() == (answerRects.length / 2)) {
					getNextQuestions();
				}
			} else {
				//set them to wrong
				currentQuestion.changeStatus(Status.WRONG_RESULT, false);
				currentAnswer.changeStatus(Status.WRONG_RESULT, false);
				currentQuestion.setSensitive(false);
				currentAnswer.setSensitive(false);
				//wait a bit
				myTimer.start();
			}
		}
	} //END private void checkChosenForMemory(TextRectangle)
	
	private synchronized void resetCurrentMemoryChoice() {
		if (null != currentQuestion) {
			currentQuestion.changeStatus(Status.OUT, false);
			currentQuestion.setSensitive(true);
			currentQuestion = null;
		}
		if (null != currentAnswer) {
			currentAnswer.changeStatus(Status.OUT, false);
			currentAnswer.setSensitive(true);
			currentAnswer = null;
		}
	} //END private void resetCurrentMemoryChoice()
	
	/**
	 * Sets the next question to be displayed for MULTI. I.e. the next 
	 * question within the same set of questions != getNextQuetions()
	 * @param questPos the position of the actual question in questionRects.
	 *         If -1 then get position from the currentQuestion.getId()
	 */
	private void setNextQuestionForMulti(int questPos) {
		boolean found = false;
		if (questPos < (questionRects.length -1)) {
			for (int i = questPos + 1; i < questionRects.length; i++) {
				if (null != questionRects[i]) {
					currentQuestion.setId(questionRects[i].getId());
					currentQuestion.setText(questionRects[i].getText());
					found = true;
					break;
				}
			}
		}
		if (false == found) {
			for (int i = 0; i < questPos; i++) {
				if (null != questionRects[i]) {
					currentQuestion.setId(questionRects[i].getId());
					currentQuestion.setText(questionRects[i].getText());
					break;
				}
			}
		}
		//wait and hide
		myTimer.start();
	} //END private void setNextQuestionForMulti(int)
	
	/**
	 * Convenience method because myTimer.actionPerformed has not
	 * access to currentQuestion
	 */
	private void setNextQuestionForMultiTimer() {
		setNextQuestionForMulti(getIndexPos(currentQuestion.getId(), questionRects));
	} //END private void setNextQuestionForMultiTimer()

	/**
	 * 
	 * @param anId
	 * @param rectangles
	 * @return the index position of the TextRectangle with id = anId within the
	 *          submitted array of TextRectangles. -1 if not found
	 */
	private static int getIndexPos(String anId, TextRectangle[] rectangles) {
		for (int i = 0; i < rectangles.length; i++) {
			if (null != rectangles[i] && rectangles[i].getId().equals(anId)) {
				return i;
			}
		}
		return -1;
	} //END private static int getIndexPos(String, TextRectangle[])
	
	/**
	 * 
	 * @param rectangles
	 * @return The number of array elements, which are not null
	 */
	private static int countNotNull(TextRectangle[] rectangles) {
		int counter = 0;
		for (int i = 0; i < rectangles.length; i++) {
			if (null != rectangles[i]) {
				counter++;
			}
		}
		return counter;
	} //END private static int countNotNull(TextRectangle[])

	//implements MouseMotionListener
	public void mouseDragged(MouseEvent e) {
		//nothing to do	
	} //ENd public void mouseDragged(MouseEvent)

	//implements MouseMotionListener
	public void mouseMoved(MouseEvent e) {
		this.repaint();	
	} //END public void mouseMoved(MouseEvent)
	
	/**
	 * Prepares to show the next questions after
	 * sending learning results to processing.
	 */
	private void getNextQuestions() {
		controller.processLearningResults(results);
		results = null;
		if (ChoiceType.MATCH == type || ChoiceType.MEMORY == type) {
			controller.showNext();
		} else {
			controller.next();
		}
	} //END private void getNextQuestions()
	
	/** 
	 * 
	 * @param position
	 * @return A Color based anon the Colors in <code>COLORS</code> array. If the position is
	 *         higher then the number of available colors then the Color at position 0 is used.
	 */
	private Color getColorForPosition(int position) {
		int colorArrayPos = position -1;
		if (position > COLORS.length) {
			colorArrayPos = 0;
		}
		return new Color(COLORS[colorArrayPos][0],COLORS[colorArrayPos][1],COLORS[colorArrayPos][2]);
	} //END private Color getColorForPosition(int
} //END public class LearnByChoicePane extends JPanel implements MouseMotionListener
