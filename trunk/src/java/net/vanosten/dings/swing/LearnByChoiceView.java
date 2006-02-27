/*
 * WelcomeView.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2002, 03, 04, 2005 Rick Gruber-Riemer (rick@vanosten.net)
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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.model.Entry;
import net.vanosten.dings.model.Toolbox;

import net.vanosten.dings.swing.LearnByChoicePane.ChoiceType;
import net.vanosten.dings.uiif.ILearnByChoiceView;

public class LearnByChoiceView extends AViewWithButtons implements ILearnByChoiceView {
	private final static long serialVersionUID = 1L;

	private JButton startB;
	private JButton doneB;
	
	//the card panels
	/** Configuration information */
	private JPanel configureP;
	//private JPanel resultP;
	
	private LearnByChoicePane choicePane;
	/** Choose the right solution among a set of choices */
	//private JPanel setP;
	/** Map a set of terms with a set of definitions */
	//private JPanel mapP;
	//private JPanel multiP;
	//keys for the card panels
	private enum Card {
		CARD_CONFIGURE
		, CARD_RESULT
		, CARD_SET
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
	
	/** Choose the number of rows for CARD_MULTI */
	JComboBox numberOfRowsCB;
	JLabel numberOfRowsL;

	public LearnByChoiceView(ComponentOrientation aComponentOrientation) {
		super(Toolbox.getInstance().getLocalizedString("viewtitle.learn_by_choice"), aComponentOrientation);
		initializeGUI();
		this.setGUIOrientation();
	} //END public WelcomeView(ComponentOrientation)
	
	//implements AViewWithButtons
	protected void initializeMainP() {
		mainP = new JPanel();
		mainP.setLayout(new CardLayout());
		
		showConfigureP();
	} //END protected void initializeMainP()
	
	//implements AViewWithButtons
	protected final void initializeButtonP() {
		buttonsP = new JPanel();
		buttonsP.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		buttonsP.setLayout(new GridLayout(1,2, DingsSwingConstants.SP_H_C, 0));

		
		buttonsP.add(startB);
		buttonsP.add(doneB);
	} //END protected final void initializeButtonP()
	
	//implements AViewWithButtons
	protected final void initButtonComponents() {
		startB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.start")
				//FIXME, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_NEW_BTN, "FIXME")
				);
		startB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.start").charAt(0));
		startB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				actionStart();
			}
		});
		
		doneB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.done")
				//, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_NEW_BTN, "FIXME")
				);
		doneB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.done").charAt(0));
		doneB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
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
			numberOfRowsCB = new JComboBox();
			for (int i = 1; i <= 5; i++) {
				numberOfRowsCB.addItem(Integer.valueOf(i));
			}
			numberOfRowsL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.numberofrows.label"));
			numberOfRowsL.setDisplayedMnemonic(Toolbox.getInstance().getLocalizedString("lbcv.numberofrows.mnemonic").charAt(0));
			numberOfRowsL.setLabelFor(numberOfRowsCB);
			
			numberOfChoicesCB = new JComboBox();
			for (int i = 2; i <= 20; i++) {
				numberOfChoicesCB.addItem(Integer.valueOf(i));
			}
			JLabel numberOfChoicesL = new JLabel(Toolbox.getInstance().getLocalizedString("lbcv.numberofchoices.label"));
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
						.add(numberOfRowsL)
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
						.add(numberOfRowsCB)
					)
				)
			);
			layout.linkSize(new Component[] {numberOfChoicesCB, numberOfRowsCB}, GroupLayout.HORIZONTAL);
			
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
				.add(layout.createParallelGroup(GroupLayout.CENTER)
					.add(pauseIntervalL)
					.add(pauseIntervalS)
				)
				.addPreferredGap(LayoutStyle.UNRELATED)
				.add(layout.createParallelGroup(GroupLayout.BASELINE)
					.add(numberOfRowsL)
					.add(numberOfRowsCB)
				)
			);
			
			//add the panel to the container
			mainP.add(configureP, Card.CARD_CONFIGURE.name());
		}
		CardLayout cl = (CardLayout)(mainP.getLayout());
		cl.show(mainP, Card.CARD_CONFIGURE.name());
	} //END private void showConfigureP()
	
	/**
	 * Shows or hides the properties for CARD_MULTI
	 * @param doShow
	 */
	private void showMultiProperties(boolean doShow) {
		pauseIntervalS.setEnabled(doShow);
		pauseIntervalL.setEnabled(doShow);
		numberOfRowsCB.setEnabled(doShow);
		numberOfRowsL.setEnabled(doShow);
	} //END private void showMultiProperties(boolean)
	
	/**
	 * Shows the card with set type of learning
	 */
	private void showChoicePane() {
		if (null == choicePane) {
			choicePane = new LearnByChoicePane(this);
			if (setRB.isSelected()) {
				choicePane.setType(ChoiceType.SET);
			} else if (mappingRB.isSelected()) {
				choicePane.setType(ChoiceType.MAPPING);
			} else {
				choicePane.setType(ChoiceType.MULTI);
			}
			mainP.add(choicePane, Card.CARD_SET.name());
		}
		
		CardLayout cl = (CardLayout)(mainP.getLayout());
		cl.show(mainP, Card.CARD_SET.name());
	} //private void showChoicePane()
	
	/**
	 * Changes the displayed learning game after pressing the start button
	 * based on the chosen type
	 */
	private void actionStart() {
		//FIXME: check whether there are enough entries in the current selection.
		//if not, display a Warning telling either to change selection, change
		//the number of displayed choices or add new entries first.
		//Use buttons in the dialog to access the options directly
		showChoicePane();
		startB.setVisible(false);
		doneB.setVisible(true);
	} //END private void actionStart()

	public void handleAppEvent(AppEvent evt) {
	} //END public void handleAppEvent(AppEvent)

	public void setChoiceSet(Entry[] theEntries) {
		
	}
} //END public class LearnByChoiceView extends AViewWithButtons implements ILearnbyChoiceView
