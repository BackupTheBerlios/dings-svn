/*
 * PreferencesEditView.java
 * :tabSize=4:indentSize=4:noTabs=false:
 *
 * Copyright (C) 2002, 2003 Rick Gruber (rick@vanosten.net)
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

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.vanosten.dings.consts.MessageConstants;
import net.vanosten.dings.consts.Constants;
import net.vanosten.dings.event.AppEvent;
import net.vanosten.dings.event.IAppEventHandler;
import net.vanosten.dings.uiif.IPreferencesEditView;

public class PreferencesEditView extends JDialog implements IPreferencesEditView {

	private IAppEventHandler controller;
	private JPanel mainP;
	private JPanel leftP;
	private JPanel rightP;
	private JPanel buttonP;
	private JList choiceLi; //the list of preferences to choose from in the left panel
	private JLabel titleL; //the title on the right for the preferences
	private JLabel descriptionL; //the description of the preference to be displayed on the right side
	private final static String LAF = "Look and Feel";
	private final static String LH = "Learn Hints";
	private final static String FILEENC = "File Encoding";
	private final static String LOGGING = "Logging";
	private final static String SELECTION_UPDATE = "Selection Update";
	private final static String STATS = "Statistics";

	private JPanel learnHintP;
	private JSlider learnHintCoverPercentSL;
	private JSlider learnHintFlashTimeSL;
	private JCheckBox learnHintShuffleByWordCB;

	private JPanel fileEncodingP;
	private JComboBox fileEncodingCB;

	private JPanel loggingP;
	private JCheckBox loggingEnabledCB, loggingToFileCB;
	
	private JPanel lookAndFeelP;
	private JCheckBox lookAndFeelCB;
	
	private JPanel selectionUpdateP;
	private JCheckBox selUpdInstEditingCB;
	private JCheckBox selUpdInstLearningCB;

	private JPanel statsP;
	private JCheckBox statsOnQuitCB;

	/**
	 * Indicates whether the gui value is changed programmatically
	 * or by the user. This is important, as otherwise the programmatic
	 * changes would again call the method (endless loop).
	 * The value is true when programmatically updated.
	 * TODO: this is the same as in AEditView -> reuse?
	*/
	protected boolean isUpdating = false;

	public PreferencesEditView(JFrame parentF, ComponentOrientation aComponentOrientation) {
		super(parentF, "Preferences", false);
		super.setComponentOrientation(aComponentOrientation);
		initializeGUI();
		this.applyComponentOrientation(aComponentOrientation);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onClose();
			}
		});
		choiceLi.setSelectedIndex(0);
	} //END public PreferencesEditView()

	private void initializeGUI() {
		getContentPane().setLayout(new BorderLayout());

		//the choice area
		initializeLeftPanel();
		getContentPane().add(leftP, BorderLayout.BEFORE_LINE_BEGINS);
		
		//the preferences area on the right
		initializeRightPanel();
		getContentPane().add(rightP, BorderLayout.CENTER);

		//the button area
		initializeButtonPanel();
		getContentPane().add(buttonP, BorderLayout.SOUTH);
	} //END private void initializeGUI()
	
	private void initializeLeftPanel() {
		leftP = new JPanel();
		EmptyBorder border = new EmptyBorder(DingsSwingConstants.SP_D_TOP, DingsSwingConstants.SP_D_LEFT, 0, 0);
		leftP.setBorder(border);
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		leftP.setLayout(gbl);
		
		JLabel categoryL = new JLabel();
		categoryL.setText("Categories:");
		String[] choices = {FILEENC, LAF, LH, LOGGING, SELECTION_UPDATE, STATS};
		choiceLi = new JList(choices);
		choiceLi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		choiceLi.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				switchPreferencePanel();
			}
		});
		JScrollPane listScroller = new JScrollPane(choiceLi);
		//listScroller.setPreferredSize(new Dimension(250, 80));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbl.setConstraints(categoryL, gbc);
		leftP.add(categoryL);
		//----
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(listScroller, gbc);
		leftP.add(listScroller);
	} //END private void initializeLeftPanel()
	
	private void initializeRightPanel() {
		rightP = new JPanel();
		EmptyBorder border = new EmptyBorder(
			DingsSwingConstants.SP_D_TOP
			, DingsSwingConstants.SP_H_G
			, 0
			, DingsSwingConstants.SP_D_RIGHT
		);
		rightP.setBorder(border);
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		rightP.setLayout(gbl);
		
		//the title area
		JPanel titleP = new JPanel();
		titleP.setBackground(DingsSwingConstants.COLOR_PRIMARY_1);
		Border titleBo = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		titleP.setBorder(titleBo);
		GridBagLayout tgbl = new GridBagLayout();
		GridBagConstraints tgbc = new GridBagConstraints();
		titleP.setLayout(tgbl);
		titleL = new JLabel();
		titleL.setFont(Constants.TITLE_ONE_FONT);
		tgbc.weightx = 1.0;
		tgbc.fill = GridBagConstraints.HORIZONTAL;
		tgbc.anchor = GridBagConstraints.LINE_START;
		tgbl.setConstraints(titleL, tgbc);
		titleP.add(titleL);
				
		//the description area
		descriptionL = new JLabel();
		
		//the area with preferences
		mainP = new JPanel();
		mainP.setLayout(new CardLayout());
		initializeFileEncodingPanel();
		mainP.add(fileEncodingP, FILEENC);
		initializeLookAndFeelPanel();
		mainP.add(lookAndFeelP, LAF);
		initializeLearnHintPanel();
		mainP.add(learnHintP, LH);
		initializeLoggingPanel();
		mainP.add(loggingP, LOGGING);
		initializeSelectionUpdatePanel();
		mainP.add(selectionUpdateP, SELECTION_UPDATE);
		initializeStatsOnQuitPanel();
		mainP.add(statsP, STATS);
		
		//layout
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(titleP, gbc);
		rightP.add(titleP);
		//----
		gbc.gridy = 1;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0,0,0);
		gbl.setConstraints(descriptionL, gbc);
		rightP.add(descriptionL);
		//----
		gbc.gridy = 2;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_T, 0,0,0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbl.setConstraints(mainP, gbc);
		rightP.add(mainP);
	} //END private void initializeRightPanel()
	
	private void initializeButtonPanel() {
		buttonP = new JPanel();
		EmptyBorder border = new EmptyBorder(
			DingsSwingConstants.SP_V_COM
			, DingsSwingConstants.SP_D_LEFT
			, DingsSwingConstants.SP_D_BUTTOM
			, DingsSwingConstants.SP_D_RIGHT
		);
		buttonP.setBorder(border);
		buttonP.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton b = new JButton("Close", Constants.createImageIcon(Constants.IMG_CLOSE_24, "FIXME"));
		b.setMnemonic("C".charAt(0));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onClose();
			}
		});
		buttonP.add(b); //FlowLayout will center button
	} //END private void initializeButtonPanel()

	/**
	 * Shows a new view of preferences.
	 */
	private void switchPreferencePanel() {		
		//set the title and description
		switch(choiceLi.getSelectedIndex()) {
			case 1: //look and feel
				titleL.setText("Look And Feel");
				descriptionL.setText(
						"<html>Determines whether the look and feel should mimic the operating system or show the Java " +
						"cross platform \"Metal\" theme.</html>"
				);
				break;
			case 2: //learn hints
				titleL.setText("Learning Hints");
				descriptionL.setText(
					"<html>The settings influence the way learning hints are presented when learning one by one.</html>"
				);
				break;
			case 3: //logging
				titleL.setText("Logging");
				descriptionL.setText(
					"<html>Logging in this application can be turned on and off You can also choose to log to the Console or to a File."
					+ " Under normal conditions you can turn loggin off."
					+ " If there is a problem with the application it would help me a lot if you would set the logging to Enabled and"
					+ " and send me the output together with a bug report, so I can find and correct the error.</html>"
				);
				break;
			case 4: //selection update
				titleL.setText("Selection Updates");
				descriptionL.setText(
					"<html>These settings influence the way the current selection of entries is applied to updates."
					+ " If you tick the options off, then a change in an Entry (e.g. the score, the category) is"
					+ " checked immediately against the current selection criterias and the Entry is eventually removed"
					+ " from the current selection of criterias. Otherwise the current selection is only changed"
					+ " when the selection criteria are changed and / or the \"Apply Selection\" button is pressed.</html>"
				);
				break;
			case 5: //statistics on quit
				titleL.setText("Statistics");
				descriptionL.setText(
					"<html>These setting influence the way statistics about learning are saved."
					+ " If you tick the option off, then the learnings statistics will be saved each time you"
					+ " quit the application (and accept saving the changes)."
					+ " Alternatively or additionally you can save learning statistics from the "
					+ " statistics view by pressing a button.</html>"
				);
				break;
			default: //file encoding
				titleL.setText("File Encoding");
				descriptionL.setText(
					"<html><p>Description of the encoding formats:"
					+ "<ul>"
					+ "<li>US-ASCII: Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode character set</li>"
					+ "<li>ISO-8859-1: ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1</li>"
					+ "<li>UTF-8: Eight-bit Unicode Transformation Format</li>"
					+ "<li>UTF-16BE: Sixteen-bit Unicode Transformation Format, big-endian byte order</li>"
					+ "<li>UTF-16LE: Sixteen-bit Unicode Transformation Format, little-endian byte order</li>"
					+ "<li>UTF-16: Sixteen-bit Unicode Transformation Format, byte order specified by a mandatory initial byte-order mark (either order accepted on input, big-endian used on output)</li>"
					+ "</ul></p></html>"
				);
				break;
		}
		//show the panel
		CardLayout cl = (CardLayout)(mainP.getLayout());
		cl.show(mainP, (String)choiceLi.getSelectedValue());
	} //END private void switchPreferencePanel()

	private void initializeFileEncodingPanel() {
		fileEncodingP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		fileEncodingP.setLayout(gbl);
		
		JLabel fileEncodingL = new JLabel("File encoding:");
		fileEncodingCB = new JComboBox(FILE_ENCODINGS);
		fileEncodingCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChange();
			}
		});
		fileEncodingL.setLabelFor(fileEncodingCB);
			
		//layout
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbl.setConstraints(fileEncodingL, gbc);
		fileEncodingP.add(fileEncodingL);
		//----
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(0, DingsSwingConstants.SP_H_G, 0, 0);
		gbl.setConstraints(fileEncodingCB, gbc);
		fileEncodingP.add(fileEncodingCB);
	} //END private void initializeFileEncodingPanel()

	private void initializeLookAndFeelPanel() {
		lookAndFeelP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		lookAndFeelP.setLayout(gbl);
		
		lookAndFeelCB = new JCheckBox("Use system specific look and feel");
		lookAndFeelCB.setMnemonic(("U").charAt(0));
		lookAndFeelCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChange();
			}
		});
		
		//layout
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbl.setConstraints(lookAndFeelCB, gbc);
		lookAndFeelP.add(lookAndFeelCB);
	} //END private void initializeLookAndFeelPanel()
	
	private void initializeLearnHintPanel() {
		learnHintP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		learnHintP.setLayout(gbl);

		JLabel coverL = new JLabel("Vertical coverage in %:");
		learnHintCoverPercentSL = new JSlider(SwingConstants.HORIZONTAL, LH_COVER_PERCENT_MIN, LH_COVER_PERCENT_MAX, LH_COVER_PERCENT_DEFAULT);
		learnHintCoverPercentSL.setMajorTickSpacing(20);
		learnHintCoverPercentSL.setMinorTickSpacing(5);
		learnHintCoverPercentSL.setSnapToTicks(true);
		learnHintCoverPercentSL.setPaintTicks(true);
		learnHintCoverPercentSL.setPaintLabels(true);
		learnHintCoverPercentSL.putClientProperty("JSlider.isFilled", Boolean.TRUE);
		learnHintCoverPercentSL.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					onChange();
				}
			}
		});
		coverL.setLabelFor(learnHintCoverPercentSL);

		JLabel visibleL = new JLabel("Visible time for flash in milli seconds:");
		learnHintFlashTimeSL = new JSlider(SwingConstants.HORIZONTAL, LH_FLASH_TIME_MIN, LH_FLASH_TIME_MAX, LH_FLASH_TIME_DEFAULT);
		learnHintFlashTimeSL.setMajorTickSpacing(1000);
		learnHintFlashTimeSL.setMinorTickSpacing(100);
		learnHintFlashTimeSL.setSnapToTicks(true);
		learnHintFlashTimeSL.setPaintTicks(true);
		learnHintFlashTimeSL.setPaintLabels(true);
		learnHintFlashTimeSL.putClientProperty("JSlider.isFilled", Boolean.TRUE);
		learnHintFlashTimeSL.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					onChange();
				}
			}
		});
		visibleL.setLabelFor(learnHintFlashTimeSL);
		
		learnHintShuffleByWordCB = new JCheckBox("Shuffle Word by Word");
		learnHintShuffleByWordCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChange();
			}
		});
		learnHintShuffleByWordCB.setMnemonic(("W").charAt(0));
			
		//layout
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbl.setConstraints(coverL, gbc);
		learnHintP.add(coverL);
		//----
		gbc.gridy = 1;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(learnHintCoverPercentSL, gbc);
		learnHintP.add(learnHintCoverPercentSL);
		//----
		gbc.gridy = 2;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_T, 0, 0, 0);
		gbl.setConstraints(visibleL, gbc);
		learnHintP.add(visibleL);
		//----
		gbc.gridy = 3;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_G, 0, 0, 0);
		gbl.setConstraints(learnHintFlashTimeSL, gbc);
		learnHintP.add(learnHintFlashTimeSL);
		//----
		gbc.gridy = 4;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_T, 0, 0, 0);
		gbl.setConstraints(learnHintShuffleByWordCB, gbc);
		learnHintP.add(learnHintShuffleByWordCB);
	} //END private void initializeLearnHintPanel()

	private void initializeLoggingPanel() {
		loggingP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		loggingP.setLayout(gbl);
		
		loggingEnabledCB = new JCheckBox("Enable Logging");
		loggingEnabledCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChange();
			}
		});
		loggingEnabledCB.setMnemonic(("L").charAt(0));
		
		loggingToFileCB = new JCheckBox("Log to file \"" + Constants.LOGGING_FILE_NAME + "\" in your home directory");
		loggingToFileCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChange();
			}
		});
		loggingToFileCB.setMnemonic(("F").charAt(0));
		
		//layout
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbl.setConstraints(loggingEnabledCB, gbc);
		loggingP.add(loggingEnabledCB);
		//----
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_C, 0, 0, 0);
		gbl.setConstraints(loggingToFileCB, gbc);
		loggingP.add(loggingToFileCB);
	} //END private void initializeLoggingPanel()

	private void initializeSelectionUpdatePanel() {
		selectionUpdateP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		selectionUpdateP.setLayout(gbl);
		
		selUpdInstEditingCB = new JCheckBox("Update selection instantly when editing");
		selUpdInstEditingCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChange();
			}
		});
		selUpdInstEditingCB.setMnemonic(("e").charAt(0));
		
		selUpdInstLearningCB = new JCheckBox("Update selection instantly when learning (score change)");
		selUpdInstLearningCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChange();
			}
		});
		selUpdInstLearningCB.setMnemonic(("l").charAt(0));
		
		//layout
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbl.setConstraints(selUpdInstEditingCB, gbc);
		selectionUpdateP.add(selUpdInstEditingCB);
		//----
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(DingsSwingConstants.SP_V_C, 0, 0, 0);
		gbl.setConstraints(selUpdInstLearningCB, gbc);
		selectionUpdateP.add(selUpdInstLearningCB);
	} //END private void initializeSelectionUpdatePanel()

	private void initializeStatsOnQuitPanel() {
		statsP = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		statsP.setLayout(gbl);
		
		statsOnQuitCB = new JCheckBox("Save learning statistics on quit");
		statsOnQuitCB.setMnemonic(("S").charAt(0));
		statsOnQuitCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				onChange();
			}
		});
		
		//layout
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbl.setConstraints(statsOnQuitCB, gbc);
		statsP.add(statsOnQuitCB);
	} //END private void initializeStatsOnQuitPanel()

	//Implements IView
	public boolean init(IAppEventHandler aController) {
		this.controller = aController;
		if (null == controller) return false;
		onRevert();
		return true;
	} //END public boolean init(IAppEventHandler)

	private void onRevert() {
		AppEvent ape = new AppEvent(AppEvent.DATA_EVENT);
		ape.setMessage(MessageConstants.D_PREFERENCES_EDIT_REVERT);
		controller.handleAppEvent(ape);
	} //END private void onRevert()

	private void onChange() {
		if (!isUpdating) {
			//set logging to file enabled based on logging enabled
			loggingToFileCB.setEnabled(loggingEnabledCB.isSelected());
			//check for real changes
			AppEvent ape = new AppEvent(AppEvent.DATA_EVENT);
			ape.setMessage(MessageConstants.D_PREFERENCES_EDIT_APPLY);
			controller.handleAppEvent(ape);
		}
	} //END private void onChange()

	private void onClose() {
		//store the application's size
		AppEvent ape = new AppEvent(AppEvent.STATUS_EVENT);
		ape.setMessage(MessageConstants.S_SAVE_DIALOG_SIZE);
		controller.handleAppEvent(ape);

		//close the dialog
		setVisible(false);
	} //END private void onClose()

	//implements IPreferencesEditView
	public void setFileEncoding(String anEncoding) {
		isUpdating = true;
		fileEncodingCB.setSelectedItem(anEncoding);
		isUpdating = false;
	} //END public void setFileEncoding(String)

	//implements IPreferencesEditView
	public String getFileEncoding() {
		return (String)fileEncodingCB.getSelectedItem();
	} //END public String getFileEncoding()

	//implements IPreferencesEditView
	public void setLearnHintCoverPercent(int thePercentage) {
		isUpdating = true;
		learnHintCoverPercentSL.setValue(thePercentage);
		isUpdating = false;
	} //public void setLearnHintCoverPercent(int)

	//implements IPreferencesEditView
	public int getLearnHintCoverPercent() {
		return learnHintCoverPercentSL.getValue();
	} //END public int getLearnHintCoverPercent()

	//implements IPreferencesEditView
	public void setLearnHintFlashTime(int theFlashTime) {
		isUpdating = true;
		learnHintFlashTimeSL.setValue(theFlashTime);
		isUpdating = false;
	} //public void setLearnHintFlashTime(int)

	//implements IPreferencesEditView
	public int getLearnHintFlashTime() {
		return learnHintFlashTimeSL.getValue();
	} //END public int getLearnHintFlashTime()
	
	//implements IPreferencesEditView
	public void setLearnHintShuffleByWord(String isSelected) {
		isUpdating = true;
		learnHintShuffleByWordCB.setSelected(Boolean.valueOf(isSelected).booleanValue());
		isUpdating = false;		
	} //END public void setLearnHintShuffleByWord(String)
	
	//implements IPreferencesEditView
	public String getLearnHintShuffleByWord() {
		return Boolean.toString(learnHintShuffleByWordCB.isSelected());
	} //END public String getLearnHintShuffleByWord()

	//implements IPreferencesEditView
	public void setLoggingEnabled(String isSelected) {
		isUpdating = true;
		boolean selected = Boolean.valueOf(isSelected).booleanValue();
		loggingEnabledCB.setSelected(selected);
		loggingToFileCB.setEnabled(selected);
		isUpdating = false;
	} //END public void setLoggingEnabled(String)

	//implements IPreferencesEditView
	public String getLoggingEnabled() {
		return Boolean.toString(loggingEnabledCB.isSelected());
	} //END public String getLoggingEnabled()

	//implements IPreferencesEditView
	public void setLoggingToFile(String isSelected) {
		isUpdating = true;
		loggingToFileCB.setSelected(Boolean.valueOf(isSelected).booleanValue());
		isUpdating = false;
	} //END public void setLoggingToFile(String)

	//implements IPreferencesEditView
	public String getLoggingToFile() {
		return Boolean.toString(loggingToFileCB.isSelected());
	} //END public String getLoggingToFile()

	//implements IPreferencesEditView
	public void setDialogSize(int aWidth, int aHeight) {
		this.setSize(aWidth, aHeight);
	} //END public void setDialogSize(int, int)

	//implements IPreferncesEditView
	public int[] getDialogSize() {
		int[] returnSize = new int[2];
		Dimension d = this.getSize();
		returnSize[0] = d.width;
		returnSize[1] = d.height;
		return returnSize;
	} //END public int[] getDialogSize()
	
	//implements IPreferencesEditView
	public void setSystemLookAndFeel(boolean isSystem) {
		isUpdating = true;
		lookAndFeelCB.setSelected(isSystem);
		isUpdating = false;
	} //END public void setSystemLookAndFeel(boolean)
	
	//implements IPreferencesEditView
	public boolean isSystemLookAndFeel() {
		return lookAndFeelCB.isSelected();
	} //END public boolean isSystemLookAndFeel()
	
	//implements IPreferencesEditView
	public boolean isSelUpdInstEditing() {
		return selUpdInstEditingCB.isSelected();
	} //END public boolean isSelUpdInstEdit()
	
	//implements IPreferencesEditView	
	public boolean isSelUpdInstLearning() {
		return selUpdInstLearningCB.isSelected();
	} //END public boolean isSelUpdInstLearning()
	
	//implements IPreferencesEditView
	public void setSelUpdInst(boolean editingEnabled, boolean learningEnabled) {
		isUpdating = true;
		selUpdInstEditingCB.setSelected(editingEnabled);
		selUpdInstLearningCB.setSelected(learningEnabled);
		isUpdating = false;
	} //END public void setSelUpdInst(boolean, boolean)
	
	//implements IPreferencesEditView
	public void setStatsOnQuit(boolean isOn) {
		isUpdating = true;
		statsOnQuitCB.setSelected(isOn);
		isUpdating = false;
	} //END public void setStatsOnQuit(boolean)

	//implements IPreferencesEditView
	public boolean isStatsOnQuit() {
		return statsOnQuitCB.isSelected();
	} //END public boolean isStatsOnQuit()

} //END public class PreferencesEditView extends JPanel implements IPreferencesEditView
