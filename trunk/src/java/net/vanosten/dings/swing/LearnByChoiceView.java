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
	//private JPanel resultP;
	
	/** Learning */
	private LearnByChoicePane learningPane;
	
	//keys for the card panels
	private enum Card {
		CARD_CONFIGURE
		, CARD_RESULT
		, CARD_LEARNING
	}
	
	//components of configureP
	/** Choose the number of choices */
	JComboBox numberOfChoicesCB;
	
	/** Choose the type of choices */
	JRadioButton setRB;
	JRadioButton mappingRB;
	JRadioButton multiRB;
	
	/** Choose the direction of learning */
	JRadioButton baseTargetRB;
	JRadioButton targetBaseRB;
	
	/** Choose the pause interval in number of seconds between questions for CARD_MULTI */
	JSlider pauseIntervalS;
	JLabel pauseIntervalL;
	
	/** Choose the number of columns for CARD_MULTI */
	JComboBox numberOfColumnsCB;
	JLabel numberOfColumnsL;

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
			
			//pause interval for CARD_MULTI; must be defined before multiRB
			pauseIntervalS = new JSlider(JSlider.HORIZONTAL, 0,60,5);
			pauseIntervalS.setMajorTickSpacing(10);
			pauseIntervalS.setMinorTickSpacing(1);
			pauseIntervalS.setPaintLabels(true);
			pauseIntervalS.setPaintTicks(true);
			pauseIntervalL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.pauseinterval.label"));
			pauseIntervalL.setDisplayedMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.pauseinterval.mnemonic").charAt(0));
			pauseIntervalL.setLabelFor(pauseIntervalS);
			
			//number of rows for CARD_MULTI; must be defined before multiRB
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
			
			//type of learning game
			JLabel typeL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.type.label"));
			setRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("lbcv.setrb.label"));
			setRB.setMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.setrb.mnemonic").charAt(0));
			mappingRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("lbcv.mappingrb.label"));
			mappingRB.setMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.mappingrb.mnemonic").charAt(0));
			multiRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("lbcv.multirb.label"));
			multiRB.setMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.multirb.mnemonic").charAt(0));
			multiRB.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					showMultiProperties(multiRB.isSelected());
				}
			});
			ButtonGroup typeOfChoiceBG = new ButtonGroup();
			typeOfChoiceBG.add(setRB);
			typeOfChoiceBG.add(mappingRB);
			typeOfChoiceBG.add(multiRB);
			setRB.setSelected(true); this.showMultiProperties(false);
			
			//Choose the direction of learning
			JLabel directionL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.direction.label"));
			baseTargetRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("lbcv.basetargetrb.label"));
			baseTargetRB.setMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.basetargetrb.mnemonic").charAt(0));
			targetBaseRB = new JRadioButton(Toolbox.getInstance().getLocalizedString("lbcv.targetbaserb.label"));
			targetBaseRB.setMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.targetbaserb.mnemonic").charAt(0));
			ButtonGroup directionBG = new ButtonGroup();
			directionBG.add(baseTargetRB);
			directionBG.add(targetBaseRB);
			baseTargetRB.setSelected(true);

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
						.add(pauseIntervalL)
						.add(numberOfColumnsL)
					)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(layout.createParallelGroup(GroupLayout.LEADING, false)
						.add(setRB)
						.add(mappingRB)
						.add(multiRB)
						.add(baseTargetRB)
						.add(targetBaseRB)
						.add(numberOfChoicesCB)
						.add(pauseIntervalS)
						.add(numberOfColumnsCB)
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
				.add(mappingRB)
				.addPreferredGap(LayoutStyle.RELATED)
				.add(multiRB)
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
				.add(layout.createParallelGroup(GroupLayout.LEADING)
					.add(pauseIntervalL)
					.add(pauseIntervalS)
				)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(layout.createParallelGroup(GroupLayout.BASELINE)
					.add(numberOfColumnsL)
					.add(numberOfColumnsCB)
				)
			);
			
			//add the panel to the container
			mainP.add(configureP, Card.CARD_CONFIGURE.name());
		}
		CardLayout cl = (CardLayout)(mainP.getLayout());
		cl.show(mainP, Card.CARD_CONFIGURE.name());
		startB.setVisible(true);
		doneB.setVisible(false);
		nextB.setVisible(false);
	} //END private void showConfigureP()
	
	/**
	 * Shows or hides the properties for CARD_MULTI
	 * @param doShow
	 */
	private void showMultiProperties(boolean doShow) {
		pauseIntervalS.setEnabled(doShow);
		pauseIntervalL.setEnabled(doShow);
		numberOfColumnsCB.setEnabled(doShow);
		numberOfColumnsL.setEnabled(doShow);
	} //END private void showMultiProperties(boolean)
	
	/**
	 * Shows the card with set type of learning
	 */
	private void showLearningPane() {
		if (null == learningPane) {
			learningPane = new LearnByChoicePane(this);
			if (setRB.isSelected()) {
				learningPane.setType(ChoiceType.SET
						, baseTargetRB.isSelected()
						, ((Integer)numberOfColumnsCB.getSelectedItem()).intValue());
			} else if (mappingRB.isSelected()) {
				learningPane.setType(ChoiceType.MAPPING
						, baseTargetRB.isSelected()
						, ((Integer)numberOfColumnsCB.getSelectedItem()).intValue());
				} else {
				learningPane.setType(ChoiceType.MULTI
						, baseTargetRB.isSelected()
						, ((Integer)numberOfColumnsCB.getSelectedItem()).intValue());
			}
			mainP.add(learningPane, Card.CARD_LEARNING.name());
		}
		next();
		CardLayout cl = (CardLayout)(mainP.getLayout());
		cl.show(mainP, Card.CARD_LEARNING.name());
		startB.setVisible(false);
		doneB.setVisible(true);
		nextB.setVisible(false);
	} //private void showLearningPane()
	
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
