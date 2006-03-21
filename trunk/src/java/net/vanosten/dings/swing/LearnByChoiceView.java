/*
 * LearnByChoiceView.java
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

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.model.EntriesCollection;
import net.vanosten.dings.utils.Toolbox;
import net.vanosten.dings.model.Entry.Result;

import net.vanosten.dings.swing.LearnByChoicePane.ChoiceType;
import net.vanosten.dings.uiif.ILearnByChoiceView;

public class LearnByChoiceView extends AViewWithButtons implements ILearnByChoiceView {
	private final static long serialVersionUID = 1L;

	private JButton startB;
	private JButton doneB;
	private JButton nextB;
	
	private EntriesCollection entries = null;
	
	//the card panels
	/** Configuration information */
	private JPanel configureP;
	/** Learning */
	private LearnByChoicePane learningPane;
	
	//keys for the card panels
	private enum Card {
		CARD_CONFIGURE
		, CARD_LEARNING
	}
	
	//components of configureP
	/** Choose the number of choices */
	JComboBox numberOfChoicesCB;
	
	/** Choose the type of choices */
	JRadioButton setRB;
	JRadioButton matchingRB;
	JRadioButton multiRB;
	JRadioButton memoryRB;
	
	/** Choose the direction of learning */
	JRadioButton baseTargetRB;
	JRadioButton targetBaseRB;
	JLabel directionL;
	
	/** Choose the pause interval in number of seconds between questions for MULTI */
	JSlider pauseIntervalS;
	JLabel pauseIntervalL;
	
	/** Choose the number of columns for MULTI */
	JComboBox numberOfColumnsCB;
	JLabel numberOfColumnsL;
	
	JSlider waitHideS;
	JLabel waitHideL;

	public LearnByChoiceView(ComponentOrientation aComponentOrientation) {
		super(Toolbox.getInstance().getLocalizedString("viewtitle.learn_by_choice"), aComponentOrientation);
		initializeGUI();
		this.setGUIOrientation();
	} //END public WelcomeView(ComponentOrientation)
	
	public void setEntriesCollection(EntriesCollection entries) {
		this.entries = entries;
	} //END public void setEntriesCollection(EntriesCollection)

	//implements AViewWithButtons
	protected void initializeMainP() {
		mainP = new JPanel();
		mainP.setLayout(new CardLayout());
		
		showConfigureP();
	} //END protected void initializeMainP()
	
	//implements AViewWithButtons
	protected final void initializeButtonP() {
		buttonsP = new JPanel();
		buttonsP.setLayout(new FlowLayout(FlowLayout.TRAILING, DingsSwingConstants.SP_H_C, 0));
		
		buttonsP.add(nextB);
		buttonsP.add(startB);
		buttonsP.add(doneB);
	} //END protected final void initializeButtonP()
	
	//implements AViewWithButtons
	protected final void initButtonComponents() {
		nextB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.next"));
		nextB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.next").charAt(0));
		nextB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				next();
			}
		});
		nextB.setVisible(false);
		startB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.start"));
		startB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.start").charAt(0));
		startB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				actionStart();
			}
		});
		
		doneB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.done"));
		doneB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.done").charAt(0));
		doneB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				showConfigureP(); //FIXME: the results should be shown
			}
		});
		doneB.setVisible(false);
	} //END protected final void initButtonComponents();
	
	private void showConfigureP() {
		if (null == configureP) {
			configureP = new JPanel();
			
			JLabel howtoL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.howto"));
			
			//pause interval for MULTI; must be defined before multiRB
			pauseIntervalS = new JSlider(JSlider.HORIZONTAL, 0,60,5);
			pauseIntervalS.setMajorTickSpacing(10);
			pauseIntervalS.setMinorTickSpacing(1);
			pauseIntervalS.setPaintLabels(true);
			pauseIntervalS.setPaintTicks(true);
			pauseIntervalL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.pauseinterval.label"));
			pauseIntervalL.setDisplayedMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.pauseinterval.mnemonic").charAt(0));
			pauseIntervalL.setLabelFor(pauseIntervalS);
			
			//wait before hide for MEMORY; must be defined before memoryRB
			waitHideS = new JSlider(JSlider.HORIZONTAL, 0,10,2);
			waitHideS.setMajorTickSpacing(5);
			waitHideS.setMinorTickSpacing(1);
			waitHideS.setPaintLabels(true);
			waitHideS.setPaintTicks(true);
			waitHideL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.waithide.label"));
			waitHideL.setDisplayedMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.waithide.mnemonic").charAt(0));
			waitHideL.setLabelFor(waitHideS);
			
			//number of rows for MULTI; must be defined before multiRB
			numberOfColumnsCB = new JComboBox();
			for (int i = 1; i <= 5; i++) {
				numberOfColumnsCB.addItem(Integer.valueOf(i));
			}
			numberOfColumnsL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.numberofcolumns.label"));
			numberOfColumnsL.setDisplayedMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.numberofcolumns.mnemonic").charAt(0));
			numberOfColumnsL.setLabelFor(numberOfColumnsCB);
			
			numberOfChoicesCB = new JComboBox();
			for (int i = 2; i <= 50; i++) {
				numberOfChoicesCB.addItem(Integer.valueOf(i));
			}
			JLabel numberOfChoicesL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.numberofchoices.label"));
			numberOfChoicesL.setDisplayedMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.numberofchoices.mnemonic").charAt(0));
			numberOfChoicesL.setLabelFor(numberOfChoicesCB);
			
			//Choose the direction of learning
			directionL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.direction.label"));
			baseTargetRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("lbcv.basetargetrb.label"));
			baseTargetRB.setMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.basetargetrb.mnemonic").charAt(0));
			targetBaseRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("lbcv.targetbaserb.label"));
			targetBaseRB.setMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.targetbaserb.mnemonic").charAt(0));
			ButtonGroup directionBG = new ButtonGroup();
			directionBG.add(baseTargetRB);
			directionBG.add(targetBaseRB);
			baseTargetRB.setSelected(true);
			
			//type of learning game
			JLabel typeL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.type.label"));
			setRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("lbcv.setrb.label"));
			setRB.setMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.setrb.mnemonic").charAt(0));
			matchingRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("lbcv.matchingrb.label"));
			matchingRB.setMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.matchingrb.mnemonic").charAt(0));
			multiRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("lbcv.multirb.label"));
			multiRB.setMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.multirb.mnemonic").charAt(0));
			multiRB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					showOptionalProperties();
				}
			});
			memoryRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("lbcv.memoryrb.label"));
			memoryRB.setMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.memoryrb.mnemonic").charAt(0));
			memoryRB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					showOptionalProperties();
				}
			});
			ButtonGroup typeOfChoiceBG = new ButtonGroup();
			typeOfChoiceBG.add(setRB);
			typeOfChoiceBG.add(matchingRB);
			typeOfChoiceBG.add(multiRB);
			typeOfChoiceBG.add(memoryRB);
			setRB.setSelected(true); this.showOptionalProperties();
			
			//make the gui
			GroupLayout layout = new GroupLayout(configureP);
			configureP.setLayout(layout);
			
			layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.LEADING, false)
				.add(howtoL)
				.add(layout.createSequentialGroup()
					.add(layout.createParallelGroup(GroupLayout.TRAILING)
						.add(typeL)
						.add(directionL)
						.add(numberOfChoicesL)
						.add(numberOfColumnsL)
						.add(pauseIntervalL)
						.add(waitHideL)
					)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(layout.createParallelGroup(GroupLayout.LEADING, false)
						.add(setRB)
						.add(matchingRB)
						.add(multiRB)
						.add(memoryRB)
						.add(baseTargetRB)
						.add(targetBaseRB)
						.add(numberOfChoicesCB)
						.add(numberOfColumnsCB)
						.add(pauseIntervalS)
						.add(waitHideS)
					)
				)
			);
			layout.linkSize(new Component[] {numberOfChoicesCB, numberOfColumnsCB}, GroupLayout.HORIZONTAL);
			
			layout.setVerticalGroup(layout.createSequentialGroup()
				.add(howtoL)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(layout.createParallelGroup(GroupLayout.BASELINE)
					.add(typeL)
					.add(setRB)
				)
				.addPreferredGap(LayoutStyle.RELATED)
				.add(matchingRB)
				.addPreferredGap(LayoutStyle.RELATED)
				.add(multiRB)
				.addPreferredGap(LayoutStyle.RELATED)
				.add(memoryRB)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(layout.createParallelGroup(GroupLayout.BASELINE)
					.add(directionL)
					.add(baseTargetRB)
				)
				.addPreferredGap(LayoutStyle.RELATED)
				.add(targetBaseRB)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(layout.createParallelGroup(GroupLayout.BASELINE)
					.add(numberOfChoicesL)
					.add(numberOfChoicesCB)
				)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(layout.createParallelGroup(GroupLayout.BASELINE)
					.add(numberOfColumnsL)
					.add(numberOfColumnsCB)
				)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(layout.createParallelGroup(GroupLayout.LEADING)
					.add(pauseIntervalL)
					.add(pauseIntervalS)
				)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(layout.createParallelGroup(GroupLayout.LEADING)
					.add(waitHideL)
					.add(waitHideS)
				)
			);
			
			//add the panel to the container
			mainP.add(configureP, Card.CARD_CONFIGURE.name());
		}
		CardLayout cl = (CardLayout)(mainP.getLayout());
		cl.show(mainP, Card.CARD_CONFIGURE.name());
		this.setViewTitle(Toolbox.getInstance().getLocalizedString("viewtitle.learn_by_choice") 
				+ Toolbox.getInstance().getLocalizedString("lbcv.viewtitle.config"));
		startB.setVisible(true);
		doneB.setVisible(false);
		nextB.setVisible(false);
	} //END private void showConfigureP()
	
	/**
	 * Shows or hides some of the optional properties
	 */
	private void showOptionalProperties() {
		boolean showNOC = multiRB.isSelected() || memoryRB.isSelected();
		boolean showPI = multiRB.isSelected();
		boolean showWH = memoryRB.isSelected();
		boolean showLD = (false == memoryRB.isSelected());
		directionL.setEnabled(showLD);
		baseTargetRB.setEnabled(showLD);
		targetBaseRB.setEnabled(showLD);
		numberOfColumnsCB.setEnabled(showNOC);
		numberOfColumnsL.setEnabled(showNOC);
		pauseIntervalS.setEnabled(showPI);
		pauseIntervalL.setEnabled(showPI);
		waitHideS.setEnabled(showWH);
		waitHideL.setEnabled(showWH);
	} //END private void showOptionalProperties()
	
	/**
	 * Shows the card with set type of learning
	 */
	private void showLearningPane() {
		if (null == learningPane) {
			learningPane = new LearnByChoicePane(this);
			mainP.add(learningPane, Card.CARD_LEARNING.name());
		}
		ChoiceType chosen;
		if (setRB.isSelected()) {
			chosen = ChoiceType.SET;
		} else if (matchingRB.isSelected()) {
			chosen = ChoiceType.MATCH;
		} else if (multiRB.isSelected()) {
			chosen = ChoiceType.MULTI;
		} else {
			chosen = ChoiceType.MEMORY;
		}
		learningPane.setType(chosen
				, baseTargetRB.isSelected()
				, ((Integer)numberOfColumnsCB.getSelectedItem()).intValue()
				, pauseIntervalS.getValue()
				, waitHideS.getValue());
		next();
		CardLayout cl = (CardLayout)(mainP.getLayout());
		cl.show(mainP, Card.CARD_LEARNING.name());
		changeViewTitleForLearning(chosen);
		startB.setVisible(false);
		doneB.setVisible(true);
		nextB.setVisible(false);
	} //private void showLearningPane()
	
	/**
	 * Changes the title of the view based on the currently selected ChoiceType for learning
	 * @param type
	 */
	private void changeViewTitleForLearning(ChoiceType type) {
		String viewTitle = null;
		switch(type) {
		case SET: viewTitle = Toolbox.getInstance().getLocalizedString("lbcv.viewtitle.set"); break;
		case MATCH: viewTitle = Toolbox.getInstance().getLocalizedString("lbcv.viewtitle.match"); break;
		case MULTI: viewTitle = Toolbox.getInstance().getLocalizedString("lbcv.viewtitle.multi"); break;
		default: viewTitle = Toolbox.getInstance().getLocalizedString("lbcv.viewtitle.memory"); break;
		}
		this.setViewTitle(Toolbox.getInstance().getLocalizedString("viewtitle.learn_by_choice") 
				+ viewTitle);
	} //END private void changeViewTitleForLearning(ChoiceType)
	
	/**
	 * Changes the displayed learning game after pressing the start button
	 * based on the chosen type
	 */
	private void actionStart() {
		//Check whether there are enough entries in the current selection.
		if (entries.hasEnoughChosenEntries(((Integer)numberOfChoicesCB.getSelectedItem()).intValue())) {
			AppEvent ape = new AppEvent(AppEvent.EventType.DATA_EVENT);
			ape.setMessage(MessageConstants.Message.D_ENTRIES_INITIALIZE_LEARNING);
			controller.handleAppEvent(ape);			
			showLearningPane();
		} else {
			Object[] options = {Toolbox.getInstance().getLocalizedString("lbcv.dialog.addentries")
					, Toolbox.getInstance().getLocalizedString("lbcv.dialog.changeselection")
					, Toolbox.getInstance().getLocalizedString("lbcv.dialog.numberofchoices")};
			JLabel messageL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.dialog.missingentries"));
			int answer = JOptionPane.showOptionDialog(this
					, messageL
					, Toolbox.getInstance().getLocalizedString("lbcv.dialog.missingentries_title")
					, JOptionPane.YES_NO_CANCEL_OPTION
					, JOptionPane.ERROR_MESSAGE
					, null
					, options
					, options[2]);
			if (JOptionPane.YES_OPTION == answer) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_ENTRIES_LIST);
				controller.handleAppEvent(ape);
			} else if (JOptionPane.NO_OPTION == answer) {
				AppEvent ape = new AppEvent(AppEvent.EventType.NAV_EVENT);
				ape.setMessage(MessageConstants.Message.N_VIEW_ENTRIES_SELECTION);
				controller.handleAppEvent(ape);
			} else { //CANCEL_OPTION
				//do nothng
			}
		}
	} //END private void actionStart()

	//implements IAppEventHandler in ILearnedByChoiceView
	public void handleAppEvent(AppEvent evt) {
	} //END public void handleAppEvent(AppEvent)
	
	/**
	 * Get the results of learning stored
	 * @param results
	 */
	protected void processLearningResults(Map<String,Result> results) {
		entries.setLearningResults(results);
	} //END protected void processLearningResults(Map<String,Result>
	
	/**
	 * Shows the next set of choices for learning
	 */
	protected void next() {
		learningPane.nextChoices(entries.getNextChoiceSet(((Integer)numberOfChoicesCB.getSelectedItem()).intValue()));
		nextB.setVisible(false);
	} //END protected void next()
	
	protected void showNext() {
		nextB.setVisible(true);
	} //END protected void showNext()
} //END public class LearnByChoiceView extends AViewWithButtons implements ILearnByChoiceView
