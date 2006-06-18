/*
 * EntriesSelectionView.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * DingsBums?! A flexible flashcard application written in Java.
 * Copyright (C) 2002, 03, 04, 05, 2006 Rick Gruber-Riemer (dingsbums@vanosten.net)
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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.model.Entry;
import net.vanosten.dings.utils.Toolbox;
import net.vanosten.dings.swing.helperui.ListID;
import net.vanosten.dings.swing.helperui.ListIDModel;
import net.vanosten.dings.uiif.IEntriesSelectionView;

public class EntriesSelectionView extends AViewWithButtons implements IEntriesSelectionView {
	private final static long serialVersionUID = 1L;

	private JButton applyB;
	private ListID unitsLi, categoriesLi, typesLi;
	private JComboBox statusCB;
	private JComboBox minScoreCB;
	private JComboBox maxScoreCB;
	private JTextField lastLearnedBeforeTF;
	private JLabel statusL, lastLearnedBeforeL, minScoreL, maxScoreL, unitsL, categoriesL, typesL;

	/**
	 * Empty constructor the EntriesSelectionView
	 */
	public EntriesSelectionView(ComponentOrientation aComponentOrientation) {
		super(Toolbox.getInstance().getLocalizedString("viewtitle.entries_selection"), aComponentOrientation);
		initializeGUI();
		this.setGUIOrientation();
	} //END public EntriesSelectionView(ComponentOrientation)

	private final void initComponents() {
		statusCB = new JComboBox();
		statusCB.addItem("All");
		statusCB.addItem("Only up-to-date entries");
		statusCB.addItem("Only entries that need editing");
		statusL = new JLabel("Status:");
		statusL.setDisplayedMnemonic(("S").charAt(0));
		statusL.setLabelFor(statusCB);

		lastLearnedBeforeTF = new JTextField();
		lastLearnedBeforeTF.setText(Integer.toString(0));
		lastLearnedBeforeTF.setColumns(5);
		lastLearnedBeforeL = new JLabel("Last learned before (days):");
		lastLearnedBeforeL.setDisplayedMnemonic(("A").charAt(0));
		lastLearnedBeforeL.setLabelFor(lastLearnedBeforeTF);

		minScoreCB = new JComboBox();
		maxScoreCB = new JComboBox();
		for (int i = Entry.SCORE_MIN; i <= Entry.SCORE_MAX; i++) {
			minScoreCB.addItem(Integer.toString(i));
			maxScoreCB.addItem(Integer.toString(i));
		}
		minScoreCB.setSelectedIndex(0);
		maxScoreCB.setSelectedIndex(maxScoreCB.getItemCount() -1);
		minScoreL = new JLabel("Minimal Score:");
		minScoreL.setDisplayedMnemonic(("i").charAt(0));
		minScoreL.setLabelFor(minScoreCB);
		maxScoreL = new JLabel("Maximal Score:");
		maxScoreL.setDisplayedMnemonic(("x").charAt(0));
		maxScoreL.setLabelFor(maxScoreCB);

		//units
		unitsLi = new ListID(4, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		unitsL = new JLabel(Toolbox.getInstance().getInfoPointer().getUnitLabel() + ":");
		//unitsL.setDisplayedMnemonic(("U").charAt(0));
		unitsL.setLabelFor(unitsLi);

		//categories
		categoriesLi = new ListID(4, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		categoriesL = new JLabel(Toolbox.getInstance().getInfoPointer().getCategoryLabel() + ":");
		//categoriesL.setDisplayedMnemonic(("C").charAt(0));
		categoriesL.setLabelFor(categoriesL);

		//EntryTypes
		typesLi = new ListID(4, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		typesL = new JLabel("Selected Entry Types:");
		typesL.setDisplayedMnemonic(("Y").charAt(0));
		typesL.setLabelFor(typesLi);
	} //END private final void initComponents()

	//implements A ViewWithButtons
	protected void initializeMainP() {
		initComponents();

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		mainP = new JPanel();
		mainP.setLayout(gbl);

		Insets vghz = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		Insets vghg = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_G, 0, 0);
		Insets vght = new Insets(DingsSwingConstants.SP_V_G, DingsSwingConstants.SP_H_T, 0, 0);
		Insets vzhg = new Insets(0, DingsSwingConstants.SP_H_G, 0, 0);

		//----status
		gbc.anchor = GridBagConstraints.LINE_END;
		gbl.setConstraints(statusL, gbc);
		mainP.add(statusL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vzhg;
		gbl.setConstraints(statusCB, gbc);
		mainP.add(statusCB);
		//----last learned before
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghz;
		gbl.setConstraints(lastLearnedBeforeL, gbc);
		mainP.add(lastLearnedBeforeL);
		//-----
		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(lastLearnedBeforeTF, gbc);
		mainP.add(lastLearnedBeforeTF);
		//----score
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vghz;
		gbl.setConstraints(minScoreL, gbc);
		mainP.add(minScoreL);
		//----
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(minScoreCB, gbc);
		mainP.add(minScoreCB);
		//----
		gbc.gridx = 2;
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = vght;
		gbl.setConstraints(maxScoreL, gbc);
		mainP.add(maxScoreL);
		//----
		gbc.gridx = 3;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(maxScoreCB, gbc);
		mainP.add(maxScoreCB);
		//----units
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.insets = vghz;
		gbl.setConstraints(unitsL, gbc);
		mainP.add(unitsL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(unitsLi, gbc);
		mainP.add(unitsLi);
		//----categories
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.insets = vghz;
		gbl.setConstraints(categoriesL, gbc);
		mainP.add(categoriesL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(categoriesLi, gbc);
		mainP.add(categoriesLi);
		//----entry types
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.insets = vghz;
		gbl.setConstraints(typesL, gbc);
		mainP.add(typesL);
		//----
		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = vghg;
		gbl.setConstraints(typesLi, gbc);
		mainP.add(typesLi);
	} //END protected void initializeMainP()

	//implements AViewWithButtons
	protected final void initializeButtonP() {
		buttonsP = new JPanel();
		buttonsP.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));

		buttonsP.add(applyB);
	} //END protected final void initializeButtonP()

	//implements AViewWithButtons
	protected final void initButtonComponents() {
		applyB = new JButton(Toolbox.getInstance().getLocalizedString("label.button.apply_selection")
				, DingsSwingConstants.createImageIcon(DingsSwingConstants.IMG_APPLY_BTN, "FIXME"));
		applyB.setMnemonic(Toolbox.getInstance().getLocalizedString("mnemonic.button.apply_selection").charAt(0));
		applyB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onApply();
			}
		});
	} //END protected final void initButtonComponents()

	//overrides AViewWithButtons
	public final boolean init(IAppEventHandler aHandler) {
		boolean foo = super.init(aHandler);
		AppEvent ape = new AppEvent(AppEvent.EventType.DATA_EVENT);
		ape.setMessage(MessageConstants.Message.D_ENTRIES_SELECTION_REFRESH);
		controller.handleAppEvent(ape);
		return foo;
	} //END public final boolean init(IAppEventHandler)

	private final void onApply() {
		AppEvent ape = new AppEvent(AppEvent.EventType.DATA_EVENT);
		ape.setMessage(MessageConstants.Message.D_ENTRIES_SELECTION_APPLY);
		controller.handleAppEvent(ape);
	} //END private final void onApply()

	//implements IEntriesSelectionView
	public void setUnitsList(String[][] theUnits) {
		unitsLi.setListIDModel(new ListIDModel(theUnits));
		unitsLi.setSelectionInterval(0,theUnits.length-1);
	} //END public void setUnitsList(String[])

	//implements IEntriesSelectionView
	public void setCategoriesList(String[][] theCategories) {
		categoriesLi.setListIDModel(new ListIDModel(theCategories));
		categoriesLi.setSelectionInterval(0,theCategories.length-1);
	} //END public void setCategoriesList(String[])

	//implements IEntriesSelectionView
	public void setTypesList(String[][] theEntryTypes) {
		typesLi.setListIDModel(new ListIDModel(theEntryTypes));
		typesLi.setSelectionInterval(0,theEntryTypes.length-1);
	} //END public void setTypesList(String[])

	//implements IEntriesSelectionView
	public int getStatusChoice() {
		return statusCB.getSelectedIndex();
	} //END public int getStatusChoice()

	//implements IEntriesSelectionView
	public void setStatusChoice(int aStatusChoice) {
		statusCB.setSelectedIndex(aStatusChoice);
	} //END public void setStatusChoice(int)

	//implements IEntriesSelectionView
	public String[] getUnitsChoice() {
		return unitsLi.getSelectedIDs();
	} //END public String[] getUnitsChoice()

	//implements IEntriesSelectionView
	public void setUnitsChoice(String[] theIds) {
		unitsLi.clearSelection();
		unitsLi.setSelectedIDs(theIds);
	} //END public void setUnitsChoice(String[])

	//implements IEntriesSelectionView
	public String[] getCategoriesChoice() {
		return categoriesLi.getSelectedIDs();
	} //END public String[] geCategoriesChoice()

	//implements IEntriesSelectionView
	public void setCategoriesChoice(String[] theIds) {
		categoriesLi.clearSelection();
		categoriesLi.setSelectedIDs(theIds);
	} //END public void setCategoriesChoice(String[])

	//implements IEntriesSelectionView
	public String[] getTypesChoice() {
		return typesLi.getSelectedIDs();
	} //END public String[] getTypesChoice()

	//implements IEntriesSelectionView
	public void setTypesChoice(String[] theIds) {
		typesLi.clearSelection();
		typesLi.setSelectedIDs(theIds);
	} //END public void setTypesChoice(String[])

	//implements IEntriesSelectionView
	public String getLastLearnedBefore() {
		return lastLearnedBeforeTF.getText();
	} //END public int getLastLearnedBefore()

	//implements IEntriesSelectionView
	public void setLastLearnedBefore(int aNumberOfDays) {
		lastLearnedBeforeTF.setText(Integer.toString(aNumberOfDays));
	} //END public void setLastLearnedBefore(int)

	//implements IEntriesSelectionView
	public int[] getMinMaxScore() {
		int[] scores = new int[2];
		scores[0] = minScoreCB.getSelectedIndex() + 1;
		scores[1] = maxScoreCB.getSelectedIndex() + 1;
		return scores;
	} //END public int[] getMinMaxScore()

	//implements IEntriesSelectionView
	public void setMinMaxScore(int[] theScores) {
		minScoreCB.setSelectedIndex(theScores[0] - 1);
		maxScoreCB.setSelectedIndex(theScores[1] - 1);
	} //END public void setMinMaxScore(int[])
} //END public class EntriesSelectionView extends JPanel
